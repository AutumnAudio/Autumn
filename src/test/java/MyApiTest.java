import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.PlayHistory;

import models.MyApi;

public class MyApiTest {
  MyApi api = new MyApi();
  
  String refreshToken = SpotifyAccount.getRefreshToken();
  String token = api.refreshSpotifyToken(refreshToken);

  @Test
  public void recentlyPlayedTest() {
    SpotifyApi api = new SpotifyApi.Builder()
            .setAccessToken(token)
            .build();
    MyApi myApi = new MyApi();
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
    MyApi myApi = new MyApi();
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
    MyApi myApi = new MyApi();
    myApi.setApi(api);
    try {
    	String ret = myApi.addSong("spotify:track:4jAIqgrPjKLTY9Gbez25Qb");
    	assertNull(ret);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }

  // ------------------------- GetSpotifyTokenFromCode ------------------------- //
  @Test
  public void getSpotifyTokenFromCodeTestOK() {
    Map<String, String> map = api.getSpotifyTokenFromCode("1");
    assertEquals(2, map.size());
  }

  @Test
  public void getSpotifyTokenFromCodeTestNullCode() {
    assertNull(api.getSpotifyTokenFromCode(null));
  }

  @Test
  public void getSpotifyTokenFromCodeTestEmptyCode() {
    assertNull(api.getSpotifyTokenFromCode(""));
  }  

  // ------------------------- RefreshSpotifyToken ------------------------- //
  @Test
  public void refreshSpotifyTokenTestOK() {
    String token = api.refreshSpotifyToken(refreshToken);
    assertNotNull(token);
  }

  @Test
  public void refreshSpotifyTokenTestNullToken() {
    String token = api.refreshSpotifyToken(null);
    assertNull(token);
  }

  @Test
  public void refreshSpotifyTokenTestEmptyToken() {
    String token = api.refreshSpotifyToken("");
    assertNull(token);
  }

  // ----------------------- GetEmailFromSpotifyToken ------------------------ //
  @Test
  public void getEmailFromSpotifyTokenTestOK() {
    String token = api.refreshSpotifyToken(refreshToken);
    assertEquals("cherrychu_120@hotmail.com", api.getEmailFromSpotifyToken(token));
  }
  
  @Test
  public void getEmailFromSpotifyTokenTestNullToken() {
    assertNull(api.getEmailFromSpotifyToken(null));
  }

  @Test
  public void getEmailFromSpotifyTokenTestEmptyToken() {
    assertNull(api.getEmailFromSpotifyToken(""));
  }
}
