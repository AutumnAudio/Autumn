import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import models.ChatRoom;
import models.Message;
import models.Song;
import models.User;

public class ChatRoomTest {
  @Test
  public void addParticipantTest() {
    ChatRoom chatroom = new ChatRoom();
    Map<String, User> participants = new HashMap<>();
    chatroom.setParticipant(participants);
    assertEquals(0, chatroom.getParticipant().size());
    User user1 = new User();
    user1.setUsername("test");
    chatroom.addParticipant(user1);
    assertEquals(1, chatroom.getParticipant().size());
  }

  @Test
  public void addMessageTest() {
    ChatRoom chatroom = new ChatRoom();
    List<Message> chat = new ArrayList<>();
    chatroom.setChat(chat);
    assertEquals(0, chatroom.getChat().size());
    Message msg = new Message();
    chatroom.addMessage(msg);
    assertEquals(1, chatroom.getChat().size());
  }

  @Test
  public void addSongTest() {
    ChatRoom chatroom = new ChatRoom();
    List<Song> playlist = new ArrayList<>();
    chatroom.setPlaylist(playlist);
    assertEquals(0, chatroom.getPlaylist().size());
    Song song = new Song();
    chatroom.addSong(song);
    assertEquals(1, chatroom.getPlaylist().size());
  }
}
