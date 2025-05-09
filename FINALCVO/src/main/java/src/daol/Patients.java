package src.daol;



import src.connection.DBConnection;
import src.pojo.Appointment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Patients {
    public static List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        try {
            String sql = "SELECT appointment_id, patient_id, doctor_id, appointment_date, appointment_time, status, created_at FROM clinic_visit_optimizer.appointment";
            Connection conn= DBConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();
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
            System.out.println("\u001B[31mDatabase : "+e.getMessage()+"\u001B[0m");
        }
        return appointments;
    }


    public static void viewAppointments() {
        Scanner scanner = new Scanner(System.in);
        List<Appointment> list=getAllAppointments();
        System.out.println("\033[38;2;255;165;0mEnter Your Choice : \u001B[0m");
        while (true) {
            List<Appointment> filteredList = new ArrayList<>();

            // Display menu options
            System.out.println("Enter 1 For Particular Date");
            System.out.println("Enter 2 For Particular Month");
            System.out.println("Enter 3 For Today");
            System.out.println("Enter 4 For This Month");
            System.out.println("Enter 5 To Go Back");

            int choice;
            while (true) {

                try {
                    choice = Integer.parseInt(scanner.nextLine());
                    if (choice >= 1 && choice <= 5) {
                        break;
                    } else {
                        System.out.println("\u001B[31mEnter a Valid Choice :\u001B[0m");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("\u001B[31mEnter a Valid Choice :\u001B[0m");
                }
            }

            if (choice == 5) {
              return;
            }

            switch (choice) {
                case 1: // Daily (manual)
                    LocalDate targetDate = null;
                    System.out.print("\u001B[32mEnter Date (yyyy-MM-dd): \u001B[0m");
                    while (true) {
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
                    int month, year;
                    System.out.print("\u001B[31mEnter Month (1-12): \u001B[0m");
                    while (true) {
                        try {
                            month = Integer.parseInt(scanner.nextLine());
                            if (month >= 1 && month <= 12) {
                                break;
                            } else {
                                System.out.println("\u001B[31mInvalid format. Month must be between 1 and 12 : \u001B[0m");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("\u001B[31mInvalid number. Please enter a valid month.\u001B[0m");
                        }
                    }

                    while (true) {
                        System.out.print("\033[38;2;255;165;0mEnter Year (e.g., 2025): \u001B[0m");
                        try {
                            year = Integer.parseInt(scanner.nextLine());
                            if (year >= 1900 && year <= 2100) {
                                break;
                            } else {
                                System.out.println("\u001B[31mEnter a Valid Year : \u001B[0m");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("\u001B[31mEnter a Valid Year : \u001B[0m");
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
}