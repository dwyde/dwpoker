package game.human;
import game.Betting;
import game.Poker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import java.util.HashMap;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import dwpoker.*;

/**
 * A graphical frontend for the poker program
 */
public class Interface extends JFrame implements Constants {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * A Poker object
	 */
	private Poker poker;
	
	/**
	 * A Betting object
	 */
	private Betting betting;
	
	/**
	 * A HashMap containing the fold, check/call, and bet/raise buttons
	 */
	private HashMap<String, JButton> buttons;
	
	// Start of variables for the call/bet ActionListener
	/**
	 * A JLabel for displaying messages to the user
	 */
	private static JLabel statusBar;
	
	/**
	 * A label for displaying the size of the pot
	 */
	private static JLabel potLabel;
	
	/**
	 * An array of JLabels for displaying each player's bankroll
	 */
	private static JLabel[] bankRollLabels;
	
	/**
	 * An array of JLabels to display each player's hole cards
	 */
	private static JLabel[][] holeCardLabels;
	
	/**
	 * An array of JLabels for displaying the board cards
	 */
	private static JLabel[] boardLabels;
	
	// End of variables for the call/bet ActionListener
	
	/**
	 * No-arg constructor for the Interface class
	 */
	public Interface() {
	}
	
	/**
	 * A constructor for the Interface class
	 */
	public Interface(Poker poker, Betting betting) {
		this.poker = poker;
		this.betting = betting;
		
		setLayout(new BorderLayout());
		
		createButtons();
		add(createMainPanel(), BorderLayout.CENTER);
		add(createHoleCardPanel(), BorderLayout.EAST);
		
		BetListener bl = new BetListener(betting, poker, this);
        buttons.get("call").addActionListener(bl);
        buttons.get("bet").addActionListener(bl);
        FoldListener fl = new FoldListener(betting, poker, this);
        buttons.get("fold").addActionListener(fl);
        
        // Computer preflop betting
        ConcreteBet cb = new ConcreteBet(this);
        cb.compPreFlop();
        if (bl.oneLeft()) {
        	return;
        }
        setStatusBar(betting.callOrCheck());
	}
	
	/**
	 * Creates a JPanel that contains buttons, a status bar JLabel, and the board cards
	 * @return The main panel for the interface
	 */
	private JPanel createMainPanel() {
		JPanel main = new JPanel(new BorderLayout());
        main.add(createButtonPanel(), BorderLayout.SOUTH);
        main.add(createLabelPanel(), BorderLayout.NORTH);
        main.add(createCommunityPanel(), BorderLayout.CENTER);
        
        return main;
	}
	
	/**
	 * Creates JButtons to fold, check/call, or bet/raise.
	 */
	private void createButtons() {
		buttons = new HashMap<String, JButton>();
		buttons.put("fold", new JButton(FOLD_TEXT));
		buttons.put("call", new JButton(CALL_TEXT));
		buttons.put("bet", new JButton(BET_TEXT));
	}
	
	/**
	 * Creates a panel to hold the fold, check/call, and bet/raise buttons.
	 * @return A JPanel containing buttons for player input
	 */
	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(buttons.get("fold"));
        buttonPanel.add(buttons.get("call"));
        buttonPanel.add(buttons.get("bet"));
        
