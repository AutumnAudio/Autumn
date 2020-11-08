package models;

public class Player {

  /**
   * Symbol that represents player on the board (X or O).
   */
  private char type;

  /**
   * Player ID.
   */
  private int id;

  /**
   * Get symbol of the player.
   * @return char
   */
  public char getType() {
    return type;
  }

  /**
   * Set symbol for the player.
   * @param mark char
   */
  public void setType(final char mark) {
    this.type = mark;
  }

  /**
   * Get ID of the player.
   * @return integer
   */
  public int getId() {
    return id;
  }

  /**
   * Set ID for the player.
   * @param i ID
   */
  public void setId(final int i) {
    this.id = i;
  }

}
