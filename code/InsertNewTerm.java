/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package database_insert;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 *
 * @author christina
 */
@WebServlet("/insertnewterm")
public class InsertNewTerm extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet InsertNewTerm</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet InsertNewTerm at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JSONObject jsonResponse = new JSONObject();

        try {
            String url = "jdbc:mysql://localhost";
            String databaseName = "glossary";
            int port = 3306;
            String username = "root";
            String password = "";
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = null;
            try {
                con = DriverManager.getConnection(url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password);
                System.out.println("Connected to the database (insert)!");

                String term = request.getParameter("term");
                String definition = request.getParameter("definition");
                String source = request.getParameter("source");
                String link = request.getParameter("link");
                String insertQuery = "INSERT INTO terms (term, definition, source, link) VALUES "
                        + "('" + term + "', '" + definition + "', '" + source + "', '" + link + "')";

                try ( Statement statement = con.createStatement()) {
                    int rowsAffected = statement.executeUpdate(insertQuery);
                    if (rowsAffected > 0) {
                        jsonResponse.put("success", true);
                        jsonResponse.put("message", "New term inserted successfully!");
                    } else {
                        jsonResponse.put("success", false);
                        jsonResponse.put("message", "Failed to insert the new term.");
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(InsertNewTerm.class.getName()).log(Level.SEVERE, null, ex);
                jsonResponse.put("success", false);
                jsonResponse.put("message", "An error occurred while connecting to the database or inserting the new term.");
            } finally {
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            Logger.getLogger(InsertNewTerm.class.getName()).log(Level.SEVERE, null, e);
            jsonResponse.put("success", false);
            jsonResponse.put("message", "An error occurred while loading the database driver.");
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse.toString());
    }
}
