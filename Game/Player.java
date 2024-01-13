package Game;
import java.util.*;
import Archive.*;
public class Player {
    private final List<Gameplay.Cards.PlayingCard> hand;
    private int totalScore;
    private int totalWins;
    private int totalLosses;
    private int lives;

    public Player() {
        this.hand = new ArrayList<>();
        this.totalScore = 0;
        this.totalWins = 0;
        this.totalLosses = 0;
        this.lives = 3;

        drawStarterCard();
    }

    private void drawStarterCard() {
        for (int i = 0; i < 4; i++) {
            drawCard();
        }
    }

    public void drawCard() {
        Gameplay.Cards.PlayingCard card = Gameplay.Cards.GameDeck.getInstance().dealCards;//deal a card from the deck
        hand.add(card);
    }

    public void displayHand() {
        System.out.print("Your cards are:   ");
        for (Gameplay.Cards.PlayingCard card : hand) {
            System.out.print(card + "  ");
        }
        System.out.print("Your current score is:   " + totalScore);
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public void increaseTotalWins() {
        this.totalWins++;
    }

    public int getTotalLosses() {
        return totalLosses;
    }

    public void increaseTotalLosses() {
        this.totalLosses++;
    }

    public int getLives() {
        return lives;
    }

    public void decreaseLives() {
        this.lives--;
    }

    public List<Gameplay.Cards.PlayingCard> getHand() {//get the hand from the player
        return hand;
    }

    public String getUsername() {//get the username from the profile
        Profile profile = new Profile();
        return profile.getUsername();
    }
}

