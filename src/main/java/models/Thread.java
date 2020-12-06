package models;
import com.google.gson.Gson;

import controllers.StartChat;

public class Thread implements Runnable {
  /**
   * Database.
   */
  private SqLite db;

  /**
   * public constructor.
   * @param database SqLite
   */
  public Thread(final SqLite database) {
    this.db = database;
  }

  /**
   * override run method for runnable.
   */
  @Override
  public void run() {
    ChatList chatListData = db.update();
    chatListData.refreshChatList();
    for (ChatRoom chatroom : chatListData.getChatrooms().values()) {
      String genre = chatroom.getGenre().getGenre();
      StartChat.sendChatRoomToAllParticipants(genre,
              new Gson().toJson(chatroom));
    }
  }
}
