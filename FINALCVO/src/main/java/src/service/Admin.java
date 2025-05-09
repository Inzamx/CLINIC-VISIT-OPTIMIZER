package src.service;


import src.connection.DBConnection;
import src.daol.Doctor;
import src.daol.FrontDesk;
import src.daol.Patients;
import src.usermain.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class Admin {
    public static void adminValidation() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            int attempts = 3;

            while (attempts > 0) {
                System.out.println("\033[38;2;255;165;0mEnter User Name : \u001B[0m");
                String user_Name = sc.nextLine().trim().toLowerCase();

                System.out.println("\033[38;2;255;165;0mEnter Password : \u001B[0m");
                String password = sc.nextLine().trim();

                if (user_Name.equals("admin") && password.equals("admin@123")) {
                    System.out.println("\033[1m\u001B[32m . * Welcome Admin * . \u001B[0m\033[0m");
                    adminPanel();
                    return;
                } else {
                    attempts--;
                    System.out.println("\u001B[31mUserName Or Password Wrong\u001B[0m");
                    if (attempts > 0) {
                        System.out.println("\u001B[31mYou have only \u001B[0m" + attempts + "\u001B[31m chances left.\u001B[0m");
                    }
                }
            }

            // Wait for 10 seconds after 3 wrong attempts
            System.out.println("\u001B[33mToo many failed attempts. Please wait:");
            for (int i = 10; i > 0; i--) {
                System.out.print(i + " \r");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Interrupted!");
                    return;
                }
            }
            System.out.println("\u001B[0m"); // Reset color
            User.entry(); // Call entry menu again
        }
    }


    public static void adminPanel() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\033[1m\033[38;2;255;165;0m-----Admin Panel----- \u001B[0m\033[0m");
        System.out.println("\033[38;2;255;165;0mEnter Your Choice : \u001B[0m");
        boolean flag = false;
        while (!flag) {
            System.out.println("Enter 1. For Doctor Management" +
                    "\nEnter 2. For Front Desk Executive Management" +
                    "\nEnter 3. For Appointment Monitoring" +
                    "\nEnter 4. For System Analytics" +
                    "\nEnter 5.Go Back");
            try {
                int choice1 = sc.nextInt();
                switch (choice1) {
                    case 1:
                        Doctor.doctorsPanel();
                        Admin.adminPanel();
                        flag = true;
                        break;
                    case 2:
                        FrontDesk.frontDeskPanel();
                        Admin.adminPanel();
                        flag = true;
                        break;
                    case 3:
                        Patients.viewAppointments();
                        Admin.adminPanel();
                        flag = true;
                        break;
                    case 4:
                        viewSystemAnalytics();
                        Admin.adminPanel();
                        flag = true;
                        break;
                    case 5:
                        User.entry();
                        flag = true;
                        break;
                    default:
                        System.out.println("\u001B[31mEnter a Valid Choice :\u001B[0m");
                }
            } catch (InputMismatchException e) {
                System.out.println("\u001B[31mEnter a Valid Choice :\u001B[0m");
                sc.nextLine();
            }
        }
    }

    public static void viewSystemAnalytics() {
        Scanner sc = new Scanner(System.in);
        System.out.print("\033[38;2;255;165;0mEnter Your Choice : \u001B[0m");
        while (true) {
            try {
                System.out.println("\nEnter 1. For Total Appointments" +
                        "\nEnter 2. For Total count Particular Date" +
                        "\nEnter 3. For Total count of Doctors" +
                        "\nEnter 4. For Go Back");
                int choice = sc.nextInt();
                if (choice == 1) {
                    totalCountAppointments();
                    viewSystemAnalytics();
                    break;
                } else if (choice == 2) {
                    totalCountParticularAppointments();
                    viewSystemAnalytics();
                    break;
                } else if (choice == 3) {
                    findMostConsultedDoctorThisMonth();
                    viewSystemAnalytics();
                    break;
                } else if (choice == 4) {
                    adminPanel();
                } else {
                    System.out.print("\u001B[31mEnter a Valid Choice :\u001B[0m");
                }
            } catch (InputMismatchException e) {
                System.out.print("\u001B[31mEnter a Valid Choice :\u001B[0m");
                sc.next();
            }
        }
    }

    public static void totalCountAppointments() {
        try {
            String query = "SELECT COUNT(*) AS total FROM clinic_visit_optimizer.appointment WHERE DATE(appointment_date) = CURDATE();";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt("total");
            }
            if (count > 0) {
                System.out.println("Total Appointments : " + count);
            } else {
                System.out.println("\u001B[31mNO Appointments Today !\u001B[0m");
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31m Database : "+e.getMessage()+"\u001B[0m");
        }
    }

    public static void totalCountParticularAppointments() {
        try {
            Scanner sc = new Scanner(System.in);
            String query = "SELECT COUNT(*) AS total FROM clinic_visit_optimizer.appointment WHERE DATE(appointment_date) = ?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            String newDate;
            System.out.print("\033[38;2;255;165;0mEnter Appointment Date (YYYY-MM-DD) : \u001B[0m");
            while (true) {
                newDate = sc.nextLine();
                if (newDate.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                    break;
                } else {
                    System.out.println("\u001B[31mInvalid date format. Please use YYYY-MM-DD.\u001B[31m");
                }
            }
            ps.setDate(1, Date.valueOf(newDate));
            ResultSet rs = ps.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt("total");
            }
            if (count > 0) {
                System.out.println("Total Appointments :" + count);
            } else {
                System.out.println("\u001B[31mNO Appointments !\u001B[31m");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void findMostConsultedDoctorThisMonth() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\033[38;2;255;165;0mEnter Your Choice : \u001B[0m");
        while (true) {
            System.out.println("1. View most consulted doctor for a particular month");
            System.out.println("2. View most consulted doctor for the current month");
            System.out.println("3. Go back");
            int choice = scanner.nextInt();
            int month, year;
            switch (choice) {
                case 1:
                    System.out.print("\033[38;2;255;165;0mEnter month (1-12): \u001B[0m");
                    month = scanner.nextInt();
                    System.out.print("\033[38;2;255;165;0mEnter year (e.g., 2025): \u001B[0m");
                    year = scanner.nextInt();
                    findMostConsultedDoctorByMonth(month, year);
                    break;

                case 2:
                    LocalDate now = LocalDate.now();
                    findMostConsultedDoctorByMonth(now.getMonthValue(), now.getYear());
                    break;

                case 3:
                    viewSystemAnalytics();
                    return;

                default:
                    System.out.println("\u001B[31mEnter a Valid Choice :\u001B[0m");
            }
        }
    }

    public static int findMostConsultedDoctorByMonth(int month, int year) {
        Map<Integer, Integer> doctorAppointmentCount = new HashMap<>();

        try {
            Connection con = DBConnection.getConnection();

            // Fixed query: use appointment_date for filtering by month and year
            String query = "SELECT doctor_id FROM clinic_visit_optimizer.appointment " +
                    "WHERE MONTH(appointment_date) = ? AND YEAR(appointment_date) = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, month);
            ps.setInt(2, year);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int doctorId = rs.getInt("doctor_id");
                doctorAppointmentCount.put(
                        doctorId,
                        doctorAppointmentCount.getOrDefault(doctorId, 0) + 1
                );
            }

            if (doctorAppointmentCount.isEmpty()) {
                System.out.println("\u001B[31mNo Appointment Data found For \u001B[0m " + month + " / " + year);
                return -1;
            }

            System.out.println("Consultation summary for " + month + " /" + year + " :");

            int maxAppointments = -1;
            int mostConsultedDoctorId = -1;

            for (Map.Entry<Integer, Integer> entry : doctorAppointmentCount.entrySet()) {
                int doctorId = entry.getKey();
                int totalMinutes = entry.getValue() * 30;
                System.out.println();
                System.out.println("Doctor ID : " + doctorId +
                        "\nTotal consultation time: " + totalMinutes + " minutes");
                System.out.println("============================================");
                if (entry.getValue() > maxAppointments) {
                    maxAppointments = entry.getValue();
                    mostConsultedDoctorId = doctorId;
                }
            }

            int totalMinutes = maxAppointments * 30;
            System.out.println("Most consulted doctor: ID = " + mostConsultedDoctorId +
                    "\nAppointments = " + maxAppointments +
                    "\nTotal consultation time = " + totalMinutes + " minutes");
            System.out.println("============================================");
            System.out.println();
            return mostConsultedDoctorId;

        } catch (SQLException e) {
            System.out.println("\u001B[31mDatabase : "+e.getMessage()+"\u001B[0m");
            return -1;
  }
}

}
