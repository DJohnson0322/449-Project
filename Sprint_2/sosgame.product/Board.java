package sosgame.product;

public class Board {

    private char[][] grid;
    private char turn = 'X';
    private int size;
    private String mode;

    public Board(int size, String mode) {
        if (size < 2) {
            throw new IllegalArgumentException("Board size less than 2.");
        }
        this.size = size;
        this.mode = mode;
        grid = new char[size][size];
    }

    public int getSize() {
        return size;
    }

    public String getMode() {
        return mode;
    }

    public char getTurn() {
        return turn;
    }

    public char getCell(int row, int column) {
        if (row >= 0 && row < size && column >= 0 && column < size) {
            return (grid[row][column] == '\0') ? ' ' : grid[row][column];
        } else {
            return '?';
        }
    }

    public void makeMove(int row, int column, char letter) {
        if (row >= 0 && row < size && column >= 0 && column < size
                && grid[row][column] == ' ' || grid[row][column] == '\0') {
            if (letter == 'S' || letter == 'O') {
                grid[row][column] = letter;
                turn = (turn == 'X') ? 'O' : 'X';
            }
        }
    }
}
