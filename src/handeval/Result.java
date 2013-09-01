package handeval;
import java.util.ArrayList;

public class Result {
	private int score;
	private ArrayList<Integer> values = new ArrayList<Integer>();
	private ArrayList<Integer> kickers = new ArrayList<Integer>();
	private int suit;
	
	public Result() {
	}
	
	void setScore(int score) {
		this.score = score;
	}
	
	void addValue(int value) {
		values.add(value);
	}
	
	void addKicker(int kicker) {
		kickers.add(kicker);
	}
	
	void setSuit(int suit) {
		this.suit = suit;
	}
	
	int getScore() {
		return score;
	}
	
	int getSuit() {
		return suit;
	}
	
	int getValue(int index) {
		return values.get(index);
	}
	
	int getKicker(int index) {
		return kickers.get(index);
	}
	
	int getNumValues() {
		return values.size();
	}
	
	int getNumKickers() {
		return kickers.size(); 
	}
}
