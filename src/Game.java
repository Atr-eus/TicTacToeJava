import java.util.Random;
import java.util.Scanner;

public class Game {
    public static void start() {
        TicTacToePlayer tttp1 = new TicTacToePlayer();
        TicTacToePlayer tttp2 = new TicTacToePlayer();

        System.out.print("Taking input for the first player:\n");
        tttp1.take_input();
        tttp1.set_symbol(new Random().nextBoolean());
        System.out.print("Taking input for the second player:\n");
        tttp2.take_input();
        tttp2.set_symbol(!tttp1.get_symbol());

        System.out.print("\nFirst player info:\n");
        tttp1.display();
        System.out.print("\nSecond player info:\n");
        tttp2.display();

        boolean letsplay;
        Scanner sc = new Scanner(System.in);
        do {
            Board board = new Board();

            do {
                board.display_board();
                System.out.print("Move to be made by " + (board.whose_turn() ? "O" : "X") + ":\n");
                int x = sc.nextInt();
                int y = sc.nextInt();
                board.play_move(x, y, board.whose_turn());
            } while (!board.is_game_over());
            board.display_board();

            System.out.println("\nStart next round? [yes/no]: ");
            String consent = sc.nextLine();
            letsplay = consent.equalsIgnoreCase("yes");
        } while(letsplay);
    }
}