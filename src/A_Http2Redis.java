/* Http2Redis.java
   - fetches ADS-B sentences from the WEB server http://flugmon-it.hs-esslingen.de
   - publishes each sentence in redis under key="ads.sentence"
   please note: this requires a running Redis server instance
*/


// jedis import
import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

import redis.clients.jedis.Jedis;

public final class A_Http2Redis implements Runnable {
    
    // url for reading the ads sentences
    private final String url2 = "http://flugmon-it.hs-esslingen.de/subscribe/ads.sentence";
    private final AdsMessageFactory msgFactory = AdsMessageFactory.getInstance();
    
	// redis instance
    private Jedis jedisClient;

    
    // server task
    @Override 
    public void run () 
    {
	    byte[] buffer = new byte[101];
		try {
			
		    // make connection with the flugmon-it web server
		    
			URLConnection con = new URL(url2).openConnection();
			jedisClient = new Jedis("localhost");
		   
	
		    // open input stream for reading data
			BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
			int bytesRead=0;
		    
		    // we are a server, i.e. run forever
		    while (true) 
		    { 
		    	bytesRead = bis.read(buffer);
				String message = new String(buffer,0,bytesRead);;
				
				//Switching the messageTypes in order to deliver the correct MessageType-Objects
				//// and publish sentence in redis
				if(message.indexOf('!') > 0) 
				{
					AdsMessage msg = msgFactory.sentence2Message(message);
					if(msg != null)
					{
						publish(msg);
					}

				}
				
		    } // while
		} catch (Exception e) 
		{
		    e.printStackTrace ();
		} 
    } // @Override public void run()
    
    private void publish(AdsMessage msg)
    {
    	switch(msg.getMessageTypeD())
		{
		case 1: case 2: case 3: case 4: //Aircraft Identification Message
				jedisClient.publish("ads.msg.identification", ((AirborneIdentificationMessage)msg).toJedisString());
				break;
		case 9: //9-18, 20-22 Airborne Position Message
		case 10:case 11: case 12:case 13:case 14:case 15:case 16:case 17:case 18: case 20:case 21:
		case 22: jedisClient.publish("ads.msg.position", ((AirbornePositionMessage)msg).toJedisString());
				 break;
		case 19: jedisClient.publish("ads.msg.velocity", ((AirborneVelocityMessage)msg).toJedisString());
				 break;
		default: System.out.println("Unbekannter Message-Type@A_Http2Redis");break;
		}
    }
	public String toString()
	{
		return "";// super.toString()+
			//", Instance: " +getInstance()+
	}/* toString() hinzugefuegt - glkeit00 */
}