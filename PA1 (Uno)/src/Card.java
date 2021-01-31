

public class Card {
	
	public enum Colors {RED, GREEN, BLUE, YELLOW};
	
	private int number;
	private Colors color;
	private Card next;
	
	/*
	 *  Constructor
	 */
	public Card(int number, Colors color, Card next) {
		this.number=number;
		this.color=color;
		this.next=next;
	}
	
	public int getNumber() {
		return this.number;
	}
	
	public Colors getColor() {
		return this.color;
	}
	
	public Card getNext() {
		return this.next;
	}
	
	public void setNext(Card next) {
		this.next=next;
	}

	
	/* 
	 * toString: is not actually used in the gui version of the program, 
	 * but it is useful while debugging the program. It is used by traverse
	 * so that System.out.println can display a Card
	 */
	public String toString() {
		String card="Color = "+color+", Number = "+number;
		return card;
	}

	/* 
	 * matches: <------ do this one
	 *     if exact is true:
	 *          returns true if this exactly matches card (same number and color)
	 *          otherwise returns false
	 *     if exact is false:     
	 *          returns true if this partially matches card (same number or color)
	 *          otherwise returns false          
	 */
	public boolean matches(boolean exact,Card card) {
		if (exact) {
			if (this.color.equals(card.color) && this.number == card.number) return true;
			return false;
		} else {
			if (this.color.equals(card.color) || this.number == card.number) return true;
			return false;
		}
	}
}
