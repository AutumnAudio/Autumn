package models;

public class Message {

  /**
   * Indicate if move is valid.
   */
  private boolean moveValidity;

  /**
   * 3-digit code that record the move.
   */
  private int code;

  /**
   * Message return to player after move (if any).
   */
  private String message;

  /**
   * Set moveValidity.
   * @param validity boolean
   */
  public void setMoveValidity(final boolean validity) {
    this.moveValidity = validity;
  }

  /**
   * Get moveValidity variable.
   * @return moveValidity boolean
   */
  public boolean getMoveValidity() {
    return moveValidity;
  }

  /**
   * Set Code for a move.
   * @param c int
   */
  public void setCode(final int c) {
    this.code = c;
  }

  /**
   * Get code of a move.
   * @return code integer
   */
  public int getCode() {
    return code;
  }

  /**
   * Set message.
   * @param msg String
   */
  public void setMessage(final String msg) {
    this.message = msg;
  }

  /**
   * Get message.
   * @return message string
   */
  public String getMessage() {
    return message;
  }

}
