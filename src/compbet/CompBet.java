package compbet;


import java.util.ArrayList;

import dwpoker.Card;
import dwpoker.Constants;

public abstract class CompBet implements Constants {
	private final static int HAND_GROUPS = 8;
	
	// A random number to resolve probability triples
	protected int random;
	
	public CompBet() {
	}

	public abstract int bet(ArrayList<Card> hand, int toCall, int street, int pot);
	
	protected boolean isSuited(ArrayList<Card> hand) {
		if (hand.get(0).getSuit() == hand.get(1).getSuit()) {
			return true;
		}
		
		else {
			return false;
		}
	}
	
	// helper method
	protected int lookupUnique(ArrayList<Card> hand) {
		return lookupUnique(hand.get(0).getValue(), hand.get(1).getValue(), isSuited(hand));
	}
	
	protected int lookupUnique(int value1, int value2, boolean isSuited) {
		int[][][] unique = {
			{{0}, {1, 2}, {3, 4}, {5, 6}, {7, 8}, {9, 10}, {11, 12}, {13, 14}, {15, 16}, {17, 18}, {19, 20}, {21, 22}, {23, 24}}, 
			{{1, 2}, {25}, {26, 27}, {28, 29}, {30, 31}, {32, 33}, {34, 35}, {36, 37}, {38, 39}, {40, 41}, {42, 43}, {44, 45}, {46, 47}}, 
			{{3, 4}, {26, 27}, {48}, {49, 50}, {51, 52}, {53, 54}, {55, 56}, {57, 58}, {59, 60}, {61, 62}, {63, 64}, {65, 66}, {67, 68}}, 
			{{5, 6}, {28, 29}, {49, 50}, {69}, {70, 71}, {72, 73}, {74, 75}, {76, 77}, {78, 79}, {80, 81}, {82, 83}, {84, 85}, {86, 87}}, 
			{{7, 8}, {30, 31}, {51, 52}, {70, 71}, {88}, {89, 90}, {91, 92}, {93, 94}, {95, 96}, {97, 98}, {99, 100}, {101, 102}, {103, 104}}, 
			{{9, 10}, {32, 33}, {53, 54}, {72, 73}, {89, 90}, {105}, {106, 107}, {108, 109}, {110, 111}, {112, 113}, {114, 115}, {116, 117}, {118, 119}}, 
			{{11, 12}, {34, 35}, {55, 56}, {74, 75}, {91, 92}, {106, 107}, {120}, {121, 122}, {123, 124}, {125, 126}, {127, 128}, {129, 130}, {131, 132}}, 
			{{13, 14}, {36, 37}, {57, 58}, {76, 77}, {93, 94}, {108, 109}, {121, 122}, {133}, {134, 135}, {136, 137}, {138, 139}, {140, 141}, {142, 143}}, 
			{{15, 16}, {38, 39}, {59, 60}, {78, 79}, {95, 96}, {110, 111}, {123, 124}, {134, 135}, {144}, {145, 146}, {147, 148}, {149, 150}, {151, 152}}, 
			{{17, 18}, {40, 41}, {61, 62}, {80, 81}, {97, 98}, {112, 113}, {125, 126}, {136, 137}, {145, 146}, {153}, {154, 155}, {156, 157}, {158, 159}}, 
			{{19, 20}, {42, 43}, {63, 64}, {82, 83}, {99, 100}, {114, 115}, {127, 128}, {138, 139}, {147, 148}, {154, 155}, {160}, {161, 162}, {163, 164}}, 
			{{21, 22}, {44, 45}, {65, 66}, {84, 85}, {101, 102}, {116, 117}, {129, 130}, {140, 141}, {149, 150}, {156, 157}, {161, 162}, {165}, {166, 167}}, 
			{{23, 24}, {46, 47}, {67, 68}, {86, 87}, {103, 104}, {118, 119}, {131, 132}, {142, 143}, {151, 152}, {158, 159}, {163, 164}, {166, 167}, {168}} 
		};
		
		int suited = isSuited ? 1 : 0; // 1 = suited, 0 = unsuited
		
		if (value1 == value2 && isSuited) {
			System.out.println("Exiting after call for a suited pocket pair: " + value1 + " " + value2);
			System.exit(EXIT_FAILURE);
		}
		
		return unique[value1][value2][suited];
	}
	
	public int handRanking(int index) {
		int[] values = {
			7, 0, 8, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 
			0, 0, 0, 0, 0, 0, 0, 0, 0, 7, 0, 5, 7, 
			0, 7, 0, 7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
			0, 0, 0, 0, 0, 0, 7, 0, 5, 7, 8, 6, 0, 
			7, 0, 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
			0, 7, 0, 5, 6, 8, 5, 0, 6, 0, 8, 0, 0, 
			0, 0, 0, 0, 0, 0, 0, 7, 0, 5, 6, 8, 5, 
			0, 6, 0, 8, 0, 0, 0, 0, 0, 0, 0, 7, 0, 
			5, 5, 8, 5, 0, 5, 0, 7, 0, 8, 0, 0, 0, 
			7, 0, 5, 4, 7, 4, 8, 5, 8, 6, 0, 7, 0, 
			7, 0, 5, 3, 7, 4, 7, 4, 8, 5, 8, 6, 8, 
			0, 2, 5, 3, 6, 4, 6, 4, 6, 3, 1, 5, 3, 
			5, 3, 4, 2, 1, 4, 2, 3, 2, 1, 2, 1, 1, 
		};
		
		if (values[index] == 0) {
			return HAND_GROUPS + 1;
		}
		
		else {
			return values[index];
		}
	}
	
	protected int probabilityTriple(double bet, double call, double fold) {
		if (random < bet) {
			return COMP_RAISE;
		}
		
		else if (random < bet + call) {
			return COMP_CALL;
		}
		
		else {
			return COMP_FOLD;
		}
	}
	
	protected int numBets(int toCall, int street) {
		int currentBet = street <= FLOP ? SMALL_BET : BIG_BET;
		return toCall / currentBet;
	}
	
	protected void setRandom(int random) {
		this.random = random;
	}
}
