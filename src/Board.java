import java.util.Random;

public class Board {
    private final char[][] board;
    private boolean turn;

    Board() {
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

    public void play_move(int x, int y) {
        x--;
        y--;
        if (x < 0 || x >= 3 || y < 0 || y >= 3 || board[x][y] != '-') {
            System.out.println("Invalid move, try again.");
            return;
        }

        this.board[x][y] = this.turn ? 'O' : 'X';
        this.turn = !this.turn;
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

    public GameOverStatus is_game_over() {
        char target = this.turn ? 'X' : 'O';

        for (int i = 0; i < 3; i++) {
            if (board[0][i] == target && board[1][i] == target && board[2][i] == target) {
                return this.turn ? GameOverStatus.X : GameOverStatus.O;
            }
        }
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == target && board[i][1] == target && board[i][2] == target) {
                return this.turn ? GameOverStatus.X : GameOverStatus.O;
            }
        }

        if ((board[0][0] == target && board[1][1] == target && board[2][2] == target) || (board[0][2] == target && board[1][1] == target && board[2][0] == target)) {
            return this.turn ? GameOverStatus.X : GameOverStatus.O;
        }

        if (this.check_draw()) {
            return GameOverStatus.DRAW;
        }

        return GameOverStatus.ONGOING;
    }
}