package handeval;
import java.util.ArrayList;
import java.util.Collections;

import dwpoker.*;

public class HasHand implements Constants {	
	Result hasHighCard(int[] counts) {
		Result result = new Result();
		
	    for (int i = ACE; i >= TWO; i--) {
	    	if (counts[i] > 0) {
		    	result.addValue(i);
		    	if (result.getNumValues() == 5) { // five cards to a poker hand
		    		result.setScore(HIGH_CARD);
		    		return result;
		    	}
	    	}
	    }

	    result.setScore(0);
		return result;
	}

	Result hasPair(int[] counts) {
		Result result = new Result();

		for (int i = DECK_VALS - 1; i >= 0; i--) {
			if (counts[i] == 2) { // find one pair, at most
				result.addValue(i);
				result.setScore(PAIR);
				kickersMultiples(result, counts);
				return result;
			}
		}

		result.setScore(0);
		return result;
	}

	Result hasTwoPair(int counts[]) {
	    Result result = new Result();
	    ArrayList<Integer> temp = new ArrayList<Integer>();
	    
	    for (int i = DECK_VALS - 1; i >= 0; i--) {
	        if (counts[i] == 2) {
	            temp.add(i); // adds a value for one pair...
	        }
	        if (temp.size() == 2) {
	        	for (int j = 0; j < temp.size(); j++) {
	        		result.addValue(temp.get(j));
	        	}
	        	result.setScore(TWO_PAIR);
	        	kickersMultiples(result, counts);
	        	return result;
	        }
	    }

	    result.setScore(0);
		return result;
	}

	Result hasTrips(int[] counts) {
		Result result = new Result();

		for (int i = DECK_VALS - 1; i >= 0; i--) {
			if (counts[i] == 3) { // find one set of trips, at most
				result.addValue(i);
				result.setScore(THREE_OF_A_KIND);
				kickersMultiples(result, counts);
				return result;
			}
		}

		result.setScore(0);
		return result;
	}

	Result hasStraight(int counts[]) {
	    Result result = new Result();
	    int count = 0; /* cards to a straight */

	    for (int i = ACE; i > FIVE; i--) { /* loop examines values of counts[i] to counts[i-4] */
	        for (int j = 0; j <= 4; j++) {
	        	if (counts[i - j] != 0)  {
					count++;
	        	}
	        }
	        if (count >= 5) { /* player has a regular straight */
	        	result.addValue(i);
	        	result.setScore(STRAIGHT);
	        	return result;
	        }
	        else { /* no straight */
	        	count = 0;
	        }
	    }

	    for (int i = TWO; i <= FIVE; i++) { /* check for A-5 straight (wheel) */
	    	if (counts[FIVE - i] != 0)  {
				count++;
	    	}
	    }
	    if (count == 4 && counts[12] != 0) {
	    	result.addValue(FIVE);
	    	result.setScore(STRAIGHT);
	    	return result;
	    } /* end wheel check */

	    result.setScore(0);
	    return result;
	}

	Result hasFlush(ArrayList<Card> hand) {
	    Result result = new Result();
	    int i;

	    int count[] = new int[DECK_SUITS];

	    for (i = 0; i < hand.size(); i++) {
	        for (int j = 0; j < DECK_SUITS; j++) {
		        if (hand.get(i).getSuit() == j) {
		        	count[j]++;
		        }
	        }
	    }

	    for (i = 0; i < DECK_SUITS; i++) {
	    	if (count[i] >= 5) {
	        	result.setScore(FLUSH);
	        	result.setSuit(i);
	        }
	    }

	    if (result.getScore() != FLUSH) { /* no flush */
	    	result.setScore(0);
	    	return result;
	    }

	    // Separate out cards composing the flush
	    ArrayList<Integer> tempValues = new ArrayList<Integer>();
	    for (i = 0; i < hand.size(); i++) {
	    	if (hand.get(i).getSuit() == result.getSuit()) {
	    		tempValues.add(hand.get(i).getValue());
	    	}
	    }

	    Collections.sort(tempValues);
	    
	    // Get the 5 highest flush cards
	    for (i = tempValues.size() - 1; i >= 0; i--) {
	    	result.addValue(tempValues.get(i));
	    	if (result.getNumValues() == 5) {
	    		break;
	    	}
	    }

	    return result;
	}

