package sosgame.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import sosgame.product.*;

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
        game.makeMove(0, 0, 'O');
        game.makeMove(0, 1, 'O'); 
        game.makeMove(0, 2, 'O');

        game.makeMove(1, 0, 'O'); 
        game.makeMove(1, 1, 'O'); 
        game.makeMove(1, 2, 'O'); 

        game.makeMove(2, 0, 'O'); 
        game.makeMove(2, 1, 'O'); 
        game.makeMove(2, 2, 'O'); 

        assertTrue("Game should be over when board is full", game.isGameOver());
        assertEquals("Should be a draw", "Draw!", game.getWinner());
    }

    @Test
    public void testHumanVsComputerTurnFlow() {

        BaseGame localGame = new SimpleGame(5);

        Player blue = new HumanPlayer('B');
        Player red = new ComputerPlayer('R');
        localGame.setPlayers(blue, red);

        boolean placed = localGame.makeMove(0, 0, 'S');
        assertTrue("Human should be able to place at (0,0)", placed);
        assertEquals('S', localGame.getCell(0, 0));

        assertEquals("Red should now move", 'R', localGame.getCurrentPlayer());
        Player cpu = localGame.getCurrentPlayerObject();
        assertTrue("Red must be ComputerPlayer", cpu instanceof ComputerPlayer);

        Move m = cpu.getMove(localGame);
        assertNotNull("Computer must return a move", m);

        assertTrue(m.r >= 0 && m.r < 5);
        assertTrue(m.c >= 0 && m.c < 5);
        assertTrue("Letter must be S or O", m.letter == 'S' || m.letter == 'O');
        assertEquals("Computer must place on empty tile", ' ', localGame.getCell(m.r, m.c));

        assertTrue(localGame.makeMove(m.r, m.c, m.letter));
        if (!localGame.isGameOver()) {
            assertEquals("Turn should switch back to Blue", 'B', localGame.getCurrentPlayer());
        }
    }
}
