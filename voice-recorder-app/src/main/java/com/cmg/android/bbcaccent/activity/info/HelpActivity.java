package com.cmg.android.bbcaccent.activity.info;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.actionbarsherlock.view.MenuItem;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.activity.BaseActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by luhonghai on 4/10/15.
 */
public class HelpActivity extends BaseActivity {

    private static final int[] HELP_CONTENT_DRAWABLE = new int[] {
            R.drawable.help_content_1,
            R.drawable.help_content_2,
            R.drawable.help_content_3,
            R.drawable.help_content_4,
            R.drawable.help_content_5,
            R.drawable.help_content_6,
            R.drawable.help_content_7,
            R.drawable.help_content_8,
            R.drawable.help_content_9,
            R.drawable.help_content_10,
            R.drawable.help_content_11
    };

    private ViewPager pagerHelpContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pagerHelpContent = (ViewPager) findViewById(R.id.pagerHelpContent);
        HelpPageAdapter pageAdapter = new HelpPageAdapter(getSupportFragmentManager());
        pagerHelpContent.setAdapter(pageAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    public class HelpPageAdapter extends FragmentStatePagerAdapter {

        public HelpPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            HelpFragment helpFragment = new HelpFragment();
            helpFragment.setDrawableId(HELP_CONTENT_DRAWABLE[position]);
            return helpFragment;
        }

        @Override
        public int getCount() {
            return HELP_CONTENT_DRAWABLE.length;
        }
    }

    public static class HelpFragment extends Fragment {
        private int drawableId;

        public void setDrawableId(int drawableId) {
            this.drawableId = drawableId;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.help_content_layout, container, false);
            ImageLoader.getInstance().displayImage("drawable://" + drawableId, (ImageView) v.findViewById(R.id.imgHelpContent));
            return v;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
        }
    }
}
