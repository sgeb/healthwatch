package me.sgeb.healthwatch.hgclient;

import me.sgeb.healthwatch.hgclient.model.GeneralMeasurementSet;
import me.sgeb.healthwatch.hgclient.model.GeneralMeasurementSetFeed;
import me.sgeb.healthwatch.hgclient.model.User;
import me.sgeb.healthwatch.hgclient.model.WeightSet;
import me.sgeb.healthwatch.hgclient.model.WeightSetFeed;
import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

public interface HgClient {
    // Path constants
    final String PATH_WEIGHT = "/weight";
    final String PATH_USER = "/user";
    final String PATH_GENERAL_MEASUREMENT = "/generalMeasurements";

    // Header constants
    final String HEADER_ACCEPT = "Accept: ";
    final String HEADER_CONTENT_TYPE = "Content-Type: ";

    // User

    @GET(PATH_USER)
    @Headers(HEADER_ACCEPT + "application/vnd.com.runkeeper.User+json")
    void getUser(Callback<User> cb);

    // Weight

    @GET(PATH_WEIGHT)
    @Headers(HEADER_ACCEPT + "application/vnd.com.runkeeper.WeightSetFeed+json")
    void getWeightSetFeed(Callback<WeightSetFeed> cb);

    @POST(PATH_WEIGHT)
    @Headers(HEADER_CONTENT_TYPE + "application/vnd.com.runkeeper.NewWeightSet+json")
    void postWeightSet(@Body WeightSet weightSet, ResponseCallback cb);

    @DELETE(PATH_WEIGHT + "/{id}")
    void deleteWeighSet(@Path("id") String id, ResponseCallback cb);

    // General Measurement

    @GET(PATH_GENERAL_MEASUREMENT)
    @Headers(HEADER_ACCEPT + "application/vnd.com.runkeeper.GeneralMeasurementSetFeed+json")
    void getGeneralMeasurementSetFeed(Callback<GeneralMeasurementSetFeed> cb);

    @POST(PATH_GENERAL_MEASUREMENT)
    @Headers(HEADER_CONTENT_TYPE + "application/vnd.com.runkeeper.NewGeneralMeasurementSet+json")
    void postGeneralMeasurementSet(@Body GeneralMeasurementSet gmSet, ResponseCallback cb);

    @DELETE(PATH_GENERAL_MEASUREMENT + "/{id}")
    void deleteGeneralMeasurementSet(@Path("id") String id, ResponseCallback cb);
}
