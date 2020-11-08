import static org.junit.jupiter.api.Assertions.assertEquals;

import controllers.PlayGame;
import models.GameBoard;
import models.Message;
import models.Move;
import models.Player;
import org.junit.jupiter.api.Test;


public class GameBoardTest {

  Player p1 = new Player() {
    {  
      setType('X');
      setId(1);
    }
  };
  
  Player p2 = new Player() {
    {  
      setType('O');
      setId(2);
    }
  };

  GameBoard board = new GameBoard() {
    {
      setP1(p1);
      setP2(p2);
      setGameStarted(true);
    }
  };

  /**
   *  Set dimension of board.
   */
  private static final int DIM = 3;

  @Test
  public void testSwitchTurn() {
    board.setTurn(1);
    board.switchTurn();
    assertEquals(2, board.getTurn());
    board.switchTurn();
    assertEquals(1, board.getTurn());
  }

  @Test
  public void testMoveBeforeGameStart() {
    Move move1 = new Move();
    final Message msg = new Message();
    
    move1.setPlayer(p1);
    move1.setMoveX(0);
    move1.setMoveY(0);
    
    board.setGameStarted(false);
    
    char[][] state = new char[DIM][DIM];
    board.setBoardState(state);
    board.setTurn(1);
    assertEquals(false, board.isValid(move1, msg));
    assertEquals(false, msg.getMoveValidity());
    assertEquals("Wait for Player 2 to join!", msg.getMessage());
  }

  @Test
  public void testMoveAfterWinner() {
    Move move1 = new Move();
    final Message msg = new Message();
    
    move1.setPlayer(p1);
    move1.setMoveX(0);
    move1.setMoveY(0);

    board.setWinner(1);
    
    char[][] state = new char[DIM][DIM];
    board.setBoardState(state);
    board.setTurn(1);
    assertEquals(false, board.isValid(move1, msg));
    assertEquals(false, msg.getMoveValidity());
    assertEquals("Game ended.", msg.getMessage());
  }

  @Test
  public void testMoveAfterDraw() {
    Move move1 = new Move();
    final Message msg = new Message();
    
    move1.setPlayer(p1);
    move1.setMoveX(0);
    move1.setMoveY(0);

    board.setIsDraw(true);
    
    char[][] state = new char[DIM][DIM];
    board.setBoardState(state);
    board.setTurn(1);
    assertEquals(false, board.isValid(move1, msg));
    assertEquals(false, msg.getMoveValidity());
    assertEquals("Game ended.", msg.getMessage());
  }

  @Test
  public void testInvalidTurn() { 
    Move move1 = new Move();
    final Message msg = new Message();

    move1.setPlayer(p1);
    move1.setMoveX(0);
    move1.setMoveY(0);

    char[][] state = new char[DIM][DIM];
    board.setBoardState(state);
    board.setTurn(2);
    assertEquals(false, board.isValid(move1, msg));
    assertEquals(false, msg.getMoveValidity());
    assertEquals("Not Your Turn!", msg.getMessage());
  }

  @Test
  public void testOutOfBoundX1Move() {
    final Move move = new Move();
    final Message msg = new Message();

    move.setPlayer(p1);
    move.setMoveX(-1);
    move.setMoveY(0);
    
    char[][] state = new char[DIM][DIM];
    board.setBoardState(state);
    board.setTurn(1);
    assertEquals(false, board.isValid(move, msg));
    assertEquals(false, msg.getMoveValidity());
    assertEquals("Invalid Row Index!", msg.getMessage());
  }

  @Test
  public void testOutOfBoundX2Move() {
    final Move move = new Move();
    final Message msg = new Message();

    move.setPlayer(p1);
    move.setMoveX(3);
    move.setMoveY(0);
    
    char[][] state = new char[DIM][DIM];
    board.setBoardState(state);
    board.setTurn(1);
    assertEquals(false, board.isValid(move, msg));
    assertEquals(false, msg.getMoveValidity());
    assertEquals("Invalid Row Index!", msg.getMessage());
  }

  @Test
  public void testOutOfBoundY1Move() {
    final Move move = new Move();
    final Message msg = new Message();

    move.setPlayer(p1);
    move.setMoveX(0);
    move.setMoveY(-1);
    
    char[][] state = new char[DIM][DIM];
    board.setBoardState(state);
    board.setTurn(1);
    assertEquals(false, board.isValid(move, msg));
    assertEquals(false, msg.getMoveValidity());
    assertEquals("Invalid Column Index!", msg.getMessage());
  }

  @Test
  public void testOutOfBoundY2Move() {
    final Move move = new Move();
    final Message msg = new Message();

    move.setPlayer(p1);
    move.setMoveX(0);
    move.setMoveY(3);
    
    char[][] state = new char[DIM][DIM];
    board.setBoardState(state);
    board.setTurn(1);
    assertEquals(false, board.isValid(move, msg));
    assertEquals(false, msg.getMoveValidity());
    assertEquals("Invalid Column Index!", msg.getMessage());
  }

  @Test
  public void testValidMove() {
    final Move move = new Move();
    final Message msg = new Message();

    move.setPlayer(p1);
    move.setMoveX(0);
    move.setMoveY(0);
    
    char[][] state = new char[DIM][DIM];
    board.setBoardState(state);
    board.setTurn(1);
    assertEquals(true, board.isValid(move, msg));
    assertEquals(true, msg.getMoveValidity());
  }

