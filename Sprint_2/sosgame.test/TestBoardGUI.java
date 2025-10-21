package sosgame.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sosgame.product.Board;
import sosgame.product.GUI;

public class TestBoardGUI {
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
        System.out.println("=== Test: Empty Board GUI ===");
        new GUI(board); 
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNonEmptyBoard() {
        System.out.println("=== Test: Non-Empty Board GUI ===");
        board.makeMove(0, 0, 'S');
        board.makeMove(1, 1, 'O');
        board.makeMove(2, 2, 'S');
        new GUI(board); 
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLargerBoard() {
        System.out.println("=== Test: 5x5 Board GUI ===");
        Board largeBoard = new Board(5, "General Game");
        largeBoard.makeMove(0, 0, 'S');
        largeBoard.makeMove(4, 4, 'O');
        new GUI(largeBoard);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
