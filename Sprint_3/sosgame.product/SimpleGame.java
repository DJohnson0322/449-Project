package sosgame.product;

public class SimpleGame extends BaseGame
{
    public SimpleGame(int size)
    {
        super(size);
    }

    @Override
    public boolean makeMove(int r, int c, char letter)
    {
        if (gameOver)
            return false;

        boolean placed = board.place(r, c, letter, currentPlayer);

        if (!placed)
            return false;

        if (checkSOS(r, c))
        {
            winner = currentPlayer;
            gameOver = true;
            return true;
        }

        switchPlayer();
        return true;
    }
}
