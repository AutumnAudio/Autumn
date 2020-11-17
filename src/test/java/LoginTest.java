import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.Test;

import models.Login;

public class LoginTest {
  String refreshToken = "AQC4PDMZlGf7HXG5OZV82LDcnjLyzSCv74HGw4pL8QrsSyvVYMYlkErQ6N5nu_99J_l4E9ScIq7cW5knr4mFBm6b3sHMEclSk9c4F1-TAKffO8ds2JgJj9uOC00-LcqTS1E";

  @Test
  public void getSpotifyAuthUrlTest() {
    String url = Login.getSpotifyAuthUrl();
    assertEquals("https://accounts.spotify.com/authorize?response_type=code&client_id=17b185367b7d45a8b8cb068eda7787cf&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fprocess_auth&scope=user-read-email+user-read-recently-played+user-read-currently-playing+playlist-modify-public+playlist-modify-private", url);
  }

  @Test
  public void getSpotifyTokenFromCodeTest() {
    Map<String, String> map = Login.getSpotifyTokenFromCode("123");
    assertEquals(2, map.size());
  }

  @Test
  public void refreshSpotifyTokenTest() {
    String token = Login.refreshSpotifyToken(refreshToken);
    assertNotNull(token);
  }

  @Test
  public void getEmailFromSpotifyTokenTest() {
    String token = Login.refreshSpotifyToken(refreshToken);
    assertEquals("cherrychu_120@hotmail.com", Login.getEmailFromSpotifyToken(token));
  }
}