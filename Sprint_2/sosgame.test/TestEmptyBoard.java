package sosgame.test;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sosgame.product.Board;

public class TestEmptyBoard {
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
    public void testNewBoard() {
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                assertEquals("Each cell should be empty initially", ' ', board.getCell(row, col));
            }
        }
    }
    @Test
    public void testInvalidRow() {
        assertEquals("Invalid row access should return '?'", '?', board.getCell(3, 0));
    }
    @Test
    public void testInvalidColumn() {
        assertEquals("Invalid column access should return '?'", '?', board.getCell(0, 3));
    }
}
