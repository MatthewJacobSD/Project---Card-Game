
package Archive;

import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class Profile {
    static final String USER_CREDENTIAL_INFO_FILE_PATH = "User_CredentialInfo.txt";
    static final String PLAYER_DETAILS_FILE_PATH = "Player_Details.txt";
    static class Profile_Info_Structure {
        String name = "";
        String surname = "";
        String username = "";
        String password = "";
        String dob = "";

        }

    public String User_Profile() {
        Scanner input = new Scanner(System.in);
        Profile_Info_Structure info = new Profile_Info_Structure();

        while (info.username.isEmpty()){
            System.out.print("Username cannot be empty. Enter your username: ");
            info.username = input.nextLine();
        }

        while (info.name.isEmpty()) {
            System.out.print("Name cannot be empty. Enter your name:   ");
            info.name = input.nextLine();
        }

        while  (info.surname.isEmpty()) {
            System.out.print("Enter your surname:   ");
            info.surname = input.nextLine();
        }

        while (info.dob.isEmpty()) {
            System.out.print("Enter your date of birth (dd/mm/yyyy): ");
            info.dob = input.nextLine();
        }

        info.password = generatePassword(info.name, info.surname, info.dob);

        // Add user information to the files
        addUserCredentialInfo(info.name, info.surname, info.dob);
        addPlayerDetails(info.username, info.password);

        return String.format("Username: %s%nPassword: %s%nName: %s%nSurname: %s%nDate of Birth: %s", info.username, info.password, info.name, info.surname, info.dob);
    }

    private String generatePassword(String name, String surname, String dob) {
        return surname.toLowerCase().substring(3, 6) + name.toUpperCase().substring(0, 2) + dob.substring(6) + surname.toUpperCase().substring(0, 2) + name.toLowerCase().substring(3, 7);
    }

    public static void addUserCredentialInfo (String name, String surname, String dob) {//Add user information to the file
        try {
            FileWriter fileWriter = new FileWriter(PLAYER_DETAILS_FILE_PATH, true);
            fileWriter.write(name + "\n" + surname + "\n" + dob + "\n\n");
            fileWriter.close();
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    public static void addPlayerDetails (String username, String password){//Add player details to the file
        try {
            FileWriter fileWriter = new FileWriter(USER_CREDENTIAL_INFO_FILE_PATH, true);
            fileWriter.write(username + ": " + password + "\n");
            fileWriter.close();
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }
}