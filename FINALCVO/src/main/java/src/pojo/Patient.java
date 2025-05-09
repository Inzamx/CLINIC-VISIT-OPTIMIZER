package src.pojo;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Patient {
    private int id;
    private String name;
    private int age;
    private String gender;
    private String blood;
    private long contact;
    private String email;
    private String address;
    private String specialization;
    private String doctor;
    private static Timestamp registrationDate;
    private Date appointmentDate;

    public Patient(String name, int age, String gender, String blood, long contact,String email, String address, String specialization, String doctor, Date appointmentDate) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.blood = blood;
        this.contact = contact;
        this.email=email;
        this.address = address;
        this.specialization = specialization;
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
    }

    public Patient(int id, String name, int age, String gender, String blood, long contact,String email,String address, String specialization, String doctor, LocalDateTime registrationDate, LocalDate appointmentDate) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.blood = blood;
        this.contact = contact;
        this.email=email;
        this.address = address;
        this.specialization = specialization;
        this.doctor = doctor;
        this.registrationDate = Timestamp.valueOf(registrationDate);
        this.appointmentDate = Date.valueOf(appointmentDate);
    }

    public Patient() {}

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public long getContact() {
        return contact;
    }

    public void setContact(long contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDoctor() {
        return String.valueOf(doctor);
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public static Timestamp getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    public static final String PURPLE = "\u001B[35m";
    @Override
    public String toString() {
        return "Patient ID        : " + id +
                "\nName              : " + name +
                "\nAge               : " + age +
                "\nGender            : " + gender +
                "\nBlood Group       : " + blood +
                "\nContact           : " + contact +
                "\nEmail             : " +email+
                "\nAddress           : " + address +
                "\nSpecialization    : " + specialization +
                "\nDoctor            : " + doctor +
                "\nAppointment Date  : " + appointmentDate +
                "\nRegistration Date : " + registrationDate + "\n";

    }
}
