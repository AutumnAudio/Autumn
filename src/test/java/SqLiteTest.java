import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.ChatList;
import models.ChatRoom;
import models.Genre;
import models.Message;
import models.MyApi;
import models.SqLite;
import models.User;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import static org.mockito.Mockito.*;


@TestMethodOrder(OrderAnnotation.class) 
public class SqLiteTest {

  static SqLite db = new SqLite();
  
  @BeforeEach
  public void beforeEach() {
    db.start();
    db.clear();  
  }

  //----------------------------- InsertAuthenticatedUser --------------------------------------- //
  @Test
  public void testInsertAuthenticatedUserOK() {
    String res = db.insertAuthenticatedUser("m", "s", "r", "s");
    assertEquals("OK", res);
  }

  @Test
  public void testInsertAuthenticatedUserNullUsername() {
    String res = db.insertAuthenticatedUser(null, "spotify_token", "refresh_token", "sessionId");
    assertEquals("No username", res);
  }

  @Test
  public void testInsertAuthenticatedUserEmptyUsername() {
    String res = db.insertAuthenticatedUser("", "spotify_token", "refresh_token", "sessionId");
    assertEquals("No username", res);
  }

  @Test
  public void testInsertAuthenticatedUserNullToken() {
    String res = db.insertAuthenticatedUser("mary", null, "refresh_token", "sessionId");
    assertEquals("No token", res);
  }

  @Test
  public void testInsertAuthenticatedUserEmptyToken() {
    String res = db.insertAuthenticatedUser("mary", "", "refresh_token", "sessionId");
    assertEquals("No token", res);
  }

  @Test
  public void testInsertAuthenticatedUserNullRefreshToken() {
    String res = db.insertAuthenticatedUser("mary", "spotify_token", null, "sessionId");
    assertEquals("No refresh token", res);
  }

  @Test
  public void testInsertAuthenticatedUserEmptyRefreshToken() {
    String res = db.insertAuthenticatedUser("mary", "spotify_token", "", "sessionId");
    assertEquals("No refresh token", res);
  }

  @Test
  public void testInsertAuthenticatedUserNullSessionId() {
    String res = db.insertAuthenticatedUser("mary", "spotify_token", "refresh_token", null);
    assertEquals("No session id", res);
  }

  @Test
  public void testInsertAuthenticatedUserEmptySessionId() {
    String res = db.insertAuthenticatedUser("mary", "spotify_token", "refresh_token", "");
    assertEquals("No session id", res);
  }

  //----------------------------- UpdateUserAttribute --------------------------------------- //
  @Test
  public void testUpdateUserAttributeOK() {
    db.insertAuthenticatedUser("mary", "spotify_token", "refresh_token", "sessionId");
    String res = db.updateUserAttribute("spotify_token", "n", "m");
    assertEquals("OK", res);
  }

  @Test
  public void testUpdateUserAttributeNullAttribute() {
    db.insertAuthenticatedUser("mary", "spotify_token", "refresh_token", "sessionId");
    String res = db.updateUserAttribute(null, "new_token", "mary");
    assertEquals("No attribute", res);
  }

  @Test
  public void testUpdateUserAttributeEmptyAttribute() {
    db.insertAuthenticatedUser("mary", "spotify_token", "refresh_token", "sessionId");
    String res = db.updateUserAttribute("", "new_token", "mary");
    assertEquals("No attribute", res);
  }

  @Test
  public void testUpdateUserAttributeNullValue() {
    db.insertAuthenticatedUser("mary", "spotify_token", "refresh_token", "sessionId");
    String res = db.updateUserAttribute("spotify_token", null, "mary");
    assertEquals("No value", res);
  }

  @Test
  public void testUpdateUserAttributeEmptyValue() {
    db.insertAuthenticatedUser("mary", "spotify_token", "refresh_token", "sessionId");
    String res = db.updateUserAttribute("spotify_token", "", "mary");
    assertEquals("No value", res);
  }

  @Test
  public void testUpdateUserAttributeNullUsername() {
    db.insertAuthenticatedUser("mary", "spotify_token", "refresh_token", "sessionId");
    String res = db.updateUserAttribute("spotify_token", "new_token", null);
    assertEquals("No username", res);
  }

