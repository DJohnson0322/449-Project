package sosgame.test;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sosgame.product.BaseGame;
import sosgame.product.SimpleGame;

public class TestEmptyBoard {

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
    public void testNewBoard() {
        for (int row = 0; row < game.getSize(); row++) {
            for (int col = 0; col < game.getSize(); col++) {
                assertEquals("Each cell should be empty initially", ' ', game.getCell(row, col));
            }
        }
    }

    @Test
    public void testInvalidRow() {
        assertEquals("Invalid row access should return '?'", '?', game.getCell(3, 0));
    }

    @Test
    public void testInvalidColumn() {
        assertEquals("Invalid column access should return '?'", '?', game.getCell(0, 3));
    }
}
