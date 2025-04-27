import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class GameUI {
    private Board board;
    private Connection connection;
    private final JFrame frame;
    private final JPanel info_panel;
    private final JPanel game_panel;
    private TicTacToePlayer p1;
    private TicTacToePlayer p2;
    private final JLabel p1_name;
    private final JLabel p1_mail;
    private final JLabel p1_won;
    private final JLabel p1_symbol;
    private final JLabel p2_name;
    private final JLabel p2_mail;
    private final JLabel p2_won;
    private final JLabel p2_symbol;
    private final JButton[][] tiles;
    private final JLabel status_label;

    GameUI(TicTacToePlayer player1, TicTacToePlayer player2, Connection connection, String board_state, boolean turn) throws SQLException {
        this.p1 = player1;
        this.p2 = player2;
        p1.set_symbol(new Random().nextBoolean());
        p2.set_symbol(!p1.get_symbol());

        this.board = new Board();
        if(board_state != null) {
            board.load_board(board_state.toCharArray());
            board.set_turn(turn);
        }
        this.connection = connection;
        this.tiles = new JButton[3][3];

        this.p1_name = new JLabel("Name: " + p1.get_name());
        this.p1_mail = new JLabel("Email: " + p1.get_email());
        this.p1_won = new JLabel("Won: " + p1.get_won());
        this.p1_symbol = new JLabel("Symbol: " + (p1.get_symbol() ? "O" : "X"));
        this.p2_name = new JLabel("Name: " + p2.get_name());
        this.p2_mail = new JLabel("Email: " + p2.get_email());
        this.p2_won = new JLabel("Won: " + p2.get_won());
        this.p2_symbol = new JLabel("Symbol: " + (p2.get_symbol() ? "O" : "X"));
        this.status_label = new JLabel("Current turn: " + (board.whose_turn() ? "O" : "X"));

        this.frame = new JFrame();
        frame.setTitle("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 1000);
        frame.setLayout(new BorderLayout());

        this.info_panel = new JPanel();
        info_panel.setLayout(new GridLayout(4, 2, 10, 10));
        info_panel.add(p1_name);
        info_panel.add(p1_mail);
        info_panel.add(p1_won);
        info_panel.add(p1_symbol);
        info_panel.add(p2_name);
        info_panel.add(p2_mail);
        info_panel.add(p2_won);
        info_panel.add(p2_symbol);

        this.game_panel = new JPanel();
        game_panel.setLayout(new GridLayout(3, 3, 5, 5));
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                JButton btn = new JButton();
                btn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
                btn.setPreferredSize(new Dimension(100, 100));
                btn.setFocusable(false);

                if(board.get_tile(i, j) != '-') {
                    btn.setText(String.valueOf(board.get_tile(i, j)));
                    btn.setEnabled(false);
                }

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
        board.play_move(x, y);
        btn.setText(board.whose_turn() ? "X" : "O");
        btn.setEnabled(false);
        status_label.setText("Current turn: " + (board.whose_turn() ? "O" : "X"));

        GameOverStatus game_over_status = board.is_game_over();
        if(game_over_status != GameOverStatus.ONGOING) {
            delete_game();

            String msg = switch(game_over_status) {
                case X -> {
                    if(!p1.get_symbol()) {
                        increment_won(p1, p1.get_email(), p1_won);
                        yield p1.get_name() + " is the winner.";
                    } else {
                        increment_won(p2, p2.get_email(), p2_won);
                        yield p2.get_name() + " is the winner.";
                    }
                }
                case O -> {
                    if(p1.get_symbol()) {
                        increment_won(p1, p1.get_email(), p1_won);
                        yield p1.get_name() + " is the winner.";
                    } else {
                        increment_won(p2, p2.get_email(), p2_won);
                        yield p2.get_name() + " is the winner.";
                    }
                }
                case DRAW -> "It is a draw.";
                default -> "";
            };

            int opt = JOptionPane.showConfirmDialog(frame, msg + "\nPlay again?", "Game Over", JOptionPane.YES_NO_OPTION);
            if(opt == JOptionPane.YES_OPTION) {
                this.reset_board();
            } else {
                frame.dispose();
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Failed to close connection: " + e.getMessage());
                }
            }
        } else {
            save_game();
        }
    }

    private void increment_won(TicTacToePlayer player, String email, JLabel won_label) {
        try {
            String sql = "UPDATE credentials SET won = won + 1 WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, email);
            statement.executeUpdate();

            player.set_won(player.get_won() + 1);
            won_label.setText("Won: " + player.get_won());
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to update win count: " + e.getMessage());
        }
    }

    private void save_game() {
        try {
            String email1 = p1.get_email();
            String email2 = p2.get_email();
            if(email1.compareTo(email2) > 0) {
                String tmp = email1;
                email1 = email2;
                email2 = tmp;
            }

            PreparedStatement statement = connection.prepareStatement("REPLACE INTO saved_games (p1_email, p2_email, board_state, turn) VALUES (?, ?, ?, ?)");

            statement.setString(1, email1);
            statement.setString(2, email2);
            statement.setString(3, board.serialize());
            statement.setBoolean(4, board.whose_turn());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to save game: " + e.getMessage());
        }
    }

    public void delete_game() {
        try {
            String email1 = p1.get_email();
            String email2 = p2.get_email();
            if(email1.compareTo(email2) > 0) {
                String tmp = email1;
                email1 = email2;
                email2 = tmp;
            }

            PreparedStatement statement = connection.prepareStatement("DELETE FROM saved_games WHERE p1_email = ? AND p2_email = ?");

            statement.setString(1, email1);
            statement.setString(2, email2);
            statement.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to delete game: " + e.getMessage());
        }
    }

    private void reset_board() {
        board = new Board();
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                tiles[i][j].setText("");
                tiles[i][j].setEnabled(true);
            }
        }
    }
}