	Result hasFullHouse(int counts[]) {
		Result result = new Result();
	    ArrayList<Integer> values = new ArrayList<Integer>();
	    int i;
	    
	    for (i = DECK_VALS - 1; i >= 0; i--) {
	        if (counts[i] == 3) {
	            values.add(i);
	        }
	    }

	    if (values.size() == 0) { // no trips, no full house
	    	result.setScore(0);
	        return result;
	    }
	    
	    for (i = DECK_VALS - 1; i >= 0; i--) {
	        if (counts[i] == 2) {
	            values.add(i);
	        }
	    }

	    if (values.size() > 1) { // full house
	    	result.setScore(FULL_HOUSE);
	    	result.addValue(values.get(0)); // trips
	    	result.addValue(values.get(1)); // (higher) pair
	    	return result;
	    }
	    
	    else {
	        result.setScore(0);
	        return result;
	    }
	}

	Result hasQuads(int[] counts) {
		Result result = new Result();

		for (int i = DECK_VALS - 1; i >= 0; i--) {
			if (counts[i] == 4) { // find one set of trips, at most
				result.addValue(i);
				result.setScore(FOUR_OF_A_KIND);
				kickersMultiples(result, counts);
				return result;
			}
		}

		result.setScore(0);
		return result;
	}

	Result hasStraightFlush(ArrayList<Card> hand) {
	    Result result = new Result();
	    int i, suit = 0;

	    int count[] = new int[DECK_SUITS];

	    for (i = 0; i < hand.size(); i++) {
	        for (int j = 0; j < DECK_SUITS; j++) {
		        if (hand.get(i).getSuit() == j) {
		        	count[j]++;
		        }
	        }
	    }

	    for (i = 0; i < DECK_SUITS; i++) {
	    	if (count[i] >= 5) {
	        	result.setScore(FLUSH);
	        	suit = i;
	        }
	    }

	    if (result.getScore() != FLUSH) { /* no flush */
	    	result.setScore(0);
	    	return result;
	    }

	    // Separate out cards composing the flush
	    ArrayList<Card> tempValues = new ArrayList<Card>();
	    for (i = 0; i < hand.size(); i++) {
	    	if (hand.get(i).getSuit() == suit) {
	    		tempValues.add(hand.get(i));
	    	}
	    }

	    int[] counts = countVals(tempValues);
	    result = hasStraight(counts);
	    
	    if (result.getScore() == 0) { // no straight, no straight flush
	    	return result;
	    }
	    
	    else { // straight flush
	    	result.setSuit(suit);
	    	result.setScore(STRAIGHT_FLUSH);
	    }

	    return result;
	}
	
	/* find kickers for one pair, two pair, three of a kind, and four of a kind */
	void kickersMultiples(Result result, int counts[]) {
		int numKickers = 0;
		switch (result.getScore()) {
			case FOUR_OF_A_KIND:
				numKickers = 1;
				break;
			case THREE_OF_A_KIND:
				numKickers = 2;
				break;
			case TWO_PAIR:
				numKickers = 1;
				break;
			case PAIR:
				numKickers = 3;
				break;
			default:
				System.out.println("Unknown hand: score of " + result.getScore());
				System.exit(EXIT_FAILURE);
		}

		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (int i = ACE; i >= TWO; i--) {
			if (counts[i] > 0) {
				temp.add(i);
				for (int j = 0; j < result.getNumValues(); j++) {
					if (result.getValue(j) == i) {
						temp.remove((Object) i);
						break;
					}
				}
			}
			
			if (temp.size() == numKickers) {
				for (int j = 0; j < numKickers; j++) {
					result.addKicker(temp.get(j));
				}
				
				return;
			}
		}
	}

	public int[] countVals(ArrayList<Card> hand) {
		int i;
	
		int[] values = new int[hand.size()];
		for (i = 0; i < values.length; i++) {
			values[i] = hand.get(i).getValue();
		}
	
		int[] counts = new int[DECK_VALS];
		for (i = 0; i < counts.length; i++) {
			counts[i] = 0;
		}
	
		for (i = 0; i < hand.size(); i++) {
			counts[values[i]]++;
		}
	
		return counts;
	}
}