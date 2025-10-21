package sosgame.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sosgame.product.Board;

public class TestCrossMoves {

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
    public void testValidMoveVacantCell() {
        board.makeMove(0, 0, 'S');
        assertEquals("Cell (0,0) should now contain 'S'", 'S', board.getCell(0, 0));
    }

    @Test
    public void testMoveOnOccupiedCell() {
        board.makeMove(0, 0, 'S');
        char before = board.getCell(0, 0);
        board.makeMove(0, 0, 'O'); 
        assertEquals("Cell (0,0) should remain 'S'", before, board.getCell(0, 0));
    }

    @Test
    public void testInvalidRowMove() {
        board.makeMove(5, 0, 'S');
        assertEquals("Out-of-bounds row should not modify board", ' ', board.getCell(0, 0));
    }

    @Test
    public void testInvalidColumnMove() {
        board.makeMove(0, 5, 'O');
        assertEquals("Out-of-bounds column should not modify board", ' ', board.getCell(0, 0));
    }
}
