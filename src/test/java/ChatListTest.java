import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import models.ChatList;
import models.ChatRoom;
import models.Genre;
import models.User;

public class ChatListTest {

  @Test
  public void refreshChatListTestWithToken() {
    ChatList chatlist = new ChatList();
    ChatRoom chatroom = new ChatRoom();
    Map<String, ChatRoom> map = new HashMap<>();
    map.put("testRoom", chatroom);
    chatlist.setChatrooms(map);
    Map<String, User> participants = new HashMap<>();
    chatroom.setParticipant(participants);
    User mockUser = mock(User.class);
    when(mockUser.getSpotifyToken()).thenReturn("token");
    when(mockUser.refreshRecentlyPlayed()).thenReturn("OK");
    when(mockUser.refreshCurrentlyPlaying()).thenReturn("OK");
    doNothing().when(mockUser).setApi(null);
    chatroom.addParticipant(mockUser);
    chatlist.refreshChatList();
    assertEquals(1, chatlist.size());
    assertEquals(1, chatroom.getNumParticipants());
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
    User mockUser = mock(User.class);
    when(mockUser.getSpotifyToken()).thenReturn("null");
    chatroom.addParticipant(mockUser);
    chatlist.refreshChatList();
    assertEquals(1, chatlist.size());
    assertEquals(1, chatroom.getNumParticipants());
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

  @Test
  public void getTotalParticipantsTest() {
    ChatList chatlist = new ChatList();
    ChatRoom chatroom1 = new ChatRoom();
    Map<String, ChatRoom> map = new HashMap<>();
    chatlist.setChatrooms(map);
    map.put("testRoom1", chatroom1);
    Map<String, User> participants1 = new HashMap<>();
    chatroom1.setParticipant(participants1);
    User user1 = new User();
    user1.setUsername("user1");
    chatroom1.addParticipant(user1);
    assertEquals(1, chatlist.getTotalParticipants());
    ChatRoom chatroom2 = new ChatRoom();
    map.put("testRoom2", chatroom2);
    Map<String, User> participants2 = new HashMap<>();
    chatroom2.setParticipant(participants2);
    User user2 = new User();
    user2.setUsername("user2");
    User user3 = new User();
    user3.setUsername("user3");
    chatroom2.addParticipant(user2);
    chatroom2.addParticipant(user3);
    assertEquals(3, chatlist.getTotalParticipants());
  }
}
