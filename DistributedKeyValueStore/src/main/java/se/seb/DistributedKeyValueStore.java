package se.seb;

import se.seb.gossip.Gossip;
import se.seb.server.Config;
import se.seb.server.Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class DistributedKeyValueStore {

    public static void main(String[] args) {
    	FileInputStream fis = null;
        try {
            if (args.length < 1){
                System.out.println("No Config available, please provide sever port and peers address");
            }
            String configFileName = args[0];
            File file = new File(configFileName);
            fis = new FileInputStream(file);
            byte[] b = new byte[fis.available()];
            fis.read(b);
            fis.close();
            String configFileString = new String(b);
            System.out.println("***********************************************************************");
            System.out.println("Config:");
            System.out.println(configFileString);
            System.out.println("***********************************************************************");
            String[] lines = configFileString.split("\n");
            int port = Integer.parseInt(lines[0].split(":\\s")[1].trim());
            String allNodes = lines[1].split(":\\s")[1].trim().replaceAll("\\'","");
            String masterNodeName = lines[2].split(":\\s")[1].trim();
            String[] nodesList = allNodes.split(",");
            Config.setConfig(port,nodesList,masterNodeName);
            Server s = new Server(masterNodeName.split(":")[0], port);
            s.startServer();
            s.startAllNodes(nodesList);
            Gossip gossip = new Gossip();
            gossip.startGossip(masterNodeName,nodesList);
            System.out.println("******************************Started************************************");
        }catch (IOException e){
            System.out.println("Error Starting the Server : " + e);
        }finally {
        	
        }

    }
}
