package me.sgeb.healthwatch;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.testflightapp.lib.TestFlight;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.sgeb.healthwatch.hgclient.AuthClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AuthCallbackActivity extends Activity {

    @InjectView(R.id.auth_callback_textview)
    TextView textView;

    @InjectView(R.id.auth_callback_progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_callback);
        ButterKnife.inject(this);
        TestFlight.passCheckpoint(MyCheckpoints.AUTH_CALLBACK_ONCREATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TestFlight.passCheckpoint(MyCheckpoints.AUTH_CALLBACK_ONRESUME);
        fetchAccessToken();
    }

    private void fetchAccessToken() {
        Uri intentData = getIntent().getData();
        AuthClient authClient = new AuthClient();

        if (!authClient.isIntentDataValid(intentData)) {
            TestFlight.passCheckpoint(MyCheckpoints.AUTH_CALLBACK_INTENT_DATA_INVALID);
            textView.setText("Not a valid auth callback, aborting.");
            textView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            authClient.tradeIntentDataForAccessToken(intentData, new Callback<String>() {
                @Override
                public void success(String accessToken, Response response) {
                    TestFlight.passCheckpoint(MyCheckpoints.AUTH_CALLBACK_FETCH_TOKEN_SUCCESS);
                    new Preferences(AuthCallbackActivity.this).setAuthAccessToken(accessToken);
                    redirectToMainActivity();
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    TestFlight.passCheckpoint(MyCheckpoints.AUTH_CALLBACK_FETCH_TOKEN_FAILURE);
                    String message = String.format(getString(R.string.auth_callback_auth_error),
                            retrofitError.getResponse().getStatus());
                    Log.d("xxx", "retrofit failure: " + message);
                    textView.setText(message);
                    textView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void redirectToMainActivity() {
        Toast.makeText(this, R.string.auth_callback_authorized, Toast.LENGTH_LONG)
                .show();

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

        finish();
    }
}