  @Test
  public void testUpdateUserAttributeEmptyUsername() {
    db.insertAuthenticatedUser("mary", "spotify_token", "refresh_token", "sessionId");
    String res = db.updateUserAttribute("spotify_token", "new_token", "");
    assertEquals("No username", res);
  }

//----------------------------- InsertUserwithGenre --------------------------------------- //
  @Test
  public void testInsertUserwithGenreOK() {
    String res = db.insertUserwithGenre("m", "blues");
    assertEquals("OK", res);
  }

  @Test
  public void testInsertUserwithGenreNullUsername() {
    String res = db.insertUserwithGenre(null, "blues");
    assertEquals("No username", res);
  }

  @Test
  public void testInsertUserwithGenreEmptyUsername() {
    String res = db.insertUserwithGenre("", "blues");
    assertEquals("No username", res);
  }

  @Test
  public void testInsertUserwithGenreNullGenre() {
    String res = db.insertUserwithGenre("mary", null);
    assertEquals("No genre", res);
  }

  @Test
  public void testInsertUserwithGenreEmptyGenre() {
    String res = db.insertUserwithGenre("mary", "");
    assertEquals("No genre", res);
  }

  //---------------------------------- GetUserByName ----------------------------------------- //
  @Test
  public void testGetUserByName() {
    db.insertAuthenticatedUser("m", "spotify_token", "refresh_token", "sessionId");
    assertEquals("m", db.getUserByName("m").getUsername());
  }
  @Test
  public void testGetUserByNameNullUsername() {
    assertNull(db.getUserByName(null).getUsername());
  }
  @Test
  public void testGetUserByNameEmptyUsername() {
    assertNull(db.getUserByName("").getUsername());
  }
  
  //-------------------------------- AuthenticateUser ---------------------------------------- //
  @Test
  public void testAuthenticateUserOld() {
    MyApi mockApi = mock(MyApi.class);
    Map<String, String> map = new HashMap<>();
    map.put("access_token", "t");
    map.put("refresh_token", "r");
    when(mockApi.getSpotifyTokenFromCode("c")).thenReturn(map);
    when(mockApi.getEmailFromSpotifyToken("t")).thenReturn("e");
    db.setApi(mockApi);
    db.insertAuthenticatedUser("e", "s", "r", "s");
    assertEquals("User exists", db.authenticateUser("c", "s"));
  }

  @Test
  public void testAuthenticateUserNew() {
    MyApi mockApi = mock(MyApi.class);
    Map<String, String> map = new HashMap<>();
    map.put("access_token", "token");
    map.put("refresh_token", "refreshToken");
    when(mockApi.getSpotifyTokenFromCode("code")).thenReturn(map);
    db.setApi(mockApi);
    assertEquals("New user", db.authenticateUser("code", "sessionId"));
  }

  @Test
  public void testAuthenticateUserNullCode() {
    assertEquals("No code", db.authenticateUser(null, "sessionId"));
  }

  @Test
  public void testAuthenticateUserEmptyCode() {
    assertEquals("No code", db.authenticateUser("", "sessionId"));
  }

  @Test
  public void testAuthenticateUserNoSessionId() {
    assertEquals("No session id", db.authenticateUser("code", null));
  }

  @Test
  public void testAuthenticateUserEmptySessionId() {
    assertEquals("No session id", db.authenticateUser("code", ""));
  }

  //--------------------------------- GetUserCount ------------------------------------------ //
  @Test
  public void testGetUserCountOK() {
    db.insertAuthenticatedUser("m", "spotify_token", "refresh_token", "sessionId");
    assertEquals(1, db.getUserCount("m"));
  }

  @Test
  public void testGetUserCountNullUsername() {
    assertEquals(0, db.getUserCount(null));
  }

  @Test
  public void testGetUserCountEmptyUsername() {
    assertEquals(0, db.getUserCount(""));
  }

  //--------------------------------- GetGenreUser ------------------------------------------ //
  @Test
  public void testGetGenreUserOK() {
    db.insertUserwithGenre("m", "jazz");
    assertEquals("jazz", db.getGenreUser("m"));
  }

  @Test
  public void testGetGenreUserNullUsername() {
    assertNull(db.getGenreUser(null));
  }

  @Test
  public void testGetGenreUserEmptyUsername() {
    assertNull(db.getGenreUser(""));
  }

  //------------------------------- GetUserBySessionId --------------------------------------- //
  @Test
  public void testGetUserBySessionIdOK() {
    db.insertAuthenticatedUser("m", "spotify_token", "refresh_token", "s");
    assertEquals("m", db.getUserBySessionId("s").getUsername());
  }

