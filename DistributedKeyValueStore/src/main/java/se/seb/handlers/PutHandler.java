package se.seb.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import se.seb.server.Client;
import se.seb.server.Config;
import se.seb.store.KeyValueStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("restriction")
public class PutHandler implements HttpHandler {

	private final KeyValueStore kvStore = KeyValueStore.getInstance();

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		String path = exchange.getRequestURI().getPath();
		System.out.println("Receiving put call "+path);
		byte[] response = "SERVER_ERROR, CAN'T PROCESS THIS REQUEST TYPE".getBytes();
		int responseCode = 503;
		if (path == null) {
			response = "INVALID_REQUEST".getBytes();
			responseCode = 403;
		} else {
			String[] s = path.split("/");
			if (s.length < 3 || s[2].isEmpty()) {
				response = "NOT_FOUND".getBytes();
				responseCode = 404;
			} else {
				InputStreamReader isr = new InputStreamReader(exchange.getRequestBody());
				BufferedReader br = new BufferedReader(isr);
				int b;
				StringBuilder buf = new StringBuilder(512);
				while ((b = br.read()) != -1) {
					buf.append((char) b);
				}
				br.close();
				isr.close();
				String key = s[2];
				String val = buf.toString();
				kvStore.put(key, val);
				if (!val.isEmpty() && kvStore.put(key, val)) {
					response = "OK".getBytes();
					responseCode = 200;
					
				}
			}
		}
		exchange.sendResponseHeaders(responseCode, response.length);
		OutputStream os = exchange.getResponseBody();
		os.write(response);
		os.flush();
		os.close();
		System.out.println("Finished put call "+path);
	}



}
