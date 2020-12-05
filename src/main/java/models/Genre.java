package models;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Genre enum. Accept the following types.
 * @author Cherry
 *
 */
public enum Genre {
  /**
   * Blues music.
   */
  BLUES("blues"),

  /**
   * Classical music.
   */
  CLASSICAL("classical"),

  /**
   * Country music.
   */
  COUNTRY("country"),

  /**
   * Jazz music.
   */
  JAZZ("jazz"),

  /**
   * Pop music.
   */
  POP("pop"),

  /**
   * Rock music.
   */
  ROCK("rock");

  /**
   * genre of object.
   */
  private String genre;

  /**
   * Minumum length of genre.
   */
  private static final int MINLENGTH = 3;

  /**
   * Constructor of genre object.
   * @param newGenre String
   */
  Genre(final String newGenre) {
    this.genre = newGenre;
  }

  /**
   * get genre of the object.
   * @return genre String
   */
  public String getGenre() {
    return this.genre;
  }

  /**
   * Check if genre is valid.
   * @param genre String
   * @return true/false boolean
   */
  public static boolean isValidGenre(final String genre) {
    if (genre == null || genre.length() < MINLENGTH) {
      return false;
    }
    return Arrays.stream(Genre.values())
          .map(Genre::name)
          .collect(Collectors.toSet())
          .contains(genre);
  }
}
