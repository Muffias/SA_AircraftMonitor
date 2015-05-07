package domain;

public final class Aircraft {
private double 	velocity, //absolute Velocity of the aircraft
				veloAngle;	//Angle in degrees for the direction of flight
private int		lastOdd;
private final int aircraftID;
private AirbornePositionMessage msg_even,msg_odd;
private double latitude,longitude;
private String flightNo=new String("");
public Aircraft(int aircraftID)
{
	this.aircraftID = aircraftID;
	velocity = -1;
	veloAngle = -1;
	latitude = -1;
	longitude = -1;
	flightNo = "";
}

public Aircraft(String jedisString)//aircraftID+","+flightNo+"."+velocity+";"+veloAngle+":"+latitude+"-"+longitude+"_"+lastOdd+"+"+msg_even.toJedisString()+"*"+msg_odd.toJedisString()+"#";
{
	String even = new String("");
	String odd = new String ("");
	String entry [] = jedisString.split(",");
	aircraftID = Integer.parseInt(entry[0]);
	flightNo = entry[1];
	velocity = Double.parseDouble(entry[2]);
	veloAngle = Double.parseDouble(entry[3]);
	latitude = Double.parseDouble(entry[4]);
	longitude = Double.parseDouble(entry[5]);
	lastOdd = Integer.parseInt(entry[6]);
	if(entry.length>7)
	{
		even=entry[7];
		if(entry.length==9)
			odd=entry[8];
	}
	if (!even.equals(""))
		msg_even = new AirbornePositionMessage(even);
	if (!odd.equals(""))
		msg_odd = new AirbornePositionMessage(odd);
	
}

public void setVelocity(double velo)
{
	this.velocity = velo;
}
public void setVeloAngle(double angle)
{
	this.veloAngle = angle;
}
public void setFlightNo(String no)
{
	this.flightNo = no;
}
public void setLatitude(double lat)
{
	this.latitude=lat;
}
public void setLongitude(double lon)
{
	this.longitude=lon;
}
public void setLastOdd(int i)
{
	this.lastOdd = i;
}
public void setEvenMsg(AirbornePositionMessage even)
{
	this.msg_even=even;
	this.lastOdd=0;
}
public void setOddMsg(AirbornePositionMessage odd)
{
	this.msg_odd=odd;
	this.lastOdd=1;
}


public AirbornePositionMessage getEven()
{
	if(msg_even != null)
		return msg_even;
	else
		return null;
}
public AirbornePositionMessage getOdd()
{
	if(msg_odd != null)
		return msg_odd;
	else
		return null;
}
public int getLastOdd()
{
	return lastOdd;
}
public int getID()
{
	return aircraftID;
}
public double getVelocity()
{
	return velocity;
}
public double getVeloAngle()
{
	return veloAngle;
}
public void print()
{
	System.out.println("********************************");
		System.out.println("Aircraft ID: " + aircraftID);
	if(velocity != -1)
		System.out.println("Geschwindigkeit: "+ velocity);
	if(veloAngle != -1)
		System.out.println("Flugrichtung: " + veloAngle);
	if(latitude != -1)
		System.out.println("Latitude: "+latitude);
	if(longitude != -1)
		System.out.println("Longitude: "+longitude);
	
	System.out.println("FlightNo: "+flightNo);
}
public String toJedisKey()
{
	return String.valueOf(aircraftID);
}
public String toJedisString()
{
	String res = aircraftID+","+flightNo+","+velocity+","+veloAngle+","+latitude+","+longitude+","+lastOdd+",";
	if(msg_even != null)
		res += msg_even.toJedisString();
	res += ",";
	if(msg_odd != null)
		res += msg_odd.toJedisString();
	//res +=",";
	return res;
}
public String toString()
{
	return super.toString()+
		", velocity: " +getVelocity()+
		", veloAngle: " +getVeloAngle()+
		", lastOdd: " +getLastOdd()+
		", aircraftID: " +getID()+
		", latitude: " +latitude+
		", longitude: " +longitude;

}/* toString() hinzugefuegt - glkeit00 */





}
