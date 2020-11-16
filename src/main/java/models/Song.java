package models;

public class Song {

  /**
   * User who shared the song.
   */
  private String username;

  /**
   * Song that is being shared.
   */
  private String song;

  /**
   * Name of the song.
   */
  private String name;

  /**
   * Artist who performs the song.
   */
  private String[] artists;

  /**
   * Set name of the song.
   * @param songName String
   */
  public void setName(final String songName) {
    this.name = songName;
  }

  /**
   * Get name of song.
   * @return name String
   */
  public String getName() {
    return this.name;
  }

  /**
   * set artists of the song.
   * @param songArtists String[]
   */
  public void setArtists(final String[] songArtists) {
    artists = new String[songArtists.length];
    for (int i = 0; i < songArtists.length; i++) {
      artists[i] = songArtists[i];
    }
  }

  /**
   * get artists of the song.
   * @return artists String[]
   */
  public String[] getArtists() {
    String[] ret = new String[artists.length];
    for (int i = 0; i < artists.length; i++) {
      ret[i] = artists[i];
    }
    return ret;
  }

  /**
   * Set user for a song.
   * @param userName String
   */
  public void setUsername(final String userName) {
    this.username = userName;
  }

  /**
   * Get user of a song.
   * @return username String
   */
  public String getUsername() {
    return this.username;
  }

  /**
   * Set song.
   * @param spotifySong String
   */
  public void setSong(final String spotifySong) {
    this.song = spotifySong;
  }

  /**
   * Get song.
   * @return message song
   */
  public String getSong() {
    return this.song;
  }

}
