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