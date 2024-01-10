import java.util.*;
public class Authentication{
    public static void main (String []args){
        Scanner input= new Scanner(System.in);
        System.out.println("~~~~~Enter your credentials~~~~~");
        String name = input.nextLine();
        String surname = input.nextLine();
        int bod = input.nextInt();
        System.out.print("@@@@@@Creating password@@@@@");
        String password = surname.toLowerCase().substring(3,6) + name.toUpperCase().substring(0,2) + (bod) + surname.toUpperCase().substring(0,2) + name.toLowerCase().substring(3,7);
        String username = input.nextLine();
        System.out.format("Welcome to the card game, %d, the password generated is: %d", username, password);

    }
}
