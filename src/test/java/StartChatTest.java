import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import controllers.StartChat;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import models.ChatList;
import models.ChatRoom;
import models.SqLite;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class) 
public class StartChatTest {

  /**
  * Runs only once before the testing starts.
  */
  @BeforeAll
  public static void init() {
    // Start Server
    //StartChat.main(null);
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
    HttpResponse<String> response = Unirest.get("http://localhost:8080/chatrooms/").asString();
    assertEquals(200, response.getStatus());
    
    SqLite db = StartChat.getDb();
    
    String sessionId = db.getLatestSession();
    
    db.insertUserWithSession("testing1", sessionId);
    db.commit();
    
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
    assertEquals(6, chatlist.size());
  
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

    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.get("http://localhost:8080/chatrooms/").asString();
    assertEquals(200, response.getStatus());
    
    // sessionId is new after restarting, need to log again
    SqLite db = StartChat.getDb();
    String sessionId = db.getLatestSession();
    db.insertUserWithSession("testing2", sessionId);
    db.commit();
    
    response = Unirest.get("http://localhost:8080/chatrooms/").asString();

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
    assertEquals(6, chatlist.size());
  
    System.out.println("Test /chatrooms");
  }

  /**
  * This is a test case to evaluate the joinroom endpoint.
  */
  @Test
  @Order(3)
  public void joinRoomTest() {

    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.get("http://localhost:8080/chatrooms/").asString();
    assertEquals(200, response.getStatus());
    
    SqLite db = StartChat.getDb();
    
    String sessionId = db.getLatestSession();
    
    //db.insertUserWithSession("testing3", sessionId);
    db.commit();
	response = Unirest.post("http://localhost:8080/joinroom/blues/").body("username=ben").asString();
    response = Unirest.get("http://localhost:8080/blues/?user=ben").asString();

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

    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.post("http://localhost:8080/joinroom/jazz/").body("username=ben").asString();
    response = Unirest.post("http://localhost:8080/joinroom/jazz/").body("username=mary").asString();
    response = Unirest.post("http://localhost:8080/joinroom/jazz/").body("username=ben").asString();
    response = Unirest.get("http://localhost:8080/jazz/?user=ben").asString();

    assertEquals(200, response.getStatus());

    // --------------------------- JSONObject Parsing ----------------------------------

    System.out.println("/joinroom Response: " + response.getBody());

    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(response.getBody());

    // ---------------------------- GSON Parsing -------------------------

    // GSON use to parse data to object
    Gson gson = new Gson();
    ChatRoom chatroom = gson.fromJson(jsonObject.toString(), ChatRoom.class);
    
    // Check if player type is correct
    assertEquals(2, chatroom.getParticipant().size());
  
    System.out.println("Test rejoining chatroom");
  }

  /**
  * This is a test case to evaluate the joinroom endpoint.
  */
  @Test
  @Order(5)
  public void invalidJoinRoomTest() {

    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.post("http://localhost:8080/joinroom/metal/?skip_auth_testing=true").body("username=ben").asString();
    
    assertEquals("Invalid Room", response.getBody());
  
    System.out.println("Test invalid join chatroom");
  }

  /**
  * This is a test case to evaluate the chatroom/genre endpoint.
  */
  @Test
  @Order(6)
  public void invalidChatroomGenreTest() {

    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.get("http://localhost:8080/metal/?user=ben&skip_auth_testing=true").asString();

    assertEquals(200, response.getStatus());
    assertEquals("Invalid Room", response.getBody());
  
    System.out.println("Test invalid chatroom genre");
  }
 
