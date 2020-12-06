import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import models.Thread;
import models.ChatList;
import models.ChatRoom;
import models.Genre;
import models.SqLite;

public class ThreadTest {
  @Test
  public void runTest() throws InterruptedException {
    ChatRoom pop = new ChatRoom();
    pop.setGenre(Genre.POP);
    Map<String, ChatRoom> map = new HashMap<>();
    map.put("pop", pop);
    ChatList mockChatlist = mock(ChatList.class);
    when(mockChatlist.getChatrooms()).thenReturn(map);
    SqLite mockDb = mock(SqLite.class);
    when(mockDb.update()).thenReturn(mockChatlist);
    Thread thread = new Thread(mockDb);

    ExecutorService service = Executors.newSingleThreadExecutor();
    service.execute(thread);

    // Add something like this.
    service.shutdown();
    service.awaitTermination(5, TimeUnit.SECONDS);
  }
}
