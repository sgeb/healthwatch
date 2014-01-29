package me.sgeb.healthwatch.hgclient;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

public class HgClientFactory {

    private static final String BASE_URL = "https://api.runkeeper.com";

    public static HgClient createClient(final String accessToken) {
        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade requestFacade) {
                requestFacade.addHeader("Authorization", "Bearer " + accessToken);
            }
        };

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setServer(BASE_URL)
                .setRequestInterceptor(requestInterceptor)
//                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        return restAdapter.create(HgClient.class);
    }
}
