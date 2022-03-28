package se.seb.server;

import java.util.*;

public class Config {

    private static Config config;
    private final int port;
    private Set<String> nodesList;
    private final String serverName;
   
    private Config(int port, String[] allNodes, String masterNodeName){
        this.port = port;
        this.nodesList = new HashSet<>();
        this.serverName = masterNodeName;
        nodesList.addAll(Arrays.asList(allNodes));
    }

    public static Config getConfig(){
        if (config == null){
            throw new RuntimeException("Config not set");
        }
        return config;
    }

    public static Config setConfig(int port, String[] nodesList, String masterNodeName){
        config = new Config(port, nodesList, masterNodeName);
        return config;
    }

    public int getPort(){
        return this.port;
    }


    public Set<String> getNodesList(){
        return this.nodesList;
    }

    public void addNodes(String nodes){
        this.nodesList.add(nodes);
    }

    public String getServerName(){
        return this.serverName;
    }
}
