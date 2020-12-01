import models.Login;
import models.Song;
import models.SpotifyAPI;
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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.apache.hc.core5.http.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.mockito.Spy;


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
    db.commit();
  }

// ----------------------------- Refresh Recently Played --------------------------------------------- //
  @Test
  public void testResfreshRecentlyPlayed() throws ParseException, SpotifyWebApiException, IOException {
    SpotifyAPI mockAPI = mock(SpotifyAPI.class);
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
    SpotifyAPI mockAPI = mock(SpotifyAPI.class);
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
    SpotifyAPI mockAPI = mock(SpotifyAPI.class);
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
    SpotifyAPI mockAPI = mock(SpotifyAPI.class);
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
    SpotifyAPI mockAPI = mock(SpotifyAPI.class);
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
    SpotifyAPI mockAPI = mock(SpotifyAPI.class);
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
    SpotifyAPI mockAPI = mock(SpotifyAPI.class);
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
    SpotifyAPI mockAPI = mock(SpotifyAPI.class);
    when(mockAPI.currentlyPlaying()).thenReturn(null);
    User user = new User(mockAPI);
    String refreshToken = "refresh";
    when(mockAPI.refreshSpotifyToken("refresh")).thenReturn("newToken");
    user.setSpotifyRefreshToken(refreshToken);
	assertEquals("Currently not playing any tracks", user.refreshCurrentlyPlaying());
  }

  @Test
  public void testResfreshCurrentlyPlayingNotNull() throws ParseException, SpotifyWebApiException, IOException {
    SpotifyAPI mockAPI = mock(SpotifyAPI.class);
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
    SpotifyAPI mockAPI = mock(SpotifyAPI.class);
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
    SpotifyAPI mockAPI = mock(SpotifyAPI.class);
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
    SpotifyAPI mockAPI = mock(SpotifyAPI.class);
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
    SpotifyAPI mockAPI = mock(SpotifyAPI.class);
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
    SpotifyAPI mockAPI = mock(SpotifyAPI.class);
    when(mockAPI.addSong("song uri")).thenReturn(null);
    User user = new User(mockAPI);
    String refreshToken = "refresh";
    when(mockAPI.refreshSpotifyToken("refresh")).thenReturn("newToken");
    user.setSpotifyRefreshToken(refreshToken);
	assertEquals(null, user.addToQueue("spotify:track:x"));
  }

  @Test
  public void testAddToQueueshortUri() throws ParseException, SpotifyWebApiException, IOException {
    SpotifyAPI mockAPI = mock(SpotifyAPI.class);
    when(mockAPI.addSong("song uri")).thenReturn(null);
    User user = new User(mockAPI); 
    String refreshToken = "refresh";
    when(mockAPI.refreshSpotifyToken("refresh")).thenReturn("newToken");
    user.setSpotifyRefreshToken(refreshToken);
	assertEquals("invalid uri", user.addToQueue("spotifytrack:"));
  }

  @Test
  public void testAddToQueueInvalidUri() throws ParseException, SpotifyWebApiException, IOException {
    SpotifyAPI mockAPI = mock(SpotifyAPI.class);
    when(mockAPI.addSong("song uri")).thenReturn(null);
    User user = new User(mockAPI);
    String refreshToken = "refresh";
    when(mockAPI.refreshSpotifyToken("refresh")).thenReturn("newToken");
    user.setSpotifyRefreshToken(refreshToken);
	assertEquals("invalid uri", user.addToQueue("song uri"));
  }

//------------------------------------------ Other --------------------------------------------- //
  @Test
  public void testRefreshSpotifyTokenValid() throws Exception {
    SpotifyAPI mockAPI = mock(SpotifyAPI.class);
    User user = new User(mockAPI);
    String refreshToken = "refresh";
    when(mockAPI.refreshSpotifyToken("refresh")).thenReturn("t");
    user.setSpotifyRefreshToken(refreshToken);
    String newToken = user.refreshSpotifyToken();
    assertEquals("t", newToken);
  }

  @Test
  public void testRefreshSpotifyEmptyToken() {
    SpotifyAPI mockAPI = mock(SpotifyAPI.class);
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
	    SpotifyAPI mockAPI = mock(SpotifyAPI.class);
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

  @Test
  public void testSetSessionId() {
    User user = new User();
    db.insertUserWithSession("cherry", "session");
    db.commit();
    assertEquals("session", db.getUserByName("cherry").getSessionId());
    user.setDb(db);
    user.setUsername("cherry");
    user.setSessionIdDb("new_session");
    assertEquals("new_session", db.getUserByName("cherry").getSessionId());
  }

  @Test
  public void testSetSpotifyToken() {
    User user = new User();
    db.insertUserWithToken("cherry", "token");
    db.commit();
    assertEquals("token", db.getUserByName("cherry").getSpotifyToken());
    user.setDb(db);
    user.setUsername("cherry");
    user.setSpotifyTokenDb("new_token");
    assertEquals("new_token", db.getUserByName("cherry").getSpotifyToken());
  }

  @Test
  public void testSetSpotifyRefreshToken() {
    User user = new User();
    db.insertUserWithToken("cherry", "token");
    db.commit();
    assertNull(db.getUserByName("cherry").getSpotifyRefreshToken());
    user.setDb(db);
    user.setUsername("cherry");
    user.setSpotifyRefreshTokenDb("new_refresh_token");
    assertEquals("new_refresh_token", db.getUserByName("cherry").getSpotifyRefreshToken());
  }

  @AfterEach
  public void afterEach() {
    db.clear();
    db.commit();
    db.close();
  }
}
