import java.util.Random;

public class Board {
    private final char[][] board;
    private boolean turn;

    public Board() {
        this.board = new char[3][3];
        this.turn = new Random().nextBoolean();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '-';
            }
        }
    }

    public boolean whose_turn() {
        return this.turn;
    }

    public void play_move(int x, int y, boolean symbol) {
        x--;
        y--;
        if (x < 0 || x >= 3 || y < 0 || y >= 3 || board[x][y] != '-') {
            System.out.println("Invalid move, try again.");
            return;
        }

        this.board[x][y] = symbol ? 'O' : 'X';
        this.turn = !this.turn;
    }

    public void display_board() {
        for (int i = 0; i < 3; i++) {
            System.out.print("|");
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + "|");
            }
            System.out.println();
        }
    }

    private boolean check_draw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '-') {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean is_game_over() {
        char target = this.turn ? 'X' : 'O';

        for (int i = 0; i < 3; i++) {
            if (board[0][i] == target && board[1][i] == target && board[2][i] == target) {
                System.out.println("Game over, " + target + " has won!");
                return true;
            }
        }
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == target && board[i][1] == target && board[i][2] == target) {
                System.out.println("Game over, " + target + " has won!");
                return true;
            }
        }

        if ((board[0][0] == target && board[1][1] == target && board[2][2] == target) || (board[0][2] == target && board[1][1] == target && board[2][0] == target)) {
            System.out.println("Game over, " + target + " has won!");
            return true;
        }

        if (this.check_draw()) {
            System.out.println("Game over, it's a draw!");
            return true;
        }

        return false;
    }
}