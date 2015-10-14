package com.cmg.android.bbcaccent.fragment.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmg.android.bbcaccent.utils.AppLog;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.fragment.DetailActivity;
import com.cmg.android.bbcaccent.data.dto.UserVoiceModel;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by luhonghai on 4/2/15.
 */
public class GraphFragmentParent extends Fragment {

    private ViewPager mViewPager;

    private GraphPageAdapter pageAdapter;

    private UserVoiceModel model = null;

    private int listenerId;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_graph_parent, container, false);
        Bundle bundle = getArguments();
        String word = "";

        if (bundle != null) {
            word = bundle.getString(FragmentTab.ARG_WORD);
            String rawModel = bundle.getString(DetailActivity.USER_VOICE_MODEL);
            Gson gson = new Gson();
            model = gson.fromJson(rawModel, UserVoiceModel.class);
        }

        pageAdapter = new GraphPageAdapter(getChildFragmentManager(), model, word);
        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        mViewPager.setAdapter(pageAdapter);
        listenerId = MainBroadcaster.getInstance().register(new MainBroadcaster.ReceiverListener() {
            @Override
            public void onDataUpdate(final String data,final int type) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (type) {
                            case FragmentTab.TYPE_SELECT_PHONEME_GRAPH:
                                if (model != null && mViewPager != null) {
                                    AppLog.logString("Select phoneme: " + data);
                                    List<String> phonemeScoreList = model.getResult().getCorrectPhonemes();
                                    for (int i = 0; i < phonemeScoreList.size(); i ++) {
                                        if (data.equalsIgnoreCase(phonemeScoreList.get(i))) {
                                            mViewPager.setCurrentItem(i + 1);
                                            break;
                                        }
                                    }
                                }
                                break;
                        }
                    }
                });
            }
        });
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewPager = null;
        MainBroadcaster.getInstance().unregister(listenerId);
    }

    private class GraphPageAdapter extends FragmentStatePagerAdapter {
        private final String word;

        private final UserVoiceModel userVoiceModel;

        public GraphPageAdapter(FragmentManager fm,UserVoiceModel userVoiceModel, String word) {
            super(fm);
            this.word = word;
            this.userVoiceModel = userVoiceModel;
        }

        @Override
        public Fragment getItem(int position) {
            GraphFragment graphFragment = new GraphFragment();
            if (position == 0) {
                graphFragment.setWord(word);
            } else {
                try {
                    graphFragment.setPhoneme(userVoiceModel.getResult().getCorrectPhonemes().get(position - 1));
                } catch (NullPointerException npe) {
                    graphFragment.setPhoneme("N/A");
                }
            }
            return graphFragment;
        }

        @Override
        public int getCount() {
            try {
                return userVoiceModel.getResult().getCorrectPhonemes().size() + 1;
            } catch (NullPointerException npe) {
                return 1;
            }
        }
    }
}
