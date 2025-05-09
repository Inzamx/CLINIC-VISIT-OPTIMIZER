package src.pojo;

public class Pojo {
    private static int doctor_id;
    private static String name;
    private static String password;
    private static String specialization;
    private static String contact_Number;
    private static String email;
    private static String qualification;
    private static int experience;
    private static String working_Hours;
    private static String salary;
    private static String is_active;

    public static String getSalary() {
        return salary;
    }

    public static String setSalary(String salary) {
        Pojo.salary = salary;
        return salary;
    }

    public static String  isIs_active() {
        return is_active;
    }

    public static String setIs_active(String is_active) {
        Pojo.is_active = is_active;
        return is_active;
    }


    public static String getPassword() {
        return password;
    }

    public static String setPassword(String password) {
        Pojo.password = password;
        return password;
    }

    public static int getDoctor_id() {
        return doctor_id;
    }

    public static int setDoctor_id(int doctor_id) {
        Pojo.doctor_id = doctor_id;
        return doctor_id;
    }

    public static String getName(String name) {
        return Pojo.name;
    }

    public static String setName(String name) {
        Pojo.name = name;
        return name;
    }

    public static String getSpecialization(String specialization) {
        return Pojo.specialization;
    }

    public static String setSpecialization(String specialization) {
        Pojo.specialization = specialization;
        return specialization;
    }

    public static String getContact_Number(String number) {
        return contact_Number;
    }

    public static String setContact_Number(String contact_Number) {
        Pojo.contact_Number = contact_Number;
        return contact_Number;
    }

    public static String getEmail(String email) {
        return Pojo.email;
    }

    public static String setEmail(String email) {
        Pojo.email = email;
        return email;
    }

    public static String getQualification(String qualification) {
        return Pojo.qualification;
    }

    public static String setQualification(String qualification) {
        Pojo.qualification = qualification;
        return qualification;
    }

    public static int getExperience(int experience) {
        return Pojo.experience;
    }

    public static int setExperience(int experience) {
        Pojo.experience = experience;
        return experience;
    }

    public static String getWorking_Hours(String working_Hours) {
        return Pojo.working_Hours;
    }

    public static String setWorking_Hours(String working_Hours) {
        Pojo.working_Hours = working_Hours;
        return working_Hours;
    }


}
