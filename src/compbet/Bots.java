package compbet;

import java.util.Random;

import dwpoker.Constants;


public class Bots implements Constants {
	private static CompBet[] bots;
	
	protected Bots() {
		bots = new CompBet[PLAYERS];
		if (SIMULATION) {
			initSimulation();
		}
		
		else {
			initHumanGame();
		}
		
		refreshRandoms();
	}

	private static class BotsHolder { 
		private final static Bots INSTANCE = new Bots();
	}
 
	public static Bots getInstance() {
		return BotsHolder.INSTANCE;
	}
	
	private void initSimulation() {
		for (int player = 0; player < bots.length; player++) {
			if (player == 0) {
				bots[player] = new Comp0();
			}
			
			else if (player == 1) {
				bots[player] = new Comp1();
			}
			
			else if (player == 2) {
				bots[player] = new Comp4();
			}
			
			else if (player == 3) {
				bots[player] = new Comp3();
			}
			
			else {
				bots[player] = new Comp0();
			}
		}
	}
	
	private void initHumanGame() {
		for (int player = 0; player < bots.length; player++) {
			if (player == HUMAN_PLAYER) {
				bots[player] = null;
			}
			
			else if (player == 1) {
				bots[player] = new Comp1();
			}
			
			else if (player == 2) {
				bots[player] = new Comp4();
			}
			
			else if (player == 3) {
				bots[player] = new Comp3();
			}
			
			else {
				bots[player] = new Comp0();
			}
		}
	}
	
	public CompBet getBot(int player) {
		return bots[player];
	}
	
	public static void refreshRandoms() {
		Random generator = new Random();
		for (int i = 0; i < PLAYERS; i++) {
			if (!(!SIMULATION && i == HUMAN_PLAYER)) {
				bots[i].setRandom(generator.nextInt(100));
			}
		}
	}
}