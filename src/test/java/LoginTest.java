import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.Test;

import models.Login;
import models.MyApi;

public class LoginTest {

  @Test
  public void getSpotifyAuthUrlTest() {
    String url = Login.getSpotifyAuthUrl();
    assertEquals("https://accounts.spotify.com/authorize?response_type=code&client_id=17b185367b7d45a8b8cb068eda7787cf&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fprocess_auth&scope=user-read-email+user-read-recently-played+user-read-currently-playing+user-modify-playback-state+playlist-modify-public+playlist-modify-private", url);
  }
}
