package controllers;

import com.google.gson.Gson;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsHandler;
import io.javalin.websocket.WsMessageHandler;
import io.javalin.websocket.WsMessageContext;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import models.Genre;
import org.eclipse.jetty.websocket.api.Session;


/** Web socket class: DO NOT MODIFY.
 * @author Shirish Singh
 *
 */
public class UiWebSocket implements Consumer<WsHandler>  {

  // Store sessions to broadcast a message to all users
  private static final Queue<Session> SESSIONS = new ConcurrentLinkedQueue<>();
  private static Map<Session, String> sessionGenre = new ConcurrentHashMap<>();

  @Override
  public void accept(final WsHandler t) {

    // On Connect
    t.onConnect(new WsConnectHandler() {

      @Override
      public void handleConnect(final WsConnectContext ctx) throws Exception {
        // TODO Auto-generated method stub
        SESSIONS.add(ctx.session);
      }

    });
    
    // on Message
    t.onMessage(new WsMessageHandler() {
    	@Override
    	public void handleMessage(final WsMessageContext ctx) throws Exception {
    		Genre genre = ctx.message(Genre.class); 
    		sessionGenre.put(ctx.session, genre.getGenre());
    	}
    });

    // On Close
    t.onClose(new WsCloseHandler() {

      @Override
      public void handleClose(final WsCloseContext ctx) throws Exception {
        SESSIONS.remove(ctx.session);
        sessionGenre.remove(ctx.session);
      }
    });
  }

  public static Queue<Session> getSessions() {
    return SESSIONS;
  }
  
  public static Map<Session, String> getGenreMap() {
    return sessionGenre;
  }

}
