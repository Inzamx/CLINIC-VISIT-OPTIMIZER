package src.connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/clinic_visit_optimizer";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "suresh@2003";

    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            System.out.println("\u001B[31m❌ Database connection failed. See details below:\u001B[0m");
            e.printStackTrace(); // Print full error to debug issues like ClassNotFound, access denied, etc.
        }
        return connection;
    }
}
