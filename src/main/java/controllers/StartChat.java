package controllers;

import com.google.gson.Gson;

import io.javalin.Javalin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;
import models.ChatList;
import models.ChatRoom;
import models.Genre;
import models.Login;
import models.Message;
import models.User;
import models.Song;
import models.SqLite;
import org.eclipse.jetty.websocket.api.Session;


public final class StartChat {

  /**
   * Private constructor that prevents utility class instantiation.
   */
  private StartChat() {
  }

  /**
   * Static method that called private constructor for testing.
   * @return StartChat object
   */
  public static StartChat make() {
    return new StartChat();
  }

  /**
   *  Set listening port.
   */
  private static final int PORT_NUMBER = 8080;

  /**
   * Create Javalin instance.
   */
  private static Javalin app;

  /**
   * Create database instance.
   */
  private static SqLite db = new SqLite();

  /**
   * get current database
   * @return database SqLite
   */
  public static SqLite getDb() {
	  return db;
  }

  /**
   * Create ChatList.
   */
  private static ChatList chatlist = new ChatList();

  /** Main method of the application.
   * @param args Command line arguments
   */
  public static void main(final String[] args) {
    db.start();
    db.commit();
    chatlist = db.update();
    db.commit();
    // Add to DB only when app starts for the first time
    if (chatlist.size() == 0) {
      Map<String, ChatRoom> map = new HashMap<>();
      Genre[] genres = Genre.class.getEnumConstants();
      for (Genre genre : genres) {
        ChatRoom chatroom = new ChatRoom();
        chatroom.setParticipant(new HashMap<String, User>());
        chatroom.setChat(new ArrayList<Message>());
        chatroom.setPlaylist(new ArrayList<Song>());
        chatroom.setGenre(genre);
      	chatroom.setLink("/joinroom/" + genre.getGenre());
      	map.put(genre.getGenre(), chatroom);
      	db.insertChatRoom(chatroom.getGenre(), chatroom.getLink(), "playlist-" + genre.getGenre());
      	db.commit();
      }
      chatlist.setChatrooms(map);
    }
    
    ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
    exec.scheduleAtFixedRate(new Runnable() {
      public void run() {
        chatlist = db.update();
        chatlist.refreshChatList(db);
        for (ChatRoom chatroom : chatlist.getChatrooms().values()) {
          sendChatRoomToAllParticipants(chatroom.getGenre().getGenre(), new Gson().toJson(chatroom));
        }
        
      }
    }, 0, 30, TimeUnit.SECONDS);

    
    app = Javalin.create(config -> {
      config.addStaticFiles("/public");
      //config.addStaticFiles("/resources");
    }).start(PORT_NUMBER);
  
    //authentication
    
    app.before(ctx -> {
        
      String sessionId = (String)ctx.sessionAttribute("sessionId");
      
      //if /auth and code present skip rest
      if(ctx.url().contains("/process_auth") && ctx.queryParam("code") != null && sessionId != null)
        return;
      
      if(sessionId == null) {
        sessionId = UUID.randomUUID().toString();
        ctx.sessionAttribute("sessionId", sessionId);
      }
      
      if(db.getUserBySessionId(sessionId).getUsername() == null) {
        db.insertSession("" + System.currentTimeMillis(), sessionId);
        db.commit();
        ctx.redirect(Login.getSpotifyAuthUrl()); 
      }
    });
    
    //handle spotify authentication
    app.get("/process_auth", ctx -> {
      
      String sessionId = (String) ctx.sessionAttribute("sessionId");
      
      Map<String, String> response = Login.getSpotifyTokenFromCode(ctx.queryParam("code"));
      
      User user = new User(response.get("access_token"), db);
      user.setSpotifyRefreshToken(response.get("refresh_token"), true);
      user.setSessionId(sessionId, true);
      
      ctx.redirect("/chatrooms");
    });
    
    // Front page
    app.get("/", ctx -> {
      ctx.redirect("/chatrooms");
    });
	
    // Test Echo Server
    app.post("/echo", ctx -> {
      ctx.result(ctx.body());
    });

    // Send Chatroom List
    app.get("/chatrooms", ctx -> {
      chatlist.setChatrooms(db.getAllChatRooms());
      ctx.result(new Gson().toJson(chatlist));
    });

    app.post("/joinroom/:genre", ctx -> {
      if (Genre.isValidGenre(ctx.pathParam("genre").toUpperCase())) {
        Genre genre = Genre.valueOf(ctx.pathParam("genre").toUpperCase());
        String username = ctx.formParam("username");
        ctx.redirect("/" + genre + "?user=" + username);
        // add to DB only if participant is new
        Map<String, User> participants = chatlist.getChatroomByGenre(genre).getParticipant();
        if (!participants.containsKey(username)) {
          // only perform database operation if user is new
          User user = db.getUserByUsername(username);
          db.insertParticipant(genre, username, user.getSpotifyToken(), user.getSpotifyRefreshToken(), user.getSessionId());
          db.commit();
          chatlist = db.update();
          db.commit();
        }
      } else {
        ctx.result("Invalid Room");
      }
    });

    // user chatroom view
    app.get("/:genre", ctx -> {
      if (Genre.isValidGenre(ctx.pathParam("genre").toUpperCase())) {
        Genre genre = Genre.valueOf(ctx.pathParam("genre").toUpperCase());
        // TODO use username to check user's permission to enter room
        String username = ctx.queryParam("user");
        ChatRoom chatroom = chatlist.getChatroomByGenre(genre);
        //chatlist.refreshChatList(db);
        sendChatRoomToAllParticipants(genre.getGenre(), new Gson().toJson(chatroom));
        ctx.result(new Gson().toJson(chatroom));
      } else {
        ctx.result("Invalid Room");
      }
    });

    app.post("/send/:username", ctx -> {
      // TODO implement endpoint
      // add message to chatroom DB
      // update participant views
      String username = ctx.pathParam("username");
      String text = ctx.formParam("text");
      Message message = new Message();
      message.setUsername(username);
      message.setMessage(text);
    });

    app.post("/share/:username", ctx -> {
      // TODO implement endpoint
      // add song to DB
      // update participant views
      String username = ctx.pathParam("username");
      String spotifySong = ctx.formParam("song");
      Song song = new Song();
      song.setUsername(username);
      song.setSong(spotifySong);
    });

    app.delete("/leaveroom/:genre", ctx -> {
      // TODO implement endpoint
      Genre genre = Genre.valueOf(ctx.pathParam("genre").toUpperCase());
      String username = ctx.formParam("username");
      ChatRoom chatroom = chatlist.getChatroomByGenre(genre);
      if (chatroom.getParticipant().containsKey(username)) {
        db.removeParticipant(genre, username);
        db.commit();
        chatlist = db.update();
        db.commit();
        chatroom = chatlist.getChatroomByGenre(genre);
        sendChatRoomToAllParticipants(genre.getGenre(), new Gson().toJson(chatroom));
        ctx.result(new Gson().toJson(chatroom));
      } else {
        ctx.result("You are not in the room");
      }
    });

    // Web sockets - DO NOT DELETE or CHANGE
    app.ws("/gameboard", new UiWebSocket());
  }

  /** Send message to all players.
   * @param gameBoardJson Gameboard JSON
   * @throws IOException Websocket message send IO Exception
   */
  private static void sendChatRoomToAllParticipants(final String genre, final String chatRoomJson) {
    Queue<Session> sessions = UiWebSocket.getSessions();
    Map<Session, String> sessionGenre = UiWebSocket.getGenreMap();
    // check if refreshChatlist update song data
    //System.out.println(genre + ": " + chatRoomJson);
    for (Session sessionPlayer : sessions) {
      // TODO: Need extra check for participant in the room
      if (!sessionGenre.get(sessionPlayer).equals(genre)) {
        continue;
      }
      try {
        sessionPlayer.getRemote().sendString(chatRoomJson);
      } catch (IOException e) {
        // Add logger here
      }
    }
  }

  /**
   * Stop the application.
   */
  public static void stop() {
    db.close();
    app.stop();
  }
}
