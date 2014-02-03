package me.sgeb.healthwatch.hgclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class HgClientHelper {

    private static final String BASE_URL = "https://api.runkeeper.com";

    public static HgClient createClient(final String accessToken) {
        // Inject authorization header into each request
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade requestFacade) {
                requestFacade.addHeader("Authorization", "Bearer " + accessToken);
            }
        };

        // Accommodate for HealthGraph's date format
        Gson gson = new GsonBuilder().setDateFormat("EEE, d MMM yyyy HH:mm:ss").create();
        GsonConverter converter = new GsonConverter(gson);

        // Create and return the restAdapter
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setServer(BASE_URL)
                .setRequestInterceptor(requestInterceptor)
                .setConverter(converter)
//                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        return restAdapter.create(HgClient.class);
    }

    public static String getResponseHeadersAsString(Response response) {
        StringBuilder sb = new StringBuilder();
        if (response != null) {
            for (Header header : response.getHeaders()) {
                sb.append(header.getName() + ": " + header.getValue() + "\n");
            }
        }
        return sb.toString();
    }

    public static String getResponseBodyAsString(Response response) {
        String theString = "";
        if (response != null) {
            InputStream in = null;
            try {
                in = response.getBody().in();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Scanner scanner = new Scanner(in, "UTF-8").useDelimiter("\\A");
            theString = scanner.hasNext() ? scanner.next() : "";
            scanner.close();
        }
        return theString;
    }

}
