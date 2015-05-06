

public final class AirbornePositionMessage extends AdsMessage{

	public AirbornePositionMessage(String binarySentence,int messageTypeD, int originatorD, long time) 
	{
		super(binarySentence,messageTypeD, originatorD, time);
	}
	public AirbornePositionMessage(String jedisString)
	{
		super(jedisString);
	}
	public int getAltitude()
	{
		int factor = -1;
		if(Integer.parseInt(binarySentence.substring(15,16),2) == 0)
			factor = 100;
		else
			factor = 25;
		int result = factor*Integer.parseInt(binarySentence.substring(8,15)+binarySentence.substring(16,20),2);
		if(result < -1000 || result > 50175)
			System.out.println("Error at getAltitude!");
		return result;
	}
	public boolean isOdd()
	{
		boolean result = false;
		if(binarySentence.charAt(21) != '0')
			result = true;
		return result;
	}
	public boolean getTimeFlag()
	{
		boolean result = false;
		if(binarySentence.charAt(20) != '0')
			result = true;
		return result;
	}
	public int getLatitude()
	{
		return Integer.parseInt(binarySentence.substring(22,39),2);
	}
	public int getLongitude()
	{
		return Integer.parseInt(binarySentence.substring(39,56),2);
	}
	
	public void print()
	{
		super.print();
		System.out.println("Altitude: \t\t\t"+getAltitude());
		System.out.println("Time Flag: \t\t\t"+getTimeFlag());
		System.out.println("isOdd: \t\t\t\t"+isOdd());
		System.out.println("Latitude: \t\t\t"+getLatitude());
		System.out.println("Longitude: \t\t\t"+getLongitude());
	}
	public String toString()
	{
		return super.toString()+", Lat: "+getLatitude()+", Lon: "+getLongitude();
	}
	public String toJedisString()
	{
		String res = super.toJedisString();
		res += ";"+binarySentence+";"+tStamp.getTime();
		return res;
	}
	

}

