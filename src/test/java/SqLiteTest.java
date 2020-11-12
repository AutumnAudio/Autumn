import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Time;
import java.time.LocalTime;

import models.Genre;
import models.SqLite;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


@TestMethodOrder(OrderAnnotation.class) 
public class SqLiteTest {

  static SqLite db = new SqLite();
  
  @BeforeEach
  public void beforeEach() {
    db.start();
    db.commit();
    db.clear();
    db.commit();	  
  }

  @Test
  public void testInsertUser1() {
    db.insertUser("mary", "spotify_token");
    db.commit();
    assertEquals("mary", db.getUserByName("mary").getUsername());
  }

  @Test
  public void testInsertChatRoom() {
    db.insertChatRoom(Genre.JAZZ, "/jazz-links", "jazz-playlist");
    db.insertChatRoom(Genre.BLUES, "/blues-links", "blues-playlist");
    db.commit();
    assertEquals(2, db.getAllChatRooms().size());
  }

  @Test
  public void testInsertParticipant() {
    db.insertParticipant(Genre.JAZZ, "mary");
    db.commit();
    assertEquals(true, db.getChatRoomParticipant(Genre.JAZZ).containsKey("mary"));
  }

  @Test
  public void testInsertMessage() {
    db.insertMessage("mary", Time.valueOf(LocalTime.now()), Genre.JAZZ, "Jazz is the best");
    db.commit();
    assertEquals("Jazz is the best", db.getChatRoomChat(Genre.JAZZ).get(0).getMessage());
  }

  @Test
  public void testInsertSong() {
    db.insertSong("mary", Time.valueOf(LocalTime.now()), Genre.BLUES, "blues-song");
    db.commit();
    assertEquals("blues-song", db.getChatRoomPlaylist(Genre.BLUES).get(0).getSong());
  }

  @AfterEach
  public void afterEach() {
    db.clear();
    db.commit();
    db.close();	  
  }
}