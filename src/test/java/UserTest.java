import models.ChatList;
import models.ChatRoom;
import models.Genre;
import models.MyApi;
import models.Song;
import models.SqLite;
import models.User;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.PagingCursorbased;
import com.wrapper.spotify.model_objects.specification.PlayHistory;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hc.core5.http.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;


@TestMethodOrder(OrderAnnotation.class) 
public class UserTest {
  
  //String refreshToken = SpotifyAccount.getRefreshToken();
  //String token = Login.refreshSpotifyToken(refreshToken);
  //SpotifyApi api = new SpotifyApi.Builder()
  //        .setAccessToken(Login.refreshSpotifyToken(refreshToken))
  //        .build();
  
  SqLite db = new SqLite();
  
  @Mock
  PagingCursorbased<PlayHistory> pcb;
  
  @BeforeEach
  public void beforeEach() {
    db.start();
    
  }

// ----------------------------- Refresh Recently Played --------------------------------------------- //
  @Test
  public void testResfreshRecentlyPlayed() throws ParseException, SpotifyWebApiException, IOException {
    MyApi mockAPI = mock(MyApi.class);
	PlayHistory[] history = new PlayHistory[10];
	for (int i = 0; i < history.length; i++) {
      TrackSimplified mockTrack = mock(TrackSimplified.class);
      when(mockTrack.getName()).thenReturn("a");
      ArtistSimplified[] artistList = new ArtistSimplified[1];
      ArtistSimplified artist = mock(ArtistSimplified.class);
      when(artist.getName()).thenReturn("test artist");
      artistList[0] = artist;
      when(mockTrack.getArtists()).thenReturn(artistList);
      when(mockTrack.getUri()).thenReturn("song uri");
      
      PlayHistory mockHistory = mock(PlayHistory.class);
      when(mockHistory.getTrack()).thenReturn(mockTrack);
      history[i] = mockHistory;
    }
    when(mockAPI.recentlyPlayed()).thenReturn(history);
    User user = new User(mockAPI);
    String refreshToken = "refresh";
    when(mockAPI.refreshSpotifyToken("refresh")).thenReturn("newToken");
    user.setSpotifyRefreshToken(refreshToken);
    assertEquals("OK", user.refreshRecentlyPlayed());
  }

  @Test
  public void testResfreshRecentlyPlayedLessThan10() throws ParseException, SpotifyWebApiException, IOException {
    MyApi mockAPI = mock(MyApi.class);
	PlayHistory[] history = new PlayHistory[9];
	for (int i = 0; i < history.length; i++) {
      TrackSimplified mockTrack = mock(TrackSimplified.class);
      when(mockTrack.getName()).thenReturn("song" + i);
      ArtistSimplified[] artistList = new ArtistSimplified[1];
      ArtistSimplified artist = mock(ArtistSimplified.class);
      when(artist.getName()).thenReturn("test artist");
      artistList[0] = artist;
      when(mockTrack.getArtists()).thenReturn(artistList);
      when(mockTrack.getUri()).thenReturn("song uri");
      
      PlayHistory mockHistory = mock(PlayHistory.class);
      when(mockHistory.getTrack()).thenReturn(mockTrack);
      history[i] = mockHistory;
    }
    when(mockAPI.recentlyPlayed()).thenReturn(history);
    User user = new User(mockAPI);
    String refreshToken = "refresh";
    when(mockAPI.refreshSpotifyToken("refresh")).thenReturn("newToken");
    user.setSpotifyRefreshToken(refreshToken);
    assertEquals("Number of items return not matched.", user.refreshRecentlyPlayed());
  }

