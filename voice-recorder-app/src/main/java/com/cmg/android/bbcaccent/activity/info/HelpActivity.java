package com.cmg.android.bbcaccent.activity.info;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.activity.BaseActivity;
import com.cmg.android.bbcaccent.activity.fragment.Preferences;
import com.cmg.android.bbcaccent.data.dto.UserProfile;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * Created by luhonghai on 4/10/15.
 */
public class HelpActivity extends BaseActivity {

    private static final long HELP_DISPLAY_TIME = 5000;

    private static final int HELP_CONTENT_LENGTH = 11;

    private ViewPager pagerHelpContent;

    private int currentItem = 0;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (currentItem < HELP_CONTENT_LENGTH) {
                if (pagerHelpContent != null) {
                    if (isInit) {
                        pagerHelpContent.setCurrentItem(++currentItem, true);
                    }
                    handler.postDelayed(runnable, HELP_DISPLAY_TIME);
                }
            } else {
                HelpActivity.this.finish();
            }
        }
    };

    private Handler handler = new Handler();

    private int previousState;
    private boolean userScrollChange = false;
    private boolean isScrolling = false;

    private boolean isInit = false;

    private SimpleImageLoadingListener imageLoadingListener;

    private boolean isLastPageSwipe = false;

    private long lastPageSwipeCount = 0;

    private static final int MAX_PAGE_SWIPE_COUNT = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        findViewById(R.id.btnSkip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpActivity.this.finish();
            }
        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pagerHelpContent = (ViewPager) findViewById(R.id.pagerHelpContent);
        pagerHelpContent.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                isScrolling = true;
                SimpleAppLog.debug("onPageScrolled. position: " + position + ". positionOffset: " + positionOffset + ". positionOffsetPixels: " + positionOffsetPixels);
                if (position == HELP_CONTENT_LENGTH - 1 && positionOffset == 0.0f && positionOffsetPixels == 0) {
                    lastPageSwipeCount++;
                    isLastPageSwipe = true;
                } else {
                    lastPageSwipeCount = 0;
                    isLastPageSwipe = false;
                }
            }

            @Override
            public void onPageSelected(int position) {
                SimpleAppLog.debug("onPageSelected: " + position);
                currentItem = position;
                isScrolling = false;
                if (position == HELP_CONTENT_LENGTH - 1) {
                    ((TextView)findViewById(R.id.btnSkip)).setText(getString(R.string.dialog_close));
                } else {
                    ((TextView)findViewById(R.id.btnSkip)).setText(getString(R.string.skip));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                SimpleAppLog.debug("onPageScrollStateChanged: " + state);
                if (previousState == ViewPager.SCROLL_STATE_DRAGGING
                        && state == ViewPager.SCROLL_STATE_SETTLING)
                    userScrollChange = true;

                else if (previousState == ViewPager.SCROLL_STATE_SETTLING
                        && state == ViewPager.SCROLL_STATE_IDLE)
                    userScrollChange = false;

                if (previousState == ViewPager.SCROLL_STATE_DRAGGING
                        && state == ViewPager.SCROLL_STATE_IDLE
                        && isLastPageSwipe
                        ) {
                    SimpleAppLog.debug("lastPageSwipeCount: " + lastPageSwipeCount);
                    if (lastPageSwipeCount >= MAX_PAGE_SWIPE_COUNT) {
                        HelpActivity.this.finish();
                    } else {
                        lastPageSwipeCount = 0;
                    }

                }

                previousState = state;

            }
        });
        imageLoadingListener = new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                isInit = true;
            }
        };
        HelpPageAdapter pageAdapter = new HelpPageAdapter(getSupportFragmentManager(), imageLoadingListener);
        pagerHelpContent.setAdapter(pageAdapter);
        handler.postDelayed(runnable, HELP_DISPLAY_TIME);

        UserProfile userProfile = Preferences.getCurrentProfile(this);
        if (userProfile != null && userProfile.getHelpStatus() == UserProfile.HELP_INIT) {
            Preferences.setHelpStatusProfile(this, userProfile.getUsername(), UserProfile.HELP_SKIP);
        }
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
        private final SimpleImageLoadingListener imageLoadingListener;
        public HelpPageAdapter(FragmentManager fm, SimpleImageLoadingListener imageLoadingListener) {
            super(fm);
            this.imageLoadingListener = imageLoadingListener;
        }

        @Override
        public Fragment getItem(int position) {
            HelpFragment helpFragment = new HelpFragment();
            helpFragment.setDrawableId(position + 1);
            helpFragment.setImageLoadingListener(imageLoadingListener);
            return helpFragment;
        }

        @Override
        public int getCount() {
            return HELP_CONTENT_LENGTH;
        }
    }

    public static class HelpFragment extends Fragment {
        private int drawableId;

        private DisplayImageOptions options;

        private SimpleImageLoadingListener imageLoadingListener;

        public HelpFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    //.displayer(new FadeInBitmapDisplayer(300))
                    .build();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View imageLayout = inflater.inflate(R.layout.help_content_layout, container, false);
            if (!ImageLoader.getInstance().isInited()) {
                ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
            }

            final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.imgHelpContent);
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
//            imageView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//                @Override
//                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//
//                }
//            });

            ImageLoader.getInstance().displayImage("assets://help-content/help_content_" + drawableId + ".png", imageView, options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    spinner.setVisibility(View.VISIBLE);
                    if (imageLoadingListener != null) {
                        imageLoadingListener.onLoadingStarted(imageUri, view);
                    }
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    String message = null;
                    switch (failReason.getType()) {
                        case IO_ERROR:
                            message = "Input/Output error";
                            break;
                        case DECODING_ERROR:
                            message = "Image can't be decoded";
                            break;
                        case NETWORK_DENIED:
                            message = "Downloads are denied";
                            break;
                        case OUT_OF_MEMORY:
                            message = "Out Of Memory error";
                            break;
                        case UNKNOWN:
                            message = "Unknown error";
                            break;
                    }
                    SimpleAppLog.error("Could not load help image. Message: " + message);
                    //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                    spinner.setVisibility(View.GONE);
                    if (imageLoadingListener != null) {
                        imageLoadingListener.onLoadingFailed(imageUri, view, failReason);
                    }
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    spinner.setVisibility(View.GONE);
                    if (imageLoadingListener != null) {
                        imageLoadingListener.onLoadingComplete(imageUri, view, loadedImage);
                    }

                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    spinner.setVisibility(View.GONE);
                    SimpleAppLog.error("Could not load help image. Message: " + "Cancelled");
                    //Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
                }
            });

            return imageLayout;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
        }

        public void setDrawableId(int drawableId) {
            this.drawableId = drawableId;
        }

        public void setImageLoadingListener(SimpleImageLoadingListener imageLoadingListener) {
            this.imageLoadingListener = imageLoadingListener;
        }
    }

    @Override
    protected void onDestroy() {
        Preferences.setIsSetupProfile(this, Preferences.getSelectedUsername(this));
        super.onDestroy();
    }
}
