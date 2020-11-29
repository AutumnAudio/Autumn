package models;

import java.io.IOException;

import org.apache.hc.core5.http.ParseException;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import com.wrapper.spotify.model_objects.specification.PlayHistory;

public class SpotifyAPI {
  
  private SpotifyApi api;

  
  public void setApi(final SpotifyApi newApi) {
    this.api = newApi;
  }

  public SpotifyApi getApi() {
    return this.api;
  }
  
  private static final int LIMIT = 10;
  
  public PlayHistory[] recentlyPlayed() throws ParseException, SpotifyWebApiException, IOException {
    PlayHistory[] playHistory = api
    	    .getCurrentUsersRecentlyPlayedTracks()
            .limit(LIMIT)
            .build()
            .execute()
            .getItems();
    return playHistory;
  }

  public CurrentlyPlaying currentlyPlaying() throws ParseException, SpotifyWebApiException, IOException {
    CurrentlyPlaying currentTrack = api
      .getUsersCurrentlyPlayingTrack()
      .build()
      .execute();
    return currentTrack;
  }
}
