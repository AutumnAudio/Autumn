package models;

public class User {

  private String username; //spotify email
  private String passwordHash;
  private String sessionId;
  private String spotifyToken;
  private String spotifyRefreshToken;
  
  private int lastConnectionTime;
  
  private SqLite db;
  
  public User(String spotifyToken, SqLite db) {
    
    setDb(db);
    
    String email = Login.getEmailFromSpotifyToken(spotifyToken);

    User tmpUser = db.getUserByName(email);
    
    if(tmpUser.username != null) {
      copyUser(tmpUser);
    }
    
    else {
      db.insertUser(email, spotifyToken);
    }
    
    this.setSpotifyToken(spotifyToken, true);
  }
  
  public User() {
    
  }
  
  public void copyUser(User other) {
    
    this.username = other.username;
    this.passwordHash = other.passwordHash;
    this.sessionId = other.sessionId;
    this.spotifyToken = other.spotifyToken;
    this.spotifyRefreshToken = other.spotifyRefreshToken;
    this.lastConnectionTime = other.lastConnectionTime;
    
    //this.db = other.db;
  }

  public String getPassword_hash() {
    return passwordHash;
  }

  public void setPasswordHash(String password_hash) {
    this.passwordHash = password_hash;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }
  
  public void setSessionId(String sessionId, boolean saveToDb) {
    setSessionId(sessionId);
    
    if(saveToDb) {
      db.updateUserAttribute("SESSION_ID", sessionId, username);
    }
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
  
  public void setSpotifyToken(String spotifyToken, boolean saveToDb) {
    
    setSpotifyToken(spotifyToken);
    
    if(saveToDb) {
      db.updateUserAttribute("SPOTIFY_TOKEN", spotifyToken, username);
    }
  }
  
  public String getSpotifyRefreshToken() {
    return spotifyRefreshToken;
  }

  public String refreshSpotifyToken() {
    
    setSpotifyToken(Login.refreshSpotifyToken(spotifyRefreshToken), true);
    return getSpotifyToken();
  }
  
  public void setSpotifyRefreshToken(String spotifyRefreshToken) {
    this.spotifyRefreshToken = spotifyRefreshToken;
  }
  
  public void setSpotifyRefreshToken(String spotifyRefreshToken, boolean saveToDb) {
    setSpotifyRefreshToken(spotifyRefreshToken);
    
    if(saveToDb) {
      db.updateUserAttribute("SPOTIFY_REFRESH_TOKEN", spotifyRefreshToken, username);
    }
  }
  
  public SqLite getDb() {
    return db;
  }

  public void setDb(SqLite db) {
    this.db = db;
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
