package models;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Genre {
    BLUES ("blues"),
    CLASSICAL ("classical"),
    COUNTRY ("country"),
    JAZZ ("jazz"),
    POP ("pop"),
    ROCK ("rock");

	private final String genre;
	
    Genre(String genre) {
        this.genre = genre;
    }
 
    public String getGenre() {
        return this.genre;
    }
    
    public static boolean isValidGenre(final String genre) {
      return Arrays.stream(Genre.values())
          .map(Genre::name)
          .collect(Collectors.toSet())
          .contains(genre);
    }
}