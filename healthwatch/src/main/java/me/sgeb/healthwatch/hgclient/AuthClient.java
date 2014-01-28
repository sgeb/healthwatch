package me.sgeb.healthwatch.hgclient;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public class AuthClient {

    private static final String AUTH_BASE_URL = "https://runkeeper.com/apps";
    private static final String AUTH_USER_AUTHORIZE_PATH = "/authorize";
    private static final String AUTH_TOKEN_PATH = "/token";
    private static final String REDIRECT_URI = "healthwatch://localhost/authcallback";

    private final AuthKeys myAuthKeys;
    private final RestAdapter restAdapter;
    private final AuthService authService;

    public AuthClient() {
        myAuthKeys = new AuthKeys();
        restAdapter = new RestAdapter.Builder()
                .setServer(AUTH_BASE_URL)
//                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        authService = restAdapter.create(AuthService.class);
    }

    public String getUserAuthorizeUri() {
        return Uri.parse(AUTH_BASE_URL + AUTH_USER_AUTHORIZE_PATH
                + "?response_type=code&client_id=" + myAuthKeys.getClientId())
                + "&redirect_uri=" + Uri.encode(REDIRECT_URI);
    }

    public void tradeIntentDataForAccessToken(Uri intentData, Callback<String> cb) {
        if (intentData != null) {
            String authCode = intentData.getQueryParameter("code");
            fetchAccessToken(authCode, cb);
        }
    }

    private void fetchAccessToken(String authCode, final Callback<String> cb) {
        authService.fetchAccessToken("authorization_code", authCode, myAuthKeys.getClientId(),
                myAuthKeys.getClientSecret(), REDIRECT_URI, new Callback<AccessTokenResponse>() {
            @Override
            public void success(AccessTokenResponse atr, Response response) {
                cb.success(atr != null ? atr.accessToken : null, response);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                cb.failure(retrofitError);
            }
        });
    }

    private class AccessTokenResponse {
        @SerializedName("access_token")
        private String accessToken;
    }

    private interface AuthService {

        @FormUrlEncoded
        @POST(AUTH_TOKEN_PATH)
        void fetchAccessToken(@Field("grant_type") String grantType,
                              @Field("code") String code,
                              @Field("client_id") String clientId,
                              @Field("client_secret") String clientSecret,
                              @Field("redirect_uri") String redirectUri,
                              Callback<AccessTokenResponse> cb);

    }
}
