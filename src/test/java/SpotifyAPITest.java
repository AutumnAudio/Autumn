import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.PlayHistory;

import models.Login;
import models.SpotifyAPI;

public class SpotifyAPITest {
  SpotifyAPI api = new SpotifyAPI();
  
  String refreshToken = SpotifyAccount.getRefreshToken();
  String token = api.refreshSpotifyToken(refreshToken);

  @Test
  public void recentlyPlayedTest() {
    SpotifyApi api = new SpotifyApi.Builder()
            .setAccessToken(token)
            .build();
    SpotifyAPI myApi = new SpotifyAPI();
    myApi.setApi(api);
    try {
    	PlayHistory[] playHistory = myApi.recentlyPlayed();
		assertEquals(10, playHistory.length);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }

  @Test
  public void currentlyPlayedTest() {
    SpotifyApi api = new SpotifyApi.Builder()
            .setAccessToken(token)
            .build();
    SpotifyAPI myApi = new SpotifyAPI();
    myApi.setApi(api);
    try {
    	myApi.currentlyPlaying();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }

  @Test
  public void addSongTest() {
    SpotifyApi api = new SpotifyApi.Builder()
            .setAccessToken(token)
            .build();
    SpotifyAPI myApi = new SpotifyAPI();
    myApi.setApi(api);
    try {
    	String ret = myApi.addSong("spotify:track:4jAIqgrPjKLTY9Gbez25Qb");
    	assertNull(ret);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
}
