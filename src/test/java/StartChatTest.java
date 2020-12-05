import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import controllers.StartChat;
import java.net.URI;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import models.ChatList;
import models.ChatRoom;
import models.Message;
import models.Song;
import models.SqLite;
import models.User;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import io.javalin.Javalin;



@TestMethodOrder(OrderAnnotation.class) 
public class StartChatTest {

  private static int getHerokuAssignedPort() {
    String herokuPort = System.getenv("PORT");
    if (herokuPort != null) {
      return Integer.parseInt(herokuPort);
    }
    return 7000;
  }

  /**
  * Runs only once before the testing starts.
  */
  @BeforeAll
  public static void init() {

    System.out.println("Before All");
  }

  /**
  * This method starts a new game before every test run. It will run every time before a test.
  */
  @BeforeEach
  public void startAllChats() {
    // Test if server is running. You need to have an endpoint /
    // If you do not wish to have this end point, it is okay to not have anything in this method.
    // Create HTTP request and get response
    StartChat.main(null);
    String port = Integer.toString(getHerokuAssignedPort());
    HttpResponse<String> response = Unirest.get("http://localhost:" + port + "/lobby").asString();
    assertEquals(200, response.getStatus());
    
    SqLite db = StartChat.getDb();
    
    String sessionId = db.getLatestSession();

    db.insertAuthenticatedUser("testing1", "token1", "refresh1", sessionId);
    
    
    System.out.println("Before Each");
  }

  /**
  * This is a test case to evaluate the chatrooms endpoint.
  */
  @Test
  @Order(1)
  public void chatroomsTest() {
    HttpResponse<String> response = Unirest.get("http://localhost:8080/chatrooms/").asString();
    
    //assertEquals(200, response.getStatus());

    // --------------------------- JSONObject Parsing ----------------------------------

    System.out.println("/chatrooms Response: " + response.getBody());

    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(response.getBody());

    // ---------------------------- GSON Parsing -------------------------

    // GSON use to parse data to object
    Gson gson = new Gson();
    ChatList chatlist = gson.fromJson(jsonObject.toString(), ChatList.class);
    
    // Check if player type is correct
    assertEquals(6, chatlist.getChatrooms().size());
  
    System.out.println("Test /chatrooms");
  }

  /**
  * This is a test case to evaluate the chatrooms endpoint with restart.
  */
  
  @Test
  @Order(2)
  public void chatroomsRestartTest() {
    StartChat.stop();
    StartChat.main(null);
    String port = Integer.toString(getHerokuAssignedPort());
    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.get("http://localhost:" + port + "/chatrooms/").asString();
    assertEquals(200, response.getStatus());
    
    // sessionId is new after restarting, need to log again
    SqLite db = StartChat.getDb();
    String sessionId = db.getLatestSession();
    db.insertAuthenticatedUser("testing2", "token2", "refresh2", sessionId);
    
    
    
    response = Unirest.get("http://localhost:" + port + "/chatrooms/").asString();

    assertEquals(200, response.getStatus());

    // --------------------------- JSONObject Parsing ----------------------------------

    System.out.println("/chatrooms Response: " + response.getBody());

    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(response.getBody());

    // ---------------------------- GSON Parsing -------------------------

    // GSON use to parse data to object
    Gson gson = new Gson();
    ChatList chatlist = gson.fromJson(jsonObject.toString(), ChatList.class);
    
    // Check if player type is correct
    assertEquals(6, chatlist.getChatrooms().size());
  
    System.out.println("Test /chatrooms");
  }

  /**
  * This is a test case to evaluate the joinroom endpoint.
  */
  @Test
  @Order(3)
  public void joinRoomTest() {
    String port = Integer.toString(getHerokuAssignedPort());
    HttpResponse<String> response = Unirest.post("http://localhost:" + port + "/joinroom/blues/").body("username=ben").asString();
    response = Unirest.get("http://localhost:" + port + "/chatroom/blues/?user=ben").asString();

    assertEquals(200, response.getStatus());

    // --------------------------- JSONObject Parsing ----------------------------------

    System.out.println("/[chatroom]/[user] Response: " + response.getBody());

    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(response.getBody());

    // ---------------------------- GSON Parsing -------------------------

    // GSON use to parse data to object
    Gson gson = new Gson();
    ChatRoom chatroom = gson.fromJson(jsonObject.toString(), ChatRoom.class);
    
    // Check if player type is correct
    assertEquals(1, chatroom.getParticipant().size());

    System.out.println("Test join chatroom");
  }

