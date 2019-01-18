package com.attendancesolution.bams.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.attendancesolution.bams.R;
import com.attendancesolution.bams.adapters.DBRecentAdapter;
import com.attendancesolution.bams.adapters.RecentAdapter;

/**
 * Created by Akshay on 23-Apr-16.
 */
public class RecentFragment extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecentAdapter adapter;
    DBRecentAdapter dbAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recent, null);

        initialize(view);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        openDB();
        /*dbAdapter.deleteAll();
        dbAdapter.insertRow("http://www.media.com", "Wednesday", "25-Mar-2016",0, 0);
        dbAdapter.insertRow("http://www.facebook.com", "Thursday", "26-Mar-2016", 0, 0);
        dbAdapter.insertRow("http://www.dailymotion.com", "Sunday", "27-Mar-2016", 0, 0);
        dbAdapter.insertRow("http://www.treehouse.com", "Monday", "28-Mar-2016", 0, 0);
        dbAdapter.insertRow("http://www.apple.com", "Tuesday", "29-Mar-2016", 0, 0);*/
        polpulateRecyclerViewfromDB();
    }

    private void polpulateRecyclerViewfromDB() {

        Cursor c = dbAdapter.getAllRows();

        if (c != null) {
            adapter = new RecentAdapter(getActivity(), c);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(linearLayoutManager);
        }

    }

    private void initialize(View view) {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_recent_list);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    }

    private void openDB() {
        dbAdapter = new DBRecentAdapter(getActivity());
        dbAdapter.open();
    }

    private void closeDB() {
        dbAdapter.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeDB();
    }
}
