package sosgame.product;

public class Board {

    private char[][] grid;
    private char[][] playerColors; 
    private int size;
    private String mode;

    public Board(int size, String mode) {
        if (size < 3) { 
            throw new IllegalArgumentException("Board size must be at least 3.");
        }
        this.size = size;
        this.mode = mode;
        grid = new char[size][size];
        playerColors = new char[size][size];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = ' ';
                playerColors[i][j] = ' ';
            }
        }
    }

    public int getSize() {
        return size;
    }

    public String getMode() {
        return mode;
    }

    public char getCell(int row, int column) {
        if (row >= 0 && row < size && column >= 0 && column < size) {
            return grid[row][column];
        } else {
            return '?';
        }
    }

    public char getPlayerAt(int row, int column) {
        if (row >= 0 && row < size && column >= 0 && column < size) {
            return playerColors[row][column];
        } else {
            return '?';
        }
    }

    public void makeMove(int row, int column, char letter, char player) {
        if (row < 0 || row >= size || column < 0 || column >= size) return;
        if (grid[row][column] != ' ') return; 
        if (letter != 'S' && letter != 'O') return; 

        grid[row][column] = letter;
        playerColors[row][column] = player; 
    }
}
