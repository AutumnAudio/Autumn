package models;

import java.util.HashMap;
import java.util.Map;

public class ChatList {
  /**
   * Chatrooms data structure.
   */
  private Map<String, ChatRoom> chatrooms = new HashMap<>();

  /**
   * Get chatrooms.
   * @return chatrooms
   */
  public Map<String, ChatRoom> getChatrooms() {
    return this.chatrooms;
  }

  /**
   * Set chatroom list.
   * @param newChatrooms Map
   */
  public void setChatrooms(final Map<String, ChatRoom> newChatrooms) {
    this.chatrooms = newChatrooms;
  }

  /**
   * Get chatrooms by genre.
   * @param genre Genre
   * @return chatroom Chatroom.
   */
  public ChatRoom getChatroomByGenre(final Genre genre) {
    return chatrooms.get(genre.getGenre());
  }

  /**
   * Return size of ArrayList.
   * @return size integer
   */
  public int size() {
    return this.chatrooms.size();
  }
 
  /**
   * Return total people currently in all chatrooms.
   * @return numPeopleChatting integer
   */
  public int getTotalParticipants() {
    
    int numPeopleChatting = 0;
    
    for (Map.Entry<String,ChatRoom> chatEntry : this.chatrooms.entrySet()) {
      numPeopleChatting += chatEntry.getValue().getParticipant().size();
    }
    
    return numPeopleChatting;
  }

  /**
   * Refresh Chatlist song data.
   */
  public void refreshChatList() {
    for (ChatRoom chatroom : chatrooms.values()) {
      Map<String, User> participants = chatroom.getParticipant();
      for (Map.Entry<String, User> entry : participants.entrySet()) {
        User user = entry.getValue();
        String token = user.getSpotifyToken();
        if (!token.equals("null")) {
          user.refreshRecentlyPlayed();
          user.refreshCurrentlyPlaying();
        }
        // SpotifyAPI object cannot be serialize by Gson
        user.setApi(null);
      }
    }
  }

}
