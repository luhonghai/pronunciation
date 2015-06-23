package com.cmg.android.bbcaccent.activity;

import android.os.Bundle;

import com.cmg.android.bbcaccent.activity.fragment.Preferences;

/**
 * Created by luhonghai on 9/29/14.
 */
public class SettingsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new Preferences()).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        Preferences.setIsSetupProfile(this, Preferences.getSelectedUsername(this));
        super.onDestroy();

    }
}
