package compbet;

import game.Poker;
import handeval.Eval;
import handeval.Result;

import java.util.ArrayList;

import dwpoker.Card;


public class Comp2 extends Comp1 {
	public Comp2() {
	}
	
	public int bet(ArrayList<Card> hand, int toCall, int street, int pot) {
		double score = 0.0;
		if (street > PREFLOP) {
			score = odds(hand, toCall, street, pot);
		}
		
		else {
			score = super.bet(hand, toCall, street, pot) == COMP_FOLD ? COMP_FOLD : COMP_RAISE;
		}
		
		//System.out.println(score);
		
		if (score >= .2) {
			return COMP_RAISE;
		}
		
		else {
			return COMP_FOLD;
		}
	}
	
	public double odds(ArrayList<Card> hand, int toCall, int street, int pot) {
		if (street == PREFLOP) {
			return BAD_INPUT;
		}
		
		// create a deck, without cards this player can see
		Poker p = new Poker();
		ArrayList<Card> deck = p.makeDeck();		
		for (int i = 0; i < hand.size(); i++) {
			for (int j = 0; j < deck.size(); j++) {
				if (equivalent(hand.get(i), deck.get(j))) {
					deck.remove(j);
				}
			}
		}
		
		//System.out.println(deck);
		//System.out.println(hand);
		//System.out.println(deck.size());
		
		ArrayList<Card> oppHand = new ArrayList<Card>();
		oppHand.addAll(hand);
		//System.out.println(oppHand);
		
	
		int total = 0;
		double handRank = 0.0;
		for (int i = 0; i < deck.size() - 1; i++) {
			for (int j = i + 1; j < deck.size(); j++) {
				oppHand.set(0, deck.get(i));
				oppHand.set(1, deck.get(j));
				//System.out.println(oppHand);
				ArrayList<Integer> currentPlayers = new ArrayList<Integer>();
				currentPlayers.add(0);
				currentPlayers.add(1);
				Result[] results = new Result[PLAYERS];
        		Eval evaluation = new Eval();
        		results[0] = evaluation.evaluate(hand);
        		results[1] = evaluation.evaluate(oppHand);
        		ArrayList<Integer> winners = 
        			evaluation.findWinners(results, currentPlayers);
        		//System.out.println(winners);
        		if (winners.size() == 2) {
        			handRank += .5;
        		}
        		
        		else if (winners.size() == 1 && winners.get(0) == 0) {
        			handRank += 1;
        		}
        		
        		total++;
			}
		}
		
		double score = handRank / total;
		//System.out.println("1 opp: " + score);
		//System.out.println(PLAYERS - 1 + " opps: " + Math.pow(score, PLAYERS - 1));
		
		return Math.pow(score, PLAYERS - 1);
	}
	
	private boolean equivalent(Card card1, Card card2) {
		if (card1.toString().equals(card2.toString())) {
			return true;
		}
		
		else {
			return false;
		}
	}
}