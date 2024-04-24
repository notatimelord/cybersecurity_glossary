package database_insert;


/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author aleks
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

public class InsertExistentTerms {

    public static void main(String[] args) throws SQLException, ClassNotFoundException, ParseException {
        String url = "jdbc:mysql://localhost";
        String databaseName = "glossary";
        int port = 3306;
        String username = "root";
        String password = "";
        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection con = DriverManager.getConnection(
                url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password);
        System.out.println("Connected to the database (example)!");

        insertTerm(con, "ACCEPTABLE RISK", "The level of residual risk that has been determined to be a reasonable level of potential loss/disruption",
                "CIAO – Critical Infrastructure Assurance Office - USA", "unknown");
        insertTerm(con, "ACCOUNTABILITY", "The property that ensures that the actions of an entity may be traced uniquely to the entity",
                "ENISA", "unknown");
        insertTerm(con, "ALERT", "A formal notification that an incident has occurred which may develop into a Business Continuity Management or Crisis Management invocation",
                "ENISA", "unknown");
        insertTerm(con, "ALERT PHASE", "The first phase of a Business Continuity Plan in which the initial emergency procedures and damage assessments are activated",
                "ENISA", "unknown");
        insertTerm(con, "VENDOR", "An individual or company providing a service to a department or the organisation as a whole",
                "ENISA", "unknown");
        insertTerm(con, "VIRUS", "An unauthorised programme that inserts itself into a computer system and then propagates itself to other computers via networks or disks",
                "The BCI, modified by ENISA", "unknown");
        insertTerm(con, "VULNERABILITY", "The existence of a weakness, or design or implementation error that can lead to an unexpected undesirable event, compromising the security of the computer system, network, application, or protocol involved.",
                "ENISA", "unknown");
        insertTerm(con, "WARM (STANDBY) SITE", "Partially equipped office space which contains some or all of the system hardware, software, telecommunications and power sources.  The site may need to be prepared before receiving the system and recovery personnel.  See Work Area Recovery Facility",
                "ENISA", "unknown");
        insertTerm(con, "WORK AROUND", "A process of avoiding an incident or problem, either by employing a temporary fix or technique that means a Customer is not reliant on a CI that is known to cause failure",
                "ENISA", "unknown");

    }

    private static void insertTerm(Connection con, String term, String definition, String source, String link) throws SQLException {
        String insertQuery = "INSERT INTO terms (term, definition, source, link) VALUES "
                + "('" + term + "', '" + definition + "', '" + source + "', '" + link + "')";

        try ( Statement statement = con.createStatement()) {
            int rowsAffected = statement.executeUpdate(insertQuery);

            if (rowsAffected > 0) {
                System.out.println("New Term inserted successfully!");
            } else {
                System.out.println("Failed to insert the new term.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
