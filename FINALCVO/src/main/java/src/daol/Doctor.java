package src.daol;



import src.connection.DBConnection;
import src.pojo.Pojo;
import src.service.Admin;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Doctor {


    public static void doctorsPanel() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\033[38;2;255;165;0mEnter Your Choice : \u001B[0m");
            System.out.println("Enter 1. To Display All Doctors" +
                    "\nEnter 2. To Display Particular Doctor" +
                    "\nEnter 3. To Insert Doctor" +
                    "\nEnter 4. To Update Doctor" +
                    "\nEnter 5. To Delete Doctor" +
                    "\nEnter 6. To Go Back");

            String input = sc.nextLine();
            if (!input.matches("\\d+")) {
                System.out.println("\u001B[31mEnter a Valid Choice :\u001B[0m");
                continue; // Keeps user in the loop
            }

            int choice = Integer.parseInt(input);
            switch (choice) {
                case 1:
                    displayAllDoctors();
                    break;
                case 2:
                    while (true) {
                        System.out.print("\033[38;2;255;165;0mEnter Doctor Id : \u001B[0m");
                        String docInput = sc.nextLine();
                        if (docInput.matches("\\d+")) {
                            int doctor_id = Integer.parseInt(docInput);
                            if (doctor_id != 0) {
                                displayDoctor(doctor_id);
                                break;
                            } else {
                                System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                            }
                        } else {
                            System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                        }
                    }
                    break;
                case 3:
                    insertDoctor();
                    break;
                case 4:
                    updateDocValidation();
                    break;
                case 5:
                    deleteDoctor();
                    break;
                case 6:
                    Admin.adminPanel(); // returns to main panel
                    return; // exit current method
                default:
                    System.out.println("\u001B[31mEnter a Valid Choice :\u001B[0m");
            }
        }
    }



    public static void displayAllDoctors() {
        try {
            String query = "Select * from clinic_visit_optimizer.doctor";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println();
                System.out.println("Id             : " + rs.getInt(1));
                System.out.println("Name           : " + rs.getString(2));
                System.out.println("Password       : " + rs.getString(3));
                System.out.println("Specialization : " + rs.getString(4));
                System.out.println("Contact Number : " + rs.getString(5));
                System.out.println("Email          : " + rs.getString(6));
                System.out.println("Qualification  : " + rs.getString(7));
                System.out.println("Experience     : " + rs.getInt(8));
                System.out.println("Working Hours  : " + rs.getString(9));
                System.out.println("Salary         : " + rs.getString(10));
                System.out.println("Status         : " + rs.getString(11));
                System.out.println();
                System.out.println("==================================================");
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31mDatabase :"+ e.getMessage()+" \u001B[0m");
        }
    }

    public static void displayDoctor(int doctor_id) {
        try {
            String query = "Select * from clinic_visit_optimizer.doctor where doctor_id=?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, doctor_id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                System.out.println();
                System.out.println("Id             : " + rs.getInt(1));
                System.out.println("Name           : " + rs.getString(2));
                System.out.println("Password       : " + rs.getString(3));
                System.out.println("Specialization : " + rs.getString(4));
                System.out.println("Contact Number : " + rs.getString(5));
                System.out.println("Email          : " + rs.getString(6));
                System.out.println("Qualification  : " + rs.getString(7));
                System.out.println("Experience     : " + rs.getInt(8));
                System.out.println("Working Hours  : " + rs.getString(9));
                System.out.println("Salary         : " + rs.getString(10));
                System.out.println("Status         : " + rs.getString(11));
                System.out.println();
            }
            else
                throw new SQLException("Doctor Id "+doctor_id+" not found");
        } catch (SQLException e) {
            System.out.println("\u001B[31mDatabase : "+e.getMessage()+"\u001B[0m");
        }
    }

    public static void insertDoctor() {
        try {
            Scanner sc = new Scanner(System.in);
            String query = "insert into clinic_visit_optimizer.doctor (full_name,password, specialization, contact_number, email, qualification, experience_years, working_hours,salary,Status)\n" +
                    "VALUES (?,?,?,?,?,?,?,?,?,?);";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            System.out.println("\033[38;2;255;165;0mEnter Name : \u001B[0m");
            while (true) {
                String name = sc.nextLine();
                if (name.matches(".[!@#$%^&*0-9].")) {
                    System.out.println("\u001B[31mEnter a Valid Name : \u001B[0m");
                } else if (name.trim().isEmpty()) {
                    System.out.println("\u001B[31mName Cannot be Empty !\u001B[0m");
                    System.out.println("\u001B[31mEnter a Valid Name : \u001B[0m");
                } else {
                    ps.setString(1, Pojo.setName(name));
                    break;
                }
            }
            System.out.println("\033[38;2;255;165;0mEnter Password : \u001B[0m");
            while (true) {
                String password = sc.nextLine();
                if (password.trim().isEmpty()) {
                    System.out.println("\u001B[31mPassword Cannot be Empty !\u001B[0m");
                    System.out.println("\u001B[31mEnter a Valid Password :\u001B[0m");
                }else if (!password.matches(".{0,5}")) {  // 0 to 5 of any character
                    System.out.println("\u001B[31mInvalid length ! Max 5 Charcters\u001B[0m");
                }
                else {
                    ps.setString(2, Pojo.setPassword(password));
                    break;
                }
            }
            System.out.println("\033[38;2;255;165;0mEnter Your Choice : \u001B[0m");
            String specialization;
            while (true) {
                try {
                    System.out.println("Enter 1. For General Medicine" +
                            "\nEnter 2. For Pediatrics" +
                            "\nEnter 3. For Dermatology" +
                            "\nEnter 4. ENT");
                    int choice = sc.nextInt();
                    sc.nextLine();
                    if (choice == 1) {
                        specialization = "General Medicine";
                        break;
                    } else if (choice == 2) {
                        specialization = "Pediatrics";
                        break;
                    } else if (choice == 3) {
                        specialization = "Dermatology";
                        break;
                    } else if (choice == 4) {
                        specialization = "ENT";
                        break;
                    } else
                        System.out.println("\u001B[31mEnter a Valid Choice :\u001B[0m");
                } catch (InputMismatchException e) {
                    System.out.println("\u001B[31mEnter a Valid Choice :\u001B[0m");
                    sc.nextLine();
                }
            }
            ps.setString(3, Pojo.setSpecialization(specialization));
            System.out.println("\033[38;2;255;165;0mEnter Contact Number : \u001B[0m");
            while (true) {
                String number = sc.nextLine();
                if (number.isEmpty()) {
                    System.out.println("\u001B[31mNumber Cannot be Empty !\u001B[0m");
                    System.out.println("\u001B[31mEnter a Valid Contact Number :\u001B[0m");
                } else if (!number.matches("\\d{10}")) {
                    System.out.println("\u001B[31mInvalid input! Only 10 Numbers Allowed :\u001B[0m");
                } else {
                    ps.setString(4, Pojo.setContact_Number(number));
                    break;
                }
            }
            System.out.println("\033[38;2;255;165;0mEnter Email : \u001B[0m");
            while (true) {
                String email = sc.nextLine();
                if (email.isEmpty()) {
                    System.out.println("\u001B[31mEmail Cannot be Empty !\u001B[0m");
                    System.out.println("\u001B[31mEmail a Valid Email :\u001B[0m");
                } else if (!email.matches("^[\\w.-]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    System.out.println("\u001B[31mInvalid email format! Example: user@example.com\u001B[0m");
                } else {
                    ps.setString(5, Pojo.setEmail(email));
                    break;
                }
            }
            System.out.println("\033[38;2;255;165;0mEnter Qualification : \u001B[0m");
            while (true) {
                String qualification = sc.nextLine();
                if (qualification.trim().isEmpty()) {
                    System.out.println("\u001B[31mQualification Cannot be Empty !\u001B[0m");
                    System.out.println("\u001B[31mEnter a Valid Qualification : \u001B[0m");
                } else if (qualification.matches(".[!@#$%^&*0-9].")) {
                    System.out.println("\u001B[31mEnter a Valid Name : \u001B[0m");
                } else {
                    ps.setString(6, Pojo.setQualification(qualification));
                    break;
                }
            }
            System.out.println("\033[38;2;255;165;0mEnter Experience : \u001B[0m");
            while (true) {
                try {
                    int experience = sc.nextInt();
                    sc.nextLine();
                    ps.setInt(7, Pojo.setExperience(experience));
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("\u001B[31mEnter a Valid Experience Year : \u001B[0m");
                    sc.nextLine();
                }
            }
            System.out.println("\033[38;2;255;165;0mEnter Your Choice : \u001B[0m:");
            String working_Hours;
            while (true) {
                try {
                    System.out.println("Enter 1. Morning Shift" +
                            "\nEnter 2. For Afternoon Shift");
                    int choice = sc.nextInt();
                    sc.nextLine();
                    if (choice == 1) {
                        working_Hours = "9:00 AM To 1:00 PM";
                        ps.setString(8, Pojo.setWorking_Hours(working_Hours));
                        break;
                    } else if (choice == 2) {
                        working_Hours = "1:00 PM To 5:00 PM";
                        ps.setString(8, Pojo.setWorking_Hours(working_Hours));
                        break;
                    } else
                        System.out.println("\u001B[31mEnter a Valid Choice :\u001B[0m");
                } catch (InputMismatchException e) {
                    System.out.println("\u001B[31mEnter a Valid Choice :\u001B[0m");
                    sc.nextLine();
                }
            }
            System.out.println("\033[38;2;255;165;0mEnter Salary : \u001B[0m");
            String sal;
            while (true) {
                sal = sc.nextLine();
                if (!sal.matches("^[0-9]+$")) {
                    System.out.println("\u001B[31mEnter a Valid Salary : \u001B[0m");
                } else {
                    break;
                }
            }
            ps.setString(9, Pojo.setSalary(sal));
            System.out.println("\033[38;2;255;165;0mEnter Your Choice : \u001B[0m");
            while (true) {
                try {
                    System.out.println("Enter 1 For Online" +
                            "\nEnter 2 for Offline");
                    int choice = sc.nextInt();
                    String status;
                    if (choice == 1) {
                        status = "Online";
                        ps.setString(10, Pojo.setIs_active(status));
                        break;
                    } else if (choice == 2) {
                        status = "Offline";
                        ps.setString(10, Pojo.setIs_active(status));
                        break;
                    } else
                        System.out.println("\u001B[31mEnter a Valid Choice : \u001B[0m");
                } catch (InputMismatchException e) {
                    System.out.println("\u001B[31mEnter a Valid Choice : \u001B[0m");
                    sc.nextLine();
                }
            }
            ps.execute();
            System.out.println("\033[32mInserted Data Successfully\033[0m");
            Thread.sleep(1000);
        } catch (SQLException | InterruptedException e) {
            System.out.println("\u001B[31mDatabase : "+e.getMessage()+"\u001B[0m");
        }
    }

    public static void updateDocValidation() {
        Scanner sc = new Scanner(System.in);
        boolean flag = false;
        System.out.println("\033[38;2;255;165;0mEnter Your Choice : \u001B[0m");
        while (!flag) {
            System.out.println("Enter 1 for Updating Name" +
                    "\nEnter 2 for Updating Password" +
                    "\nEnter 3 for Updating Specialization" +
                    "\nEnter 4 for Updating Contact Number" +
                    "\nEnter 5 for Updating Email" +
                    "\nEnter 6 for Updating Qualification" +
                    "\nEnter 7 for Updating Experience" +
                    "\nEnter 8 for Updating Working Hours" +
                    "\nEnter 9 for Updating Salary" +
                    "\nEnter 10 for Updating Status" +
                    "\nEnter 11 for Go Back");
            try {
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        updateDocName();
                        updateDocValidation();
                        flag = true;
                        break;
                    case 2:
                        updateDocPassword();
                        updateDocValidation();
                        flag = true;
                        break;
                    case 3:
                        updateDocSpecialization();
                        updateDocValidation();
                        flag = true;
                        break;
                    case 4:
                        updateDocContactNumber();
                        updateDocValidation();
                        flag = true;
                        break;
                    case 5:
                        updateDocEmail();
                        updateDocValidation();
                        flag = true;
                        break;
                    case 6:
                        updateDocQualification();
                        updateDocValidation();
                        flag = true;
                        break;
                    case 7:
                        updateDocExperience();
                        updateDocValidation();
                        flag = true;
                        break;
                    case 8:
                        updateDocWorkingHours();
                        updateDocValidation();
                        flag = true;
                        break;
                    case 9:
                        updateDocSalary();
                        updateDocValidation();
                        flag = true;
                        break;
                    case 10:
                        updateDocStatus();
                        updateDocValidation();
                        flag = true;
                        break;
                    case 11:
                        doctorsPanel();
                        return;
                    default:
                        System.out.println("\u001B[31mEnter a Valid Choice :\u001B[0m");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("\u001B[31mEnter a Valid Choice :\u001B[0m");
                sc.nextLine();
            }
        }

    }

    public static ResultSet selectID() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = "SELECT doctor_id FROM clinic_visit_optimizer.doctor";
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            return rs; // Return the ResultSet immediately (don't consume it here)
        } catch (SQLException e) {
            // Close resources if error occurs
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("\033[32mFailed to fetch doctor IDs\033[0m\"", e);
        }
        // Note: We don't close resources here because caller needs the ResultSet
    }
    public static void updateDocName() {
        Scanner sc = new Scanner(System.in);
        try {
            String query = "update clinic_visit_optimizer.doctor set full_name=? where doctor_id=?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            System.out.println("\033[38;2;255;165;0mEnter Doctor Id : \u001B[0m");
            int id;
            while (true) {
                try {
                    id = sc.nextInt();
                    sc.nextLine();
                    if (id != 0) {
                        break;
                    } else {
                        System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    sc.nextLine();
                }
            }
            try {
                ResultSet rs = selectID();
                boolean idFound = false;

                try {
                    while (rs.next()) {
                        if (rs.getInt("doctor_id") == id) {
                            idFound = true;
                            break;
                        }
                    }

                    if (!idFound) {
                        System.out.println("\u001B[31mDoctor ID: " + id + " not found\u001B[31m");
                        return;
                    }
                } finally {
                    // Always close ResultSet and Statement when done
                    try {
                        if (rs != null) {
                            Statement stmt = rs.getStatement();
                            rs.close();
                            if (stmt != null) stmt.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                // Continue with your logic for when ID exists...

            } catch (RuntimeException e) {
                System.err.println("\033[32mError accessing database: " + e.getMessage()+"\033[0m");
                return;
            }
            System.out.println("\033[38;2;255;165;0mEnter Doctor Name : \u001B[0m");
            String name;
            while (true) {
                name = sc.nextLine();
                if (name.matches(" .[!@#$%^&*0-9].")) {
                    System.out.println("\u001B[31mEnter a Valid Name : \u001B[0m");
                } else if (name.trim().isEmpty()) {
                    System.out.println("\u001B[31mName Cannot be Empty !\u001B[0m");
                    System.out.println("\u001B[31mEnter a Valid Name : \u001B[0m");
                } else {
                    break;
                }
            }
            ps.setString(1, name);
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println();
            Thread.sleep(1000);
            displayDoctor(id);
        } catch (SQLException | InterruptedException e) {
            System.out.println("\u001B[31mDatabase : "+e.getMessage()+"\u001B[0m");
        }
        System.out.println();
    }

    public static void updateDocPassword() {
        Scanner sc = new Scanner(System.in);
        try {
            String query = "update clinic_visit_optimizer.doctor set password=? where doctor_id=?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            System.out.println("\033[38;2;255;165;0mEnter Doctor Id : \u001B[0m");
            int id;
            while (true) {
                try {
                    id = sc.nextInt();
                    sc.nextLine();
                    if (id != 0) {
                        break;
                    } else {
                        System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    sc.nextLine();
                }
            }
            try {
                ResultSet rs = selectID();
                boolean idFound = false;

                try {
                    while (rs.next()) {
                        if (rs.getInt("doctor_id") == id) {
                            idFound = true;
                            break;
                        }
                    }

                    if (!idFound) {
                        System.out.println("\u001B[31mDoctor ID: " + id + " not found\u001B[31m");
                        return;
                    }
                } finally {
                    // Always close ResultSet and Statement when done
                    try {
                        if (rs != null) {
                            Statement stmt = rs.getStatement();
                            rs.close();
                            if (stmt != null) stmt.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                // Continue with your logic for when ID exists...

            } catch (RuntimeException e) {
                System.err.println("\033[32mError accessing database: " + e.getMessage()+"\033[0m");
                return;
            }
            System.out.println("\033[38;2;255;165;0mEnter Doctor Password : \u001B[0m");
            String password;
            while (true) {
                password = sc.nextLine();
                if (password == null) {
                    System.out.println("\u001B[31mPassword Cannot be Empty !\u001B[0m");
                    System.out.println("\u001B[31mEnter a Valid Password Number :\u001B[0m");
                } else {
                    break;
                }
            }
            ps.setString(1, password);
            ps.setInt(2, id);
            System.out.println();
            ps.executeUpdate();
            System.out.println("\033[32mUpdated Password Successfully\033[0m");
            System.out.println();
            Thread.sleep(1000);
            displayDoctor(id);
        } catch (SQLException | InterruptedException e) {
            System.out.println("\u001B[31mDatabase : "+e.getMessage()+"\u001B[0m");
        }
        System.out.println();
    }

    public static void updateDocSpecialization() {
        Scanner sc = new Scanner(System.in);
        try {
            String query = "update clinic_visit_optimizer.doctor set specialization=? where doctor_id=?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            System.out.println("\033[38;2;255;165;0mEnter Doctor Id : \u001B[0m");
            int id;
            while (true) {
                try {
                    id = sc.nextInt();
                    sc.nextLine();
                    if (id != 0) {
                        break;
                    } else {
                        System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    sc.nextLine();
                }
            }
            try {
                ResultSet rs = selectID();
                boolean idFound = false;

                try {
                    while (rs.next()) {
                        if (rs.getInt("doctor_id") == id) {
                            idFound = true;
                            break;
                        }
                    }

                    if (!idFound) {
                        System.out.println("\u001B[31mDoctor ID: " + id + " not found\u001B[31m");
                        return;
                    }
                } finally {
                    // Always close ResultSet and Statement when done
                    try {
                        if (rs != null) {
                            Statement stmt = rs.getStatement();
                            rs.close();
                            if (stmt != null) stmt.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                // Continue with your logic for when ID exists...

            } catch (RuntimeException e) {
                System.err.println("\033[32mError accessing database: " + e.getMessage()+"\033[0m");
                return;
            }
            System.out.println("\033[38;2;255;165;0mEnter Your Choice \u001B[0m");
            String specialization;
            while (true) {
                try {
                    System.out.println("Enter 1. For General Medicine" +
                            "\nEnter 2. For Pediatrics" +
                            "\nEnter 3. For Dermatology" +
                            "\nEnter 4. ENT");
                    int choice = sc.nextInt();
                    sc.nextLine();
                    if (choice == 1) {
                        specialization = "General Medicine";
                        break;
                    } else if (choice == 2) {
                        specialization = "Pediatrics";
                        break;
                    } else if (choice == 3) {
                        specialization = "Dermatology";
                        break;
                    } else if (choice == 4) {
                        specialization = "ENT";
                        break;
                    } else
                        System.out.println("\u001B[31mEnter a Valid Choice :\u001B[0m");
                } catch (InputMismatchException e) {
                    System.out.println("\u001B[31mEnter a Valid Choice :\u001B[0m");
                    sc.nextLine();
                }
            }
            ps.setString(1, specialization);
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("\033[32mUpdated Specialization Successfully\033[0m");
            System.out.println();
            Thread.sleep(1000);
            displayDoctor(id);
        } catch (SQLException | InterruptedException e) {
            System.out.println("\u001B[31mDatabase : "+e.getMessage()+"\u001B[0m");
        }
        System.out.println();
    }

    public static void updateDocContactNumber() {
        Scanner sc = new Scanner(System.in);
        try {
            String query = "update clinic_visit_optimizer.doctor set contact_number=? where doctor_id=?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            System.out.println("\033[38;2;255;165;0mEnter Doctor Id : \u001B[0m");
            int id;
            while (true) {
                try {
                    id = sc.nextInt();
                    sc.nextLine();
                    if (id != 0) {
                        break;
                    } else {
                        System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    sc.nextLine();
                }
            }
            try {
                ResultSet rs = selectID();
                boolean idFound = false;

                try {
                    while (rs.next()) {
                        if (rs.getInt("doctor_id") == id) {
                            idFound = true;
                            break;
                        }
                    }

                    if (!idFound) {
                        System.out.println("\u001B[31mDoctor ID: " + id + " not found\u001B[31m");
                        return;
                    }
                } finally {
                    // Always close ResultSet and Statement when done
                    try {
                        if (rs != null) {
                            Statement stmt = rs.getStatement();
                            rs.close();
                            if (stmt != null) stmt.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                // Continue with your logic for when ID exists...

            } catch (RuntimeException e) {
                System.err.println("\033[32mError accessing database: " + e.getMessage()+"\033[0m");
                return;
            }
            System.out.println("\033[38;2;255;165;0mEnter Doctor Contact Number : \u001B[0m");
            String number;
            while (true) {
                number = sc.nextLine();
                if (number.isEmpty()) {
                    System.out.println("\u001B[31mNumber Cannot be Empty !\u001B[0m");
                    System.out.println("\u001B[31mEnter a Valid Contact Number :\u001B[0m");
                } else if (!number.matches("\\d{10}")) {
                    System.out.println("\u001B[31mInvalid input! Only Numbers Allowed :\u001B[0m");
                } else {
                    break;
                }
            }
            ps.setString(1, number);
            ps.setInt(2, id);
            System.out.println();
            Thread.sleep(1000);
            ps.executeUpdate();
            System.out.println("\033[32mUpdated Contact Number Successfully\033[0m");
            System.out.println();
            Thread.sleep(1000);
            displayDoctor(id);
        } catch (SQLException | InterruptedException e) {
            System.out.println("\u001B[31mCan't Update Data !\u001B[0m");
        }
        System.out.println();
    }

    public static void updateDocEmail() {
        Scanner sc = new Scanner(System.in);
        try {
            String query = "update clinic_visit_optimizer.doctor set email=? where doctor_id=?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            System.out.println("\033[38;2;255;165;0mEnter Doctor Id : \u001B[0m");
            int id;
            while (true) {
                try {
                    id = sc.nextInt();
                    sc.nextLine();
                    if (id != 0) {
                        break;
                    } else {
                        System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    sc.nextLine();
                }
            }
            try {
                ResultSet rs = selectID();
                boolean idFound = false;

                try {
                    while (rs.next()) {
                        if (rs.getInt("doctor_id") == id) {
                            idFound = true;
                            break;
                        }
                    }

                    if (!idFound) {
                        System.out.println("\u001B[31mDoctor ID: " + id + " not found\u001B[31m");
                        return;
                    }
                } finally {
                    // Always close ResultSet and Statement when done
                    try {
                        if (rs != null) {
                            Statement stmt = rs.getStatement();
                            rs.close();
                            if (stmt != null) stmt.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                // Continue with your logic for when ID exists...

            } catch (RuntimeException e) {
                System.err.println("\033[32mError accessing database: " + e.getMessage()+"\033[0m");
                return;
            }
            System.out.println("\033[38;2;255;165;0mEnter Doctor Email : \u001B[0m");
            String email;
            while (true) {
                email = sc.nextLine();
                if (email.isEmpty()) {
                    System.out.println("\u001B[31mEmail Cannot be Empty !\u001B[0m");
                    System.out.println("\u001B[31mEmail a Valid Email :\u001B[0m");
                } else if (!email.matches("^[\\w.-]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    System.out.println("\u001B[31mInvalid email format! Example: user@example.com\u001B[0m");
                } else {
                    break;
                }
            }
            ps.setString(1, email);
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("\033[32mUpdated Email Successfully\033[0m");
            System.out.println();
            Thread.sleep(1000);
            displayDoctor(id);
        } catch (SQLException | InterruptedException e) {
            System.out.println("\u001B[31mCan't Update Data !\u001B[0m");
        }
        System.out.println();
    }

    public static void updateDocQualification() {
        Scanner sc = new Scanner(System.in);
        try {
            String query = "update clinic_visit_optimizer.doctor set qualification=? where doctor_id=?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            System.out.println("\033[38;2;255;165;0mEnter Doctor Id : \u001B[0m");
            int id;
            while (true) {
                try {
                    id = sc.nextInt();
                    sc.nextLine();
                    if (id != 0) {
                        break;
                    } else {
                        System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    sc.nextLine();
                }
            }
            try {
                ResultSet rs = selectID();
                boolean idFound = false;

                try {
                    while (rs.next()) {
                        if (rs.getInt("doctor_id") == id) {
                            idFound = true;
                            break;
                        }
                    }

                    if (!idFound) {
                        System.out.println("\u001B[31mDoctor ID: " + id + " not found\u001B[31m");
                        return;
                    }
                } finally {
                    // Always close ResultSet and Statement when done
                    try {
                        if (rs != null) {
                            Statement stmt = rs.getStatement();
                            rs.close();
                            if (stmt != null) stmt.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                // Continue with your logic for when ID exists...

            } catch (RuntimeException e) {
                System.err.println("\033[32mError accessing database: " + e.getMessage()+"\033[0m");
                return;
            }
            System.out.println("\033[38;2;255;165;0mEnter Doctor Quallification : \u001B[0m");
            String qualification;
            while (true) {
                qualification = sc.nextLine();
                if (qualification.matches(".[!@#$%^&*0-9].")) {
                    System.out.println("\u001B[31mEnter a Valid Name : \u001B[0m");
                } else if (qualification.trim().isEmpty()) {
                    System.out.println("\u001B[31mName Cannot be Empty !\u001B[0m");
                    System.out.println("\u001B[31mEnter a Valid Name : \u001B[0m");
                } else {
                    break;
                }
            }
            ps.setString(1, qualification);
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("\033[32mUpdated Qualification Successfully\033[0m");
            System.out.println();
            Thread.sleep(1000);
            displayDoctor(id);
        } catch (SQLException | InterruptedException e) {
            System.out.println("\u001B[31mCan't Update Data !\u001B[0m");
        }
        System.out.println();
    }

    public static void updateDocExperience() {
        Scanner sc = new Scanner(System.in);
        try {
            String query = "update clinic_visit_optimizer.doctor set experience_years=? where doctor_id=?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            System.out.println("\033[38;2;255;165;0mEnter Doctor Id : \u001B[0m");
            int id;
            while (true) {
                try {
                    id = sc.nextInt();
                    sc.nextLine();
                    if (id != 0) {
                        break;
                    } else {
                        System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    sc.nextLine();
                }
            }
            try {
                ResultSet rs = selectID();
                boolean idFound = false;

                try {
                    while (rs.next()) {
                        if (rs.getInt("doctor_id") == id) {
                            idFound = true;
                            break;
                        }
                    }

                    if (!idFound) {
                        System.out.println("\u001B[31mDoctor ID: " + id + " not found\u001B[31m");
                        return;
                    }
                } finally {
                    // Always close ResultSet and Statement when done
                    try {
                        if (rs != null) {
                            Statement stmt = rs.getStatement();
                            rs.close();
                            if (stmt != null) stmt.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                // Continue with your logic for when ID exists...

            } catch (RuntimeException e) {
                System.err.println("\033[32mError accessing database: " + e.getMessage()+"\033[0m");
                return;
            }
            System.out.println("\033[38;2;255;165;0mEnter Doctor Experience : \u001B[0m");
            int experience;
            while (true) {
                try {
                    experience = sc.nextInt();
                    sc.nextLine();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("\u001B[31mEnter a Valid Experience Year : \u001B[0m");
                    sc.nextLine();
                }
            }
            ps.setInt(1, experience);
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("\033[32mUpdated Experience Successfully\033[0m");
            System.out.println();
            Thread.sleep(1000);
            displayDoctor(id);
        } catch (SQLException | InterruptedException e) {
            System.out.println("\u001B[31mCan't Update Data !\u001B[0m");
        }
        System.out.println();
    }

    public static void updateDocWorkingHours() {
        Scanner sc = new Scanner(System.in);
        try {
            String query = "update clinic_visit_optimizer.doctor set working_hours=? where doctor_id=?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            System.out.println("\033[38;2;255;165;0mEnter Doctor Id : \u001B[0m");
            int id;
            while (true) {
                try {
                    id = sc.nextInt();
                    sc.nextLine();
                    if (id != 0) {
                        break;
                    } else {
                        System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    sc.nextLine();
                }
            }
            try {
                ResultSet rs = selectID();
                boolean idFound = false;

                try {
                    while (rs.next()) {
                        if (rs.getInt("doctor_id") == id) {
                            idFound = true;
                            break;
                        }
                    }

                    if (!idFound) {
                        System.out.println("\u001B[31mDoctor ID: " + id + " not found\u001B[31m");
                        return;
                    }
                } finally {
                    // Always close ResultSet and Statement when done
                    try {
                        if (rs != null) {
                            Statement stmt = rs.getStatement();
                            rs.close();
                            if (stmt != null) stmt.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                // Continue with your logic for when ID exists...

            } catch (RuntimeException e) {
                System.err.println("\033[32mError accessing database: " + e.getMessage()+"\033[0m");
                return;
            }
            System.out.println("\033[38;2;255;165;0mEnter Your Choice : \u001B[0m");
            String working_Hours;
            while (true) {
                try {
                    System.out.println("Enter 1. Morning Shift" +
                            "\nEnter 2. For Afternoon Shift");
                    int choice = sc.nextInt();
                    if (choice == 1) {
                        working_Hours = "9:00 AM To 1:00 PM";

                        break;
                    } else if (choice == 2) {
                        working_Hours = "1:00 PM To 5:00 PM";
                        break;
                    } else
                        System.out.println("\u001B[31mEnter a Valid Choice :\u001B[0m");
                } catch (InputMismatchException e) {
                    System.out.println("\u001B[31mEnter a Valid Choice :\u001B[0m");
                    sc.nextLine();
                }
            }
            ps.setString(1, Pojo.setWorking_Hours(working_Hours));
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("\033[32mUpdated Working Hours Successfully\033[0m");
            System.out.println();
            Thread.sleep(1000);
            displayDoctor(id);
        } catch (SQLException | InterruptedException e) {
            System.out.println("\u001B[31mCan't Update Data !\u001B[0m");
        }
        System.out.println();
    }

    public static void updateDocSalary() {
        Scanner sc = new Scanner(System.in);
        try {
            String query = "update clinic_visit_optimizer.doctor set salary=? where doctor_id=?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            System.out.println("\033[38;2;255;165;0mEnter Doctor Id : \u001B[0m");
            int id;
            while (true) {
                try {
                    id = sc.nextInt();
                    sc.nextLine();
                    if (id != 0) {
                        break;
                    } else {
                        System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    sc.nextLine();
                }
            }
            try {
                ResultSet rs = selectID();
                boolean idFound = false;

                try {
                    while (rs.next()) {
                        if (rs.getInt("doctor_id") == id) {
                            idFound = true;
                            break;
                        }
                    }

                    if (!idFound) {
                        System.out.println("\u001B[31mDoctor ID: " + id + " not found\u001B[31m");
                        return;
                    }
                } finally {
                    // Always close ResultSet and Statement when done
                    try {
                        if (rs != null) {
                            Statement stmt = rs.getStatement();
                            rs.close();
                            if (stmt != null) stmt.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                // Continue with your logic for when ID exists...

            } catch (RuntimeException e) {
                System.err.println("\033[32mError accessing database: " + e.getMessage()+"\033[0m");
                return;
            }
            System.out.println("\033[38;2;255;165;0mEnter Doctor Salary : \u001B[0m");
            String sal;
            while (true) {
                sal = sc.nextLine();
                if (!sal.matches("^[0-9]+$")) {
                    System.out.println("\u001B[31mEnter a Valid Salary\u001B[0m");
                } else {
                    break;
                }
            }
            ps.setString(1, Pojo.setSalary(sal));
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("\033[32mUpdated Salary Successfully\033[0m");
            System.out.println();
            Thread.sleep(1000);
            displayDoctor(id);

        }
        catch (SQLException | InterruptedException e) {
            System.out.println("\u001B[31mCan't Update Salary !\u001B[0m");
        }
    }


    private static void updateDocStatus() {
        Scanner sc = new Scanner(System.in);
        try {
            String query = "update clinic_visit_optimizer.doctor set Status=? where doctor_id=?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            System.out.println("\033[38;2;255;165;0mEnter Doctor Id : \u001B[0m");
            int id;
            while (true) {
                try {
                    id = sc.nextInt();
                    sc.nextLine();
                    if (id != 0) {
                        break;
                    } else {
                        System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    sc.nextLine();
                }
            }
            try {
                ResultSet rs = selectID();
                boolean idFound = false;

                try {
                    while (rs.next()) {
                        if (rs.getInt("doctor_id") == id) {
                            idFound = true;
                            break;
                        }
                    }

                    if (!idFound) {
                        System.out.println("\u001B[31mDoctor ID: " + id + " not found\u001B[31m");
                        return;
                    }
                } finally {
                    // Always close ResultSet and Statement when done
                    try {
                        if (rs != null) {
                            Statement stmt = rs.getStatement();
                            rs.close();
                            if (stmt != null) stmt.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                // Continue with your logic for when ID exists...

            } catch (RuntimeException e) {
                System.err.println("\033[32mError accessing database: " + e.getMessage()+"\033[0m");
                return;
            }
            System.out.println("\033[38;2;255;165;0mEnter Your Choice : \u001B[0m");
            while (true) {
                try {
                    System.out.println("Enter 1 For Online" +
                            "\nEnter 2 for Offline");
                    int choice = sc.nextInt();
                    String status;
                    if (choice == 1) {
                        status = "Online";
                        ps.setString(1, Pojo.setIs_active(status));
                        break;
                    } else if (choice == 2) {
                        status = "Offline";
                        ps.setString(1, Pojo.setIs_active(status));
                        break;
                    } else
                        System.out.println("\u001B[31mEnter a Valid Choice : \u001B[0m");
                } catch (InputMismatchException e) {
                    System.out.println("\u001B[31mEnter a Valid Choice : \u001B[0m");
                    sc.nextLine();
                }
            }
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("\033[32mUpdated Status Successfully\033[0m");
            System.out.println();
            Thread.sleep(1000);
            displayDoctor(id);
        }
        catch (SQLException | InterruptedException e){
            System.out.println("\u001B[31mCan't Update Status !\u001B[0m");
        }
        System.out.println();
    }

    public static void deleteDoctor() {
        Scanner sc = new Scanner(System.in);
        try {
            Connection conn = DBConnection.getConnection();

            // Step 1: Get valid ID input
            System.out.println("\033[38;2;255;165;0mEnter Doctor Id : \u001B[0m");
            int id;
            while (true) {
                try {
                    id = sc.nextInt();
                    if (id != 0) {
                        break;
                    } else {
                        System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    sc.nextLine();
                }
            }

            // Step 2: Check if doctor exists
            PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT * FROM clinic_visit_optimizer.doctor WHERE doctor_id = ?");
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                System.out.println("\u001B[31mDoctor ID: " + id + " not found\u001B[0m");
                return;
            }
            rs.close();
            checkStmt.close();

            // Step 3: Delete related appointments first
            PreparedStatement deleteAppointments = conn.prepareStatement(
                    "DELETE FROM clinic_visit_optimizer.appointment WHERE doctor_id = ?");
            deleteAppointments.setInt(1, id);
            deleteAppointments.executeUpdate();
            deleteAppointments.close();

            // Step 4: Delete doctor
            PreparedStatement deleteDoctor = conn.prepareStatement(
                    "DELETE FROM clinic_visit_optimizer.doctor WHERE doctor_id = ?");
            deleteDoctor.setInt(1, id);
            int rowsAffected = deleteDoctor.executeUpdate();
            deleteDoctor.close();

            // Step 5: Result feedback
            if (rowsAffected > 0) {
                System.out.println("\033[32mDeleted Successfully\033[0m");
            } else {
                System.out.println("\u001B[31mCan't delete Data!\u001B[0m");
            }

        } catch (SQLException e) {
            System.out.println("\u001B[31mCan't delete Data! Reason: " + e.getMessage() + "\u001B[0m");
        }

        System.out.println();
        doctorsPanel();
    }


}