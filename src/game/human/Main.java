package game.human;
import javax.swing.WindowConstants;

import dwpoker.Constants;


import game.*;

public class Main implements Constants {
	public static void main(String[] args) {
		Betting betting = new Betting();
		betting.initHuman();
		Interface frame = new Interface(new Poker(PLAYERS, HOLECARDS), betting);
		frame.setTitle("Poker Interface");   
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);    
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}
}
