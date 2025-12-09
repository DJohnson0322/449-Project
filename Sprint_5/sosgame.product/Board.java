package sosgame.product;

public class Board
{
    private char[][] grid;
    private char[][] playerPlaced;
    private int size;

    public Board(int size)
    {
        if (size < 3)
        {
            throw new IllegalArgumentException("Board size must be >= 3");
        }

        this.size = size;
        grid = new char[size][size];
        playerPlaced = new char[size][size];

        for (int r = 0; r < size; r++)
        {
            for (int c = 0; c < size; c++)
            {
                grid[r][c] = ' ';
                playerPlaced[r][c] = ' ';
            }
        }
    }

    public int getSize()
    {
        return size;
    }

    public char getCell(int r, int c)
    {
        if (r < 0 || r >= size || c < 0 || c >= size)
            return '?';
        return grid[r][c];
    }

    public char getPlayerAt(int r, int c)
    {
        if (r < 0 || r >= size || c < 0 || c >= size)
            return '?';
        return playerPlaced[r][c];
    }

    public boolean place(int r, int c, char letter, char player)
    {
        if (letter != 'S' && letter != 'O')
            return false;

        if (r < 0 || r >= size || c < 0 || c >= size)
            return false;

        if (grid[r][c] != ' ')
            return false;

        grid[r][c] = letter;
        playerPlaced[r][c] = player;
        return true;
    }

    public char[][] getGrid()
    {
        return grid;
    }
}
