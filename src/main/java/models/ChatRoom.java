package models;

import java.util.List;
import java.util.Map;

public class ChatRoom {

  /**
   * Participant in chatroom.
   */
  private Map<String, User> participants;

  /**
   * Participant in chatroom.
   */
  private List<Message> chat;

  /**
   * Participant in chatroom.
   */
  private List<Song> playlist;

  /**
   * Genre of chatroom.
   */
  private Genre genre;

  /**
   * link chatroom.
   */
  private String link;

  /**
   * Get participant list.
   * @return participant List
   */
  public Map<String, User> getParticipant() {
    return this.participants;
  }


  /**
   * Set participant list.
   * @param participantList List
   */
  public void setParticipant(final Map<String, User> participantList) {
    this.participants = participantList;
  }

  /**
   * Add user to chatroom participant list.
   * @param user User Object
   * @return response String
   */
  public String addParticipant(final User user) {
    if (user == null || user.getUsername() == null
            || user.getUsername().length() == 0) {
      return "No user";
    }
    this.participants.put(user.getUsername(), user);
    return "OK";
  }

  /**
   * Get chat history.
   * @return chat List
   */
  public List<Message> getChat() {
    return this.chat;
  }

  /**
   * Set chat history.
   * @param newChat List
   */
  public void setChat(final List<Message> newChat) {
    this.chat = newChat;
  }

  /**
   * Add message to chat history.
   * @param message Message Object
   * @return response String
   */
  public String addMessage(final Message message) {
    // call sql query to add message
	if (message == null || message.getMessage() == null
            || message.getMessage().length() == 0) {
      return "No message";
	}
	this.chat.add(message);
	return "OK";
  }

  /**
   * Get shared songs.
   * @return songs List
   */
  public List<Song> getPlaylist() {
    return this.playlist;
  }

  /**
   * Set shared songs.
   * @param songList List
   */
  public void setPlaylist(final List<Song> songList) {
    this.playlist = songList;
  }

  /**
   * Add song to shared songs.
   * @param song Song Object
   * @return response String
   */
  public String addSong(final Song song) {
    if (song == null || song.getName() == null
            || song.getName().length() == 0) {
      return "No song";
    }
    this.playlist.add(song);
    return "OK";
  }

  /**
   * Get chatroom genre.
   * @return genre String.
   */
  public Genre getGenre() {
    return this.genre;
  }

  /**
   * Set chatroom genre.
   * @param newGenre String.
   */
  public void setGenre(final Genre newGenre) {
    this.genre = newGenre;
  }

  /**
   * Get link to chatroom.
   * @return link String.
   */
  public String getLink() {
    return this.link;
  }

  /**
   * Set link to chatroom.
   * @param newLink String.
   */
  public void setLink(final String newLink) {
    this.link = newLink;
  }
}
