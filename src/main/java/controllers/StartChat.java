package controllers;

import com.google.gson.Gson;
import io.javalin.Javalin;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import models.ChatList;
import models.ChatRoom;
import models.Genre;
import models.Login;
import models.Message;
import models.Song;
import models.SqLite;
import models.User;
import org.eclipse.jetty.websocket.api.Session;
import java.util.concurrent.ConcurrentHashMap;


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
   * Set listening port.
   */
  private static final int PORT_NUMBER = 8080;

  /**
   * Set time interval (seconds) for refreshChatList().
   */
  private static final int INTERVAL = 30;

  /**
   * Create Javalin instance.
   */
  private static Javalin app;

  /**
   * Create database instance.
   */
  private static SqLite db = new SqLite();

  /**
   * Map username to genre.
   */
  private static Map<String, Genre> userGenre = new ConcurrentHashMap<>();

  /**
   * get current database.
   * @return database SqLite
   */
  public static SqLite getDb() {
    return db;
  }

  /**
   * set current database.
   * @param database SqLite
   */
  public static void setDb(final SqLite database) {
    db = database;
  }

  /**
   * Create ChatList.
   */
  private static ChatList chatlist = new ChatList();

  /**
   * get chatlist.
   * @return chatlist ChatList
   */
  public static ChatList getChatlist() {
    return chatlist;
  }

  /**
   * set chatlist.
   * @param list ChatList
   */
  public static void setChatlist(final ChatList list) {
    chatlist = list;
  }

  /**
   * Initialize Chatrooms.
   */
  public static void initializeChatlist() {
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
      db.insertChatRoom(chatroom.getGenre(), chatroom.getLink(),
                                "playlist-" + genre.getGenre());
      db.commit();
    }
    chatlist.setChatrooms(map);
  }

  private static void refreshSongDataRepeatly() {
    ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
    exec.scheduleAtFixedRate(new Runnable() {
      public void run() {
        chatlist = db.update();
        chatlist.refreshChatList();
        for (ChatRoom chatroom : chatlist.getChatrooms().values()) {
          String genre = chatroom.getGenre().getGenre();
          sendChatRoomToAllParticipants(genre,
                    new Gson().toJson(chatroom));
        }
      }
    }, 0, INTERVAL, TimeUnit.SECONDS);
  }

  /** Main method of the application.
   * @param args Command line arguments
   */
  public static void main(final String[] args) {
    db.start();
    chatlist = db.update();
    db.commit();
    if (chatlist.size() == 0) {
      initializeChatlist();
    }
    refreshSongDataRepeatly();

    app = Javalin.create(config -> {
      config.addStaticFiles("/public");
    }).start(PORT_NUMBER);

    //authentication
    app.before(ctx -> {
      String sessionId = (String) ctx.sessionAttribute("sessionId");
      //if /auth and code present skip rest
      if (ctx.url().contains("/process_auth")
             && ctx.queryParam("code") != null && sessionId != null) {
        return;
      }
      if (sessionId == null) {
        sessionId = UUID.randomUUID().toString();
        ctx.sessionAttribute("sessionId", sessionId);
      }
      if (db.getUserBySessionId(sessionId).getUsername() == null) {
        db.insertSession("" + System.currentTimeMillis(), sessionId);
        db.commit();
        ctx.redirect(Login.getSpotifyAuthUrl());
      }
    });

    //handle spotify authentication
    app.get("/process_auth", ctx -> {
      String sessionId = (String) ctx.sessionAttribute("sessionId");
      Map<String, String> response =
         Login.getSpotifyTokenFromCode(ctx.queryParam("code"));
      User user = new User(response.get("access_token"), db);
      user.setSpotifyRefreshTokenDb(response.get("refresh_token"));
      user.setSessionIdDb(sessionId);
      ctx.redirect("/home");
    });

    // Front page
    app.get("/", ctx -> {
      ctx.redirect("/chatrooms");
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
        Map<String, User> participants =
              chatlist.getChatroomByGenre(genre).getParticipant();
//        userGenre.put(username, genre);
        if (!participants.containsKey(username)) {
          User user = db.getUserByName(username);
          db.insertParticipant(genre, username, user.getSpotifyToken(),
              user.getSpotifyRefreshToken(), user.getSessionId());
          db.commit();
          chatlist = db.update();
          db.commit();
        }
      } else {
        ctx.result("Invalid Room");
      }
    });
    
//     user chatroom view
    app.get("/genre/:genre", ctx -> {
      if (Genre.isValidGenre(ctx.pathParam("genre").toUpperCase())) {
        Genre genre = Genre.valueOf(ctx.pathParam("genre").toUpperCase());
        // TODO use username to check user's permission to enter room
        String username = ctx.queryParam("user");
        ChatRoom chatroom = chatlist.getChatroomByGenre(genre);
        sendChatRoomToAllParticipants(genre.getGenre(),
                     new Gson().toJson(chatroom));
        ctx.result(new Gson().toJson(chatroom));
      } else {
        ctx.result("Invalid Room");
      }
    });
    app.get("/home", ctx -> {
    	ctx.redirect("index.html");
    });
    app.post("/send/:username", ctx -> {
      String username = ctx.pathParam("username");
      String text = ctx.formParam("text");
      Message message = new Message();
      message.setUsername(username);
      message.setMessage(text);
      Genre genre = userGenre.get(username);
      if (genre != null) {
        System.out.println(genre);
        ChatRoom chatroom = chatlist.getChatroomByGenre(genre);
        chatroom.addMessage(message);
        sendChatRoomToAllParticipants(genre.getGenre(),
                new Gson().toJson(chatroom));
        ctx.redirect("/" + genre + "?user=" + username);
      } else {
        ctx.result("User not in any chatroom");
      }
    });

    app.post("/share/:username", ctx -> {
      // TODO implement endpoint
      // add song to DB
      // update participant views
      /*
      String username = ctx.pathParam("username");
      String spotifySong = ctx.formParam("song");
      Song song = new Song();
      song.setUsername(username);
      song.setSong(spotifySong);
      */
    });

    app.delete("/leaveroom/:genre", ctx -> {
      Genre genre = Genre.valueOf(ctx.pathParam("genre").toUpperCase());
      String username = ctx.formParam("username");
      ChatRoom chatroom = chatlist.getChatroomByGenre(genre);
      if (userGenre.get(username) != null) {
        userGenre.remove(username);
      }
      if (chatroom.getParticipant().containsKey(username)) {
        db.removeParticipant(genre, username);
        db.commit();
        chatlist = db.update();
        db.commit();
        chatroom = chatlist.getChatroomByGenre(genre);
        sendChatRoomToAllParticipants(genre.getGenre(),
              new Gson().toJson(chatroom));
        ctx.result(new Gson().toJson(chatroom));
      } else {
        ctx.result("You are not in the room");
      }
    });

    // Web sockets - DO NOT DELETE or CHANGE
    app.ws("/chatroom", new UiWebSocket());
  }

  /**
   * Send chatroom to all participants.
   * @param genre String
   * @param chatRoomJson String
   */
  private static void sendChatRoomToAllParticipants(final String genre,
        final String chatRoomJson) {
    Queue<Session> sessions = UiWebSocket.getSessions();
    // check if refreshChatlist update song data
    //System.out.println(genre + ": " + chatRoomJson);
    for (Session sessionPlayer : sessions) {
      // TODO Need extra check for participant in the room
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
