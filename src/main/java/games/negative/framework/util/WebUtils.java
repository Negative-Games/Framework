package games.negative.framework.util;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Interact with HTTP requests with ease
 * @author Seailz
 */
public class WebUtils {

    /**
     * Get a JSONObject from a URL
     * @param url THe URL
     * @author Negative - modified by Seailz
     * @return JSON Object from the HTTP request
     */
    @SneakyThrows
    public JSONObject sendGetRequest(@NotNull String url) {
        URL rawURL = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) rawURL.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        connection.disconnect();

        if (content.toString().isEmpty()) {
            return null;
        }

        JSONObject json = new JSONObject(content.toString());
        return json;
    }

    /**
     * Send a POST request to a URL
     * @param url URL
     * @param data Data to send
     * @throws IOException If the request fails
     * @author Seailz
     */
    public void sendPostRequest(@NotNull String url, @NotNull String data) throws IOException {
        URL rawURL = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) rawURL.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.getOutputStream().write(data.getBytes());
        connection.getOutputStream().flush();
        connection.getOutputStream().close();
        connection.disconnect();
    }

}