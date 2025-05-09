package src.service;



import src.daol.PatientDAO;
import src.pojo.Patient;
import src.usermain.User;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class PatientMain {
    public static void patient() {
        Scanner sc = new Scanner(System.in);
        PatientDAO dao = new PatientDAO();
        boolean running = true;
        System.out.println("\033[1m\033[38;2;255;165;0m-----Patient Panel----- \u001B[0m\033[0m");
        System.out.println("\033[38;2;255;165;0mEnter Your Choice : \u001B[0m");
        while (running) {
            System.out.println("Enter 1 For Booking Appointment");
            System.out.println("Enter 2 For Update Patient Details");
            System.out.println("Enter 3 For View Patient Details");
            System.out.println("Enter 4 To Exit");

            if (!sc.hasNextInt()) {
                System.out.println("\u001B[31mEnter a Valid Choice :\u001B[0m");
                sc.next();
                continue;
            }

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline character after number input

            switch (choice) {
                case 1:
                    // Get patient details and validate
                    String name;
                    while (true) {
                        System.out.print("\033[38;2;255;165;0mEnter Name : \u001B[0m");
                        name = sc.nextLine().trim().toUpperCase();

                        if (name.isEmpty()) {
                            System.out.println("\u001B[31mEnter a Valid Name :\u001B[0m");
                        } else if (!name.matches("^[A-Za-z ]{3,}$")) {
                            System.out.println("\u001B[31mEnter a Valid Name : (min 3 Char)\u001B[0m");
                        } else {
                            break; // valid name
                        }
                    }

                    int age;
                    while (true) {
                        System.out.print("\033[38;2;255;165;0mEnter Age : \u001B[0m ");
                        if (sc.hasNextInt()) {
                            age = sc.nextInt();
                            if (age >= 1 && age <= 120) break;
                            else System.out.println("\u001B[31mEnter a Valid Age\u001B[0m");
                        } else {
                            System.out.println("\u001B[31mEnter a Valid Age\u001B[0m");
                            sc.next();
                        }
                    }

                    sc.nextLine(); // Consume newline character after number input
                    String gender = "";
                    while (true) {
                        System.out.print("\033[38;2;255;165;0mEnter Gender (M/F/O) : \u001B[0m  ");
                        gender = sc.nextLine().trim().toUpperCase();
                        if (gender.equals("M") || gender.equals("F") || gender.equals("O")) break;
                        System.out.println("\u001B[31mEnter a Valid Gender\u001B[0m");
                    }
                    //Blood Group Selection
                    String blood = "";
                    String[] bloodGroups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

                    while (true) {
                        System.out.println("\033[38;2;255;165;0mEnter a Valid Choice : \u001B[0m");
                        for (int i = 0; i < bloodGroups.length; i++) {
                            System.out.println((i + 1) + ". " + bloodGroups[i]);
                        }
                        System.out.print("\033[38;2;255;165;0mEnter Your Choice : \u001B[0m");
                        if (sc.hasNextInt()) {
                            int ch = sc.nextInt();
                            sc.nextLine(); // consume newline to avoid skipping input later
                            if (ch >= 1 && ch <= bloodGroups.length) {
                                blood = bloodGroups[ch - 1];
                                break;
                            } else {
                                System.out.println("\u001B[31mInvalid choice. Please select from 1 to 8.\u001B[0m");
                            }
                        } else {
                            System.out.println("\u001B[31mPlease enter a number between 1 and 8.\u001B[0m");
                            sc.next(); // clear invalid input
                        }
                    }



                    String contact;
                    while (true) {
                        System.out.print("\033[38;2;255;165;0mEnter contact number : \u001B[0m");
                        contact = sc.nextLine().trim();
                        if (contact.matches("\\d{10}")) break;
                        System.out.println("\u001B[31mEnter a valid 10-digit contact number.\u001B[0m");
                    }

                    String email;
                    while (true) {
                        System.out.print("\033[38;2;255;165;0mEnter Email : \u001B[0m ");
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
                    String address;
                    while (true) {
                        System.out.print("\033[38;2;255;165;0mEnter address : \u001B[0m ");
                        address = sc.nextLine().trim().toUpperCase();
                        if (address.matches("^[A-Za-z0-9, /-]{4,200}$")) break;
                        System.out.println("\u001B[31mEnter a Valid address.\u001B[0m");
                    }

                    // Select specialization with validation
                    // Fetch specializations dynamically from DB
                    List<String> specializations = dao.getDistinctSpecializations();

                    if (specializations.isEmpty()) {
                        System.out.println("\u001B[31mNo specializations available at the moment : \u001B[0m.");
                        break;
                    }

                    // Show specializations to user
                    List<String> doctors;
                    String specialization;
                    String doctor = null;
                    while (true) {
                        for (int i = 0; i < specializations.size(); i++) {
                            System.out.println((i + 1) + ". " + specializations.get(i));
                        }
                        System.out.print("\033[38;2;255;165;0mSelect specialization (1-" + specializations.size() + ") : \u001B[0m");

                        if (sc.hasNextInt()) {
                            int specializationChoice = sc.nextInt();
                            sc.nextLine(); // consume newline character

                            if (specializationChoice >= 1 && specializationChoice <= specializations.size()) {
                                specialization = specializations.get(specializationChoice - 1);
                                // Fetch doctors dynamically for chosen specialization
                                doctors = dao.getDoctorsBySpecialization(specialization);

                                if (doctors.isEmpty()) {
                                    System.out.println("\u001B[31mNo Doctors available for this Specialization\u001B[0m..");
                                    break;
                                }
                                while (true) {
                                    for (int i = 0; i < doctors.size(); i++) {
                                        System.out.println((i + 1) + ". " + doctors.get(i));
                                    }
                                    System.out.print("\033[38;2;255;165;0mSelect doctor (1-" + doctors.size() + "): \u001B[0m ");
                                    if (sc.hasNextInt()) {
                                        int doctorChoice = sc.nextInt();
                                        sc.nextLine(); // consume newline
                                        if (doctorChoice >= 1 && doctorChoice <= doctors.size()) {
                                            doctor = doctors.get(doctorChoice - 1); // Assign the selected doctor
                                            System.out.println("You selected: " + doctor);
                                            break; // exit loop after valid selection
                                        } else {
                                            System.out.println("\u001B[31mInvalid doctor choice. Please enter a number from 1 to " + doctors.size() + "\u001B[0m");
                                        }
                                    } else {
                                        System.out.println("\u001B[31mInvalid input. Please enter a number : \u001B[0m");
                                        sc.next(); // consume invalid input
                                    }
                                }
                                break; // Exit the specialization selection loop
                            } else {
                                System.out.println("\u001B[31mInvalid specialization choice. Please enter a valid option : \u001B[0m");
                            }
                        } else {
                            System.out.println("\u001B[31mInvalid input. Please enter a number : \u001B[0m");
                            sc.next(); // consume invalid input
                        }
                    }
                    // Enter appointment date with validation
                    LocalDate appointmentDate = null;
                    while (true) {
                        System.out.print("\033[38;2;255;165;0mEnter Appointment Date (yyyy-mm-dd) :\u001B[0m ");
                        String dateStr = sc.nextLine().trim();
                        try {
                            appointmentDate = LocalDate.parse(dateStr);
                            LocalDate today = LocalDate.now();
                            LocalDate maxDate = today.plusMonths(2);

                            if (appointmentDate.isBefore(today)) {
                                System.out.println("\u001B[31mAppointment Date cannot be in the Past !\u001B[0m");
                            } else if (appointmentDate.isAfter(maxDate)) {
                                System.out.println("\u001B[31mAppointment Date cannot be more than 2 Months from Today !\u001B[0m");
                            } else {
                                break;
                            }
                        } catch (DateTimeParseException e) {
                            System.out.println("\u001B[31mEnter a Valid Date Format (yyyy-mm-dd) : \u001B[0m");
                        }
                    }


                    // Convert LocalDate to java.sql.Date
                    Date sqlDate = Date.valueOf(appointmentDate);

                    // Create Patient object and insert into database
                    Patient patient = new Patient(name, age, gender, blood, Long.parseLong(contact),email,address, specialization, doctor, sqlDate);
                    boolean inserted = dao.insertPatient(patient);

                    if (inserted) {
                        System.out.println("\u001B[32mAppointment Success \u001B[0m");

                    } else {
                        System.out.println("\u001B[31mFailed to insert patient.\u001B[0m");
                    }
                    break;

                case 2:
                    int id = 0;
                    while (true) {
                        System.out.print("\033[38;2;255;165;0mEnter Patient ID to Update Contact :\u001B[0m");
                        if (sc.hasNextInt()) {
                            id = sc.nextInt();
                            sc.nextLine();
                            if (dao.doesPatientExist(id)) {
                                break;
                            } else {
                                System.out.println("\u001B[31mNo patient found with ID: " + id + "\u001B[0m");
                            }
                        } else {
                            System.out.println("\u001B[31mPlease enter a valid numeric ID.\u001B[0m");
                            sc.next();
                        }
                    }

                    String newContact;
                    while (true) {
                        System.out.print("\033[38;2;255;165;0mEnter New Contact Number :\u001B[0m");
                        newContact = sc.nextLine().trim();
                        if (newContact.matches("\\d{10}")) break;
                        System.out.println("\u001B[31mInvalid contact number. Enter a 10-digit number.\u001B[0m");
                    }

                    boolean updated = dao.updatePatientContact(id, Long.parseLong(newContact));
                    if (updated) {
                        System.out.println("\u001B[32mContact updated successfully!\u001B[0m");
                    } else {
                        System.out.println("\u001B[31mFailed to Update Contact.\u001B[0m");
                    }
                    break;


                case 3:
                    String contactSearch;
                    while (true) {
                        System.out.print("\033[38;2;255;165;0mEnter your contact number to view your registered details :\u001B[0m");
                        contactSearch = sc.nextLine().trim();
                        if (!contactSearch.matches("\\d{10}")) {
                            System.out.println("\u001B[31mInvalid contact number. Please enter a 10-digit number.\u001B[0m");
                        } else {
                            break; // valid input, exit loop
                        }
                    }

                    List<Patient> yourPatients = dao.getPatientsByContact(Long.parseLong(contactSearch));
                    if (yourPatients.isEmpty()) {
                        System.out.println("\u001B[31mNo Data found for this Contact Number.\u001B[0m");
                    } else {
                        System.out.println("\n\u001B[32m Your Registered Details \u001B[0m");
                        for (Patient p : yourPatients) {
                            System.out.println(p);
                        }
                    }
                    break;
                case 4:
                    running = false;
                    User.entry();
                    break;

                default:
                    System.out.println("\u001B[31mEnter a Valid Choice :\u001B[0m");
            }
        }
        sc.close();
    }
}
