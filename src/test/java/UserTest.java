import models.Song;
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

  @AfterEach
  public void afterEach() { 
  }
}
