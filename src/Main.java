import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        DatabaseUtil.init();

        SwingUtilities.invokeLater(LoginUI::new);
    }
}