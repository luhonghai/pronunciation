package com.cmg.android.bbcaccent.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmg.android.bbcaccent.BaseActivity;

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
}
