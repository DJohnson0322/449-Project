package sosgame.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import sosgame.product.BaseGame;
import sosgame.product.SimpleGame;

public class TestSimpleGame {

    private BaseGame game;

    @Before
    public void setUp() {
        game = new SimpleGame(3);
    }

    @Test
    public void testGameEndsAfterOneSOS() {
        game.makeMove(0, 0, 'S');  
        game.makeMove(1, 1, 'O'); 
        game.makeMove(0, 1, 'O');  
        game.makeMove(2, 1, 'S');  
        game.makeMove(0, 2, 'S');

        assertTrue("Game should end when SOS is formed", game.isGameOver());
        assertEquals("Blue player should win", "Blue player wins!", game.getWinner());
    }

    @Test
    public void testDrawGameIfNoSOS() {
        game.makeMove(0, 0, 'S');
        game.makeMove(0, 1, 'S');
        game.makeMove(0, 2, 'O');

        game.makeMove(1, 0, 'O');
        game.makeMove(1, 1, 'S');
        game.makeMove(1, 2, 'S');

        game.makeMove(2, 0, 'S');
        game.makeMove(2, 1, 'O');
        game.makeMove(2, 2, 'O');

        assertTrue("Game should be over when board is full", game.isGameOver());
        assertEquals("Should be a draw", "Draw!", game.getWinner());
    }
}
