import models.Login;
import models.Song;
import models.SqLite;
import models.User;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

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
  
  String spotifyToken = "BQCRMQoge3BUYgzoM05a0KdxcLcHENyDn90zgE4pwq5Cl-ykDV1sZIVmbWOdsubDbyvmLLNApaWbSrmadtCK4XRMw4q0Og0YLOh-6aDylQBnWRlJHRxIb7-0-09kyYq7XjKJEzrnqG0sz9J6U3OuJw5Al2G21kVh64D0Xyt0zuwAg7KAF_3SJRhfdtTlAgw0E6SkgJsZYM4kvI0EGhWv0qseUfrE8Im53S9jf7U4WpQ";
  String spotifyRefreshToken = "AQCDFdBbzda1H17Vcv3FMIFR5hvFEZjrNGhkCnjDXpDHHohCvp_vX2cvXyg0XpJ2SC69M9A4EOMCUVCgBjcUYMJYZZsbcUysqzf-kUrDev0LE0Wxi283o2l3k13JdXUiShc";
  
  SqLite db = new SqLite();
  
  @BeforeEach
  public void beforeEach() {
    db.start();
    db.commit();
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
    String token = Login.refreshSpotifyToken(spotifyRefreshToken);
    User user = new User(token, db);
    assertNotNull(user.getSpotifyToken());
    assertEquals(1, db.getUserCount("cherrychu_120@hotmail.com"));
  }

  @Test
  public void testRefreshSpotifyToken() {
    User user = new User(spotifyToken, db);
    user.setSpotifyRefreshTokenDb(spotifyRefreshToken);
    user.refreshSpotifyToken();
    assertNotEquals(spotifyToken, user.getSpotifyToken());
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
