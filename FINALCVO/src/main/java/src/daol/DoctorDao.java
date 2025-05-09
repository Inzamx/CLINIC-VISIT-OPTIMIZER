package src.daol;



import src.connection.DBConnection;
import src.service.FrontDeskUI;
import src.usermain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

public class DoctorDao {

    public static void docValidation() {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("\033[38;2;255;165;0mEnter Doctor Id : \u001B[0m");
            int doctor_id = sc.nextInt();
            sc.nextLine(); // Clear buffer

            String query = "SELECT full_name, password FROM clinic_visit_optimizer.doctor WHERE doctor_id = ?";
            Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, doctor_id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new Exception("Resource with ID " + doctor_id + " not found in the database.");
            }

            String username = rs.getString("full_name");
            String dbPassword = rs.getString("password");

            int attempts = 3;

            while (attempts > 0) {
                System.out.println("\033[38;2;255;165;0mEnter Password : \u001B[0m");
                String inputPassword = sc.nextLine();

                if (inputPassword.equals(dbPassword)) {
                    System.out.print("\033[1m\u001B[32m . * Welcome * . \u001B[0m\033[0m");
                    System.out.println("\033[1m\u001B[32m " + username + " \u001B[0m\033[0m");
                    docSelection(doctor_id);
                    return;
                } else {
                    attempts--;
                    System.out.println("\u001B[31mIncorrect password.\u001B[0m");
                    if (attempts > 0) {
                        System.out.println("\u001B[31mYou have " + attempts + " attempt(s) left.\u001B[0m");
                    }
                }
            }

