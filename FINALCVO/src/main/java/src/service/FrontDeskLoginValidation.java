package src.service;


import src.connection.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class FrontDeskLoginValidation {

    public static void fdLoginvalidation() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            int chances = 3;

            while (chances > 0) {
                System.out.println("\033[38;2;255;165;0mEnter Front Desk User Name:  \u001B[0m");
                String userName = sc.nextLine().trim().toLowerCase();

                System.out.println("\033[38;2;255;165;0mEnter Password:  \u001B[0m");
                String password = sc.nextLine().trim();

                if (validateCredentials(userName, password)) {
                    System.out.println("\u001B[32m. * Welcome Front Desk * .\u001B[0m");
                    frontDeskPanel(); // Call to front desk functionalities
                    return;
                } else {
                    chances--;
                    System.out.println("\u001B[31mUserName or Password Wrong\u001B[0m");
                    if (chances > 0) {
                        System.out.println("You have " + chances + " chance(s) left.");
                    }
                }
            }

            // After 3 failed attempts
            System.out.println("\u001B[33mToo many failed attempts. Please wait:");
            for (int i = 10; i > 0; i--) {
                System.out.print(i + "\r");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Interrupted!");
                    return;
                }
            }
            System.out.println("\u001B[0m"); // Reset color
        }
    }

    private static boolean validateCredentials(String username, String password) {
        boolean isValid = false;

        try {
            // JDBC connection (update database name, user, password accordingly)
            Connection conn = DBConnection.getConnection();


            String sql = "SELECT * FROM front_desk WHERE LOWER(clinic_visit_optimizername) = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            isValid = rs.next(); // True if at least one record matches

            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }

        return isValid;
    }

    public static void frontDeskPanel() {
        System.out.println(">> Front Desk Panel Opened...");
        FrontDeskUI fdUI = new FrontDeskUI();
        fdUI.frontDeskMenu();
    }
}
