package sosgame.product;

import java.util.Scanner;

public class Console {
    private Board board;

    public Console(Board board) {
        this.board = board;
    }

    public void displayBoard() {
        int size = board.getSize();
        for (int row = 0; row < size; row++) {
            for (int i = 0; i < size * 2 + 1; i++) System.out.print("-");
            System.out.println();

            for (int col = 0; col < size; col++) {
                System.out.print("|" + board.getCell(row, col));
            }
            System.out.println("|");
        }

        for (int i = 0; i < board.getSize() * 2 + 1; i++) System.out.print("-");
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter board size: ");
        int size = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter game mode (Simple Game or General Game): ");
        String mode = scanner.nextLine();

        Board board = new Board(size, mode);
        Console console = new Console(board);
        console.displayBoard();

        scanner.close();
    }
}