  /**
  * This is a test case to evaluate the joinroom endpoint.
  */
  @Test
  @Order(4)
  public void rejoinRoomTest() {
    String port = Integer.toString(getHerokuAssignedPort());
    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.post("http://localhost:" + port + "/joinroom/jazz/").asString();
    response = Unirest.post("http://localhost:" + port + "/joinroom/jazz/").asString();
    response = Unirest.get("http://localhost:" + port + "/chatroom/jazz/?user=ben").asString();

    assertEquals(200, response.getStatus());

    // --------------------------- JSONObject Parsing ----------------------------------

    System.out.println("/joinroom Response: " + response.getBody());

    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(response.getBody());

    // ---------------------------- GSON Parsing -------------------------

    // GSON use to parse data to object
    Gson gson = new Gson();
    ChatRoom chatroom = gson.fromJson(jsonObject.toString(), ChatRoom.class);

    assertEquals(1, chatroom.getParticipant().size());
  
    System.out.println("Test rejoining chatroom");
  }

  /**
  * This is a test case to evaluate the joinroom endpoint.
  */
  @Test
  @Order(5)
  public void invalidJoinRoomTest() {
    String port = Integer.toString(getHerokuAssignedPort());
    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.post("http://localhost:" + port + "/joinroom/metal").asString();
    
    assertEquals("Invalid Room", response.getBody());
  
    System.out.println("Test invalid join chatroom");
  }

  /**
  * This is a test case to evaluate the chatroom/genre endpoint.
  */
  @Test
  @Order(6)
  public void validChatroomGenreTest() {

    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.get("http://localhost:8080/chatroom/pop/?user=ben").asString();

    assertEquals(200, response.getStatus());
    assertNotEquals("Invalid Room", response.getBody());
  
    System.out.println("Test invalid chatroom genre");
  }

  /**
  * This is a test case to evaluate the chatroom/genre endpoint.
  */
  @Test
  @Order(6)
  public void invalidChatroomGenreTest() {
    String port = Integer.toString(getHerokuAssignedPort());
    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.get("http://localhost:" + port + "/chatroom/metal/?user=ben").asString();

    assertEquals(200, response.getStatus());
    assertEquals("Invalid Room", response.getBody());
  
    System.out.println("Test invalid chatroom genre");
  }
 
  /**
  * This is a test case to evaluate the leaveroom endpoint.
  */
  @Test
  @Order(7)
  public void lasttoleaveRoomTest() {

    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.post("http://localhost:8080/joinroom/country").asString();
    response = Unirest.get("http://localhost:8080/chatroom/country").asString();  
    assertEquals(200, response.getStatus());

    JSONObject jsonObject = new JSONObject(response.getBody());
    Gson gson = new Gson();
    ChatRoom chatroom = gson.fromJson(jsonObject.toString(), ChatRoom.class);
    
    // Check if user is present after joinroom
    assertEquals(1, chatroom.getParticipant().size());
	
    response = Unirest.delete("http://localhost:" + port + "/leaveroom/country").body("username=sean").asString();
    response = Unirest.get("http://localhost:" + port + "/chatroom/country/?user=sean").asString(); 
    assertEquals(200, response.getStatus());
    System.out.println("/leaveroom/[user] Response: " + response.getBody());

    jsonObject = new JSONObject(response.getBody());
    chatroom = gson.fromJson(jsonObject.toString(), ChatRoom.class);
    
    // Check if user is not present after leaveroom
    assertEquals(0, chatroom.getParticipant().size());
  
    System.out.println("Test leave chatroom");
  }

