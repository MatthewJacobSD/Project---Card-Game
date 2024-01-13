package Game;

import Archive.Scores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Gameplay {
    public static class Cards {
        public enum Suit {//enumerate the different suits
            Clover, Diamond, Heart, Spade
        }

        public enum Rank {//enumerate the different ranks/classes
            Ace, Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Jack, Queen, King, Joker
        }

        public record PlayingCard(Suit suit, Rank rank) {
            @Override//catch potential errors and notifies you when it happens
            public String toString() {
                return rank + "of" + suit;
            }
        }

        public static class Deck {//inside deck
            public PlayingCard dealCards;
            private List<Cards.PlayingCard> cards;

            public Deck() {
                startDeck();//start the deck
            }

            private void startDeck() {
                cards = new ArrayList<>();
                for (Suit suit : Suit.values()) {//loop through all the suits
                    for (Rank rank : Rank.values()) {//loop through all the ranks
                        cards.add(new Cards.PlayingCard(suit, rank));//add the card to the deck
                    }
                }
                shuffle();
            }

            public void shuffle() {
                Collections.shuffle(cards);//shuffle the deck
            }

            public Cards.PlayingCard dealCard() {
                if (cards.isEmpty()) {
                    startDeck();
                }//re-start the deck if empty
                return cards.remove(0);
            }

            public PlayingCard[] deckScore() {//create a score of the deck
                PlayingCard[] score = new PlayingCard[13];

                for (int i = 0; i < 13; i++) {
                    score[i] = dealCard();
                }
                return score;
            }
        }

        public static class AI {//inside the AI aka butler
            private final List<Cards.PlayingCard> hand;

            public AI() {
                this.hand = new ArrayList<>();//create a new hand
                drawStarterCard();
            }

            private void drawStarterCard() {
                for (int i = 0; i < 4; i++) {
                    drawCard();
                }
            }

            public void drawCard() {
                Cards.PlayingCard card = GameDeck.getInstance().dealCards;//deal a card from the deck
                hand.add(card);//add the card to the hand
            }

            public void displayHand() {
                System.out.print("Butler's cards are:   ");
                {
                    for (Cards.PlayingCard card : hand) {//loop through the hand
                        System.out.print(card + "  ");
                    }
                }
            }

            public List<Cards.PlayingCard> getHand() {
                return hand;
            }
        }

        public static class GameDeck {
            private static final Cards.Deck instance = new Cards.Deck();

            private GameDeck() {
            }

            public static Cards.Deck getInstance() {//create a new instance of the deck
                return instance;
            }
        }
        private static int calculateRoundScore(Cards.PlayingCard playerCard, Cards.PlayingCard butlerCard) {
            int playerValue = getValueScore(playerCard.rank());
            int butlerValue = getValueScore(butlerCard.rank());
            if (playerValue + butlerValue <= 25) {
                System.out.print("Correct guess! You won 10 points.");
                return +10;
            } else {
                System.out.print("Wrong guess! You lost 10 points.");
                return -10;
            }
        }

        private static int getValueScore(Cards.Rank rank) {
            return switch (rank) {
                case Ace -> 1;
                case Two, Three, Four, Five -> rank.ordinal() + 1;//Value 2 to 5 have corresponding scores
                default -> 10;//For other values such us Joker, Jack, Queen, King
            };
        }

        private static int calculateDeckScore(PlayingCard[] playingCards) {//Calculate the score of the deck
            int deckScore = 0;
            for (Cards.PlayingCard card : GameDeck.getInstance().deckScore()) {//loop through the deck
                deckScore += getValueScore(card.rank());
            }
            return deckScore / playingCards.length;
        }

        private static int calculateHandScore(List<Cards.PlayingCard> hand) {//Calculate the score of the hand
            int handScore = 0;
            for (Cards.PlayingCard card : hand) {
                handScore += getValueScore(card.rank());
            }
            return handScore;
        }

        public static void main(String[] args){
            Scanner scanner = new Scanner(System.in);
            Player player = new Player();
            AI butler = new AI();

            //Pass the player to the score class to access its details
            Scores.setPlayer(player);

            while (player.getLives() >= 0 && player.getTotalScore() > 0) {
                player.displayHand();
                butler.displayHand();

                //Player turn
                System.out.print("Choose a card by entering a number from 0 to 3: ");
                int playerChoice = scanner.nextInt();
                if (playerChoice < 0 || playerChoice > 3) {
                    System.out.println("Invalid choice. Please try again.");
                    continue;
                }
                Cards.PlayingCard playerCard = player.getHand().get(playerChoice);
                System.out.println("You chose: " + playerCard);
                player.getHand().remove(playerChoice);

                //AI turn
                Cards.PlayingCard butlerCard = butler.getHand().remove(0);
                System.out.println("Butler chose: " + butlerCard);

                //Update scores
                int roundScore = calculateRoundScore(playerCard, butlerCard);
                player.setTotalScore(player.getTotalScore() + roundScore);

                //Check for end conditions
                Cards.PlayingCard nextCard = GameDeck.getInstance().dealCard();
                if (player.getTotalScore() > 25 || nextCard.rank == Cards.Rank.Joker) {
                    System.out.print("Game over!");
                    player.decreaseLives();
                    System.out.println("You have " + player.getLives() + " lives left.");
                    player.increaseTotalLosses();
                    System.out.println("You have " + player.getTotalLosses() + " total losses.");
                    Scores.addScoretoLeaderboard();
                    continue;
                } else
                    nextCard = GameDeck.getInstance().dealCard();
                int calculateDeckScore = calculateDeckScore(GameDeck.getInstance().deckScore());
                if (butler.getHand().isEmpty()) {
                    System.out.print("You won!, the butler has no cards left.");
                    player.increaseTotalWins();
                    System.out.println("You have " + player.getTotalWins() + " total wins.");
                    Scores.addScoretoLeaderboard();
                    continue;
                } else if (player.getTotalScore() < 25 && calculateHandScore(butler.hand) > 25 || calculateDeckScore <= 15) {
                    System.out.print(player.getUsername() + " won!, the butler has a value greater then 25"); //or the deck has a value below 15");
                    player.increaseTotalWins();
                    Scores.addScoretoLeaderboard();
                    continue;
                } else {
                    System.out.println("Next card: " + nextCard);
                }
                break;
            }
            //Display final score
            System.out.println("~~~~~~~Final Scores~~~~~~");
            player.displayHand();
            butler.displayHand();
            Scores.displayLeaderboard();
        }

    }

}