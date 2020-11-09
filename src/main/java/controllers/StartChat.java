package controllers;

import com.google.gson.Gson;
import io.javalin.Javalin;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;

import models.ChatList;
import models.ChatRoom;
import models.Genre;
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
   *  Set number of rooms/genres.
   */
  private static final int GENRES = 1;

  /**
   * Create Javalin instance.
   */
  private static Javalin app;

  /**
   * Create database instance.
   */
  private static SqLite db = new SqLite();

  /**
   * Create ChatRoom instance.
   */
  private static ChatRoom chatroom = new ChatRoom();

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
    app = Javalin.create(config -> {
      config.addStaticFiles("/public");
    }).start(PORT_NUMBER);

    // Test Echo Server
    app.post("/", ctx -> {
      ctx.result(ctx.body());
    });

    // Test Echo Server
    app.post("/echo", ctx -> {
      ctx.result(ctx.body());
    });

    // Send Chatroom List
    app.get("/chatrooms", ctx -> {
      ctx.result(new Gson().toJson(chatlist));
    });

    app.post("/registration", ctx -> {
      // TODO implement endpoint
    });

    app.post("/login", ctx -> {
      // TODO implement endpoint
    });

    app.post("/joinroom/:genre", ctx -> {
      // TODO implement endpoint
      // get chatroom data from DB
      // update participant views
      String genre = ctx.pathParam("genre");
      String userId = ctx.formParam("userId");
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
      // get chatroom data from DB
      // update participant views
      String genre = ctx.pathParam("genre");
      int userId = Integer.parseInt(ctx.formParam("userId"));
    });

    app.after(ctx -> {
      db.commit();
    });

    // Web sockets - DO NOT DELETE or CHANGE
    app.ws("/chatroom", new UiWebSocket());
  }

  /** Send message to all players.
   * @param gameBoardJson Gameboard JSON
   * @throws IOException Websocket message send IO Exception
   */
  private static void sendChatRoomToAllParticipants(final String chatRoomJson) {
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
   * Stop the application.
   */
  public static void stop() {
    db.close();
    app.stop();
  }
}
