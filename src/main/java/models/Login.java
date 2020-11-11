package models;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Login {
  
  private static String clientId = "17b185367b7d45a8b8cb068eda7787cf";
  private static String clientSecret = "d693166032a24dcc8e7ff14ef71366c7";
  
  public static String getSpotifyAuthUrl() {
    
    String url = "https://accounts.spotify.com/authorize?response_type=code"
        + "&client_id=" + clientId
        + "&redirect_uri=" + URLEncoder.encode("http://localhost:8080/process_auth", StandardCharsets.UTF_8)
        + "&scope=user-read-email,user-read-recently-played,user-read-currently-playing,playlist-modify-public,playlist-modify-private";

    return url;
    
  }
  
  public static String getSpotifyTokenFromCode(String code) {
    
    
    return "";
  }
}