package co.impic.stardewvalley.CustomFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import co.impic.stardewvalley.AnalyticsApplication;
import co.impic.stardewvalley.CustomRecyclerAdapter.VillagersListsRecyclerAdapter;
import co.impic.stardewvalley.R;

public class VillagersListsFragment extends Fragment {

    // Recycler View Variable
    protected RecyclerView mRecyclerView;
    protected VillagersListsRecyclerAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected JSONArray mDataset;

    Tracker mTracker;

    public static VillagersListsFragment newInstance() {
        VillagersListsFragment fragment = new VillagersListsFragment();
        return fragment;
    }


    public VillagersListsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDataset();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_villagers_lists, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Villagers Lists Page");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        AdView adView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("272DDD5399A8569C9C1C6A5967EC5282")
                .build();
        adView.loadAd(adRequest);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.VillagersListsRecyclerView);
        mLayoutManager = new GridLayoutManager(getActivity(), 2);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new VillagersListsRecyclerAdapter(getActivity(), mDataset, getFragmentManager());

        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);

        String[] splitSize = adView.getAdSize().toString().split("_");
        String[] splitSize2 = splitSize[0].split("x");

        final float scale = getResources().getDisplayMetrics().density;
        Integer bottomSize = (int) (Integer.parseInt(splitSize2[1]) * scale + 0.5f);

        mRecyclerView.setPadding(16, 16, 16, bottomSize);
    }

    @Override
    public void onResume() {
        super.onResume();

        mTracker.setScreenName("Villagers Lists Page");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void initDataset() {
        JSONArray objArr;
        String json = loadJSONFromAsset();
        try {
            JSONObject obj = new JSONObject(json);
            objArr = obj.getJSONArray("villagers");
            mDataset = objArr;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("json/villagers.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