            // Wait for 10 seconds after 3 wrong attempts
            System.out.println("\u001B[33mToo many failed attempts. Please wait:\u001B[0m");
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

        } catch (SQLException e) {
            System.out.println("\u001B[31mDatabase Error : " + e.getMessage() + " \u001B[0m");
        } catch (Exception e) {
            System.out.println("\u001B[31mError : " + e.getMessage() + " \u001B[0m");
        }

        User.entry(); // Return to main entry point
    }

    public static void docSelection(int doctor_id) {
        Scanner sc = new Scanner(System.in);
        boolean flag = false;
        System.out.println("\033[38;2;255;165;0mEnter Your Choice : \u001B[0m");
        while (!flag) {
            System.out.println("Enter 1 For Today's Appointment" +
                    "\nEnter 2 For View Up Coming Appointments" +
                    "\nEnter 3 For View Patient " +
                    "\nEnter 4 For Update Patient" +
                    "\nEnter 5 For View Patient History" +
                    "\nEnter 6 For View My Profile" +
                    "\nEnter 7 To Go Back");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    viewTodaysAppointments(doctor_id);
                    docSelection(doctor_id);
                    flag = true;
                    break;
                case 2:
                    viewUpComingAppointments(doctor_id);
                    docSelection(doctor_id);
                    flag = true;
                    break;
                case 3:
                    FrontDeskUI.viewPatientDetails();
                    docSelection(doctor_id);
                    flag = true;
                    break;
                case 4:
                    updateAppointmentDetailsById();
                    docSelection(doctor_id);
                    flag = true;
                    break;
                case 5:
                    viewPatientHistoryFromAppointmentId();
                    docSelection(doctor_id);
                    flag = true;
                    break;
                case 6:
                    myProfile(doctor_id);
                    docSelection(doctor_id);
                    flag = true;
                    break;
                case 7:
                    flag = true;
                    User.entry();
                    break;
                default:
                    System.out.println("\u001B[31mEnter a Valid Choice :\u001B[0m");
                    break;
            }
        }
    }


    public static void viewTodaysAppointments(int doctor_id) {
        String query = "SELECT a.appointment_id, a.appointment_time, a.status,p.pid, p.pname, p.page, p.pgender, p.pblood_group FROM clinic_visit_optimizer.appointment a JOIN clinic_visit_optimizer.patient p ON a.patient_id = p.pid WHERE a.doctor_id = ? AND a.appointment_date = CURDATE()";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, doctor_id);
            ResultSet rs = ps.executeQuery();

            boolean hasAppointments = false;
            while (rs.next()) {
                hasAppointments = true;
                System.out.println("Appointment ID : " + rs.getInt("appointment_id"));
                System.out.println("Time           : " + rs.getTime("appointment_time"));
                System.out.println("Status         : " + rs.getString("status"));
                System.out.println("Patient ID     : " + rs.getInt("pid"));
                System.out.println("Name           : " + rs.getString("pname"));
                System.out.println("Age            : " + rs.getInt("page"));
                System.out.println("Gender         : " + rs.getString("pgender"));
                System.out.println("Blood Group    : " + rs.getString("pblood_group"));
                System.out.println("---------------------------------------------------");
            }

            if (!hasAppointments) {
                System.out.println("\u001B[31mNo appointments scheduled for today : \u001B[0m");
            }

        } catch (SQLException e) {
            System.out.println("\u001B[31mDatabase Error : " + e.getMessage() + " \u001B[0m");
        }
    }


    public static void viewUpComingAppointments(int doctor_id) {
        String query = "SELECT a.appointment_id, a.appointment_date, a.appointment_time, a.status,p.pid, p.pname, p.page, p.pgender, p.pblood_group FROM clinic_visit_optimizer.appointment a JOIN clinic_visit_optimizer.patient p ON a.patient_id = p.pid WHERE a.doctor_id = ? AND a.appointment_date > CURDATE() ORDER BY a.appointment_date, a.appointment_time";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, doctor_id);
            ResultSet rs = ps.executeQuery();

            boolean hasAppointments = false;
            while (rs.next()) {
                hasAppointments = true;
                System.out.println("Appointment ID : " + rs.getInt("appointment_id"));
                System.out.println("Date           : " + rs.getDate("appointment_date"));
                System.out.println("Time           : " + rs.getTime("appointment_time"));
                System.out.println("Status         : " + rs.getString("status"));
                System.out.println("Patient ID     : " + rs.getInt("pid"));
                System.out.println("Name           : " + rs.getString("pname"));
                System.out.println("Age            : " + rs.getInt("page"));
                System.out.println("Gender         : " + rs.getString("pgender"));
                System.out.println("Blood Group    : " + rs.getString("pblood_group"));
                System.out.println("---------------------------------------------------");
            }

            if (!hasAppointments) {
                System.out.println("\u001B[31mNo upcoming appointments found : \u001B[0m");
            }

        } catch (SQLException e) {
            System.out.println("\u001B[31mDatabase Error : " + e.getMessage() + " \u001B[0m");
        }
    }



    public static void updateAppointmentDetailsById() {
        Scanner sc = new Scanner(System.in);
        int appointmentId;
        System.out.print("\033[38;2;255;165;0mEnter Appointment ID : \u001B[0m");

        // Validate appointment ID
        while (true) {
            String input = sc.nextLine();
            try {
                appointmentId = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println("\u001B[31mInvalid Id. Please Enter a valid Appointment ID.\u001B[0m");
            }
        }

        // Input validation for string fields
        String symptoms = getValidTextInput(sc, "Enter Symptoms");
        String diagnosis = getValidTextInput(sc, "Enter Diagnosis");
        String prescription = getValidTextInput(sc, "Enter Prescription");
        String notes = getValidTextInput(sc, "Enter Notes");

        // SQL update
        String query = "UPDATE clinic_visit_optimizer.appointment SET status = ?, symptoms = ?, diagnosis = ?, prescription = ?, notes = ? " +
                "WHERE appointment_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            String status = "Completed";
            ps.setString(1, status);
            ps.setString(2, symptoms);
            ps.setString(3, diagnosis);
            ps.setString(4, prescription);
            ps.setString(5, notes);
            ps.setInt(6, appointmentId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("\u001B[32mAppointment details updated successfully.\u001B[0m");
            } else {
                System.out.println("\u001B[31mNo appointment found with ID: " + appointmentId + "\u001B[0m");
            }

        } catch (SQLException e) {
            System.out.println("\u001B[31mDatabase Error : " + e.getMessage() + " \u001B[0m");
        }
    }

    // Helper method to validate string input
    private static String getValidTextInput(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            String input = sc.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("\u001B[31mInput cannot be empty. Please enter valid text.\u001B[0m");
            } else if (input.matches("\\d+")) {
                System.out.println("\u001B[31mInvalid input. Only text is allowed, not just numbers.\u001B[0m");
            } else {
                return input;
            }
        }
    }

    public static void viewPatientHistoryFromAppointmentId() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter old Appointment ID: ");
        int oldAppointmentId = scanner.nextInt();
        scanner.nextLine(); // clear buffer

        String getPatientIdQuery = "SELECT patient_id FROM Appointment WHERE appointment_id = ?";

        String fetchAllAppointmentsQuery = "SELECT a.appointment_date, d.full_name AS doctor_name, " +

                "a.diagnosis, a.prescription, a.notes, a.symptoms, a.status, " +

                "p.Pid AS patient_id, p.page AS patient_age, p.pgender AS patient_gender, " +

                "p.pblood_group AS patient_blood_group, p.pcontact AS patient_contact, " +

                "p.Pname AS patient_name " +

                "FROM Appointment a " +

                "JOIN Doctor d ON a.doctor_id = d.doctor_id " +

                "JOIN Patient p ON a.patient_id = p.Pid " +

                "WHERE a.patient_id = ? " +

                "ORDER BY a.appointment_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement getPatientIdStmt = conn.prepareStatement(getPatientIdQuery)) {

            getPatientIdStmt.setInt(1, oldAppointmentId);
            ResultSet patientRs = getPatientIdStmt.executeQuery();

            if (patientRs.next()) {
                int patientId = patientRs.getInt("patient_id");

                try (PreparedStatement historyStmt = conn.prepareStatement(fetchAllAppointmentsQuery)) {
                    historyStmt.setInt(1, patientId);
                    ResultSet historyRs = historyStmt.executeQuery();

                    System.out.println("\n=== Patient Appointment History ===");
                    while (historyRs.next()) {
                        Date date = historyRs.getDate("appointment_date");
                        String doctor = historyRs.getString("doctor_name");
                        String diagnosis = historyRs.getString("diagnosis");
                        String prescription = historyRs.getString("prescription");
                        String notes = historyRs.getString("notes");
                        String symptoms = historyRs.getString("symptoms");
                        String status = historyRs.getString("status");

                        // New patient details
                        patientId = historyRs.getInt("patient_id");
                        int age = historyRs.getInt("patient_age");
                        String gender = historyRs.getString("patient_gender");
                        String bloodGroup = historyRs.getString("patient_blood_group");
                        String contact = historyRs.getString("patient_contact");
                        String name = historyRs.getString("patient_name");

                        // Print the patient and appointment details
                        System.out.println("=== Patient Information ===");
                        System.out.println("Patient ID   : " + patientId);
                        System.out.println("Name         : " + name);
                        System.out.println("Age          : " + age);
                        System.out.println("Gender       : " + gender);
                        System.out.println("Blood Group  : " + bloodGroup);
                        System.out.println("Contact No.  : " + contact);
                        System.out.println("------------------------------");

                        System.out.println("=== Appointment Details ===");
                        System.out.println("Date        : " + date);
                        System.out.println("Doctor      : " + doctor);
                        System.out.println("Symptoms    : " + (symptoms != null ? symptoms : "N/A"));
                        System.out.println("Diagnosis   : " + (diagnosis != null ? diagnosis : "N/A"));
                        System.out.println("Prescription: " + (prescription != null ? prescription : "N/A"));
                        System.out.println("Notes       : " + (notes != null ? notes : "N/A"));
                        System.out.println("Status      : " + (status != null ? status : "N/A"));
                        System.out.println("------------------------------");
                    }
                }

            } else {
                System.out.println("\u001B[31mNo patient found for the given appointment ID.\u001B[0m");
            }

        } catch (SQLException e) {
            System.out.println("\u001B[31mDatabase Error : " + e.getMessage() + " \u001B[0m");
        }
    }


    public static void myProfile(int doctor_id) {
        try {
            String query = "Select doctor_id,full_name,specialization,contact_number,email,qualification,experience_years,working_Hours,salary,Status from clinic_visit_optimizer.doctor where doctor_id=?";
            Connection conn =DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, doctor_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("Id             : " + rs.getInt(1));
                System.out.println("Name           : " + rs.getString(2));
                System.out.println("Specialization : " + rs.getString(3));
                System.out.println("Contact Number : " + rs.getString(4));
                System.out.println("Email          : " + rs.getString(5));
                System.out.println("Qualification  : " + rs.getString(6));
                System.out.println("Experience     : " + rs.getInt(7));
                System.out.println("Working Hours  : " + rs.getString(8));
                System.out.println("Salary         : " + rs.getString(9));
                System.out.println("Status         : " + rs.getString(10));

            }
        } catch (SQLException e) {
            System.out.println("\u001B[31mDatabase Error : " + e.getMessage() + " \u001B[0m");
        }
    }

}