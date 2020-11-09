package models;

import java.util.ArrayList;
import java.util.List;

public class ChatList {
  /**
   * Player who starts the game.
   */
  private List<ChatRoom> chatrooms = new ArrayList<ChatRoom>();

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

  /**
   * Return size of ArrayList
   * @return size integer
   */
  public int size() {
    return this.chatrooms.size();
  }

}
