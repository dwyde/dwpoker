package compbet;

import java.util.ArrayList;

import dwpoker.Card;


public class Comp4 extends Comp2 {
	public int bet(ArrayList<Card> hand, int toCall, int street, int pot) {
		double score = 0.0;
		if (street > PREFLOP) {
			score = super.odds(hand, toCall, street, pot);
		}
		
		else {
			//score = super.bet(hand, toCall, street, pot) == COMP_FOLD ? COMP_FOLD : COMP_RAISE;
			int unique = lookupUnique(hand);
			int rank = handRanking(unique);
			return sklansky(rank);
		}
		
		double potOdds = (toCall * 1.0) / pot;
		//System.out.println("score4: " + score);
		//System.out.println(toCall + " " + pot);
		//System.out.println("pot odds4: " + potOdds);
		
		if (score >= potOdds) {
			return COMP_RAISE;
		}
		
		else {
			return COMP_FOLD;
		}
	}
	
	public int sklansky(int ranking) {
		if (ranking > 0 && ranking <= 3) {
			return probabilityTriple(85,25,0);
		}
		
		else if (ranking > 0) {
			return probabilityTriple(50, 25, 25);
		}
		
		else {
			return probabilityTriple(10, 20, 70);
		}
	}
}
