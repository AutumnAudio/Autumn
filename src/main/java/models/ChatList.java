package models;

import java.util.ArrayList;
import java.util.List;

public class ChatList {
  /**
   * Player who starts the game.
   */
  private List<ChatRoom> chatrooms = new ArrayList<ChatRoom>();

  public ChatList() {
  	Genre[] genres = Genre.class.getEnumConstants();
    for (Genre genre : genres) {
  	  ChatRoom chatroom = new ChatRoom();
  	  chatroom.setParticipant(new ArrayList<User>());
  	  chatroom.setChat(new ArrayList<Message>());
  	  chatroom.setPlaylist(new ArrayList<Song>());
  	  chatroom.setGenre(genre);
  	  chatroom.setLink("/joinroom/" + genre.getGenre());
  	  chatrooms.add(chatroom);
    }
  }

  /**
   * Get chatrooms.
   * @return chatrooms List<Chatroom>.
   */
  public List<ChatRoom> getChatrooms() {
    return this.chatrooms;
  }

  /**
   * Set chatroom list.
   * @param chatrooms List<ChatRoom>.
   */
  public void setChatrooms(List<ChatRoom> chatroomList) {
    this.chatrooms = chatroomList;
  }

}
