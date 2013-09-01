package dwpoker;

/** 
 * A representation of playing cards.
 */

public class Card implements Constants {
	/**
	 * An integer value 0-3 representing the card's suit.
	 */
	private int suit;
	
	/**
	 * An integer value 0-12 representing the card's value.
	 * A two is 0, a three is 1, ... an ace is 12.
	 */
	private int value;
	
	/**
	 * No-arg constructor, not useful by default.
	 */
	public Card() {
	}
	
	/**
	 * Constructor used to create this object.
	 * @param suit The card's suit
	 * @param value The card's value
	 */
	public Card(int suit, int value) {
		this.suit = suit;
		this.value = value;
	}
	
	/**
	 * Returns the suit set in the constructor.
	 * @return The card's suit
	 */
	public int getSuit() {
		return suit;
	}
	
	/**
	 * Returns the value set in the constructor.
	 * @return The card's value
	 */
	public int getValue() {
		return value;
	}
	
	public String toString() {
		return suit + "|" + value;
	}
}
