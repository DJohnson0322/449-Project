package sosgame.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComputerPlayer extends Player {

    private Random rand = new Random();

    public ComputerPlayer(char id) {
        super(id);
    }

    @Override
    public Move getMove(BaseGame game) {
        int size = game.getSize();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (game.getCell(r, c) != ' ')
                    continue;

                for (char letter : new char[]{'S', 'O'}) {
                    if (wouldFormSOS(game, r, c, letter))
                        return new Move(r, c, letter);
                }
            }
        }
        List<int[]> empty = new ArrayList<>();
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                if (game.getCell(r, c) == ' ')
                    empty.add(new int[]{r, c});

        int[] pos = empty.get(rand.nextInt(empty.size()));
        char letter = rand.nextBoolean() ? 'S' : 'O';

        return new Move(pos[0], pos[1], letter);
    }

    private boolean wouldFormSOS(BaseGame game, int r, int c, char letter) {
        int size = game.getSize();
        char[][] g = game.board.getGrid();

        int[][] dirs = {
                {0, 1}, {1, 0}, {1, 1}, {1, -1},
                {0, -1}, {-1, 0}, {-1, -1}, {-1, 1}
        };

        for (int[] d : dirs) {
            int dr = d[0], dc = d[1];

            if (letter == 'S') {
                if (inside(r + dr, c + dc, size) &&
                    inside(r + 2*dr, c + 2*dc, size)) {

                    if (g[r+dr][c+dc] == 'O' && g[r+2*dr][c+2*dc] == 'S')
                        return true;
                }

                if (inside(r - dr, c - dc, size) &&
                    inside(r - 2*dr, c - 2*dc, size)) {

                    if (g[r-dr][c-dc] == 'O' && g[r-2*dr][c-2*dc] == 'S')
                        return true;
                }
            }

            if (letter == 'O') {
                if (inside(r - dr, c - dc, size) &&
                    inside(r + dr, c + dc, size)) {

                    if (g[r-dr][c-dc] == 'S' && g[r+dr][c+dc] == 'S')
                        return true;
                }
            }
        }
        return false;
    }

    private boolean inside(int r, int c, int size) {
        return r >= 0 && r < size && c >= 0 && c < size;
    }
}