  /**
  * This is a test case to evaluate the leaveroom endpoint.
  */
  @Test
  @Order(8)
  public void notLasttoleaveRoomTest() {

    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.post("http://localhost:8080/joinroom/country").asString();
    response = Unirest.get("http://localhost:8080/chatroom/country/").asString();  
    assertEquals(200, response.getStatus());
    
    JSONObject jsonObject = new JSONObject(response.getBody());
    Gson gson = new Gson();
    ChatRoom countryChatroom = gson.fromJson(jsonObject.toString(), ChatRoom.class);
    
    // Check if user is present after joinroom
    assertEquals(1, countryChatroom.getParticipant().size());
    
    response = Unirest.post("http://localhost:8080/joinroom/pop").asString();
    response = Unirest.get("http://localhost:8080/chatroom/pop").asString();  
    assertEquals(200, response.getStatus());

    jsonObject = new JSONObject(response.getBody());
    gson = new Gson();
    ChatRoom popChatroom = gson.fromJson(jsonObject.toString(), ChatRoom.class);
    
    // Check if user is present after joinroom
    assertEquals(1, popChatroom.getParticipant().size());
	
    response = Unirest.delete("http://localhost:8080/leaveroom/country").asString();
    response = Unirest.get("http://localhost:8080/chatroom/country").asString(); 
    assertEquals(200, response.getStatus());
    System.out.println("/leaveroom/[user] Response: " + response.getBody());

    jsonObject = new JSONObject(response.getBody());
    countryChatroom = gson.fromJson(jsonObject.toString(), ChatRoom.class);
    
    // Check if user is not present after leaveroom
    assertEquals(0, countryChatroom.getParticipant().size());
  
    System.out.println("Test leave chatroom");
  }

  /**
  * This is a test case to evaluate the leaveroom endpoint.
  */
  @Test
  @Order(9)
  public void invalidLeaveRoomTest() {
    String port = Integer.toString(getHerokuAssignedPort());
    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.delete("http://localhost:" + port + "/leaveroom/pop/").asString();
	
    assertEquals(200, response.getStatus());
    assertEquals("You are not in the room", response.getBody());
  
    System.out.println("Test invalid leave chatroom");
  }

  /**
  * This is a test case to evaluate the process_auth endpoint.
  */
  
  @Test
  @Order(10)
  public void processAuthTest() {
    String port = Integer.toString(getHerokuAssignedPort());
    // Create HTTP request and get response
    Unirest.config().reset();
    Unirest.config().followRedirects(false);
    
    HttpResponse<String> response = Unirest.get("http://localhost:8080/process_auth").asString();
    
    response = Unirest.get("http://localhost:8080/process_auth?code=123").asString();
	
    assertEquals(302, response.getStatus());
    
    assertEquals("New user", response.getBody());
	
    System.out.println("/process-auth Response: " + response.getBody());
    
    Unirest.config().reset();
    Unirest.config().followRedirects(true);
  }
  
  @Test
  @Order(11)
  public void processAuthTestNoSessionId() {
    StartChat.stop();
    StartChat.main(null);
    
    Unirest.config().reset();
    Unirest.config().followRedirects(false);

    HttpResponse<String> response = Unirest.get("http://localhost:8080/process_auth?code=123").asString();
    
    System.out.println("/process-auth no sessionId Response: " + response.getBody());
    
    assertEquals("New user", response.getBody());
    
    assertEquals(302, response.getStatus());
    Unirest.config().reset();
    Unirest.config().followRedirects(true);
  }
  
  @Test
  @Order(12)
  public void processAuthTestNoCode() {
    StartChat.stop();
    StartChat.main(null);
    
    Unirest.config().reset();
    Unirest.config().followRedirects(false);
    
    HttpResponse<String> response = Unirest.get("http://localhost:8080/process_auth").asString();
    
    System.out.println("/process-auth no code Response: " + response.getBody());
    
    assertEquals("No code", response.getBody());
    
    assertEquals(302, response.getStatus());
    
    response = Unirest.get("http://localhost:8080/").asString();
  
    System.out.println("Test process authorization");
    
    Unirest.config().reset();
    Unirest.config().followRedirects(true);
    
  }

  
  /**
  * This is a test case to evaluate the before endpoint.
  */
  @Test
  @Order(13)
  public void LoginRedirectionTest() {
    String port = Integer.toString(getHerokuAssignedPort());
    // reset sessionId to enable redirection
    StartChat.stop();
    StartChat.main(null);
    
    HttpResponse<String> response = Unirest.get("http://localhost:" + port + "/").asString();
    assertEquals(200, response.getStatus());
	
    System.out.println("/ Response: " + response.getBody());
    System.out.println("Test Login Redirection");
  }

