package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import dwpoker.Constants;

/**
 * Betting is a class that handles various aspects of 
 */
public class Betting implements Constants {
	
	/**
	 * The player currently on the button
	 */
	protected static int button;
	
	/**
	 * An array storing how many chips each player has left to call
	 */
	protected static int[] toCall;
	
	/**
	 * Values corresponding to the number of chips a player has left
	 */
	protected static int[] bankRolls;
	
	/**
	 * A list of the players who haven't folded
	 */
	protected static ArrayList<Integer> currentPlayers;
	
	/**
	 * The number of chips in the pot
	 */
	protected static int pot;
	
	/**
	 * The last player to make a raiser
	 */
	protected static int raiser;
	
	/**
	 * Counts how many consecutive players have called
	 */
	protected static int calls;
	
	/**
	 * The number of raises this round, capped at 4
	 */
	protected static int numRaises;
	
	public Betting() {
	}
	
	public void initHuman() {
		readBankRolls();
		buttonPlayer();
		newGame();
	}
	
	public void initSimulation() {
		button = nextPlayer(button);
		newGame();
	}
	
	public void newGame() {
		allPlayers();
		blinds();
		raiser = NO_RAISER;
		calls = 0;
		numRaises = 1; // blinds
	}
	
	/**
	 * Initializes the bankRolls[] array with values from a file.
	 */
	public void readBankRolls() { // read bankroll from file
		bankRolls = new int[PLAYERS];
		
		try {
            File bank = new File(BANKROLL_FILE);
            if (!bank.exists()) {
            	System.out.println("File \"" + BANKROLL_FILE + "\" " +
                "does not exist, creating it now");
            	PrintWriter output = new PrintWriter(bank);
        		for (int i = 0; i < PLAYERS; i++) {
        			bankRolls[i] = INITIAL_BANKROLL;
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
                			bankRolls[j] = INITIAL_BANKROLL;
                		}
                		throw new FileNotFoundException();
                	}
                }
                scanner.close();
            }
		}     
        
        catch (FileNotFoundException ex) {
            /*JOptionPane.showMessageDialog(null, "Invalid or missing file" + 
                    " \"" + BANKROLL_FILE + "\"." + 
                    "\nThe results of this hand will not be saved.");*/
        }
    }
	
	/**
	 * Reads the current button player from a file
	 */
	void buttonPlayer() {
		try {
            File buttonFile = new File(BUTTON_FILE);
            if (!buttonFile.exists()) {
            	System.out.println("File \"" + BUTTON_FILE + "\" " +
                "does not exist, creating it now");
            	PrintWriter output = new PrintWriter(buttonFile);
        		output.print(0);
        		output.close();
            }
            
            else {
            	Scanner scanner = new Scanner(buttonFile);
            	try {
            		button = scanner.nextInt();
            	}
            	catch (NoSuchElementException ex) {
            		button = 0;
            		throw new FileNotFoundException();
            	}
            	
                scanner.close();
            }
		}     
        
        catch (FileNotFoundException ex) {
        	/*JOptionPane.showMessageDialog(null, "Invalid or missing file" + 
                    " \"" + BUTTON_FILE + "\"." + 
                    "\nThe results of this hand will not be saved.");*/
        }
	}
	
	/**
	 * Returns the next player in the cycle of betting
	 * @param current The current player
	 * @return The player whose turn it is to act
	 */
	public int nextPlayer(int current) {
		current++;
		if (current >= PLAYERS) {
			return current - PLAYERS;
		}
		
		else {
			return current;
		}
	}
	
	/**
	 * Determines the next player to act from the list of current players
	 * @param current The current player
	 * @param currentPlayers A list of players who have not folded
	 * @return The player whose turn it is to act
	 */
	public int nextPlayer(int current, ArrayList<Integer> currentPlayers) {
		current = nextPlayer(current);
		
		for (int i = 0; i < currentPlayers.size(); i++) {
			if (current == currentPlayers.get(i)) {
				return current;
			}
		}
		
		return nextPlayer(nextPlayer(current - 1), currentPlayers);
	}
	
	/**
	 * Puts in the small blind and big blind, and adjusts the pot accordingly
	 */
	void blinds() {
		toCall = new int[PLAYERS];
		int smallBlind = nextPlayer(button);
		int bigBlind = nextPlayer(smallBlind);
		
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
		
		pot = SMALL_BLIND + BIG_BLIND;
	}
	
	/**
	 * Initializes the currentPlayers ArrayList to contain all players
	 */
	void allPlayers() {
		currentPlayers = new ArrayList<Integer>();
		for (int i = 0; i < PLAYERS; i++) {
			currentPlayers.add(i);
		}
	}
	
	/**
	 * Getter method for the "pot" class variable
	 * @return The size of the pot
	 */
	public int getPot() {
		return pot;
	}
	
	/**
	 * Getter method for the "button" class variable
	 * @return The player currently on the button
	 */
	public int getButton() {
		return button;
	}
	
	/**
	 * Getter method for the "toCall" class variable
	 * @return An array of how many chips each player still has to call
	 */
	public int getToCall(int player) {
		return toCall[player];
	}
	
	/**
	 * Getter method for the "bankRolls" class variable
	 * @return How many chips each player has left
	 */
	public int getBankRoll(int player) {
		return bankRolls[player];
	}
	
	/**
	 * Reveals which player raised most recently
	 * @return The last player to make a raise
	 */
	protected int getRaiser() {
		return raiser;
	}
	
	/**
	 * Gets the full list of current players
	 * @return All of the players who are still in the hand
	 */
	public ArrayList<Integer> getCurrentPlayers() {
		return currentPlayers;
	}
	
	/**
	 * Gets a particular player from the "currentPlayers" ArrayList
	 * @param index The index of currentPlayers to be returned
	 * @return A given player who hasn't folded
	 */
	public int getCurrentPlayer(int index) {
		return currentPlayers.get(index);
	}
	
	/**
	 * Gets the number of players who are still in the hand
	 * @return The number of players who haven't folded
	 */
	public int numCurrentPlayers() {
		return currentPlayers.size();
	}
	
	/**
	 * 
	 * @param index The index of the player in currentPlayers
	 * @return A boolean indicating whether a player is still in the hand
	 */
	protected boolean isCurrentPlayer(int index) {
		return currentPlayers.contains(index);
	}
	
	/**
	 * 
	 * @param i The number of the player to be removed
	 */
	protected void removePlayer(int player) {
		currentPlayers.remove((Object) player);
		toCall[player] = FOLDED;
	}
	
	
	/**
	 * Adds chips to the pot
	 * @param toAdd The number of chips to add
	 */
	protected void addToPot(int toAdd) {
		pot += toAdd;
	}
	
	/**
	 * Increases or decreases the size of a player's bankroll
	 * @param player A player whose bankroll will be changed
	 * @param amount An amount by which to change the bankroll
	 */
	protected void setBankRoll(int player, int amount) {
		bankRolls[player] += amount;
	}
	
	
	protected void addToCall(int player, int chips) {
		toCall[player] += chips;
	}
	
	protected void setToCall(int player, int value) {
		toCall[player] = value;
	}
	
	/**
	 * Updates the "raiser" class variable
	 * @param player The last player to make a raise
	 */
	protected void setRaiser(int player) {
		raiser = player;
		calls = 0;
		if (player == NO_RAISER) {
			numRaises = 0;
		}
		else {
			numRaises++;
		}
	}
	
	/**
	 * Adds one to the count of consecutive calls
	 */
	protected void addCall() {
		calls++;
	}
	
	/**
	 * Gets the number of consecutive non-raises
	 * @return The notRaises class variable
	 */
	protected int getNumCalls() {
		return calls;
	}
	
	protected boolean potIsCapped() {
		if (numRaises >= 4) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Tells the player whether s/he has any bets to call
	 * @return A string to display if there were any prior bets
	 */
	public String callOrCheck() {
		int bet = toCall[HUMAN_PLAYER];
		if (bet > 0) {
			return "Call $" + bet + "?";
		}
		else {
			return "Check?";
		}
	}
	
	/**
	 * Gives the okay to deal the cards if there are no bets to be called
	 * @return True if there are no outstanding debts
	 */
	public boolean readyToDeal() {
		for (int i = 0; i < currentPlayers.size(); i++) {
			if (toCall[currentPlayers.get(i)] > 0) {
				return false;
			}
		}
		return true;
	}
	
	public int[] getPayments(ArrayList<Integer> winners) {
		int[] payments = new int[PLAYERS];
		for (int i = 0; i < winners.size(); i++) {
			payments[winners.get(i)] += pot / winners.size();
		}
		
		int leftovers = pot % winners.size();
		for (int i = 0; i < leftovers; i++) {
			payments[winners.get(i)] += 1;
		}
		
		return payments;
	}
	
	/**
	 * Awards the pot to the player(s) who won the hand
	 * @param winners An ArrayList representing the winners of the hand
	 */
	public void payWinners(ArrayList<Integer> winners, int[] payments) {
		for (int i = 0; i < winners.size(); i++) {
			bankRolls[winners.get(i)] += payments[winners.get(i)];
		}
		
		pot = 0;
	}
	
	/**
	 * Saves bankrolls to a file
	 */
	public void saveBankRolls() {
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
            System.out.println("Could not write to file" + 
                    " \"" + BANKROLL_FILE + "\"." +
                    "\nThe data from this hand will not be saved.");
        }                                
    }
	
	/**
	 * Saves the button player to a file
	 */
	public void saveButtonPlayer() {
        /** Store bankRolls */        
        try {
            File buttonFile = new File(BUTTON_FILE);
            PrintWriter output = new PrintWriter(buttonFile);
            output.print(nextPlayer(button));
            output.close();
        }
        
        catch (FileNotFoundException ex) {
            System.out.println("Could not write to file" + 
                    " \"" + BUTTON_FILE + "\"." +
                    "\nThe data from this hand will not be saved.");
        }                                
    }
}
