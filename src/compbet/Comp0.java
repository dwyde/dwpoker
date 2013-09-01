package compbet;

import java.util.ArrayList;

import dwpoker.Card;


public class Comp0 extends CompBet {
	public Comp0() {
	}
	
	public int bet(ArrayList<Card> hand, int toCall, int street, int pot) {
		return COMP_CALL;
		/*int rand = (int) (Math.random() * 10);
		
		if (rand < 1) {
			return COMP_FOLD;
		}
		
		else if (rand < 3) {
			return COMP_RAISE;
		}
		
		else {
			return COMP_CALL;
		}*/
	}
}