        return buttonPanel;
	}
	
	/**
	 * Creates a JPanel containing two JLabels: a "status bar", and the size of the pot.
	 * @return A JPanel that holds the status bar and pot label
	 */
	private JPanel createLabelPanel() {
		JPanel labelPanel = new JPanel(new BorderLayout());
        statusBar = new JLabel(" ");
        statusBar.setHorizontalAlignment(SwingConstants.CENTER);
        statusBar.setOpaque(true);
        statusBar.setBackground(new Color(203, 216, 232));
        labelPanel.add(statusBar);

        // pot JLabel
        potLabel = new JLabel(POT_TEXT + betting.getPot());
        potLabel.setBorder(BorderFactory.createMatteBorder(0,0,0,1, Color.black));
        labelPanel.add(potLabel, BorderLayout.WEST);
        labelPanel.setBorder(BorderFactory.createMatteBorder(0,0,1,0, Color.black));
        
        return labelPanel;
	}
	
	/**
	 * Creates a JPanel that displays the community cards as they are dealt.
	 * @return The JLabel described
	 */
	private JPanel createCommunityPanel() {
		JPanel community = new JPanel(new GridLayout(1, 5));
	    Color green = new Color(17, 162, 56);
	    community.setBackground(green);
	    
	    boardLabels = new JLabel[BOARD_SIZE];
	    for (int i = 0; i < boardLabels.length; i++) {
	    	ImageIcon invisible = new ImageIcon(BOARD_IMAGES + "invisible.png");
	    	boardLabels[i] = new JLabel(invisible);
	    	boardLabels[i].setBorder(BorderFactory.createEmptyBorder(5,3,5,2));
	    	community.add(boardLabels[i]);
	    }
	    
	    return community;
	}	
	
	/**
	 * Creates a JPanel to display each player's hole cards
	 * @param poker A Poker object used to retrieve the human player's hand 
	 * @return A JPanel that fits the above description
	 */
	private JPanel createHoleCardPanel() {
		final JPanel holeCardPanel = new JPanel();
		
		if (PLAYERS <= 5) {
			holeCardPanel.setLayout(new GridLayout(PLAYERS, 1));
		}
		
		else {
			holeCardPanel.setLayout(new GridLayout((PLAYERS / 2) + PLAYERS % 2, 2));
		}
		
		holeCardLabels = new JLabel[PLAYERS][HOLECARDS];
        bankRollLabels = new JLabel[PLAYERS];
        for (int i = 0; i < PLAYERS; i++) {
	        JPanel player = new JPanel(new BorderLayout());
	        JPanel holeCards = new JPanel();
	        if (i == HUMAN_PLAYER) {
	        	for (int j = 0; j < HOLECARDS; j++) {
	        		ImageIcon card = cardToImage(HOLECARD_IMAGES, poker.getHand(HUMAN_PLAYER).get(j));
	        		holeCardLabels[i][j] = new JLabel(card);
	        		holeCards.add(holeCardLabels[i][j]);
	        	}
	        }
	        else { // computer player
	        	ImageIcon blank = new ImageIcon(HOLECARD_IMAGES + "blank.png");
	        	for (int j = 0; j < HOLECARDS; j++) {
		        	holeCardLabels[i][j] = new JLabel(blank);
			        holeCards.add(holeCardLabels[i][j]);
	        	}
	        }
	        
	        player.add(holeCards, BorderLayout.WEST);
	        
	        JLabel label = new JLabel("Player " + (i + 1));
	        player.add(label, BorderLayout.NORTH);
	        
	        bankRollLabels[i] = new JLabel("$" + betting.getBankRoll(i));
	        if (i == betting.getButton()) {
	        	bankRollLabels[i].setIcon(new ImageIcon(HOLECARD_IMAGES + "button.png"));
	        	bankRollLabels[i].setVerticalTextPosition(SwingConstants.BOTTOM);
	            bankRollLabels[i].setHorizontalTextPosition(SwingConstants.CENTER);
	        }
	        player.add(bankRollLabels[i], BorderLayout.EAST);
	        player.setBorder(BorderFactory.createMatteBorder(0,1,1,0, Color.black));
	        
	        holeCardPanel.add(player);
        }
        
        return holeCardPanel;
	}
	
	/**
	 * Converts a Card object to an ImageIcon of that particular card.
	 * @param directory A folder containing the card images, defined in Constants.java
	 * @param card A Card image to be converted
	 * @return An ImageIcon of the given card
	 */
	ImageIcon cardToImage(String directory, Card card) {
		char suit = '?';
		switch (card.getSuit()) {
			case HEARTS:
				suit = 'h'; break;
			case CLUBS:
				suit = 'c'; break;
			case DIAMONDS:
				suit = 'd'; break;
			case SPADES:
				suit = 's'; break;
			default:
				System.out.println("Unknown suit: " + card.getSuit());
				System.exit(0);
		}
		
		return new ImageIcon(directory + card.getValue() + suit + ".png");
	}
	
	void boardImages(ArrayList<Card> hand, int street) {
		int current = 0, numCards = 0, start = 0;
		
		switch (street) {
			case FLOP:
				start = HOLECARDS;
				numCards = FLOP_SIZE;
				break;
			case TURN:
				start = HOLECARDS + FLOP_SIZE;
				numCards = TURN_SIZE;
				break;
			case RIVER:
				start = HOLECARDS + FLOP_SIZE + TURN_SIZE;
				numCards = RIVER_SIZE;
		}
		
		 current = start - HOLECARDS;
		
    	for (int i = start; i < numCards + start; i++) {
			ImageIcon card = cardToImage(BOARD_IMAGES, hand.get(i));
			boardLabels[current++].setIcon(card);
    	}
	}
	
	/**
	 * Displays the remaining computer players' hole cards at the end of a hand
	 */
	void displayHoleCards() {
		for (int i = 0; i < betting.numCurrentPlayers(); i++) {
			int current = betting.getCurrentPlayer(i);
			if (current == HUMAN_PLAYER) {
				continue;
			}
			
			for (int j = 0; j < HOLECARDS; j++) {
				ImageIcon icon = cardToImage(HOLECARD_IMAGES, poker.getHand(current).get(j));
				holeCardLabels[current][j].setIcon(icon);
			}
		}
	}
	
	// Get methods for class variables
	/**
	 * Gets text for the JLabel representing the pot
	 * @return Text corresponding to the size of the pot
	 */
	JLabel getPotLabel() {
		return potLabel;
	}
	
	// Set methods for class variables
	/**
	 * Sets the text for the JLabel representing the pot
	 */
	void setPotLabel(String text) {
		potLabel.setText(text);
	}
	
	/**
	 * Sets the text of a JLabel representing a player's bankroll
	 * @param player The player whose bankroll has changed
	 * @param s The string that the label will be set to
	 */
	void setBankRollLabel(int player, String s) {
		bankRollLabels[player].setText(s);
	}
	
	/**
	 * 
	 * @param i The array index corresponding to a given player
	 * @param j The array index for which hole card is to be changed
	 * @param icon
	 */
	void setHoleCard(int i, int j, ImageIcon icon) {
		holeCardLabels[i][j].setIcon(icon);
	}
	
	void setStatusBar(String s) {
		statusBar.setText(s);
	}
	
	void appendStatusBar(String s) {
		statusBar.setText(statusBar.getText() + s);
	}
	
	public void gameOver() {
		// change buttons at the end of the game
		buttons.get("fold").setEnabled(false);
		buttons.get("fold").setText("");
		buttons.get("bet").setEnabled(false);
		buttons.get("bet").setText("");
		buttons.get("call").setText("New game?");
	}
}