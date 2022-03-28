package se.seb.gossip;

import java.net.InetSocketAddress;

public interface GossipUpdater {
	void update(InetSocketAddress inetSocketAddress);
}

