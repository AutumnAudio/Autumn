package models;

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
    if (code == null || code.length() == 0) {
      return null;
    }
    HttpClient httpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .build();

    // form parameters
    Map<Object, Object> data = new HashMap<>();
    data.put("grant_type", "authorization_code");
    data.put("code", code);
    data.put("redirect_uri", Login.getRedirectURI());

    HttpRequest request = HttpRequest.newBuilder()
            .POST(buildFormDataFromMap(data))
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
    if (refreshToken == null || refreshToken.length() == 0) {
      return null;
    }
    HttpClient httpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .build();

    // form parameters
    Map<Object, Object> data = new HashMap<>();
    data.put("grant_type", "refresh_token");
    data.put("refresh_token", refreshToken);

    HttpRequest request = HttpRequest.newBuilder()
            .POST(buildFormDataFromMap(data))
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

  /**
   * get email from given Spotify token.
   * @param spotifyToken String
   * @return email String
   */
  public String getEmailFromSpotifyToken(final String spotifyToken) {
    if (spotifyToken == null || spotifyToken.length() == 0) {
      return null;
    }
    HttpClient httpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .build();

    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.spotify.com/v1/me"))
            .setHeader("User-Agent", "Java 11 HttpClient Bot")
            .header("Authorization", "Bearer " + spotifyToken)
            .build();

    HttpResponse<String> response = null;
    try {
      response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      Gson gson = new Gson();
      Map<String, String> result = gson.fromJson(response.body(),
          new TypeToken<Map<String, Object>>() { }.getType());

      return result.get("email");
    } catch (IOException | InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }
 
  /**
   * Build HTTP Form Data with given data.
   * @param data Map
   * @return Form data HTTPRequest
   */
  private static HttpRequest.BodyPublisher buildFormDataFromMap(
        final Map<Object, Object> data) {
    var builder = new StringBuilder();
    for (Map.Entry<Object, Object> entry : data.entrySet()) {
      if (builder.length() > 0) {
        builder.append("&");
      }
      builder.append(URLEncoder.encode(entry.getKey().toString(),
                StandardCharsets.UTF_8));
      builder.append("=");
      builder.append(URLEncoder.encode(entry.getValue().toString(),
                StandardCharsets.UTF_8));
    }

    return HttpRequest.BodyPublishers.ofString(builder.toString());
  }
}