  /**
  * This is a test case to evaluate the / endpoint.
  */
  @Test
  @Order(14)
  public void frontPageTest() {
    String port = Integer.toString(getHerokuAssignedPort());
    // Create HTTP request and get response

    HttpResponse<String> response = Unirest.get("http://localhost:" + port + "/").asString();
    
    assertEquals(200, response.getStatus());
  
    System.out.println("Test front page");
  }

  /**
  * This is a test case to evaluate initializeChatlist function.
  */
  @Test
  @Order(15)
  public void initializeChatListTest() {

    SqLite db = StartChat.getDb();
    db.clear();
    
    ChatList chatlist = new ChatList();
    assertEquals(0, chatlist.getChatrooms().size());
    StartChat.setChatlist(chatlist);
    StartChat.initializeChatlist();
    chatlist = StartChat.getChatlist();
    
    assertEquals(6, chatlist.getChatrooms().size());
  
    System.out.println("Test initializeChatlist function");
  }
  
  @Test
  @Order(16)
  public void sendMessageTest() {
    String port = Integer.toString(getHerokuAssignedPort());
    HttpResponse<String> response = Unirest.post("http://localhost:" + port + "/joinroom/blues/").asString();
    response = Unirest.get("http://localhost:" + port + "/chatroom/blues").asString();
    assertEquals(200, response.getStatus());

    HttpResponse<String> response1 = Unirest.post("http://localhost:" + port + "/send").body("text=hello").asString();
    response1 = Unirest.get("http://localhost:" + port + "/chatroom/blues").asString();
    System.out.println("/[chatroom]/[user] Response: " + response1.getBody());
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(response1.getBody());

    // GSON use to parse data to object
    Gson gson = new Gson();
    ChatRoom chatroom = gson.fromJson(jsonObject.toString(), ChatRoom.class);
    List<Message> msgList = chatroom.getChat();
    assertEquals(1, msgList.size());
    Message msg = msgList.get(0);
    System.out.println(msg.getUsername());
    assertEquals("testing1", msg.getUsername());
    assertEquals("hello", msg.getMessage());
  }

  @Test
  @Order(17)
  public void sendMessageInvalidUserTest() {
    String port = Integer.toString(getHerokuAssignedPort());
    HttpResponse<String> response = Unirest.post("http://localhost:" + port + "/send").body("text=hello").asString();
    assertEquals("User not in any chatroom", response.getBody());
  }
  
