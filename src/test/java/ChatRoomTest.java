import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import models.ChatRoom;
import models.Message;
import models.Song;
import models.User;

public class ChatRoomTest {
  // ------------------------------- addParticipant -------------------------------- //
  @Test
  public void addParticipantTestOK() {
    ChatRoom chatroom = new ChatRoom();
    Map<String, User> participants = new HashMap<>();
    chatroom.setParticipant(participants);
    assertEquals(0, chatroom.getParticipant().size());
    User user1 = new User();
    user1.setUsername("test");
    chatroom.addParticipant(user1);
    assertEquals("OK", chatroom.addParticipant(user1));
  }

  @Test
  public void addParticipantTestNullUser() {
    ChatRoom chatroom = new ChatRoom();
    assertEquals("No user", chatroom.addParticipant(null));
  }

  @Test
  public void addParticipantTestNullUsername() {
    ChatRoom chatroom = new ChatRoom();
    User mockUser = mock(User.class);
    when(mockUser.getUsername()).thenReturn(null);
    assertEquals("No user", chatroom.addParticipant(mockUser));
  }

  @Test
  public void addParticipantTestEmptyUsername() {
    ChatRoom chatroom = new ChatRoom();
    User mockUser = mock(User.class);
    when(mockUser.getUsername()).thenReturn("");
    assertEquals("No user", chatroom.addParticipant(mockUser));
  }

  // ------------------------------- addMessage -------------------------------- //
  @Test
  public void addMessageTestOK() {
    ChatRoom chatroom = new ChatRoom();
    List<Message> chat = new ArrayList<>();
    chatroom.setChat(chat);
    assertEquals(0, chatroom.getChat().size());
    Message msg = new Message();
    msg.setMessage("hello");
    assertEquals("OK", chatroom.addMessage(msg));
  }

  @Test
  public void addMessageTestNullMessage() {
    ChatRoom chatroom = new ChatRoom();
    assertEquals("No message", chatroom.addMessage(null));
  }

  @Test
  public void addMessageTestNullMessageContent() {
    ChatRoom chatroom = new ChatRoom();
    Message mockMsg = mock(Message.class);
    when(mockMsg.getMessage()).thenReturn(null);
    chatroom.addMessage(mockMsg);
    assertEquals("No message", chatroom.addMessage(mockMsg));
  }

  @Test
  public void addMessageTestEmptyMessageContent() {
    ChatRoom chatroom = new ChatRoom();
    Message mockMsg = mock(Message.class);
    when(mockMsg.getMessage()).thenReturn("");
    assertEquals("No message", chatroom.addMessage(mockMsg));
  }

  // ------------------------------- addSong -------------------------------- //
  @Test
  public void addSongTestOK() {
    ChatRoom chatroom = new ChatRoom();
    List<Song> playlist = new ArrayList<>();
    chatroom.setPlaylist(playlist);
    assertEquals(0, chatroom.getPlaylist().size());
    Song song = new Song();
    song.setName("song");
    assertEquals("OK", chatroom.addSong(song));
  }

  @Test
  public void addSongTestNullSong() {
    ChatRoom chatroom = new ChatRoom();
    assertEquals("No song", chatroom.addSong(null));
  }

  @Test
  public void addSongTestNullSongName() {
    ChatRoom chatroom = new ChatRoom();
    Song mockSong = mock(Song.class);
    when(mockSong.getName()).thenReturn(null);
    assertEquals("No song", chatroom.addSong(mockSong));
  }

  @Test
  public void addSongTestEmptySongName() {
    ChatRoom chatroom = new ChatRoom();
    Song mockSong = mock(Song.class);
    when(mockSong.getName()).thenReturn("");
    assertEquals("No song", chatroom.addSong(mockSong));
  }

}