  @Test
  public void testResfreshRecentlyPlayedMoreThan10() throws ParseException, SpotifyWebApiException, IOException {
    MyApi mockAPI = mock(MyApi.class);
	PlayHistory[] history = new PlayHistory[11];
	for (int i = 0; i < history.length; i++) {
      TrackSimplified mockTrack = mock(TrackSimplified.class);
      when(mockTrack.getName()).thenReturn("song" + i);
      ArtistSimplified[] artistList = new ArtistSimplified[1];
      ArtistSimplified artist = mock(ArtistSimplified.class);
      when(artist.getName()).thenReturn("test artist");
      artistList[0] = artist;
      when(mockTrack.getArtists()).thenReturn(artistList);
      when(mockTrack.getUri()).thenReturn("song uri");
      
      PlayHistory mockHistory = mock(PlayHistory.class);
      when(mockHistory.getTrack()).thenReturn(mockTrack);
      history[i] = mockHistory;
    }
    when(mockAPI.recentlyPlayed()).thenReturn(history);
    User user = new User(mockAPI);
    String refreshToken = "refresh";
    when(mockAPI.refreshSpotifyToken("refresh")).thenReturn("newToken");
    user.setSpotifyRefreshToken(refreshToken);
    assertEquals("Number of items return not matched.", user.refreshRecentlyPlayed());
  }

  @Test
  public void testResfreshRecentlyPlayedNullTrack() throws ParseException, SpotifyWebApiException, IOException {
    MyApi mockAPI = mock(MyApi.class);
	PlayHistory[] history = new PlayHistory[10];
	for (int i = 0; i < history.length; i++) {
      PlayHistory mockHistory = mock(PlayHistory.class);
      when(mockHistory.getTrack()).thenReturn(null);
      history[i] = mockHistory;
    }
    when(mockAPI.recentlyPlayed()).thenReturn(history);
    User user = new User(mockAPI);
    String refreshToken = "refresh";
    when(mockAPI.refreshSpotifyToken("refresh")).thenReturn("newToken");
    user.setSpotifyRefreshToken(refreshToken);
    assertEquals("List contains null track", user.refreshRecentlyPlayed());
  }

  @Test
  public void testResfreshRecentlyPlayedNullArtist() throws ParseException, SpotifyWebApiException, IOException {
    MyApi mockAPI = mock(MyApi.class);
	PlayHistory[] history = new PlayHistory[10];
	for (int i = 0; i < history.length; i++) {
      TrackSimplified mockTrack = mock(TrackSimplified.class);
      when(mockTrack.getName()).thenReturn("song" + i);
      when(mockTrack.getArtists()).thenReturn(null);
      when(mockTrack.getUri()).thenReturn("song uri");
      
      PlayHistory mockHistory = mock(PlayHistory.class);
      when(mockHistory.getTrack()).thenReturn(mockTrack);
      history[i] = mockHistory;
    }
    when(mockAPI.recentlyPlayed()).thenReturn(history);
    User user = new User(mockAPI);
    String refreshToken = "refresh";
    when(mockAPI.refreshSpotifyToken("refresh")).thenReturn("newToken");
    user.setSpotifyRefreshToken(refreshToken);
    assertEquals("List contains null artist list", user.refreshRecentlyPlayed());
  }

  @Test
  public void testResfreshRecentlyPlayedNullTrackName() throws ParseException, SpotifyWebApiException, IOException {
    MyApi mockAPI = mock(MyApi.class);
	PlayHistory[] history = new PlayHistory[10];
	for (int i = 0; i < history.length; i++) {
      TrackSimplified mockTrack = mock(TrackSimplified.class);
      when(mockTrack.getName()).thenReturn(null);
      ArtistSimplified[] artistList = new ArtistSimplified[1];
      ArtistSimplified artist = mock(ArtistSimplified.class);
      when(artist.getName()).thenReturn("test artist");
      artistList[0] = artist;
      when(mockTrack.getArtists()).thenReturn(artistList);
      when(mockTrack.getUri()).thenReturn("song uri");
      
      PlayHistory mockHistory = mock(PlayHistory.class);
      when(mockHistory.getTrack()).thenReturn(mockTrack);
      history[i] = mockHistory;
    }
    when(mockAPI.recentlyPlayed()).thenReturn(history);
    User user = new User(mockAPI);
    String refreshToken = "refresh";
    when(mockAPI.refreshSpotifyToken("refresh")).thenReturn("newToken");
    user.setSpotifyRefreshToken(refreshToken);
    assertEquals("List contains invalid track name", user.refreshRecentlyPlayed());
  }

