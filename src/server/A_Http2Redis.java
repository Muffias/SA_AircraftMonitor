package server;



// jedis import
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.IIOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import domain.AdsMessage;
import domain.AirborneIdentificationMessage;
import domain.AirbornePositionMessage;
import domain.AirborneVelocityMessage;
import exception.Http2RedisException;
import factory.AdsMessageFactory;
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
	    URLConnection con = null;
	    String message = null;
	    AdsMessage msg = null;

		try {
			
		    // make connection with the flugmon-it web server
		    
			con = new URL(url2).openConnection();
			jedisClient = new Jedis("localhost");
		   
	
		    // open input stream for reading data
			BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
			int bytesRead=0;
		    
		    // we are a server, i.e. run forever
		    while (true) 
		    { 
		    	bytesRead = bis.read(buffer);
		    	message = new String(buffer,0,bytesRead);;
				System.out.println("message::::"+message);
				System.out.println("message::::"+message.substring(0, 38));
				//Switching the messageTypes in order to deliver the correct MessageType-Objects
				//// and publish sentence in redis
				System.out.println(message.substring(0, 38));
				if("{\"subscribe\":[\"message\",\"ads.sentence\"".equals(message.substring(0, 38))  && message.indexOf('!') > 0) //{"subscribe":["message","ads.sentence"....!ADS-B*...  <--Strings from Flugmon server look like this
				{
					msg = msgFactory.sentence2Message(message);
					if(msg != null)
					{
						publish(msg);
					}

				}
				/*else if(!message.equals("{\"subscribe\":[\"subscribe\",\"ads.sentence\",1]}"))
				{
					throw new Http2RedisException(501, "String received from Flugmon werbserver does not match the pattern. Message is checked from pos 0 to 38 and must be equal to :\n{\"subscribe\":[\"message\",\"ads.sentence\".",buffer,con,message);
				}*/
				
		    } // while
		} 
		catch(IOException e)
		{
			JOptionPane.showMessageDialog(new JFrame(),  "No. 500: Unknown Error while trying to open connection or while trying to read data from Flugmon-server.\nMaybe no OpenVPN-Exception (HS-ESSLINGEN) is established.");
			//throw new Http2RedisException(500, "Unknown Error while trying to open connection or while trying to read data from Flugmon-server.");
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
		case 0: case 5: case 6: case 7: case 8: case 23: case 24: case 25: case 26: case 27: case 28: case 29: case 30: case 31: //Message Types from 0 to 31 are allowed. Those listed here are reserverd and not handled in this program.
				System.err.println("Unhandeled Message-Type@A_Http2Redis. Type: "+ msg.getMessageTypeD());break;
		default:  throw new Http2RedisException(502, "Unknown Message type received from server. Message Type exeeded the given range (0-31): "+msg.getMessageTypeD());
		}
    }
}