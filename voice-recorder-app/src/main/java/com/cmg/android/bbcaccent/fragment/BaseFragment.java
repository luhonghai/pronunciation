package com.cmg.android.bbcaccent.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;

import com.cmg.android.bbcaccent.BaseActivity;
import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.utils.AndroidHelper;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;

/**
 * Created by luhonghai on 12/10/2015.
 */
public class BaseFragment extends Fragment {

    final GestureDetector gesture = new GestureDetector(getActivity(),
            new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    SimpleAppLog.debug("onFling has been called");
                    final int SWIPE_MIN_DISTANCE = 120;
                    final int SWIPE_MAX_OFF_PATH = 250;
                    final int SWIPE_THRESHOLD_VELOCITY = 200;
                    try {
                        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                            return false;
                        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            SimpleAppLog.debug( "Right to Left");
                            onSwipeRightToLeft();
                        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                            SimpleAppLog.debug("Left to Right");
                            onSwipeLeftToRight();
                        }
                    } catch (Exception e) {
                        // nothing
                    }
                    return super.onFling(e1, e2, velocityX, velocityY);
                }
            });


    int listenerId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listenerId = MainBroadcaster.getInstance().register(new MainBroadcaster.ReceiverListener() {
            @Override
            public void onReceiveMessage(MainBroadcaster.Filler filler, Bundle bundle) {
                if (filler == MainBroadcaster.Filler.UPDATE_FULL_VERSION) {
                    onUpdateFullVersion();
                } else if (filler == MainBroadcaster.Filler.LANGUAGE_CHANGED) {
                    SimpleAppLog.debug("Receive on language changed");
                    onLanguageChanged();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainBroadcaster.getInstance().unregister(listenerId);
    }

    public boolean checkNetwork(boolean closeApp) {
        final BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null)
            return activity.checkNetwork(closeApp);
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SimpleAppLog.debug("On destroy fragment " + this.getClass().getName());
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (!MainApplication.enablePopbackFragmentAnimation) {
            Animation a = new Animation() {};
            a.setDuration(0);
            return a;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    protected void onUpdateFullVersion() {

    }

    protected void onLanguageChanged() {

    }

    protected void onSwipeLeftToRight() {

    }

    protected void onSwipeRightToLeft() {

    }

    protected void registerGestureSwipe(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });
    }

    protected void showHelp() {

    }

    @Override
    public void onPause() {
        AndroidHelper.takeScreenShot(getActivity());
        super.onPause();
    }
}
