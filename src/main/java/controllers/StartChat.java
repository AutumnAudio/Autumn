package controllers;

import com.google.gson.Gson;
import io.javalin.Javalin;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
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
import models.Thread;
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
   * Set listening port.
   */
  private static final int PORT_NUMBER = 8080;

  /**
   * Set time interval (seconds) for refreshChatList().
   */
  private static final int INTERVAL = 1;

  /**
   * Create Javalin instance.
   */
  private static Javalin app;

  /**
   * Create database instance.
   */
  private static SqLite db = new SqLite();

  /**
   * get current database.
   * @return database SqLite
   */
  public static SqLite getDb() {
    return db;
  }

  private static int getHerokuAssignedPort() {
    String herokuPort = System.getenv("PORT");
    if (herokuPort != null) {
      return Integer.parseInt(herokuPort);
    }
    return 7000;
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
    }
    chatlist.setChatrooms(map);
  }

  /**
   * Start a thread that read DB periodically.
   */
  private static ScheduledFuture<?> refreshDataInterval;

  /**
   * refresh song data in separate thread.
   */
  public static void refreshSongDataRepeatly() {
    ScheduledFuture<?> running = refreshDataInterval;
    if (running != null && !running.isCancelled()) {
      return;
    }
    ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
    Thread thread = new Thread(db);
    refreshDataInterval = exec.scheduleAtFixedRate(
            thread, 0, INTERVAL, TimeUnit.SECONDS);
  }

  /** Main method of the application.
   * @param args Command line arguments
   */
  public static void main(final String[] args) {
    db.start();
    chatlist = db.update();
    if (chatlist.getChatrooms().size() == 0) {
      initializeChatlist();
    }
    app = Javalin.create(config -> {
      config.addStaticFiles("/public");
    }).start(getHerokuAssignedPort());
    app.get("/", ctx -> {
      ctx.redirect("/home");
    });
    app.get("/home", ctx -> {
      ctx.redirect("index.html");
    });
    app.get("/lobby", ctx -> {
      ctx.redirect("index.html?place=lobby");
    });

    app.get("/auth", ctx -> {
      String sessionId = (String) ctx.sessionAttribute("sessionId");
      if (db.getUserBySessionId(sessionId).getUsername() == null) {
        db.insertSession("" + System.currentTimeMillis(), sessionId);
        System.out.println(Login.getSpotifyAuthUrl());
        ctx.result("{\"error\":\"not authenticated\",\"auth_url\":\""
                + Login.getSpotifyAuthUrl() + "\"}");
      } else {
        ctx.result("{}");
      }
    });

    app.before(ctx -> {
      String sessionId = (String) ctx.sessionAttribute("sessionId");
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
        ctx.result("{\"error\":\"not authenticated\",\"auth_url\":\""
                + Login.getSpotifyAuthUrl() + "\"}");
      }
    });

    app.get("/process_auth", ctx -> {
      String response = db.authenticateUser(ctx.queryParam("code"),
              ctx.sessionAttribute("sessionId"));
      ctx.result(response);
      ctx.redirect("http://localhost:3000"); //TODO change to HOME constant
    });

    app.get("/chatrooms", ctx -> {
      chatlist.setChatrooms(db.getAllChatRooms());
      ctx.result(new Gson().toJson(chatlist));
    });

    app.post("/joinroom/:genre", ctx -> {
      if (Genre.isValidGenre(ctx.pathParam("genre").toUpperCase())) {
        Genre genre = Genre.valueOf(ctx.pathParam("genre").toUpperCase());
        String username = db.getUserBySessionId(
                (String) ctx.sessionAttribute("sessionId")).getUsername();
        chatlist = db.userJoin(genre, username, chatlist);
        chatlist = db.update();
        sendChatRoomToAllParticipants(genre.getGenre(),
              new Gson().toJson(chatlist.getChatroomByGenre(genre)));
        ctx.result("success");
        refreshSongDataRepeatly();
      } else {
        ctx.result("Invalid Room");
      }
    });

    app.get("/chatroom/:genre", ctx -> {
      if (Genre.isValidGenre(ctx.pathParam("genre").toUpperCase())) {
        Genre genre = Genre.valueOf(ctx.pathParam("genre").toUpperCase());
        ChatRoom chatroom = chatlist.getChatroomByGenre(genre);
        ctx.result(new Gson().toJson(chatroom));
      } else {
        ctx.result("Invalid Room");
      }
    });

    app.post("/send", ctx -> {
      String username = db.getUserBySessionId(
                (String) ctx.sessionAttribute("sessionId")).getUsername();
      String text = ctx.formParam("text");
      Message message = db.userSend(username, text, chatlist);
      if (message == null) {
        ctx.result("User not in any chatroom");
      } else {
        sendChatMsgToAllParticipants(message.getGenre().getGenre(),
                new Gson().toJson(message));
        ctx.result("chat sent");
      }
    });

    app.post("/add", ctx -> {
      String uri = ctx.formParam("uri");
      if (uri == null) {
        ctx.result("no song uri");
        return;
      }
      String username = db.getUserBySessionId(
              (String) ctx.sessionAttribute("sessionId")).getUsername();
      User user = db.getUserByName(username);
      user.addToQueue(uri);
      ctx.result("song added to your queue");
    });

    app.post("/share", ctx -> {
      String username = db.getUserBySessionId(
                (String) ctx.sessionAttribute("sessionId")).getUsername();
      User sharer = db.getUserByName(username);
      Message message = sharer.share(chatlist, db);
      if (message == null) {
        ctx.result("no song shared");
      } else {
        sendChatMsgToAllParticipants(db.getGenreUser(username),
                new Gson().toJson(message));
        ctx.result("song shared");
      }
    });

    app.delete("/leaveroom/:genre", ctx -> {
      Genre genre = Genre.valueOf(ctx.pathParam("genre").toUpperCase());
      String username = db.getUserBySessionId(
              (String) ctx.sessionAttribute("sessionId")).getUsername();
      ChatRoom chatroom = chatlist.getChatroomByGenre(genre);
      if (chatroom.getParticipant().containsKey(username)) {
        db.removeUserGenre(username);
        db.removeParticipant(genre, username);
        chatlist = db.update();
        sendChatRoomToAllParticipants(genre.getGenre(),
              new Gson().toJson(chatroom));
        ctx.result(new Gson().toJson(chatroom));
        if (chatlist.getTotalParticipants() == 0) {
          refreshDataInterval.cancel(false);
        }
      } else {
        ctx.result("You are not in the room");
      }
    });

    app.ws("/chatroom", new UiWebSocket());
  }

  /**
   * Send chatroom to all participants.
   * @param genre String
   * @param chatRoomJson String
   */
  public static void sendChatRoomToAllParticipants(final String genre,
        final String chatRoomJson) {
    Queue<Session> sessions = UiWebSocket.getSessions();
    for (Session sessionPlayer : sessions) {
      try {
        sessionPlayer.getRemote().sendString(chatRoomJson);
      } catch (IOException e) {
        // Add logger here
      }
    }
  }

  /**
   * Send message to all participants.
   * @param genre String
   * @param msg String
   */
  public static void sendChatMsgToAllParticipants(
      final String genre, final String msg) {
    Queue<Session> sessions = UiWebSocket.getSessions();
    for (Session sessionPlayer : sessions) {
      try {
        sessionPlayer.getRemote().sendString(msg);
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
