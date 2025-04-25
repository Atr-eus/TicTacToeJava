import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GameUI {
    private Board board;
    private final JFrame frame;
    private final JPanel info_panel;
    private final JPanel game_panel;
    private final JLabel p1_name;
    private final JLabel p1_mail;
    private final JLabel p1_symbol;
    private final JLabel p2_name;
    private final JLabel p2_mail;
    private final JLabel p2_symbol;
    private final JButton[][] tiles;
    private final JLabel status_label;

    GameUI(TicTacToePlayer p1, TicTacToePlayer p2) {
        p1.set_symbol(new Random().nextBoolean());
        p2.set_symbol(!p1.get_symbol());

        board = new Board();
        tiles = new JButton[3][3];

        p1_name = new JLabel("Name: " + p1.get_name());
        p1_mail = new JLabel("Email: " + p1.get_email());
        p1_symbol = new JLabel("Symbol: " + (p1.get_symbol() ? "O" : "X"));
        p2_name = new JLabel("Name: " + p2.get_name());
        p2_mail = new JLabel("Email: " + p2.get_email());
        p2_symbol = new JLabel("Symbol: " + (p2.get_symbol() ? "O" : "X"));
        status_label = new JLabel("Current turn: " + (board.whose_turn() ? "O" : "X"));

        frame = new JFrame();
        frame.setTitle("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());

        info_panel = new JPanel();
        info_panel.setLayout(new GridLayout(2, 2, 10, 10));
        info_panel.add(p1_name);
        info_panel.add(p1_mail);
        info_panel.add(p1_symbol);
        info_panel.add(p2_name);
        info_panel.add(p2_mail);
        info_panel.add(p2_symbol);

        game_panel = new JPanel();
        game_panel.setLayout(new GridLayout(3, 3, 5, 5));
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                JButton btn = new JButton();
                btn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
                btn.setPreferredSize(new Dimension(100, 100));
                btn.setFocusable(false);

                final int x = i + 1, y = j + 1;
                btn.addActionListener(_ -> {
                    make_move(x, y, btn);
                });

                tiles[i][j] = btn;
                game_panel.add(btn);
            }
        }

        frame.add(info_panel, BorderLayout.NORTH);
        frame.add(game_panel, BorderLayout.CENTER);
        frame.add(status_label, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    void make_move(int x, int y, JButton btn) {
        char target = board.whose_turn() ? 'O' : 'X';

        board.play_move(x, y, board.whose_turn());
        btn.setText(board.whose_turn() ? "0" : "X");
        btn.setEnabled(false);
        status_label.setText("Current turn: " + (board.whose_turn() ? "X" : "O"));

        GameOverStatus game_over_status = board.is_game_over(target);
        if(game_over_status != GameOverStatus.ONGOING) {
            String msg = switch(game_over_status) {
                case X -> "Player X is the winner.";
                case O -> "Player O is the winner.";
                case DRAW -> "It is a draw.";
                default -> "";
            };

            int opt = JOptionPane.showConfirmDialog(frame, msg + "\nPlay again?", "Game Over", JOptionPane.YES_NO_OPTION);
            if(opt == JOptionPane.YES_OPTION) {
                this.reset_board();
            } else {
                frame.dispose();
            }
        }
    }

    void reset_board() {
        board = new Board();
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                tiles[i][j].setText("");
                tiles[i][j].setEnabled(true);
            }
        }
    }
}