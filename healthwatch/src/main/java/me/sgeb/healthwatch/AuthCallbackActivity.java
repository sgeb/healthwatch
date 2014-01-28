package me.sgeb.healthwatch;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.sgeb.healthwatch.hgclient.AuthClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AuthCallbackActivity extends Activity {

    @InjectView(R.id.auth_callback_textview)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_callback);
        ButterKnife.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        textView.setVisibility(View.INVISIBLE);

        Uri intentData = getIntent().getData();
        new AuthClient().tradeIntentDataForAccessToken(intentData, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                String message = String.format(getString(R.string.auth_callback_authorized), s);
                textView.setText(message);
                textView.setVisibility(View.VISIBLE);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                String message = String.format(getString(R.string.auth_callback_not_authorized),
                        retrofitError.getResponse().getStatus());
                textView.setText(message);
                textView.setVisibility(View.VISIBLE);
            }
        });
    }
}
