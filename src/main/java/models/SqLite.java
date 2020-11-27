package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
   * connect to autumn database.
   */
  public void connect() {
  try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    try {
      conn = DriverManager.getConnection("jdbc:sqlite:autumn.db");
      conn.setAutoCommit(false);
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
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    try {
      conn = DriverManager.getConnection("jdbc:sqlite:autumn.db");
      conn.setAutoCommit(false);
      stmt = conn.createStatement();
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
      sql = "CREATE TABLE IF NOT EXISTS CHAT "
                     + " (USERNAME         VARCHAR NOT NULL, "
                     + " TIME_SENT         TIME NOT NULL, "
                     + " GENRE             VARCHAR NOT NULL, "
                     + " MESSAGE           VARCHAR, "
                     + " CONSTRAINT        MESSAGE_ID PRIMARY KEY "
                     + "(USERNAME, TIME_SENT) ) ";
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
  public void clear() {
    try {
      conn.setAutoCommit(false);
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
   */
  public void insertUserWithToken(final String username,
      final String spotifyToken) {
    try {
      conn.setAutoCommit(false);
      String sql = String.format("INSERT INTO USERS (USERNAME, SPOTIFY_TOKEN) "
                     + "VALUES ('%s','%s');", username, spotifyToken);
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Add user to Users Table.
   * @param username String
   * @param sessionId String
   */
  public void insertUserWithSession(final String username,
      final String sessionId) {
    try {
      conn.setAutoCommit(false);
      String sql = String.format("INSERT INTO USERS (USERNAME, SESSION_ID) "
            + "VALUES ('%s','%s');", username, sessionId);
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Add user name and genre pair.
   * @param username String
   * @param genre String
   */
  public void insertUserwithGenre(final String username, final String genre) {
    try {
      conn.setAutoCommit(false);
      String sql = String.format("INSERT INTO USERGENRE (USERNAME, GENRE) "
                  + "VALUES ('%s','%s');", username, genre);
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Update user attribute in Users Table.
   * @param attribute String
   * @param value String
   * @param username String
   */
  public void updateUserAttribute(final String attribute,
          final String value, final String username) {
    try {
      conn.setAutoCommit(false);
      String sql = String.format("UPDATE USERS SET %s = '%s'"
                     + " WHERE USERNAME = '%s';", attribute, value, username);
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Get user by username.
   * @param username String
   * @return user User Object
   */
  public User getUserByName(final String username) {
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
   * Get user by username.
   * @param username String
   * @return count equals 1 boolean
   */
  public int getUserCount(final String username) {
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
   * @return count equals 1 boolean
   */
  public String getGenreUser(final String username) {
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
  public User getUserBySessionId(final String sessionId) {
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
   */
  public void insertChatRoom(final Genre genre, final String link,
          final String spotifyPlaylist) {
    try {
      conn.setAutoCommit(false);
      String sql = String.format("INSERT INTO CHATROOMS "
                   + "(GENRE, LINK, SPOTIFY_PLAYLIST) "
                   + "VALUES ('%s','%s','%s');",
                   genre.getGenre(), link, spotifyPlaylist);
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Get the list of Chatroom available.
   * @return Chatroom list List
   */
  public Map<String, ChatRoom> getAllChatRooms() {
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
      List<Message> chat = getChatRoomChat(genres.get(i));
      List<Song> playlist = getChatRoomPlaylist(genres.get(i));
      chatroom.setParticipant(participants);
      chatroom.setChat(chat);
      chatroom.setPlaylist(playlist);
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
   */
  public void insertParticipant(final Genre genre, final String username,
      final String token, final String refreshToken, final String sessionId) {
    try {
      conn.setAutoCommit(false);
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
  }

  /**
   * Remove participant to Participant Table.
   * @param genre String
   * @param username String
   */
  public void removeParticipant(final Genre genre, final String username) {
    try {
      conn.setAutoCommit(false);
      String sql = String.format("DELETE FROM PARTICIPANTS "
                   + "WHERE GENRE = '%s' AND USERNAME = '%s';",
                   genre.getGenre(), username);
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Remove user from USERGENRE.
   * @param username String
   */
  public void removeUserGenre(final String username) {
    try {
      conn.setAutoCommit(false);
      String sql = String.format("DELETE FROM USERGENRE "
                   + "WHERE USERNAME = '%s';",
                   username);
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * get participant of a chatroom.
   * @param genre String
   * @return participant List
   */
  public Map<String, User> getChatRoomParticipant(final Genre genre) {
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
   * Add message to Chat Table.
   * @param username String
   * @param timeSent Time
   * @param genre String
   * @param message String
   */
  public void insertMessage(final String username, final Time timeSent,
          final Genre genre, final String message) {
    try {
      conn.setAutoCommit(false);
      String sql = String.format("INSERT INTO CHAT "
                     + "(USERNAME, TIME_SENT, GENRE, MESSAGE) "
                     + "VALUES ('%s','%s','%s','%s');",
                     username, timeSent, genre.getGenre(), message);
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Get chat of a chatroom.
   * @param genre String
   * @return chat history List
   */
  public List<Message> getChatRoomChat(final Genre genre) {
    List<Message> list = new ArrayList<>();
    try {
      ResultSet rs;
      String sql = String.format("SELECT * FROM CHAT "
              + "WHERE GENRE = '%s'", genre.getGenre());
      rs = stmt.executeQuery(sql);
      try {
        while (rs.next()) {
          Message message = new Message();
          message.setUsername(rs.getString("USERNAME"));
          message.setMessage(rs.getString("MESSAGE"));
          list.add(message);
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
   */
  public void insertSong(final String username, final Time timeShared,
          final Genre genre, final String song) {
    try {
      conn.setAutoCommit(false);
      String sql = String.format("INSERT INTO PLAYLIST "
                   + "(USERNAME, TIME_SHARED, GENRE, SONG) "
                   + "VALUES ('%s','%s','%s','%s');",
                   username, timeShared, genre.getGenre(), song);
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Get playlist of a chatroom.
   * @param genre Genre
   * @return playlist List
   */
  public List<Song> getChatRoomPlaylist(final Genre genre) {
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
   */
  public void insertSession(final String time, final String sessionId) {
    try {
      conn.setAutoCommit(false);
      String sql = String.format("DELETE FROM SESSIONS "
              + "WHERE SESSION_ID = '%s'", sessionId);
      stmt.executeUpdate(sql);
      sql = String.format("INSERT INTO SESSIONS "
                   + "(TIME_VISITED, SESSION_ID) "
                   + "VALUES ('%s','%s');", time, sessionId);
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Get sessionId by username.
   * @return sessionId
   */
  public String getLatestSession() {
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
   * Commit last sql execution.
   */
  public void commit() {
    try {
      conn.commit();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
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
