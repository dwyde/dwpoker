package dwpoker;

public interface Constants {
	boolean SIMULATION = false;
	int PLAYERS = 4;
	int RUNS = 10000;
	int SMALL_BLIND = 2;
	int BIG_BLIND = SMALL_BLIND * 2;
	int SMALL_BET = BIG_BLIND;
	int BIG_BET = SMALL_BET * 2;
	int HUMAN_PLAYER = 0;
	int HOLECARDS = 2;
	int OFFSET = 2;
	int DECK_VALS = 13;
	int DECK_SUITS = 4;
	int DECK_COUNT = DECK_VALS * DECK_SUITS;
	int FLOP_SIZE = 3;
	int TURN_SIZE = 1;
	int RIVER_SIZE = 1;
	int BOARD_SIZE = FLOP_SIZE + TURN_SIZE + RIVER_SIZE;
	
	int TWO = 0;
	int THREE = 1;
	int FOUR = 2;
	int FIVE = 3;
	int SIX = 4;
	int SEVEN = 5;
	int EIGHT = 6;
	int NINE = 7;
	int TEN = 8;
	int JACK = 9;
	int QUEEN = 10;
	int KING = 11;
	int ACE = 12;
	
	int HEARTS = 0;
	int CLUBS = 1;
	int DIAMONDS = 2;
	int SPADES = 3;
	
	/*public enum scores {HIGH_CARD, PAIR, TWO_PAIR, THREE_OF_A_KIND,
		STRAIGHT, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH, ROYAL_FLUSH}*/
	int HIGH_CARD = 1;
	int PAIR = 2;
	int TWO_PAIR = 3;
	int THREE_OF_A_KIND = 4;
	int STRAIGHT = 5;
	int FLUSH = 6;
	int FULL_HOUSE = 7;
	int FOUR_OF_A_KIND = 8;
	int STRAIGHT_FLUSH = 9;
	int ROYAL_FLUSH = 10;
	
	int PREFLOP = 0;
	int FLOP = 1;
	int TURN = 2;
	int RIVER = 3;
	int END = 4;
	
	int EVAL_TIE = -1;
	int EXIT_FAILURE = 1;
	
	int FOLDED = -1;
	int COMP_CALL = 0;
	int COMP_RAISE = 1;
	int COMP_FOLD = -1;
	int GAME_OVER = 1000;
	int BAD_INPUT = -99999;
	
	String BOARD_IMAGES = "images/board/";
	String HOLECARD_IMAGES = "images/holecards/";
	
	String BANKROLL_FILE = "data/bankrolls.dat";
	String BUTTON_FILE = "data/button.dat";
	 
	int INITIAL_BANKROLL = 1500; 
	int NO_RAISER = -1;
	 
	String FOLD_TEXT = "Fold";
	String CALL_TEXT = "Check/Call";
	String BET_TEXT = "Bet/Raise";
	 
	String POT_TEXT = "Pot: $";
}
