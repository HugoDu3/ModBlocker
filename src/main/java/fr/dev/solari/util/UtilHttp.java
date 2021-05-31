package fr.dev.solari.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class UtilHttp {
    private UtilHttp(){}

    public static final String USER_AGENT = "ModBlocker";

    private static final JsonParser PARSER = new JsonParser();

    public static JsonElement getJsonFromUrl(String address) {
        try {
            URL url = new URL(address);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("User-Agent", USER_AGENT);

            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);

            return PARSER.parse(reader);
        }
        catch (IOException ex) {
            return null;
        }
    }
}
