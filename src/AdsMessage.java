


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/*
 Base class for all subTypes of Ads-B Messages
 */
public class AdsMessage {
private final int 		messageTypeD, //contains MessageType in decimal format
						originatorD;//Sender of the message in decimal format
protected final Timestamp tStamp; //contains time of the message
protected final String binarySentence;

public AdsMessage(String binarySentence, int messageTypeD, int originatorD, long time)
{
	this.binarySentence = binarySentence.substring(32);
	this.messageTypeD=messageTypeD;
	this.originatorD=originatorD;
	this.tStamp= new Timestamp(time);
}
public AdsMessage(String jedisString)
{
	String entry [] = jedisString.split(";");

	this.binarySentence = entry[2];
	this.messageTypeD = Integer.parseInt(entry[0]);
	this.originatorD = Integer.parseInt(entry[1]);
	this.tStamp= new Timestamp(Long.parseLong(entry[3]));
}
public void print()
{
	System.out.println("-------------------------------------------");
	DateFormat simpleDate = new SimpleDateFormat("EEEE', 'dd.MM.YYYY', 'H:mm:ss.S");
	System.out.println("MessageType: "+messageTypeD);
	System.out.println("Originator: "+originatorD);
	System.out.println("Zeit: "+simpleDate.format(tStamp));
}

public int getMessageTypeD()
{
	return messageTypeD;
}
public int getOriginatorD()
{
	return originatorD;
}
public Timestamp getTimeStamp()
{
	return tStamp;
}
public String toString()
{
	//return "Originator:"+originatorD;
	return super.toString()+
			", messageTypeD: " +getMessageTypeD()+
			", OriginatorD: " +getOriginatorD()+
			", TimeStamp: " +getTimeStamp();
	/* toString() hinzugefuegt - glkeit00 */
}
public String toJedisString()
{
	return messageTypeD+";"+originatorD;

}

}
