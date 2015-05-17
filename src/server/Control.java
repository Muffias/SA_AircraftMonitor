package server;


import java.io.IOException;
import java.net.InetSocketAddress;
import redis.clients.jedis.Jedis;
import com.sun.net.httpserver.HttpServer;

public final class Control {
public static void main(String[] args) throws IOException
{
	Jedis jed = new Jedis ("localhost");
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
}