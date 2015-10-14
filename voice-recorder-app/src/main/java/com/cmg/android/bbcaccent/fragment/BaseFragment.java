package com.cmg.android.bbcaccent.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.cmg.android.bbcaccent.BaseActivity;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;

/**
 * Created by luhonghai on 12/10/2015.
 */
public class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
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
        Animation animation = super.onCreateAnimation(transit, enter, nextAnim);
        if (animation != null) {
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        return animation;
    }
}
