package Game;

import java.util.*;
public class Gameplay {
    public static class Cards {
        public enum Suit {
            Clover, Diamond, Heart, Spade
        }

        public enum Rank {
            Ace, Two, Three, Four, Five, Six, Seven, Eight, Nine, Ten, Jack, Queen, King, Joker
        }

        public record PlayingCard(Suit suit, Rank rank) {
            @Override
            public String toString() {
                return rank + "of" + suit;
            }
        }

        public static class Deck {
            public PlayingCard dealCards;
            private List<Cards.PlayingCard> cards;

            public Deck() {
                startDeck();
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
                }//re-start the deck if empty
                return cards.remove(0);
            }
        }

        public static class AI {
            private final List<Cards.PlayingCard> hand;

            public AI() {
                this.hand = new ArrayList<>();
                drawStarterCard();
            }

            private void drawStarterCard() {
                for (int i = 0; i < 4; i++) {
                    drawCard();
                }
            }

            public void drawCard() {
                Cards.PlayingCard card = GameDeck.getInstance().dealCards;
                hand.add(card);
            }

            public void displayHand() {
                System.out.print("Butler's cards are:   ");
                {
                    for (Cards.PlayingCard card : hand) {
                        System.out.print(card);
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

            public static Cards.Deck getInstance() {
                return instance;
            }
        }

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            Player player = new Player();
            AI butler = new AI();
            while (player.getLives() > 0 && player.getTotalScore() > 0) {
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
                if (player.getTotalScore() > 25 || GameDeck.getInstance().dealCard().rank.ordinal() <= Cards.Rank.Joker.ordinal()) {
                    System.out.println("Player wins!");
                    player.increaseTotalWins();
                    System.out.println("Player wins: " + player.getTotalWins());
                    System.out.println("Player losses: " + player.getTotalLosses());
                    System.out.println("Player lives: " + player.getLives());
                    break;
                }
                if (player.getTotalScore() > 25 || GameDeck.getInstance().dealCard().rank.ordinal() <= Cards.Rank.Joker.ordinal()) {
                    System.out.print("Game over!");
                    break;
                }
            }
            //Display final score
            System.out.println("~~~~~~~Final Scores~~~~~~");
            player.displayHand();
            butler.displayHand();
            System.out.print("Your score :" + player.getTotalScore());
            System.out.print("Your wins :" + player.getTotalWins());
            System.out.print("Your losses :" + player.getTotalLosses());
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
    }
}