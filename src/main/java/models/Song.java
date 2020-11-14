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
  
  /*
   * Name of the song
   */
  private String name;
  
  /*
   * Artist who performs the song
   */
  private String[] artists;

  
  /**
   * Set name of the song.
   * @param name String
   */
  public void setName(final String name) {
	this.name = name;
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
   * @param artists
   */
  public void setArtists(String[] artists) {
    this.artists = artists;
  }
  
  /**
   * get artists of the song.
   * @return artists String[]
   */
  public String[] getArtists() {
	return this.artists;
  }
  
  /**
   * Set user for a song.
   * @param username String
   */
  public void setUsername(final String username) {
    this.username = username;
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
   * @param song String
   */
  public void setSong(final String song) {
    this.song = song;
  }

  /**
   * Get song.
   * @return message song
   */
  public String getSong() {
    return this.song;
  }

}