  @Test
  public void testGetUserBySessionIdNullSessionId() {
    assertNull(db.getUserBySessionId(null));
  }

  @Test
  public void testGetUserBySessionIdEmptySessionId() {
    assertNull(db.getUserBySessionId(""));
  }

  //---------------------------------- InsertChatRoom ----------------------------------------- //
  @Test
  public void testInsertChatRoomOK() {
    String res = db.insertChatRoom(Genre.JAZZ, "l", "p");
    assertEquals("OK", res);
  }

  @Test
  public void testInsertChatRoomNullGenre() {
    String res = db.insertChatRoom(null, "/jazz-links", "jazz-playlist");
    assertEquals("No genre", res);
  }

  @Test
  public void testInsertChatRoomNullLink() {
    String res = db.insertChatRoom(Genre.JAZZ, null, "jazz-playlist");
    assertEquals("No link", res);
  }

  @Test
  public void testInsertChatRoomEmptyLink() {
    String res = db.insertChatRoom(Genre.JAZZ, "", "jazz-playlist");
    assertEquals("No link", res);
  }

  @Test
  public void testInsertChatRoomNullPlaylist() {
    String res = db.insertChatRoom(Genre.JAZZ, "/jazz-links", null);
    assertEquals("No playlist", res);
  }

  @Test
  public void testInsertChatRoomEmptyPlaylist() {
    String res = db.insertChatRoom(Genre.JAZZ, "/jazz-links", "");
    assertEquals("No playlist", res);
  }

  //---------------------------------- InsertParticipant --------------------------------------- //
  @Test
  public void testInsertParticipantOK() {
    String res = db.insertParticipant(Genre.JAZZ, "m", "a", "a", "a");
    assertEquals("OK", res);
  }

  @Test
  public void testInsertParticipantNullGenre() {
    String res = db.insertParticipant(null, "mary", "a", "a", "a");
    assertEquals("No genre", res);
  }

  @Test
  public void testInsertParticipantNullUsername() {
    String res = db.insertParticipant(Genre.JAZZ, null, "a", "a", "a");
    assertEquals("No username", res);
  }

  @Test
  public void testInsertParticipantEmptyUsername() {
    String res = db.insertParticipant(Genre.JAZZ, "", "a", "a", "a");
    assertEquals("No username", res);
  }

  @Test
  public void testInsertParticipantNullToken() {
    String res = db.insertParticipant(Genre.JAZZ, "mary", null, "a", "a");
    assertEquals("No token", res);
  }

  @Test
  public void testInsertParticipantEmptyToken() {
    String res = db.insertParticipant(Genre.JAZZ, "mary", "", "a", "a");
    assertEquals("No token", res);
  }

  @Test
  public void testInsertParticipantNullRefreshToken() {
    String res = db.insertParticipant(Genre.JAZZ, "mary", "a", null, "a");
    assertEquals("No refresh token", res);
  }

  @Test
  public void testInsertParticipantEmptyRefreshToken() {
    String res = db.insertParticipant(Genre.JAZZ, "mary", "a", "", "a");
    assertEquals("No refresh token", res);
  }

  @Test
  public void testInsertParticipantNullSessionId() {
    String res = db.insertParticipant(Genre.JAZZ, "mary", "a", "a", null);
    assertEquals("No session id", res);
  }

  @Test
  public void testInsertParticipantEmptySessionId() {
    String res = db.insertParticipant(Genre.JAZZ, "mary", "a", "a", "");
    assertEquals("No session id", res);
  }

  //---------------------------------- RemoveParticipant --------------------------------------- //  
  @Test
  public void testRemoveParticipantOK() {
    db.insertParticipant(Genre.JAZZ, "m", "a", "a", "a");
    assertEquals("OK", db.removeParticipant(Genre.JAZZ, "m"));
  }

  @Test
  public void testRemoveParticipantNullGenre() {
    db.insertParticipant(Genre.JAZZ, "m", "a", "a", "a");
    assertEquals("No genre", db.removeParticipant(null, "mary"));
  }

  @Test
  public void testRemoveParticipantNullUsername() {
    db.insertParticipant(Genre.JAZZ, "m", "a", "a", "a");
    assertEquals("No username", db.removeParticipant(Genre.JAZZ, null));
  }

