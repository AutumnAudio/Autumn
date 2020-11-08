import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import controllers.PlayGame;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import models.GameBoard;
import models.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class) 
public class GameTest {

  /**
  * Runs only once before the testing starts.
  */
  @BeforeAll
  public static void init() {
    // Start Server
    PlayGame.main(null);
    System.out.println("Before All");
  }

  /**
  * This method starts a new game before every test run. It will run every time before a test.
  */
  @BeforeEach
  public void startNewGame() {
    // Test if server is running. You need to have an endpoint /
    // If you do not wish to have this end point, it is okay to not have anything in this method. 
    HttpResponse<String> response = Unirest.post("http://localhost:8080/").asString();
    int restStatus = response.getStatus();

    assertEquals(200, restStatus);
    System.out.println("Before Each");
  }

  /**
  * This is a test case to evaluate the newgame endpoint.
  */
  @Test
  @Order(1)
  public void newGameTest() {

    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.get("http://localhost:8080/newgame").asString();
    int restStatus = response.getStatus();

    // Check assert statement (New Game has started)
    assertEquals(200, restStatus);
    System.out.println("Test New Game");
  }

  /**
  * This is a test case to evaluate the startgame endpoint.
  */
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
  
  /**
  * This is a test case for when player 1 moves before player 2 join the game.
  */
  @Test
  @Order(3)
  public void moveBeforePlayer2JoinTest() {

    // Create a POST request to move endpoint and get the body
    // Remember to use asString() only once for an endpoint call. Every time you call asString()
    //a new request will be sent to the endpoint. Call it once and then use the data in the object.
    HttpResponse<String> response = Unirest.get("http://localhost:8080/newgame").asString();
    assertEquals(200, response.getStatus());
    response = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    assertEquals(200, response.getStatus());
    response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    assertEquals(200, response.getStatus());

    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(response.getBody());

    // Check if move is valid with correct code
    assertEquals(false, jsonObject.get("moveValidity"));
    assertEquals("Wait for Player 2 to join!", jsonObject.get("message"));
    assertEquals(100, jsonObject.get("code"));

    System.out.println("Test 1b: Player 1 moves before Player 2 joined.");
  }

  /**
  * This is a test case for when player 1 moves after player 2 join the game.
  */
  @Test
  @Order(4)
  public void moveAfterPlayer2JoinTest() {

    // Create a POST request to move endpoint and get the body
    // Remember to use asString() only once for an endpoint call. Every time you call asString()
    //a new request will be sent to the endpoint. Call it once and then use the data in the object.
    HttpResponse<String> response = Unirest.get("http://localhost:8080/newgame").asString();
    assertEquals(200, response.getStatus());
    response = Unirest.post("http://localhost:8080/startgame").body("type=X").asString();
    assertEquals(200, response.getStatus());
    response = Unirest.get("http://localhost:8080/joingame").asString();
    assertEquals(200, response.getStatus());
    response = Unirest.get("http://localhost:8080/gameboard").asString();
    assertEquals(200, response.getStatus());

    JSONObject jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("gameStarted"));
    
    // ---------------------------- GSON Parsing -------------------------

    // GSON use to parse data to object
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    Player player1 = gameBoard.getP1();
    Player player2 = gameBoard.getP2();
    
    // Check if player type is correct
    assertEquals('X', player1.getType());
    assertEquals('O', player2.getType());

    // -------------------------- After game started ----------------------------------

    response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    assertEquals(200, response.getStatus());
    jsonObject = new JSONObject(response.getBody());

    // Check if move is valid with correct code
    assertEquals(true, jsonObject.get("moveValidity"));
    assertEquals(100, jsonObject.get("code"));

