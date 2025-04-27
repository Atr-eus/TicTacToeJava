import java.sql.*;

public class DatabaseUtil {
    public static final String DB_URL = "jdbc:mysql://localhost:3306/";
    public static final String DB_NAME = "entries";
    public static final String DB_USER = "root";
    public static final String DB_PASS = "";

    public static void init() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL + DB_NAME, DB_USER, DB_PASS);
            System.out.println("Database already exists, connected.");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database doesn't seem to exist.");
            create_db();
        }
    }

    private static void create_db() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            Statement statement = connection.createStatement();

            System.out.println("Creating database...");
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
            System.out.println("Database created successfully.");

            statement.executeUpdate("USE " + DB_NAME);
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS credentials (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100), email VARCHAR(100) UNIQUE , won INT DEFAULT 0, passwd VARCHAR(255) )");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS saved_games (id INT AUTO_INCREMENT PRIMARY KEY, p1_email VARCHAR(100), p2_email VARCHAR(100), board_state VARCHAR(9), turn BOOLEAN, UNIQUE (p1_email, p2_email))");
            System.out.println("Tables credentials and saved_games created successfully.");

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to database: " + e.getMessage());
            System.exit(1);
        }
    }
}