import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;


public class JedisAircraftServer extends JedisPubSub{
	private static JedisAircraftServer instance;
	private static Jedis jedisClient;
	public static boolean bInit = false;
	
	private JedisAircraftServer(){}
	public static JedisAircraftServer getInstance()
	{
		if(instance == null)
			instance = new JedisAircraftServer();
		return instance;
	}
	@Override
	public void onMessage(String channel, String message) {
		//Aircraft update/Statische Methode der Aircraft Factory aufrufen, sodass die Nachricht verarbeitet werden kann
		//System.out.println("------------------\n");
		//System.out.println(channel+"\n");
		//System.out.println("NACHRICHT: "+message+"\n");
				if (!bInit)
				{
					jedisClient = new Jedis("localhost");
					bInit = true;
				}
				String entry[] = message.split(";"); // 
				//System.err.println(entry[0] + " " +entry[1] + " "+ entry[2]+ " "+ entry[3]);
				String aircraftString = jedisClient.get(entry[1]);//jedisClient.get((String)message.substring(message.indexOf(',')+1,message.indexOf('?')));
				//System.out.println("Air String: "+aircraftString);
				//((AdsMessage) adsMessage).getOriginatorD();
				Aircraft currentAircraft;
				if(aircraftString!=null)//Überprüfung ob Aircraft bereits vorhanden
					currentAircraft = new Aircraft(aircraftString);
				else											//Wenn nicht, neues bauen
				{
					currentAircraft=AircraftFactory.message2Aircraft(message);
					//jedisClient.set((String)message.substring(message.indexOf(',')+1,message.indexOf('?')), currentAircraft.toJedisString());
				}
				switch(channel)
				{
				case "ads.msg.identification":
					AircraftFactory.updateIdentification(message, currentAircraft); break;
				case "ads.msg.position":
					AircraftFactory.updatePosition(message, currentAircraft); break;
				case "ads.msg.velocity": 
					AircraftFactory.updateVelocity(message, currentAircraft); break;
					default: System.out.println("Unknown Message"); break;
				}
				jedisClient.set(currentAircraft.toJedisKey(),currentAircraft.toJedisString());
				jedisClient.expire(currentAircraft.toJedisKey(),300);
				currentAircraft.print();
				//GUI aufrufen/informieren
		
	}

	@Override
	public void onPMessage(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPSubscribe(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPUnsubscribe(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSubscribe(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnsubscribe(String arg0, int arg1) {
		// TODO Auto-generated method stub
	

	}

	public String toString()
	{
		return super.toString()+
			", Instance: " +getInstance()+
	}/* toString() hinzugefuegt - glkeit00 */
}
