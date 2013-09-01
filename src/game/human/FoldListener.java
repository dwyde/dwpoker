package game.human;
import game.Betting;
import game.Poker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import dwpoker.Constants;


public class FoldListener extends PokerListener implements ActionListener, Constants {
	public FoldListener() {
	}
	
	public FoldListener(Betting betting, Poker poker, Interface gui) {
		this.betting = betting;
		this.poker = poker;
		this.gui = gui;
    }
	
	public void actionPerformed(ActionEvent event) {
		ConcreteBet cb = new ConcreteBet(gui);
		cb.fold(HUMAN_PLAYER);
		
		if (oneLeft()) {
			return;
		}
		
		compBetAfter();
		compBetReady();
		
		while (poker.getStreet() < END) {
			for (int i = nextPlayer(getButton(), getCurrentPlayers());
			getNumCalls() < numCurrentPlayers() && i != getRaiser();
			i = nextPlayer(i, getCurrentPlayers())) {
				if (isCurrentPlayer(i)) {
					processCompBet(i);
					if (oneLeft()) {
						return;
					}
				}
			}
			
			compBetReady();
		}
	}
}