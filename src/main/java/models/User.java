package models;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.IPlaylistItem;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.PlayHistory;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;

public class User {

  /**
   * email associated to spotify account.
   */
  private String username;

  /**
   * hashed password.
   */
  private String passwordHash;

  /**
   * sessionId.
   */
  private String sessionId;

  /**
   * Spotify access token.
   */
  private String spotifyToken;

  /**
   * Spotify Refresh Token.
   */
  private String spotifyRefreshToken;

  /**
   * last connection time.
   */
  private int lastConnectionTime;

  /**
   * SqLite database.
   */
  private SqLite db;

  /**
   * Recently played song limit.
   */
  private static final int LIMIT = 10;

  /**
   * Recently Played song list.
   */
  private Song[] recentlyPlayed = new Song[LIMIT];

  /**
   * currently playing track.
   */
  private Song currentTrack;

  /**
   * Spotify api object associated with user
   */
  private SpotifyAPI api = new SpotifyAPI();

  /**
   * refresh recently playing.
   */
  public void refreshRecentlyPlayed() {
    PlayHistory[] playHistory = null;
	try {
      api.setApi(new SpotifyApi.Builder()
            .setAccessToken(spotifyToken)
            .build());
      playHistory = api.recentlyPlayed();
	} catch (Exception e) {
      // TODO Auto-generated catch block
      if (e.getMessage().equals("The access token expired")) {
        spotifyToken = Login.refreshSpotifyToken(
            spotifyRefreshToken);
        refreshRecentlyPlayed();
      } else {
        System.out.println("Something went wrong!\n"
          + e.getMessage());
      }
	}
    for (int i = 0; i < playHistory.length; i++) {
      TrackSimplified track = playHistory[i].getTrack();
      ArtistSimplified[] artists = track.getArtists();
      String[] songArtists = new String[artists.length];
      for (int j = 0; j < artists.length; j++) {
        songArtists[j] = artists[j].getName();
      }
      Song song = new Song();
      song.setUsername(username);
      song.setName(track.getName());
      song.setArtists(songArtists);
      song.setUri(track.getUri());
      recentlyPlayed[i] = song;
    }
  }

  /**
   * refresh currently playing.
   */
  public void refreshCurrentlyPlaying() {
    CurrentlyPlaying currentlyPlaying = null;
    try {
      api.setApi(new SpotifyApi.Builder()
	            .setAccessToken(spotifyToken)
	            .build());
      currentlyPlaying = api.currentlyPlaying();
      if (currentlyPlaying != null) {
        IPlaylistItem playlistItem = currentlyPlaying.getItem();
        Track track = (Track) playlistItem;
        ArtistSimplified[] artists = track.getArtists();
        String[] songArtists = new String[artists.length];
        for (int j = 0; j < artists.length; j++) {
          songArtists[j] = artists[j].getName();
          //System.out.println(songArtists[j]);
        }
        Song song = new Song();
        song.setUsername(username);
        song.setName(track.getName());
        song.setArtists(songArtists);
        song.setUri(track.getUri());
        System.out.println(track.getUri());
        currentTrack = song;
      } else {
        currentTrack = null;
      }
    } catch (Exception e) {
      if (e.getMessage().equals("The access token expired")) {
        spotifyToken =
                Login.refreshSpotifyToken(spotifyRefreshToken);
        refreshCurrentlyPlaying();
      } else {
        System.out.println("Something went wrong!\n"
            + e.getMessage());
      }
    }
  }

  public String addToQueue(String uri) {
    String ret = "";
    try {
      api.setApi(new SpotifyApi.Builder()
         .setAccessToken(spotifyToken)
            .build());
      ret = api.addSong(uri);
    } catch (Exception e) {
      if (e.getMessage().equals("The access token expired")) {
        spotifyToken =
                Login.refreshSpotifyToken(spotifyRefreshToken);
        addToQueue(uri);
      } else {
        System.out.println("Something went wrong!\n"
            + e.getMessage());
      }
    }
    return ret;
  }

  /**
   * Public constructor.
   * @param token String
   * @param database SqLite
   */
  public User(final String token, final SqLite database) {
    this.db = database;
    String email = Login.getEmailFromSpotifyToken(token);
    User tmpUser = database.getUserByName(email);
    if (tmpUser.username != null) {
      this.username = tmpUser.username;
      this.passwordHash = tmpUser.passwordHash;
      this.sessionId = tmpUser.sessionId;
      this.spotifyToken = tmpUser.spotifyToken;
      this.spotifyRefreshToken = tmpUser.spotifyRefreshToken;
      this.lastConnectionTime = tmpUser.lastConnectionTime;
    } else {
      database.insertUserWithToken(email, token);
      database.commit();
    }
    this.setSpotifyTokenDb(token);
  }

