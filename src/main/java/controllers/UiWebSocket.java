package controllers;

import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsHandler;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import org.eclipse.jetty.websocket.api.Session;

import com.google.gson.Gson;

/** Web socket class: DO NOT MODIFY.
 * @author Shirish Singh
 *
 */
public class UiWebSocket implements Consumer<WsHandler>  {

  // Store sessions to broadcast a message to all users
  private static final Queue<Session> SESSIONS = new ConcurrentLinkedQueue<>();
  private static Map<WsConnectContext, Integer> userUsernameMap = new ConcurrentHashMap<>();
  private static int nextUserNumber = 1;

  @Override
  public void accept(final WsHandler t) {

    // On Connect
    t.onConnect(new WsConnectHandler() {

      @Override
      public void handleConnect(final WsConnectContext ctx) throws Exception {
        // TODO Auto-generated method stub
        SESSIONS.add(ctx.session);
        int userid = nextUserNumber++; 
        userUsernameMap.put(ctx, userid);
        broadcastMessage("Server", (userid + " left the chat"));
      }

    });

    // On Close
    t.onClose(new WsCloseHandler() {

      @Override
      public void handleClose(final WsCloseContext ctx) throws Exception {
    	ctx.send("someone leave");
        SESSIONS.remove(ctx.session);
      }
    });
  }

  public static Queue<Session> getSessions() {
    return SESSIONS;
  }
  
  // Sends a message from one user to all users, along with a list of current usernames
  private static void broadcastMessage(String sender, String message) {
      userUsernameMap.keySet().stream().filter(ctx -> ctx.session.isOpen()).forEach(session -> {
          session.send(new Gson().toJson(sender + ": " + message));
      });
  }

}