  @Test
  public void testRemoveParticipantEmptyUsername() {
    db.insertParticipant(Genre.JAZZ, "m", "a", "a", "a");
    assertEquals("No username", db.removeParticipant(Genre.JAZZ, ""));
  }

  //---------------------------------- RemoveUserGenre ---------------------------------------- //  
  @Test
  public void testRemoveUserGenreOK() {
    db.insertUserwithGenre("m", "blues");
    assertEquals("blues", db.getGenreUser("m"));
    assertEquals("OK", db.removeUserGenre("m"));
  }

  @Test
  public void testRemoveUserGenreNullUsername() {
    assertEquals("No username", db.removeUserGenre(null));
  }

  @Test
  public void testRemoveUserGenreEmptyUsername() {
    assertEquals("No username", db.removeUserGenre(""));
  }

  //--------------------------------- GetChatRoomParticpant ------------------------------------ //
  @Test
  public void testGetChatRoomParticipantOK() {
    db.insertParticipant(Genre.JAZZ, "mary", "token", "refresh", "session");
    assertEquals(1, db.getChatRoomParticipant(Genre.JAZZ).size());
  }

  @Test
  public void testGetChatRoomParticipantNullGenre() {
    db.insertParticipant(Genre.JAZZ, "mary", "token", "refresh", "session");
    assertNull(db.getChatRoomParticipant(null));
  }

  //-------------------------------------- InsertSong ------------------------------------------ //  
  @Test
  public void testInsertSongOK() {
    String res = db.insertSong("m", Time.valueOf(LocalTime.now()), Genre.BLUES, "b");
    assertEquals("OK", res);
  }

  @Test
  public void testInsertSongNullUsername() {
    String res = db.insertSong(null, Time.valueOf(LocalTime.now()), Genre.BLUES, "blues-song");
    assertEquals("No username", res);
  }

  @Test
  public void testInsertSongEmptyUsername() {
    String res = db.insertSong("", Time.valueOf(LocalTime.now()), Genre.BLUES, "blues-song");
    assertEquals("No username", res);
  }

  @Test
  public void testInsertSongNullTimeShared() {
    String res = db.insertSong("mary", null, Genre.BLUES, "blues-song");
    assertEquals("No time", res);
  }

  @Test
  public void testInsertSongNullGenre() {
    String res = db.insertSong("mary", Time.valueOf(LocalTime.now()), null, "blues-song");
    assertEquals("No genre", res);
  }

  @Test
  public void testInsertSongNullSong() {
    String res = db.insertSong("mary", Time.valueOf(LocalTime.now()), Genre.BLUES, null);
    assertEquals("No song", res);
  }

  @Test
  public void testInsertSongEmptySong() {
    String res = db.insertSong("mary", Time.valueOf(LocalTime.now()), Genre.BLUES, "");
    assertEquals("No song", res);
  }
  
  //----------------------------------- GetChatRoomPlaylist -------------------------------------- //
  @Test
  public void testGetChatRoomPlaylistOK() {
    db.insertSong("m", Time.valueOf(LocalTime.now()), Genre.JAZZ, "b");
    assertEquals(1, db.getChatRoomPlaylist(Genre.JAZZ).size());
  }

  @Test
  public void testGetChatRoomPlaylistNullGenre() {
    assertNull(db.getChatRoomPlaylist(null));
  }

  //------------------------------------- InsertSession ----------------------------------------- // 
  @Test
  public void testInsertSessionOK() {
    assertEquals("OK", db.insertSession("0", "1"));
  }

  @Test
  public void testInsertSessionNullTime() {
    assertEquals("No time", db.insertSession(null, "session1"));
  }

  @Test
  public void testInsertSessionEmptyTime() {
    assertEquals("No time", db.insertSession("", "session1"));
  }

  @Test
  public void testInsertSessionNullSessionId() {
    assertEquals("No session id", db.insertSession("0912", null));
  }

  @Test
  public void testInsertSessionEmptySessionId() {
    assertEquals("No session id", db.insertSession("0912", ""));
  }

  //----------------------------------- UserJoin ---------------------------------------- // 
  @Test
  public void UserJoinOldOK() {
    ChatList mockChatlist = mock(ChatList.class);
    User user1 = new User();
    user1.setUsername("t");
    User user2 = new User();
    user2.setUsername("testing2");
    Map<String, User> map = new HashMap<>();
    map.put("t", user1);
    map.put("testing2", user2);
    ChatRoom chatroom = new ChatRoom();
    chatroom.setParticipant(map);
    when(mockChatlist.getChatroomByGenre(Genre.BLUES)).thenReturn(chatroom);
    db.userJoin(Genre.BLUES, "t", mockChatlist);
    assertNull(db.getGenreUser("t"));
  }

