package models;

public class Move {

  /**
   * Player who makes the move.
   */
  private Player player;

  /**
   * Index of row of the move.
   */
  private int moveX;

  /**
   * Index of column of the move.
   */
  private int moveY;

  /**
   * Set player of the move.
   * @param p Player object
   */
  public void setPlayer(final Player p) {
    this.player = p;
  }

  /**
   * Get player of the move.
   * @return Player object
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * Set row index.
   * @param x int
   */
  public void setMoveX(final int x) {
    this.moveX = x;
  }

  /**
   * Get row index.
   * @return row index integer
   */
  public int getMoveX() {
    return moveX;
  }

  /**
   * Set column index.
   * @param y int
   */
  public void setMoveY(final int y) {
    this.moveY = y;
  }

  /**
   * Get column index.
   * @return column index integer
   */
  public int getMoveY() {
    return moveY;
  }

}
