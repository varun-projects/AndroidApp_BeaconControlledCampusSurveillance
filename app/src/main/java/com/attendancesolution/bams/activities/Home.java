package com.attendancesolution.bams.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.attendancesolution.bams.R;
import com.attendancesolution.bams.adapters.DBAttendanceAdapter;
import com.attendancesolution.bams.adapters.DBRecentAdapter;
import com.attendancesolution.bams.adapters.HomeAdapter;
import com.attendancesolution.bams.services.MyService;
import com.attendancesolution.bams.singletonlClasses.AppPrefs;
import com.attendancesolution.bams.views.CustomDialog;

/**
 * Created by Akshay on 06-May-16.
 */
public class Home extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    HomeAdapter adapter;
    DBAttendanceAdapter dbAdapter;
    DBRecentAdapter dbRecentAdapter;
    RelativeLayout links, messages;
    TextView n_link, n_message;
    ImageView iv_link, iv_message, demo, close;
    AppPrefs appPrefs;
    Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initialize();

        getSupportActionBar().hide();


    }

    @Override
    public void onResume() {
        super.onResume();
        openDB();
        openRecentDB();
        /*dbRecentAdapter.deleteAll();
        dbRecentAdapter.insertRow("http://www.media.com", "Wednesday", "25-Mar-2016", 0, 0);
        dbRecentAdapter.insertRow("http://www.facebook.com", "Thursday", "26-Mar-2016", 0, 0);
        dbRecentAdapter.insertRow("http://www.dailymotion.com", "Sunday", "27-Mar-2016", 0, 0);
        dbRecentAdapter.insertRow("http://www.treehouse.com", "Monday", "28-Mar-2016", 0, 0);
        dbRecentAdapter.insertRow("http://www.apple.com", "Tuesday", "29-Mar-2016", 0, 0);
        dbRecentAdapter.insertRow("http://www.youtube.com", "Wednesday", "25-Mar-2016", 0, 0);
        dbRecentAdapter.insertRow("http://www.microsoft.com", "Thursday", "26-Mar-2016", 0, 0);
        dbRecentAdapter.insertRow("http://www.google.co.in", "Sunday", "27-Mar-2016", 0, 0);
        dbRecentAdapter.insertRow("http://www.mait.ac.in", "Monday", "28-Mar-2016", 0, 0);
        dbRecentAdapter.insertRow("AI test tom at 9", "Tuesday", "29-Mar-2016", 0, 1);
        dbRecentAdapter.insertRow("Submit farewell fees", "Wednesday", "30-Mar-2016", 0, 1);
        dbRecentAdapter.insertRow("Check internal marks", "Thursday", "31-Mar-2016", 0, 1);
        dbAdapter.deleteAll();
        dbAdapter.insertRow("13", "Wednesday", "Apr", 6, 3, 1, 1, 1);
        dbAdapter.insertRow("14", "Thursday", "Apr", 4, 3, 1, 1, 1);
        dbAdapter.insertRow("15", "Friday", "Apr", 7, 5, 1, 1, 1);
        dbAdapter.insertRow("18", "Monday", "Apr", 3, 2, 1, 1, 1);
        dbAdapter.insertRow("19", "Tuesday", "Apr", 6, 4, 1, 1, 1);
        dbAdapter.insertRow("20", "Wednesday", "Apr", 4, 4, 1, 1, 1);
        dbAdapter.insertRow("21", "Thursday", "Apr", 2, 2, 1, 1, 1);
        dbAdapter.insertRow("22", "Friday", "Apr", 5, 1, 1, 1, 1);
        dbAdapter.insertRow("25", "Monday", "Apr", 7, 6, 1, 1, 1);
        dbAdapter.insertRow("26", "Tuesday", "Apr", 5, 2, 1, 1, 1);
        dbAdapter.insertRow("27", "Wednesday", "Apr", 4, 3, 1, 1, 1);
        dbAdapter.insertRow("28", "Thursday", "Apr", 3, 2, 1, 0, 1);*/
        polpulateRecyclerViewfromDB();
    }

    private void polpulateRecyclerViewfromDB() {

        Cursor c = dbAdapter.getAllRows();

        if (c != null) {
            adapter = new HomeAdapter(this, c);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(linearLayoutManager);
        }

    }

    private void initialize() {
        handler = new Handler();
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_attendance_list);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        links = (RelativeLayout) findViewById(R.id.rl_link);
        messages = (RelativeLayout) findViewById(R.id.rl_mess);
        n_link = (TextView) findViewById(R.id.tv_n_link);
        n_message = (TextView) findViewById(R.id.tv_n_mess);
        iv_link = (ImageView) findViewById(R.id.iv_links);
        iv_message = (ImageView) findViewById(R.id.iv_messges);
        demo = (ImageView) findViewById(R.id.iv_demo_button);
        close = (ImageView) findViewById(R.id.iv_close_button);

        appPrefs = new AppPrefs(this);



        links.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog cdd = new CustomDialog(Home.this, "Links");
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
                appPrefs.setIsNewLink(false);
                n_link.setVisibility(View.INVISIBLE);
            }
        });

        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog cdd = new CustomDialog(Home.this, "Messages");
                cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                cdd.show();
                appPrefs.setIsNewMessage(false);
                n_message.setVisibility(View.INVISIBLE);
            }
        });

        if (appPrefs.getIsServiceStarted()) {
            demo.setVisibility(View.GONE);
            close.setVisibility(View.VISIBLE);
        } else {
            demo.setVisibility(View.VISIBLE);
            close.setVisibility(View.GONE);
        }

        demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(Home.this, MyService.class));
                Toast.makeText(Home.this, "Demonstration Started", Toast.LENGTH_SHORT).show();
                demo.setVisibility(View.GONE);
                appPrefs.setIsServiceStarted(true);
                close.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        appPrefs.setIsServiceStarted(false);
                        demo.setVisibility(View.VISIBLE);
                        stopService(new Intent(Home.this, MyService.class));
                    }
                }, 540000);

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(Home.this, MyService.class));
                Toast.makeText(Home.this, "Demonstration Aborted", Toast.LENGTH_SHORT).show();
                demo.setVisibility(View.VISIBLE);
                close.setVisibility(View.GONE);
                appPrefs.setIsServiceStarted(false);
            }
        });


        if (appPrefs.getIsNewLink()) {
            n_link.setVisibility(View.VISIBLE);
        } else n_link.setVisibility(View.INVISIBLE);

        if (appPrefs.getIsNewMessage()) {
            n_message.setVisibility(View.VISIBLE);
        } else n_message.setVisibility(View.INVISIBLE);

    }

    private void openDB() {
        dbAdapter = new DBAttendanceAdapter(this);
        dbAdapter.open();
    }

    private void openRecentDB() {
        dbRecentAdapter = new DBRecentAdapter(this);
        dbRecentAdapter.open();
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
