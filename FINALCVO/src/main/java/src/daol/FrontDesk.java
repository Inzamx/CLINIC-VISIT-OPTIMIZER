package src.daol;



import src.connection.DBConnection;
import src.pojo.Pojo;
import src.pojo.PojoFrontDeskExecutive;
import src.service.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class FrontDesk {
    public static void frontDeskPanel(){
        Scanner sc = new Scanner(System.in);
        System.out.println("\033[38;2;255;165;0mEnter Your Choice : \u001B[0m");
        boolean flag = false;
        while (!flag) {
            System.out.println("Enter 1. To Display All Front Desk Executive" +
                    "\nEnter 2. To Display particular Front Desk Executive" +
                    "\nEnter 3. To Insert Front Desk Executive" +
                    "\nEnter 4. To Update Front Desk Executive" +
                    "\nEnter 5. To Delete Front Desk Executive" +
                    "\nEnter 6. To Go Back");
            try {
                int choice1 = sc.nextInt();
                switch (choice1) {
                    case 1:
                        // select all the doctors
                        displayAllFrontDesk();
                        frontDeskPanel();
                        flag = true;
                        break;
                    case 2:
                        // select particular doctor
                        int id;
                        while (true) {
                            try {
                                System.out.println("\033[38;2;255;165;0mEnter Front Desk Id : \u001B[0m");
                                id = sc.nextInt();
                                if (id != 0) {
                                    displayFrontDesk(id);
                                    break;
                                } else {
                                    System.out.println("\u001B[31mEnter a Valid Id :\u001B[0m");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("\u001B[31mEnter a Valid Id :\u001B[0m");
                                sc.nextLine();
                            }
                        }
                        frontDeskPanel();
                        flag = true;
                        break;
                    case 3:
                        // insertion of the doctor
                        insertFrontDesk();
                        frontDeskPanel();
                        flag = true;
                        break;
                    case 4:
                        // update of the doctor
                        updateValidation();
                        frontDeskPanel();
                        flag = true;
                        break;
                    case 5:
                        // delete of the doctor
                        deleteFrontDesk();
                        flag = true;
                        break;
                    case 6:
                        Admin.adminPanel();
                        flag=true;
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
    public static void displayAllFrontDesk(){
        try {
            String query="Select * from clinic_visit_optimizer.front_desk";
            Connection conn= DBConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(query);
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
                System.out.println();
                System.out.println("Id             : " + rs.getInt(1));
                System.out.println("Name           : " + rs.getString(2));
                System.out.println("Password       : " + rs.getString(3));
                System.out.println("Gender         : " + rs.getString(4));
                System.out.println("Address        : " + rs.getString(5));
                System.out.println("Contact Number : " + rs.getString(6));
                System.out.println("Email          : " + rs.getString(7));
                System.out.println("Qualification  : " + rs.getString(8));
                System.out.println("Experience     : " + rs.getInt(9));
                System.out.println("Working Hours  : " + rs.getString(10));
                System.out.println("Salary         : " + rs.getString(11));
                System.out.println("Status         : " + rs.getString(12));
                System.out.println();
                System.out.println("=======================================================");
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31mDatabase : "+e.getMessage()+"\u001B[0m");
        }
    }

    public static void displayFrontDesk(int id){
        try {
            String query="Select * from clinic_visit_optimizer.front_desk where fd_id=?";
            Connection conn=DBConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                System.out.println();
                System.out.println("Id             : " + rs.getInt(1));
                System.out.println("Name           : " + rs.getString(2));
                System.out.println("Password       : " + rs.getString(3));
                System.out.println("Gender         : " + rs.getString(4));
                System.out.println("Address        : " + rs.getString(5));
                System.out.println("Contact Number : " + rs.getString(6));
                System.out.println("Email          : " + rs.getString(7));
                System.out.println("Qualification  : " + rs.getString(8));
                System.out.println("Experience     : " + rs.getInt(9));
                System.out.println("Working Hours  : " + rs.getString(10));
                System.out.println("Salary         : " + rs.getString(11));
                System.out.println("Status         : " + rs.getString(12));
                System.out.println();
            }
            else
                throw new SQLException("Front Desk Id : "+id+" not found");
        } catch (SQLException e) {
            System.out.println("\u001B[31mDatabase : "+e.getMessage()+"\u001B[0m");
        }
    }

    public static void insertFrontDesk() {
        try {
            Scanner sc = new Scanner(System.in);
            String query = "insert into clinic_visit_optimizer.front_desk values(?,?,?,?,?,?,?,?,?,?,?,?);";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            System.out.println("\033[38;2;255;165;0mEnter Front Desk Id : \u001B[0m");
            int id ;
            while (true) {
                try {
                    id = sc.nextInt();
                    sc.nextLine();
                    if (id != 0) {
                        ps.setInt(1, PojoFrontDeskExecutive.setFront_desk_id(id));
                        break;
                    } else {
                        System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    sc.nextLine();
                }
            }
            ResultSet rs=selectFdId();
            while (rs.next()){
                if(rs.getInt("fd_id")==id){
                    System.out.println("\u001B[31mDoctor Id : "+id+" Duplicate Entry\u001B[0m");
                    return;
                }
                else {
                    break;
                }
            }
            System.out.println("\033[38;2;255;165;0mEnter Name : \u001B[0m");
            while (true) {
                String name = sc.nextLine();
                if (name.matches(".[!@#$%^&*0-9].")) {
                    System.out.println("\u001B[31mEnter a Valid Name : \u001B[0m");
                } else if (name.trim().isEmpty()) {
                    System.out.println("\u001B[31mName Cannot be Empty !\u001B[0m");
                    System.out.println("\u001B[31mEnter a Valid Name : \u001B[0m");
                } else {
                    ps.setString(2, PojoFrontDeskExecutive.setName(name));
                    break;
                }
            }
            System.out.println("\033[38;2;255;165;0mEnter  Password : \u001B[0m");
            while (true) {
                String password = sc.nextLine();
                if (password==null) {
                    System.out.println("\u001B[31mPassword Cannot be Empty !\u001B[0m");
                    System.out.println("\u001B[31mEnter a Valid Password :\u001B[0m");
                } else {
                    ps.setString(3, PojoFrontDeskExecutive.setPassword(password));
                    break;
                }
            }
            System.out.println("\033[38;2;255;165;0mEnter Gender : \u001B[0m");
            while (true) {
                System.out.println("Enter 1 For Female" +
                        "\nEnter 2 For Male" +
                        "\nEnter 3 For Others");
                try{
                    int choice=sc.nextInt();
                    sc.nextLine();
                    String gender ;
                    if (choice==1) {
                        gender="Female";
                        ps.setString(4, PojoFrontDeskExecutive.setGender(gender));
                        break;
                    } else if (choice==2) {
                        gender="Male";
                        ps.setString(4,PojoFrontDeskExecutive.setGender(gender));
                        break;
                    } else if(choice==3){
                        gender="Others";
                        ps.setString(4,PojoFrontDeskExecutive.setGender(gender));
                        break;
                    }else{
                        System.out.println("\u001B[31mEnter a Valid Choice :\u001B[0m");
                        sc.nextLine();
                    }
                }catch (InputMismatchException e){
                    System.out.println("\u001B[31mEnter a Valid Choice :\u001B[0m");
                    sc.nextLine();
                }
            }
            System.out.println("\033[38;2;255;165;0mEnter Address : \u001B[0m");
            while (true) {
                String address= sc.nextLine();
                if (address.isEmpty()) {
                    System.out.println("\u001B[31mAddress Cannot be Empty !\u001B[0m");
                    System.out.println("\u001B[31mEnter a Valid Address :\u001B[0m");
                } else {
                    ps.setString(5, PojoFrontDeskExecutive.setAddress(address));
                    break;
                }
            }
            System.out.println("\033[38;2;255;165;0mEnter Contact Number : \u001B[0m");
            while (true) {
                String number = sc.nextLine();
                if (number.isEmpty()) {
                    System.out.println("\u001B[31mNumber Cannot be Empty !\u001B[0m");
                    System.out.println("\u001B[31mEnter a Valid Contact Number :\u001B[0m");
                } else if (!number.matches("\\d{10}")) {
                    System.out.println("\u001B[31mInvalid input! Only 10 Numbers Allowed :\u001B[0m");
                } else {
                    ps.setString(6, PojoFrontDeskExecutive.setPhone_number(number));
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
                    ps.setString(7, Pojo.setEmail(email));
                    break;
                }
            }
            System.out.println("\033[38;2;255;165;0mEnter Qualification : \u001B[0m");
            while (true) {
                String qualification = sc.nextLine();
                if ((qualification.trim().isEmpty())) {
                    System.out.println("\u001B[31mEnter a Valid Qualification : \u001B[0m");
                } else if (qualification.matches(".[!@#$%^&*0-9].")) {
                    System.out.println("\u001B[31mQualification Cannot be Empty !\u001B[0m");
                    System.out.println("\u001B[31mEnter a Valid Qualification : \u001B[0m");
                } else {
                    ps.setString(8, Pojo.setQualification(qualification));
                    break;
                }
            }
            System.out.println("\033[38;2;255;165;0mEnter Experience : \u001B[0m");
            while (true) {
                try {
                    int experience = sc.nextInt();
                    sc.nextLine();
                    ps.setInt(9, Pojo.setExperience(experience));
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("\u001B[31mEnter a Valid Experience Year : \u001B[0m");
                    sc.nextLine();
                }
            }
            String working_Hours = "9:00 AM To 5:00 PM";
            ps.setString(10,PojoFrontDeskExecutive.setWorking_hours(working_Hours));
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
            ps.setString(11, Pojo.setSalary(sal));
            System.out.println("\033[38;2;255;165;0mEnter Status : \u001B[0m");
            while (true) {
                try {
                    System.out.println("Enter 1 For Online" +
                            "\nEnter 2 for Offline");
                    int choice = sc.nextInt();
                    String status;
                    if (choice == 1) {
                        status = "Online";
                        ps.setString(12, Pojo.setIs_active(status));
                        break;
                    } else if (choice == 2) {
                        status = "Offline";
                        ps.setString(12, Pojo.setIs_active(status));
                        break;
                    } else
                        System.out.println("\u001B[31mEnter a Valid Status : \u001B[0m");
                } catch (InputMismatchException e) {
                    System.out.println("\u001B[31mEnter a Valid Status : \u001B[0m");
                    sc.nextLine();
                }
            }
            ps.execute();
            System.out.println("\033[32mInserted Data Successfully\033[0m");
            Thread.sleep(1000);
            displayFrontDesk(id);
        } catch (SQLException | InterruptedException e) {
            System.out.println("\u001B[31mDatabase : "+e.getMessage()+"\u001B[0m");
        }
    }


    public static void updateValidation() {
        Scanner sc = new Scanner(System.in);
        boolean flag = false;
        System.out.println("\033[38;2;255;165;0mEnter Your Choice : \u001B[0m");
        while (!flag) {
            System.out.println("Enter 1 for Updating Name" +
                    "\nEnter 2 for Updating Password" +
                    "\nEnter 3 for Updating Gender" +
                    "\nEnter 4 for Updating Address" +
                    "\nEnter 5 for Updating Phone Number" +
                    "\nEnter 6 for Updating Email" +
                    "\nEnter 7 for Updating Qualification" +
                    "\nEnter 8 for Updating Experience" +
                    "\nEnter 9 for Updating Salary" +
                    "\nEnter 10 for Updating Status" +
                    "\nEnter 11 for Go Back");
            try {
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        updateFdName();
                        updateValidation();
                        flag = true;
                        break;
                    case 2:
                        updateFdPassword();
                        updateValidation();
                        flag=true;
                        break;
                    case 3:
                        updateFdGender();
                        updateValidation();
                        flag = true;
                        break;
                    case 4:
                        updateFdAddress();
                        updateValidation();
                        flag = true;
                        break;
                    case 5:
                        updateFdContactNumber();
                        updateValidation();
                        flag = true;
                        break;
                    case 6:
                        updateFdEmail();
                        updateValidation();
                        flag = true;
                        break;
                    case 7:
                        updateFdQualification();
                        updateValidation();
                        flag = true;
                        break;
                    case 8:
                        updateFdExperience();
                        updateValidation();
                        flag = true;
                        break;
                    case 9:
                        updateFdSalary();
                        updateValidation();
                        flag = true;
                        break;
                    case 10:
                        updateFdStatus();
                        updateValidation();
                        flag = true;
                        break;
                    case 11:
                        frontDeskPanel();
                        flag=true;
                        break;
                    default:
                        System.out.println("\u001B[31mEnter a Valid Choice :\u001B[0m");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("\u001B[31mEnter a Valid Choice :\u001B[0m");
                sc.next();
            }
        }
    }

    public static ResultSet selectFdId(){
        try {
            String query="Select fd_id from clinic_visit_optimizer.front_desk";
            Connection conn=DBConnection.getConnection();
            PreparedStatement ps =conn.prepareStatement(query);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                return rs;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static void updateFdName() {
        Scanner sc = new Scanner(System.in);
        try {
            String query = "update clinic_visit_optimizer.front_desk set clinic_visit_optimizername=? where fd_id=?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            System.out.println("\033[38;2;255;165;0mEnter Front Desk Id : \u001B[0m");
            int id;
            while (true){
                try{
                    id=sc.nextInt();
                    sc.nextLine();
                    if(id!=0 ){
                        break;
                    }
                    else {
                        System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    }
                }
                catch (InputMismatchException e){
                    System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    sc.nextLine();
                }
            }
            ResultSet rs=selectFdId();
            while (rs.next()){
                if(rs.getInt("fd_id")!=id){
                    System.out.println("\u001B[31mFront Desk Id : "+id+" not Found\u001B[0m");
                    return;
                }
            }
            System.out.println("\033[38;2;255;165;0mEnter Name : \u001B[0m");
            String name;
            while (true){
                name = sc.nextLine();
                if(name.matches(" *.[!@#$%^&*0-9].*")){
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
            System.out.println("\033[32mUpdated Name Successfully\033[0m");
            System.out.println();
            displayFrontDesk(id);
            Thread.sleep(1000);
        } catch (SQLException | InterruptedException e) {
            System.out.println("\u001B[31mDatabase : "+e.getMessage()+"\u001B[0m");
        }
        System.out.println();
    }

    public static void updateFdPassword() {
        Scanner sc = new Scanner(System.in);
        try {
            String query = "update clinic_visit_optimizer.front_desk set password=? where fd_id=?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            System.out.println("\033[38;2;255;165;0mEnter Front Desk Id : \u001B[0m");
            int id;
            while (true){
                try{
                    id=sc.nextInt();
                    sc.nextLine();
                    if(id!=0 ){
                        break;
                    }
                    else {
                        System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    }
                }
                catch (InputMismatchException e){
                    System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    sc.nextLine();
                }
            }
            ResultSet rs=selectFdId();
            while (rs.next()){
                if(rs.getInt("fd_id")!=id){
                    System.out.println("\u001B[31mFront Desk Id : "+id+" not Found\u001B[0m");
                    return;
                }
            }
            System.out.println("\033[38;2;255;165;0mEnter Password : \u001B[0m");
            String password;
            while (true) {
                password = sc.nextLine();
                if (password==null) {
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
            displayFrontDesk(id);
            Thread.sleep(1000);
        } catch (SQLException | InterruptedException e) {
            System.out.println("\u001B[31mDatabase : "+e.getMessage()+"\u001B[0m");
        }
        System.out.println();
    }

    public static void updateFdGender() {
        Scanner sc = new Scanner(System.in);
        try {
            String query = "update clinic_visit_optimizer.front_desk set gender=? where fd_id=?";
            Connection conn =DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            System.out.println("\033[38;2;255;165;0mEnter Front Desk Id : \u001B[0m");
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
            ResultSet rs=selectFdId();
            while (rs.next()){
                if(rs.getInt("fd_id")!=id){
                    System.out.println("\u001B[31mFront Desk Id : "+id+" not Found\u001B[0m");
                    return;
                }
            }
            System.out.println("\033[38;2;255;165;0mEnter Gender : \u001B[0m");
            String gender;
            while (true) {
                System.out.println("Enter 1 For Female" +
                        "\nEnter 2 For Male" +
                        "\nEnter 3 For Others");
                try {
                    int choice = sc.nextInt();
                    sc.nextLine();
                    if (choice == 1) {
                        gender = "Female";
                        break;
                    } else if (choice == 2) {
                        gender = "Male";
                        break;
                    } else if (choice == 3) {
                        gender = "Others";
                        break;
                    } else {
                        System.out.println("\u001B[31mEnter a Valid Choice :\u001B[0m");
                        sc.nextLine();
                    }
                } catch (InputMismatchException e) {
                    System.out.println("\u001B[31mEnter a Valid Choice :\u001B[0m");
                    sc.nextLine();
                }
            }
            ps.setString(1, gender);
            ps.setInt(2, id);
            System.out.println();
            ps.executeUpdate();
            System.out.println("\033[32mUpdated Gender Successfully\033[0m");
            System.out.println();
            displayFrontDesk(id);
            Thread.sleep(1000);
        } catch (SQLException | InterruptedException e) {
            System.out.println("\u001B[31mDatabase : "+e.getMessage()+"\u001B[0m");
        }
        System.out.println();
    }

    public static void updateFdAddress() {
        Scanner sc = new Scanner(System.in);
        try {
            String query = "update clinic_visit_optimizer.front_desk set address=? where fd_id=?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            System.out.println("\033[38;2;255;165;0mEnter Front Desk Id : \u001B[0m");
            int id;
            while (true){
                try{
                    id=sc.nextInt();
                    sc.nextLine();
                    if(id!=0 ){
                        break;
                    }
                    else {
                        System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    }
                }
                catch (InputMismatchException e){
                    System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    sc.nextLine();
                }
            }
            ResultSet rs=selectFdId();
            while (rs.next()){
                if(rs.getInt("fd_id")!=id){
                    System.out.println("\u001B[31mFront Desk Id : "+id+" not Found\u001B[0m");
                    return;
                }
            }
            String address;
            System.out.println("\033[38;2;255;165;0mEnter Address : \u001B[0m");
            while (true) {
                address= sc.nextLine();
                if (address.isEmpty()) {
                    System.out.println("\u001B[31mAddress Cannot be Empty !\u001B[0m");
                    System.out.println("\u001B[31mEnter a Valid Address :\u001B[0m");
                } else {
                    break;
                }
            }
            ps.setString(1, address);
            ps.setInt(2, id);
            System.out.println();
            ps.executeUpdate();
            System.out.println("\033[32mUpdated Address Successfully\033[0m");
            System.out.println();
            Thread.sleep(1000);
            displayFrontDesk(id);
        } catch (SQLException | InterruptedException e) {
            System.out.println("\u001B[31mDatabase : "+e.getMessage()+"\u001B[0m");
        }
        System.out.println();
    }

    public static void updateFdContactNumber() {
        Scanner sc = new Scanner(System.in);
        try {
            String query = "update clinic_visit_optimizer.front_desk set phone_number=? where fd_id=?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            System.out.println("\033[38;2;255;165;0mEnter Front Desk Id : \u001B[0m");
            int id;
            while (true){
                try{
                    id=sc.nextInt();
                    sc.nextLine();
                    if(id!=0 ){
                        break;
                    }
                    else {
                        System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    }
                }
                catch (InputMismatchException e){
                    System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    sc.nextLine();
                }
            }
            ResultSet rs=selectFdId();
            while (rs.next()){
                if(rs.getInt("fd_id")!=id){
                    System.out.println("\u001B[31mFront Desk Id : "+id+" not Found\u001B[0m");
                    return;
                }
            }
            System.out.println("\033[38;2;255;165;0mEnter Contact Number : \u001B[0m");
            String number;
            while (true){
                number = sc.nextLine();
                if (number.isEmpty()) {
                    System.out.println("\u001B[31mNumber Cannot be Empty !\u001B[0m");
                    System.out.println("\u001B[31mEnter a Valid Contact Number :\u001B[0m");
                }
                else if (!number.matches("\\d{10}")) {
                    System.out.println("\u001B[31mInvalid input! Only Numbers Allowed :\u001B[0m");
                } else {
                    break;
                }
            }
            ps.setString(1, number);
            ps.setInt(2, id);
            System.out.println();
            ps.executeUpdate();
            System.out.println("\033[32mUpdated Contact Number Successfully\033[0m");
            System.out.println();
            displayFrontDesk(id);
            Thread.sleep(1000);
        } catch (SQLException | InterruptedException e) {
            System.out.println("\u001B[31mDatabase : "+e.getMessage()+"\u001B[0m");
        }
        System.out.println();
    }

    public static void updateFdEmail() {
        Scanner sc = new Scanner(System.in);
        try {
            String query = "update clinic_visit_optimizer.front_desk set email=? where fd_id=?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            System.out.println("\033[38;2;255;165;0mEnter Front Desk Id : \u001B[0m");
            int id;
            while (true){
                try{
                    id=sc.nextInt();
                    sc.nextLine();
                    if(id!=0 ){
                        break;
                    }
                    else {
                        System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    }
                }
                catch (InputMismatchException e){
                    System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    sc.nextLine();
                }
            }
            ResultSet rs=selectFdId();
            while (rs.next()){
                if(rs.getInt("fd_id")!=id){
                    System.out.println("\u001B[31mFront Desk Id : "+id+" not Found\u001B[0m");
                    return;
                }
            }
            System.out.println("\033[38;2;255;165;0mEnter Email : \u001B[0m");
            String email ;
            while (true){
                email = sc.nextLine();
                if(email.isEmpty()) {
                    System.out.println("\u001B[31mEmail Cannot be Empty !\u001B[0m");
                    System.out.println("\u001B[31mEmail a Valid Email :\u001B[0m");
                } else if (!email.matches("^[\\w.-]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    System.out.println("\u001B[31mInvalid email format! Example: user@example.com\u001B[0m");
                } else{
                    break;
                }
            }
            ps.setString(1, email);
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("\033[32mUpdated Email Successfully\033[0m");
            displayFrontDesk(id);
            Thread.sleep(1000);
        } catch (SQLException | InterruptedException e) {
            System.out.println("\u001B[31mDatabase : "+e.getMessage()+"\u001B[0m");
        }
        System.out.println();
    }

    public static void updateFdQualification() {
        Scanner sc = new Scanner(System.in);
        try {
            String query = "update clinic_visit_optimizer.front_desk set qualification=? where fd_id=?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            System.out.println("\033[38;2;255;165;0mEnter Front Desk Id : \u001B[0m");
            int id;
            while (true){
                try{
                    id=sc.nextInt();
                    sc.nextLine();
                    if(id!=0 ){
                        break;
                    }
                    else {
                        System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    }
                }
                catch (InputMismatchException e){
                    System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    sc.nextLine();
                }
            }
            ResultSet rs=selectFdId();
            while (rs.next()){
                if(rs.getInt("fd_id")!=id){
                    System.out.println("\u001B[31mFront Desk Id : "+id+" not Found\u001B[0m");
                    return;
                }
            }
            System.out.println("\033[38;2;255;165;0mEnter Qualification : \u001B[0m");
            String qualification ;
            while (true){
                qualification = sc.nextLine();
                if(qualification.trim().isEmpty()){
                    System.out.println("\u001B[31mQualification Cannot be Empty !\u001B[0m");
                    System.out.println("\u001B[31mEnter a Valid Qualification : \u001B[0m");
                } else if (qualification.matches(".[!@#$%^&*0-9].")) {
                    System.out.println("\u001B[31mEnter a Valid Qualification : \u001B[0m");
                } else {
                    break;
                }
            }
            ps.setString(1, qualification);
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("\033[32mUpdated Qualification Successfully\033[0m");
            Thread.sleep(1000);
            displayFrontDesk(id);
        } catch (SQLException | InterruptedException e) {
            System.out.println("\u001B[31mDatabase : "+e.getMessage()+"\u001B[0m");
        }
        System.out.println();
    }

    public static void updateFdExperience() {
        Scanner sc = new Scanner(System.in);
        try {
            String query = "update clinic_visit_optimizer.front_desk set experience=? where fd_id=?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            System.out.println("\033[38;2;255;165;0mEnter Front Desk Id : \u001B[0m");
            int id;
            while (true){
                try{
                    id=sc.nextInt();
                    sc.nextLine();
                    if(id!=0 ){
                        break;
                    }
                    else {
                        System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    }
                }
                catch (InputMismatchException e){
                    System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    sc.nextLine();
                }
            }
            ResultSet rs=selectFdId();
            while (rs.next()){
                if(rs.getInt("fd_id")!=id){
                    System.out.println("\u001B[31mFront Desk Id : "+id+" not Found\u001B[0m");
                    return;
                }
            }
            System.out.println("\033[38;2;255;165;0mEnter Experience : \u001B[0m");
            int experience;
            while (true){
                try{
                    experience = sc.nextInt();
                    sc.nextLine();
                    break;
                }
                catch (InputMismatchException e){
                    System.out.println("\u001B[31mEnter a Valid Experience Year : \u001B[0m");
                    sc.nextLine();
                }
            }
            ps.setInt(1, experience);
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("\033[32mUpdated Experience Successfully\033[0m");
            System.out.println();
            displayFrontDesk(id);
            Thread.sleep(1000);
        } catch (SQLException | InterruptedException e) {
            System.out.println("\u001B[31mDatabase : "+e.getMessage()+"\u001B[0m");
        }
        System.out.println();
    }

    public static void updateFdSalary(){
        Scanner sc = new Scanner(System.in);
        try {
            String query = "update clinic_visit_optimizer.front_desk set salary=? where fd_id=?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            System.out.println("\033[38;2;255;165;0mEnter Front Desk Id : \u001B[0m");
            int id;
            while (true){
                try{
                    id=sc.nextInt();
                    sc.nextLine();
                    if(id!=0 ){
                        break;
                    }
                    else {
                        System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    }
                }
                catch (InputMismatchException e){
                    System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    sc.nextLine();
                }
            }
            ResultSet rs=selectFdId();
            while (rs.next()){
                if(rs.getInt("fd_id")!=id){
                    System.out.println("\u001B[31mFront Desk Id : "+id+" not Found\u001B[0m");
                    return;
                }
            }
            System.out.println("\033[38;2;255;165;0mEnter Salary : \u001B[0m");
            String sal;
            while (true) {
                sal = sc.nextLine();
                if (!sal.matches("^[0-9]+$")) {
                    System.out.println("Enter a valid salary :");
                } else {

                    break;
                }
            }
            ps.setString(1, Pojo.setSalary(sal));
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("\033[32mUpdated Salary Successfully\033[0m");
            Thread.sleep(1000);
            displayFrontDesk(id);
        } catch (SQLException | InterruptedException e) {
            System.out.println("\u001B[31mDatabase : "+e.getMessage()+"\u001B[0m");
        }
        System.out.println();
    }
    public static void updateFdStatus() {
        Scanner sc = new Scanner(System.in);
        try {
            String query = "update clinic_visit_optimizer.front_desk set status=? where fd_id=?";
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            System.out.println("\033[38;2;255;165;0mEnter Front Desk Id : \u001B[0m");
            int id;
            while (true){
                try{
                    id=sc.nextInt();
                    sc.nextLine();
                    if(id!=0 ){
                        break;
                    }
                    else {
                        System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    }
                }
                catch (InputMismatchException e){
                    System.out.println("\u001B[31mEnter a Valid Id\u001B[0m");
                    sc.nextLine();
                }
            }
            ResultSet rs=selectFdId();
            while (rs.next()){
                if(rs.getInt("fd_id")!=id){
                    System.out.println("\u001B[31mFront Desk Id : "+id+" not Found\u001B[0m");
                    return;
                }
            }
            System.out.println("\033[38;2;255;165;0mEnter Status : \u001B[0m");
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
                        System.out.println("\u001B[31mEnter a Valid Status :\u001B[0m");
                } catch (InputMismatchException e) {
                    System.out.println("\u001B[31mEnter a Valid Status :\u001B[0m");
                    sc.nextLine();
                }
            }
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("\033[32mUpdated Status Successfully\033[0m");
            System.out.println();
            Thread.sleep(1000);
            displayFrontDesk(id);
        } catch (SQLException | InterruptedException e) {
            System.out.println("\u001B[31mDatabase : "+e.getMessage()+"\u001B[0m");
        }
        System.out.println();
    }

    public static void deleteFrontDesk() {
        Scanner sc = new Scanner(System.in);
            try {
                String query = "delete from clinic_visit_optimizer.front_desk where fd_id=?";
                Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                System.out.println("\033[38;2;255;165;0mEnter Front Desk Id : \u001B[0m");
                int id ;
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
                ResultSet rs=selectFdId();
                while (rs.next()){
                    if(rs.getInt("fd_id")!=id){
                        System.out.println("\u001B[31mFront Desk Id : "+id+" not Found\u001B[0m");
                        return;
                    }
                }
                ps.setInt(1, id);
                ps.execute();
                System.out.println("\033[32mDeleted Data Successfully\033[0m");
            } catch (SQLException e) {
                System.out.println("\u001B[31mDatabase : "+e.getMessage()+"\u001B[0m");
            }
    }
}