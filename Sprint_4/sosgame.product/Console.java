package sosgame.product;

import java.util.Scanner;

public class Console {

    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);

        System.out.print("Board size: ");
        int size = input.nextInt();
        input.nextLine();

        System.out.print("Mode (simple / general): ");
        String mode = input.nextLine().trim();

        BaseGame game = mode.equalsIgnoreCase("simple")
                ? new SimpleGame(size)
                : new GeneralGame(size);

        while (!game.isGameOver())
        {
            print(game);
            System.out.println("Turn: " + game.getCurrentPlayer());
            System.out.print("Row Col Letter: ");

            int r = input.nextInt();
            int c = input.nextInt();
            char letter = input.next().toUpperCase().charAt(0);

            game.makeMove(r, c, letter);
        }

        print(game);
        System.out.println(game.getWinner());

        input.close();
    }

    public static void print(BaseGame g)
    {
        int size = g.getSize();

        for (int r = 0; r < size; r++)
        {
            for (int i = 0; i < size * 2 + 1; i++)
                System.out.print("-");
            System.out.println();

            for (int c = 0; c < size; c++)
                System.out.print("|" + g.getCell(r, c));
            System.out.println("|");
        }

        for (int i = 0; i < size * 2 + 1; i++)
            System.out.print("-");
        System.out.println();
    }
}