    System.out.println("Test 1b: Player 1 moves after Player 2 joined.");
  }

  /**
  * This is a test case for when player 2 moves first after game begins.
  */
  @Test
  @Order(5)
  public void player2MovesFirstTest() {

    // Create a POST request to move endpoint and get the body
    // Remember to use asString() only once for an endpoint call. Every time you call asString()
    //a new request will be sent to the endpoint. Call it once and then use the data in the object.
    HttpResponse<String> response = Unirest.get("http://localhost:8080/newgame").asString();
    assertEquals(200, response.getStatus());
    response = Unirest.post("http://localhost:8080/startgame").body("type=O").asString();
    assertEquals(200, response.getStatus());
    response = Unirest.get("http://localhost:8080/joingame").asString();
    assertEquals(200, response.getStatus());
    response = Unirest.get("http://localhost:8080/gameboard").asString();
    assertEquals(200, response.getStatus());

    JSONObject jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("gameStarted"));
 
    // -------------------------- After game started ----------------------------------
    
    response = Unirest.post("http://localhost:8080/move/2").body("x=0&y=0").asString();
    assertEquals(200, response.getStatus());
    jsonObject = new JSONObject(response.getBody());
    assertEquals(false, jsonObject.get("moveValidity"));
    assertEquals("Not Your Turn!", jsonObject.get("message"));
    assertEquals(200, jsonObject.get("code"));

    System.out.println("Test 2a: Player 2 moves first after game begins.");
  }
  
  /**
  * This is a test case to evaluate the move endpoint for valid move.
  */
  @Test
  @Order(6)
  public void player1MovesFirstTest() {

    // Create a POST request to move endpoint and get the body
    // Remember to use asString() only once for an endpoint call. Every time you call asString()
    //a new request will be sent to the endpoint. Call it once and then use the data in the object.
    HttpResponse<String> response = Unirest.get("http://localhost:8080/newgame").asString();
    assertEquals(200, response.getStatus());
    response = Unirest.post("http://localhost:8080/startgame").body("type=O").asString();
    assertEquals(200, response.getStatus());
    response = Unirest.get("http://localhost:8080/joingame").asString();
    assertEquals(200, response.getStatus());
    response = Unirest.get("http://localhost:8080/gameboard").asString();
    assertEquals(200, response.getStatus());
    
    JSONObject jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("gameStarted"));
 
    // -------------------------- After game started ----------------------------------
    
    response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    assertEquals(200, response.getStatus());
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));
    assertEquals(100, jsonObject.get("code"));

    System.out.println("Test 2b: Player 1 moves first after game begins.");
  }

  /**
  * This is a test case for when player 1 makes two moves in a row.
  */
  @Test
  @Order(7)
  public void player1MakesTwoMovesTest() {

    // Create a POST request to move endpoint and get the body
    // Remember to use asString() only once for an endpoint call. Every time you call asString()
    //a new request will be sent to the endpoint. Call it once and then use the data in the object.
    HttpResponse<String> response = Unirest.get("http://localhost:8080/newgame").asString();
    assertEquals(200, response.getStatus());
    response = Unirest.post("http://localhost:8080/startgame").body("type=O").asString();
    assertEquals(200, response.getStatus());
    response = Unirest.get("http://localhost:8080/joingame").asString();
    assertEquals(200, response.getStatus());

    response = Unirest.post("http://localhost:8080/move/1").body("x=1&y=1").asString();
    assertEquals(200, response.getStatus());    
    JSONObject jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));

    // Second move in a row. Test validity
    response = Unirest.post("http://localhost:8080/move/1").body("x=1&y=2").asString();
    assertEquals(200, response.getStatus());
    jsonObject = new JSONObject(response.getBody());
    assertEquals(false, jsonObject.get("moveValidity"));
    assertEquals("Not Your Turn!", jsonObject.get("message"));
    assertEquals(112, jsonObject.get("code"));

    System.out.println("Test 3a: Player 1 makes two moves in a row.");
  }

  /**
  * This is a test case for when player 2 makes two moves in a row.
  */
  @Test
  @Order(8)
  public void player2MakesTwoMovesTest() {

    // Create a POST request to move endpoint and get the body
    // Remember to use asString() only once for an endpoint call. Every time you call asString()
    //a new request will be sent to the endpoint. Call it once and then use the data in the object.
    HttpResponse<String> response = Unirest.get("http://localhost:8080/newgame").asString();
    assertEquals(200, response.getStatus());
    response = Unirest.post("http://localhost:8080/startgame").body("type=O").asString();
    assertEquals(200, response.getStatus());
    response = Unirest.get("http://localhost:8080/joingame").asString();
    assertEquals(200, response.getStatus());
    
    response = Unirest.post("http://localhost:8080/move/1").body("x=1&y=1").asString();
    assertEquals(200, response.getStatus());    
    JSONObject jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));
    
    
    response = Unirest.post("http://localhost:8080/move/2").body("x=1&y=2").asString();
    assertEquals(200, response.getStatus());
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));

    // Second move in a row. Test validity
    response = Unirest.post("http://localhost:8080/move/2").body("x=1&y=0").asString();
    assertEquals(200, response.getStatus());
    jsonObject = new JSONObject(response.getBody());
    assertEquals(false, jsonObject.get("moveValidity"));
    assertEquals("Not Your Turn!", jsonObject.get("message"));
    assertEquals(210, jsonObject.get("code"));

    System.out.println("Test 3b: Player 2 makes two moves in a row");
  }

  /**
  * This is a test case if game can have winner.
  */
  @Test
  @Order(9)
  public void winnerTest() {

    // Create a POST request to move endpoint and get the body
    // Remember to use asString() only once for an endpoint call. Every time you call asString()
    //a new request will be sent to the endpoint. Call it once and then use the data in the object.
    HttpResponse<String> response = Unirest.get("http://localhost:8080/newgame").asString();
    assertEquals(200, response.getStatus());
    response = Unirest.post("http://localhost:8080/startgame").body("type=O").asString();
    assertEquals(200, response.getStatus());
    response = Unirest.get("http://localhost:8080/joingame").asString();
    assertEquals(200, response.getStatus());

    response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    assertEquals(200, response.getStatus());    
    JSONObject jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));

    response = Unirest.post("http://localhost:8080/move/2").body("x=1&y=0").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));

    response = Unirest.post("http://localhost:8080/move/1").body("x=1&y=1").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));

    response = Unirest.post("http://localhost:8080/move/2").body("x=2&y=0").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));

    response = Unirest.post("http://localhost:8080/move/1").body("x=2&y=2").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));
    
    response = Unirest.get("http://localhost:8080/gameboard").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(1, jsonObject.get("winner"));

    System.out.println("Test 4a: Game can have winner.");
  }

  /**
  * This is a test case for move after winner is determined.
  */
  @Test
  @Order(10)
  public void moveAfterWinnerTest() {

    // Create a POST request to move endpoint and get the body
    // Remember to use asString() only once for an endpoint call. Every time you call asString()
    //a new request will be sent to the endpoint. Call it once and then use the data in the object.
    HttpResponse<String> response = Unirest.get("http://localhost:8080/newgame").asString();
    assertEquals(200, response.getStatus());
    response = Unirest.post("http://localhost:8080/startgame").body("type=O").asString();
    assertEquals(200, response.getStatus());
    response = Unirest.get("http://localhost:8080/joingame").asString();
    assertEquals(200, response.getStatus());

    response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    assertEquals(200, response.getStatus());    
    JSONObject jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));

    response = Unirest.post("http://localhost:8080/move/2").body("x=1&y=0").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));

    response = Unirest.post("http://localhost:8080/move/1").body("x=1&y=1").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));

    response = Unirest.post("http://localhost:8080/move/2").body("x=2&y=0").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));

    response = Unirest.post("http://localhost:8080/move/1").body("x=2&y=2").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));
    
    response = Unirest.get("http://localhost:8080/gameboard").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(1, jsonObject.get("winner"));

    // Test move after winner is determined
    response = Unirest.post("http://localhost:8080/move/2").body("x=2&y=1").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(false, jsonObject.get("moveValidity"));
    assertEquals("Game ended.", jsonObject.get("message"));
    assertEquals(221, jsonObject.get("code"));

    System.out.println("Test 4b: No moves allowed after winner is determined.");
  }

  /**
  * This is a test case for evaluating a draw when no moves left.
  */
  @Test
  @Order(11)
  public void drawTest() {

    // Create a POST request to move endpoint and get the body
    // Remember to use asString() only once for an endpoint call. Every time you call asString()
    //a new request will be sent to the endpoint. Call it once and then use the data in the object.
    HttpResponse<String> response = Unirest.get("http://localhost:8080/newgame").asString();
    assertEquals(200, response.getStatus());
    response = Unirest.post("http://localhost:8080/startgame").body("type=O").asString();
    assertEquals(200, response.getStatus());
    response = Unirest.get("http://localhost:8080/joingame").asString();
    assertEquals(200, response.getStatus());
    
    response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    assertEquals(200, response.getStatus());    
    JSONObject jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));

    response = Unirest.post("http://localhost:8080/move/2").body("x=0&y=2").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));

    response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=1").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));

    response = Unirest.post("http://localhost:8080/move/2").body("x=1&y=0").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));

    response = Unirest.post("http://localhost:8080/move/1").body("x=1&y=2").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));

    response = Unirest.post("http://localhost:8080/move/2").body("x=1&y=1").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));

    response = Unirest.post("http://localhost:8080/move/1").body("x=2&y=0").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));

    response = Unirest.post("http://localhost:8080/move/2").body("x=2&y=2").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));

    response = Unirest.post("http://localhost:8080/move/1").body("x=2&y=1").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));
    
    response = Unirest.get("http://localhost:8080/gameboard").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("isDraw"));

    System.out.println("Test 5a: Game can lead to draw.");
  }

  /**
  * This is a test case for when player attempts to move after a draw.
  */
  @Test
  @Order(12)
  public void moveAfterDrawTest() {

    // Create a POST request to move endpoint and get the body
    // Remember to use asString() only once for an endpoint call. Every time you call asString()
    //a new request will be sent to the endpoint. Call it once and then use the data in the object.
    HttpResponse<String> response = Unirest.get("http://localhost:8080/newgame").asString();
    assertEquals(200, response.getStatus());
    response = Unirest.post("http://localhost:8080/startgame").body("type=O").asString();
    assertEquals(200, response.getStatus());
    response = Unirest.get("http://localhost:8080/joingame").asString();
    assertEquals(200, response.getStatus());
    
    response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    assertEquals(200, response.getStatus());    
    JSONObject jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));
    
    response = Unirest.post("http://localhost:8080/move/2").body("x=0&y=2").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));

    response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=1").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));

    response = Unirest.post("http://localhost:8080/move/2").body("x=1&y=0").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));

    response = Unirest.post("http://localhost:8080/move/1").body("x=1&y=2").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));

    response = Unirest.post("http://localhost:8080/move/2").body("x=1&y=1").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));

    response = Unirest.post("http://localhost:8080/move/1").body("x=2&y=0").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));

    response = Unirest.post("http://localhost:8080/move/2").body("x=2&y=2").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));

    response = Unirest.post("http://localhost:8080/move/1").body("x=2&y=1").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));
    
    response = Unirest.get("http://localhost:8080/gameboard").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("isDraw"));

    // Move after game is a draw
    response = Unirest.post("http://localhost:8080/move/2").body("x=2&y=1").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(false, jsonObject.get("moveValidity"));
    assertEquals("Game ended.", jsonObject.get("message"));
    assertEquals(221, jsonObject.get("code"));

    System.out.println("Test 5b: No moves allowed after game is a draw.");
  }

  /**
  * This is a test case for crash.
  */
  @Test
  @Order(12)
  public void crashTest() {

    // Create a POST request to move endpoint and get the body
    // Remember to use asString() only once for an endpoint call. Every time you call asString()
    //a new request will be sent to the endpoint. Call it once and then use the data in the object.
    HttpResponse<String> response = Unirest.get("http://localhost:8080/newgame").asString();
    assertEquals(200, response.getStatus());
    response = Unirest.post("http://localhost:8080/startgame").body("type=O").asString();
    assertEquals(200, response.getStatus());
    response = Unirest.get("http://localhost:8080/joingame").asString();
    assertEquals(200, response.getStatus());
    
    response = Unirest.post("http://localhost:8080/move/1").body("x=0&y=0").asString();
    assertEquals(200, response.getStatus());    
    JSONObject jsonObject = new JSONObject(response.getBody());
    assertEquals(true, jsonObject.get("moveValidity"));
    
    PlayGame.stop();
    PlayGame.main(null);

    response = Unirest.get("http://localhost:8080/newgame").asString();
    assertEquals(200, response.getStatus());
    response = Unirest.post("http://localhost:8080/move/2").body("x=0&y=0").asString();
    assertEquals(200, response.getStatus());    
    jsonObject = new JSONObject(response.getBody());
    assertEquals(false, jsonObject.get("moveValidity"));
    assertEquals("Invalid Move. Try Again!", jsonObject.get("message"));

    System.out.println("Test: Tested crash.");
  }

  /**
  * This is a test case for /echo.
  */
  @Test
  @Order(13)
  public void echoTest() {

    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.post("http://localhost:8080/echo").asString();
    int restStatus = response.getStatus();

    // Check assert statement (New Game has started)
    assertEquals(200, restStatus);
    System.out.println("Test Echo");
  }

  /**
  * This will run every time after a test has finished.
  */
  @AfterEach
  public void finishGame() {
    System.out.println("After Each");
  }

  /**
   * This method runs only once after all the test cases have been executed.
   */
  @AfterAll
  public static void close() {
    // Stop Server
    PlayGame.stop();
    System.out.println("After All");
  }
}
