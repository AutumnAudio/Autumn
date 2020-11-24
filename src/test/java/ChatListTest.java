import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import models.ChatList;
import models.ChatRoom;
import models.Genre;
import models.Login;
import models.Song;
import models.User;

public class ChatListTest {

  String refreshToken = SpotifyAccount.getRefreshToken();

  @Test
  public void refreshChatListTestWithToken() {
    ChatList chatlist = new ChatList();
    ChatRoom chatroom = new ChatRoom();
    Map<String, ChatRoom> map = new HashMap<>();
    map.put("testRoom", chatroom);
    chatlist.setChatrooms(map);
    Map<String, User> participants = new HashMap<>();
    chatroom.setParticipant(participants);
    User user = new User();
	user.setUsername("test");
	user.setSpotifyToken(Login.refreshSpotifyToken(refreshToken));
	user.setSpotifyRefreshToken(refreshToken);
    chatroom.addParticipant(user);
    chatlist.refreshChatList();
	assertEquals(10, user.getRecentlyPlayed().length);
	for (Song song : user.getRecentlyPlayed()) {
	  assertNotNull(song.getName());
	}
  }

  @Test
  public void refreshChatListTestWithoutToken() {
    ChatList chatlist = new ChatList();
    ChatRoom chatroom = new ChatRoom();
    Map<String, ChatRoom> map = new HashMap<>();
    map.put("testRoom", chatroom);
    chatlist.setChatrooms(map);
    Map<String, User> participants = new HashMap<>();
    chatroom.setParticipant(participants);
    User user = new User();
	user.setUsername("test");
	user.setSpotifyToken("");
    chatroom.addParticipant(user);
    chatlist.refreshChatList();
	assertEquals(10, user.getRecentlyPlayed().length);
	for (Song song : user.getRecentlyPlayed()) {
	  assertNull(song);
	}
  }

  @Test
  public void sizeTest() {
    ChatList chatlist = new ChatList();
    ChatRoom chatroom = new ChatRoom();
    Map<String, ChatRoom> map = new HashMap<>();
    map.put("testRoom", chatroom);
    chatlist.setChatrooms(map);
	assertEquals(1, chatlist.size());
  }

  @Test
  public void getChatroomByGenreTest() {
    ChatList chatlist = new ChatList();
    ChatRoom chatroom = new ChatRoom();
    chatroom.setGenre(Genre.BLUES);
    Map<String, ChatRoom> map = new HashMap<>();
    map.put("blues", chatroom);
    chatlist.setChatrooms(map);
	assertEquals(chatroom, chatlist.getChatroomByGenre(Genre.BLUES));
  }
}
