package src.daol;



import src.connection.DBConnection;
import src.pojo.Appointment;
import src.pojo.Patient;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class AppointmentDAO {
    private Connection connection;



    // 1. View all appointments
    public static List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        try {
            String sql = "SELECT appointment_id, patient_id, doctor_id, appointment_date, appointment_time, status, created_at FROM appointment";

            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Appointment app = new Appointment(
                        rs.getInt("appointment_id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getDate("appointment_date").toLocalDate(),
                        rs.getTime("appointment_time").toLocalTime(),
                        rs.getString("status"),
                        null, // symptoms excluded
                        null, // diagnosis excluded
                        null, // prescription excluded
                        null, // notes excluded
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                appointments.add(app);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }
    //     2.view patient details
    public static List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        try {
            String sql = "SELECT Pid, Pname, Page, Pgender, Pblood_group, Pcontact, Pemail, Paddress, specialization, doctor, registration_date, appointment_date FROM patient;";
            Connection conn = DBConnection.getConnection();

            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Patient p = new Patient(
                        rs.getInt("Pid"),
                        rs.getString("Pname"),
                        rs.getInt("Page"),
                        rs.getString("Pgender"),
                        rs.getString("Pblood_group"),
                        rs.getLong("Pcontact"),
                        rs.getString("Pemail"),
                        rs.getString("Paddress"),
                        rs.getString("specialization"),
                        rs.getString("doctor"),
                        rs.getTimestamp("registration_date").toLocalDateTime(),
                        rs.getDate("appointment_date").toLocalDate()
                );
                patients.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patients;
    }

    public static boolean updateAppointment(int appointmentId, String newDate, String newTime, int newDoctorId, String status) {
        try {
            String sql = "UPDATE Appointment SET appointment_date = ?, appointment_time = ?, doctor_id = ?, status = ? WHERE appointment_id = ?";
            Connection conn = DBConnection.getConnection();

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(newDate));
            stmt.setTime(2, Time.valueOf(newTime));
            stmt.setInt(3, newDoctorId);
            stmt.setString(4, status);
            stmt.setInt(5, appointmentId);

            int rowsUpdated = stmt.executeUpdate();
            System.out.println("Rows updated: " + rowsUpdated); // Debugging output
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Appointment autoScheduleForPatient(int patientId) throws SQLException {
        String specialization = null;

        try (Connection conn = DBConnection.getConnection()) {

            // Step 1: Get specialization from patient table
            String patientQuery = "SELECT specialization FROM patient WHERE Pid = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(patientQuery)) {
                pstmt.setInt(1, patientId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        specialization = rs.getString("specialization");
                    } else {
                        System.out.println("Patient not found.");
                        return null;
                    }
                }
            }

            // Step 2: Find a doctor with the same specialization
            int doctorId = -1;
            String doctorQuery = "SELECT doctor_id FROM doctor WHERE specialization = ? LIMIT 1";
            try (PreparedStatement pstmt = conn.prepareStatement(doctorQuery)) {
                pstmt.setString(1, specialization);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        doctorId = rs.getInt("doctor_id");
                    } else {
                        System.out.println("No available doctor with specialization: " + specialization);
                        return null;
                    }
                }
            }

            // Step 3: Schedule appointment using your existing method
            return autoScheduleAppointments(patientId, doctorId, specialization);

        } // ✅ conn is automatically closed here

    }



    public static Appointment autoScheduleAppointments(int patientId, int doctorId, String specialization) throws SQLException {
        LocalDate currentDate = LocalDate.now();
        LocalTime startTime = LocalTime.of(9, 0); // Start at 9:00 AM
        LocalTime endTime = LocalTime.of(17, 0);  // End at 5:00 PM
        LocalDateTime createdAt = LocalDateTime.now();
        // Check if patient already has a scheduled appointment
        String checkSql = "SELECT COUNT(*) FROM Appointment WHERE patient_id = ? AND status = 'Scheduled'";
        Connection conn = DBConnection.getConnection();

        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, patientId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
//                System.out.println("This patient has already scheduled an appointment.");
                return null; // Don't schedule again
            }
        }

        for (int i = 0; i < 30; i++) {
            LocalDate dateToCheck = currentDate.plusDays(i);
            LocalTime timeToCheck = startTime;

            while (timeToCheck.isBefore(endTime)) {
                if (isSlotAvailable(doctorId, dateToCheck, timeToCheck)) {
                    String sql = "INSERT INTO Appointment (patient_id, doctor_id, appointment_date, appointment_time, status, symptoms, created_at) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)";

                    try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                        pstmt.setInt(1, patientId);
                        pstmt.setInt(2, doctorId);
                        pstmt.setDate(3, Date.valueOf(dateToCheck));
                        pstmt.setTime(4, Time.valueOf(timeToCheck));
                        pstmt.setString(5, "Scheduled");
                        pstmt.setString(6, specialization); // Save specialization as note/symptoms
                        pstmt.setTimestamp(7, Timestamp.valueOf(createdAt));

                        pstmt.executeUpdate();

                        ResultSet rs = pstmt.getGeneratedKeys();
                        int appointmentId = 0;
                        if (rs.next()) {
                            appointmentId = rs.getInt(1);
                        }

                        return new Appointment(appointmentId, patientId, doctorId, dateToCheck, timeToCheck,
                                "Scheduled", specialization, "", "", "", createdAt);
                    }
                }
                timeToCheck = timeToCheck.plusMinutes(30);
            }
        }

        return null; // No slot found
    }
    private static boolean isSlotAvailable(int doctorId, LocalDate date, LocalTime time) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Appointment WHERE doctor_id = ? AND appointment_date = ? AND appointment_time = ?";
        Connection conn = DBConnection.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, doctorId);
            pstmt.setDate(2, Date.valueOf(date));
            pstmt.setTime(3, Time.valueOf(time));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        }
        return false;
    }

    // 5. Cancel appointment (delete or update status)
    public static boolean cancelAppointment(int appointmentId) {
        try {
            String sql = "UPDATE Appointment SET status = 'Cancelled' WHERE appointment_id = ?";
            Connection conn = DBConnection.getConnection();

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, appointmentId);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 6. Search appointment by patient ID
    public static List<Appointment> searchAppointmentByPatientId(int patientId) {
        List<Appointment> appointments = new ArrayList<>();

        String sql = "SELECT * FROM Appointment WHERE patient_id = ?";
        Connection conn = DBConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Appointment app = new Appointment(
                            rs.getInt("appointment_id"),
                            rs.getInt("patient_id"),
                            rs.getInt("doctor_id"),
                            rs.getDate("appointment_date").toLocalDate(),
                            rs.getTime("appointment_time").toLocalTime(),
                            rs.getString("status"),
                            rs.getString("symptoms"),
                            rs.getString("diagnosis"),
                            rs.getString("prescription"),
                            rs.getString("notes"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                    appointments.add(app);
                }
            }

        } catch (SQLException e) {
            System.err.println(" Error while searching appointments for patient ID " + patientId + ": " + e.getMessage());
        }

        return appointments;
    }


}
