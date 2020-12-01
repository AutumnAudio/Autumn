package models;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.hc.core5.http.ParseException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import com.wrapper.spotify.model_objects.specification.PlayHistory;

public class SpotifyAPI {
  
  private SpotifyApi api;

  
  public void setApi(final SpotifyApi newApi) {
    this.api = newApi;
  }

  public SpotifyApi getApi() {
    return this.api;
  }
  
  private static final int LIMIT = 10;
  
  public PlayHistory[] recentlyPlayed() throws ParseException, SpotifyWebApiException, IOException {
    PlayHistory[] playHistory = api
    	    .getCurrentUsersRecentlyPlayedTracks()
            .limit(LIMIT)
            .build()
            .execute()
            .getItems();
    return playHistory;
  }

  public CurrentlyPlaying currentlyPlaying() throws ParseException, SpotifyWebApiException, IOException {
    CurrentlyPlaying currentTrack = api
      .getUsersCurrentlyPlayingTrack()
      .build()
      .execute();
    return currentTrack;
  }

  public String addSong(String uri) throws ParseException, SpotifyWebApiException, IOException {
    String ret = api
          .addItemToUsersPlaybackQueue(uri)
          .build()
          .execute();
    return ret;
  }

  /**
   * Get Spotify token from given code.
   * @param code String
   * @return response Map
   */
  public Map<String, String> getSpotifyTokenFromCode(final String code) {
    //https://mkyong.com/java/how-to-send-http-request-getpost-in-java/
    HttpClient httpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .build();

    // form parameters
    Map<Object, Object> data = new HashMap<>();
    data.put("grant_type", "authorization_code");
    data.put("code", code);
    data.put("redirect_uri", Login.getRedirectURI());

    HttpRequest request = HttpRequest.newBuilder()
            .POST(Login.buildFormDataFromMap(data))
            .uri(URI.create("https://accounts.spotify.com/api/token"))
            .setHeader("User-Agent", "Java 11 HttpClient Bot")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("Authorization", "Basic "
                + Base64.getEncoder().encodeToString((Login.getClientId() + ":"
                + Login.getClientSecret()).getBytes(Charset.forName("UTF-8"))))
            .build();

    HttpResponse<String> response = null;
    try {
      response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      Gson gson = new Gson();
      Map<String, String> result = gson.fromJson(response.body(),
          new TypeToken<Map<String, Object>>() { }.getType());
      return result;
    } catch (IOException | InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Get new Spotify token with refresh token.
   * @param refreshToken String
   * @return token String
   */
  public String refreshSpotifyToken(final String refreshToken) {

    HttpClient httpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .build();

    // form parameters
    Map<Object, Object> data = new HashMap<>();
    data.put("grant_type", "refresh_token");
    data.put("refresh_token", refreshToken);

    HttpRequest request = HttpRequest.newBuilder()
            .POST(Login.buildFormDataFromMap(data))
            .uri(URI.create("https://accounts.spotify.com/api/token"))
            .setHeader("User-Agent", "Java 11 HttpClient Bot")
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("Authorization", "Basic "
                + Base64.getEncoder().encodeToString((Login.getClientId() + ":"
                + Login.getClientSecret()).getBytes(Charset.forName("UTF-8"))))
            .build();

    HttpResponse<String> response = null;
    try {
      response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      Gson gson = new Gson();
      Map<String, String> result = gson.fromJson(response.body(),
          new TypeToken<Map<String, Object>>() { }.getType());

      return result.get("access_token");
    } catch (IOException | InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }
}
