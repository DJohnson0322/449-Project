package sosgame.product;

import java.util.ArrayList;

public abstract class BaseGame
{
    protected Board board;
    protected char currentPlayer;
    protected boolean gameOver;
    protected int blueScore;
    protected int redScore;
    protected char winner;

    protected Player bluePlayer;
    protected Player redPlayer;

    public static class SOSLine {
        public int r1, c1, r2, c2;
        public char player;

        public SOSLine(int r1, int c1, int r2, int c2, char p) {
            this.r1 = r1;
            this.c1 = c1;
            this.r2 = r2;
            this.c2 = c2;
            this.player = p;
        }
    }

    public ArrayList<SOSLine> scoredLines = new ArrayList<>();

    public BaseGame(int size)
    {
        board = new Board(size);
        currentPlayer = 'B';
        gameOver = false;
        blueScore = 0;
        redScore = 0;
        winner = ' ';
    }

    public void setPlayers(Player blue, Player red) {
        this.bluePlayer = blue;
        this.redPlayer = red;
    }

    public Player getCurrentPlayerObject() {
        return (currentPlayer == 'B') ? bluePlayer : redPlayer;
    }

    public int getSize() {
        return board.getSize();
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public char getCell(int r, int c) {
        return board.getCell(r, c);
    }

    public char getPlayerAt(int r, int c) {
        return board.getPlayerAt(r, c);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public String getWinner()
    {
        if (winner == 'B') return "Blue player wins!";
        if (winner == 'R') return "Red player wins!";
        return "Draw!";
    }

    protected void switchPlayer() {
        currentPlayer = (currentPlayer == 'B') ? 'R' : 'B';
    }

    protected boolean checkSOS(int r, int c)
    {
        char[][] g = board.getGrid();
        int size = board.getSize();
        char placed = g[r][c];

        int[][] dirs = {
                {0, 1}, {1, 0}, {1, 1}, {1, -1},
                {0, -1}, {-1, 0}, {-1, -1}, {-1, 1}
        };

        for (int[] d : dirs)
        {
            int dr = d[0];
            int dc = d[1];

            if (placed == 'S')
            {
                if (inside(r + dr, c + dc, size) &&
                    inside(r + 2*dr, c + 2*dc, size))
                {
                    if (g[r + dr][c + dc] == 'O' &&
                        g[r + 2*dr][c + 2*dc] == 'S')
                    {
                        scoredLines.add(
                            new SOSLine(r, c, r + 2*dr, c + 2*dc, currentPlayer)
                        );
                        return true;
                    }
                }

                if (inside(r - dr, c - dc, size) &&
                    inside(r - 2*dr, c - 2*dc, size))
                {
                    if (g[r - dr][c - dc] == 'O' &&
                        g[r - 2*dr][c - 2*dc] == 'S')
                    {
                        scoredLines.add(
                            new SOSLine(r - 2*dr, c - 2*dc, r, c, currentPlayer)
                        );
                        return true;
                    }
                }
            }

            if (placed == 'O')
            {
                if (inside(r - dr, c - dc, size) &&
                    inside(r + dr, c + dc, size))
                {
                    if (g[r - dr][c - dc] == 'S' &&
                        g[r + dr][c + dc] == 'S')
                    {
                        scoredLines.add(
                            new SOSLine(r - dr, c - dc, r + dr, c + dc, currentPlayer)
                        );
                        return true;
                    }
                }
            }
        }

        return false;
    }
    protected boolean inside(int r, int c, int size) {
        return r >= 0 && r < size && c >= 0 && c < size;
    }
    public abstract boolean makeMove(int r, int c, char letter);
}
