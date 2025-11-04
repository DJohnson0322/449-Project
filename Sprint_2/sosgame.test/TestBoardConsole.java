package sosgame.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sosgame.product.Console;
import sosgame.product.Board;

public class TestBoardConsole {
    private Board board;

    @Before
    public void setUp() throws Exception {
        board = new Board(3, "Simple Game");
    }

    @After
    public void tearDown() throws Exception {
        board = null;
    }

    @Test
    public void testEmptyBoard() {
        System.out.println("=== Test: Empty Board ===");
        new Console(board).displayBoard();
    }

    @Test
    public void testNonEmptyBoard() {
        System.out.println("=== Test: Non-Empty Board ===");
        board.makeMove(0, 0, 'S', 'B'); // Blue player
        board.makeMove(1, 1, 'O', 'R'); // Red player
        board.makeMove(2, 2, 'S', 'B'); // Blue player
        new Console(board).displayBoard();
    }

    @Test
    public void testDifferentBoardSize() {
        System.out.println("=== Test: 5x5 Board ===");
        Board largeBoard = new Board(5, "General Game");
        largeBoard.makeMove(0, 0, 'S', 'B');
        largeBoard.makeMove(4, 4, 'O', 'R');
        new Console(largeBoard).displayBoard();
    }
}
