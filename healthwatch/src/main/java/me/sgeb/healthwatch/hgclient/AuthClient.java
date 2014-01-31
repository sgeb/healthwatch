package me.sgeb.healthwatch.hgclient;

import android.net.Uri;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import me.sgeb.healthwatch.MySecretKeys;
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
    private static final String CALLBACK_QUERY_PARAM_ERROR = "error";
    private static final String CALLBACK_QUERY_PARAM_CODE = "code";

    private final MySecretKeys mySecretKeys;
    private final RestAdapter restAdapter;
    private final AuthService authService;

    public AuthClient() {
        mySecretKeys = new MySecretKeys();
        restAdapter = new RestAdapter.Builder()
                .setServer(AUTH_BASE_URL)
//                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        authService = restAdapter.create(AuthService.class);
    }

    public String getUserAuthorizeUri() {
        return Uri.parse(AUTH_BASE_URL + AUTH_USER_AUTHORIZE_PATH
                + "?response_type=code&client_id=" + mySecretKeys.getClientId())
                + "&redirect_uri=" + Uri.encode(REDIRECT_URI);
    }

    public boolean isIntentDataValid(Uri intentData) {
        if (intentData == null) {
            Log.d("AuthClient", "IntentData not valid: intentData is null");
            return false;
        }

        String error = intentData.getQueryParameter(CALLBACK_QUERY_PARAM_ERROR);
        if (error != null && !error.isEmpty()) {
            Log.d("AuthClient", "IntentData not valid: query param '"
                    + CALLBACK_QUERY_PARAM_ERROR + "': '" + error + "'");
            return false;
        }

        String code = intentData.getQueryParameter(CALLBACK_QUERY_PARAM_CODE);
        if (code == null || code.isEmpty()) {
            Log.d("AuthClient", "IntentData not valid: query param '"
                    + CALLBACK_QUERY_PARAM_CODE + "' is empty");
            return false;
        }

        return true;
    }

    public void tradeIntentDataForAccessToken(Uri intentData, Callback<String> cb) {
        String authCode = intentData.getQueryParameter(CALLBACK_QUERY_PARAM_CODE);
        fetchAccessToken(authCode, cb);
    }

    private void fetchAccessToken(String authCode, final Callback<String> cb) {
        authService.fetchAccessToken("authorization_code", authCode, mySecretKeys.getClientId(),
                mySecretKeys.getClientSecret(), REDIRECT_URI, new Callback<AccessTokenResponse>() {
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
        private String error;
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
