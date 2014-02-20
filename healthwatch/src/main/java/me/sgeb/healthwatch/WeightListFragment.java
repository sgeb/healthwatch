package me.sgeb.healthwatch;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.testflightapp.lib.TestFlight;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import me.sgeb.healthwatch.hgclient.HgClient;
import me.sgeb.healthwatch.hgclient.HgClientHelper;
import me.sgeb.healthwatch.hgclient.model.WeightSet;
import me.sgeb.healthwatch.hgclient.model.WeightSetFeed;
import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the
 * {@link WeightListFragment.OnFragmentInteractionListener} interface.
 */
public class WeightListFragment extends ListFragment {

    private OnFragmentInteractionListener mListener;
    private HgClient hgClient;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WeightListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TestFlight.passCheckpoint(MyCheckpoints.WEIGHT_LIST_ONCREATEVIEW);
        hgClient = HgClientHelper.createClient(new Preferences(getActivity()).getAuthAccessToken());
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
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
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.weightlist, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshWeightList();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void refreshWeightList() {
        if (isResumed()) {
            // Show spinner when refreshing
            setListShown(false);
        }

        hgClient.getWeightSetFeed(new Callback<WeightSetFeed>() {
            @Override
            public void success(WeightSetFeed weightSetFeed, Response response) {
                TestFlight.passCheckpoint(MyCheckpoints.WEIGHT_LIST_FETCH_SUCCESS);

                Log.d("xxx", "weightSetFeed size: " + weightSetFeed.getItems().size());
                Log.d("xxx", "weightSetFeed: " + weightSetFeed.toString());

                // TODO: can throw NPE - instantiate and assign final
                new Datastore(getActivity()).updateWithRemoteWeightSetFeed(weightSetFeed).apply();

                if (isResumed()) {
                    final WeightListAdapter adapter = new WeightListAdapter(getActivity(),
                            R.layout.layout_list_weightlist_listitem, weightSetFeed.getItems());
                    setListAdapter(adapter);
                    setupListView(getListView(), adapter);
                    setListShown(true);
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                TestFlight.passCheckpoint(MyCheckpoints.WEIGHT_LIST_FETCH_FAILURE);

                Log.d("WeightList", "Fetch weight list failed");
                Log.d("WeightList", "Response headers: \n"
                        + HgClientHelper.getResponseHeadersAsString(retrofitError.getResponse()));
                Log.d("WeightList", "Response body: "
                        + HgClientHelper.getResponseBodyAsString(retrofitError.getResponse()));

                Toast.makeText(getActivity(),
                        R.string.weight_list_fetch_failure, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupListView(final ListView listView, final WeightListAdapter adapter) {
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new MyMultiChoiceModeListener(adapter));
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                listView.setItemChecked(position, !adapter.isPositionChecked(position));
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.weightlist_menu_new_weight:
                navigateToWeightEntryFragment();
                return true;
            case R.id.weightlist_menu_reload:
                refreshWeightList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void navigateToWeightEntryFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new WeightEntryFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        v.setSelected(true);

        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
//            mListener.onFragmentInteraction(some_resource_id);
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

        void hideNavigationDrawerIndicator();
    }

    private class WeightListAdapter extends ArrayAdapter<WeightSet> {
        private final Activity context;
        private final int textViewResourceId;
        private final List<WeightSet> weightSets;
        private List<Integer> selection;

        private WeightListAdapter(Activity context, int textViewResourceId, List<WeightSet> weightSets) {
            super(context, textViewResourceId, weightSets);
            this.context = context;
            this.textViewResourceId = textViewResourceId;
            this.weightSets = weightSets;
            this.selection = new ArrayList<Integer>();
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

        public void clearSelection() {
            selection.clear();
            notifyDataSetChanged();
        }

        public void addSelection(int position) {
            selection.add(position);
            notifyDataSetChanged();
        }

        public boolean isPositionChecked(int position) {
            return selection.contains(position);
        }

        public void removeSelection(int position) {
            // without cast remove(int) instead of remove(Object) is chosen
            selection.remove((Integer) position);
            notifyDataSetChanged();
        }

        public int countSelection() {
            return selection.size();
        }

        public List<WeightSet> getSelectedWeightSets() {
            List<WeightSet> result = new ArrayList<WeightSet>();
            for (Integer integer : selection) {
                result.add(weightSets.get(integer));
            }
            return result;
        }
    }

    private class ViewHolder {
        public TextView date;
        public TextView weight;
        public TextView fatPercent;
        public String weightSetUri;
    }

    private class MyMultiChoiceModeListener implements AbsListView.MultiChoiceModeListener {
        private final WeightListAdapter adapter;

        public MyMultiChoiceModeListener(WeightListAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            adapter.clearSelection();
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            getActivity().getMenuInflater().inflate(R.menu.contextual_weight_actionbar, menu);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.item_delete:
                    deleteWeightSets(actionMode, adapter.getSelectedWeightSets());
                    return true;
            }
            return false;
        }

        private void deleteWeightSets(final ActionMode actionMode, List<WeightSet> weightSets) {
            for (WeightSet weightSet : weightSets) {
                hgClient.deleteWeighSet(weightSet.getId(), new ResponseCallback() {
                    @Override
                    public void success(Response response) {
                        TestFlight.passCheckpoint(MyCheckpoints.WEIGHT_DELETE_SUCCESS);
                        Toast.makeText(getActivity(),
                                R.string.weight_delete_success, Toast.LENGTH_LONG).show();

                        adapter.clearSelection();
                        actionMode.finish();
                        refreshWeightList();
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        TestFlight.passCheckpoint(MyCheckpoints.WEIGHT_DELETE_FAILURE);

                        Log.d("WeightList", "Fetch weight list failed");
                        Log.d("WeightList", "Response headers: \n"
                                + HgClientHelper.getResponseHeadersAsString(retrofitError.getResponse()));
                        Log.d("WeightList", "Response body: "
                                + HgClientHelper.getResponseBodyAsString(retrofitError.getResponse()));

                        Toast.makeText(getActivity(),
                                R.string.weight_delete_failure, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        @Override
        public void onItemCheckedStateChanged(ActionMode actionMode, int position,
                                              long id, boolean checked) {
            if (checked) {
                adapter.addSelection(position);
            } else {
                adapter.removeSelection(position);
            }
            actionMode.setTitle(adapter.countSelection() + " selected");
        }
    }
}
