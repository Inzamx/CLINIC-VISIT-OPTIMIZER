package src.service;




import src.connection.DBConnection;
import src.daol.AppointmentDAO;
import src.pojo.Appointment;
import src.pojo.Patient;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



//=======================

import java.sql.*;
import java.util.Properties;

import static java.util.Currency.getInstance;
import static src.usermain.User.entry;


public class FrontDeskUI {

//    AppointmentDAO appointmentDAO = new AppointmentDAO();

    Scanner scanner = new Scanner(System.in);

    public void frontDeskMenu() {
        while (true) {
            System.out.println("\u001B[32m ---  Front Desk menu --- \u001B[0m");
            System.out.println("1. View All Appointments");
            System.out.println("2. View Patient Details");
            System.out.println("3. Schedule Appointment");
            System.out.println("4. Update Appointment");
            System.out.println("5. Cancel Appointment");
            System.out.println("6. Search Appointment by Patient Id");
            System.out.println("7. Exit");
            System.out.println("8. Send Mail to Patient"); // ✅ New Option
            System.out.print("\033[38;2;255;165;0mEnter choice:  \u001B[0m");

            int choice = 0;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\u001B[31mInvalid input. Please enter a valid number.\u001B[0m");
                continue; // Loop again
            }

            switch (choice) {
                case 1:
                    viewAppointments();
                    break;
                case 2:
                    viewPatientDetails();
                    break;
                case 3:
                    scheduleApp();
                    break;
                case 4:
                    updateAppointment();
                    break;
                case 5:
                    cancelAppointment();
                    break;
                case 6:
                    searchAppointment();
                    break;
                case 7:
                    System.out.println("Exiting Front Desk Menu...");
                    entry();
                    return; // Exit method
                case 8:
                    sendMailToPatient(); // ✅ New Method Call
                    break;
                default:
                    System.out.println("\u001B[31mInvalid choice.\u001B[0m");
            }
        }
    }

    //1.view all appointments

    public void viewAppointments() {
        Scanner scanner = new Scanner(System.in);
        List<Appointment> list = AppointmentDAO.getAllAppointments();

        while (true) {
            List<Appointment> filteredList = new ArrayList<>();

            // Display menu options
            System.out.println("Choose option to view appointments:");
            System.out.println("1) Daily (manual)");
            System.out.println("2) Monthly (manual)");
            System.out.println("3) Today (auto)");
            System.out.println("4) This Month (auto)"                                                                           );
            System.out.println("5) Go Back");

            int choice = 0;
            while (true) {
                System.out.print("\033[38;2;255;165;0mEnter choice(1 to 5):  \u001B[0m");
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                    if (choice >= 1 && choice <= 5) {
                        break;
                    } else {
                        System.out.println("\u001B[31mInvalid choice. Please enter a number between 1 and 5.\u001B[0m");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\u001B[31mInvalid input. Please enter a valid number.\u001B[0m");
                }
            }

            if (choice == 5) {
                System.out.println("Returning to the previous menu...");
                return;
            }

            switch (choice) {
                case 1: // Daily (manual)
                    LocalDate targetDate = null;
                    while (true) {
                        System.out.print("\033[38;2;255;165;0mEnter date (yyyy-MM-dd):  \u001B[0m");

                        String dateInput = scanner.nextLine();
                        try {
                            targetDate = LocalDate.parse(dateInput);
                            break;
                        } catch (Exception e) {
                            System.out.println("\u001B[31mInvalid date format. Use yyyy-MM-dd.\u001B[0m");
                        }
                    }
                    for (Appointment app : list) {
                        if (app.getAppointmentDate().equals(targetDate)) {
                            filteredList.add(app);
                        }
                    }
                    break;

                case 2: // Monthly (manual)
                    int month = 0, year = 0;
                    while (true) {
                        System.out.println("\033[38;2;255;165;0mEnter month (1-12):  \u001B[0m");
                        try {
                            month = Integer.parseInt(scanner.nextLine());
                            if (month >= 1 && month <= 12) {
                                break;
                            } else {
                                System.out.println("Month must be between 1 and 12.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("\u001B[31mInvalid number. Please enter a valid month.\u001B[0m");
                        }
                    }

                    while (true) {
                        System.out.println("\033[38;2;255;165;0mEnter year (e.g., 2025):  \u001B[0m");
                        try {
                            year = Integer.parseInt(scanner.nextLine());
                            if (year >= 1900 && year <= 2100) {
                                break;
                            } else {
                                System.out.println("Enter a realistic year (e.g., 2025).");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("\u001B[31mInvalid number. Please enter a valid year.\u001B[0m");
                        }
                    }

                    for (Appointment app : list) {
                        LocalDate appDate = app.getAppointmentDate();
                        if (appDate.getMonthValue() == month && appDate.getYear() == year) {
                            filteredList.add(app);
                        }
                    }
                    break;

                case 3: // Today
                    LocalDate today = LocalDate.now();
                    for (Appointment app : list) {
                        if (app.getAppointmentDate().equals(today)) {
                            filteredList.add(app);
                        }
                    }
                    break;

                case 4: // This Month
                    LocalDate now = LocalDate.now();
                    int currentMonth = now.getMonthValue();
                    int currentYear = now.getYear();
                    for (Appointment app : list) {
                        LocalDate appDate = app.getAppointmentDate();
                        if (appDate.getMonthValue() == currentMonth && appDate.getYear() == currentYear) {
                            filteredList.add(app);
                        }
                    }
                    break;
            }

            // Display results
            if (!filteredList.isEmpty()) {
                System.out.println("------------------------------------------------------------------------------------------");
                System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s%n",
                        "Appointment ID", "Patient ID", "Doctor ID", "Date", "Time", "Status");
                System.out.println("------------------------------------------------------------------------------------------");

                for (Appointment app : filteredList) {
                    System.out.printf("%-15d %-15d %-15d %-15s %-15s %-15s%n",
                            app.getAppointmentId(),
                            app.getPatientId(),
                            app.getDoctorId(),
                            app.getAppointmentDate().toString(),
                            app.getAppointmentTime().toString(),
                            app.getStatus());
                }
            } else {
                System.out.println("\u001B[31mNo appointments found for the selected criteria.\u001B[0m");
            }
        }
    }
    //    2. viewnewpatientregistration
    public static void viewPatientDetails() {
        Scanner scanner = new Scanner(System.in);
        List<Patient> list = AppointmentDAO.getAllPatients();

        while (true) {
            List<Patient> filteredList = new ArrayList<>();

            // Display menu options
            System.out.println("Choose option to view patient details:");
            System.out.println("1) Full Patient Records");
            System.out.println("2) This Month's Patient Records");
            System.out.println("3) Go Back");

            int choice = 0;
            while (true) {
                System.out.print("\033[38;2;255;165;0mEnter choice(1 to 3):  \u001B[0m");
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                    if (choice >= 1 && choice <= 3) {
                        break;
                    } else {
                        System.out.println("\u001B[31mInvalid choice. Please enter a number between 1 and 3.\u001B[0m");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\u001B[31mInvalid input. Please enter a valid number.\u001B[0m");
                }
            }

            if (choice == 3) {
                System.out.println("Returning to the previous menu...");
                return;
            }

            switch (choice) {
                case 1: // Full Patient Records
                    filteredList = list; // Show all records
                    break;

                case 2: // This Month's Patient Records
                    LocalDate currentDate = LocalDate.now();
                    Month currentMonth = currentDate.getMonth();
                    int currentYear = currentDate.getYear();

                    for (Patient patient : list) {
                        LocalDate regDate = patient.getRegistrationDate().toLocalDateTime().toLocalDate();
                        if (regDate.getMonth() == currentMonth && regDate.getYear() == currentYear) {
                            filteredList.add(patient);
                        }
                    }
                    break;

            }

            // Display results
            if (!filteredList.isEmpty()) {
                System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.printf("%-5s %-20s %-5s %-10s %-12s %-20s %-15s %-20s %-20s %-15s%n",
                        "ID", "Name", "Age", "Gender", "Blood Group", "Contact Number","Email" ,"Specialization", "Doctor", "Registration Date", "Appointment");
                System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------");

                for (Patient patient : filteredList) {
                    System.out.printf("%-5d %-20s %-5d %-10s %-12s %-20s %-15s %-20s %-20s %-15s%n",
                            patient.getId(),
                            patient.getName(),
                            patient.getAge(),
                            patient.getGender(),
                            patient.getBlood(),
                            patient.getContact(),
                            patient.getEmail(),
                            patient.getSpecialization(),
                            patient.getDoctor(),
                            patient.getRegistrationDate().toLocalDateTime().toLocalDate(), // Or format as needed
                            patient.getAppointmentDate());
                }
            } else {
                System.out.println("\u001B[31mNo patients found for the selected criteria.\u001B[0m");
            }
        }
    }
    //auto schdule appointment
    // 4. Update appointment (including doctor change)
    public void updateAppointment() {
        int appointmentId;
        while (true) {
            System.out.print("\033[38;2;255;165;0mEnter Appointment ID to update: \u001B[0m");

            String input = scanner.nextLine();
            try {
                appointmentId = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println("\u001B[31m Invalid Appointment ID. Please enter numbers only.\u001B[0m");
            }
        }

        String newDate;
        while (true) {
            System.out.print("\033[38;2;255;165;0mEnter New Appointment Date (YYYY-MM-DD): \u001B[0m");

            newDate = scanner.nextLine();
            if (newDate.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                break;
            } else {
                System.out.println("\u001B[31m Invalid date format. Please use YYYY-MM-DD.\u001B[0m");
            }
        }

        // Validate and convert time input (12-hour or 24-hour)
        String newTime;
        while (true) {
            System.out.print("\033[38;2;255;165;0mEnter New Appointment Time (e.g., 14:00, 2:30 PM):\u001B[0m");

            newTime = scanner.nextLine().trim().toUpperCase();

            try {
                // Handle 12-hour format
                if (newTime.matches(".*[AP]M$")) {
                    DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("h[:mm[:ss]] a");
                    LocalTime time = LocalTime.parse(newTime, inputFormat);
                    newTime = time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                    break;
                }

                // Handle 24-hour format
                if (newTime.matches("^\\d{2}:\\d{2}$")) {
                    newTime += ":00";
                    break;
                } else if (newTime.matches("^\\d{2}:\\d{2}:\\d{2}$")) {
                    break;
                } else {
                    System.out.println("\u001B[31m Invalid time format. Try formats like '2:30 PM', '14:30', or '14:30:00'.\u001B[0m");
                }

            } catch (DateTimeParseException e) {
                System.out.println("\u001B[31m Could not parse time. Please try again with correct format.\u001B[0m");
            }
        }

        int newDoctorId;
        while (true) {
            System.out.print("\033[38;2;255;165;0mEnter New Doctor ID:\u001B[0m");

            String docInput = scanner.nextLine();
            try {
                newDoctorId = Integer.parseInt(docInput);
                break;
            } catch (NumberFormatException e) {
                System.out.println("\u001B[31m Invalid Doctor ID. Please enter numbers only.\u001B[0m");
            }
        }

        String status;
        while (true) {
            System.out.print("\033[38;2;255;165;0mEnter Status (S/s for Scheduled, C/c for Cancelled):\u001B[0m");

            status = scanner.nextLine().trim().toUpperCase();
            if (status.equals("S") || status.equals("SCHEDULED")) {
                status = "SCHEDULED";
                break;
            } else if (status.equals("C") || status.equals("CANCELLED")) {
                status = "CANCELLED";
                break;
            } else {
                System.out.println("\u001B[31m Invalid status. Enter 'S' for Scheduled or 'C' for Cancelled.\u001B[0m");
            }
        }

        System.out.println("\u001B[33m⏳ Updating appointment...\u001B[0m");

        boolean result = AppointmentDAO.updateAppointment(appointmentId, newDate, newTime, newDoctorId, status);
        if (result) {
            System.out.println("\u001B[32m Appointment updated successfully!\u001B[0m");
        } else {
            System.out.println("\u001B[31m Failed to update appointment. Please check the Appointment ID and try again.\u001B[0m");
        }
    }

    //    // 5. Cancel appointment
    public void cancelAppointment() {
        int appointmentId;
        while (true) {
            System.out.print("\033[38;2;255;165;0mEnter Appointment ID to cancel:\u001B[0m");

            String input = scanner.nextLine();
            try {
                appointmentId = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println("\u001B[31m Invalid Appointment ID. Please enter numbers only.\u001B[0m");
            }
        }

        System.out.println("\u001B[33m⏳ Cancelling appointment...\u001B[0m");

        boolean result = AppointmentDAO.cancelAppointment(appointmentId);
        if (result) {
            System.out.println("\u001B[32m Appointment cancelled successfully!\u001B[0m");
        } else {
            System.out.println("\u001B[31m Failed to cancel appointment. Please check if the Appointment ID exists.\u001B[0m");
        }
    }


    // 6. Search appointment by patient ID
    public void searchAppointment() {
        int patientId;

        // Validate Patient ID input
        while (true) {
            System.out.print("\033[38;2;255;165;0mEnter Patient ID to search appointments:\u001B[0m");

            String input = scanner.nextLine();
            try {
                patientId = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println("\u001B[31m Invalid Patient ID. Please enter numbers only.\u001B[0m");
            }
        }

        System.out.println("\u001B[33m⏳ Searching for appointments...\u001B[0m");

        List<Appointment> list = AppointmentDAO.searchAppointmentByPatientId(patientId);

        if (list.isEmpty()) {
            System.out.println("\u001B[31m No appointments found for Patient ID: " + patientId + "\u001B[0m");
            return;
        }

        // Display header
        System.out.println("\n\u001B[32m  Appointments Found:\u001B[0m");
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.printf("\033[1;34m%-15s %-15s %-15s %-15s %-15s %-15s\033[0m%n",
                "Appointment ID", "Patient ID", "Doctor ID", "Date", "Time", "Status");
        System.out.println("------------------------------------------------------------------------------------------");

        // Display data rows
        for (Appointment app : list) {
            System.out.printf("%-15d %-15d %-15d %-15s %-15s %-15s%n",
                    app.getAppointmentId(),
                    app.getPatientId(),
                    app.getDoctorId(),
                    app.getAppointmentDate(),
                    app.getAppointmentTime(),
                    app.getStatus());
        }
    }

    public void autoScheduleForAllPatients() throws SQLException {
        String sql = "SELECT pid FROM patient";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            // Print header
            System.out.printf("%-15s %-20s\n", "Patient ID", "Appointment Status");
            System.out.println("------------------------------------------");

            while (rs.next()) {
                int patientId = rs.getInt("pid");
                Appointment appointment = AppointmentDAO.autoScheduleForPatient(patientId);

                if (appointment != null) {
                    System.out.printf("%-15d %-20s\n", patientId, "Scheduled on " + appointment.getAppointmentDate());
                } else {
                    System.out.printf("%-15d %-20s\n", patientId, "Already scheduled");
                }
            }
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (stmt != null) stmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }



    public void scheduleApp() {
        Scanner scanner = new Scanner(System.in);
        AppointmentDAO service = new AppointmentDAO();

        while (true) {
            // Display options
            System.out.println("Choose an option:");
            System.out.println("1) Auto-schedule for a particular patient");
            System.out.println("2) Auto-schedule for all patients");
            System.out.println("3) Go Back");

            int choice = 0;
            while (true) {
                System.out.print("\033[38;2;255;165;0mEnter choice (1 to 3): \u001B[0m");
                try {
                    choice = Integer.parseInt(scanner.nextLine());
                    if (choice >= 1 && choice <= 3) {
                        break;
                    } else {
                        System.out.println("\u001B[31mInvalid choice. Please enter a number between 1 and 3.\u001B[0m");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\u001B[31mInvalid input. Please enter a valid number.\u001B[0m");
                }
            }

            if (choice == 3) {
                System.out.println("Returning to the previous menu...");
                return;
            }

            try {
                switch (choice) {
                    case 1:
                        System.out.print("\033[38;2;255;165;0mEnter patient ID: \u001B[0m");
                        int patientId = Integer.parseInt(scanner.nextLine());
                        Appointment appointment = service.autoScheduleForPatient(patientId);
                        if (appointment != null) {
                            System.out.println("\033[32mAppointment successfully scheduled.\u001B[0m");
                            System.out.println("Patient ID: " + appointment.getPatientId() +
                                    " | Date: " + appointment.getAppointmentDate() +
                                    " | Time: " + appointment.getAppointmentTime());
                        } else {
                            System.out.println("\u001B[31mThis patient has already scheduled an appointment.\u001B[0m");
                        }
                        break;

                    case 2:
                        System.out.println("Auto-scheduling appointments for all patients...");
                        autoScheduleForAllPatients();
                        System.out.println("\033[32mAll patients have been scheduled successfully (if eligible).\u001B[0m");
                        break;
                }
            } catch (SQLException | NumberFormatException e) {
                System.out.println("\u001B[31mError: " + e.getMessage() + "\u001B[0m");
            }
        }
    }

//    public void sendMailToPatient() {
//        Scanner sc = new Scanner(System.in);
//        System.out.print("Enter Patient ID to send mail: ");
//        int patientId = sc.nextInt();
//        String toEmail = null;
//
//        // DB connection
//        try (Connection con = DBConnection.getConnection()) {
//            String query = "SELECT Pemail FROM patient WHERE Pid = ?";
//            PreparedStatement pst = con.prepareStatement(query);
//            pst.setInt(1, patientId);
//            ResultSet rs = pst.executeQuery();
//
//            if (rs.next()) {
//                toEmail = rs.getString("Pemail");
//            } else {
//                System.out.println("\u001B[31mPatient not found!\u001B[0m");
//                return;
//            }
//        } catch (Exception e) {
//            System.out.println("\u001B[31mDatabase error: " + e.getMessage() + "\u001B[0m");
//            return;
//        }
//
//        // Mail sending part
//        final String fromEmail = "sureshchandrabose111@gmail.com.com"; // Replace with actual email
//        final String password = "cwab aqkw niof uwge";     // Use app password if Gmail
//
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");
//
//        Session session = Session.getInstance(props, new Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(fromEmail, password);
//            }
//        });
//
//        try {
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(fromEmail));
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
//            message.setSubject("Clinic Appointment Confirmation");
//            message.setText("Dear Patient,\n\nYour appointment has been confirmed.\n\nThank you!");
//
//            Transport.send(message);
//            System.out.println("\u001B[32mMail sent successfully to " + toEmail + "\u001B[0m");
//
//        } catch (MessagingException e) {
//            System.out.println("\u001B[31mFailed to send email: " + e.getMessage() + "\u001B[0m");
//        }
//    }


    // Method to send mail to the patient
    public void sendMailToPatient() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Patient ID to send mail: ");
        int patientId = sc.nextInt();
        String toEmail = null;

        // Step 1: Database lookup for patient's email
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT Pemail FROM patient WHERE Pid = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, patientId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                toEmail = rs.getString("Pemail");
            } else {
                System.out.println("\u001B[31mPatient not found!\u001B[0m");
                return;
            }
        } catch (Exception e) {
            System.out.println("\u001B[31mDatabase error: " + e.getMessage() + "\u001B[0m");
            return;
        }

        // Step 2: Set up email sending configurations
        final String fromEmail = "sureshchandrabose111@gmail.com"; // Replace with sender email
        final String password = "cwab aqkw niof uwge"; // App-specific password

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Step 3: Create Session and Send Email
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Clinic Appointment Confirmation");
            message.setContent(
                    "<html><body>" +
                            "<p>Dear Patient,</p>" +
                            "<p>Your appointment has been confirmed. Thank you!</p>" +
                            "</body></html>",
                    "text/html");

            Transport.send(message);
            System.out.println("\u001B[32mMail sent successfully to " + toEmail + "\u001B[0m");

        } catch (MessagingException e) {
            System.out.println("\u001B[31mFailed to send email: " + e.getMessage() + "\u001B[0m");
        }
    }



}
