package factory;
import translator.NumberOfLongitudeZones;
import domain.AirbornePositionMessage;
import domain.Aircraft;


public final class AircraftFactory
{
	private static AircraftFactory instance;
	
	private AircraftFactory(){}
	
	public static AircraftFactory getInstance()
	{
		if(instance == null)
			instance = new AircraftFactory();
		return instance;
	}
	public Aircraft message2Aircraft(String msg)
	{
		String entry [] = msg.split(";");
		

		return new Aircraft(Integer.parseInt(entry[1]));
	}
	public void updateIdentification(String msg, Aircraft aircraft)
	{
		String entry [] = msg.split(";");
		aircraft.setFlightNo(entry[2]);
	}
	public void updateVelocity(String msg, Aircraft aircraft)
	{
		String entry [] = msg.split(";");
		System.out.println("Update"+msg);
		aircraft.setVelocity(Double.parseDouble(entry[2]));
		aircraft.setVeloAngle(Double.parseDouble(entry[3]));
	}
	public void updatePosition(String msg, Aircraft aircraft)
	{
		
		AirbornePositionMessage adsMsg = new AirbornePositionMessage(msg);
		if(adsMsg.isOdd())
			aircraft.setOddMsg(adsMsg);
		else
			aircraft.setEvenMsg(adsMsg);
		calculatePosition(aircraft);
	}
	


//decodes the position data from 2 objects. The returned object is the one with the coordinates considered the "real" ones. 
//returns null if data is not usable (different Latitude zones) 
	private static void calculatePosition(Aircraft aircraft)//this has Type 1, passed obj has type 0, last one received has type i 
	{ 
		AirbornePositionMessage obj_even=aircraft.getEven();
		AirbornePositionMessage obj_odd=aircraft.getOdd();
		int i=aircraft.getLastOdd();
		NumberOfLongitudeZones calcNL = NumberOfLongitudeZones.getInstance();
		if(obj_even != null && obj_odd != null)
		{
			boolean lonZonesEqual = true;
			double AirDlat0 = 6.0, AirDlat1 = (360.0/59.0); 
			int j = (int) Math.floor(( (double)(59*obj_even.getLatitude()-60*obj_odd.getLatitude()) / 131072.0)+0.5); //zone index 
			double rlat0 = AirDlat0 * (((double)modulo(j, 60) + ((double)obj_even.getLatitude()/131072.0))); //calculate recovered latitude 
			double rlat1 = AirDlat1 * (((double)modulo(j, 59) + ((double)obj_odd.getLatitude()/131072.0))); //last one received will be considered the actual position 
			int nl = calcNL.nl(rlat0); //will be often used later on 
			if (nl != calcNL.nl(rlat1))
				lonZonesEqual = false;//  if Longitude zones are not the same 
			int m = (int) Math.floor(( (double)( ((nl-1)*obj_even.getLongitude())-(nl*obj_odd.getLongitude())) / 131072.0)+0.5); 
			double rlong0 = (360.0/(double)(nl))*( (double)modulo(m, nl) + ((double)obj_even.getLongitude()/131072.0) ); //recovered longitude 
			double rlong1 = (360.0/(double)(nl-1))*( (double)modulo(m, nl-1) + ((double)obj_odd.getLongitude()/131072.0) ); //recovered longitude (also last one == valid) 
			
			if(lonZonesEqual)
			{
				if(i == 1 ) 
				{
					aircraft.setLongitude(rlong1);
					aircraft.setLatitude(rlat1);
				}
				else 
				{
					aircraft.setLongitude(rlong0);
					aircraft.setLatitude(rlat0);
				}
			}
		}
	}
	private static int modulo (int x, int y) 
	{
	int mod = x % y;
	if ( x<0 && mod!=0) mod = mod + y;
		return mod;
	}
}