  @Test
  public void testResfreshRecentlyPlayedEmptyTrackName() throws ParseException, SpotifyWebApiException, IOException {
    MyApi mockAPI = mock(MyApi.class);
	PlayHistory[] history = new PlayHistory[10];
	for (int i = 0; i < history.length; i++) {
      TrackSimplified mockTrack = mock(TrackSimplified.class);
      when(mockTrack.getName()).thenReturn("");
      ArtistSimplified[] artistList = new ArtistSimplified[1];
      ArtistSimplified artist = mock(ArtistSimplified.class);
      when(artist.getName()).thenReturn("test artist");
      artistList[0] = artist;
      when(mockTrack.getArtists()).thenReturn(artistList);
      when(mockTrack.getUri()).thenReturn("song uri");
      
      PlayHistory mockHistory = mock(PlayHistory.class);
      when(mockHistory.getTrack()).thenReturn(mockTrack);
      history[i] = mockHistory;
    }
    when(mockAPI.recentlyPlayed()).thenReturn(history);
    User user = new User(mockAPI);
    String refreshToken = "refresh";
    when(mockAPI.refreshSpotifyToken("refresh")).thenReturn("newToken");
    user.setSpotifyRefreshToken(refreshToken);
    assertEquals("List contains invalid track name", user.refreshRecentlyPlayed());
  }
  
//----------------------------- Refresh Currently Playing --------------------------------------------- //
  @Test
  public void testResfreshCurrentlyPlayingNull() throws ParseException, SpotifyWebApiException, IOException {
    MyApi mockAPI = mock(MyApi.class);
    when(mockAPI.currentlyPlaying()).thenReturn(null);
    User user = new User(mockAPI);
    String refreshToken = "refresh";
    when(mockAPI.refreshSpotifyToken("refresh")).thenReturn("newToken");
    user.setSpotifyRefreshToken(refreshToken);
	assertEquals("Currently not playing any tracks", user.refreshCurrentlyPlaying());
  }

  @Test
  public void testResfreshCurrentlyPlayingNotNull() throws ParseException, SpotifyWebApiException, IOException {
    MyApi mockAPI = mock(MyApi.class);
    CurrentlyPlaying currentlyPlaying = mock(CurrentlyPlaying.class);
    when(mockAPI.currentlyPlaying()).thenReturn(currentlyPlaying);
    Track mockTrack = mock(Track.class);
    when(currentlyPlaying.getItem()).thenReturn(mockTrack);
    when(mockTrack.getName()).thenReturn("a");
    ArtistSimplified[] artistList = new ArtistSimplified[1];
    ArtistSimplified artist = mock(ArtistSimplified.class);
    when(artist.getName()).thenReturn("test artist");
    artistList[0] = artist;
    when(mockTrack.getArtists()).thenReturn(artistList);
    when(mockTrack.getUri()).thenReturn("song uri");
    User user = new User(mockAPI);
    String refreshToken = "refresh";
    when(mockAPI.refreshSpotifyToken("refresh")).thenReturn("newToken");
    user.setSpotifyRefreshToken(refreshToken);
	assertEquals("OK", user.refreshCurrentlyPlaying());
  }

  @Test
  public void testResfreshCurrentlyPlayingNullTrack() throws ParseException, SpotifyWebApiException, IOException {
    MyApi mockAPI = mock(MyApi.class);
    CurrentlyPlaying currentlyPlaying = mock(CurrentlyPlaying.class);
    when(mockAPI.currentlyPlaying()).thenReturn(currentlyPlaying);
    when(currentlyPlaying.getItem()).thenReturn(null);
    User user = new User(mockAPI);
    String refreshToken = "refresh";
    when(mockAPI.refreshSpotifyToken("refresh")).thenReturn("newToken");
    user.setSpotifyRefreshToken(refreshToken);
	assertEquals("Null track", user.refreshCurrentlyPlaying());
  }