  /**
   * Public constructor
   * @param newApi
   */
  public User(final SpotifyAPI newApi) {
    this.api = newApi;
  }

  /**
   * Public Constructor.
   */
  public User() {
  }

  /**
   * Get recently played.
   * @return recentlyPlayed Song[]
   */
  public Song[] getRecentlyPlayed() {
    Song[] ret = new Song[LIMIT];
    for (int i = 0; i < LIMIT; i++) {
      ret[i] = recentlyPlayed[i];
    }
    return ret;
  }

  /**
   * Set recently played.
   * @param recentlyPlayedList Song[]
   */
  public void setRecentlyPlayed(final Song[] recentlyPlayedList) {
    for (int i = 0; i < LIMIT; i++) {
      recentlyPlayed[i] = recentlyPlayedList[i];
    }
  }

  /**
   * Get current track.
   * @return currentTrack Song
   */
  public Song getCurrentTrack() {
    return this.currentTrack;
  }

  /**
   * set current track.
   * @param track Song
   */
  public void setCurrentTrack(final Song track) {
    this.currentTrack = track;
  }

  /**
   * get password.
   * @return passwordHash String
   */
  public String getPasswordHash() {
    return passwordHash;
  }

  /**
   * set password.
   * @param password String
   */
  public void setPasswordHash(final String password) {
    this.passwordHash = password;
  }

  /**
   * Get session id.
   * @return sessionId String
   */
  public String getSessionId() {
    return sessionId;
  }

  /**
   * set session id.
   * @param id String
   */
  public void setSessionId(final String id) {
    this.sessionId = id;
  }

  /**
   * set session id and save to DB.
   * @param id String
   */
  public void setSessionIdDb(final String id) {
    setSessionId(id);
    db.updateUserAttribute("SESSION_ID", id, username);
    db.commit();
  }

  /**
   * get last connection time.
   * @return time integer
   */
  public int getLastConnectionTime() {
    return lastConnectionTime;
  }

  /**
   * Set last connection time.
   * @param connectionTime integer
   */
  public void setLastConnectionTime(final int connectionTime) {
    this.lastConnectionTime = connectionTime;
  }

  /**
   * Get Spotify token.
   * @return spotifyToken String
   */
  public String getSpotifyToken() {
    return spotifyToken;
  }

  /**
   * Set Spotify token.
   * @param token String
   */
  public void setSpotifyToken(final String token) {
    this.spotifyToken = token;
  }

  /**
   * Set Spotify token and save to DB.
   * @param token String
   */
  public void setSpotifyTokenDb(final String token) {
    setSpotifyToken(token);
    db.updateUserAttribute("SPOTIFY_TOKEN",
        token, username);
    db.commit();
  }

  /**
   * get Spotify refresh token.
   * @return refresh token String
   */
  public String getSpotifyRefreshToken() {
    return spotifyRefreshToken;
  }

  /**
   * Refresh Spotify token.
   * @return token String
   */
  public String refreshSpotifyToken() {
    setSpotifyToken(
        Login.refreshSpotifyToken(spotifyRefreshToken));
    return getSpotifyToken();
  }

  /**
   * Set Spotify refresh token.
   * @param refreshToken String
   */
  public void setSpotifyRefreshToken(final String refreshToken) {
    this.spotifyRefreshToken = refreshToken;
  }

  /**
   * Set Spotify refresh token and save to DB.
   * @param refreshToken String
   */
  public void setSpotifyRefreshTokenDb(final String refreshToken) {
    setSpotifyRefreshToken(refreshToken);
    db.updateUserAttribute("SPOTIFY_REFRESH_TOKEN",
        refreshToken, username);
    db.commit();
  }

  /**
   * get database.
   * @return db SqLite
   */
  public SqLite getDb() {
    return db;
  }

  /**
   * set database.
   * @param database SqLite
   */
  public void setDb(final SqLite database) {
    this.db = database;
  }


  /**
   * Get name of the user.
   * @return String
   */
  public String getUsername() {
    return this.username;
  }

  /**
   * Set username for the user.
   * @param name String
   */
  public void setUsername(final String name) {
    this.username = name;
  }

  /**
   * get Api
   * @return api String
   */
  public SpotifyAPI getApi() {
    return this.api;
  }

  /**
   * set Api
   * @param spotifyApi SpotifyApi
   */
  public void setApi(final SpotifyAPI spotifyApi) {
    this.api = spotifyApi;
  }
 }
