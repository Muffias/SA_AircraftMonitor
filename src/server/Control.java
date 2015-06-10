package server;


import java.io.IOException;
import java.net.InetSocketAddress;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import com.sun.net.httpserver.HttpServer;

import exception.*;

public final class Control {
public static void main(String[] args) throws IOException
{
	
	try{
	Jedis jed = new Jedis("localhost");
	JedisAircraftServer myAircraftServer = new JedisAircraftServer();
	HttpServer server = HttpServer.create(new InetSocketAddress(3333), 0); 
    server.createContext( "/map.basic", new WebServer.MapBasic());
    server.createContext( "/active.kml", new WebServer.ActiveKML());
    server.setExecutor(null); // create a default executor
    server.start();
    
    A_Http2Redis localServer = new A_Http2Redis();
	Thread serverThread = new Thread (localServer);
	// make sure redis-server is running
	serverThread.start ();
	jed.subscribe(myAircraftServer, "ads.msg.identification", "ads.msg.velocity", "ads.msg.position");
	
	}
	catch(JedisConnectionException e)
	{
		JOptionPane.showMessageDialog(new JFrame(), "No Connection to Jedis.");
	}
	catch(AdsMessageException e)
	{
		System.out.println(e.getMessage());
	}
	catch(AdsMessageFactoryException e)
	{
		System.out.println(e.getMessage());
	}
	catch(AircraftException e)
	{
		System.out.println(e.getMessage());
	}
	catch(AircraftFactoryException e)
	{
		System.out.println(e.getMessage());
	}
	catch(SixBitASCIIException e)
	{
		System.out.println(e.getMessage());
	}
	catch(Http2RedisException e)
	{
		//System.out.println(e.getMessage());
		String errorText = e.getMessage();
		if(e.getErrorNumber() == 500)
			errorText += "\n Maybe there is missing a OPEN-VPN Connection to HS-Esslingen";
		JOptionPane.showMessageDialog(new JFrame(), errorText);
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	
}
}