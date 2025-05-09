package src.pojo;

public class PojoFrontDeskExecutive {
    private static int front_desk_id;
    private static String name;
    private static String password;
    private static String gender;
    private static String address;
    private static String phone_number;
    private static String email;
    private static String qualification;
    private static  int experience;
    private static String working_hours;
    private static String salary;
    private static String status;

    public static String getSalary() {
        return salary;
    }

    public static void setSalary(String salary) {
        PojoFrontDeskExecutive.salary = salary;
    }

    public static String getStatus() {
        return status;
    }

    public static void setStatus(String status) {
        PojoFrontDeskExecutive.status = status;
    }

    public static int getFront_desk_id(int id) {
        return front_desk_id;
    }

    public static int setFront_desk_id(int front_desk_id) {
        PojoFrontDeskExecutive.front_desk_id = front_desk_id;
        return front_desk_id;
    }

    public static String getName(String name) {
        return PojoFrontDeskExecutive.name;
    }

    public static String setName(String name) {
        PojoFrontDeskExecutive.name = name;
        return name;
    }

    public static String getPassword(String password) {
        return PojoFrontDeskExecutive.password;
    }

    public static String setPassword(String password) {
        PojoFrontDeskExecutive.password = password;
        return password;
    }

    public static String getGender(String gender) {
        return PojoFrontDeskExecutive.gender;
    }

    public static String setGender(String gender) {
        PojoFrontDeskExecutive.gender = gender;
        return gender;
    }

    public static String getAddress(String address) {
        return PojoFrontDeskExecutive.address;
    }

    public static String setAddress(String address) {
        PojoFrontDeskExecutive.address = address;
        return address;
    }

    public static String getPhone_number(String phone_number) {
        return PojoFrontDeskExecutive.phone_number;
    }

    public static String setPhone_number(String phone_number) {
        PojoFrontDeskExecutive.phone_number = phone_number;
        return phone_number;
    }

    public static String getEmail(String email) {
        return PojoFrontDeskExecutive.email;
    }

    public static String setEmail(String email) {
        PojoFrontDeskExecutive.email = email;
        return email;
    }

    public static String getQualification(String qualification) {
        return PojoFrontDeskExecutive.qualification;
    }

    public static String setQualification(String qualification) {
        PojoFrontDeskExecutive.qualification = qualification;
        return qualification;
    }

    public static int getExperience(int experience) {
        return PojoFrontDeskExecutive.experience;
    }

    public static int setExperience(int experience) {
        PojoFrontDeskExecutive.experience = experience;
        return experience;
    }

    public static String getWorking_hours(String working_Hours) {
        return working_hours;
    }

    public static String setWorking_hours(String working_hours) {
        PojoFrontDeskExecutive.working_hours = working_hours;
        return working_hours;
    }
}

