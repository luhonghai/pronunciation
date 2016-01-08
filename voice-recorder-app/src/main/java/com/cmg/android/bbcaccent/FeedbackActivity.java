package com.cmg.android.bbcaccent;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.cmg.android.bbcaccent.fragment.FeedbackFragment;

/**
 * Created by luhonghai on 13/10/2015.
 */
public class FeedbackActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_layout);
        FeedbackFragment feedbackFragment = new FeedbackFragment();
        if (getIntent() != null)
            feedbackFragment.setArguments(getIntent().getExtras());
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment, feedbackFragment)
                .commit();

    }
}
