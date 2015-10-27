package com.cmg.android.bbcaccent.fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTabHost;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.adapter.PhoneScoreAdapter;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.data.dto.SphinxResult;
import com.cmg.android.bbcaccent.data.dto.UserVoiceModel;
import com.cmg.android.bbcaccent.data.sqlite.freestyle.WordDBAdapter;
import com.cmg.android.bbcaccent.dictionary.DictionaryItem;
import com.cmg.android.bbcaccent.dictionary.DictionaryListener;
import com.cmg.android.bbcaccent.dictionary.DictionaryWalker;
import com.cmg.android.bbcaccent.dictionary.OxfordDictionaryWalker;
import com.cmg.android.bbcaccent.fragment.tab.FragmentTab;
import com.cmg.android.bbcaccent.fragment.tab.GraphFragmentParent;
import com.cmg.android.bbcaccent.fragment.tab.HistoryFragment;
import com.cmg.android.bbcaccent.fragment.tab.TipFragment;
import com.cmg.android.bbcaccent.helper.PlayerHelper;
import com.cmg.android.bbcaccent.utils.AndroidHelper;
import com.cmg.android.bbcaccent.utils.AppLog;
import com.cmg.android.bbcaccent.utils.ColorHelper;
import com.cmg.android.bbcaccent.utils.FileHelper;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.cmg.android.bbcaccent.view.AlwaysMarqueeTextView;
import com.cmg.android.bbcaccent.view.RecordingView;
import com.cmg.android.bbcaccent.view.ShowcaseHelper;
import com.cmg.android.bbcaccent.view.SlidingUpPanelLayout;
import com.google.gson.Gson;
import com.luhonghai.litedb.exception.LiteDatabaseException;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.sephiroth.android.library.widget.HListView;

/**
 * Created by luhonghai on 12/22/14.
 */
public class DetailFragment extends BaseFragment implements RecordingView.OnAnimationListener {

    private DisplayingState displayingState;

    @Bind(android.R.id.tabhost)
    FragmentTabHost mTabHost;

    @Bind(R.id.main_recording_view)
    RecordingView recordingView;

    @Bind(R.id.btnAudio)
    ImageButton btnAudio;

    @Bind(R.id.txtWord)
    AlwaysMarqueeTextView txtWord;

    @Bind(R.id.txtPhoneme)
    AlwaysMarqueeTextView txtPhonemes;

    @Bind(R.id.listViewScore)
    HListView hListView;

    @Bind(R.id.panelSlider)
    SlidingUpPanelLayout panelSlider;

    private UserVoiceModel model;

    private PlayerHelper player;

    private ButtonState lastState;

    private boolean isPlaying = false;

    private Map<String, String> mapCMUvsIPA;

    private int receiverListenerId;

    private ShowcaseHelper showcaseHelper;

    private void initDetailView(View root) {
        recordingView.setAnimationListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.detail, null);
        ButterKnife.bind(this, root);
        Bundle bundle = getArguments();
        WordDBAdapter wordDBAdapter = new WordDBAdapter();
        mapCMUvsIPA = wordDBAdapter.getPhonemeCMUvsIPA();
        Gson gson = new Gson();
        model = gson.fromJson(bundle.getString(MainBroadcaster.Filler.USER_VOICE_MODEL.toString()), UserVoiceModel.class);
        initTabHost(root);
        initDetailView(root);
        try {
            showData(model, false);
        } catch (Exception e) {
            SimpleAppLog.error("Could not open database",e);
        }

