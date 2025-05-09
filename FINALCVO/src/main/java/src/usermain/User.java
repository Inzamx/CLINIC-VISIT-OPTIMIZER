package src.usermain;



import src.daol.DoctorDao;
import src.service.Admin;
import src.service.FrontDeskLoginValidation;
import src.service.PatientMain;

import java.util.InputMismatchException;
import java.util.Scanner;

public class User {
    public static void entry() {
        Scanner sc = new Scanner(System.in);
        boolean flag = false;
        System.out.println("\033[38;2;255;165;0mEnter Your Choice : \u001B[0m");
        while (!flag) {
            System.out.println("Enter 1. For Administrative Officer" +
                    "\nEnter 2. For Patient Registration" +
                    "\nEnter 3. For Doctor" +
                    "\nEnter 4. For Front Desk Executive" +
                    "\nEnter 5. For Exit");
            try {
                int choice = sc.nextInt();
                choice = Integer.parseInt(String.valueOf(choice));
                switch (choice) {
                    case 1:
                        Admin.adminValidation();
                        flag = true;
                        break;
                    case 2:
                        PatientMain.patient();
                        flag = true;
                        break;
                    case 3:
                        DoctorDao.docValidation();
                        flag = true;
                        break;
                    case 4:
                        FrontDeskLoginValidation.fdLoginvalidation();
                        flag = true;
                        break;
                    case 5:
                        System.out.println("\033[1m\u001B[94m * * * THANK YOU VISIT AGAIN * * * \u001B[0m\033[0m");
                        System.exit(0);
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
}
