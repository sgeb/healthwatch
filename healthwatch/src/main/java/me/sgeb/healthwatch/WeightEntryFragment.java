package me.sgeb.healthwatch;


import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.testflightapp.lib.TestFlight;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity) getActivity()).hideNavigationDrawerIndicator();
        setHasOptionsMenu(true);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weight_entry, container, false);
        ButterKnife.inject(this, view);
        TestFlight.passCheckpoint(MyCheckpoints.WEIGHT_ENTRY_ONCREATEVIEW);

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

        TestFlight.passCheckpoint(MyCheckpoints.WEIGHT_ENTRY_ONDESTROYVIEW);
        ButterKnife.reset(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.weight_entry_submit_button)
    public void submit() {
        disableViews();

        WeightSet weightSet = createWeightSetFromViews();
        String authAccessToken = new Preferences(getActivity()).getAuthAccessToken();
        HgClientHelper.createClient(authAccessToken).postWeightSet(weightSet, new ResponseCallback() {
            @Override
            public void success(Response response) {
                TestFlight.passCheckpoint(MyCheckpoints.WEIGHT_ENTRY_SUBMIT_SUCCESS);

                Toast.makeText(getActivity(), getString(R.string.weight_entry_submit_success), Toast.LENGTH_LONG).show();
                getActivity().onBackPressed();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                TestFlight.passCheckpoint(MyCheckpoints.WEIGHT_ENTRY_SUBMIT_FAILURE);

                Log.d("WeightEntry", "Save WeightSet failed");
                Log.d("WeightEntry", "Response headers: \n"
                        + HgClientHelper.getResponseHeadersAsString(retrofitError.getResponse()));
                Log.d("WeightEntry", "Response body: "
                        + HgClientHelper.getResponseBodyAsString(retrofitError.getResponse()));

                Toast.makeText(getActivity(), getString(R.string.weight_entry_submit_failure), Toast.LENGTH_LONG).show();
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
}
