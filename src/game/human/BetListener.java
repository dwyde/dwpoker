package game.human;

import game.Betting;
import game.Poker;

import java.awt.event.ActionEvent;

import compbet.Bots;


/**
 * An ActionListener for the check/call and bet/raise buttons
 */
public class BetListener extends PokerListener {
	
	/**
	 * A no-arg constructor for the BetListener class
	 */
    public BetListener() {
    }

    public BetListener(Betting betting, Poker poker, Interface gui) {
    	this.betting = betting;
		this.poker = poker;
		this.gui = gui;
    }
    
	/**
	 * Reacts to the push of the Check/Call and Bet/Raise buttons
	 */
	public void actionPerformed(ActionEvent event) {                                                                                
		if (poker.getStreet() < END) { // preflop betting
			processHumanBet(event.getActionCommand());
			
			// computer betting after human's action
			compBetAfter();
			compBetReady();
			if (poker.getStreet() < END && betting.readyToDeal()) {
				// Computer players' action for the next round of betting
				for (int i = nextPlayer(getButton(), getCurrentPlayers());
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
		}
		
		else {
    		// start a new game
    		gui.dispose();
    		Bots.refreshRandoms();
    		System.out.println("### New game ###");
            Main.main(new String[0]);
    	}
	}
}