package domain;
import exception.AdsMessageException;
import translator.SixBitASCII;



public final class AirborneIdentificationMessage extends AdsMessage{
	public AirborneIdentificationMessage(String binarySentence, int messageTypeD, int originator, long time) throws AdsMessageException
	{
		super(binarySentence,messageTypeD,originator,time);
	}
	public String getAircraftID()
	{
		return SixBitASCII.bin2ASCII(binarySentence.substring(8,56));
	}
	public void print()
	{
		super.print();
		System.out.println("AircraftID: "+getAircraftID());
	}
	public String toString()
	{
		return super.toString()+", ID: "+getAircraftID();
	}
	public String toJedisString()
	{
		String res = super.toJedisString();
		res+= ";"+getAircraftID();
		return res;
	}



	
}
