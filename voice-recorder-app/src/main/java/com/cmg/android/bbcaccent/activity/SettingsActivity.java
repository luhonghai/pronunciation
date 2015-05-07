package com.cmg.android.bbcaccent.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.cmg.android.bbcaccent.activity.fragment.Preferences;

/**
 * Created by luhonghai on 9/29/14.
 */
public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new Preferences()).commit();
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
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
