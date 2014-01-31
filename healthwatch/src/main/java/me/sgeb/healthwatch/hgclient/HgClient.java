package me.sgeb.healthwatch.hgclient;

import me.sgeb.healthwatch.hgclient.model.User;
import me.sgeb.healthwatch.hgclient.model.WeightSet;
import me.sgeb.healthwatch.hgclient.model.WeightSetFeed;
import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;

public interface HgClient {
    final String ACCEPT = "Accept: ";
    final String CONTENT_TYPE = "Content-Type: ";

    @GET("/user")
    @Headers(ACCEPT + "application/vnd.com.runkeeper.User+json")
    void getUser(Callback<User> cb);

    @GET("/weight")
    @Headers(ACCEPT + "application/vnd.com.runkeeper.WeightSetFeed+json")
    void getWeightSetFeed(Callback<WeightSetFeed> cb);

    @POST("/weight")
    @Headers(CONTENT_TYPE + "application/vnd.com.runkeeper.NewWeightSet+json")
    void postWeightSet(@Body WeightSet weightSet, ResponseCallback cb);
}
