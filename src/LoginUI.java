import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class LoginUI {
    private TicTacToePlayer player1;
    private TicTacToePlayer player2;

    LoginUI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Tic Tac Toe");
        frame.setResizable(false);
        frame.setSize(800, 600);

        JTextField p1_name = new JTextField();
        JTextField p1_mail = new JTextField();
        JTextField p2_name = new JTextField();
        JTextField p2_mail = new JTextField();
        JLabel status_label = new JLabel("standby");
        JButton go_btn = new JButton("Let's play!");

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(20, 10));
        panel.add(new JLabel("Player 1 info"));
        panel.add(p1_name);
        panel.add(p1_mail);
        panel.add(new JLabel("Player 2 info"));
        panel.add(p2_name);
        panel.add(p2_mail);
        panel.add(go_btn);

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.getContentPane().add(status_label, BorderLayout.SOUTH);

        go_btn.addActionListener(_ -> {
            if(!Objects.equals(p1_name.getText(), "") && !Objects.equals(p1_mail.getText(), "") && !Objects.equals(p2_name.getText(), "") && !Objects.equals(p2_mail.getText(), "")) {
                player1 = new TicTacToePlayer(p1_name.getText(), p1_mail.getText());
                player2 = new TicTacToePlayer(p2_name.getText(), p2_mail.getText());

                frame.dispose();
                new GameUI(player1, player2);
            } else {
                status_label.setText("Please enter all the fields.");
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}