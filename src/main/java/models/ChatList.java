package models;

import java.util.List;

public class ChatList {

  /**
   * Player who starts the game.
   */
  private List<ChatRoom> chatrooms;

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
   * Add chatroom to chatroom list.
   * @param chatroom ChatRoom Object
   */
  public void addChatRoom(final ChatRoom chatroom) {
    this.chatrooms.add(chatroom);
  }

}
