import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Time;
import java.time.LocalTime;

import models.ChatList;
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
  public void testInsertUserToken() {
    db.insertUserWithToken("mary", "spotify_token");
    db.commit();
    assertEquals("mary", db.getUserByName("mary").getUsername());
  }

  @Test
  public void testInsertUserSession() {
    db.insertUserWithSession("mary", "test_id");
    db.commit();
    assertEquals("mary", db.getUserBySessionId("test_id").getUsername());
  }

  @Test
  public void testUpdateUserAttribute() {
    db.insertUserWithToken("mary", "token");
    db.commit();
    db.updateUserAttribute("spotify_token", "new_token", "mary");
    db.commit();
    assertEquals("new_token", db.getUserByName("mary").getSpotifyToken());
  }

  @Test
  public void testGetUserCount() {
    assertEquals(0, db.getUserCount("mary"));
    db.insertUserWithToken("mary", "spotify_token");
    db.commit();
    assertEquals(1, db.getUserCount("mary"));
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
    db.insertParticipant(Genre.JAZZ, "mary", "", "", "");
    db.commit();
    assertEquals(true, db.getChatRoomParticipant(Genre.JAZZ).containsKey("mary"));
  }

  @Test
  public void testRemoveParticipant() {
    db.insertParticipant(Genre.JAZZ, "mary", "", "", "");
    db.commit();
    assertEquals(true, db.getChatRoomParticipant(Genre.JAZZ).containsKey("mary"));
    db.removeParticipant(Genre.JAZZ, "mary");
    db.commit();
    assertEquals(false, db.getChatRoomParticipant(Genre.JAZZ).containsKey("mary"));
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

  @Test
  public void testInsertSession() {
    db.insertSession("0920", "session1");
    db.commit();
    assertEquals("session1", db.getLatestSession());
  }

  @Test
  public void testGetLatestSession() {
    db.insertSession("0920", "session2");
    db.insertSession("1020", "session3");
    db.insertSession("0820", "session1");
    db.commit();
    assertEquals("session3", db.getLatestSession());
  }

  @Test
  public void testUpdate() {
    ChatList chatlist = db.update();
    assertEquals(0, chatlist.size());
    db.insertChatRoom(Genre.JAZZ, "/jazz-links", "jazz-playlist");
    db.commit();
    chatlist = db.update();
    assertEquals(1, chatlist.size());
  }

  @Test
  public void testConnect() {
    ChatList chatlist = db.update();
    assertEquals(0, chatlist.size());
    db.insertChatRoom(Genre.JAZZ, "/jazz-links", "jazz-playlist");
    db.commit();
    chatlist = db.update();
    assertEquals(1, chatlist.size());
    SqLite db2 = new SqLite();
    db2.connect();
    ChatList chatlist2 = db2.update();
    assertEquals(1, chatlist2.size());
    db2.close();
  }

  @Test
  public void testInsertUserwithGenre() {
    db.insertUserwithGenre("mary", "blues");
    db.commit();
    assertEquals("blues", db.getGenreUser("mary"));
  }

  @Test
  public void testRemoveUserGenre() {
    db.insertUserwithGenre("mary", "blues");
    db.commit();
    assertEquals("blues", db.getGenreUser("mary"));
    db.removeUserGenre("mary");
    db.commit();
    assertNull(db.getGenreUser("mary"));
  }

  @AfterEach
  public void afterEach() {
    db.clear();
    db.commit();
    db.close();	  
  }
}