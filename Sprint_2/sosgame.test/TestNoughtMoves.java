package sosgame.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import sosgame.product.Board;

public class TestNoughtMoves {
    private Board board;
    @Before
    public void setUp() throws Exception {
        board = new Board(3, "Simple Game");
        board.makeMove(1, 1, 'S', 'R');
    }
    @Test
    public void testValidMoveVacantCell() {
        board.makeMove(0, 0, 'O', 'B');
        assertEquals("Cell (0,0) should now contain 'O'", 'O', board.getCell(0, 0));
    }
    @Test
    public void testMoveNonVacantCell() {
        board.makeMove(1, 1, 'O', 'R');
        assertEquals("Occupied cell (1,1) should still be 'S'", 'S', board.getCell(1, 1));
    }
    @Test
    public void testInvalidRowMove() {
        board.makeMove(5, 0, 'O', 'B');
        assertEquals("Invalid row should not affect valid cells", ' ', board.getCell(0, 0));
    }
    @Test
    public void testInvalidColumnMove() {
        board.makeMove(0, 5, 'O', 'R');
        assertEquals("Invalid column should not affect valid cells", ' ', board.getCell(0, 0));
    }
}
