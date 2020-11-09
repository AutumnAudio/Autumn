import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Time;
import java.time.LocalTime;

import models.SqLite;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


@TestMethodOrder(OrderAnnotation.class) 
public class SqLiteTest {

  static SqLite db = new SqLite();

  @Test
  public void testInsertUser1() {
    db.start();
    db.commit();
    db.clear();
    db.commit();
    db.insertUser("cherry", "password", "spotify_token");
    db.commit();
    assertEquals("cherry", db.getUserByName("cherry").getUsername());
    db.close();
  }

  @Test
  public void testInsertChatRoom() {
    db.start();
    db.commit();
    db.clear();
    db.commit();
    db.insertChatRoom("Jazz", "/jazz-links", "jazz-playlist");
    db.insertChatRoom("Blues", "/blues-links", "blues-playlist");
    db.commit();
    assertEquals(2, db.getChatRooms().size());
    db.close();
  }

  @Test
  public void testInsertParticipant() {
    db.start();
    db.commit();
    db.clear();
    db.commit();
    db.insertParticipant("Jazz", "cherry");
    db.commit();
    assertEquals("cherry", db.getChatRoomParticipant("Jazz").get(0).getUsername());
    db.close();
  }

  @Test
  public void testInsertMessage() {
    db.start();
    db.commit();
    db.clear();
    db.commit();
    db.insertMessage("cherry", Time.valueOf(LocalTime.now()), "Jazz", "Jazz is the best");
    db.commit();
    assertEquals("Jazz is the best", db.getChatRoomChat("Jazz").get(0).getMessage());
    db.close();
  }

  @Test
  public void testInsertSong() {
    db.start();
    db.commit();
    db.clear();
    db.commit();
    db.insertSong("cherry", Time.valueOf(LocalTime.now()), "Blues", "blues-song");
    db.commit();
    assertEquals("blues-song", db.getChatRoomPlaylist("Blues").get(0).getSong());
    db.close();
  }

/*
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
*/
}