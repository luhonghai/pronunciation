package com.cmg.android.bbcaccent.fragment.tab;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.data.dto.UserVoiceModel;
import com.cmg.android.bbcaccent.data.dto.lesson.word.IPAMapArpabet;
import com.cmg.android.bbcaccent.data.dto.lesson.word.WordCollection;
import com.cmg.android.bbcaccent.data.sqlite.lesson.LessonDBAdapterService;
import com.cmg.android.bbcaccent.utils.AppLog;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.luhonghai.litedb.exception.LiteDatabaseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luhonghai on 4/2/15.
 */
public class GraphFragmentParent extends Fragment {

    private ViewPager mViewPager;

    private int listenerId;

    private Map<String, IPAMapArpabet> phonemes = new HashMap<>();

    private List<String> phonemeList = new ArrayList<>();

    private String word = "";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_graph_parent, container, false);
        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MainBroadcaster.getInstance().getSender().sendMessage(MainBroadcaster.Filler.RESET_TIMING_HELP, null);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(MainBroadcaster.Filler.USER_VOICE_MODEL.toString())) {
            UserVoiceModel model = MainApplication.fromJson(bundle.getString(MainBroadcaster.Filler.USER_VOICE_MODEL.toString()), UserVoiceModel.class);
            updateWord(model.getWord());
        } else {
            updateWord(MainApplication.getContext().getSelectedWord());
        }
        listenerId = MainBroadcaster.getInstance().register(new MainBroadcaster.ReceiverListener() {
            @Override
            public void onDataUpdate(final String data,final int type) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (type) {
                            case FragmentTab.TYPE_SELECT_PHONEME_GRAPH:
                                if (mViewPager != null) {
                                    AppLog.logString("Select phoneme: " + data);
                                    if (phonemeList != null && phonemeList.size() > 0) {
                                        for (int i = 0; i < phonemeList.size(); i++) {
                                            if (data.equalsIgnoreCase(phonemeList.get(i))) {
                                                mViewPager.setCurrentItem(i + 1);
                                                break;
                                            }
                                        }
                                    }
                                }
                                break;
                            case FragmentTab.TYPE_CHANGE_SELECTED_WORD:
                                updateWord(data);
                                break;
                        }
                    }
                });
            }
        });
        return v;
    }

    protected void updateWord(final String word) {
        if (word == null || word.length() == 0) {
//            phonemeList.clear();
//            phonemes.clear();
//            this.word = "";
//            doNotifyDataSetChangedOnce = true;
//            if (mViewPager != null && mViewPager.getAdapter() != null)
//                mViewPager.getAdapter().notifyDataSetChanged();
        } else {
            this.word = word;
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        phonemeList.clear();
                        phonemes.clear();
                        WordCollection wordCollection = LessonDBAdapterService.getInstance().findObject("word=?", new String[]{word}, WordCollection.class);
                        String arpabet = wordCollection.getArpabet();
                        if (arpabet != null && arpabet.length() > 0) {
                            arpabet = arpabet.trim();
                            while (arpabet.contains("  ")) arpabet = arpabet.replace("  ", " ");
                            String[] listPhoneme = arpabet.split(" ");
                            if (listPhoneme.length > 0)
                                for (String phone : listPhoneme) {
                                    IPAMapArpabet ipaMapArpabet = LessonDBAdapterService.getInstance().findObject("arpabet = ?", new String[]{phone.toUpperCase()}, IPAMapArpabet.class);
                                    phonemes.put(phone.toUpperCase(), ipaMapArpabet);
                                    phonemeList.add(phone.toUpperCase());
                                }
                        }
                    } catch (LiteDatabaseException e) {
                        SimpleAppLog.error("could not get word from database " + word, e);
                    }
                    if (getActivity() != null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mViewPager != null) {
                                    if (mViewPager.getAdapter() != null)
                                        mViewPager.getAdapter().notifyDataSetChanged();
                                    GraphPageAdapter pageAdapter = new GraphPageAdapter(getChildFragmentManager(),
                                            new HashMap<String, IPAMapArpabet>(phonemes),
                                            new ArrayList<String>(phonemeList));
                                    mViewPager.setAdapter(pageAdapter);
                                    mViewPager.getAdapter().notifyDataSetChanged();
                                    if (mViewPager.getChildCount() > 0)
                                        mViewPager.setCurrentItem(0);
                                }
                            }
                        });
                    return null;
                }
            }.execute();
        }
    }

    @Override
    public void onResume() {
        if (mViewPager != null && mViewPager.getAdapter() != null)
            mViewPager.getAdapter().notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewPager = null;
        MainBroadcaster.getInstance().unregister(listenerId);
    }

    private class GraphPageAdapter extends FragmentStatePagerAdapter {
        private final Map<String, IPAMapArpabet> mPhonemes;

        private final List<String> mPhonemeList;

        public GraphPageAdapter(FragmentManager fm, Map<String, IPAMapArpabet> phonemes, List<String> phonemeList) {
            super(fm);
            this.mPhonemes = phonemes;
            this.mPhonemeList = phonemeList;
        }

        @Override
        public Fragment getItem(int position) {
            GraphFragment graphFragment = new GraphFragment();
            Bundle bundle = getArguments();
            if (bundle == null) bundle = new Bundle();
            if (position == 0) {
                bundle.putString(MainBroadcaster.Filler.Key.WORD.toString(), word);
                graphFragment.setWord(word);
            } else {
                try {
                    graphFragment.setPhoneme(mPhonemes.get(mPhonemeList.get(position - 1)));
                } catch (NullPointerException npe) {
                    graphFragment.setPhoneme(null);
                }
            }
            graphFragment.setArguments(bundle);
            return graphFragment;
        }

        @Override
        public int getCount() {
            try {
                return mPhonemeList.size() + 1;
            } catch (NullPointerException npe) {
                return 1;
            }
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }
}
