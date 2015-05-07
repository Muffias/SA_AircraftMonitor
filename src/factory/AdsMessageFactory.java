package factory;





import java.math.BigInteger;

import domain.AdsMessage;
import domain.AirborneIdentificationMessage;
import domain.AirbornePositionMessage;
import domain.AirborneVelocityMessage;

/*
 * This class creates Messages and holds Methods to parse the Messages
 */
public final class AdsMessageFactory 
{
	private static AdsMessageFactory instance;
	private AdsMessageFactory(){};
	public static AdsMessageFactory getInstance()
	{
		if(instance == null)
			instance = new AdsMessageFactory();
		return instance;
	}
	public AdsMessage sentence2Message(String sentence)
	{
		String payload = sentence.substring(sentence.indexOf('*'),sentence.indexOf(';'));
		String binarySentence = (String.format("%112s",(new BigInteger(payload.substring(1),16) ).toString(2))).replace(' ','0');
		
		//Decoding of Message(Sub-)Type and so on
		int messageType = Integer.parseInt(binarySentence.substring(32,37),2);
		int originator = Integer.parseInt(binarySentence.substring(8,32),2);
		long time = Long.valueOf(  (sentence.substring(sentence.lastIndexOf(',')+2,sentence.lastIndexOf('.')+4)).replace(".", "") );
		
		
		//Switching the messageTypes in order to deliver the correct MessageType-Objects
		switch(messageType)
		{
		case 1: case 2: case 3: case 4: //Aircraft Identification Message
				return new AirborneIdentificationMessage(binarySentence,messageType,originator,time);
	
		case 9: //9-18, 20-22 Airborne Position Message
		case 10:case 11: case 12:case 13:case 14:case 15:case 16:case 17:case 18: case 20:case 21:
		case 22: return new AirbornePositionMessage(binarySentence,messageType,originator,time);
		
		case 19: return new AirborneVelocityMessage(binarySentence, messageType, originator, time);
		default: return new AdsMessage(binarySentence,messageType,originator,time);
		}
	}
}
//All static decoding methods here
