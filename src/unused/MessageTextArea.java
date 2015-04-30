package unused;
import AdsMessage;
import AirborneIdentificationMessage;
import AirbornePositionMessage;
import AirborneVelocityMessage;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JTextArea;


public class MessageTextArea extends JTextArea implements Observer{

	private static final long serialVersionUID = 1L;
	private int type;
	//private JTextArea txtArea;
	//private boolean firstRun = true;
	public MessageTextArea(int type)//1..ID 2..Pos 3..Velo
	{
		super();
		switch(type)
		{
		case 1: this.append("IDENTIFICATION-MESSAGES\n\n");
				this.setBackground(Color.MAGENTA);
				break;
		case 2: this.append("POSITION-MESSAGES\n\n");
				this.setBackground(Color.ORANGE);
				break;
		case 3: this.append("VELOCITY-MESSAGES\n\n");
				this.setBackground(Color.YELLOW);
				break;
		default:break;
		}
		this.type = type;
		//this.setSize(new Dimension(200,500));
		
		//txtArea = new JTextArea("Quark");
		//JScrollPane sPane = new JScrollPane(txtArea);
		//this.add(sPane);
		
		//this.add(sPane);
		//this.setVisible(true);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		System.out.println("MessagePassed");
		int messageType = ((AdsMessage) arg1).getMessageTypeD();
		
		switch(messageType)
		{
		case 1: case 2: case 3: case 4: //Aircraft Identification Message
					if(type == 1) {this.append( ((AirborneIdentificationMessage)arg1).toString());
					this.append("\n");
					//this.setBackground(new Color((int)Math.floor(Math.random()*255),(int)Math.floor(Math.random()*255),(int)Math.floor(Math.random()*255)));
					}
					break;
		case 9: //9-18, 20-22 Airborne Position Message
		case 10:case 11: case 12:case 13:case 14:case 15:case 16:case 17:case 18: case 20:case 21:
		case 22: 
					if(type == 2) {this.append( ((AirbornePositionMessage)arg1).toString());
					this.append("\n");
					//this.setBackground(new Color((int)Math.floor(Math.random()*255),(int)Math.floor(Math.random()*255),(int)Math.floor(Math.random()*255)));
					}
					break;
		case 19: 
					if(type == 3){ this.append( ((AirborneVelocityMessage)arg1).toString());
					this.append("\n");
					//this.setBackground(new Color((int)Math.floor(Math.random()*255),(int)Math.floor(Math.random()*255),(int)Math.floor(Math.random()*255)));
					}
					break;
		default: 	System.out.println("Anderer Typ@MainFrame");break;
		}	
		
	
		
	}

}
	