  @Test
  public void testUserJoinNewOK() {
    ChatList mockChatlist = mock(ChatList.class);
    User user1 = new User();
    user1.setUsername("testing1");
    User user2 = new User();
    user2.setUsername("testing2");
    Map<String, User> map = new HashMap<>();
    map.put("testing2", user2);
    ChatRoom chatroom = new ChatRoom();
    chatroom.setParticipant(map);
    when(mockChatlist.getChatroomByGenre(Genre.BLUES)).thenReturn(chatroom);
    db.userJoin(Genre.BLUES, "testing1", mockChatlist);
    assertEquals("blues", db.getGenreUser("testing1"));
  }

  @Test
  public void testUserJoinNullGenre() {
    assertNull(db.userJoin(null, "testing1", new ChatList()));
  }

  @Test
  public void testUserJoinNullUsername() {
    assertNull(db.userJoin(Genre.BLUES, null, new ChatList()));
  }

  @Test
  public void testUserJoinEmptyUsername() {
    assertNull(db.userJoin(Genre.BLUES, "", new ChatList()));
  }

  @Test
  public void testUserJoinNullChatlist() {
    assertNull(db.userJoin(Genre.BLUES, "testing1", null));
  }

  //----------------------------------- UserSend ---------------------------------------- //
  @Test
  public void testUserSendOK() {
    db.insertUserwithGenre("t", "blues");
    ChatList mockChatlist = mock(ChatList.class);
    ChatRoom chatroom = new ChatRoom();
    List<Message> chat = new ArrayList<>();
    //chatroom.setParticipant(map);
    chatroom.setChat(chat);
    when(mockChatlist.getChatroomByGenre(Genre.BLUES)).thenReturn(chatroom);
    db.userSend("t", "h", mockChatlist);
    assertEquals(1, chatroom.getChat().size());
  }

  @Test
  public void testUserSendNoGenreOK() {
    ChatList mockChatlist = mock(ChatList.class);
    ChatRoom chatroom = new ChatRoom();
    List<Message> chat = new ArrayList<>();
    chatroom.setChat(chat);
    when(mockChatlist.getChatroomByGenre(Genre.BLUES)).thenReturn(chatroom);
    assertNull(db.userSend("testing1", "hello", mockChatlist));
  }

  @Test
  public void testUserSendNullUsername() {
    assertNull(db.userSend(null, "hello", new ChatList()));
  }

  @Test
  public void testUserSendEmptyUsername() {
    assertNull(db.userSend("", "hello", new ChatList()));
  }

  @Test
  public void testUserSendNullText() {
    assertNull(db.userSend("testing1", null, new ChatList()));
  }

  @Test
  public void testUserSendEmptyText() {
    assertNull(db.userSend("testing1", "", new ChatList()));
  }

  @Test
  public void testUserSendNullChatlist() {
    assertNull(db.userSend("testing1", null, null));
  }

  //----------------------------------- GetLatestSession ---------------------------------------- // 
  @Test
  public void testGetLatestSession() {
    db.insertSession("0920", "session2");
    db.insertSession("1020", "session3");
    db.insertSession("0820", "session1");
    
    assertEquals("session3", db.getLatestSession());
  }

  //------------------------------------------ update -------------------------------------------- // 
  @Test
  public void testUpdate() {
    ChatList chatlist = db.update();
    assertEquals(0, chatlist.size());
    db.insertChatRoom(Genre.JAZZ, "/jazz-links", "jazz-playlist");
    
    chatlist = db.update();
    assertEquals(1, chatlist.size());
  }

  //----------------------------------------- Connect ------------------------------------------- // 
  @Test
  public void testConnect() {
    ChatList chatlist = db.update();
    assertEquals(0, chatlist.size());
    db.insertChatRoom(Genre.JAZZ, "/jazz-links", "jazz-playlist");
    
    chatlist = db.update();
    assertEquals(1, chatlist.size());
    SqLite db2 = new SqLite();
    db2.connect();
    ChatList chatlist2 = db2.update();
    assertEquals(1, chatlist2.size());
    db2.close();
  }

  @AfterEach
  public void afterEach() {
    db.clear();
    
    db.close();	  
  }
}