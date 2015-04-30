public class AirborneIdentificationMessage extends AdsMessage
{
	public AirborneIdentificationMessage(String binarySentence, int messageTypeD, int originator, long time)
	{
		super(binarySentence,messageTypeD,originator,time);
	}

	public String getAircraftID()
	{
		//System.out.println(binarySentence.substring(8,56));
		return SixBitASCII.bin2ASCII(binarySentence.substring(8,56));
	}

	public void print()
	{
		super.print();
		System.out.println("AircraftID: "+getAircraftID());
	}
	
	public String toJedisString()
	{
		String res = super.toJedisString();
		//res += getAircraftID()+";";
		res+= ";"+getAircraftID();//+",";
		return res;
	}

	public String toString()
	{
		return super.toString()+
			", AircraftID: " +getAircraftID()+
	}/* toString() hinzugefuegt - glkeit00 */
}
