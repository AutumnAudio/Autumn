package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqLite {

  /**
   * Connection to database.
   */
  private Connection conn = null;

  /**
   * database statement for execution.
   */
  private Statement stmt = null;

  /**
   * Dimension Constant for boardState.
   */
  private static final int DIM = 3;

  /**
   * Start database.
   */
  public void start() {
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    try {
      conn = DriverManager.getConnection("jdbc:sqlite:tic-tac-toe.db");
      conn.setAutoCommit(false);
      stmt = conn.createStatement();
      String sql = "CREATE TABLE IF NOT EXISTS GAMEBOARD "
                     + " (PLAYER         INT NOT NULL, "
                     + " ROW             INT NOT NULL, "
                     + " COLUMN          INT NOT NULL, "
                     + " WINNER          INT, "
                     + " DRAW            INT, "
                     + " CONSTRAINT POSITION PRIMARY KEY (ROW, COLUMN) ) ";
      stmt.executeUpdate(sql);
      sql = "CREATE TABLE IF NOT EXISTS PLAYER "
              + " (ID         INT PRIMARY KEY NOT NULL, "
              + " TYPE        CHAR            NOT NULL) ";
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * clean tables in database.
   */
  public void clear() {
    try {
      conn.setAutoCommit(false);
      String sql = "DELETE FROM GAMEBOARD;";
      stmt.executeUpdate(sql);
      sql = "DELETE FROM PLAYER;";
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Insert moves to gameboard.
   * @param player int
   * @param x int
   * @param y int
   * @param winner int
   * @param draw int
   */
  public void insertBoard(final int player, final int x, final int y,
             final int winner, final int draw) {
    try {
      conn.setAutoCommit(false);
      String sql = "INSERT INTO GAMEBOARD (PLAYER,ROW,COLUMN,WINNER,DRAW) "
                     + "VALUES (" + player + "," + x + "," + y
                     + "," + winner + "," + draw + ");";
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Insert Player info to Player table.
   * @param id int
   * @param type char
   */
  public void insertPlayer(final int id, final char type) {
    try {
      conn.setAutoCommit(false);
      String sql = "INSERT INTO PLAYER (ID,TYPE) "
                   + "VALUES (" + id + "," + "'" + type + "'" + ");";
      stmt.executeUpdate(sql);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Commit last sql execution.
   */
  public void commit() {
    try {
      conn.commit();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Get number of rows matched given row, column and player.
   * @param player int
   * @param row int
   * @param column int
   * @return count int
   */
  public int getBoard(final int player, final int row, final int column) {
    int count = 0;
    try {
      ResultSet rs;
      rs = stmt.executeQuery("SELECT * FROM GAMEBOARD "
              + "WHERE PLAYER= " + player + " AND "
              + "ROW= " + row + " AND "
              + "Column= " + column);
      try {
        while (rs.next()) {
          count += 1;
        }
      } finally {
        rs.close();
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return count;
  }

  /**
   * Get player type.
   * @param id int
   * @return type char
   */
  public char getType(final int id) {
    char type = 0;
    try {
      ResultSet rs;
      rs = stmt.executeQuery("SELECT * FROM PLAYER "
                               + "WHERE ID= " + id);
      try {
        while (rs.next()) {
          type = rs.getString("TYPE").charAt(0);
        }
      } finally {
        rs.close();
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return type;
  }

  /**
   * Update board after reboot.
   * @param board GameBoard
   */
  public void inProgress(final GameBoard board) {
    int move1 = 0;
    int move2 = 0;
    char[][] state = new char[DIM][DIM];
    Player p1 = new Player();
    Player p2 = new Player();
    p1.setId(1);
    p2.setId(2);
    p1.setType(getType(1));
    p2.setType(getType(2));
    try {
      ResultSet rs;
      rs = stmt.executeQuery("SELECT * FROM GAMEBOARD ");
      try {
        while (rs.next()) {
          int winner = rs.getInt("WINNER");
          int draw = rs.getInt("DRAW");
          if (winner == 1 | winner == 2) {
            board.setWinner(winner);
          }
          if (draw == 1) {
            board.setIsDraw(true);
          }
          int player = rs.getInt("PLAYER");
          int row = rs.getInt("ROW");
          int column = rs.getInt("COLUMN");
          if (player == 1) {
            state[row][column] = p1.getType();
            move1++;
          } else {
            state[row][column] = p2.getType();
            move2++;
          }
        }
      } finally {
        rs.close();
      }
      if (getType(1) != '\u0000' && getType(2) != '\u0000') {
        board.setGameStarted(true);
      } else {
        board.setGameStarted(false);
      }
      board.setTurn(1 + move1 - move2);
      board.setBoardState(state);
      board.setP1(p1);
      board.setP2(p2);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * close database.
   */
  public void close() {
    try {
      stmt.close();
      conn.close();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
