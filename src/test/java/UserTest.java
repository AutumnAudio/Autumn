import models.Login;
import models.Song;
import models.SpotifyAPI;
import models.SqLite;
import models.User;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.IPlaylistItem;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.PagingCursorbased;
import com.wrapper.spotify.model_objects.specification.PlayHistory;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import com.wrapper.spotify.requests.data.player.GetCurrentUsersRecentlyPlayedTracksRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.apache.hc.core5.http.ParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;


@TestMethodOrder(OrderAnnotation.class) 
public class UserTest {
  
  String refreshToken = SpotifyAccount.getRefreshToken();
  String token = Login.refreshSpotifyToken(refreshToken);
  SpotifyApi api = new SpotifyApi.Builder()
          .setAccessToken(Login.refreshSpotifyToken(refreshToken))
          .build();
  
  SqLite db = new SqLite();
  
  @Mock
  PagingCursorbased<PlayHistory> pcb;
  
  @BeforeEach
  public void beforeEach() {
    db.start();
    db.commit();
  }

  @Test
  public void testResfreshRecentlyPlayed() throws ParseException, SpotifyWebApiException, IOException {
    SpotifyAPI mockAPI = mock(SpotifyAPI.class);
	PlayHistory[] history = new PlayHistory[10];
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
    user.refreshRecentlyPlayed();
    assertEquals(10, user.getRecentlyPlayed().length);
	for (Song song : user.getRecentlyPlayed()) {
	  assertNotNull(song.getName());
	}
  }

  @Test
  public void testResfreshCurrentlyPlayingNull() throws ParseException, SpotifyWebApiException, IOException {
    SpotifyAPI mockAPI = mock(SpotifyAPI.class);
    when(mockAPI.currentlyPlaying()).thenReturn(null);
    User user = new User(mockAPI);
	user.refreshCurrentlyPlaying();
	assertNull(user.getCurrentTrack());
  }

  @Test
  public void testResfreshCurrentlyPlayingNotNull() throws ParseException, SpotifyWebApiException, IOException {
    SpotifyAPI mockAPI = mock(SpotifyAPI.class);
    CurrentlyPlaying currentlyPlaying = mock(CurrentlyPlaying.class);
    when(mockAPI.currentlyPlaying()).thenReturn(currentlyPlaying);
    Track mockTrack = mock(Track.class);
    when(currentlyPlaying.getItem()).thenReturn(mockTrack);
    when(mockTrack.getName()).thenReturn("song name");
    ArtistSimplified[] artistList = new ArtistSimplified[1];
    ArtistSimplified artist = mock(ArtistSimplified.class);
    when(artist.getName()).thenReturn("test artist");
    artistList[0] = artist;
    when(mockTrack.getArtists()).thenReturn(artistList);
    when(mockTrack.getUri()).thenReturn("song uri");
    User user = new User(mockAPI);
	user.refreshCurrentlyPlaying();
	assertEquals("song name", user.getCurrentTrack().getName());
  }

  @Test
  public void testAddToQueue() throws ParseException, SpotifyWebApiException, IOException {
    SpotifyAPI mockAPI = mock(SpotifyAPI.class);
    when(mockAPI.addSong("song uri")).thenReturn("song added");
    User user = new User(mockAPI);
	assertEquals("song added", user.addToQueue("song uri"));
  }

  @Test
  public void testUserConstructor() {
    String token = Login.refreshSpotifyToken(refreshToken);
    User user = new User(token, db);
    assertNotNull(user.getSpotifyToken());
    assertEquals(1, db.getUserCount("cherrychu_120@hotmail.com"));
  }

  @Test
  public void testRefreshSpotifyToken() {
    User user = new User(token, db);
    user.setSpotifyRefreshTokenDb(refreshToken);
    user.refreshSpotifyToken();
    assertNotEquals(token, user.getSpotifyToken());
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
