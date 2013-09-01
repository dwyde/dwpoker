package game.simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;

import dwpoker.Card;
import dwpoker.Constants;


public class Setup implements Constants {
	public Card[] makeDeck() {
		Card[] deck = new Card[52];
		int current = 0;
		for (int i = 0; i < DECK_SUITS; i++) {
			for (int j = 0; j < DECK_VALS; j++) {
				deck[current++] = new Card(i, j);
			}
		}
		
		return deck;
	}
	
	public void shuffle(Card[] deck) {
		Random rgen = new Random();  // Random number generator
		for (int i=0; i< deck.length; i++) {
		    int randomPosition = rgen.nextInt(deck.length);
		    Card temp = deck[i];
		    deck[i] = deck[randomPosition];
		    deck[randomPosition] = temp;
		}
	}
	
	public ArrayList<ArrayList<Card>> dealHoleCards(ArrayList<Card> deck,
			int players, int cards) {
		ArrayList<ArrayList<Card>> hands = new ArrayList<ArrayList<Card>>();
		for (int i = 0; i < players; i++) {
			hands.add(new ArrayList<Card>());
			for (int j = 0; j < cards; j++) {
				hands.get(i).add(deck.get(0));
				deck.remove(0);
			}
		}
		
		return hands;
	}
	
	public void payWinners(ArrayList<Integer> winners, int[] bankRolls, int pot) {
		for (int i = 0; i < winners.size(); i++) {
			bankRolls[winners.get(i)] += pot / winners.size();
		}
		
		int leftovers = pot % winners.size();
		for (int i = 0; i < leftovers; i++) {
			bankRolls[winners.get(i)] += 1;
		}
	}
	
	public void saveBankRolls(int[] bankRolls) {
        /** Store bankRolls */        
        try {
            File bank = new File(BANKROLL_FILE);
            PrintWriter output = new PrintWriter(bank);
            for (int i = 0; i < PLAYERS; i++) {
            	output.print(bankRolls[i] + " ");
            }
            output.close();
        }
        
        catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Could not write to file" + 
                    " \"" + BANKROLL_FILE + "\"." +
                    "\nThe data from this hand will not be saved.");
        }                                
    }
	
	public int getButtonPlayer() {
		int player = 0;
		
		try {
            File buttonFile = new File(BUTTON_FILE);
            if (!buttonFile.exists()) {
            	JOptionPane.showMessageDialog(null, "File \"" + BUTTON_FILE + "\" " +
                "does not exist, creating it now");
            	PrintWriter output = new PrintWriter(buttonFile);
        		output.print(0);
        		output.close();
            }
            
            else {
            	Scanner scanner = new Scanner(buttonFile);
            	try {
            		player = scanner.nextInt();
            	}
            	catch (NoSuchElementException ex) {
            		player = 0;
            		throw new FileNotFoundException();
            	}
            	
                scanner.close();
            }
		}     
        
        catch (FileNotFoundException ex) {
        	JOptionPane.showMessageDialog(null, "Invalid or missing file" + 
                    " \"" + BUTTON_FILE + "\"." + 
                    "\nThe results of this hand will not be saved.");
        }
        
        return player;
	}
	
	public void saveButtonPlayer(int button, int players) {
        /** Store bankRolls */        
        try {
            File buttonFile = new File(BUTTON_FILE);
            PrintWriter output = new PrintWriter(buttonFile);
            Simulation sim = new Simulation();
            output.print(sim.nextPlayer(button));
            output.close();
        }
        
        catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Could not write to file" + 
                    " \"" + BUTTON_FILE + "\"." +
                    "\nThe data from this hand will not be saved.");
        }                                
    }
	
	public int[] readBankRolls() { // read bankroll from file
		int initial = INITIAL_BANKROLL;
		int[] bankRolls = new int[PLAYERS];
		
		try {
            File bank = new File(BANKROLL_FILE);
            if (!bank.exists()) {
            	JOptionPane.showMessageDialog(null, "File \"" + BANKROLL_FILE + "\" " +
                "does not exist, creating it now");
            	PrintWriter output = new PrintWriter(bank);
        		for (int i = 0; i < PLAYERS; i++) {
        			bankRolls[i] = initial;
        			output.print(bankRolls[i] + " ");
        		}
        		output.close();
            }
            
            else {
            	Scanner scanner = new Scanner(bank);
                for (int i = 0; i < PLAYERS; i++) {
                	try {
                		bankRolls[i] = scanner.nextInt();
                	}
                	catch (NoSuchElementException ex) {
                		for (int j = 0; j < PLAYERS; j++) {
                			bankRolls[j] = initial;
                		}
                		throw new FileNotFoundException();
                	}
                }
                scanner.close();
            }
		}     
        
        catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Invalid or missing file" + 
                    " \"" + BANKROLL_FILE + "\"." + 
                    "\nThe results of this hand will not be saved.");
        }
        
        return bankRolls;
    }
	
	public int[] blinds(int[] bankRolls, int button, int players) {
		int[] toCall = new int[players];
		Simulation sim = new Simulation();
		int smallBlind = sim.nextPlayer(button);
		int bigBlind = sim.nextPlayer(smallBlind);
		
		bankRolls[smallBlind] -= SMALL_BLIND;
		bankRolls[bigBlind] -= BIG_BLIND;
		
		for (int i = 0; i < toCall.length; i++) {
			if (i == smallBlind) {
				toCall[i] = SMALL_BLIND;
			}
			else if (i != bigBlind) {
				toCall[i] = BIG_BLIND;
			}
		}
		
		return toCall;
	}
}
