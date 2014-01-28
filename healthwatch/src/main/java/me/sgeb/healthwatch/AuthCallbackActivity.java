package me.sgeb.healthwatch;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        Uri intentData = getIntent().getData();
        new AuthClient().tradeIntentDataForAccessToken(intentData, new Callback<String>() {
            @Override
            public void success(String accessToken, Response response) {
                new Preferences(AuthCallbackActivity.this).setAuthAccessToken(accessToken);

                String message = getString(R.string.auth_callback_authorized);
                Toast.makeText(AuthCallbackActivity.this, message, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(AuthCallbackActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

                finish();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                String message = String.format(getString(R.string.auth_callback_auth_error),
                        retrofitError.getResponse().getStatus());
                textView.setText(message);
                textView.setVisibility(View.VISIBLE);
            }
        });
    }
}