  @Test
  @Order(18)
  public void webSocketTest() {
	  String port = Integer.toString(getHerokuAssignedPort());
    WebSocketClient client = new WebSocketClient();
    SimpleWebSocket socket = new SimpleWebSocket();
    try {
      client.start();
      URI uri = new URI("ws://localhost:" + port + "/chatroom");
      //ClientUpgradeRequest request = new ClientUpgradeRequest();
      Future<Session> future = client.connect(socket, uri);
      System.out.printf("Connecting to : %s%n", uri);
      Session session = future.get();
      session.getRemote().sendString("hi");
      session.close();

      // wait for closed socket connection.
      socket.awaitClose(5, TimeUnit.SECONDS);
    } catch (Throwable t) {
      t.printStackTrace();
    } finally {
      try {
        client.stop();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Test
  @Order(18)
  public void sendJsonToAllParticipants() {
	
    WebSocketClient client = new WebSocketClient();
    SimpleWebSocket socket = new SimpleWebSocket();
    try {
      client.start();
      URI uri = new URI("ws://localhost:8080/chatroom");
      //ClientUpgradeRequest request = new ClientUpgradeRequest();
      Future<Session> future = client.connect(socket, uri);
      System.out.printf("Connecting to : %s%n", uri);
      Session session = future.get();
      session.getRemote().sendString("hi");
      
      
      HttpResponse<String> response = Unirest.post("http://localhost:8080/joinroom/blues/").asString();
      response = Unirest.get("http://localhost:8080/chatroom/blues").asString();
      assertEquals(200, response.getStatus());

      HttpResponse<String> response1 = Unirest.post("http://localhost:8080/send").body("text=hello").asString();
      response1 = Unirest.get("http://localhost:8080/chatroom/blues").asString();
      System.out.println("/[chatroom]/[user] Response: " + response1.getBody());
      // Parse the response to JSON object
      JSONObject jsonObject = new JSONObject(response1.getBody());

      // GSON use to parse data to object
      Gson gson = new Gson();
      ChatRoom chatroom = gson.fromJson(jsonObject.toString(), ChatRoom.class);
      List<Message> msgList = chatroom.getChat();
      assertEquals(1, msgList.size());
      Message msg = msgList.get(0);
      System.out.println(msg.getUsername());
      assertEquals("testing1", msg.getUsername());
      assertEquals("hello", msg.getMessage());

      session.close();
      // wait for closed socket connection.
      socket.awaitClose(5, TimeUnit.SECONDS);
    } catch (Throwable t) {
      t.printStackTrace();
    } finally {
      try {
        client.stop();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Test
  @Order(19)
  public void shareSongNullTest() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/joinroom/blues/").asString();
    response = Unirest.get("http://localhost:8080/chatroom/blues").asString();
    assertEquals(200, response.getStatus());
    
    SqLite mockDb = mock(SqLite.class);
    User mockUser = mock(User.class);
    when(mockUser.getUsername()).thenReturn("testing1");
    when(mockUser.getCurrentTrack()).thenReturn(null);
    
    SqLite origDb = StartChat.getDb();
    String sessionId = origDb.getLatestSession();
    System.out.println(mockDb.getUserBySessionId(sessionId));
    
    when(mockDb.getUserBySessionId(sessionId)).thenReturn(mockUser);
    when(mockDb.getUserByName("testing1")).thenReturn(mockUser);
    when(mockUser.refreshCurrentlyPlaying()).thenReturn("OK");
    
    StartChat.setDb(mockDb);

    HttpResponse<String> response1 = Unirest.post("http://localhost:8080/share").asString();
    System.out.println("/share Response: " + response1.getBody());

    assertEquals("no song shared", response1.getBody());
    
    StartChat.setDb(origDb);
  }

  @Test
  @Order(20)
  public void shareSongNoGenreTest() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/joinroom/blues/").asString();
    response = Unirest.get("http://localhost:8080/chatroom/blues").asString();
    assertEquals(200, response.getStatus());
    
    SqLite mockDb = mock(SqLite.class);
    User mockUser = mock(User.class);
    Song song = new Song();
    song.setUsername("testing1");
    song.setName("my song");
    String[] artists = new String[1];
    artists[0] = "my artist";
    song.setArtists(artists);
    song.setUri("my uri");
    when(mockUser.getUsername()).thenReturn("testing1");
    when(mockUser.getCurrentTrack()).thenReturn(song);
    
    SqLite origDb = StartChat.getDb();
    String sessionId = origDb.getLatestSession();
    
    when(mockDb.getUserBySessionId(sessionId)).thenReturn(mockUser);
    when(mockDb.getUserByName("testing1")).thenReturn(mockUser);
    when(mockUser.refreshCurrentlyPlaying()).thenReturn("OK");
    
    StartChat.setDb(mockDb);

    HttpResponse<String> response1 = Unirest.post("http://localhost:8080/share").asString();
    System.out.println("/share Response: " + response1.getBody());

    assertEquals("no song shared", response1.getBody());
    
    StartChat.setDb(origDb);
  }

  @Test
  @Order(21)
  public void shareSongNotNullTest() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/joinroom/blues/").asString();
    response = Unirest.get("http://localhost:8080/chatroom/blues").asString();
    assertEquals(200, response.getStatus());
    
    SqLite origDb = StartChat.getDb();
    String sessionId = origDb.getLatestSession();
    
    SqLite mockDb = mock(SqLite.class);
    User mockUser1 = mock(User.class);
    Message message = new Message();
    message.setMessage("I shared xxx song");
    
    ChatList mockChatlist = mock(ChatList.class);

    StartChat.setDb(mockDb);
    StartChat.setChatlist(mockChatlist);
    
    when(mockUser1.share(mockChatlist, mockDb)).thenReturn(message);
    when(mockUser1.getUsername()).thenReturn("testing1");
    Song song = new Song();
    song.setName("song name");
    song.setUri("uri");
    when(mockUser1.getCurrentTrack()).thenReturn(song);
    
    when(mockDb.getUserBySessionId(sessionId)).thenReturn(mockUser1);
    when(mockDb.getUserByName("testing1")).thenReturn(mockUser1);
    when(mockDb.getGenreUser("testing1")).thenReturn("blues");
    

    HttpResponse<String> response1 = Unirest.post("http://localhost:8080/share").asString();
    System.out.println("/share Response: " + response1.getBody());

    assertEquals("song shared", response1.getBody());
    
    StartChat.setDb(origDb);
  }

  @Test
  @Order(22)
  public void addSongTest() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/joinroom/blues/").asString();
    response = Unirest.get("http://localhost:8080/chatroom/blues").asString();
    assertEquals(200, response.getStatus());
    
    SqLite mockDb = mock(SqLite.class);
    User mockUser = mock(User.class);
    when(mockUser.getUsername()).thenReturn("testing1");
    when(mockUser.addToQueue("hello")).thenReturn("song added");
    
    SqLite origDb = StartChat.getDb();
    String sessionId = origDb.getLatestSession();
    
    
    when(mockDb.getUserBySessionId(sessionId)).thenReturn(mockUser);
    when(mockDb.getUserByName("testing1")).thenReturn(mockUser);
    
    StartChat.setDb(mockDb);

    HttpResponse<String> response1 = Unirest.post("http://localhost:8080/add").body("uri=hello").asString();
    System.out.println("/add Response: " + response1.getBody());

    assertEquals("song added to your queue", response1.getBody());
    
    StartChat.setDb(origDb);
  }

  @Test
  @Order(22)
  public void addSongTestNoSong() {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/joinroom/blues/").asString();
    response = Unirest.get("http://localhost:8080/chatroom/blues").asString();
    assertEquals(200, response.getStatus());
    
    SqLite mockDb = mock(SqLite.class);
    User mockUser = mock(User.class);
    when(mockUser.getUsername()).thenReturn("testing1");
    when(mockUser.addToQueue("hello")).thenReturn("song added");
    
    SqLite origDb = StartChat.getDb();
    String sessionId = origDb.getLatestSession();
    
    
    when(mockDb.getUserBySessionId(sessionId)).thenReturn(mockUser);
    when(mockDb.getUserByName("testing1")).thenReturn(mockUser);
    
    StartChat.setDb(mockDb);

    HttpResponse<String> response1 = Unirest.post("http://localhost:8080/add").asString();
    System.out.println("/add Response: " + response1.getBody());

    assertEquals("no song uri", response1.getBody());
    
    StartChat.setDb(origDb);
  }
  
  @Test
  @Order(23)
  public void authTest() {

    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.get("http://localhost:8080/auth").asString();
    SqLite db = StartChat.getDb();
    
    String sessionId = db.getLatestSession();

    db.insertAuthenticatedUser("authTest", "authToken", "authRefresh", sessionId);
    
    response = Unirest.get("http://localhost:8080/auth").asString();
    
    assertEquals("{}", response.getBody());
	
    assertEquals(200, response.getStatus());
    
    //assertEquals("New user", response.getBody());
	
    System.out.println("/auth Response: " + response.getBody());
    
    Unirest.config().reset();
    Unirest.config().followRedirects(true);
  }
  
  @Test
  @Order(24)
  public void authTestNoSessionId() {
    StartChat.stop();
    StartChat.main(null);
    
    Unirest.config().reset();
    Unirest.config().followRedirects(false);

    HttpResponse<String> response = Unirest.get("http://localhost:8080/auth").asString();
    
    System.out.println("/auth no sessionId Response: " + response.getBody());
    
    assertNotEquals("{}", response.getBody());
    
    assertEquals(200, response.getStatus());
    Unirest.config().reset();
    Unirest.config().followRedirects(true);
  }

  /**
  * This will run every time after a test has finished.
  */
  @AfterEach
  public void finishChat() {
    SqLite db = StartChat.getDb();
    db.clear();
    
    StartChat.stop();
    System.out.println("After Each");
  }

  /**
   * This method runs only once after all the test cases have been executed.
   */
  @AfterAll
  public static void close() {
    System.out.println("After All");
  }
}
