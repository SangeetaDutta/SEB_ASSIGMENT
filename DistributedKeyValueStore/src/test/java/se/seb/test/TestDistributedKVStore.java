package se.seb.test;

import se.seb.client.PerformOperation;
import se.seb.server.Config;
import se.seb.server.Server;

import java.io.IOException;

public class TestDistributedKVStore {

    private static Server serverA;
    private static PerformOperation performOperation;
    private static Config config;


    private static void testSetKeys(String address) throws IOException {
        String[] res = performOperation.setKeyValu("test","testset","http://"+address+"/set");
        assert res[0].equalsIgnoreCase("200");
        assert res[1].equalsIgnoreCase("OK");
    }

    private static void testGetKeys(String address){
        String[] res = performOperation.getKeyValue("test","http://"+address+"/get");
        assert res[1].equalsIgnoreCase("testset");
    }

    private static void testStandAlone(String address) {
        try {
            testSetKeys(String.valueOf(address));
            testGetKeys(String.valueOf(address));
            System.out.println("***=== Test success on standalone process ***===");
        }catch (Exception e){
            System.out.println("Test failed");
        }
    }

   private static void testOnOtherProcess(String thisAddress,String otherAddress){
        if (config.getNodesList().size() == 0){
            config.addNodes(otherAddress);
        }
        try {
            testSetKeys(otherAddress);
            testGetKeys(thisAddress);
            System.out.println("***=== Test success on other process ***===");
        }catch (IOException e){
            System.out.println("***=== Test failed check if remote server is up ***===");
        }
    }

    public static void main(String[] args) throws IOException {
        config = Config.setConfig(13000,new String[]{"localhost:13001"},"localhost:13000");
        serverA = new Server("localhost",config.getPort());
        serverA.startServer();
        performOperation = new PerformOperation();
        testStandAlone(config.getServerName());
        testOnOtherProcess(config.getServerName(),"localhost:13001");
       serverA.stopServer(0);
    }

}
