package com.attendancesolution.bams.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.attendancesolution.bams.R;
import com.attendancesolution.bams.fragments.AttendanceFragment;
import com.attendancesolution.bams.fragments.RecentFragment;
import com.attendancesolution.bams.services.MyService;

public class MainActivity extends AppCompatActivity {


    ViewPager viewpager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        FragmentManager fragmentmanager = getSupportFragmentManager();

        viewpager.setAdapter(new MyAdapter(fragmentmanager));

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(MainActivity.this, MyService.class));
    }

    private void initialize() {

        viewpager = (ViewPager) findViewById(R.id.pager);
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    class MyAdapter extends FragmentStatePagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int arg0) {

            Fragment fragment = null;
            if (arg0 == 0) {
                fragment = new RecentFragment();
            }
            if (arg0 == 1) {
                fragment = new AttendanceFragment();
            }

            return fragment;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return 2;
        }

        public CharSequence getPageTitle(int position) {
            String title = new String();
            if (position == 0) {
                return "Recent Links";
            }
            if (position == 1) {
                return "My Attendance";
            }
            return title;

        }

    }
}
