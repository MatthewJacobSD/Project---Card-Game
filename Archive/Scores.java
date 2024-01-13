package Archive;

import java.io.FileWriter;
import java.io.IOException;

public class Scores {
    static final String LEADERBOARD_SCORE_FILE_PATH = "Leaderboard.txt";
    private static class leaderboard_info {
        Profile.Profile_Info_Structure player_info = new Profile.Profile_Info_Structure();
        int lives;
        int score;
        int wins;
        int losses;
    }
    public static void getUserScore(String username) {
        try {
            FileWriter fileWriter = new FileWriter(LEADERBOARD_SCORE_FILE_PATH);
            fileWriter.write(username + ": " + 100 + "\n");
            fileWriter.close();
        } catch(IOException e) {
            e.fillInStackTrace();
        }
    }
    public static void addScoretoLeaderboard(String username, String score) {
        try {
            FileWriter fileWriter = new FileWriter(LEADERBOARD_SCORE_FILE_PATH, true);
            fileWriter.write(username + ": " + score + "\n");
            fileWriter.close();
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

}
