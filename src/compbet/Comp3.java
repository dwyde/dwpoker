package compbet;

import java.util.ArrayList;

import dwpoker.Card;


public class Comp3 extends Comp2 {
	public int bet(ArrayList<Card> hand, int toCall, int street, int pot) {
		double score = 0.0;
		if (street > PREFLOP) {
			score = super.odds(hand, toCall, street, pot);
		}
		
		else {
			score = super.bet(hand, toCall, street, pot) == COMP_FOLD ? COMP_FOLD : COMP_RAISE;
		}
		
		double potOdds = (toCall * 1.0) / pot;
		//System.out.println("score3: " + score);
		//System.out.println(toCall + " " + pot);
		//System.out.println("pot odds3: " + potOdds);
		
		if (score >= potOdds) {
			return COMP_RAISE;
		}
		
		else {
			return COMP_FOLD;
		}
	}
}
