package game.human;

import game.AbstractBet;
import game.Poker;

import javax.swing.ImageIcon;

public class ConcreteBet extends AbstractBet {
	private Interface gui;
	// private int player;
	
	public ConcreteBet() {
	}
	
	public ConcreteBet(Interface gui) {
		this.gui = gui;
	}
	
	public void call(int player) {
		super.call(player);
		updateDisplay(player);
	}
	
	public void raise(int player, int street) {
		if (potIsCapped()) {
			super.call(player);
		}
		else {
			super.raise(player, street);
		}
		updateDisplay(player);
	}

	public void fold(int player) {
		super.fold(player);
		
		ImageIcon icon = new ImageIcon(HOLECARD_IMAGES + "folded.png");
		for (int j = 0; j < HOLECARDS; j++) {
			gui.setHoleCard(player, j, icon);
		}
	}
	
	void updateDisplay(int player) {
		gui.setPotLabel("Pot: $" + pot);
		gui.setBankRollLabel(player, "$" + getBankRoll(player));
	}
	
	/**
	 * Makes the computer players' preflop bets, prior to the human acting
	 */
	void compPreFlop() {
		int next;
		if (PLAYERS <= 2) {
			next = nextPlayer(getButton());
		}
		else {
			next = nextPlayer(getButton() + 2);
		}
		
		BetListener bl = new BetListener(this, new Poker(), gui);
		for (int i = next; i != HUMAN_PLAYER;
		i = nextPlayer(i, getCurrentPlayers())) {
        	bl.processCompBet(i);
        }
	}
}
