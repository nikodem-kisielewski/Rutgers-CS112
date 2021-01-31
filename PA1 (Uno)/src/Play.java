

import java.util.Random;

public class Play {

	private CardList deck=new CardList(true);
	private CardList deck1=new CardList(true);
	private CardList human=new CardList(false);
	private CardList computer=new CardList(false);
	private CardList game=new CardList(false);
	public final int HUMAN_TURN=1;
	public final int COMPUTER_TURN=2;
	public final int HUMAN_TURN_NO_MOVES=3;
	public final int COMPUTER_TURN_NO_MOVES=4;
	private int state;
	private Random rand=new Random();
	
	
	public int getState() {
		return state;
	}
	
	public CardList getDeck() {
		return this.deck;
	}
	
	public CardList getHuman() {
		return this.human;
	}

	public CardList getComputer() {
		return this.computer;
	}

	public CardList getGame() {
		return this.game;
	}

	public void shuffle() {

/* -------------------------------------
 * This commented out shuffle can be used instead of
 * the random shuffle (below) to test the program with
 * the same decks every time.
----------------------------------------
		deck.shuffle(35);
		deck.shuffle(30);
		deck.shuffle(10);
		deck.shuffle(36);
		deck.shuffle(37);
		deck.shuffle(35);
		deck.shuffle(30);
		deck.shuffle(10);
		deck.shuffle(36);
		deck.shuffle(37);
*/
		
		int l=deck.size();
		int x=0;
		for (int i=1;i<=20;i++) {
			x=rand.nextInt(l-2)+2;
			deck.shuffle(x);
		}
		
	}
	
	public Play() {
		// initialize and shuffle decks
		deck.concatenateWith(deck1);
		shuffle();			
		// give initial 7 cards to human and computer
		deck.moveTo(7,human);
		deck.moveTo(7,computer);
		deck.moveTo(1,game);
		if (check_for_human_move())
			state=HUMAN_TURN;
		else
			state=HUMAN_TURN_NO_MOVES;
	}

	public void moveToDeck() {
		// when deck is almost empty it takes the cards in game and
		// moves them back to deck so that they can be taken by players
		game.concatenateWith(deck);
		deck=game;
		game=new CardList(false);
		deck.moveTo(1,game);
		shuffle();
	}
	
	public boolean check_for_human_move() {
		Card y=human.search(game.getFirst()); 
		if (y!=null) {
			return true;
		}
		return false;		
	}
	
	public boolean check_for_computer_move() {
		Card y=computer.search(game.getFirst());
		if (y!=null) {
			return true;
		}
		return false;
	}
	
	public boolean human_turn(Card z) {
		// human player tries to throw card z
		// if it matches the top card on game it is used and returns true
		// otherwise returns false (the card does not match) 
		// the variable played contains the value to be returned
		boolean played=true;
		if (state==HUMAN_TURN && z!=null) {
			if (! z.matches(false,game.getFirst()))
				played=false;
			else {
				Card x=human.getCard(z);
				human.moveTo(x,game);
				if (check_for_computer_move())
					state=COMPUTER_TURN;
				else
					state=COMPUTER_TURN_NO_MOVES;
			}
		}
		// in case there are no moves, card z is ignored (which is likely null)
		// and the player takes one card from the deck
		else if (state==HUMAN_TURN_NO_MOVES) {
			deck.moveTo(1,human);
			if (check_for_human_move())
				state=HUMAN_TURN;
			else
				state=HUMAN_TURN_NO_MOVES;			
			played=false;
		}
		if (deck.size()<5) {
			moveToDeck();
		}
		return played;
	}
		
	public void computer_turn() {
		// computer player throws the first card that matches the top
		// card on game (level 1 of the game). No other levels are implemented
		if (state==COMPUTER_TURN) {
			Card y=computer.search(game.getFirst());
			if (y!=null) {
				computer.moveTo(y,game);
			}
		}
		// in case that the computer has no cards that match the top one
		// on game, it will take cards from the deck until it finds a
		// card that matches, and then will throw that card
		else if (state==COMPUTER_TURN_NO_MOVES)
		{
			Card y=deck.getFirst();
			while (! y.matches(false,game.getFirst())) {
				deck.moveTo(1,computer);
				if (deck.size()<5) {
					moveToDeck();
				}
				y=deck.getFirst();					
			}
			deck.moveTo(1,game);
		}		
		if (deck.size()<5) {
			moveToDeck();
		}
		if (check_for_human_move())
			state=HUMAN_TURN;
		else
			state=HUMAN_TURN_NO_MOVES;			
	}
}
