package se.seb.store;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class KeyValueStore{

    private static KeyValueStore kvStore = null;

    private ConcurrentHashMap<String,String> keyValue;
    
    private final LinkedList<String> keysAppendList;  // for replication;

    public boolean put(String key, String value){
      keyValue.put(key,value);
      keysAppendList.add(key);
      if (keysAppendList.size() >= Integer.MAX_VALUE-1000){
          keysAppendList.clear();
      }
      return true;
    }
    
    public boolean update(String key, String value){
        keyValue.put(key,value);
        return true;
    }

    public String get(String key){
        return keyValue.get(key);
    }

    public static KeyValueStore getInstance(){
        if (kvStore == null){
            synchronized (KeyValueStore.class){
                if (kvStore == null){
                    kvStore = new KeyValueStore();
                }
            }
        }
        return kvStore;
    }
    
    private KeyValueStore(){
        keyValue = new ConcurrentHashMap<>();
        keysAppendList = new LinkedList<>();
    }

    public LinkedList<String> getKeysInAppendList(){
        return this.keysAppendList;
    }
}
