package models;

import java.util.List;

public class ChatRoom {

  /**
   * Participant in chatroom.
   */
  List<User> participants;

  /**
   * Participant in chatroom.
   */
  List<Message> chat;

  /**
   * Participant in chatroom.
   */
  List<Song> playlist;

  /**
   * Genre of chatroom
   */
  Genre genre;

  /**
   * link chatroom
   */
  String link;

  /**
   * Get participant list.
   * @return participant List<User>.
   */
  public List<User> getParticipant() {
    return this.participants;
  }

  /**
   * Set participant list.
   * @param participant List<User>.
   */
  public void setParticipant(List<User> participantList) {
    this.participants = participantList;
  }

  /**
   * Add user to chatroom participant list.
   * @param user User Object
   */
  public void addParticipant(final User user) {
    this.participants.add(user);
  }

  /**
   * Get chat history.
   * @return chat List<Message>.
   */
  public List<Message> getChat() {
    return this.chat;
  }

  /**
   * Set chat history.
   * @param chat List<Message>.
   */
  public void setChat(List<Message> chat) {
    this.chat = chat;
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
   * @return songs List<Song>.
   */
  public List<Song> getPlaylist() {
    return this.playlist;
  }

  /**
   * Set shared songs.
   * @param shared songs List<Song>.
   */
  public void setPlaylist(List<Song> songList) {
    this.playlist = songList;
  }

  /**
   * Add song to shared songs.
   * @param song Song Object
   */
  public void addsong(final Song song) {
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
   * @param genre String.
   */
  public void setGenre(Genre genre) {
    this.genre = genre;
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
   * @param link String.
   */
  public void setLink(String link) {
    this.link = link;
  }
}
