package Archive;

import java.util.*;
import java.io.FileWriter;
import java.io.IOException;

public class Profile {
    //file paths
    static final String USER_CREDENTIAL_INFO_FILE_PATH = "User_CredentialInfo.txt";
    static final String PLAYER_DETAILS_FILE_PATH = "Player_Details.txt";

    //Basic structure for user profile info
    static class Profile_Info_Structure {
        String name = "";
        String surname = "";
        String username = "";
        String password = "";
        String dob = "";
    }

    public String User_profile() {
        Scanner input = new Scanner(System.in);
        Profile_Info_Structure info = new Profile_Info_Structure();

        //check if the user already exists
        if (userExists(info.username) || (userExists(info.password))) {
            System.out.println("Welcome back " + info.username + "!");
        }
        //otherwise, create a new user
        while (info.username.isEmpty()) {
            System.out.print("Username cannot be empty.\nEnter your username: ");
            info.username = input.nextLine();
        }
        while (info.name.isEmpty()) {
            System.out.print("Name cannot be empty.\nEnter your name:   ");
            info.name = input.nextLine();
        }

        while (info.surname.isEmpty()) {
            System.out.print("Surname cannot be empty.\nEnter your surname:   ");
            info.surname = input.nextLine();
        }

        while (info.dob.isEmpty()) {
            System.out.print("Date of birth cannot be empty.\nEnter your date of birth (dd/mm/yyyy): ");
            info.dob = input.nextLine();
        }

        //generate password
        info.password = generatePassword(info.name, info.surname, info.dob);

        // Add user information to the files
        addUserCredentialInfo(info.name, info.surname, info.dob);
        addPlayerDetails(info.username, info.password);

        return info.username;
    }

    private boolean userExists(String username) {
        Scanner file = new Scanner(USER_CREDENTIAL_INFO_FILE_PATH);
        while (file.hasNextLine()) {
            String line = file.nextLine();
            if (line.startsWith(username + ":")) {
                file.close();
                return true;
            }
        }
        file.close();
        return false;
    }

    public String getUsername(){
        return User_profile();
    }

    private String generatePassword(String name, String surname, String dob) {
        return surname.toLowerCase().substring(3, 6) + name.toUpperCase().substring(0, 2) + dob.substring(6) + surname.toUpperCase().substring(0, 2) + name.toLowerCase().substring(3, 7);
    }

    public static void addUserCredentialInfo(String name, String surname, String dob) {//Add user information to the file
        try {
            FileWriter fileWriter = new FileWriter(PLAYER_DETAILS_FILE_PATH, true);
            fileWriter.write(name + "\n" + surname + "\n" + dob + "\n\n");
            fileWriter.close();
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    public static void addPlayerDetails(String username, String password) {//Add player details to the file
        try {
            FileWriter fileWriter = new FileWriter(USER_CREDENTIAL_INFO_FILE_PATH, true);
            fileWriter.write(username + ": " + password + "\n");
            fileWriter.close();
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }
}