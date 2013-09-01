package game.human;
import game.Betting;
import game.Poker;
import handeval.Eval;
import handeval.Result;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import compbet.Bots;
import compbet.CompBet;
import dwpoker.Constants;


public abstract class PokerListener extends ConcreteBet implements ActionListener, Constants {
	Betting betting;
	Poker poker;
	Interface gui;
	
	public PokerListener() {
	}
	
	public PokerListener(Betting betting, Poker poker, Interface gui) {
		this.betting = betting;
		this.poker = poker;
		this.gui = gui;
	}
	
	public abstract void actionPerformed(ActionEvent event);
	
	void processHumanBet(String event) {
		ConcreteBet humanBet = new ConcreteBet(gui);
		
		if (event == CALL_TEXT) { // player calls
			//System.out.println("call " + getToCall(HUMAN_PLAYER));
			humanBet.call(HUMAN_PLAYER);
 		}
 		
		else { // player raises
 			//System.out.println("raise " + (getToCall(HUMAN_PLAYER) + bet));
 			humanBet.raise(HUMAN_PLAYER, poker.getStreet());
		}
	}
	
	/**
	 * Determines a computer player's bet
	 * @param i The current player
	 */
	void processCompBet(int i) {
		Bots bots = Bots.getInstance();
		CompBet c = bots.getBot(i);
		int bet = c.bet(poker.getHand(i), betting.getToCall(i), poker.getStreet(), betting.getPot());
		
		ConcreteBet cb = new ConcreteBet(gui);
		
		switch (bet) {
			case COMP_CALL:
				System.out.println(i + " call");
				cb.call(i);
				break;
			case COMP_FOLD:
				System.out.println(i + " folds");
				cb.fold(i);
				break;
			case COMP_RAISE:
				System.out.println(i + " raise");
				cb.raise(i, poker.getStreet());
				break;
			default:
				System.out.println("Unknown computer bet");
				break;
		}
	}
	
	void compBetAfter() {
		for (int i = nextPlayer(HUMAN_PLAYER, getCurrentPlayers());
		getNumCalls() < numCurrentPlayers() && i != getRaiser() &&
		i != HUMAN_PLAYER; i = nextPlayer(i, getCurrentPlayers())) {
			if (isCurrentPlayer(i)) {
				processCompBet(i);
				if (oneLeft()) {
					return;
				}
			}
		}
		
		gui.setStatusBar(betting.callOrCheck());
	}
	
	void compBetReady() {
		if (betting.readyToDeal()) {
			// If there are no more rounds of betting, end the game
			if (poker.getStreet() == RIVER) {
				gui.displayHoleCards();
				gui.setStatusBar("<html>"); // clear the status bar
        		
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

        		for (int i = 0; i < payments.length; i++) {
        			if (winners.contains(i)) {
        				gui.appendStatusBar("Player " + (i + 1) +
            					" wins $" + payments[i] + "<br>");
        			}
        		}
        		
        		gui.appendStatusBar(evaluation.resultString(
        				results[winners.get(0)]) + "<br>");
        		
        		betting.saveBankRolls();
            	betting.saveButtonPlayer();
        		
            	// update JLabels for the pot, bankrolls
            	gui.setPotLabel("Pot: $0");
        		for (int i = 0; i < winners.size(); i++) {
        			gui.setBankRollLabel(winners.get(i), 
        					"$" + betting.getBankRoll(winners.get(i)));
        		}
            	
        		gui.gameOver();
        		poker.end();
        		
        		return;
			}
			
			// Else, carry on with the dealing and betting
			poker.dealBoard();
    		gui.boardImages(poker.getHand(getCurrentPlayer(0)), poker.getStreet());
    		setRaiser(NO_RAISER);
    		//notRaises = 0;
    		System.out.println("deal");
		}
	}
	
	protected boolean oneLeft() {
		if (numCurrentPlayers() != 1) {
			return false;
		}
		
		int player = getCurrentPlayer(0);
		
		gui.setStatusBar("<html>Player " + (player + 1) + " wins $" + getPot() + 
		"<br>Everyone else folded");
		
		ArrayList<Integer> winners = new ArrayList<Integer>();
		winners.add(player);
    	int[] payments = getPayments(winners);
		payWinners(winners, payments);
		saveBankRolls();
    	saveButtonPlayer();
    	
    	// update JLabels for the pot, bankrolls
    	gui.setPotLabel("Pot: $0");
		for (int i = 0; i < winners.size(); i++) {
			gui.setBankRollLabel(winners.get(i), "$" + 
					getBankRoll(winners.get(i)));
		}
		
		gui.gameOver();
		poker.end();
		
		return true;
	}
}