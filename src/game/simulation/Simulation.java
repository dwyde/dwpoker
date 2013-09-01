package game.simulation;

import game.AbstractBet;
import game.Betting;
import game.Poker;
import handeval.Eval;
import handeval.Result;

import java.util.ArrayList;

import compbet.Bots;
import compbet.CompBet;


public class Simulation extends AbstractBet {
	Betting betting;
	Poker poker;
	
	public Simulation() {
	}
	
	public Simulation(Betting betting, Poker poker) {
		this.betting = betting;
		this.poker = poker;
	}
	
	public void go() {
		for (int street = 0; street < END; street++) {
			int next = firstToBet(currentPlayers, button);
			
			for (int i = next;
			getNumCalls() < numCurrentPlayers() && i != getRaiser();
			i = nextPlayer(i, getCurrentPlayers())) {	
				if (isCurrentPlayer(i)) {
					processCompBet(i);
					if (oneLeft()) {
						return;
					}
				}
			}
			
			if (betting.readyToDeal()) {
				// If there are no more rounds of betting, end the game
				if (poker.getStreet() == RIVER) {
	        		Result[] results = new Result[PLAYERS];
	        		Eval evaluation = new Eval();
	        		for (int i = 0; i < betting.numCurrentPlayers(); i++) {
	        			int current = getCurrentPlayer(i);
	        			results[current] = evaluation.evaluate(poker.getHand(current));
	        		}
	        	
	        		ArrayList<Integer> winners = 
	        			evaluation.findWinners(results, getCurrentPlayers());
	        		int[] payments = getPayments(winners);        		
	        		payWinners(winners, payments);
	
	        		poker.end();
	        		
	        		return;
				}
				
				// Else, carry on with the dealing and betting
				poker.dealBoard();
	    		setRaiser(NO_RAISER);
	    		///System.out.println("deal");
			}
		}
	}
			
				
	void processCompBet(int i) {
		Bots bots = Bots.getInstance();
		CompBet c = bots.getBot(i);
		int bet = c.bet(poker.getHand(i), betting.getToCall(i), poker.getStreet(), betting.getPot());
		
		AbstractBet ab = new AbstractBet();
		
		switch (bet) {
			case COMP_CALL:
				///System.out.println(i + " call");
				ab.call(i);
				break;
			case COMP_FOLD:
				///System.out.println(i + " folds");
				ab.fold(i);
				break;
			case COMP_RAISE:
				///System.out.println(i + " raise");
				// Adjust the raise to a call if the pot is capped
				if (potIsCapped()) {
					ab.call(i);
				}
				else {
					ab.raise(i, poker.getStreet());
				}
				break;
			default:
				System.out.println("Unknown computer bet");
				break;
		}
	}
	
	int firstToBet(ArrayList<Integer> currentPlayers, int button) {
		if (poker.getStreet() == PREFLOP) {
			return nextPlayer(button + 2, currentPlayers); // UTG 
		}
		else {
			return nextPlayer(button, currentPlayers); // small blind
		}
	}
	
	protected boolean oneLeft() {
		if (numCurrentPlayers() != 1) {
			return false;
		}
		
		int player = getCurrentPlayer(0);
		
		ArrayList<Integer> winners = new ArrayList<Integer>();
		winners.add(player);
    	int[] payments = getPayments(winners);
		payWinners(winners, payments);
		
		poker.end();
		
		return true;
	}
		
}