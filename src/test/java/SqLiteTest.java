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
  
  String spotifyToken = "BQCRMQoge3BUYgzoM05a0KdxcLcHENyDn90zgE4pwq5Cl-ykDV1sZIVmbWOdsubDbyvmLLNApaWbSrmadtCK4XRMw4q0Og0YLOh-6aDylQBnWRlJHRxIb7-0-09kyYq7XjKJEzrnqG0sz9J6U3OuJw5Al2G21kVh64D0Xyt0zuwAg7KAF_3SJRhfdtTlAgw0E6SkgJsZYM4kvI0EGhWv0qseUfrE8Im53S9jf7U4WpQ";
  
  @BeforeEach
  public void beforeEach() {
    db.start();
    db.commit();
    db.clear();
    db.commit();	  
  }

  @Test
  public void testInsertUser1() {
    db.insertUserWithToken("mary", "spotify_token");
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
    db.insertParticipant(Genre.JAZZ, "mary", "", "");
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