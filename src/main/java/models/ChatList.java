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
    if (genre == null) {
      return null;
    }
    return chatrooms.get(genre.getGenre());
  }

  /**
   * Return total people currently in all chatrooms.
   * @return numPeopleChatting integer
   */
  public int getTotalParticipants() {
    if (this.chatrooms == null) {
      return 0;
    }
    int numPeopleChatting = 0;
    for (Map.Entry<String, ChatRoom> chatEntry : this.chatrooms.entrySet()) {
      numPeopleChatting += chatEntry.getValue().getParticipant().size();
    }
    return numPeopleChatting;
  }

  /**
   * Refresh Chatlist song data.
   * @return response String
   */
  public String refreshChatList() {
    if (this.chatrooms == null) {
      return "Null chatroom list";
    }
    for (ChatRoom chatroom : chatrooms.values()) {
      if (chatroom == null) {
        return "Null chatroom";
      }
      Map<String, User> participants = chatroom.getParticipant();
      for (Map.Entry<String, User> entry : participants.entrySet()) {
        User user = entry.getValue();
        String token = user.getSpotifyToken();
        if (!token.equals("null")) {
          user.refreshRecentlyPlayed();
          user.refreshCurrentlyPlaying();
        }
        // MyApi object cannot be serialize by Gson
        user.setApi(null);
      }
    }
    return "OK";
  }

}
