import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Arrays;

public class LoginUI {
    private JFrame frame;
    private Connection connection;
    private TicTacToePlayer player1;
    private TicTacToePlayer player2;

    LoginUI() {
        this.improved_UI();
        this.connect_to_DB();
        this.initial_UI();
    }

    private void improved_UI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Button.font", new Font("SansSerif", Font.BOLD, 16));
            UIManager.put("Label.font", new Font("SansSerif", Font.BOLD, 14));
            UIManager.put("TextField.font", new Font("SansSerif", Font.PLAIN, 14));
            UIManager.put("PasswordField.font", new Font("SansSerif", Font.PLAIN, 14));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to set system look and feel: " + e.getMessage());
        }
    }

    private void initial_UI() {
        frame = new JFrame("Tic Tac Toe - Welcome");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(450, 250);
        frame.setLayout(new FlowLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        panel.setBackground(new Color(27, 39, 56));

        JLabel title = new JLabel("Welcome to Tic Tac Toe!");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 26));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Color.CYAN);

        JButton login_btn = new JButton("Login and Play");
        JButton register_btn = new JButton("Register");

        login_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        register_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        login_btn.setBackground(Color.ORANGE);
        login_btn.setForeground(Color.WHITE);
        login_btn.setFocusPainted(false);
        register_btn.setBackground(Color.GREEN);
        register_btn.setForeground(Color.WHITE);

        register_btn.addActionListener(_ -> this.register_UI());
        login_btn.addActionListener(_ -> this.login_UI());

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(login_btn);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(register_btn);

        frame.setContentPane(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void register_UI() {
        JFrame register_frame = new JFrame();
        register_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        register_frame.setTitle("Tic Tac Toe Register");
        register_frame.setResizable(false);
        register_frame.setSize(300, 300);
        register_frame.setLayout(new GridLayout(4, 2, 10, 10));

        JTextField name_field = new JTextField();
        JTextField email_field = new JTextField();
        JPasswordField passwd_field = new JPasswordField();

        JButton register_btn = new JButton("Go");
        register_btn.addActionListener(_ -> {
            String name = name_field.getText().trim();
            String email = email_field.getText().trim();
            char[] passwd = passwd_field.getPassword();

            if(name.isEmpty() || email.isEmpty() || passwd.length == 0) {
                JOptionPane.showMessageDialog(register_frame, "Please fill in all the fields.");
                return;
            } else if(!CryptoUtil.check_email_format(email)) {
                JOptionPane.showMessageDialog(register_frame, "Invalid email format.");
                return;
            }

            try {
                String hashed_passwd = CryptoUtil.hash_passwd(passwd);
                Arrays.fill(passwd, '\0');

                String sql = "INSERT INTO credentials (name, email, won, passwd) VALUES (?, ?, 0, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);

                statement.setString(1, name);
                statement.setString(2, email);
                statement.setString(3, hashed_passwd);
                statement.executeUpdate();

                JOptionPane.showMessageDialog(register_frame, "Registered successfully as " + email + ".");
                register_frame.dispose();
            } catch(SQLIntegrityConstraintViolationException e) {
                JOptionPane.showMessageDialog(register_frame, "Email already registered.");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(register_frame, "Registration failed: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(register_frame, "Hashing error: " + e.getMessage());
            }
        });

        register_frame.add(new JLabel("Name"));
        register_frame.add(name_field);
        register_frame.add(new JLabel("Email"));
        register_frame.add(email_field);
        register_frame.add(new JLabel("Password"));
        register_frame.add(passwd_field);
        register_frame.add(new JLabel());
        register_frame.add(register_btn);

        register_frame.setLocationRelativeTo(frame);
        register_frame.setVisible(true);
    }

    private void login_UI() {
        JFrame login_frame = new JFrame();
        login_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login_frame.setTitle("Tic Tac Toe Login");
        login_frame.setResizable(false);
        login_frame.setSize(400, 300);
        login_frame.setLayout(new GridLayout(6, 2, 10, 10));

        JTextField email1_field = new JTextField();
        JPasswordField passwd1_field = new JPasswordField();
        JTextField email2_field = new JTextField();
        JPasswordField passwd2_field = new JPasswordField();

        JButton login_btn = new JButton("Go");
        login_btn.addActionListener(_ -> {
            String email1 = email1_field.getText().trim();
            char[] passwd1 = passwd1_field.getPassword();
            String email2 = email2_field.getText().trim();
            char[] passwd2 = passwd2_field.getPassword();

            if(email1.isEmpty() || passwd1.length == 0 || email2.isEmpty() || passwd2.length == 0) {
                JOptionPane.showMessageDialog(login_frame, "Please fill in all the fields.");
                return;
            } else if(email1.equals(email2)) {
                JOptionPane.showMessageDialog(login_frame, "Player 1 and Player 2 cannot be the same.");
                return;
            }

            try {
                String hashed_passwd1 = CryptoUtil.hash_passwd(passwd1);
                Arrays.fill(passwd1, '\0');
                String hashed_passwd2 = CryptoUtil.hash_passwd(passwd2);
                Arrays.fill(passwd2, '\0');

                String sql = "SELECT * FROM credentials WHERE email = ? AND passwd = ?";
                PreparedStatement statement = connection.prepareStatement(sql);

                statement.setString(1, email1);
                statement.setString(2, hashed_passwd1);
                ResultSet res = statement.executeQuery();
                if(!res.next()) {
                    JOptionPane.showMessageDialog(login_frame, "Invalid credentials for player 1.");
                    return;
                }

                String name1 = res.getString("name");
                int won1 = res.getInt("won");

                statement.setString(1, email2);
                statement.setString(2, hashed_passwd2);
                res = statement.executeQuery();
                if(!res.next()) {
                    JOptionPane.showMessageDialog(login_frame, "Invalid credentials for player 2.");
                    return;
                }

                String name2 = res.getString("name");
                int won2 = res.getInt("won");

                res = check_saved(email1, email2);
                boolean has_saved = false;
                String board_state = null;
                boolean turn = true;

                if(res.next()) {
                    has_saved = true;
                    board_state = res.getString("board_state");
                    turn = res.getBoolean("turn");
                }

                player1 = new TicTacToePlayer(name1, email1, won1);
                player2  = new TicTacToePlayer(name2, email2, won2);

                if(has_saved) {
                    int opt = JOptionPane.showConfirmDialog(frame, "An unfinished game between the two users was found. Load saved game? (If chosen no, the saved game will be lost.)", "Load Game", JOptionPane.YES_NO_OPTION);
                    if(opt == JOptionPane.YES_OPTION) {
                        new GameUI(player1, player2, connection, board_state, turn);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Starting a new game between " + name1 + " and " + name2 + ".");
                        new GameUI(player1, player2, connection, null, turn);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Logged in successfully. Starting a game between " + name1 + " and " + name2 + ".");
                    new GameUI(player1, player2, connection, null, false);
                }

                login_frame.dispose();
                frame.dispose();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(login_frame, "Login failed: " + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(login_frame, "Hashing error: " + e.getMessage());
            }
        });

        login_frame.add(new JLabel("Player 1 email"));
        login_frame.add(email1_field);
        login_frame.add(new JLabel("Player 1 password"));
        login_frame.add(passwd1_field);
        login_frame.add(new JLabel("Player 2 email"));
        login_frame.add(email2_field);
        login_frame.add(new JLabel("Player 2 password"));
        login_frame.add(passwd2_field);
        login_frame.add(new JLabel());
        login_frame.add(login_btn);

        login_frame.setLocationRelativeTo(frame);
        login_frame.setVisible(true);
    }

    private ResultSet check_saved(String p1_email, String p2_email) throws SQLException {
        if(p1_email.compareTo(p2_email) > 0) {
            String tmp = p1_email;
            p1_email = p2_email;
            p2_email = tmp;
        }

        String sql = "SELECT board_state, turn FROM saved_games WHERE p1_email = ? AND p2_email = ?";
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, p1_email);
        statement.setString(2, p2_email);

        return statement.executeQuery();
    }

    private void connect_to_DB() {
        try {
            connection = DriverManager.getConnection(DatabaseUtil.DB_URL + DatabaseUtil.DB_NAME, DatabaseUtil.DB_USER, DatabaseUtil.DB_PASS);
        } catch (SQLException e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(frame, "Database connection failed: " + e.getMessage());
            System.exit(1);
        }
    }
}