

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

/**
 * Basic Echo Client Socket.
 */
@WebSocket(maxTextMessageSize = 64 * 1024)
public final class SimpleWebSocket {

  /**
   * Socket close latch.
   */
  private final CountDownLatch closeLatch;

  /**
   * Current session.
   */
  @SuppressWarnings("unused")
  private Session session;

  /**
   * Simple Web Socket contructor.
   */
  public SimpleWebSocket() {
    this.closeLatch = new CountDownLatch(1);
  }

  /**
   * Socket await close.
   * @param duration Integer
   * @param unit TimeUnit
   * @return true/false boolean
   * @throws InterruptedException
   */
  public boolean awaitClose(final int duration, final TimeUnit unit)
        throws InterruptedException {
    return this.closeLatch.await(duration, unit);
  }

  /**
   * Socket on Close.
   * @param statusCode Integer
   * @param reason String
   */
  @OnWebSocketClose
  public void onClose(final int statusCode, final String reason) {
    System.out.printf("Connection closed: %d - %s%n", statusCode, reason);
    this.session = null;
    this.closeLatch.countDown(); // trigger latch
  }

  /**
   * Socket on Connect.
   * @param newSession Session
   */
  @OnWebSocketConnect
  public void onConnect(final Session newSession) {
    System.out.printf("Got connect: %s%n", newSession);
    this.session = newSession;
    try {
      Future<Void> fut;
      fut = newSession.getRemote().sendStringByFuture("Hello");
      fut.get(2, TimeUnit.SECONDS); // wait for send to complete.

      fut = newSession.getRemote()
          .sendStringByFuture("Thanks for the conversation.");
      fut.get(2, TimeUnit.SECONDS); // wait for send to complete.
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  /**
   * Socket on Message.
   * @param msg String
   */
  @OnWebSocketMessage
  public void onMessage(final String msg) {
    System.out.printf("Got msg: %s%n", msg);
    if (msg.contains("Thanks")) {
      session.close(StatusCode.NORMAL, "I'm done");
    }
  }

  /**
   * Socket on error.
   * @param cause Throwable
   */
  @OnWebSocketError
  public void onError(final Throwable cause) {
    System.out.print("WebSocket Error: ");
    cause.printStackTrace(System.out);
  }
}
