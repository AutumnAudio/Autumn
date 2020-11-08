import static org.junit.jupiter.api.Assertions.assertEquals;

import models.GameBoard;
import models.SqLite;
import org.junit.jupiter.api.Test;


public class SqLiteTest {

  SqLite db = new SqLite();

  @Test
  public void testInsertBoard() {
    db.start();
    db.commit();
    db.clear();
    db.commit();
    db.insertBoard(1, 0, 0, 0, 0);
    db.commit();
    assertEquals(1, db.getBoard(1, 0, 0));
    db.commit();
    db.close();
  }

  @Test
  public void testInsertBoardFail() {
    db.start();
    db.commit();
    db.clear();
    db.commit();
    db.insertBoard(1, 0, 0, 0, 0);
    db.close();
    db.start();
    db.commit();
    assertEquals(0, db.getBoard(1, 0, 0));
    db.close();
  }

  @Test
  public void testInsertPlayer() {
    db.start();
    db.commit();
    db.clear();
    db.commit();
    db.insertPlayer(1, 'X');
    db.commit();
    db.close();
    db.start();
    db.commit();
    assertEquals('X', db.getType(1));
    db.close();
  }

  @Test
  public void testInProgress() {
    db.start();
    db.commit();
    db.clear();
    db.commit();
    db.insertPlayer(1, 'X');
    db.commit();
    db.insertPlayer(2, 'O');
    db.commit();
    db.insertBoard(1, 0, 0, 0, 0);
    db.commit();
    db.insertBoard(2, 0, 1, 0, 0);
    db.commit();
    db.close();
    db.start();
    db.commit();
    GameBoard board = new GameBoard();
    char[][] state = new char[3][3];
    board.setBoardState(state);
    assertEquals(false, board.getGameStarted());
    db.inProgress(board);
    assertEquals(true, board.getGameStarted());
    db.close();
  }

  @Test
  public void testInProgressWinner1() {
    db.start();
    db.commit();
    db.clear();
    db.commit();
    db.insertPlayer(1, 'X');
    db.commit();
    db.insertPlayer(2, 'O');
    db.commit();
    db.insertBoard(1, 0, 0, 1, 0);
    db.commit();
    db.close();
    db.start();
    db.commit();
    GameBoard board = new GameBoard();
    assertEquals(0, board.getWinner());
    db.inProgress(board);
    assertEquals(1, board.getWinner());
    db.close();
  }

  @Test
  public void testInProgressWinner2() {
    db.start();
    db.commit();
    db.clear();
    db.commit();
    db.insertPlayer(1, 'X');
    db.commit();
    db.insertPlayer(2, 'O');
    db.commit();
    db.insertBoard(1, 0, 0, 0, 0);
    db.commit();
    db.insertBoard(2, 1, 0, 2, 0);
    db.commit();
    db.close();
    db.start();
    db.commit();
    GameBoard board = new GameBoard();
    assertEquals(0, board.getWinner());
    db.inProgress(board);
    assertEquals(2, board.getWinner());
    db.close();
  }

  @Test
  public void testInProgressDraw() {
    db.start();
    db.commit();
    db.clear();
    db.commit();
    db.insertPlayer(1, 'X');
    db.commit();
    db.insertPlayer(2, 'O');
    db.commit();
    db.insertBoard(1, 0, 0, 0, 1);
    db.commit();
    db.close();
    db.start();
    db.commit();
    GameBoard board = new GameBoard();
    assertEquals(false, board.getIsDraw());
    db.inProgress(board);
    assertEquals(true, board.getIsDraw());
    db.close();
  }

  @Test
  public void testInProgressOnePlayer() {
    db.start();
    db.commit();
    db.clear();
    db.commit();
    db.insertPlayer(1, 'X');
    assertEquals('\u0000', db.getType(2));
    db.commit();
    db.close();
    db.start();
    db.commit();
    GameBoard board = new GameBoard();
    board.setGameStarted(true);
    db.inProgress(board);
    assertEquals(false, board.getGameStarted());
    db.close();
  }

  @Test
  public void testInProgressNoPlayer() {
    db.start();
    db.commit();
    db.clear();
    db.commit();
    assertEquals('\u0000', db.getType(1));
    assertEquals('\u0000', db.getType(2));
    db.close();
    db.start();
    db.commit();
    GameBoard board = new GameBoard();
    board.setGameStarted(true);
    db.inProgress(board);
    assertEquals(false, board.getGameStarted());
    db.close();
  }

}