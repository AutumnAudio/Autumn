package models;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class Login {

  /**
   * private constructor to prevent instantiation.
   */
  private Login() {
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

  /**
   * get client id of app.
   * @return clientId String
   */
  public static String getClientId() {
    return clientId;
  }

  /**
   * Get client secret of app.
   * @return clientSecret String
   */
  public static String getClientSecret() {
    return clientSecret;
  }

  /**
   * Get redirect URI.
   * @return redirectURI String
   */
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