        receiverListenerId = MainBroadcaster.getInstance().register(new MainBroadcaster.ReceiverListener() {
            @Override
            public void onDataUpdate(String data, int type) {
                switch (type) {
                    case FragmentTab.TYPE_SELECT_PHONEME_GRAPH:
                        mTabHost.setCurrentTab(0);
                        break;
                }
            }

            @Override
            public void onHistoryAction(final UserVoiceModel model, String word, int type) {
                if (word != null && word.length() > 0) {
                    if (model != null) {
                        switch (type) {
                            case HistoryFragment.CLICK_LIST_ITEM:
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            showData(model, true);
                                        } catch (Exception e) {
                                            SimpleAppLog.error("Could not show data", e);
                                        }
                                    }
                                });
                                break;
                        }
                    }
                } else {
                    if (type == HistoryFragment.CLICK_RECORD_BUTTON) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                closeDetail();
                            }
                        });
                    }
                }
            }
        });
        initSlider(root);
        showcaseHelper = new ShowcaseHelper(getActivity());
        return root;
    }

    private void initSlider(final View root) {
        LinearLayout rlSliderContent = (LinearLayout) root.findViewById(R.id.rlSliderContent);
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        SlidingUpPanelLayout.LayoutParams layoutParams = (SlidingUpPanelLayout.LayoutParams) rlSliderContent.getLayoutParams();
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        final int halfHeight = (displayMetrics.heightPixels - actionBarHeight) / 2;
        layoutParams.height = halfHeight;
        rlSliderContent.setLayoutParams(layoutParams);
        panelSlider.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                float test = (halfHeight - root.findViewById(R.id.btnSlider).getHeight()) / (float) halfHeight;
                if (slideOffset > test) {
                    //txtPhonemes.setVisibility(View.INVISIBLE);
                } else {
                    //txtPhonemes.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPanelCollapsed(View panel) {

            }

            @Override
            public void onPanelExpanded(View panel) {

            }

            @Override
            public void onPanelAnchored(View panel) {

            }

            @Override
            public void onPanelHidden(View panel) {

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                panelSlider.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        }, 1000);
    }

    private void showData(UserVoiceModel userVoiceModel, boolean showScore) throws LiteDatabaseException {
        model = userVoiceModel;
        WordDBAdapter dbAdapter = new WordDBAdapter();
        dbAdapter.open();
        String pronunciation = dbAdapter.getPronunciation(model.getWord());
        dbAdapter.close();
        recordingView.setScore(model.getScore());
        txtPhonemes.setText(pronunciation);
        txtWord.setText(model.getWord());
        AndroidHelper.updateMarqueeTextView(txtWord, !AndroidHelper.isCorrectWidth(txtWord, model.getWord()));
        AndroidHelper.updateMarqueeTextView(txtPhonemes, !AndroidHelper.isCorrectWidth(txtPhonemes, pronunciation));
        if (model.getScore() >= 80.0) {
            lastState = ButtonState.GREEN;
        } else if (model.getScore() >= 45.0) {
            lastState = ButtonState.ORANGE;
        } else {
            lastState = ButtonState.RED;
        }
        switchButtonState();
        //showPhonemes();
        showPhonemesListView();
        if (showScore) {
            displayingState = DisplayingState.WAIT_FOR_ANIMATION_MAX;
            recordingView.setScore(0.0f);
            recordingView.recycle();
            recordingView.invalidate();
            recordingView.startPingAnimation(getActivity(), 2000, model.getScore(), true, true);
        }
    }

    private void closeDetail() {
        MainBroadcaster.getInstance().getSender().sendPopBackStackFragment();
    }

    private void showPhonemesListView() {
        if (model == null || model.getResult() == null) return;
        List<SphinxResult.PhonemeScore> phonemeScores = model.getResult().getPhonemeScores();
        SphinxResult.PhonemeScore[] scores = null;
        if (phonemeScores == null || phonemeScores.size() == 0) {
            //TODO validate if not contain any scores
        } else {
            scores = new SphinxResult.PhonemeScore[phonemeScores.size()];
            phonemeScores.toArray(scores);
            PhoneScoreAdapter scoreAdapter = new PhoneScoreAdapter(MainApplication.getContext(), scores, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog(view);
                }
            });
            hListView.setAdapter(scoreAdapter);
            scoreAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (model != null) {
            displayingState = DisplayingState.WAIT_FOR_ANIMATION_MAX;
            recordingView.setScore(0.0f);
            recordingView.recycle();
            recordingView.invalidate();
            recordingView.startPingAnimation(getActivity(), 1000, model.getScore(), true, true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (model != null) {
            recordingView.stopPingAnimation();
            displayingState = DisplayingState.DEFAULT;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainBroadcaster.getInstance().unregister(receiverListenerId);
        try {
            recordingView.stopPingAnimation();
        } catch (Exception ex) {

        }
        try {
            recordingView.recycle();
        } catch (Exception ex) {

        }
        ButterKnife.unbind(this);
    }

    private void initTabHost(View root) {
        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        Bundle bundle = new Bundle();
        bundle.putString(MainBroadcaster.Filler.Key.WORD.toString(), model.getWord());
        Gson gson = new Gson();
        bundle.putString(MainBroadcaster.Filler.USER_VOICE_MODEL.toString(), gson.toJson(model));

        addTabImage(R.drawable.tab_graph,
                GraphFragmentParent.class, getString(R.string.tab_graph), bundle);
        addTabImage(R.drawable.tab_history,
                HistoryFragment.class, getString(R.string.tab_history), bundle);
        addTabImage(R.drawable.tab_tip,
                TipFragment.class, getString(R.string.tab_tip), bundle);
    }

    private void addTabImage(int drawableId, Class<?> c, String labelId, Bundle bundle)
    {
        TabHost.TabSpec spec = mTabHost.newTabSpec(labelId).setIndicator(null, getResources().getDrawable(drawableId));
        mTabHost.addTab(spec, c, bundle);

    }

    @OnClick({R.id.rlVoiceExample, R.id.txtPhoneme, R.id.txtWord, R.id.btnAudio})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlVoiceExample:
            case R.id.txtPhoneme:
            case R.id.txtWord:
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        DictionaryWalker walker = new OxfordDictionaryWalker(FileHelper.getAudioDir(MainApplication.getContext()));
                        walker.setListener(new DictionaryListener() {
                            @Override
                            public void onDetectWord(final DictionaryItem dItem) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        play(dItem.getAudioFile());
                                    }
                                });
                            }

                            @Override
                            public void onWordNotFound(DictionaryItem dItem, final FileNotFoundException ex) {

                            }

                            @Override
                            public void onError(DictionaryItem dItem, final Exception ex) {

                            }
                        });
                        walker.execute(model.getWord());
                        return null;
                    }
                }.execute();
                break;
            case R.id.btnAudio:
                if (isPlaying) {
                    try {
                        player.stop();
                        isPlaying = false;
                        switchButtonState();
                    } catch (Exception ex) {

                    }
                } else {
                    playAudio();
                }
                break;
        }
    }

    private void showDialog(View v) {
        final SphinxResult.PhonemeScore score = (SphinxResult.PhonemeScore) v.getTag();
        final float totalScore = score.getTotalScore();
        final Dialog dialog = new Dialog(getActivity(), R.style.Theme_WhiteDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.phone_info_dialog);
        int sdk = android.os.Build.VERSION.SDK_INT;
        Drawable drawable;
        if (totalScore >= 80.0f) {
            drawable = getResources().getDrawable(R.drawable.rounded_corner_color_green_dialog);
            ((ImageButton) dialog.findViewById(R.id.btnGraph)).setImageResource(R.drawable.p_graph_green_rev);
            ((ImageButton) dialog.findViewById(R.id.btnAudioPhoneme)).setImageResource(R.drawable.p_audio_green_rev);
        } else if (totalScore >= 45.0f) {
            drawable = getResources().getDrawable(R.drawable.rounded_corner_color_orange_dialog);
            ((ImageButton) dialog.findViewById(R.id.btnGraph)).setImageResource(R.drawable.p_graph_orange_rev);
            ((ImageButton) dialog.findViewById(R.id.btnAudioPhoneme)).setImageResource(R.drawable.p_audio_orange_rev);
        } else {
            drawable = getResources().getDrawable(R.drawable.rounded_corner_color_red_dialog);
            ((ImageButton) dialog.findViewById(R.id.btnGraph)).setImageResource(R.drawable.p_graph_red_rev);
            ((ImageButton) dialog.findViewById(R.id.btnAudioPhoneme)).setImageResource(R.drawable.p_audio_red_rev);
        }
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            dialog.findViewById(R.id.content).setBackgroundDrawable(drawable);
        } else {
            dialog.findViewById(R.id.content).setBackground(drawable);
        }
        dialog.findViewById(R.id.btnGraph).setTag(score.getName());
        dialog.findViewById(R.id.btnGraph).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainBroadcaster.getInstance().getSender().sendUpdateData(v.getTag().toString(), FragmentTab.TYPE_SELECT_PHONEME_GRAPH);
                dialog.dismiss();
            }
        });
        ((TextView) dialog.findViewById(R.id.txtPhonemeScore)).setText(Math.round(totalScore) + "%");
        String ipa = "";
        if (mapCMUvsIPA.containsKey(score.getName().toUpperCase())) {
            ipa = mapCMUvsIPA.get(score.getName().toUpperCase());
        }
        ((TextView) dialog.findViewById(R.id.txtPhonemeReal)).setText(ipa);
        ((TextView) dialog.findViewById(R.id.txtPhoneme)).setText(score.getName());
        dialog.findViewById(R.id.btnAudioPhoneme).setTag(score.getName());
        dialog.findViewById(R.id.btnAudioPhoneme).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File ipaAudio = new File(FileHelper.getIPACacheDir(MainApplication.getContext()), v.getTag().toString() + ".mp3");
                try {
                    if (ipaAudio.exists())
                        FileUtils.forceDelete(ipaAudio);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!ipaAudio.exists()) {
                    try {
                        FileUtils.copyInputStreamToFile(MainApplication.getContext().getAssets().open("ipa-help/" + v.getTag().toString().toUpperCase() + ".mp3"), ipaAudio);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                playFile(ipaAudio);
            }
        });
        dialog.setTitle(null);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 10000);
    }

    private void play(String file) {
        if (file == null || file.length() == 0) return;
        play(new File(file));
    }

    private void play(File file) {
        if (!file.exists()) return;
        try {
            if (player != null) {
                try {
                    player.stop();
                } catch (Exception ex) {

                }
            }
            player = new PlayerHelper(file, new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    try {
                        player.stop();
                    } catch (Exception ex) {

                    }
                    isPlaying = false;
                    switchButtonState();
                }

            });
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchButtonState() {
        switchButtonState(lastState);
    }

    private void switchButtonState(ButtonState state) {
        switch (state) {
            case PLAYING:
                btnAudio.setEnabled(true);
                btnAudio.setImageResource(R.drawable.p_close_red);
                txtPhonemes.setTextColor(ColorHelper.COLOR_GRAY);
                txtWord.setTextColor(ColorHelper.COLOR_GRAY);
                break;
            case RED:
                btnAudio.setEnabled(true);
                btnAudio.setImageResource(R.drawable.p_audio_red);
                txtPhonemes.setTextColor(ColorHelper.COLOR_RED);
                txtWord.setTextColor(ColorHelper.COLOR_RED);
                break;
            case ORANGE:
                btnAudio.setEnabled(true);
                btnAudio.setImageResource(R.drawable.p_audio_orange);
                txtPhonemes.setTextColor(ColorHelper.COLOR_ORANGE);
                txtWord.setTextColor(ColorHelper.COLOR_ORANGE);
                break;
            case GREEN:
                btnAudio.setEnabled(true);
                btnAudio.setImageResource(R.drawable.p_audio_green);
                txtPhonemes.setTextColor(ColorHelper.COLOR_GREEN);
                txtWord.setTextColor(ColorHelper.COLOR_GREEN);
                break;
            default:
                break;
        }
        if (model != null) {
            File audio = new File(model.getAudioFile());
            if (!audio.exists()) {
                btnAudio.setEnabled(false);
                btnAudio.setImageResource(R.drawable.p_audio_gray);
            }
        }
    }

    private void playAudio() {
        switchButtonState(ButtonState.PLAYING);
        playFile(new File(model.getAudioFile()));
    }

    private void playFile(File file) {
        if (!file.exists()) return;
        isPlaying = true;
        try {
            if (player != null) {
                try {
                    player.stop();
                } catch (Exception ex) {

                }
            }
            player = new PlayerHelper(file, new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    isPlaying = false;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switchButtonState();
                        }
                    });

                }

            });
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
            isPlaying = false;
        }
    }

    @Override
    public void onAnimationMax() {
        if (displayingState == DisplayingState.WAIT_FOR_ANIMATION_MAX) {
            AppLog.logString("On animation max");
            recordingView.stopPingAnimation();
            displayingState = DisplayingState.DEFAULT;
            showcaseHelper.showHelp(ShowcaseHelper.HelpKey.DETAIL_SELECT_PHONEME,
                    new ShowcaseHelper.HelpState(getViewByPosition(0, hListView), "<b>Press</b> a phoneme for even more detail"),
                    new ShowcaseHelper.HelpState(recordingView, "<b>Press</> the score to return to previous screen"));
        }
    }

    private View getViewByPosition(int pos, HListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    @Override
    public void onAnimationMin() {

    }

    enum DisplayingState {
        DEFAULT,
        WAIT_FOR_ANIMATION_MAX
    }

    enum ButtonState {
        RED,
        ORANGE,
        GREEN,
        PLAYING
    }

}
