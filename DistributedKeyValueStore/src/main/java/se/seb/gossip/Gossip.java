package se.seb.gossip;

import java.net.InetSocketAddress;
import java.time.Duration;

import se.seb.gossip.GossipConfiguration;
import se.seb.gossip.GossipService;

public class Gossip {
public void startGossip(String masterNodeName, String[] nodes) {
	String[] split = masterNodeName.split(":");
	String masterNode = split[0];
	String masterPort = split[1];
	GossipConfiguration gossipConfig = new GossipConfiguration(
			Duration.ofSeconds(10),
			Duration.ofSeconds(10),
			Duration.ofMillis(50000),
			Duration.ofMillis(50000),
			3
			);
	GossipService initialNode = new GossipService
			(new InetSocketAddress(masterNode, Integer.parseInt(masterPort)), gossipConfig);

	initialNode.setOnNewNodeHandler((inetSocketAddress) -> {
		System.out.println("Connected to " +
				inetSocketAddress.getHostName() + ":"
				+ inetSocketAddress.getPort());
	});

	initialNode.setOnFailedNodeHandler((inetSocketAddress) -> {
		System.out.println("Node " + inetSocketAddress.getHostName() + ":"
				+ inetSocketAddress.getPort() + " failed");
	});

	initialNode.setOnRemoveNodeHandler((inetSocketAddress) -> {
		System.out.println("Node " + inetSocketAddress.getHostName() + ":"
				+ inetSocketAddress.getPort() + " removed");
	});

	initialNode.setOnRevivedNodeHandler((inetSocketAddress) -> {
		System.out.println("Node " + inetSocketAddress.getHostName() + ":"
				+ inetSocketAddress.getPort() + " revived");
	});

	initialNode.start();

	for (int i = 1; i < nodes.length; i++) {
		GossipService gossipService = new GossipService
				(new InetSocketAddress(nodes[i].split(":")[0], Integer.parseInt(nodes[i].split(":")[1])),
						new InetSocketAddress(nodes[i-1].split(":")[0], Integer.parseInt(nodes[i-1].split(":")[1])), gossipConfig);
		gossipService.start();
	}
}
}

