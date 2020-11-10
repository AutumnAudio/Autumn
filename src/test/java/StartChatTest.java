import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import controllers.StartChat;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import models.ChatList;
import models.ChatRoom;

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
    StartChat.main(null);
    System.out.println("Before All");
  }

  /**
  * This method starts a new game before every test run. It will run every time before a test.
  */
  @BeforeEach
  public void startAllChats() {
    // Test if server is running. You need to have an endpoint /
    // If you do not wish to have this end point, it is okay to not have anything in this method. 
    HttpResponse<String> response = Unirest.post("http://localhost:8080/").asString();
    int restStatus = response.getStatus();

    assertEquals(200, restStatus);
    System.out.println("Before Each");
  }

  /**
  * This is a test case to evaluate the chatrooms endpoint.
  */
  @Test
  @Order(1)
  public void chatroomsTest() {

    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.get("http://localhost:8080/chatrooms").asString();
    int restStatus = response.getStatus();

    // Check assert statement (New Game has started)
    assertEquals(200, restStatus);

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
    HttpResponse<String> response = Unirest.get("http://localhost:8080/chatrooms").asString();
    int restStatus = response.getStatus();

    // Check assert statement (New Game has started)
    assertEquals(200, restStatus);

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
	HttpResponse<String> response = Unirest.post("http://localhost:8080/joinroom/blues").body("username=ben").asString();
    response = Unirest.get("http://localhost:8080/blues?user=ben").asString();
    int restStatus = response.getStatus();

    // Check assert statement (New Game has started)
    assertEquals(200, restStatus);

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
    HttpResponse<String> response = Unirest.post("http://localhost:8080/joinroom/jazz").body("username=ben").asString();
    response = Unirest.post("http://localhost:8080/joinroom/jazz").body("username=mary").asString();
    response = Unirest.post("http://localhost:8080/joinroom/jazz").body("username=ben").asString();
    response = Unirest.get("http://localhost:8080/jazz?user=ben").asString();
    int restStatus = response.getStatus();

    // Check assert statement (New Game has started)
    assertEquals(200, restStatus);

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
  @Order(4)
  public void invalidJoinRoomTest() {

    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.post("http://localhost:8080/joinroom/metal").body("username=ben").asString();
    response = Unirest.get("http://localhost:8080/metal?user=ben").asString();
    int restStatus = response.getStatus();

    // Check assert statement (New Game has started)
    assertEquals(200, restStatus);
    
    assertEquals("Invalid Room", response.getBody());
  
    System.out.println("Test invalid chatroom");
  }

  /**
  * This is a test case to evaluate the startgame endpoint.
  */
/*
  @Test
  @Order(2)
  public void startGameTest() {

    // Create a POST request to startgame endpoint and get the body
    // Remember to use asString() only once for an endpoint call. Every time you call asString()  
    //a new request will be sent to the endpoint. Call it once and then use the data in the object.
    HttpResponse<String> response = Unirest.get("http://localhost:8080/newgame").asString();
    response = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    String responseBody = response.getBody();
    
    int restStatus = response.getStatus();
    assertEquals(200, restStatus);

    // --------------------------- JSONObject Parsing ----------------------------------

    System.out.println("Start Game Response: " + responseBody);

    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);

    // Check if game started after player 1 joins: Game should not start at this point
    assertEquals(false, jsonObject.get("gameStarted"));

    // ---------------------------- GSON Parsing -------------------------

    // GSON use to parse data to object
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    Player player1 = gameBoard.getP1();
    
    // Check if player type is correct
    assertEquals('X', player1.getType());

    System.out.println("Test Start Game");
  }
*/
  /**
  * This will run every time after a test has finished.
  */
  @AfterEach
  public void finishChat() {
    System.out.println("After Each");
  }

  /**
   * This method runs only once after all the test cases have been executed.
   */
  @AfterAll
  public static void close() {
    // Stop Server
    StartChat.stop();
    System.out.println("After All");
  }
}
