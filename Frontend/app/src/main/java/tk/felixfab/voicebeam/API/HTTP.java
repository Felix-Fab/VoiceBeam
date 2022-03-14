package tk.felixfab.voicebeam.API;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTP {

    public static JSONObject getJSONBody(HttpURLConnection connection) throws IOException, JSONException {

        InputStream inputStream;

        if (100 <= connection.getResponseCode() && connection.getResponseCode() <= 399) {
            inputStream = connection.getInputStream();
        } else {
            inputStream = connection.getErrorStream();
        }

        String response;

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(inputStream, "utf-8"))) {
            StringBuilder responseString = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                responseString.append(responseLine.trim());
            }
            response = responseString.toString();
        }

        System.out.println(response);

        return new JSONObject(response);
    }

    public static HttpURLConnection createDefaultConnection(String URL,String RequestMethod) throws IOException {

        java.net.URL url = new URL(URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(RequestMethod);

        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        con.setRequestProperty("Content-Type", "application/json");

        return con;
    }

}
