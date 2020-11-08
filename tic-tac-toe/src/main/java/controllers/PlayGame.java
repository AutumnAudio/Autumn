package controllers;

import com.google.gson.Gson;
import io.javalin.Javalin;
import java.io.IOException;
import java.util.Queue;
import models.GameBoard;
import models.Message;
import models.Move;
import models.Player;
import models.SqLite;
import org.eclipse.jetty.websocket.api.Session;


public final class PlayGame {

  /**
   * Private constructor that prevents utility class instantiation.
   */
  private PlayGame() {
  }

  /**
   * Static method that called private constructor for testing.
   * @return PlayGame object
   */
  public static PlayGame make() {
    return new PlayGame();
  }

  /**
   *  Set listening port.
   */
  private static final int PORT_NUMBER = 8080;

  /**
   *  Set dimension of board.
   */
  private static final int DIM = 3;

  /**
   * Create Javalin instance.
   */
  private static Javalin app;

  /**
   * Create database instance.
   */
  private static SqLite db = new SqLite();

  /**
   * Create GameBoard instance.
   */
  private static GameBoard board = new GameBoard();

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

    // Send GameBoard
    app.get("/gameboard", ctx -> {
      db.inProgress(board);
      ctx.result(new Gson().toJson(board));
    });

    // Test Echo Server
    app.post("/echo", ctx -> {
      ctx.result(ctx.body());
    });

    app.get("/newgame", ctx -> {
      ctx.redirect("/tictactoe.html");
    });

    app.post("/startgame", ctx -> {
      char[][] state = new char[DIM][DIM];
      board.setGameStarted(false);
      board.setIsDraw(false);
      board.setWinner(0);
      board.setBoardState(state);
      board.setTurn(1);
      Player p1 = new Player();
      p1.setType(ctx.formParam("type").charAt(0));
      p1.setId(1);
      board.setP1(p1);
      board.setP2(null);
      db.clear();
      db.insertPlayer(1, ctx.formParam("type").charAt(0));
      ctx.result(new Gson().toJson(board));
    });

    app.get("/joingame", ctx -> {
      db.inProgress(board);
      Player p2 = new Player();
      board.setP2(p2);
      if (board.getP1().getType() == 'X') {
        p2.setType('O');
        db.insertPlayer(2, 'O');
      } else {
        p2.setType('X');
        db.insertPlayer(2, 'X');
      }
      p2.setId(2);
      ctx.redirect("/tictactoe.html?p=2");
      board.setGameStarted(true);
      sendGameBoardToAllPlayers(new Gson().toJson(board));
    });

    app.post("/move/:playerId", ctx -> {
      db.inProgress(board);
      int playerId = Integer.parseInt(ctx.pathParam("playerId"));
      int x = Integer.parseInt(ctx.formParam("x"));
      int y = Integer.parseInt(ctx.formParam("y"));
      Move move = new Move();
      final Message message = new Message();
      move.setPlayer(board.getPlayerFromId(playerId));
      move.setMoveX(x);
      move.setMoveY(y);
      message.setCode(Integer.parseInt("" + playerId + x + y));
      int winner = 0;
      int draw = 0;
      if (board.isValid(move, message)) {
        board.makeMove(move);
        if (!board.isOver(move)) {
          board.switchTurn();
        } else {
          winner = board.getWinner();
          if (board.getIsDraw()) {
            draw = 1;
          }
        }
        db.insertBoard(playerId, x, y, winner, draw);
      }
      ctx.result(new Gson().toJson(message));
      sendGameBoardToAllPlayers(new Gson().toJson(board));
    });

    app.after(ctx -> {
      db.commit();
    });

    // Web sockets - DO NOT DELETE or CHANGE
    app.ws("/gameboard", new UiWebSocket());
  }

  /** Send message to all players.
   * @param gameBoardJson Gameboard JSON
   * @throws IOException Websocket message send IO Exception
   */
  private static void sendGameBoardToAllPlayers(final String gameBoardJson) {
    Queue<Session> sessions = UiWebSocket.getSessions();
    for (Session sessionPlayer : sessions) {
      try {
        sessionPlayer.getRemote().sendString(gameBoardJson);
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
