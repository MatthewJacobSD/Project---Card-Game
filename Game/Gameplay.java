package Game;

import Archive.Profile;
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
                return rank + " of " + suit;
            }
        }

        public static class Deck {
            private List<Cards.PlayingCard> cards;

            public Deck() {
                startDeck();
            }

            public static Deck getInstance() {
                return new Deck();
            }

            public PlayingCard[] dealCards() {
                PlayingCard[] hand = new PlayingCard[4];
                for (int i = 0; i < 4; i++) {
                    hand[i] = dealCard();
                }
                return hand;
            }

            private void startDeck() {
                cards = new ArrayList<>();
                for (Suit suit : Suit.values()) {
                    for (Rank rank : Rank.values()) {
                        cards.add(new Cards.PlayingCard(suit, rank));
                    }
                }
                shuffle();
            }

            public void shuffle() {
                Collections.shuffle(cards);
            }

            public Cards.PlayingCard dealCard() {
                if (cards.isEmpty()) {
                    startDeck();
                }
                return cards.remove(0);
            }

        }

        public static class AI {
            private final List<Gameplay.Cards.PlayingCard> hand;

            public AI() {
                this.hand = new ArrayList<>();
                drawStarterCard();
            }

            private void drawStarterCard() {
                PlayingCard[] starterHand = Gameplay.Cards.Deck.getInstance().dealCards();
                Collections.addAll(hand, starterHand);
            }

            public void displayHand() {
                System.out.print("Butler's cards are:   ");
                for (Gameplay.Cards.PlayingCard card : hand) {
                    System.out.print(card + "  ");
                }
                System.out.println();
            }

            public List<Gameplay.Cards.PlayingCard> getHand() {
                return hand;
            }
        }

        public static class GameDeck {
            private static final Cards.Deck instance = new Cards.Deck();

            private GameDeck() {
            }

            public static Cards.Deck getInstance() {
                return instance;
            }
        }

        private static int calculateRoundScore(Cards.PlayingCard playerCard, Cards.PlayingCard butlerCard) {
            int playerValue = getValueScore(playerCard.rank());
            int butlerValue = getValueScore(butlerCard.rank());
            if (playerValue + butlerValue <= 25) {
                System.out.println("Correct guess! You won 10 points.");
                return 10;
            } else {
                System.out.println("Wrong guess! You lost 10 points.");
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

        private static int calculateDeckScore(PlayingCard[] playingCards) {
            int deckScore = 0;
            for (Cards.PlayingCard card : playingCards) {
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

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            Profile profile = new Profile();
            String username = profile.User_profile();
            Player player = new Player(username);
            AI butler = new AI();

            Scores.setPlayer(player);

            while (player.getLives() > 0 && player.getTotalScore() >= 0) {
                player.displayHand();
                butler.displayHand();

                System.out.print("Choose a card by entering a number from 0 to 3: ");
                int playerChoice = scanner.nextInt();
                if (playerChoice < 0 || playerChoice > 3) {
                    System.out.println("Invalid choice. Please try again.");
                    continue;
                }
                Gameplay.Cards.PlayingCard playerCard = player.getHand().get(playerChoice);
                System.out.println("You chose: " + playerCard);
                player.getHand().remove(playerChoice);

                Gameplay.Cards.PlayingCard butlerCard = butler.getHand().remove(0);
                System.out.println("Butler chose: " + butlerCard);

                int roundScore = calculateRoundScore(playerCard, butlerCard);
                player.setTotalScore(player.getTotalScore() + roundScore);

                if (player.getTotalScore() > 25) {
                    System.out.println("Game over!");
                    player.decreaseLives();
                    System.out.println("You have " + player.getLives() + " lives left.");
                    player.increaseTotalLosses();
                    System.out.println("You have " + player.getTotalLosses() + " total losses.");
                    Scores.addScoretoLeaderboard();
                    continue;
                } else {
                    Gameplay.Cards.PlayingCard nextCard = Gameplay.Cards.Deck.getInstance().dealCard();
                    System.out.println("Next card: " + nextCard);
                }

                if (butler.getHand().isEmpty()) {
                    System.out.println("You won! The butler has no cards left.");
                    player.increaseTotalWins();
                    System.out.println("You have " + player.getTotalWins() + " total wins.");
                    Scores.addScoretoLeaderboard();
                    continue;
                } else if (player.getTotalScore() < 25 && calculateHandScore(butler.getHand()) > 25) {
                    System.out.println(Player.getUsername() + " won! The butler has a value greater than 25");
                    player.increaseTotalWins();
                    Scores.addScoretoLeaderboard();
                    continue;
                }

                break;
            }
            Deck deck = Deck.getInstance();
            PlayingCard[] playingCards = deck.dealCards();
            int deckScore = calculateDeckScore(playingCards);
            System.out.println("Deck score: " + deckScore);
            //Display final score
            System.out.println("~~~~~~~Final Scores~~~~~~");
            player.displayHand();
            butler.displayHand();
            Scores.displayLeaderboard();
        }
    }
}