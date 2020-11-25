import models.Login;
import models.Song;
import models.SqLite;
import models.User;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import com.wrapper.spotify.SpotifyApi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


@TestMethodOrder(OrderAnnotation.class) 
public class UserTest {
  
  String refreshToken = SpotifyAccount.getRefreshToken();
  String token = Login.refreshSpotifyToken(refreshToken);
  SpotifyApi api = new SpotifyApi.Builder()
          .setAccessToken(Login.refreshSpotifyToken(refreshToken))
          .build();
  
  SqLite db = new SqLite();
  
  @BeforeEach
  public void beforeEach() {
    db.start();
    db.commit();
  }

  @Test
  public void testResfreshRecentlyPlayed() {
	User user = new User(api);
	user.setUsername("test");
	user.setSpotifyToken(token);
	user.setSpotifyRefreshToken(refreshToken);
	user.refreshRecentlyPlayed();
	assertEquals(10, user.getRecentlyPlayed().length);
	for (Song song : user.getRecentlyPlayed()) {
	  assertNotNull(song.getName());
	}
  }

  @Test
  public void testResfreshCurrentlyPlaying() {
	User user = new User(api);
	user.setUsername("test");
	user.setSpotifyToken(token);
	user.setSpotifyRefreshToken(refreshToken);
	user.refreshCurrentlyPlaying();
	Song song = new Song();
	user.setCurrentTrack(song);
	assertEquals(song, user.getCurrentTrack());
	user.refreshCurrentlyPlaying();
	assertNotEquals(song, user.getCurrentTrack());
  }

  @Test
  public void testUserConstructor() {
    String token = Login.refreshSpotifyToken(refreshToken);
    User user = new User(token, db);
    assertNotNull(user.getSpotifyToken());
    assertEquals(1, db.getUserCount("cherrychu_120@hotmail.com"));
  }

  @Test
  public void testRefreshSpotifyToken() {
    User user = new User(token, db);
    user.setSpotifyRefreshTokenDb(refreshToken);
    user.refreshSpotifyToken();
    assertNotEquals(token, user.getSpotifyToken());
  }

  @Test
  public void testSetSessionId() {
    User user = new User();
    db.insertUserWithSession("cherry", "session");
    db.commit();
    assertEquals("session", db.getUserByName("cherry").getSessionId());
    user.setDb(db);
    user.setUsername("cherry");
    user.setSessionIdDb("new_session");
    assertEquals("new_session", db.getUserByName("cherry").getSessionId());
  }

  @Test
  public void testSetSpotifyToken() {
    User user = new User();
    db.insertUserWithToken("cherry", "token");
    db.commit();
    assertEquals("token", db.getUserByName("cherry").getSpotifyToken());
    user.setDb(db);
    user.setUsername("cherry");
    user.setSpotifyTokenDb("new_token");
    assertEquals("new_token", db.getUserByName("cherry").getSpotifyToken());
  }

  @Test
  public void testSetSpotifyRefreshToken() {
    User user = new User();
    db.insertUserWithToken("cherry", "token");
    db.commit();
    assertNull(db.getUserByName("cherry").getSpotifyRefreshToken());
    user.setDb(db);
    user.setUsername("cherry");
    user.setSpotifyRefreshTokenDb("new_refresh_token");
    assertEquals("new_refresh_token", db.getUserByName("cherry").getSpotifyRefreshToken());
  }

  @AfterEach
  public void afterEach() {
    db.clear();
    db.commit();
    db.close();
  }
}
