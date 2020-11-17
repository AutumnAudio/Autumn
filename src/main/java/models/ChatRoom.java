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
   */
  public void addParticipant(final User user) {
    this.participants.put(user.getUsername(), user);
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
   */
  public void addMessage(final Message message) {
    // call sql query to add message
    this.chat.add(message);
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
   */
  public void addSong(final Song song) {
    // call sql query to add song
    this.playlist.add(song);
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
