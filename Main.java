import Archive.Profile;
import Game.Gameplay;

public class Main {
    public static void main(String[] args) {
        Profile profile = new Profile();//Access the Profile class
        String username = profile.User_profile();

        //Welcome the user
        System.out.println("Welcome to the game, " + username + "!");

        //Now the game begins!
        Gameplay.Cards.main(args);
    }
}