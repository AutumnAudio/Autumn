/*
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.model_objects.specification.PagingCursorbased;
import com.wrapper.spotify.model_objects.specification.PlayHistory;
import com.wrapper.spotify.requests.data.player.GetUsersCurrentlyPlayingTrackRequest;

import models.User;

public class MockitoTest  {
	
	String spotifyToken = SpotifyAccount.getRefreshToken();
	
	SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setAccessToken(spotifyToken)
            .build();
	
	PagingCursorbased<PlayHistory>
    playHistoryPagingCursorbased =
    spotifyApi.getCurrentUsersRecentlyPlayedTracks()
            .limit(10)
            .build()
            .execute();
	
	private final GetUsersCurrentlyPlayingTrackRequest defaultRequest = SPOTIFY_API
		    .getUsersCurrentlyPlayingTrack()
		    .setHttpManager(
		      TestUtil.MockedHttpManager.returningJson(
		        "requests/data/player/GetUsersCurrentlyPlayingTrackRequest.json"))
		    .market(ITest.MARKET)
		    .additionalTypes(ITest.ADDITIONAL_TYPES)
		    .build();

    @Test
    public void testQuery()  {
    	SpotifyApi mockSpotifyApi = mock(SpotifyApi.class);
    	User user = new User(mockSpotifyApi);
    	
    	PagingCursorbased<PlayHistory> ret = new PagingCursorbased<PlayHistory>;
    	
    	when(mockSpotifyApi.getCurrentUsersRecentlyPlayedTracks()
                .limit(10)
                .build()).thenReturn(null);

        // use mock in test....
        //assertEquals(mockSpotifyApi.getUniqueId(), 43);
    }
}
*/