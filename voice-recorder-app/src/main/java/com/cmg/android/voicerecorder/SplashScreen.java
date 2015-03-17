package com.cmg.android.voicerecorder;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.cmg.android.voicerecorder.activity.BaseActivity;
import com.cmg.android.voicerecorder.activity.view.RecordingView;
import com.cmg.android.voicerecorder.data.ScoreDBAdapter;

import java.sql.SQLException;

/**
 * Created by luhonghai on 3/17/15.
 */
public class SplashScreen extends BaseActivity implements RecordingView.OnAnimationListener {
    private static final long MIN_SPLASH_TIME = 6000;
    private RecordingView processCycle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        processCycle = (RecordingView) findViewById(R.id.processCycle);
        processCycle.setAnimationListener(this);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                long start = System.currentTimeMillis();
                ScoreDBAdapter scoreDBAdapter = new ScoreDBAdapter(SplashScreen.this);
                try {
                    scoreDBAdapter.open();
                    scoreDBAdapter.getAll();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    scoreDBAdapter.close();
                }
                long end = System.currentTimeMillis();
                if ((end - start) < MIN_SPLASH_TIME) {
                    try {
                        Thread.sleep(MIN_SPLASH_TIME - (end-start));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Intent mainIntent = new Intent(SplashScreen.this,MainActivity.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
                return null;
            }
        }.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (processCycle != null) {
            processCycle.setScore(0.0f);
            processCycle.recycle();
            processCycle.invalidate();
            processCycle.startPingAnimation(this, 2000, 100.0f, false);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (processCycle != null) {
            processCycle.recycle();
        }
    }

    @Override
    public void onAnimationMax() {

    }

    @Override
    public void onAnimationMin() {

    }
}
