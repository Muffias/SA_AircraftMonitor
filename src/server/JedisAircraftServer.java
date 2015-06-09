package server;

import domain.Aircraft;
import exception.AdsMessageException;
import factory.AircraftFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;


public final class JedisAircraftServer extends JedisPubSub
{
	private final AircraftFactory aircraftFactory = AircraftFactory.getInstance();
	private Jedis jedisClient;
	
	@Override
	public void onMessage(String channel, String message)
	{
		//Aircraft update/Statische Methode der Aircraft Factory aufrufen, sodass die Nachricht verarbeitet werden kann
				if(jedisClient == null)
					jedisClient = new Jedis("localhost");
				String entry[] = message.split(";"); // 
				String aircraftString = jedisClient.get(entry[1]);
				Aircraft currentAircraft;
				if(aircraftString!=null)//�berpr�fung ob Aircraft bereits vorhanden
					currentAircraft = new Aircraft(aircraftString);
				else											//Wenn nicht, neues bauen
				{
					currentAircraft=aircraftFactory.message2Aircraft(message);
				}
				switch(channel)
				{
				case "ads.msg.identification":
					aircraftFactory.updateIdentification(message, currentAircraft); break;
				case "ads.msg.position":
					aircraftFactory.updatePosition(message, currentAircraft); break;
				case "ads.msg.velocity": 
					aircraftFactory.updateVelocity(message, currentAircraft); break;
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
}
