package sosgame.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import sosgame.product.BaseGame;
import sosgame.product.SimpleGame;

public class TestNoughtMoves {

    private BaseGame game;

    @Before
    public void setUp() throws Exception {
        game = new SimpleGame(3);
        game.makeMove(1, 1, 'S');  
    }

    @Test
    public void testValidMoveVacantCell() {
        game.makeMove(0, 0, 'O'); 
        assertEquals("Cell (0,0) should now contain 'O'", 'O', game.getCell(0, 0));
    }

    @Test
    public void testMoveNonVacantCell() {
        game.makeMove(1, 1, 'O'); 
        assertEquals("Occupied cell (1,1) should still be 'S'", 'S', game.getCell(1, 1));
    }

    @Test
    public void testInvalidRowMove() {
        game.makeMove(5, 0, 'O');
        assertEquals("Invalid row should not affect valid cells", ' ', game.getCell(0, 0));
    }

    @Test
    public void testInvalidColumnMove() {
        game.makeMove(0, 5, 'O'); 
        assertEquals("Invalid column should not affect valid cells", ' ', game.getCell(0, 0));
    }
}
