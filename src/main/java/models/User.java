package models;

public class User {

  private String username;
  private String password_hash;
  private String sessionId;
  private String spotifyToken;
  private int lastConnectionTime;
  
  
  public String getPassword_hash() {
    return password_hash;
  }

  public void setPassword_hash(String password_hash) {
    this.password_hash = password_hash;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }
  
  public int getLastConnectionTime() {
    return lastConnectionTime;
  }

  public void setLastConnectionTime(int lastConnectionTime) {
    this.lastConnectionTime = lastConnectionTime;
  }

  public String getSpotifyToken() {
    return spotifyToken;
  }

  public void setSpotifyToken(String spotifyToken) {
    this.spotifyToken = spotifyToken;
  }

  /**
   * Get name of the user.
   * @return String
   */
  public String getUsername() {
    return this.username;
  }

  /**
   * Set username for the user.
   * @param username String
   */
  public void setUsername(final String name) {
    this.username = name;
  }
}
