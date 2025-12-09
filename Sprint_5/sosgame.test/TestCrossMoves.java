package sosgame.test;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sosgame.product.SimpleGame;
import sosgame.product.BaseGame;

public class TestCrossMoves {

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
    public void testValidMoveVacantCell() {
        game.makeMove(0, 0, 'S');
        assertEquals('S', game.getCell(0, 0));
    }

    @Test
    public void testMoveOnOccupiedCell() {
        game.makeMove(0, 0, 'S');       
        char before = game.getCell(0, 0);

        game.makeMove(0, 0, 'O');       

        assertEquals(before, game.getCell(0, 0));
    }

    @Test
    public void testInvalidRowMove() {
        game.makeMove(10, 0, 'S');      
        assertEquals(' ', game.getCell(0, 0));  
    }

    @Test
    public void testInvalidColumnMove() {
        game.makeMove(0, 10, 'O');    
        assertEquals(' ', game.getCell(0, 0));
    }
}