  /**
  * This is a test case to evaluate the leaveroom endpoint.
  */
  @Test
  @Order(7)
  public void leaveRoomTest() {

    // Create HTTP request and get response
	HttpResponse<String> response = Unirest.post("http://localhost:8080/joinroom/country/?skip_auth_testing=true").body("username=sean").asString();
	response = Unirest.get("http://localhost:8080/country/?user=sean&skip_auth_testing=true").asString();  
	assertEquals(200, response.getStatus());

    JSONObject jsonObject = new JSONObject(response.getBody());
    Gson gson = new Gson();
    ChatRoom chatroom = gson.fromJson(jsonObject.toString(), ChatRoom.class);
    
    // Check if user is present after joinroom
    assertEquals(true, chatroom.getParticipant().containsKey("sean"));
	
    response = Unirest.delete("http://localhost:8080/leaveroom/country/?skip_auth_testing=true").body("username=sean").asString();
    response = Unirest.get("http://localhost:8080/country/?user=sean&skip_auth_testing=true").asString(); 
    assertEquals(200, response.getStatus());
    System.out.println("/leaveroom/[user] Response: " + response.getBody());

    jsonObject = new JSONObject(response.getBody());
    chatroom = gson.fromJson(jsonObject.toString(), ChatRoom.class);
    
    // Check if user is not present after leaveroom
    assertEquals(false, chatroom.getParticipant().containsKey("sean"));
  
    System.out.println("Test leave chatroom");
  }

  /**
  * This is a test case to evaluate the leaveroom endpoint.
  */
  @Test
  @Order(8)
  public void invalidLeaveRoomTest() {

    // Create HTTP request and get response
	HttpResponse<String> response = Unirest.delete("http://localhost:8080/leaveroom/pop/?skip_auth_testing=true").body("username=taylor").asString();
	
	assertEquals(200, response.getStatus());
    assertEquals("You are not in the room", response.getBody());
  
    System.out.println("Test invalid leave chatroom");
  }

  /**
  * This is a test case to evaluate the process_auth endpoint.
  */
  @Test
  @Order(9)
  public void processAuthTest() {

    // Create HTTP request and get response
	HttpResponse<String> response = Unirest.get("http://localhost:8080/process_auth?code=123").asString();
	
	assertEquals(200, response.getStatus());
	
    StartChat.stop();
    StartChat.main(null);
    
    response = Unirest.get("http://localhost:8080/process_auth?code=123").asString();
    
    assertEquals(200, response.getStatus());
    
    response = Unirest.get("http://localhost:8080/process_auth").asString();
    
    assertEquals(200, response.getStatus());
  
    System.out.println("Test process authorization");
  }
  
  /**
  * This is a test case to evaluate the before endpoint.
  */
  @Test
  @Order(10)
  public void LoginRedirectionTest() {

    // reset sessionId to enable redirection
    StartChat.stop();
    StartChat.main(null);
    
	HttpResponse<String> response = Unirest.get("http://localhost:8080/").asString();
	
	assertEquals(200, response.getStatus());
	
	System.out.println("/ Response: " + response.getBody());
  
    System.out.println("Test Login Redirection");
  }

  /**
  * This is a test case to evaluate the / endpoint.
  */
  @Test
  @Order(11)
  public void frontPageTest() {

    // Create HTTP request and get response

    HttpResponse<String> response = Unirest.get("http://localhost:8080/").asString();
    
    assertEquals(200, response.getStatus());
    
    System.out.println("/chatrooms Response: " + response.getBody());

    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(response.getBody());

    // ---------------------------- GSON Parsing -------------------------

    // GSON use to parse data to object
    Gson gson = new Gson();
    ChatList chatlist = gson.fromJson(jsonObject.toString(), ChatList.class);
    
    // Check if player type is correct
    assertEquals(6, chatlist.size());
  
    System.out.println("Test front page");
  }
  
  /**
  * This will run every time after a test has finished.
  */
  @AfterEach
  public void finishChat() {
    SqLite db = StartChat.getDb();
    db.clear();
    db.commit();
    StartChat.stop();
    System.out.println("After Each");
  }

  /**
   * This method runs only once after all the test cases have been executed.
   */
  @AfterAll
  public static void close() {
    // Stop Server
    /*
    SqLite db = StartChat.getDb();
    db.clear();
    db.commit();
    StartChat.stop();
    */
    System.out.println("After All");
  }
}