  @Test
  public void testInvalidMove() {
    final Move move1 = new Move();
    final Move move2 = new Move();
    final Message msg = new Message();
    
    move1.setPlayer(p1);
    move1.setMoveX(0);
    move1.setMoveY(0);
    move2.setPlayer(p2);
    move2.setMoveX(0);
    move2.setMoveY(0);
    
    char[][] state = new char[DIM][DIM];
    board.setBoardState(state);
    board.makeMove(move1);
    board.setTurn(2);
    assertEquals(false, board.isValid(move2, msg));
    assertEquals(false, msg.getMoveValidity());
    assertEquals("Invalid Move. Try Again!", msg.getMessage());
  }
  
  @Test
  public void testMakeMove() {

    Move move = new Move();
    board.setTurn(2);
    move.setPlayer(p2);
    move.setMoveX(1);
    move.setMoveY(1);

    char[][] state = new char[DIM][DIM];
    board.setBoardState(state);
    assertEquals('\u0000', board.getBoardState()[move.getMoveX()][move.getMoveY()]);
    board.makeMove(move);
    assertEquals('O', board.getBoardState()[move.getMoveX()][move.getMoveY()]);
  }
  
  @Test
  public void testgetPlayer1FromId() {
    assertEquals(p1, board.getPlayerFromId(1)); 
  }

  @Test
  public void testgetPlayer2FromId() {
    assertEquals(p2, board.getPlayerFromId(2)); 
  }
  
  @Test
  public void testWinnerRow() {
    Move move = new Move();

    // three in a row
    char[][] state = new char[DIM][DIM];
    board.setBoardState(state);
    move.setPlayer(p1);
    move.setMoveX(0);
    for (int i = 0; i < 3; i++) {
      move.setMoveY(i);
      board.makeMove(move);
    }
    assertEquals(true, board.isOver(move));
    assertEquals(1, board.getWinner());
  }

  @Test
  public void testWinnerColumn() {
    Move move = new Move();

    char[][] state = new char[DIM][DIM];
    board.setBoardState(state);
    move.setPlayer(p2);
    move.setMoveY(0);
    for (int i = 0; i < 3; i++) {
      move.setMoveX(i);
      board.makeMove(move);
    }
    assertEquals(true, board.isOver(move));
    assertEquals(2, board.getWinner());
  }

  @Test
  public void testWinnerDiagonalRight() {
    Move move = new Move();
    
    char[][] state = new char[DIM][DIM];
    board.setBoardState(state);
    move.setPlayer(p2);
    for (int i = 0; i < 3; i++) {
      move.setMoveY(i);
      move.setMoveX(i);
      board.makeMove(move);
    }
    assertEquals(true, board.isOver(move));
    assertEquals(2, board.getWinner());
  }

  @Test
  public void testWinnerDiagonalLeft() {
    Move move = new Move();

    char[][] state = new char[DIM][DIM];
    board.setBoardState(state);
    move.setPlayer(p1);
    for (int i = 0; i < 3; i++) {
      move.setMoveY(i);
      move.setMoveX(2 - i);
      board.makeMove(move);
    }
    assertEquals(true, board.isOver(move));
    assertEquals(1, board.getWinner());
  }

  @Test
  public void testDraw() {
    Move move = new Move();
  
    char[][] state = new char[DIM][DIM];
    board.setBoardState(state);
    move.setPlayer(p1);
    move.setMoveX(0);
    for (int i = 0; i < 3; i++) {
      if (i == 2) {
        move.setPlayer(p2);
      }
      move.setMoveY(i);
      board.makeMove(move);
    }
    move.setPlayer(p2);
    move.setMoveX(1);
    for (int i = 0; i < 3; i++) {
      if (i == 2) {
        move.setPlayer(p1);
      }
      move.setMoveY(i);
      board.makeMove(move);
    }
    move.setPlayer(p1);
    move.setMoveX(2);
    for (int i = 0; i < 3; i++) {
      if (i == 2) {
        move.setPlayer(p2);
      }
      move.setMoveY(i);
      board.makeMove(move);
    }
    assertEquals(true, board.isOver(move));
    assertEquals(true, board.getIsDraw());
  }

  @Test
  public void testNotOverDiagonalLeft() {
    Move move = new Move();

    char[][] state = new char[DIM][DIM];
    board.setBoardState(state);
    move.setPlayer(p1);
    move.setMoveY(0);
    move.setMoveX(0);
    board.makeMove(move);
    for (int i = 1; i < 3; i++) {
      move.setMoveY(i);
      move.setMoveX(2 - i);
      board.makeMove(move);
    }
    assertEquals(false, board.isOver(move));
  }

  @Test
  public void testNotOverDiagonalRight() {
    Move move = new Move();
    
    char[][] state = new char[DIM][DIM];
    board.setBoardState(state);
    move.setPlayer(p2);
    move.setMoveY(0);
    move.setMoveX(1);
    board.makeMove(move);
    
    for (int i = 0; i < 2; i++) {
      move.setMoveY(i);
      move.setMoveX(i);
      board.makeMove(move);
    }
    assertEquals(false, board.isOver(move));
  }

  @Test
  public void testPlayGameConstructor() {
    PlayGame.make();
  }

}