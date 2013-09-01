package game.simulation;

import compbet.Bots;
import dwpoker.Constants;

import game.Betting;
import game.Poker;


public class Main implements Constants {
	public static void main(String[] args) {
		long initialTime = System.currentTimeMillis();
		
		Betting betting = new Betting();
		betting.readBankRolls();
			
		for (int i = 0; i < RUNS; i++) {
			betting.initSimulation();
			Poker poker = new Poker(PLAYERS, HOLECARDS);
			
			Simulation sim = new Simulation(betting, poker);
			sim.go();
			Bots.refreshRandoms();
			///System.out.println("### New game ###");
			
			/*for (int j = 0; j < PLAYERS; j++) {
				System.out.print(betting.getBankRoll(j) + " ");
			}
			System.out.println();*/
		}	
		
		betting.saveBankRolls();
		System.out.println("ms to run: " + (System.currentTimeMillis() - initialTime));
	}
}
