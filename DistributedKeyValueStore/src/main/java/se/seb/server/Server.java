package se.seb.server;

//import com.project.replication.Replicator;
import com.sun.net.httpserver.HttpServer;

import se.seb.handlers.GetHandler;
import se.seb.handlers.PutHandler;

import java.io.IOException;

import java.net.InetSocketAddress;


@SuppressWarnings("restriction")
public class Server {

	private final HttpServer server;
	private final InetSocketAddress address;

	public Server(String nodeIp, int port) throws IOException {
		address = new InetSocketAddress(nodeIp,port);
		server = HttpServer.create();
		server.bind(address,50);
	}


	public void startServer(){
		server.createContext("/get",new GetHandler());
		server.createContext("/set",new PutHandler());
		server.start();
		System.out.println("Server started on  "+ address.toString() + " name: "+Config.getConfig().getServerName());
	}

	public void startAllNodes(String[] nodes) {
		try {
			for (String node : nodes) {
				new Server(node.split(":")[0],Integer.parseInt(node.split(":")[1])).startServer();
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void stopServer(int delay){
		if (server != null){
			if (delay >= 0) {
				server.stop(delay);
			}else {
				server.stop(0);
			}
		}
	}




}
