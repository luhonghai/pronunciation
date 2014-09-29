package com.cmg.android.voicerecorder.activity;

import android.app.Activity;
import android.os.Bundle;

import com.cmg.android.voicerecorder.activity.fragment.Preperences;

/**
 * Created by luhonghai on 9/29/14.
 */
public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new Preperences()).commit();
    }
}
