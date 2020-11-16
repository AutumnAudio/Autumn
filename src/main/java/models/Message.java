package models;

public class Message {

  /**
   * User who sent the message.
   */
  private String username;

  /**
   * Message content.
   */
  private String message;

  /**
   * Set user of a message.
   * @param name String
   */
  public void setUsername(final String name) {
    this.username = name;
  }

  /**
   * Get user of a message.
   * @return username String
   */
  public String getUsername() {
    return this.username;
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
    return this.message;
  }

}
