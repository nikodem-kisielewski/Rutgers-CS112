

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class Gui extends JFrame{

	JPanel cardPanel;
	JButton cardButton[][] = new JButton[6][10];
	JTextField displayField;
	JTextField displayField2;
	boolean pressed=false;
	String action="";
	Play myPlay;
	Timer t;
	int start=0;
	
	    public Gui(){
			setTitle("UNO");
			setSize(600,600);
			setLocation(200,200);
			setDefaultCloseOperation(EXIT_ON_CLOSE);
	
			Container unoContentPane = getContentPane();
			cardPanel = new JPanel();
			cardPanel.setLayout(new GridLayout(5,9));
			for (int i=1;i<=5;i++) {
				for (int j=1;j<=9;j++) {
					cardButton[i][j] = new JButton("");
					cardButton[i][j].setFont(new Font("Arial", Font.PLAIN, 20));
					if (i==5 || (i==3 && j==3)) {
						cardButton[i][j].addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent e){
								cardPressedAct(e);
							}
						});
						if (j==1)
							cardButton[i][j].setText("<");
						else if (j==9) {
							cardButton[i][j].setText(">");
						}
					}				
					cardPanel.add(cardButton[i][j]);
					if (! (i==1 || i==5 || (i==3 && j==3) || (i==3 && j==5)))
						cardButton[i][j].setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
				}
			}
		
			cardButton[1][1].setFont(new Font("Arial", Font.PLAIN, 16));
			cardButton[3][3].setFont(new Font("Arial", Font.PLAIN, 14));
			unoContentPane.add(cardPanel);

			displayField = new JTextField();
			unoContentPane.add(displayField,BorderLayout.NORTH);
			unoContentPane.add(cardPanel);
			displayField.setText("UNO GAME");
			displayField.setHorizontalAlignment(JTextField.CENTER);
			displayField.setEditable(false);
			displayField.setColumns(8);
			displayField.setFont(new Font("Arial", Font.PLAIN, 30));

			displayField2 = new JTextField();
			unoContentPane.add(displayField2,BorderLayout.SOUTH);
			unoContentPane.add(cardPanel);
			displayField2.setText("UNO GAME");
			displayField2.setHorizontalAlignment(JTextField.CENTER);
			displayField2.setEditable(false);
			displayField2.setColumns(8);
			displayField2.setFont(new Font("Arial", Font.PLAIN, 20));
			
			t = new javax.swing.Timer(2000, new ActionListener() {
		          public void actionPerformed(ActionEvent e) {
		        	  spacePressedAct(e);
		          }
		       });
			
			myPlay=new Play();
			showCards();
			displayMessage("Your Turn",Color.BLACK,Color.WHITE);
			setVisible(true);

	    }

	    public void cardPressedAct(ActionEvent e) {
	    	action=e.getActionCommand();
    		int state=myPlay.getState();
	    	if (myPlay.getComputer().size()==0) {
    			displayMessage("Y O U      L O S T",Color.WHITE,Color.RED);	    		
	    	}
	    	else if (myPlay.getHuman().size()==0) {
    			displayMessage("Y O U       W O N",Color.WHITE,Color.GREEN);	    		
	    	}
	    	else if (action.equals(">")) {
	    		rightCards();
	    	}
	    	else if (action.equals("<")) {
	    		leftCards();
	    	}
	    	else if (action.contains("Deck")) {
	    		if (state==myPlay.HUMAN_TURN_NO_MOVES) {
	    			myPlay.human_turn(null);
	    			state=myPlay.getState();
	    			if (state==myPlay.HUMAN_TURN) {
	        			displayMessage("Your Turn  (pick a card)",Color.BLACK,Color.WHITE);	    				
	    			}
	    		}
	    	}
	    	else if (action.length()>=2) {
	    		if (state!=myPlay.HUMAN_TURN_NO_MOVES) {
	    			Card x=new Card(action.charAt(0)-48,getColorChar(action.charAt(1)),null);
	    			myPlay.human_turn(x);
	    			state=myPlay.getState();
	    			if (state==myPlay.COMPUTER_TURN || state==myPlay.COMPUTER_TURN_NO_MOVES) {
	    				displayMessage("Computer Thinking...",Color.BLACK,Color.GREEN);
	    				t.start();
	    			}
	    			else if (state==myPlay.HUMAN_TURN){
	    				displayMessage("Your Turn: Pick a different card",Color.WHITE,Color.RED);	    			
	    			}
	    			else if (state==myPlay.HUMAN_TURN_NO_MOVES){
	    				displayMessage("You took a card!",Color.WHITE,Color.ORANGE);	    			
	    			}
	    		}
	    	}
			showCards();
	    }
	   
	    public void spacePressedAct(ActionEvent e) {
	    	if (myPlay.getComputer().size()==0) {
    			displayMessage("Y O U      L O S T",Color.WHITE,Color.RED);	    		
	    	}
	    	else if (myPlay.getHuman().size()==0) {
    			displayMessage("Y O U       W O N",Color.WHITE,Color.GREEN);	    		
	    	}
	    	else {
	    		myPlay.computer_turn();
	    		showCards();
	    		int state=myPlay.getState();
	    		if (state==myPlay.HUMAN_TURN) {
	    			displayMessage("Your Turn",Color.BLACK,Color.WHITE);
	    		}
	    		else if (state==myPlay.HUMAN_TURN_NO_MOVES){
	    			displayMessage("Your Turn, Press 'Deck' for Cards",Color.BLACK,Color.WHITE);    			
	    		}
	    	}
	    	if (myPlay.getComputer().size()==0) {
    			displayMessage("Y O U      L O S T",Color.WHITE,Color.RED);	    		
	    	}
	    	t.stop();
	    }

	    public void leftCards() {
	    	start--;
	    	if (start<0) start++;
	    	showCards();
	    }
	    
	    public void rightCards() {
	    	start++;
	    	if (start+7>myPlay.getHuman().size()) start--;
	    	showCards();
	    }
	    
	    public Color findBGColor(Card.Colors color) {
	    	switch (color) {
	    	case RED:
	    		return Color.RED;
	    	case BLUE:
	    		return Color.BLUE;
	    	case GREEN:
	    		return Color.GREEN;
	    	case YELLOW:
	    		return Color.YELLOW;	    		
	    	}
	    	return Color.WHITE;
	    }
	    
	    public Color findFGColor(Card.Colors color) {
	    	switch (color) {
	    	case RED:
	    		return Color.WHITE;
	    	case BLUE:
	    		return Color.WHITE;
	    	case GREEN:
	    		return Color.BLACK;
	    	case YELLOW:
	    		return Color.BLACK;	    		
	    	}
	    	return Color.WHITE;
	    }
	    
	    public String getColorChar(Card.Colors color) {
	    	switch (color) {
	    	case RED:
	    		return "R";
	    	case BLUE:
	    		return "B";
	    	case GREEN:
	    		return "G";
	    	case YELLOW:
	    		return "Y";	    		
	    	}
	    	return "W";	    	
	    }
	    
	    public Card.Colors getColorChar(char color ) {
	    	switch (color) {
	    	case 'R':
	    		return Card.Colors.RED;
	    	case 'B':
	    		return Card.Colors.BLUE;
	    	case 'G':
	    		return Card.Colors.GREEN;
	    	case 'Y':
	    		return Card.Colors.YELLOW;	    		
	    	}
	    	return Card.Colors.RED;	    	
	    }

	    public void displayMessage(String message,Color fgcolor,Color bgcolor) {
	    	displayField.setText(message);
	    	displayField.setBackground(bgcolor);
	    	displayField.setForeground(fgcolor);
	    }
	    
	    public void displayCard(int i, int j,Card c) {
	    	cardButton[i][j].setText(""+c.getNumber()
				+getColorChar(c.getColor()));
	    	cardButton[i][j].setBackground(findBGColor(c.getColor()));
	    	cardButton[i][j].setForeground(findFGColor(c.getColor()));
	    }
	    
	    public void refreshCards() {
	    	for (int i=0;i<=6;i++) {
		    	cardButton[5][i+2].setText("");
		    	cardButton[5][i+2].setBackground(Color.WHITE);
	    		
	    	}
	    }
	    
	    public void displayComputerCard(int i, int j,Card c) {
	    	cardButton[i][j].setText("??");
	    	cardButton[i][j].setBackground(Color.BLACK);
	    	cardButton[i][j].setForeground(Color.BLUE);
	    }

	    public void refreshComputerCards() {
	    	for (int i=0;i<=7;i++) {
		    	cardButton[1][i+2].setText("");
		    	cardButton[1][i+2].setBackground(Color.WHITE);
	    		
	    	}
	    }
	    public void showHumanCards() {
	    	CardList human=myPlay.getHuman();
	    	CardList game=myPlay.getGame();
	    	refreshCards();
			int count=0;
			Card current=human.getFirst();		
			while (current!=null && count-start<7) {
				if (count-start>=0)
					displayCard(5,count-start+2,current);
				count++;
				current=current.getNext();
			}
			current=game.getFirst();
			displayCard(3,5,current);
			displayField2.setText("You have "+human.size()+" cards");
		}

	    public void showComputerCards() {
	    	CardList computer=myPlay.getComputer();
	    	
	    	cardButton[1][1].setText(""+computer.size());
	    	refreshComputerCards();
			int count=0;
			Card current=computer.getFirst();		
			while (current!=null && count<=7) {
				displayComputerCard(1,count+2,current);
				count++;
				current=current.getNext();
			}
		}

	    public void showCards() {
	    	showComputerCards();
	    	showHumanCards();
	    	CardList deck=myPlay.getDeck();
	    	cardButton[3][3].setText("<html>Deck<BR>("+deck.size()+")</html>");
	    	cardButton[3][3].setBackground(Color.GRAY);
	    	cardButton[3][3].setForeground(Color.BLACK);
	    }
	    
		public static void main(String[] args) {						
			new Gui();
		}
}
