import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Time;
import java.time.LocalTime;

import models.Genre;
import models.SqLite;

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
    db.insertUser("mary", "password", "spotify_token");
    db.commit();
    assertEquals("mary", db.getUserByName("mary").getUsername());
    db.close();
  }

  @Test
  public void testInsertChatRoom() {
    db.start();
    db.commit();
    db.clear();
    db.commit();
    db.insertChatRoom(Genre.JAZZ, "/jazz-links", "jazz-playlist");
    db.insertChatRoom(Genre.BLUES, "/blues-links", "blues-playlist");
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
    db.insertParticipant(Genre.JAZZ, "mary");
    db.commit();
    assertEquals("mary", db.getChatRoomParticipant(Genre.JAZZ).get(0).getUsername());
    db.close();
  }

  @Test
  public void testInsertMessage() {
    db.start();
    db.commit();
    db.clear();
    db.commit();
    db.insertMessage("mary", Time.valueOf(LocalTime.now()), Genre.JAZZ, "Jazz is the best");
    db.commit();
    assertEquals("Jazz is the best", db.getChatRoomChat(Genre.JAZZ).get(0).getMessage());
    db.close();
  }

  @Test
  public void testInsertSong() {
    db.start();
    db.commit();
    db.clear();
    db.commit();
    db.insertSong("mary", Time.valueOf(LocalTime.now()), Genre.BLUES, "blues-song");
    db.commit();
    assertEquals("blues-song", db.getChatRoomPlaylist(Genre.BLUES).get(0).getSong());
    db.close();
  }
}