package sosgame.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sosgame.product.BaseGame;
import sosgame.product.SimpleGame;
import sosgame.product.GeneralGame;
import sosgame.product.Console;

public class TestBoardConsole {

    private BaseGame game;

    @Before
    public void setUp() throws Exception {
        game = new SimpleGame(3);
    }

    @After
    public void tearDown() throws Exception {
        game = null;
    }

    @Test
    public void testEmptyBoard() {
        System.out.println("=== Test: Empty Board ===");
        Console.print(game);
    }

    @Test
    public void testNonEmptyBoard() {
        System.out.println("=== Test: Non-Empty Board ===");
        game.makeMove(0, 0, 'S');  // Blue
        game.makeMove(1, 1, 'O');  // Red
        game.makeMove(2, 2, 'S');  // Blue
        Console.print(game);
    }

    @Test
    public void testDifferentBoardSize() {
        System.out.println("=== Test: 5x5 Board ===");
        BaseGame largeGame = new GeneralGame(5);
        largeGame.makeMove(0, 0, 'S');
        largeGame.makeMove(4, 4, 'O');
        Console.print(largeGame);
    }
}
