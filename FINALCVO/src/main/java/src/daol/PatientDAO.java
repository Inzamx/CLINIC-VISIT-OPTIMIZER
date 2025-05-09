package src.daol;



import src.connection.DBConnection;
import src.pojo.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {

    public boolean insertPatient(Patient patient) {
        String sql = "INSERT INTO clinic_visit_optimizer.patient (Pname, Page, Pgender, Pblood_group, Pcontact,Pemail, Paddress, specialization, doctor, appointment_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn= DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, patient.getName());
            ps.setInt(2, patient.getAge());
            ps.setString(3, patient.getGender());
            ps.setString(4, patient.getBlood());
            ps.setLong(5, patient.getContact());
            ps.setString(6,patient.getEmail());
            ps.setString(7, patient.getAddress());
            ps.setString(8, patient.getSpecialization());
            ps.setString(9, patient.getDoctor());
            ps.setDate(10, patient.getAppointmentDate());

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("\u001B[31mDatabase : "+e.getMessage()+"\u001B[0m");
        }
        return false;
    }

    public boolean updatePatientContact(int id, long newContact) {
        String sql = "UPDATE clinic_visit_optimizer.patient SET Pcontact = ? WHERE Pid = ?";
        Connection conn= DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, newContact);
            ps.setInt(2, id);

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("\u001B[31mDatabase : "+e.getMessage()+"\u001B[0m");
        }
        return false;
    }

//    public List<Patient> getAllPatients() {
//        List<Patient> patients = new ArrayList<>();
//        String query = "SELECT * FROM clinic_visit_optimizer.patient";
//        Connection conn= DBConnection.getConnection();
//        try (Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(query)) {
//
//            while (rs.next()) {
//                Patient p = new Patient();
//                p.setId(rs.getInt("Pid"));
//                p.setName(rs.getString("Pname"));
//                p.setAge(rs.getInt("Page"));
//                p.setGender(rs.getString("Pgender"));
//                p.setBlood(rs.getString("Pblood_group"));
//                p.setContact(rs.getLong("Pcontact"));
//                p.setAddress(rs.getString("Paddress"));
//                p.setSpecialization(rs.getString("specialization"));
//                p.setDoctor(rs.getString("doctor"));
//                p.setRegistrationDate(rs.getTimestamp("registration_date"));
//                p.setAppointmentDate(rs.getDate("appointment_date"));
//
//                patients.add(p);
//            }
//        } catch (SQLException e) {
//            System.out.println("\u001B[31mDatabase : "+e.getMessage()+"\u001B[0m");
//        }
//        return patients;
//    }


    public List<String> getDistinctSpecializations() {
        List<String> specializations = new ArrayList<>();
        String query = "SELECT DISTINCT specialization FROM doctor WHERE status = 'Active'";
        Connection conn=DBConnection.getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                specializations.add(rs.getString("specialization"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return specializations;
    }

    public List<String> getDoctorsBySpecialization(String specialization) {
        List<String> doctors = new ArrayList<>();
        String query = "SELECT full_name FROM doctor WHERE specialization = ? AND status = 'Active'";
        Connection conn=DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, specialization);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                doctors.add(rs.getString("full_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }
    public boolean doesPatientExist(int id) {
        String sql = "SELECT 1 FROM clinic_visit_optimizer.patient WHERE Pid = ?";
        Connection conn= DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // returns true if a row exists
        } catch (SQLException e) {
            System.out.println("\u001B[31mDatabase : "+e.getMessage()+"\u001B[0m");
        }
        return false;
    }

    public List<Patient> getPatientsByContact(long contact) {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT * FROM clinic_visit_optimizer.patient WHERE Pcontact = ?";
        Connection conn= DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, contact);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Patient p = new Patient();
                p.setId(rs.getInt("Pid"));
                p.setName(rs.getString("Pname"));
                p.setAge(rs.getInt("Page"));
                p.setGender(rs.getString("Pgender"));
                p.setBlood(rs.getString("Pblood_group"));
                p.setContact(rs.getLong("Pcontact"));
                p.setAddress(rs.getString("Paddress"));
                p.setSpecialization(rs.getString("specialization"));
                p.setDoctor(rs.getString("doctor"));
                p.setRegistrationDate(rs.getTimestamp("registration_date"));
                p.setAppointmentDate(rs.getDate("appointment_date"));
                list.add(p);
            }
        } catch (Exception e) {
            System.out.println("\u001B[31mDatabase : "+e.getMessage()+"\u001B[0m");
        }
        return list;
    }

}
