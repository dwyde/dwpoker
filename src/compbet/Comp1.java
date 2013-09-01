package compbet;

import java.util.ArrayList;

import dwpoker.Card;


public class Comp1 extends CompBet {
	public Comp1() {
	}
	
	public int bet(ArrayList<Card> hand, int toCall, int street, int pot) {
		int unique = lookupUnique(hand);
		int rank = handRanking(unique);
		//rank += numBets(toCall, street) / 2; // adjust for previous raises
		//rank += PLAYERS / 3.5; // adjust for more players
		return sklansky(rank);
	}
	
	public int sklansky(int ranking) {
		switch (ranking) {
			case 1:
				return probabilityTriple(80, 20, 0);
			case 2:
				return probabilityTriple(70, 25, 5);
			case 3:
				return probabilityTriple(50, 40, 10);
			case 4:
				return probabilityTriple(30, 50, 20);
			case 5:
				return probabilityTriple(30, 50, 20);
			case 6:
				return probabilityTriple(25, 50, 25);
			case 7:
				return probabilityTriple(20, 45, 35);
			case 8:
				return probabilityTriple(15, 40, 45);
			default:
				return probabilityTriple(5, 5, 90);
		}
	}
}