  @Test
  public void testResfreshCurrentlyPlayingNullArtist() throws ParseException, SpotifyWebApiException, IOException {
    MyApi mockAPI = mock(MyApi.class);
    CurrentlyPlaying currentlyPlaying = mock(CurrentlyPlaying.class);
    when(mockAPI.currentlyPlaying()).thenReturn(currentlyPlaying);
    Track mockTrack = mock(Track.class);
    when(currentlyPlaying.getItem()).thenReturn(mockTrack);
    when(mockTrack.getName()).thenReturn("song name");
    ArtistSimplified[] artistList = null;
    when(mockTrack.getArtists()).thenReturn(artistList);
    when(mockTrack.getUri()).thenReturn("song uri");
    User user = new User(mockAPI);
    String refreshToken = "refresh";
    when(mockAPI.refreshSpotifyToken("refresh")).thenReturn("newToken");
    user.setSpotifyRefreshToken(refreshToken);
	assertEquals("Null artist list", user.refreshCurrentlyPlaying());
  }

  @Test
  public void testResfreshCurrentlyPlayingNullTrackName() throws ParseException, SpotifyWebApiException, IOException {
    MyApi mockAPI = mock(MyApi.class);
    CurrentlyPlaying currentlyPlaying = mock(CurrentlyPlaying.class);
    when(mockAPI.currentlyPlaying()).thenReturn(currentlyPlaying);
    Track mockTrack = mock(Track.class);
    when(currentlyPlaying.getItem()).thenReturn(mockTrack);
    when(mockTrack.getName()).thenReturn(null);
    ArtistSimplified[] artistList = new ArtistSimplified[1];
    ArtistSimplified artist = mock(ArtistSimplified.class);
    when(artist.getName()).thenReturn("test artist");
    artistList[0] = artist;
    when(mockTrack.getArtists()).thenReturn(artistList);
    when(mockTrack.getUri()).thenReturn("song uri");
    User user = new User(mockAPI);
    String refreshToken = "refresh";
    when(mockAPI.refreshSpotifyToken("refresh")).thenReturn("newToken");
    user.setSpotifyRefreshToken(refreshToken);
	assertEquals("Invalid track name", user.refreshCurrentlyPlaying());
  }

  @Test
  public void testResfreshCurrentlyPlayingEmptyTrackName() throws ParseException, SpotifyWebApiException, IOException {
    MyApi mockAPI = mock(MyApi.class);
    CurrentlyPlaying currentlyPlaying = mock(CurrentlyPlaying.class);
    when(mockAPI.currentlyPlaying()).thenReturn(currentlyPlaying);
    Track mockTrack = mock(Track.class);
    when(currentlyPlaying.getItem()).thenReturn(mockTrack);
    when(mockTrack.getName()).thenReturn("");
    ArtistSimplified[] artistList = new ArtistSimplified[1];
    ArtistSimplified artist = mock(ArtistSimplified.class);
    when(artist.getName()).thenReturn("test artist");
    artistList[0] = artist;
    when(mockTrack.getArtists()).thenReturn(artistList);
    when(mockTrack.getUri()).thenReturn("song uri");
    User user = new User(mockAPI);
    String refreshToken = "refresh";
    when(mockAPI.refreshSpotifyToken("refresh")).thenReturn("newToken");
    user.setSpotifyRefreshToken(refreshToken);
	assertEquals("Invalid track name", user.refreshCurrentlyPlaying());
  }

//---------------------------------------- Add to queue -------------------------------------------- //
  @Test
  public void testAddToQueueValidUri() throws ParseException, SpotifyWebApiException, IOException {
    MyApi mockAPI = mock(MyApi.class);
    when(mockAPI.addSong("song uri")).thenReturn(null);
    User user = new User(mockAPI);
    String refreshToken = "refresh";
    when(mockAPI.refreshSpotifyToken("refresh")).thenReturn("newToken");
    user.setSpotifyRefreshToken(refreshToken);
	assertEquals(null, user.addToQueue("spotify:track:x"));
  }

  @Test
  public void testAddToQueueshortUri() throws ParseException, SpotifyWebApiException, IOException {
    MyApi mockAPI = mock(MyApi.class);
    when(mockAPI.addSong("song uri")).thenReturn(null);
    User user = new User(mockAPI); 
    String refreshToken = "refresh";
    when(mockAPI.refreshSpotifyToken("refresh")).thenReturn("newToken");
    user.setSpotifyRefreshToken(refreshToken);
	assertEquals("invalid uri", user.addToQueue("spotifytrack:"));
  }

