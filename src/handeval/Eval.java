package handeval;
import java.util.ArrayList;

import dwpoker.*;

public class Eval implements Constants {
	public Eval() {
	}
	
	public Result evaluate(ArrayList<Card> hand) {
		Result result = new Result();
		HasHand hasHand = new HasHand();
		int[] counts = hasHand.countVals(hand);
		
		result = hasHand.hasStraightFlush(hand);
		if (result.getScore() == STRAIGHT_FLUSH) {
			return result;
		}
		
		result = hasHand.hasQuads(counts);
		if (result.getScore() == FOUR_OF_A_KIND) {
			return result;
		}
		
		result = hasHand.hasFullHouse(counts);
		if (result.getScore() == FULL_HOUSE) {
			return result;
		}
		
		result = hasHand.hasFlush(hand);
		if (result.getScore() == FLUSH) {
			return result;
		}
		
		result = hasHand.hasStraight(counts);
		if (result.getScore() == STRAIGHT) {
			return result;
		}
		
		result = hasHand.hasTrips(counts);
		if (result.getScore() == THREE_OF_A_KIND) {
			return result;
		}
		
		result = hasHand.hasTwoPair(counts);
		if (result.getScore() == TWO_PAIR) {
			return result;
		}
		
		result = hasHand.hasPair(counts);
		if (result.getScore() == PAIR) {
			return result;
		}
		
		result = hasHand.hasHighCard(counts);
		if (result.getScore() == HIGH_CARD) {
			return result;
		}
		
		System.out.println("Unknown hand score: " + result.getScore());
		System.exit(EXIT_FAILURE);
		return null;
	}
	
	public StringBuffer resultString(Result result) {
		StringBuffer strbuf = new StringBuffer();
		
		switch (result.getScore()) {
			case STRAIGHT_FLUSH:
				strbuf.append("Straight flush in " + outputSuit(result.getSuit()) + ", " +
						outputValue(result.getValue(0)) + " high");
				return strbuf;
			case FOUR_OF_A_KIND:
				strbuf.append("Four of a kind, " + outputValue(result.getValue(0)) + "'s");
				return strbuf;
			case FULL_HOUSE:
				strbuf.append("Full house, " + outputValue(result.getValue(0)) + "'s full of " +
						outputValue(result.getValue(1)) + "'s");
				return strbuf;
			case FLUSH:
				strbuf.append("Flush in " + outputSuit(result.getSuit()) + ", " +
						outputValue(result.getValue(0)) + " high");
				return strbuf;
			case STRAIGHT:
				strbuf.append("Straight, " + outputValue(result.getValue(0)) + " high");
				return strbuf;
			case THREE_OF_A_KIND:
				strbuf.append("Three of a kind, " + outputValue(result.getValue(0)) + "'s");
				return strbuf;
			case TWO_PAIR:
				strbuf.append("Two pair, " + outputValue(result.getValue(0)) + "'s and " +
						outputValue(result.getValue(1)) + "'s");
				return strbuf;
			case PAIR:
				strbuf.append("Pair of " + outputValue(result.getValue(0)) + "'s");
				return strbuf;
			case HIGH_CARD:
				strbuf.append("High card:");
				for (int i = 0; i < 5; i++) { // five cards to a poker hand
					strbuf.append(" " + outputValue(result.getValue(i)));
				}
				return strbuf;
			default:
				System.out.println("Unknown hand score: " + result.getScore());
				System.exit(EXIT_FAILURE);
		}
		
		return strbuf;
	}

	String outputSuit(int suit) {
		switch (suit) {
			case HEARTS:
				return "Hearts";
			case CLUBS:
				return "Clubs";
			case DIAMONDS:
				return "Diamonds";
			case SPADES:
				return "Spades";
			default:
				System.out.println("Unknown suit: " + suit);
				System.exit(EXIT_FAILURE);
				return "Oops";
		}
	}
	
	char outputValue(int value) {
		if (value < TEN) {
			return (char)(value + OFFSET + '0');
		} 
		
		else {
			switch (value) {
				case 8:
					return 'T';
				case 9:
					return 'J';
				case 10:
					return 'Q';
				case 11:
					return 'K';
				case 12:
					return 'A';
				default:
					System.out.println("Illegal card value: " + value);
					System.exit(EXIT_FAILURE);
					return '?';
			}
		}
	}
	
	public ArrayList<Integer> findWinners(Result[] results, ArrayList<Integer> currentPlayers) {
		ArrayList<Integer> winners = new ArrayList<Integer>();
		int better = currentPlayers.get(0);
		winners.add(better);
		
		for (int i = currentPlayers.get(1); i < PLAYERS; i++) { // hand comparison loop
			if (results[i] == null) {
				continue;
			}
			
			better = better_hand(results[i], results[winners.get(0)], i, winners.get(0));
			if (better == i) {
				winners = new ArrayList<Integer>();
				winners.add(i);
			}

			else if (better == winners.get(0)) {} // do nothing

			else if (better == EVAL_TIE) {
				winners.add(i);
			}

			else {
				System.out.printf("Unknown hand result: %d\n", better);
				System.exit(EXIT_FAILURE);
			}
		}
		
		return winners;
	}

	int better_hand(Result player1, Result player2, int p1_number, int p2_number) {
		int i;

		if (player1.getScore() > player2.getScore()) {
			return p1_number; // player1 wins
		}

		else if (player1.getScore() < player2.getScore()) {
			return p2_number; // player2 wins
		}

		else {
			for (i = 0; i < player1.getNumValues(); i++) {
				if (player1.getValue(i) > player2.getValue(i)) {
					return p1_number;
				}
				else if (player1.getValue(i) < player2.getValue(i)) {
					return p2_number;
				}
			}

			if (player1.getScore() == PAIR || player1.getScore() == TWO_PAIR || 
					player1.getScore() == THREE_OF_A_KIND || player1.getScore() == FOUR_OF_A_KIND) {
				for (i = 0; i < player1.getNumKickers(); i++) {
					if (player1.getKicker(i) > player2.getKicker(i)) {
						return p1_number;
					}
					else if (player1.getKicker(i) < player2.getKicker(i)) {
						return p2_number;
					}
				}
			}
		}

		return EVAL_TIE; // it's a tie
	}
}