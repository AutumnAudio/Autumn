package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

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
                     + " (USERNAME         VARCHAR PRIMARY KEY NOT NULL, "
                     + " PASSWORD_HASH     VARCHAR NOT NULL, "
                     + " SPOTIFY_TOKEN     VARCHAR NOT NULL, "
                     + " LOGGED_INT        BOOL, "
                     + " SESSION_ID        INT, "
                     + " LAST_CONNECTION   TIME) ";
      stmt.executeUpdate(sql);
      sql = "CREATE TABLE IF NOT EXISTS CHATROOMS "
    		         + " (GENRE            VARCHAR PRIMARY KEY NOT NULL, "
    		         + " LINK              VARCHAR NOT NULL, "
    		         + " SPOTIFY_PLAYLIST  VARCHAR) ";
      stmt.executeUpdate(sql);
      sql = "CREATE TABLE IF NOT EXISTS PARTICIPANTS "
    	             + " (GENRE            VARCHAR NOT NULL, "
    	             + " USERNAME          INT NOT NULL, "
    	             + " CONSTRAINT        PARTICIPANT PRIMARY KEY (GENRE, USERNAME) ) ";
      stmt.executeUpdate(sql);
      sql = "CREATE TABLE IF NOT EXISTS CHAT "
                     + " (USERNAME         VARCHAR NOT NULL, "
                     + " TIME_SENT         TIME NOT NULL, "
                     + " GENRE             VARCHAR NOT NULL, "
                     + " MESSAGE           VARCHAR, "
                     + " CONSTRAINT        MESSAGE_ID PRIMARY KEY (USERNAME, TIME_SENT) ) ";
      stmt.executeUpdate(sql);
      sql = "CREATE TABLE IF NOT EXISTS PLAYLIST "
                     + " (USERNAME         VARCHAR NOT NULL, "
                     + " TIME_SHARED       TIME NOT NULL, "
                     + " GENRE             VARCHAR NOT NULL, "
                     + " SONG              VARCHAR, "
                     + " CONSTRAINT        MESSAGE_ID PRIMARY KEY (USERNAME, TIME_SHARED) ) ";
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
      sql = "DELETE FROM CHATROOMS;";
      stmt.executeUpdate(sql);
      sql = "DELETE FROM PARTICIPANTS;";
      stmt.executeUpdate(sql);
      sql = "DELETE FROM CHAT;";
      stmt.executeUpdate(sql);
      sql = "DELETE FROM PLAYLIST;";
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Add user to Users Table
   * @param username String
   * @param passwordHash String
   * @param spotifyToken String
   */
  public void insertUser(final String username, final String passwordHash, final String spotifyToken) {
    try {
      conn.setAutoCommit(false);
      String sql = "INSERT INTO USERS (USERNAME, PASSWORD_HASH, SPOTIFY_TOKEN) "
                     + "VALUES (" + "'" + username + "'" + "," + "'" + passwordHash + "'" + "," + "'" + spotifyToken + "'" + ");";
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Get user by username.
   * @param username String
   * @return count equals 1 boolean
   */
  public User getUserByName(final String username) {
    User user = new User();
    try {
      ResultSet rs;
      rs = stmt.executeQuery("SELECT * FROM USERS "
                              + "WHERE USERNAME= " + "'" + username + "'");
      try {
        while (rs.next()) {
          user.setUsername(rs.getString("USERNAME"));
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
   * Add chatroom to Chatrooms Table
   * @param genre String
   * @param link String
   * @param spotifyPlaylist String
   */
  public void insertChatRoom(final String genre, final String link, final String spotifyPlaylist) {
    try {
      conn.setAutoCommit(false);
      String sql = "INSERT INTO CHATROOMS (GENRE, LINK, SPOTIFY_PLAYLIST) "
                     + "VALUES (" + "'" + genre + "'" + "," + "'" + link + "'" + "," + "'" + spotifyPlaylist + "'" + ");";
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Get the list of Chatroom available.
   * @return Chatroom list List<ChatRoom>
   */
  public List<ChatRoom> getChatRooms() {
	List<ChatRoom> list = new ArrayList<>();
	List<String> genres = new ArrayList<>();
    try {
      ResultSet rs;
      rs = stmt.executeQuery("SELECT * FROM CHATROOMS");
      try {
        while (rs.next()) {
          ChatRoom room = new ChatRoom();
          String genre = rs.getString("GENRE");
          room.setGenre(genre);
          room.setLink(rs.getString("LINK"));
          genres.add(genre);
          list.add(room);
        }
      } finally {
        rs.close();
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    for (int i = 0; i < genres.size(); i++) {
    	List<User> participants = getChatRoomParticipant(genres.get(i));
    	list.get(i).setParticipant(participants);
    	List<Message> chat = getChatRoomChat(genres.get(i));
    	list.get(i).setChat(chat);
    	List<Song> playlist = getChatRoomPlaylist(genres.get(i));
    	list.get(i).setPlaylist(playlist);
    }
    return list;
  }

  /**
   * Add participant to Participant Table
   * @param genre String
   * @param username String
   */

  public void insertParticipant(final String genre, final String username) {
    try {
      conn.setAutoCommit(false);
      String sql = "INSERT INTO PARTICIPANTS (GENRE, USERNAME) "
                     + "VALUES (" + "'" + genre + "'" + "," + "'" + username + "'" + ");";
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * get participant of a chatroom
   * @param genre String
   * @return participant List<User>
   */
  public List<User> getChatRoomParticipant(final String genre) {
	List<User> list = new ArrayList<>();
    try {
      ResultSet rs;
      rs = stmt.executeQuery("SELECT * FROM PARTICIPANTS "
                              + "WHERE GENRE= " + "'" + genre + "'");
      try {
        while (rs.next()) {
          User user = new User();
          user.setUsername(rs.getString("USERNAME"));
          list.add(user);
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
   * Add message to Chat Table
   * @param username String
   * @param timeSent Time
   * @param genre String
   * @param message String
   */

  public void insertMessage(final String username, final Time timeSent, final String genre, final String message) {
    try {
      conn.setAutoCommit(false);
      String sql = "INSERT INTO CHAT (USERNAME, TIME_SENT, GENRE, MESSAGE) "
                     + "VALUES (" + "'" + username + "'" + "," + "'" + timeSent + "'" + "," + "'" + genre  + "'" + "," + "'" + message + "'" + ");";
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Get chat of a chatroom
   * @param genre String
   * @return chat history List<Message>
   */
  public List<Message> getChatRoomChat(final String genre) {
	List<Message> list = new ArrayList<>();
    try {
      ResultSet rs;
      rs = stmt.executeQuery("SELECT * FROM CHAT "
                              + "WHERE GENRE= " + "'" + genre + "'");
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
   * Add song to Playlist
   * @param username String
   * @param timeShared Time
   * @param genre String
   * @param song String
   */

  public void insertSong(final String username, final Time timeShared, final String genre, final String song) {
    try {
      conn.setAutoCommit(false);
      String sql = "INSERT INTO PLAYLIST (USERNAME, TIME_SHARED, GENRE, SONG) "
                     + "VALUES (" + "'" + username + "'" + "," + "'" + timeShared + "'" + "," + "'" + genre + "'" + "," + "'" + song + "'" + ");";
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Get playlist of a chatroom
   * @param genre String
   * @return playlist List<Song>
   */
  public List<Song> getChatRoomPlaylist(final String genre) {
	List<Song> list = new ArrayList<>();
    try {
      ResultSet rs;
      rs = stmt.executeQuery("SELECT * FROM PLAYLIST "
                              + "WHERE GENRE= " + "'" + genre + "'");
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
   * @param chatroom ChatRoom Object
   */
  public void inProgress(final ChatRoom chatroom) {
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
