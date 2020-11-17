import models.Login;
import models.Song;
import models.SqLite;
import models.User;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


@TestMethodOrder(OrderAnnotation.class) 
public class UserTest {
  
  String spotifyToken = "BQCRMQoge3BUYgzoM05a0KdxcLcHENyDn90zgE4pwq5Cl-ykDV1sZIVmbWOdsubDbyvmLLNApaWbSrmadtCK4XRMw4q0Og0YLOh-6aDylQBnWRlJHRxIb7-0-09kyYq7XjKJEzrnqG0sz9J6U3OuJw5Al2G21kVh64D0Xyt0zuwAg7KAF_3SJRhfdtTlAgw0E6SkgJsZYM4kvI0EGhWv0qseUfrE8Im53S9jf7U4WpQ";
  String spotifyRefreshToken = "AQC4PDMZlGf7HXG5OZV82LDcnjLyzSCv74HGw4pL8QrsSyvVYMYlkErQ6N5nu_99J_l4E9ScIq7cW5knr4mFBm6b3sHMEclSk9c4F1-TAKffO8ds2JgJj9uOC00-LcqTS1E";
  
  @BeforeEach
  public void beforeEach() {	  
  }

  @Test
  public void testResfreshRecentlyPlayed() {
	User user = new User();
	user.setUsername("test");
	user.setSpotifyToken(spotifyToken);
	user.setSpotifyRefreshToken(spotifyRefreshToken);
	user.refreshRecentlyPlayed();
	assertEquals(10, user.getRecentlyPlayed().length);
	for (Song song : user.getRecentlyPlayed()) {
	  assertNotNull(song.getName());
	}
  }

  @Test
  public void testResfreshCurrentlyPlaying() {
	User user = new User();
	user.setUsername("test");
	user.setSpotifyToken(spotifyToken);
	user.setSpotifyRefreshToken(spotifyRefreshToken);
	user.refreshCurrentlyPlaying();
	Song song = new Song();
	user.setCurrentTrack(song);
	assertEquals(song, user.getCurrentTrack());
	user.refreshCurrentlyPlaying();
	assertNotEquals(song, user.getCurrentTrack());
  }

  @Test
  public void testUserConstructor() {
    SqLite db = new SqLite();
    db.start();
    db.commit();
    String token = Login.refreshSpotifyToken(spotifyRefreshToken);
    User user = new User(token, db);
    assertNotNull(user.getSpotifyToken());
    assertEquals(1, db.getUserCount("cherrychu_120@hotmail.com"));
    db.clear();
    db.close();
  }

  @Test
  public void testRefreshSpotifyToken() {
    SqLite db = new SqLite();
    db.start();
    db.commit();
    User user = new User(spotifyToken, db);
    user.setSpotifyRefreshToken(spotifyRefreshToken, true);
    user.refreshSpotifyToken();
    assertNotEquals(spotifyToken, user.getSpotifyToken());
    db.clear();
    db.close();
  }

  @Test
  public void testSetSessionId() {
    User user = new User();
    SqLite db = new SqLite();
    db.start();
    db.commit();
    user.setDb(db);
    user.setUsername("cherry");
    user.setSessionId("new_session", true);
    assertEquals("new_session", db.getUserByName("cherry").getSessionId());
    db.clear();
    db.close();
  }

  @Test
  public void testSetSpotifyToken() {
    User user = new User();
    SqLite db = new SqLite();
    db.start();
    db.commit();
    user.setDb(db);
    user.setUsername("cherry");
    user.setSpotifyToken("new_token", true);
    assertEquals("new_token", db.getUserByName("cherry").getSpotifyToken());
    db.clear();
    db.close();
  }

  @Test
  public void testSetSpotifyRefreshToken() {
    User user = new User();
    SqLite db = new SqLite();
    db.start();
    db.commit();
    user.setDb(db);
    user.setUsername("cherry");
    user.setSpotifyRefreshToken("new_refresh_token", true);
    assertEquals("new_refresh_token", db.getUserByName("cherry").getSpotifyRefreshToken());
    db.clear();
    db.close();
  }

  @AfterEach
  public void afterEach() { 
  }
}