  @Test
  public void testAddToQueueInvalidUri() throws ParseException, SpotifyWebApiException, IOException {
    MyApi mockAPI = mock(MyApi.class);
    when(mockAPI.addSong("song uri")).thenReturn(null);
    User user = new User(mockAPI);
    String refreshToken = "refresh";
    when(mockAPI.refreshSpotifyToken("refresh")).thenReturn("newToken");
    user.setSpotifyRefreshToken(refreshToken);
	assertEquals("invalid uri", user.addToQueue("song uri"));
  }

  // -------------------------------------- share ------------------------------------//
  @Test
  public void shareTestOK() throws ParseException, SpotifyWebApiException, IOException {
	SqLite mockDb = mock(SqLite.class);
	when(mockDb.getGenreUser("testing1")).thenReturn("blues");
    User sharer = new User();
    sharer.setUsername("testing1");
    sharer.setSpotifyToken("123");
    MyApi mockApi = mock(MyApi.class);
    
    CurrentlyPlaying currentlyPlaying = mock(CurrentlyPlaying.class);
    when(mockApi.currentlyPlaying()).thenReturn(currentlyPlaying);
    Track mockTrack = mock(Track.class);
    when(currentlyPlaying.getItem()).thenReturn(mockTrack);
    when(mockTrack.getName()).thenReturn("a");
    ArtistSimplified[] artistList = new ArtistSimplified[1];
    ArtistSimplified artist = mock(ArtistSimplified.class);
    when(artist.getName()).thenReturn("test artist");
    artistList[0] = artist;
    when(mockTrack.getArtists()).thenReturn(artistList);
    when(mockTrack.getUri()).thenReturn("song uri");
    
    sharer.setApi(mockApi);
    sharer.setDb(mockDb);
    Song song = new Song();
    song.setName("song name");
    song.setUri("uri");
    sharer.setCurrentTrack(song);
    User mockSharee = mock(User.class);
    when(mockSharee.getUsername()).thenReturn("testing2");
    when(mockSharee.addToQueue("uri")).thenReturn("OK");
    
    ChatList mockChatlist = mock(ChatList.class);
    ChatRoom mockChatroom = mock(ChatRoom.class);
    when(mockChatlist.getChatroomByGenre(Genre.valueOf("BLUES"))).thenReturn(mockChatroom);
    Map<String, User> map = new HashMap<>();
    map.put("testing1", sharer);
    map.put("testing2", mockSharee);
    when(mockChatroom.getParticipant()).thenReturn(map);
    assertNotNull(sharer.share(mockChatlist));
    
  }

  @Test
  public void shareTestNullChatlist() throws ParseException, SpotifyWebApiException, IOException {
    User sharer = new User();
    assertNull(sharer.share(null));
    
  }

  @Test
  public void shareTestNullCurrentTrack() throws ParseException, SpotifyWebApiException, IOException {
	SqLite mockDb = mock(SqLite.class);
	when(mockDb.getGenreUser("testing1")).thenReturn("blues");
    User sharer = new User();
    sharer.setUsername("testing1");
    sharer.setSpotifyToken("123");
    MyApi mockApi = mock(MyApi.class);
    when(mockApi.currentlyPlaying()).thenReturn(null);
    
    sharer.setApi(mockApi);
    sharer.setDb(mockDb);
    assertNull(sharer.share(new ChatList()));
  }

