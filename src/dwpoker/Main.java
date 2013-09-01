package dwpoker;

public class Main implements Constants {
	public static void main(String[] args) {
		if (SIMULATION) {
			game.simulation.Main.main(args);
		}
		
		else {
			game.human.Main.main(args);
		}
	}
}
