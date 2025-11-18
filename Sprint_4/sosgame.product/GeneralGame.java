package sosgame.product;

public class GeneralGame extends BaseGame {

    public GeneralGame(int size) {
        super(size);
    }

    @Override
    public boolean makeMove(int r, int c, char letter) {
        if (gameOver)
            return false;

        boolean placed = board.place(r, c, letter, currentPlayer);
        if (!placed)
            return false;

        boolean sos = checkSOS(r, c);

        if (sos) {
            if (currentPlayer == 'B') blueScore++;
            else redScore++;
        } else {
            switchPlayer();
        }

        if (boardIsFull()) {
            if (blueScore > redScore) winner = 'B';
            else if (redScore > blueScore) winner = 'R';
            else winner = 'D';
            gameOver = true;
        }
        return true;
    }

    private boolean boardIsFull() {
        for (int r=0; r<board.getSize(); r++)
            for (int c=0; c<board.getSize(); c++)
                if (board.getCell(r,c) == ' ')
                    return false;
        return true;
    }
}
