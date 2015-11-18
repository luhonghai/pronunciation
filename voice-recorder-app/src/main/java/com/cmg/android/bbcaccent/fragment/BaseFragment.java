package com.cmg.android.bbcaccent.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.animation.Animation;

import com.cmg.android.bbcaccent.BaseActivity;
import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;

/**
 * Created by luhonghai on 12/10/2015.
 */
public class BaseFragment extends Fragment {

    int listenerId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listenerId = MainBroadcaster.getInstance().register(new MainBroadcaster.ReceiverListener() {
            @Override
            public void onReceiveMessage(MainBroadcaster.Filler filler, Bundle bundle) {
                if (filler == MainBroadcaster.Filler.UPDATE_FULL_VERSION) {
                    onUpdateFullVersion();
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
}
