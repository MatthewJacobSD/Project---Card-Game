package Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    private final List<Gameplay.Cards.PlayingCard> hand;
    private int totalScore;
    private int lives;
    private int totalWins;
    private int totalLosses;
    private static String username = "";
    private final Gameplay.Cards.Deck deck;

    public Player(String username) {
        this.hand = new ArrayList<>();
        this.totalScore = 0;
        this.lives = 5;
        this.totalWins = 0;
        this.totalLosses = 0;
        Player.username = username;
        this.deck = Gameplay.Cards.GameDeck.getInstance(); // Initialize the deck
        drawStarterCard();
    }

    private void drawStarterCard() {
        Gameplay.Cards.PlayingCard[] starterHand = deck.dealCards();
        Collections.addAll(hand, starterHand);
    }

    public void displayHand() {
        System.out.print("Your cards are:   ");
        for (Gameplay.Cards.PlayingCard card : hand) {
            System.out.print(card + "  ");
        }
        System.out.println();
    }

    public List<Gameplay.Cards.PlayingCard> getHand() {
        return hand;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getLives() {
        return lives;
    }

    public void decreaseLives() {
        this.lives--;
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

    public static String getUsername() {
        return username;
    }
}