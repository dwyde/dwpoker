package game.simulation;
import java.util.ArrayList;

import dwpoker.Card;


public class CompBet {
	public CompBet() {		
	}
	
	public int bet(ArrayList<Card> hand, int toCall, int street) {
		int random = (int)(Math.random() * 10);
		if (random < 1) {
			return -1;
		}
		else if (random < 3) {
			return toCall + 2;
		}
		else {
			return toCall;
		}
	}
}
