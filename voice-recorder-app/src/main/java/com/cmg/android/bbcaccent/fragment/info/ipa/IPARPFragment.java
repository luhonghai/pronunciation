package com.cmg.android.bbcaccent.fragment.info.ipa;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.fragment.BaseFragment;

/**
 * Created by luhonghai on 12/11/2015.
 */
public class IPARPFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ipa_rp_fragment, null);
        registerGestureSwipe(view);
        return view;
    }

    @Override
    protected void onSwipeLeftToRight() {
        getActivity().onBackPressed();
    }
}
