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
import com.attendancesolution.bams.adapters.AttendanceAdapter2;
import com.attendancesolution.bams.adapters.DBAttendanceAdapter;

/**
 * Created by Akshay on 23-Apr-16.
 */
public class AttendanceFragment extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    AttendanceAdapter2 adapter;
    DBAttendanceAdapter dbAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_attendance, null);

        initialize(view);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        openDB();
        dbAdapter.deleteAll();
        dbAdapter.insertRow("13", "Wednesday", "Apr", 6, 3,1,1,1);
        dbAdapter.insertRow("14", "Thursday", "Apr", 4, 3,1,1,1);
        dbAdapter.insertRow("15", "Friday", "Apr", 7, 5,1,1,1);
        dbAdapter.insertRow("18", "Monday", "Apr", 3, 2,1,1,1);
        dbAdapter.insertRow("19", "Tuesday", "Apr", 6, 4,1,1,1);
        dbAdapter.insertRow("20", "Wednesday", "Apr", 4, 4,1,1,1);
        dbAdapter.insertRow("21", "Thursday", "Apr", 2, 2,1,1,1);
        dbAdapter.insertRow("22", "Friday", "Apr", 5, 1,1,1,1);
        dbAdapter.insertRow("25", "Monday", "Apr", 7, 6,1,1,1);
        dbAdapter.insertRow("26", "Tuesday", "Apr", 5, 2,1,1,1);
        dbAdapter.insertRow("27", "Wednesday", "Apr", 4, 3,1,1,1);
        dbAdapter.insertRow("28", "Thursday", "Apr", 5, 3,1,1,1);
        polpulateRecyclerViewfromDB();
    }

    private void polpulateRecyclerViewfromDB() {

        Cursor c = dbAdapter.getAllRows();

        if (c != null) {
            adapter = new AttendanceAdapter2(getActivity(), c);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(linearLayoutManager);
        }

    }

    private void initialize(View view) {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_attendance_list);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

    }

    private void openDB() {
        dbAdapter = new DBAttendanceAdapter(getActivity());
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
