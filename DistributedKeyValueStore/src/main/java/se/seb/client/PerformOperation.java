package se.seb.client;
import sun.net.www.protocol.http.HttpURLConnection;

import java.io.*;
import java.net.URL;
import java.util.Map;

public class PerformOperation {

    public String[] getKeyValue(String key, String url) {
        int responseCode = 404;
        StringBuilder response = new StringBuilder();
        System.out.println("Sending 'GET' request to URL : " + url + "/" + key);
        try {
            HttpURLConnection httpClient =
                    (HttpURLConnection) new URL(url + "/" + key).openConnection();
            httpClient.setRequestMethod("GET");
            responseCode = httpClient.getResponseCode();
            System.out.println("Response Code : " + responseCode + " from " + url);

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpClient.getInputStream()))) {

                String line;

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }

                System.out.println(response.toString());
            }
        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        }
        return new String[]{String.valueOf(responseCode), response.toString()};
    }


    public String[] setKeyValu(final String key, final String value, final String endPoint) throws IOException {

        try {
            HttpURLConnection httpClient =
                    (HttpURLConnection) new URL(endPoint + "/" + key).openConnection();
            httpClient.setRequestMethod("POST");
            OutputStream os = httpClient.getOutputStream();
            os.write(value.getBytes());
            os.flush();
            os.close();
            StringBuilder response;
            System.out.println("\nSending 'POST' request to URL : " + endPoint);
            int responseCode = httpClient.getResponseCode();
            System.out.println("Response Code : " + responseCode);
            response = new StringBuilder();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpClient.getInputStream()));

            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            //print result
            System.out.println(response.toString());

            return new String[]{String.valueOf(responseCode), response.toString()};
        } catch (Exception e) {
            System.out.println("error to the put call " + endPoint);
            throw new RuntimeException(e.getCause());
        }

    }

}
