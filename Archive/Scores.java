package Archive;

import Game.Player;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scores {
    static final String LEADERBOARD_SCORE_FILE_PATH = "Leaderboard.txt";
    private static Player player;

    public static void setPlayer(Player p) {//set the player
        player = p;
    }

    public static void addScoretoLeaderboard() {//add the score to the leaderboard
        try {
            FileWriter fileWriter = new FileWriter(LEADERBOARD_SCORE_FILE_PATH, true);
            fileWriter.write(player.getUsername() + ": " + player.getTotalScore() + "\n");
            fileWriter.close();
        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }

    public static void displayLeaderboard() {//display the leaderboard
        List<String> scores = readScoresFromFile();
        System.out.println("Leaderboard:");
        for (String score : scores) {
            System.out.println(score);
        }
    }

    private static List<String> readScoresFromFile() {//read the scores from the leaderboard
        List<String> scores = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(LEADERBOARD_SCORE_FILE_PATH));
            String line;
            while ((line = reader.readLine()) != null) {
                scores.add(line);
            }
        } catch (IOException e) {
            e.fillInStackTrace();
        }
        return scores;
    }
}
