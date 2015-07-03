package com.cmg.android.bbcaccentamt.activity.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmg.android.bbcaccentamt.R;

public abstract class FragmentTab extends Fragment {
    public static final int TYPE_RELOAD_DATA = 0;
    public static final int TYPE_ENABLE_VIEW = 1;
    public static final int TYPE_DISABLE_VIEW = 2;
    public static final int TYPE_SELECT_PHONEME_GRAPH = 3;

    public static final String ON_UPDATE_DATA = "com.cmg.android.bbcaccent.activity.fragment.FragmentTab.ON_UPDATE_DATA";
    public static final String ARG_WORD = "word";
    public static final String ACTION_TYPE = "action_type";
    public static final String ACTION_DATA = "action_data";

    protected boolean isLoadedView = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isLoadedView = false;
        getActivity().registerReceiver(mHandleUpdate, new IntentFilter(ON_UPDATE_DATA));
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_layout, container, false);
        TextView tv = (TextView) v.findViewById(R.id.text);
        tv.setText(this.getTag() + " content.");
        return v;
    }

    @Override
    public void onDestroy() {
        isLoadedView = false;
        try {
            getActivity().unregisterReceiver(mHandleUpdate);
        } catch (Exception ex) {

        }
        super.onDestroy();
    }

    private final BroadcastReceiver mHandleUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final int type = intent.getExtras().getInt(ACTION_TYPE);
            final String data = intent.getExtras().getString(ACTION_DATA);
            Log.i(this.getClass().getName(), "On update data. Type: " + type);
            if (!isLoadedView) return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (type) {
                        case TYPE_DISABLE_VIEW:
                            enableView(false);
                            break;
                        case TYPE_ENABLE_VIEW:
                            enableView(true);
                            break;
                        case TYPE_RELOAD_DATA:
                            onUpdateData(data);
                            break;
                    }
                }
            });
        }
    };

    protected abstract void onUpdateData(String data);

    protected abstract void enableView(boolean enable);
}
