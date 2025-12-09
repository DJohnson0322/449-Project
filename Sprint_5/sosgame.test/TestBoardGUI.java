package sosgame.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sosgame.product.BaseGame;
import sosgame.product.SimpleGame;
import sosgame.product.GeneralGame;
import sosgame.product.GUI;

public class TestBoardGUI {

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
        System.out.println("=== Test: Empty Board GUI ===");
        GUI gui = new GUI();

        gui.startGameFromTest(new SimpleGame(3));

        sleep();
        gui.dispose();
    }

    @Test
    public void testNonEmptyBoard() {
        System.out.println("=== Test: Non-Empty Board GUI ===");

        BaseGame game = new SimpleGame(3);
        game.makeMove(0, 0, 'S');
        game.makeMove(1, 1, 'O');
        game.makeMove(2, 2, 'S');

        GUI gui = new GUI();
        gui.startGameFromTest(game);

        sleep();
        gui.dispose();
    }

    @Test
    public void testLargerBoard() {
        System.out.println("=== Test: 5x5 Board GUI ===");

        BaseGame game = new GeneralGame(5);
        game.makeMove(0, 0, 'S');
        game.makeMove(4, 4, 'O');

        GUI gui = new GUI();
        gui.startGameFromTest(game);

        sleep();
        gui.dispose();
    }

    private void sleep() {
        try {
            Thread.sleep(2000); 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
