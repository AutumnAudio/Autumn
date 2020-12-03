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

  
}
