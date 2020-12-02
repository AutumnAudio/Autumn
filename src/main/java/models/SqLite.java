package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sqlite.SQLiteConfig;

public class SqLite {

  /**
   * Connection to database.
   */
  private Connection conn = null;

  /**
   * database statement for execution.
   */
  private Statement stmt = null;

  /**
   * spotify api.
   */
  private SpotifyAPI api = new SpotifyAPI();

  /**
   * set api.
   * @param newApi SpotifyAPI
   */
  public void setApi(SpotifyAPI newApi) {
    this.api = newApi;
  }

  /**
   * connect to autumn database.
   */
  public void connect() {
    
    try {
      if(conn != null && !conn.isClosed())
        return;
    } catch (SQLException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
      
    try {
        Class.forName("org.sqlite.JDBC");
      } catch (ClassNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      try {
        
        SQLiteConfig config = new SQLiteConfig();
        
        conn = DriverManager.getConnection("jdbc:sqlite:autumn.db",config.toProperties());
        conn.setAutoCommit(true);
        stmt = conn.createStatement();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
  }

  /**
   * Start database.
   */
  public void start() {
    
    connect();
    
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    try {
      
     // stmt = conn.createStatement();
      String sql = "CREATE TABLE IF NOT EXISTS USERS "
                     + " (USERNAME              VARCHAR PRIMARY KEY NOT NULL, "
                     + " PASSWORD_HASH          VARCHAR, "
                     + " SPOTIFY_TOKEN          VARCHAR UNIQUE, "
                     + " SPOTIFY_REFRESH_TOKEN  VARCHAR UNIQUE, "
                     + " SESSION_ID             VARCHAR) ";
                     //+ " LAST_CONNECTION_TIME   TIME) ";
      stmt.executeUpdate(sql);
      sql = "CREATE TABLE IF NOT EXISTS SESSIONS "
              + " (TIME_VISITED         VARCHAR PRIMARY KEY NOT NULL, "
              + " SESSION_ID            VARCHAR) ";
      stmt.executeUpdate(sql);
      sql = "CREATE TABLE IF NOT EXISTS CHATROOMS "
                     + " (GENRE            VARCHAR PRIMARY KEY NOT NULL, "
                     + " LINK              VARCHAR NOT NULL, "
                     + " SPOTIFY_PLAYLIST  VARCHAR) ";
      stmt.executeUpdate(sql);
      sql = "CREATE TABLE IF NOT EXISTS PARTICIPANTS "
                     + " (GENRE                 VARCHAR NOT NULL, "
                     + " USERNAME               INT NOT NULL, "
                     + " SPOTIFY_TOKEN          VARCHAR, "
                     + " SPOTIFY_REFRESH_TOKEN  VARCHAR, "
                     + " SESSION_ID             VARCHAR, "
                     + " CONSTRAINT             PARTICIPANT PRIMARY KEY "
                     + "(GENRE, USERNAME) ) ";
      stmt.executeUpdate(sql);
      sql = "CREATE TABLE IF NOT EXISTS PLAYLIST "
                     + " (USERNAME         VARCHAR NOT NULL, "
                     + " TIME_SHARED       TIME NOT NULL, "
                     + " GENRE             VARCHAR NOT NULL, "
                     + " SONG              VARCHAR, "
                     + " CONSTRAINT        MESSAGE_ID PRIMARY KEY "
                     + "(USERNAME, TIME_SHARED) ) ";
      stmt.executeUpdate(sql);
      sql = "CREATE TABLE IF NOT EXISTS USERGENRE "
            + "(USERNAME VARCHAR NOT NULL, "
            + " GENRE     VARCHAR NOT NULL)";
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * clean tables in database.
   */
  public synchronized void clear() {
    
    try {

      String sql = "DELETE FROM USERS;";
      stmt.executeUpdate(sql);
      sql = "DELETE FROM SESSIONS;";
      stmt.executeUpdate(sql);
      sql = "DELETE FROM CHATROOMS;";
      stmt.executeUpdate(sql);
      sql = "DELETE FROM PARTICIPANTS;";
      stmt.executeUpdate(sql);
      sql = "DELETE FROM CHAT;";
      stmt.executeUpdate(sql);
      sql = "DELETE FROM PLAYLIST;";
      stmt.executeUpdate(sql);
      sql = "DELETE FROM USERGENRE";
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Add user to Users Table.
   * @param username String
   * @param spotifyToken String
   * @return response String
   */
  public synchronized String insertAuthenticatedUser(final String username,
      final String token, final String refreshToken, final String sessionId) {
    if (username == null || username.length() == 0) {
      return "No username";
    }
    if (token == null || token.length() == 0) {
      return "No token";
    }
    if (refreshToken == null || refreshToken.length() == 0) {
      return "No refresh token";
    }
    if (sessionId == null || sessionId.length() == 0) {
      return "No session id";
    }
    try {
      String sql = String.format("INSERT INTO USERS (USERNAME, SPOTIFY_TOKEN, "
      		+ "SPOTIFY_REFRESH_TOKEN, SESSION_ID) "
            + "VALUES ('%s','%s','%s','%s');",
            username, token, refreshToken, sessionId);
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "OK";
  }

  /**
   * Update user attribute in Users Table.
   * @param attribute String
   * @param value String
   * @param username String
   * @return response String
   */
  public synchronized String updateUserAttribute(final String attribute,
          final String value, final String username) {
    if (attribute == null || attribute.length() == 0) {
      return "No attribute";
    }
    if (value == null || value.length() == 0) {
      return "No value";
    }
    if (username == null || username.length() == 0) {
      return "No username";
    }
    try {
      String sql = String.format("UPDATE USERS SET %s = '%s'"
                     + " WHERE USERNAME = '%s';", attribute, value, username);
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "OK";
  }

  /**
   * Add user name and genre pair.
   * @param username String
   * @param genre String
   * @return response String
   */
  public synchronized String insertUserwithGenre(final String username,
          final String genre) {
    if (username == null || username.length() == 0) {
      return "No username";
    }
    if (genre == null || genre.length() == 0) {
      return "No genre";
    }
    try {
      String sql = String.format("INSERT INTO USERGENRE (USERNAME, GENRE) "
                  + "VALUES ('%s','%s');", username, genre);
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "OK";
  }

  /**
   * Get user by username.
   * @param username String
   * @return user User Object
   */
  public synchronized User getUserByName(final String username) {
    if (username == null || username.length() == 0) {
     return new User();
    }
    User user = new User();
    try {
      ResultSet rs;
      String sql = String.format("SELECT * FROM USERS "
              + "WHERE USERNAME = '%s' LIMIT 1", username);
      rs = stmt.executeQuery(sql);
      try {
        while (rs.next()) {
          user.setUsername(rs.getString("USERNAME"));
          user.setPasswordHash(rs.getString("PASSWORD_HASH"));
          user.setSpotifyToken(rs.getString("SPOTIFY_TOKEN"));
          user.setSpotifyRefreshToken(rs.getString("SPOTIFY_REFRESH_TOKEN"));
          user.setSessionId(rs.getString("SESSION_ID"));
          //user.setLastConnectionTime(rs.getInt("LAST_CONNECTION_TIME"));
        }
      } finally {
        rs.close();
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return user;
  }

  /**
   * authenticate user
   * @param code String
   * @param sessionId String
   * @return response String
   */
  public String authenticateUser(final String code, final String sessionId) {
    String ret = "";
    if (code == null || code.length() == 0) {
      return "No code";
    }
    if (sessionId == null || sessionId.length() == 0) {
      return "No session id";
    }
    Map<String, String> response = api.getSpotifyTokenFromCode(code);
    String email = api.getEmailFromSpotifyToken(response.get("access_token"));
    User tmpUser = getUserByName(email);
	if (tmpUser.getUsername() != null) {
      updateUserAttribute("SPOTIFY_TOKEN",
    		  response.get("access_token"), email);
      updateUserAttribute("SPOTIFY_REFRESH_TOKEN",
    		  response.get("refresh_token"), email);
      updateUserAttribute("SESSION_ID",sessionId, email);
      ret = "User exists";
    } else {
      insertAuthenticatedUser(email, response.get("access_token"),
              response.get("refresh_token"), sessionId);
      ret = "New user";
      
    }
	return ret;
  }

  /**
   * Get user by username.
   * @param username String
   * @return count equals 1 boolean
   */
  public synchronized int getUserCount(final String username) {
    if (username == null || username.length() == 0) {
      return 0;
    }
    int count = 0;
    try {
      ResultSet rs;
      String sql = String.format("SELECT * FROM USERS "
              + "WHERE USERNAME = '%s' LIMIT 1", username);
      rs = stmt.executeQuery(sql);
      try {
        while (rs.next()) {
          count++;
        }
      } finally {
        rs.close();
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return count;
  }

  /**
   * Get genre by username.
   * @param username String
   * @return genre String
   */
  public synchronized String getGenreUser(final String username) {
    if (username == null || username.length() == 0) {
      return null;
    }
    String gen = null;
    try {
      ResultSet rs;
      String sql = String.format("SELECT * FROM USERGENRE "
              + "WHERE USERNAME = '%s' LIMIT 1", username);
      rs = stmt.executeQuery(sql);
      try {
        while (rs.next()) {
          gen = rs.getString("GENRE");
        }
      } finally {
        rs.close();
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return gen;
  }

  /**
   * Get user by sessionId.
   * @param sessionId String
   * @return user User
   */
  public synchronized User getUserBySessionId(final String sessionId) {
    if (sessionId == null || sessionId.length() == 0) {
      return null;
    }
    User user = new User();
    try {
      ResultSet rs;
      String sql = String.format("SELECT * FROM USERS "
              + "WHERE SESSION_ID = '%s'", sessionId);
      rs = stmt.executeQuery(sql);
      try {
        while (rs.next()) {
          user.setUsername(rs.getString("USERNAME"));
          user.setPasswordHash(rs.getString("PASSWORD_HASH"));
          user.setSpotifyToken(rs.getString("SPOTIFY_TOKEN"));
          user.setSpotifyRefreshToken(rs.getString("SPOTIFY_REFRESH_TOKEN"));
          user.setSessionId(rs.getString("SESSION_ID"));
         // user.setLastConnectionTime(rs.getInt("LAST_CONNECTION_TIME"));
        }
      } finally {
        rs.close();       
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return user;
  }

  /**
   * Add chatroom to Chatrooms Table.
   * @param genre String
   * @param link String
   * @param spotifyPlaylist String
   * @return response String
   */
  public synchronized String insertChatRoom(final Genre genre, final String link,
          final String spotifyPlaylist) {
    if (genre == null) {
      return "No genre";
    }
    if (link == null || link.length() == 0) {
      return "No link";
    }
    if (spotifyPlaylist == null || spotifyPlaylist.length() == 0) {
      return "No playlist";
    }
    try {

      String sql = String.format("INSERT INTO CHATROOMS "
                   + "(GENRE, LINK, SPOTIFY_PLAYLIST) "
                   + "VALUES ('%s','%s','%s');",
                   genre.getGenre(), link, spotifyPlaylist);
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "OK";
  }

  /**
   * Get the list of Chatroom available.
   * @return Chatroom list List
   */
  public synchronized Map<String, ChatRoom> getAllChatRooms() {
    Map<String, ChatRoom> map = new HashMap<>();
    List<Genre> genres = new ArrayList<>();
    try {
      ResultSet rs;
      rs = stmt.executeQuery("SELECT * FROM CHATROOMS");
      try {
        while (rs.next()) {
          ChatRoom room = new ChatRoom();
          Genre genre = Genre.valueOf(rs.getString("GENRE").toUpperCase());
          room.setGenre(genre);
          room.setLink(rs.getString("LINK"));
          genres.add(genre);
          map.put(genre.getGenre(), room);
        }
      } finally {
        rs.close();
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    for (int i = 0; i < genres.size(); i++) {
      ChatRoom chatroom = map.get(genres.get(i).getGenre());
      Map<String, User> participants = getChatRoomParticipant(genres.get(i));
      List<Song> playlist = getChatRoomPlaylist(genres.get(i));
      List<Message> chat = new ArrayList<>();
      chatroom.setParticipant(participants);
      chatroom.setPlaylist(playlist);
      chatroom.setChat(chat);
      map.put(genres.get(i).getGenre(), chatroom);
    }
    
    return map;
  }

  /**
   * Add participant to Participant Table.
   * @param genre Genre
   * @param username String
   * @param token String
   * @param refreshToken String
   * @param sessionId String
   * @return response String
   */
  public synchronized String insertParticipant(final Genre genre, final String username,
      final String token, final String refreshToken, final String sessionId) {
	if (genre == null) {
      return "No genre";
    }
    if (username == null || username.length() == 0) {
      return "No username";
    }
    if (token == null || token.length() == 0) {
      return "No token";
    }
    if (refreshToken == null || refreshToken.length() == 0) {
      return "No refresh token";
    }
    if (sessionId == null || sessionId.length() == 0) {
      return "No session id";
    }
    try {
      String sql = String.format("INSERT INTO PARTICIPANTS "
                   + "(GENRE, USERNAME, SPOTIFY_TOKEN, "
                   + "SPOTIFY_REFRESH_TOKEN, SESSION_ID) "
                   + "VALUES ('%s','%s','%s','%s','%s');",
                   genre.getGenre(), username, token, refreshToken, sessionId);
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "OK";
  }

  /**
   * Remove participant to Participant Table.
   * @param genre String
   * @param username String
   * @return response String
   */
  public synchronized String removeParticipant(final Genre genre, final String username) {
	if (genre == null) {
      return "No genre";
    }
    if (username == null || username.length() == 0) {
      return "No username";
    }
    try {
      String sql = String.format("DELETE FROM PARTICIPANTS "
                   + "WHERE GENRE = '%s' AND USERNAME = '%s';",
                   genre.getGenre(), username);
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "OK";
    }

  /**
   * Remove user from USERGENRE.
   * @param username String
   * @return response String
   */
  public synchronized String removeUserGenre(final String username) {  
    if (username == null || username.length() == 0) {
      return "No username";
    }
    try {
      String sql = String.format("DELETE FROM USERGENRE "
                   + "WHERE USERNAME = '%s';",
                   username);
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "OK";
  }

  /**
   * get participant of a chatroom.
   * @param genre String
   * @return participant List
   */
  public synchronized Map<String, User> getChatRoomParticipant(final Genre genre) {
	if (genre == null) {
      return null;
    }
    Map<String, User> list = new HashMap<>();
    try {
      ResultSet rs;
      String sql = String.format("SELECT * FROM PARTICIPANTS "
                              + "WHERE GENRE = '%s'", genre.getGenre());
      rs = stmt.executeQuery(sql);
      try {
        while (rs.next()) {
          User user = new User();
          user.setUsername(rs.getString("USERNAME"));
          user.setSpotifyToken(rs.getString("SPOTIFY_TOKEN"));
          user.setSpotifyRefreshToken(rs.getString("SPOTIFY_REFRESH_TOKEN"));
          user.setSessionId(rs.getString("SESSION_ID"));
          list.put(user.getUsername(), user);
        }
      } finally {
        rs.close();
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return list;
  }

  /**
   * Add song to Playlist.
   * @param username String
   * @param timeShared Time
   * @param genre String
   * @param song String
   * @return response String
   */
  public synchronized String insertSong(final String username, final Time timeShared,
          final Genre genre, final String song) {
    if (username == null || username.length() == 0) {
      return "No username";
    }
    if (timeShared == null) {
       return "No time";
    }
    if (genre == null) {
      return "No genre";
    }
    if (song == null || song.length() == 0) {
      return "No song";
    }
    try {
      String sql = String.format("INSERT INTO PLAYLIST "
                   + "(USERNAME, TIME_SHARED, GENRE, SONG) "
                   + "VALUES ('%s','%s','%s','%s');",
                   username, timeShared, genre.getGenre(), song);
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "OK";
  }

  /**
   * Get playlist of a chatroom.
   * @param genre Genre
   * @return playlist List
   */
  public synchronized List<Song> getChatRoomPlaylist(final Genre genre) {
    if (genre == null) {
      return null;
    }
    List<Song> list = new ArrayList<>();
    try {
      ResultSet rs;
      String sql = String.format("SELECT * FROM PLAYLIST "
              + "WHERE GENRE = '%s'", genre.getGenre());
      rs = stmt.executeQuery(sql);
      try {
        while (rs.next()) {
          Song song = new Song();
          song.setUsername(rs.getString("USERNAME"));
          song.setSong(rs.getString("SONG"));
          list.add(song);
        }
      } finally {
        rs.close();
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    return list;
  }

  /**
   * Insert session to SESSIONS table.
   * @param time String
   * @param sessionId String
   * @return response String
   */
  public synchronized String insertSession(final String time, final String sessionId) {
    if (time == null || time.length() == 0) {
      return "No time";
    }
    if (sessionId == null || sessionId.length() == 0) {
      return "No session id";
    }
    try {
      String sql = String.format("REPLACE INTO SESSIONS "
                   + "(TIME_VISITED, SESSION_ID) "
                   + "VALUES ('%s','%s');", time, sessionId);
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return "OK";
  }
  
  /**
   * Get sessionId by username.
   * @return sessionId
   */
  public synchronized String getLatestSession() {
	  
    String sessionId = "";
    try {
      ResultSet rs;
      rs = stmt.executeQuery("SELECT * FROM SESSIONS "
                              + "ORDER BY TIME_VISITED ASC");
      try {
        while (rs.next()) {
          sessionId = rs.getString("SESSION_ID");
        }
      } finally {
        rs.close();
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return sessionId;
  }

  /**
   * Update chatroom after reboot.
   * @return chatlist ChatList
   */
  public ChatList update() {
    ChatList chatlist = new ChatList();
    chatlist.setChatrooms(new HashMap<String, ChatRoom>());
    chatlist.setChatrooms(getAllChatRooms());
    return chatlist;
  }

  /**
   * close database.
   */
  public void close() {
    try {
      stmt.close();
      conn.close();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
