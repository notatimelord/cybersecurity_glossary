/*
author: christina
 */
package database_insert;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class GlossaryDatabaseMaker {

    public static void main(String[] args) {
        // Credentials
        String url = "jdbc:mysql://localhost:3306/";
        String databaseName = "glossary";
        String username = "root";
        String password = "";

        // Connection
        try {
            Connection con = DriverManager.getConnection(
                    url, username, password);
            System.out.println("Connected to the server!");

            // Creating a new database
            Statement statement = con.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + databaseName);
            System.out.println("Database created or already exists!");

            // Using the newly created or existing database
            statement.executeUpdate("USE " + databaseName);
            System.out.println("Using database: " + databaseName);

            // Creating a new table
            String createTableQuery = "CREATE TABLE IF NOT EXISTS Terms ("
                    + "id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "term VARCHAR(30) UNIQUE, "
                    + "definition VARCHAR(300), "
                    + "source VARCHAR(100),"
                    + "link VARCHAR(300), "
                    + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";

            statement.executeUpdate(createTableQuery);
            System.out.println("Glossary table created or already exists!");

            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
