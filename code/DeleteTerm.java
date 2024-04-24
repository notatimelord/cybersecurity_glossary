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
@WebServlet("/deleteterm")
public class DeleteTerm extends HttpServlet {

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
            out.println("<title>Servlet DeleteTerm</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet DeleteTerm at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            String term = request.getParameter("term");
            boolean success = false; // Flag to indicate whether the deletion was successful

            // Perform deletion operation
            String url = "jdbc:mysql://localhost";
            String databaseName = "glossary";
            int port = 3306;
            String username = "root";
            String password = "";

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                try ( Connection con = DriverManager.getConnection(url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password)) {
                    System.out.println("Connected to the database!");

                    try ( Statement statement = con.createStatement()) {
                        String deleteQuery = "DELETE FROM terms WHERE term='" + term + "'";
                        int rowsAffected = statement.executeUpdate(deleteQuery);
                        success = rowsAffected > 0;
                    }
                }
            } catch (ClassNotFoundException | SQLException ex) {
                // Log any exceptions
                Logger.getLogger(DeleteTerm.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Send response back to client
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("success", success); // Add success flag to JSON response

            PrintWriter out = response.getWriter();
            out.println(jsonResponse.toString());
        }



    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
