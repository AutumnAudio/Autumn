package models;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.IPlaylistItem;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.PagingCursorbased;
import com.wrapper.spotify.model_objects.specification.PlayHistory;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import com.wrapper.spotify.requests.data.player.GetCurrentUsersRecentlyPlayedTracksRequest;
import com.wrapper.spotify.requests.data.player.GetUsersCurrentlyPlayingTrackRequest;

public class User {

  private String username; //spotify email
  private String passwordHash;
  private String sessionId;
  private String spotifyToken;
  private String spotifyRefreshToken;
  
  private int lastConnectionTime;
  
  private SqLite db;
  
  private Song[] recentlyPlayed = new Song[10];
  
  private Song currentTrack;
  
  public void refreshRecentlyPlayed() {
	SpotifyApi spotifyApi = new SpotifyApi.Builder()
	        .setAccessToken(spotifyToken)
	        .build();
    final GetCurrentUsersRecentlyPlayedTracksRequest getCurrentUsersRecentlyPlayedTracksRequest = spotifyApi.getCurrentUsersRecentlyPlayedTracks()
    		.limit(10)
    		.build();
    try {
      final PagingCursorbased<PlayHistory> playHistoryPagingCursorbased = getCurrentUsersRecentlyPlayedTracksRequest.execute();
   	  PlayHistory[] playHistory = playHistoryPagingCursorbased.getItems();
   	  for (int i = 0; i < playHistory.length; i++) {
   		TrackSimplified track = playHistory[i].getTrack();
   		ArtistSimplified[] artists = track.getArtists();
   		String[] songArtists = new String[artists.length];
   		for (int j = 0; j < artists.length; j++) {
   		  songArtists[j] = artists[j].getName();
   		  //System.out.println(songArtists[j] + "---");
   		}
   		Song song = new Song();
   		song.setUsername(username);
   		song.setName(track.getName());
   		song.setArtists(songArtists);
   		recentlyPlayed[i] = song;
   		//System.out.println(track.getName());
   	  }
    } catch (Exception e) {
      if (e.getMessage().equals("The access token expired")) {
        spotifyToken = Login.refreshSpotifyToken(spotifyRefreshToken);
        //db.updateUserAttribute("SPOTIFY_REFRESH_TOKEN", spotifyRefreshToken, username);
        //db.commit();
        //setSpotifyToken(spotifyToken, true);
        //System.out.println("new token: " + spotifyToken);
        refreshRecentlyPlayed();
      } else {
        System.out.println("Something went wrong!\n" + e.getMessage());
      }
    }
  }

  public void refreshCurrentlyPlaying() {
	SpotifyApi spotifyApi = new SpotifyApi.Builder()
	        .setAccessToken(spotifyToken)
	        .build();
    final GetUsersCurrentlyPlayingTrackRequest getUsersCurrentlyPlayingTrackRequest = spotifyApi
    		.getUsersCurrentlyPlayingTrack()
			.build();
    try {
      final CurrentlyPlaying currentlyPlaying = getUsersCurrentlyPlayingTrackRequest.execute();
   	  if (currentlyPlaying != null) {
   		IPlaylistItem playlistItem = currentlyPlaying.getItem();
   	    // assuming participant only play music[Track Object] (not shows[Episode Object]) in this MVP
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
   	    currentTrack = song;
   	    //System.out.println(track.getName());
   	  } else {
   	    currentTrack = null;
   	    //System.out.println(currentTrack);
   	  }
    } catch (Exception e) {
      if (e.getMessage().equals("The access token expired")) {
          spotifyToken = Login.refreshSpotifyToken(spotifyRefreshToken);
          //setSpotifyToken(spotifyToken, true);
          //System.out.println("new token: " + spotifyToken);
          refreshCurrentlyPlaying();
        } else {
          System.out.println("Something went wrong!\n" + e.getMessage());
        }
    }
  }
  
  public User(String spotifyToken, SqLite db) {
    
    setDb(db);
    
    String email = Login.getEmailFromSpotifyToken(spotifyToken);

    User tmpUser = db.getUserByName(email);
    
    if(tmpUser.username != null) {
      copyUser(tmpUser);
    }
    
    else {
      db.insertUserWithToken(email, spotifyToken);
      db.commit();
    }
    
    this.setSpotifyToken(spotifyToken, true);
  }
  
  public User() {
    
  }
  
  public void copyUser(User other) {
    
    this.username = other.username;
    this.passwordHash = other.passwordHash;
    this.sessionId = other.sessionId;
    this.spotifyToken = other.spotifyToken;
    this.spotifyRefreshToken = other.spotifyRefreshToken;
    this.lastConnectionTime = other.lastConnectionTime;
    
    //this.db = other.db;
  }
  
  public Song[] getRecentlyPlayed() {
    return this.recentlyPlayed;
  }
  
  public void setRecentlyPlayed(Song[] recentlyPlayed) {
    this.recentlyPlayed = recentlyPlayed;
  }

  public Song getCurrentTrack() {
    return this.currentTrack;
  }
  
  public void setCurrentTrack(Song currentTrack) {
    this.currentTrack = currentTrack;
  }

  public String getPassword_hash() {
    return passwordHash;
  }

  public void setPasswordHash(String password_hash) {
    this.passwordHash = password_hash;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }
  
  public void setSessionId(String sessionId, boolean saveToDb) {
    setSessionId(sessionId);
    
    //if(saveToDb && db.getUserCount(username) == 0) {
   	if(saveToDb) {
      db.updateUserAttribute("SESSION_ID", sessionId, username);
      db.commit();
    }
  }
  
  public int getLastConnectionTime() {
    return lastConnectionTime;
  }

  public void setLastConnectionTime(int lastConnectionTime) {
    this.lastConnectionTime = lastConnectionTime;
  }

  public String getSpotifyToken() {
    return spotifyToken;
  }

  public void setSpotifyToken(String spotifyToken) {
    this.spotifyToken = spotifyToken;
  }
  
  public void setSpotifyToken(String spotifyToken, boolean saveToDb) {
    
    setSpotifyToken(spotifyToken);

    //if(saveToDb && db.getUserCount(username) == 0) {
   	if(saveToDb) {
      db.updateUserAttribute("SPOTIFY_TOKEN", spotifyToken, username);
      db.commit();
    }
  }
  
  public String getSpotifyRefreshToken() {
    return spotifyRefreshToken;
  }

  public String refreshSpotifyToken() {
    
    setSpotifyToken(Login.refreshSpotifyToken(spotifyRefreshToken), true);
    return getSpotifyToken();
  }
  
  public void setSpotifyRefreshToken(String spotifyRefreshToken) {
    this.spotifyRefreshToken = spotifyRefreshToken;
  }
  
  public void setSpotifyRefreshToken(String spotifyRefreshToken, boolean saveToDb) {
    setSpotifyRefreshToken(spotifyRefreshToken);
    
    //if(saveToDb && db.getUserCount(username) == 0) {
   	if(saveToDb) {
      db.updateUserAttribute("SPOTIFY_REFRESH_TOKEN", spotifyRefreshToken, username);
      db.commit();
    }
  }
  
  public SqLite getDb() {
    return db;
  }

  public void setDb(SqLite db) {
    this.db = db;
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
   * @param username String
   */
  public void setUsername(final String name) {
    this.username = name;
  }
}
