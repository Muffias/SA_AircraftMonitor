import java.io.IOException;
import java.net.InetSocketAddress;
import redis.clients.jedis.Jedis;
import com.sun.net.httpserver.HttpServer;

public class Main {
public static void main(String[] args) throws IOException
{
	Jedis jed = new Jedis ("localhost");
	JedisAircraftServer myAircraftServer = JedisAircraftServer.getInstance();
	HttpServer server = HttpServer.create(new InetSocketAddress(3333), 0); 
    server.createContext( "/map.basic", WebServer.MapBasic.getInstance());
    server.createContext( "/active.kml", WebServer.ActiveKML.getInstance() );
    server.setExecutor(null); // create a default executor
    server.start();
    
    A_Http2Redis localServer = A_Http2Redis.getInstance();
	Thread serverThread = new Thread (localServer);
	// make sure redis-server is running
	serverThread.start ();
	jed.subscribe(myAircraftServer, "ads.msg.identification", "ads.msg.velocity", "ads.msg.position");
}
}
