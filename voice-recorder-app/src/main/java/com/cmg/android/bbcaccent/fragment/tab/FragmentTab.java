package com.cmg.android.bbcaccent.fragment.tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;

public abstract class FragmentTab extends Fragment {
    public static final int TYPE_RELOAD_DATA = 0;
    public static final int TYPE_ENABLE_VIEW = 1;
    public static final int TYPE_DISABLE_VIEW = 2;
    public static final int TYPE_SELECT_PHONEME_GRAPH = 3;
    public static final int TYPE_CHANGE_SELECTED_WORD = 4;

    protected boolean isLoadedView = false;

    private int receiverListenerId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        isLoadedView = false;
        receiverListenerId = MainBroadcaster.getInstance().register(new MainBroadcaster.ReceiverListener() {

            @Override
            public void onDataUpdate(final String data,final int type) {
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
        });
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isLoadedView = false;
        MainBroadcaster.getInstance().unregister(receiverListenerId);
    }

    protected abstract void onUpdateData(String data);

    protected abstract void enableView(boolean enable);
}
