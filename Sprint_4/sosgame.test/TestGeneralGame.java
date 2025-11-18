package sosgame.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import sosgame.product.BaseGame;
import sosgame.product.GeneralGame;

public class TestGeneralGame {

    private BaseGame game;

    @Before
    public void setUp() {
        game = new GeneralGame(3);
    }

    @Test
    public void testGeneralGameContinuesAfterSOS() {
        game.makeMove(0, 0, 'S');  
        game.makeMove(1, 1, 'O');  
        game.makeMove(0, 1, 'O');  
        game.makeMove(2, 1, 'S');  
        game.makeMove(0, 2, 'S');  

        assertFalse("General game shouldn't end after SOS", game.isGameOver());
    }

    @Test
    public void testMultipleSOSAndCorrectWinner() {
        game.makeMove(0, 0, 'S'); 
        game.makeMove(2, 2, 'S');  
        game.makeMove(0, 1, 'O');
        game.makeMove(1, 2, 'S');  
        game.makeMove(0, 2, 'S');  
        game.makeMove(2, 0, 'S'); 
        game.makeMove(1, 0, 'O');
        game.makeMove(1, 1, 'S');  
        game.makeMove(2, 1, 'S');  

        assertTrue("Game should end when board is full", game.isGameOver());
        assertEquals("Scores are tied -> draw", "Draw!", game.getWinner());
    }
}
