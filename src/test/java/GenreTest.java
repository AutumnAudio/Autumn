import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import models.Genre;

public class GenreTest {
  @Test
  public void isValidGenreTrueTest() {
    assertEquals(true, Genre.isValidGenre("POP"));
  }

  @Test
  public void isValidGenreFalseTest() {
    assertEquals(false, Genre.isValidGenre("TEST"));
  }
}
