package models;

import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Login {
  
  private static String clientId = "17b185367b7d45a8b8cb068eda7787cf";
  private static String clientSecret = "d693166032a24dcc8e7ff14ef71366c7";
  private static String redirectURI = "http://localhost:8080/process_auth";
  
  public static String getSpotifyAuthUrl() {
    
    String url = "https://accounts.spotify.com/authorize?response_type=code"
        + "&client_id=" + clientId
        + "&redirect_uri=" + URLEncoder.encode(redirectURI, StandardCharsets.UTF_8)
        + "&scope=" + URLEncoder.encode("user-read-email user-read-recently-played user-read-currently-playing "
        + "playlist-modify-public playlist-modify-private"
            , StandardCharsets.UTF_8);

    return url;
  }
  
  public static Map<String, String> getSpotifyTokenFromCode(String code) {
    
    //https://mkyong.com/java/how-to-send-http-request-getpost-in-java/
    HttpClient httpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .build();
    
    // form parameters
    Map<Object, Object> data = new HashMap<>();
    data.put("grant_type", "authorization_code");
    data.put("code", code);
    data.put("redirect_uri", redirectURI);

    HttpRequest request = HttpRequest.newBuilder()
            .POST(buildFormDataFromMap(data))
            .uri(URI.create("https://accounts.spotify.com/api/token"))
            .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("Authorization", "Basic "
                + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes()))
            .build();

    HttpResponse<String> response = null;
    try {
      response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    Gson gson = new Gson();
    Map<String, String> result = gson.fromJson(response.body(), new TypeToken<Map<String, Object>>() {}.getType());
    
    return result;
  }
  
  public static String refreshSpotifyToken(String refreshToken) {
   
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
            .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
            .header("Content-Type", "application/x-www-form-urlencoded")
            .header("Authorization", "Basic "
                + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes()))
            .build();
    
    HttpResponse<String> response = null;
    try {
      response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    Gson gson = new Gson();
    Map<String, String> result = gson.fromJson(response.body(), new TypeToken<Map<String, Object>>() {}.getType());
     
    return result.get("access_token");
  }
  
  public static String getEmailFromSpotifyToken(String spotifyToken) {
    
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
    } catch (IOException | InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    Gson gson = new Gson();
    Map<String, String> result = gson.fromJson(response.body(), new TypeToken<Map<String, Object>>() {}.getType());
    
    return result.get("email");
  }
  
  private static HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data) {
    var builder = new StringBuilder();
    for (Map.Entry<Object, Object> entry : data.entrySet()) {
        if (builder.length() > 0) {
            builder.append("&");
        }
        builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
        builder.append("=");
        builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
    }
    
    return HttpRequest.BodyPublishers.ofString(builder.toString());
}
}