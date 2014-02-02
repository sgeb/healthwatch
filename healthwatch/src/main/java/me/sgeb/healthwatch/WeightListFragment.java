package me.sgeb.healthwatch;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.testflightapp.lib.TestFlight;

import java.text.DateFormat;
import java.util.List;

import me.sgeb.healthwatch.hgclient.HgClient;
import me.sgeb.healthwatch.hgclient.HgClientHelper;
import me.sgeb.healthwatch.hgclient.model.WeightSet;
import me.sgeb.healthwatch.hgclient.model.WeightSetFeed;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class WeightListFragment extends ListFragment {

    private OnFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WeightListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        TestFlight.passCheckpoint(MyCheckpoints.WEIGHT_LIST_ONCREATEVIEW);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        TestFlight.passCheckpoint(MyCheckpoints.WEIGHT_LIST_ONDESTROYVIEW);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        refreshWeightList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.weightlist, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void refreshWeightList() {
        if (isResumed()) setListShown(false);
        HgClient client = HgClientHelper.createClient(new Preferences(getActivity()).getAuthAccessToken());
        client.getWeightSetFeed(new Callback<WeightSetFeed>() {
            @Override
            public void success(WeightSetFeed weightSetFeed, Response response) {
                TestFlight.passCheckpoint(MyCheckpoints.WEIGHT_LIST_FETCH_SUCCESS);
                setListAdapter(new WeightListAdapter(getActivity(),
                        R.layout.layout_list_weightlist_listitem, weightSetFeed.getItems()));
                setListShown(true);
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                TestFlight.passCheckpoint(MyCheckpoints.WEIGHT_LIST_FETCH_FAILURE);

                Log.d("WeightList", "Fetch weight list failed");
                Log.d("WeightList", "Response headers: \n"
                        + HgClientHelper.getResponseHeadersAsString(retrofitError.getResponse()));
                Log.d("WeightList", "Response body: "
                        + HgClientHelper.getResponseBodyAsString(retrofitError.getResponse()));

                Toast.makeText(getActivity(), getString(R.string.weight_list_fetch_failure), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.weightlist_menu_new_weight:
                navigateToWeightEntry();
                return true;
            case R.id.weightlist_menu_reload:
                refreshWeightList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void navigateToWeightEntry() {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new WeightEntryFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            // mListener.onFragmentInteraction(some_resource_id);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

    private class WeightListAdapter extends ArrayAdapter<WeightSet> {
        private final Activity context;
        private final int textViewResourceId;
        private final List<WeightSet> weightSets;

        private WeightListAdapter(Activity context, int textViewResourceId, List<WeightSet> weightSets) {
            super(context, textViewResourceId, weightSets);
            this.context = context;
            this.textViewResourceId = textViewResourceId;
            this.weightSets = weightSets;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            WeightSet weightSet = weightSets.get(position);

            View rowView = convertView;
            if (rowView == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                rowView = inflater.inflate(textViewResourceId, null);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.date = (TextView) rowView.findViewById(R.id.weightlist_date_textview);
                viewHolder.weight = (TextView) rowView.findViewById(R.id.weightlist_weight_textview);
                viewHolder.fatPercent = (TextView) rowView.findViewById(R.id.weightlist_fatpercent_textview);
                rowView.setTag(viewHolder);
            }

            ViewHolder holder = (ViewHolder) rowView.getTag();
            holder.weightSetUri = weightSet.getUri();

            String dateStr = DateFormat.getDateInstance().format(weightSet.getTimestamp());
            holder.date.setText(dateStr);

            Double weight = weightSet.getWeight();
            if (weight == null) {
                holder.weight.setVisibility(View.GONE);
                holder.weight.setText(null);
            } else {
                holder.weight.setVisibility(View.VISIBLE);
                holder.weight.setText(String.format("%.1f kg", weight));
            }

            Double fatPercent = weightSet.getFatPercent();
            if (fatPercent == null) {
                holder.fatPercent.setVisibility(View.GONE);
                holder.fatPercent.setText(null);
            } else {
                holder.fatPercent.setVisibility(View.VISIBLE);
                holder.fatPercent.setText(String.format("%.1f %%", fatPercent));
            }

            return rowView;
        }
    }

    private class ViewHolder {
        public TextView date;
        public TextView weight;
        public TextView fatPercent;
        public String weightSetUri;
    }
}
