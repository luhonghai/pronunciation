package com.cmg.android.voicerecorder.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmg.android.voicerecorder.R;

/**
 * Created by cmg on 2/12/15.
 */
public class TipFragment extends FragmentTab {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tip, container, false);

        return v;
    }
}
