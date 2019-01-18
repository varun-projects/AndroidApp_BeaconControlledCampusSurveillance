package com.attendancesolution.bams.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.attendancesolution.bams.R;

/**
 * Created by Akshay on 12-May-16.
 */
public class SplashActivity extends AppCompatActivity {

    ImageView iv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        getSupportActionBar().hide();


        Thread timer = new Thread() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {

                } finally {
                    Intent intent = new Intent(SplashActivity.this, Home.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();
    }
}
