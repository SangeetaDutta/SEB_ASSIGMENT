package se.seb.gossip;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import se.seb.client.PerformOperation;

public class GossipReplicationService {
    PerformOperation operation;
	private String string;
	
    public void sendGossip(Node node, String key, String value) throws IOException {
        operation.setKeyValu(key, value, node.getInetAddress().toString());
    }

    public String receiveGossipValue(Node node, String key) {
        String[] keyValue = operation.getKeyValue(key, node.getInetAddress().toString());
        if(keyValue[0].equals("200")) 	
        	string = keyValue[1]; 
        return string;
    }

}
