package me.sgeb.healthwatch.hgclient;

import me.sgeb.healthwatch.hgclient.model.User;
import me.sgeb.healthwatch.hgclient.model.WeightSetFeed;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;

public interface HgClient {
    final String ACCEPT = "Accept: ";

    @GET("/user")
    @Headers(ACCEPT + "application/vnd.com.runkeeper.User+json")
    void getUser(Callback<User> cb);

    @GET("/weight")
    @Headers(ACCEPT + "application/vnd.com.runkeeper.WeightSetFeed+json")
    void getWeightSetFeed(Callback<WeightSetFeed> cb);
}
