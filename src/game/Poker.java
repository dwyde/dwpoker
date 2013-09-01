package game;
import java.util.ArrayList;
import java.util.Collections;

import dwpoker.*;

/**
 * Poker is a class for keeping track of cards, in the form of hands and the deck
 */

public class Poker implements Constants {
	/**
	 * A deck of cards
	 */
	private static ArrayList<Card> deck;
	
	/**
	 * Hole cards for each player
	 */
	private static ArrayList<ArrayList<Card>> hands;
	
	/**
	 * The current stage of betting
	 */
	private static int street = PREFLOP;
	
	/** 
	 * No-arg constructor for this object
	 */
	public Poker() {
	}
	
	/**
	 * Class constructor to create this object
	 * @param players The number of players in the hand
	 * @param holeCards How many cards each player is dealt
	 */
	public Poker(int players, int holeCards) {
		street = PREFLOP;
		deck = makeDeck();
		shuffle();
		dealHands(players, holeCards);
	}
	
	/**
	 * Creates and shuffles a 52-card deck.
	 * @return A deck of cards.
	 */
	public ArrayList<Card> makeDeck() {
		ArrayList<Card> deck = new ArrayList<Card>();
		for (int i = 0; i < DECK_SUITS; i++) {
			for (int j = 0; j < DECK_VALS; j++) {
				deck.add(new Card(i, j));
			}
		}
		
		return deck;
	}
	
	/**
	 * Removes and returns the first card from the deck.
	 * @return The top card in the deck
	 */
	Card dealCard() {
		Card card = deck.get(0);
		deck.remove(0);
		return card;
	}
	
	/**
	 * Deals each player's hole cards.
	 * @param deck The deck of cards
	 * @return Pocket cards.
	 */
	void dealHands(int players, int holecards) {
		hands = new ArrayList<ArrayList<Card>>();
		for (int i = 0; i < players; i++) {
			ArrayList<Card> hand = new ArrayList<Card>();
			for (int j = 0; j < holecards; j++) {
				hand.add(dealCard());
			}
			hands.add(hand);
		}
	}
	
	/**
	 * Adds the dealt board cards to each player's hand
	 * @param cards The number of cards to deal
	 */
	public void dealBoard() {
		int cards = 0;
		switch (street) {
			case PREFLOP:
				cards = FLOP_SIZE; break;
			case FLOP:
				cards = TURN_SIZE; break;
			case TURN:
				cards = RIVER_SIZE; break;
			default:
				return;				
		}
		
		for (int i = 0; i < cards; i++) {
			Card c = dealCard();
			for (int j = 0; j < hands.size(); j++) {
				hands.get(j).add(c);
			}
		}
		
		nextStreet();
	}

	public void nextStreet() {
		street++;
	}
	
	/**
	 * Returns a particular player's hand
	 * @param player The player whose hand will be returned
	 * @return A given hand
	 */
	public ArrayList<Card> getHand(int player) {
		return hands.get(player);
	}
	
	/**
	 * Getter method for the "street" class variable
	 * @return The current stage of the board
	 */
	public int getStreet() {
		return street;
	}
	
	public void end() {
		street = END;
	}
	
	private void shuffle() {
		Collections.shuffle(deck);
	}
}
