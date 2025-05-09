package src.usermain;

public class UserMain {
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(1000);
        String str = "\033[1m\u001B[94m * * * WELCOME TO CLINIC VISIT OPTIMIZER APPLICATION  * * * \u001B[0m\033[0m";
//        for (int i=0;i<str.length();i++){
        System.out.print(str);//.charAt(i)
        Thread.sleep(300);
//        }
        System.out.println();
        Thread.sleep(1000);
        User.entry();
    }

}