  @Test
  public void shareTestNullGenreStr() throws ParseException, SpotifyWebApiException, IOException {
	SqLite mockDb = mock(SqLite.class);
	when(mockDb.getGenreUser("testing1")).thenReturn(null);
    User sharer = new User();
    sharer.setUsername("testing1");
    MyApi mockApi = mock(MyApi.class);
    
    CurrentlyPlaying currentlyPlaying = mock(CurrentlyPlaying.class);
    when(mockApi.currentlyPlaying()).thenReturn(currentlyPlaying);
    Track mockTrack = mock(Track.class);
    when(currentlyPlaying.getItem()).thenReturn(mockTrack);
    when(mockTrack.getName()).thenReturn("a");
    ArtistSimplified[] artistList = new ArtistSimplified[1];
    ArtistSimplified artist = mock(ArtistSimplified.class);
    when(artist.getName()).thenReturn("test artist");
    artistList[0] = artist;
    when(mockTrack.getArtists()).thenReturn(artistList);
    when(mockTrack.getUri()).thenReturn("song uri");
    
    sharer.setApi(mockApi);
    sharer.setDb(mockDb);
    assertNull(sharer.share(new ChatList()));
  }
  // ------------------------------ SetSpotifyRefreshToken ---------------------------//
  @Test
  public void testSetSpotifyRefreshToken() {
    User user = new User();
    db.insertAuthenticatedUser("mary", "token", "123", "sessionId");
    
    assertEquals("123", db.getUserByName("mary").getSpotifyRefreshToken());
    user.setDb(db);
    user.setUsername("mary");
    assertEquals("OK", user.setSpotifyRefreshTokenDb("new_refresh_token"));
  }

  @Test
  public void testSetSpotifyRefreshTokenNullRefreshToken() {
    User user = new User();
    assertEquals("No token", user.setSpotifyRefreshTokenDb(null));
  }

  @Test
  public void testSetSpotifyRefreshTokenEmptyRefreshToken() {
    User user = new User();
    assertEquals("No token", user.setSpotifyRefreshTokenDb(""));
  }

  //---------------------------------- RefreshSpotifyToken ---------------------------------------- //
  @Test
  public void testRefreshSpotifyTokenValid() throws Exception {
    MyApi mockAPI = mock(MyApi.class);
    User user = new User(mockAPI);
    String refreshToken = "refresh";
    when(mockAPI.refreshSpotifyToken("refresh")).thenReturn("t");
    user.setSpotifyRefreshToken(refreshToken);
    String newToken = user.refreshSpotifyToken();
    assertEquals("t", newToken);
  }

  @Test
  public void testRefreshSpotifyEmptyToken() {
    MyApi mockAPI = mock(MyApi.class);
    User user = new User(mockAPI);
    String refreshToken = "refresh";
    when(mockAPI.refreshSpotifyToken("refresh")).thenReturn("");
    user.setSpotifyRefreshToken(refreshToken);
    
    Exception thrown = assertThrows(
            Exception.class,
            () -> user.refreshSpotifyToken()
     );

     assertEquals("Invalid Token", thrown.getMessage());
  }

  @Test
  public void testRefreshSpotifyNullToken() {
	    MyApi mockAPI = mock(MyApi.class);
	    User user = new User(mockAPI);
	    String refreshToken = "refresh";
	    when(mockAPI.refreshSpotifyToken("refresh")).thenReturn(null);
	    user.setSpotifyRefreshToken(refreshToken);
    
    Exception thrown = assertThrows(
            Exception.class,
            () -> user.refreshSpotifyToken()
     );

     assertEquals("Invalid Token", thrown.getMessage());
  }

  //---------------------------------- Minor setter methods ---------------------------------------- //
  @Test
  public void testSetSessionId() {
    User user = new User();
    db.insertAuthenticatedUser("mary", "spotify_token", "refresh_token", "session");
    
    assertEquals("session", db.getUserByName("mary").getSessionId());
    user.setDb(db);
    user.setUsername("mary");
    user.setSessionIdDb("new_session");
    assertEquals("new_session", db.getUserByName("mary").getSessionId());
  }

  @Test
  public void testSetSpotifyToken() {
    User user = new User();
    db.insertAuthenticatedUser("mary", "token", "refresh_token", "session");
    
    assertEquals("token", db.getUserByName("mary").getSpotifyToken());
    user.setDb(db);
    user.setUsername("mary");
    user.setSpotifyTokenDb("new_token");
    assertEquals("new_token", db.getUserByName("mary").getSpotifyToken());
  }

  @AfterEach
  public void afterEach() {
    db.clear();
    
    db.close();
  }
}
