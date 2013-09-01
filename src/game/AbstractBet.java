package game;

public class AbstractBet extends Betting {
	public AbstractBet() {
	}
	
	public void call(int player) {
		int chips = getToCall(player);
		addToPot(chips);
		setBankRoll(player, -chips);
		addCall();
		setToCall(player, 0);
	}
	
	public void raise(int player, int street) {
		int bet = 0;
		
		if (street == PREFLOP || street == FLOP) {
			bet = BIG_BLIND;
		}
		
		else if (street == TURN || street == RIVER) {
			bet = BIG_BET;
		}

		for (int i = 0; i < numCurrentPlayers(); i++) {
			int current = getCurrentPlayer(i);
			if (current != player) {
				addToCall(current, bet);
			}
		}
		
		bet += getToCall(player); // should this be here?
		setToCall(player, 0);
		setRaiser(player);
		addToPot(bet);
		setBankRoll(player, -bet);
	}
	
	public void fold(int player) {
		removePlayer(player);
	}
}