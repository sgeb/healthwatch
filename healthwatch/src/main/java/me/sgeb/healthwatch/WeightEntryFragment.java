package me.sgeb.healthwatch;


import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import me.sgeb.healthwatch.hgclient.HgClientHelper;
import me.sgeb.healthwatch.hgclient.model.WeightSet;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class WeightEntryFragment extends Fragment {

    @InjectView(R.id.weight_entry_weight_input)
    TextView weightInput;

    @InjectView(R.id.weight_entry_fat_percent_entry)
    TextView fatPercentInput;

    @InjectView(R.id.weight_entry_submit_button)
    Button submitButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weight_entry, container, false);
        ButterKnife.inject(this, view);

        submitButton.setEnabled(false);
        weightInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {  }

            @Override
            public void afterTextChanged(Editable s) {
                boolean shouldEnableSubmit = s.length() > 0;
                submitButton.setEnabled(shouldEnableSubmit);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.weight_entry_submit_button)
    public void submit() {
        disableViews();

        WeightSet weightSet = createWeightSetFromViews();
        String authAccessToken = new Preferences(getActivity()).getAuthAccessToken();
        HgClientHelper.createClient(authAccessToken).postWeightSet(weightSet, new ResponseCallback() {
            @Override
            public void success(Response response) {
                Toast.makeText(getActivity(), "Successfully submitted", Toast.LENGTH_LONG).show();
                resetInputViews();
                enableViews();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                Log.d("WeightEntry", "POST failed");
                Log.d("WeightEntry", "Response headers: \n"
                        + HgClientHelper.getResponseHeadersAsString(retrofitError.getResponse()));
                Log.d("WeightEntry", "Response body: "
                        + HgClientHelper.getResponseBodyAsString(retrofitError.getResponse()));
                Toast.makeText(getActivity(), "Could not submit entry", Toast.LENGTH_LONG).show();
                enableViews();
            }
        });
    }

    private WeightSet createWeightSetFromViews() {
        WeightSet weightSet = new WeightSet();
        weightSet.setWeight(getTextViewAsDouble(weightInput));
        weightSet.setFatPercent(getTextViewAsDouble(fatPercentInput));
        return weightSet;
    }

    private Double getTextViewAsDouble(TextView textView) {
        Double weight = null;
        if (textView != null && textView.getText() != null) {
            String weightStr = textView.getText().toString();
            if (weightStr != null && !weightStr.isEmpty()) {
                weight = Double.valueOf(weightStr);
            }
        }
        return weight;
    }

    private void disableViews() {
        weightInput.setEnabled(false);
        fatPercentInput.setEnabled(false);
        submitButton.setEnabled(false);
    }

    private void enableViews() {
        weightInput.setEnabled(true);
        fatPercentInput.setEnabled(true);
        submitButton.setEnabled(true);
    }

    private void resetInputViews() {
        weightInput.setText("");
        fatPercentInput.setText("");
    }
}
