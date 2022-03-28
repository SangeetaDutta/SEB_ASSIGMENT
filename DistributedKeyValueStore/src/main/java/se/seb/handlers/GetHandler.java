package se.seb.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import se.seb.store.KeyValueStore;

import java.io.IOException;
import java.io.OutputStream;

@SuppressWarnings("restriction")
public class GetHandler implements HttpHandler {

    private final static KeyValueStore keyValueStore = KeyValueStore.getInstance();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String path = exchange.getRequestURI().getPath();
        byte[] response ;
        int responseCode ;
        if (path == null){
            response = "INVALID_REQUEST".getBytes();
            responseCode = 403;
        }else {
            String[] s = path.split("/");
            for (String string : s) {
            	System.out.println(" String : " + string);
			}
            if (s.length < 2 || s[2].isEmpty()) {
                response = "NOT_FOUND".getBytes();
                responseCode = 404;
            } else {
                String key = s[2];
                String value = keyValueStore.get(key);
                if (value == null) {
                	response = "NOT_FOUND".getBytes();
                	responseCode = 404;
                } else {
                  response = value.getBytes();
                  responseCode = 200;
              }
            }
        }
        exchange.sendResponseHeaders(responseCode, response.length);
        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }
}
