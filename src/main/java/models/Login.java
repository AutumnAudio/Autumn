package models;

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


public class Login {

  /**
   * Public constructor
   */
  public Login() {
  }

  /**
   * client Id for Spotify app.
   */
  private static String clientId = "17b185367b7d45a8b8cb068eda7787cf";

  /**
   * client secret for Spotify app.
   */
  private static String clientSecret = "d693166032a24dcc8e7ff14ef71366c7";

  /**
   * redirect URI for process authentication.
   */
  private static String redirectURI = "http://localhost:8080/process_auth";

  public static String getClientId() {
    return clientId;
  }

  public static String getClientSecret() {
    return clientSecret;
  }

  public static String getRedirectURI() {
   return redirectURI;
  }

  /**
   * Get Spotify Authorization URL.
   * @return url String
   */
  public static String getSpotifyAuthUrl() {

    String url = "https://accounts.spotify.com/authorize?response_type=code"
            + "&client_id=" + clientId
            + "&redirect_uri="
            + URLEncoder.encode(redirectURI, StandardCharsets.UTF_8)
            + "&scope=" + URLEncoder.encode("user-read-email"
            + " user-read-recently-played"
            + " user-read-currently-playing"
            + " user-modify-playback-state"
            + " playlist-modify-public"
            + " playlist-modify-private", StandardCharsets.UTF_8);
    return url;
  }

  /**
   * get email from given Spotify token.
   * @param spotifyToken String
   * @return email String
   */
  public static String getEmailFromSpotifyToken(final String spotifyToken) {
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
  public static HttpRequest.BodyPublisher buildFormDataFromMap(
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
