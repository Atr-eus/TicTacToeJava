import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Arrays;

public class LoginUI {
    private final String DB_URL = "jdbc:mysql://localhost:3306/entries";
    private JFrame frame;
    private Connection connection;
    private TicTacToePlayer player1;
    private TicTacToePlayer player2;

    LoginUI() {
        this.connect_to_DB();
        this.initial_UI();
    }

    private void initial_UI() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Tic Tac Toe Auth");
        frame.setResizable(false);
        frame.setSize(300, 300);
        frame.setLayout(new FlowLayout());

        JButton login_btn = new JButton("Login and Play");
        JButton register_btn = new JButton("Register");
        register_btn.addActionListener(_ -> this.register_UI());
        login_btn.addActionListener(_ -> this.login_UI());

        frame.add(login_btn);
        frame.add(register_btn);
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

                JOptionPane.showMessageDialog(login_frame, "Logged in successfully, initiating a game between " + name1 + " and " + name2 + ".");
                login_frame.dispose();
                frame.dispose();

                player1 = new TicTacToePlayer(name1, email1, won1);
                player2  = new TicTacToePlayer(name2, email2, won2);
                new GameUI(player1, player2, connection);
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

    private void connect_to_DB() {
        try {
            connection = DriverManager.getConnection(DB_URL, "root", "");
        } catch (SQLException e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(frame, "Database connection failed: " + e.getMessage());
            System.exit(1);
        }
    }
}