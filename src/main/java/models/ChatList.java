package models;

import java.util.HashMap;
import java.util.Map;

public class ChatList {
  /**
   * Player who starts the game.
   */
  private Map<String, ChatRoom> chatrooms = new HashMap<>();

  /**
   * Get chatrooms.
   * @return chatrooms List<Chatroom>.
   */
  public Map<String, ChatRoom> getChatrooms() {
    return this.chatrooms;
  }

  /**
   * Set chatroom list.
   * @param chatrooms List<ChatRoom>.
   */
  public void setChatrooms(Map<String, ChatRoom> map) {
    this.chatrooms = map;
  }

  /**
   * Get chatrooms by genre.
   * @return chatroom Chatroom.
   */
  public ChatRoom getChatroomByGenre(Genre genre) {
    return chatrooms.get(genre.getGenre());
  }

  /**
   * Return size of ArrayList
   * @return size integer
   */
  public int size() {
    return this.chatrooms.size();
  }

  /**
   * Refresh Chatlist song data
   */
  public void refreshChatList(SqLite db) {
    for (ChatRoom chatroom : chatrooms.values()) {
      Map<String, User> participants = chatroom.getParticipant();
      for (String username : participants.keySet()) {
    	User user = participants.get(username);
       	if (!user.getSpotifyToken().equals("")) {    
          user.refreshRecentlyPlayed();
          user.refreshCurrentlyPlaying();
        }
      }
    }
  }

}
