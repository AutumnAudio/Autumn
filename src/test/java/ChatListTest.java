import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import models.ChatList;
import models.ChatRoom;
import models.Genre;
import models.Song;
import models.User;

public class ChatListTest {

  String spotifyToken = "BQCRMQoge3BUYgzoM05a0KdxcLcHENyDn90zgE4pwq5Cl-ykDV1sZIVmbWOdsubDbyvmLLNApaWbSrmadtCK4XRMw4q0Og0YLOh-6aDylQBnWRlJHRxIb7-0-09kyYq7XjKJEzrnqG0sz9J6U3OuJw5Al2G21kVh64D0Xyt0zuwAg7KAF_3SJRhfdtTlAgw0E6SkgJsZYM4kvI0EGhWv0qseUfrE8Im53S9jf7U4WpQ";
  String spotifyRefreshToken = "AQC4PDMZlGf7HXG5OZV82LDcnjLyzSCv74HGw4pL8QrsSyvVYMYlkErQ6N5nu_99J_l4E9ScIq7cW5knr4mFBm6b3sHMEclSk9c4F1-TAKffO8ds2JgJj9uOC00-LcqTS1E";

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
	user.setSpotifyToken(spotifyToken);
	user.setSpotifyRefreshToken(spotifyRefreshToken);
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
  public void geChatroomByGenreTest() {
    ChatList chatlist = new ChatList();
    ChatRoom chatroom = new ChatRoom();
    chatroom.setGenre(Genre.BLUES);
    Map<String, ChatRoom> map = new HashMap<>();
    map.put("blues", chatroom);
    chatlist.setChatrooms(map);
	assertEquals(chatroom, chatlist.getChatroomByGenre(Genre.BLUES));
  }
}
