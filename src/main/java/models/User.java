package models;

import java.sql.Time;
import java.time.LocalTime;

import com.google.gson.Gson;
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
   * SqLite database.
   */
  private SqLite db;

  /**
   * Recently played song limit.
   */
  private static final int LIMIT = 10;

  /**
   * min length for uri.
   */
  private static final int URILIMIT = 14;

  /**
   * Recently Played song list.
   */
  private Song[] recentlyPlayed = new Song[LIMIT];

  /**
   * currently playing track.
   */
  private Song currentTrack;

  /**
   * Spotify api object associated with user.
   */
  private MyApi api = new MyApi();

  /**
   * refresh recently playing.
   * @return response String
   */
  public String refreshRecentlyPlayed() {
    String ret = "OK";
    PlayHistory[] playHistory = null;
    spotifyToken = api.refreshSpotifyToken(spotifyRefreshToken);
    try {
      api.setApi(new SpotifyApi.Builder()
            .setAccessToken(spotifyToken)
            .build());
      playHistory = api.recentlyPlayed();
      if (playHistory.length <  LIMIT || playHistory.length > LIMIT) {
        return "Number of items return not matched.";
      }
      for (int i = 0; i < playHistory.length; i++) {
        TrackSimplified track = playHistory[i].getTrack();
        if (track == null) {
          return "List contains null track";
        }
        ArtistSimplified[] artists = track.getArtists();
        if (artists == null) {
          return "List contains null artist list";
        }
        String[] songArtists = new String[artists.length];
        for (int j = 0; j < artists.length; j++) {
          songArtists[j] = artists[j].getName();
        }
        Song song = new Song();
        song.setUsername(username);
        String trackName = track.getName();
        if (trackName == null || trackName.length() == 0) {
          return "List contains invalid track name";
        }
        song.setName(trackName);
        song.setArtists(songArtists);
        song.setUri(track.getUri());
        recentlyPlayed[i] = song;
      }
    } catch (Exception e) {
      if (e.getMessage().equals("The access token expired")) {
        refreshRecentlyPlayed();
      } else {
        System.out.println("Something went wrong!\n"
          + e.getMessage());
      }
    }
    return ret;
  }

  /**
   * refresh currently playing.
   * @return response String
   */
  public String refreshCurrentlyPlaying() {
    String ret = "OK";
    CurrentlyPlaying currentlyPlaying = null;
    spotifyToken = api.refreshSpotifyToken(spotifyRefreshToken);
    try {
      api.setApi(new SpotifyApi.Builder()
                .setAccessToken(spotifyToken)
                .build());
      currentlyPlaying = api.currentlyPlaying();
    } catch (Exception e) {
      if (e.getMessage().equals("The access token expired")) {
        refreshCurrentlyPlaying();
      } else {
        System.out.println("Something went wrong!\n"
            + e.getMessage());
      }
    }
    if (currentlyPlaying != null) {
      IPlaylistItem playlistItem = currentlyPlaying.getItem();
      Track track = (Track) playlistItem;
      if (track == null) {
        return "Null track";
      }
      ArtistSimplified[] artists = track.getArtists();
      if (artists == null) {
        return "Null artist list";
      }
      String[] songArtists = new String[artists.length];
      for (int j = 0; j < artists.length; j++) {
        songArtists[j] = artists[j].getName();
      }
      Song song = new Song();
      song.setUsername(username);
      String trackName = track.getName();
      if (trackName == null || trackName.length() == 0) {
        ret = "Invalid track name";
      }
      song.setName(trackName);
      song.setArtists(songArtists);
      song.setUri(track.getUri());
      currentTrack = song;
    } else {
      currentTrack = null;
      ret = "Currently not playing any tracks";
    }
    return ret;
  }

  /**
   * add song to queue.
   * @param uri String
   * @return response String
   */
  public String addToQueue(final String uri) {
    spotifyToken = api.refreshSpotifyToken(spotifyRefreshToken);
    if (uri.length() <= URILIMIT || !uri.contains("spotify:track:")) {
      return "invalid uri";
    }
    String ret = "";
    try {
      api.setApi(new SpotifyApi.Builder()
          .setAccessToken(spotifyToken)
          .build());
      ret = api.addSong(uri);
    } catch (Exception e) {
      if (e.getMessage().equals("The access token expired")) {
        addToQueue(uri);
      } else {
        System.out.println("Something went wrong!\n"
            + e.getMessage());
      }
    }
    return ret;
  }

  /**
   * share user currently playing song.
   * @param chatlist ChatList
   * @param database SqLite
   * @return message Message
   */
  public Message share(final ChatList chatlist, final SqLite database) {
    if (chatlist == null) {
      return null;
    }
    Message message = new Message();
    message.setUsername(username);
    refreshCurrentlyPlaying();
    String genreStr = database.getGenreUser(username);
    if (getCurrentTrack() == null || genreStr == null) {
      return null;
    }
    Song song = new Song();
    song.setUsername(username);
    song.setName(getCurrentTrack().getName());
    song.setArtists(getCurrentTrack().getArtists());
    song.setUri(getCurrentTrack().getUri());
    Genre genre = Genre.valueOf(genreStr.toUpperCase());
    ChatRoom chatroom = chatlist.getChatroomByGenre(genre);
    // share song with participants
    for (User sharee : chatroom.getParticipant().values()) {
      if (!getUsername().equals(sharee.getUsername())) {
        sharee.addToQueue(song.getUri());
      }
    }
    // add song to group playlist
    chatroom.addSong(song);
    database.insertSong(username, Time.valueOf(LocalTime.now()),
            genre, new Gson().toJson(song));
    // send message to chat
    message.setUsername(username);
    message.setMessage("I just shared " + song.getName()
            + " by " + song.getArtists()[0]);
    return message;
  }

  /**
   * Public constructor.
   * @param newApi MyApi
   */
  public User(final MyApi newApi) {
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
   * @throws Exception Exception
   */
  public String refreshSpotifyToken() throws Exception {
    String newToken = api.refreshSpotifyToken(spotifyRefreshToken);
    if (newToken == null || newToken.length() == 0) {
      throw new Exception("Invalid Token");
    }
    setSpotifyToken(newToken);
    return newToken;
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
   * @return response String
   */
  public String setSpotifyRefreshTokenDb(final String refreshToken) {
    if (refreshToken == null || refreshToken.length() == 0) {
      return "No token";
    }
    setSpotifyRefreshToken(refreshToken);
    db.updateUserAttribute("SPOTIFY_REFRESH_TOKEN",
        refreshToken, username);
    return "OK";
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
   * get Api.
   * @return api String
   */
  public MyApi getApi() {
    return this.api;
  }

  /**
   * set Api.
   * @param spotifyApi SpotifyApi
   */
  public void setApi(final MyApi spotifyApi) {
    this.api = spotifyApi;
  }
 }
