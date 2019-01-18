package com.attendancesolution.bams.views;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.attendancesolution.bams.R;
import com.attendancesolution.bams.adapters.DBRecentAdapter;
import com.attendancesolution.bams.adapters.RecentAdapter;

/**
 * Created by Akshay on 11-May-16.
 */
public class CustomDialog extends Dialog {

    Context context;
    Cursor cursor;
    String heading;
    TextView head;
    ImageView close;
    RecyclerView list;
    LinearLayoutManager linearLayoutManager;
    RecentAdapter adapter;
    DBRecentAdapter dbAdapter;


    public CustomDialog(Context context, String heading) {
        super(context);

        this.context = context;
        this.heading = heading;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_list_layout);
        initialize();
        openDB();
        switch (heading) {
            case "Links":
                cursor = dbAdapter.getAllLinks();
                break;
            case "Messages":
                cursor = dbAdapter.getAllMessages();
                break;
        }
        polpulateRecyclerViewfromDB();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                closeDB();
            }
        });
    }

    private void initialize() {

        head = (TextView) findViewById(R.id.tv_popup_head);
        close = (ImageView) findViewById(R.id.iv_close);
        linearLayoutManager = new LinearLayoutManager(context);
        list = (RecyclerView) findViewById(R.id.rv_list);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        head.setText(heading);

    }

    private void openDB() {
        dbAdapter = new DBRecentAdapter(context);
        dbAdapter.open();
    }

    private void closeDB() {
        dbAdapter.close();
    }

    private void polpulateRecyclerViewfromDB() {


        if (cursor != null) {
            adapter = new RecentAdapter(context, cursor);
            list.setAdapter(adapter);
            list.setLayoutManager(linearLayoutManager);
        }

    }


}
