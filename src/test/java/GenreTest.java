import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import models.Genre;

public class GenreTest {
  // ----------------------- isValidGenre -------------------------- //
  @Test
  public void isValidGenreTrueTest() {
    assertEquals(true, Genre.isValidGenre("POP"));
  }

  @Test
  public void isValidGenreFalseTest() {
    assertEquals(false, Genre.isValidGenre("T"));
  }

  @Test
  public void isValidGenreNullTest() {
    assertEquals(false, Genre.isValidGenre(null));
  }
}
