package com.cmg.android.bbcaccent.fragment;

import android.app.Dialog;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuView;
import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.adapter.PhoneScoreAdapter;
import com.cmg.android.bbcaccent.adapter.PhoneScoreItemAdapter;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.data.dto.SphinxResult;
import com.cmg.android.bbcaccent.data.dto.UserVoiceModel;
import com.cmg.android.bbcaccent.data.sqlite.WordDBAdapter;
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
import com.cmg.android.bbcaccent.view.PopoverView;
import com.cmg.android.bbcaccent.view.RecordingView;
import com.google.gson.Gson;
import com.luhonghai.litedb.exception.LiteDatabaseException;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import it.sephiroth.android.library.widget.HListView;

/**
 * Created by luhonghai on 12/22/14.
 */
public class DetailActivity extends BaseFragment implements View.OnClickListener, RecordingView.OnAnimationListener {

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

    private DisplayingState displayingState;

    private MaterialMenuView materialMenu;

    private FragmentTabHost mTabHost;

    private RecordingView recordingView;

    private ImageButton btnAudio;

    private AlwaysMarqueeTextView txtWord;

    private AlwaysMarqueeTextView txtPhonemes;

    private RelativeLayout rlVoiceExample;

    private UserVoiceModel model;

    private PlayerHelper player;

    private ButtonState lastState;

    private WebView webView;

    private HListView hListView;

    private boolean isPlaying = false;

    private Map<String, String> mapCMUvsIPA;

    private int receiverListenerId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.detail, null);
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
        return root;
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

    }

    private void showPhonemesListView() {
        if (model == null || model.getResult() == null) return;
        //List<String> phonemes = model.getResult().getCorrectPhonemes();
        //int size = model.getResult().getCorrectPhonemes().size();
        List<SphinxResult.PhonemeScore> phonemeScores = model.getResult().getPhonemeScores();
        SphinxResult.PhonemeScore[] scores = null;
        if (phonemeScores == null || phonemeScores.size() == 0) {
            //TODO validate if not contain any scores
        } else {
            scores = new SphinxResult.PhonemeScore[phonemeScores.size()];
            phonemeScores.toArray(scores);
            PhoneScoreAdapter scoreAdapter = new PhoneScoreAdapter(MainApplication.getContext(), scores, this);
            hListView.setAdapter(scoreAdapter);
            scoreAdapter.notifyDataSetChanged();
        }
    }

    private void initDetailView(View root) {
        rlVoiceExample = (RelativeLayout) root.findViewById(R.id.rlVoiceExample);
        hListView = (HListView) root.findViewById(R.id.listViewScore);
        webView = (WebView) root.findViewById(R.id.webview_score);
        recordingView = (RecordingView) root.findViewById(R.id.main_recording_view);
        recordingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDetail();
            }
        });
        recordingView.setAnimationListener(this);
        btnAudio = (ImageButton) root.findViewById(R.id.btnAudio);
        btnAudio.setOnClickListener(this);
        txtPhonemes = (AlwaysMarqueeTextView) root.findViewById(R.id.txtPhoneme);
        txtPhonemes.setOnClickListener(this);
        txtWord = (AlwaysMarqueeTextView) root.findViewById(R.id.txtWord);
        txtWord.setOnClickListener(this);
        rlVoiceExample.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (model != null) {
            displayingState = DisplayingState.WAIT_FOR_ANIMATION_MAX;
            recordingView.setScore(0.0f);
            recordingView.recycle();
            recordingView.invalidate();
            recordingView.startPingAnimation(getActivity(), 2000, model.getScore(), true, true);
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
    }

    private void initTabHost(View root) {
        mTabHost = (FragmentTabHost) root.findViewById(android.R.id.tabhost);
        if (mTabHost == null || model == null) return;

        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        Bundle bundle = new Bundle();
        bundle.putString(FragmentTab.ARG_WORD, model.getWord());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlVoiceExample:
                //case R.id.txtPhoneme:
                //case R.id.txtWord:
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
            case R.id.txtPhonemeScore:
                showDialog(v);
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

    @Deprecated
    private void showPopup(View v) {
        View root = getView();
        if (root == null) return;
        final SphinxResult.PhonemeScore score = (SphinxResult.PhonemeScore) v.getTag();
        final float totalScore = score.getTotalScore();
        RelativeLayout rootView = (RelativeLayout) root.findViewById(R.id.content);
        PopoverView popoverView = new PopoverView(getActivity(), R.layout.popover_showed_view);
        popoverView.setContentSizeForViewInPopover(new Point(440, 260));
        popoverView.setDelegate(new PopoverView.PopoverViewDelegate() {
            @Override
            public void popoverViewWillShow(PopoverView view) {

            }

            @Override
            public void popoverViewDidShow(PopoverView view) {
                RecordingView recordingView = (RecordingView) view.findViewById(R.id.cyclePhoneScore);
                if (recordingView != null) {
                    recordingView.setScore(totalScore);
                    recordingView.showScore();
                    recordingView.setOnClickListener(DetailActivity.this);
                } else {
                    AppLog.logString("No score view found");
                }

                TextView textView = (TextView) view.findViewById(R.id.txtTotalPhonemeScore);
                if (textView != null) {
                    textView.setOnClickListener(DetailActivity.this);
                    if (totalScore > 80.0f) {
                        textView.setTextColor(getResources().getColor(R.color.app_green));
                    } else if (totalScore >= 45.0f) {
                        textView.setTextColor(getResources().getColor(R.color.app_orange));
                    } else {
                        textView.setTextColor(getResources().getColor(R.color.app_red));
                    }
                    textView.setText(Math.round(totalScore) + "% like " + score.getName().toUpperCase());
                }
                List<SphinxResult.PhonemeScoreUnit> scoreItems = score.getPhonemes();
                if (scoreItems != null && scoreItems.size() > 0) {
                    SphinxResult.PhonemeScoreUnit[] scoreItemArray = new SphinxResult.PhonemeScoreUnit[scoreItems.size()];
                    scoreItems.toArray(scoreItemArray);
                    ListView listView = (ListView) view.findViewById(R.id.listViewScoreItem);
                    PhoneScoreItemAdapter adapter = new PhoneScoreItemAdapter(getActivity(), scoreItemArray);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void popoverViewWillDismiss(PopoverView view) {

            }

            @Override
            public void popoverViewDidDismiss(PopoverView view) {
                RecordingView recordingView = (RecordingView) view.findViewById(R.id.cyclePhoneScore);
                if (recordingView != null) {
                    try {
                        recordingView.recycle();
                    } catch (Exception ex) {}
                }
            }
        });
        popoverView.showPopoverFromRectInViewGroup(rootView, PopoverView.getFrameForView(v), PopoverView.PopoverArrowDirectionDown, true);
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

    private void showPhonemes() {
        if (model == null || model.getResult() == null || webView == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StringBuffer sb1 = new StringBuffer();
                StringBuffer sb2 = new StringBuffer();
                sb1.append("<table style=\"margin: 0 auto;\" cellpadding=\"3\" cellspacing=\"3\"><tr>");
                sb2.append("<table style=\"margin: 0 auto;\" cellpadding=\"3\" cellspacing=\"3\"><tr>");
                List<SphinxResult.PhonemeScore> phonemeScores = model.getResult().getPhonemeScores();
                if (phonemeScores != null && phonemeScores.size() > 0) {
                    for (SphinxResult.PhonemeScore phonemeScore : phonemeScores) {

                        sb1.append("<td style=\"background-color: " + ColorHelper.COLOR_DEFAULT_STRING + ";color: white;width: 20px;height:20px;text-align: center;font-weight: bold;border-radius: 5px;\">");
                        sb1.append(phonemeScore.getName());
                        sb1.append("</td>");

                        sb2.append("<td style=\"background-color: ");
                        if (phonemeScore.getTotalScore() >= 80.0) {
                            sb2.append(ColorHelper.COLOR_GREEN_STRING);
                        } else if (phonemeScore.getTotalScore() >= 45.0) {
                            sb2.append(ColorHelper.COLOR_ORANGE_STRING);
                        } else {
                            sb2.append(ColorHelper.COLOR_RED_STRING);
                        }
                        sb2.append(";color: white;width: 20px;height:20px;text-align: center;font-weight: bold;border-radius: 5px;\">");
                        sb2.append(phonemeScore.getName());
                        sb2.append("</td>");
                    }
                } else {
                    List<String> correctPhonemes = model.getResult().getCorrectPhonemes();
                    if (correctPhonemes != null && correctPhonemes.size() > 0) {
                        for (String phoneme : correctPhonemes) {
                            sb1.append("<td style=\"background-color: " + ColorHelper.COLOR_DEFAULT_STRING + ";color: white;width: 20px;height:20px;text-align: center;font-weight: bold;border-radius: 5px;\">");
                            sb1.append(phoneme);
                            sb1.append("</td>");

                            sb2.append("<td style=\"background-color: ");
                            sb2.append(ColorHelper.COLOR_GRAY_STRING);
                            sb2.append(";color: white;width: 20px;height:20px;text-align: center;font-weight: bold;border-radius: 5px;\">");
                            sb2.append(phoneme);
                            sb2.append("</td>");
                        }
                    }
                }
                sb1.append("</tr></table>");
                sb2.append("</tr></table>");
                webView.loadData("<style>body{margin:0;padding:0;background:white}</style><div style='background:white;margin:0;padding:0'>" + sb1.toString() + sb2.toString() + "</div>","text/html", "UTF-8");
                webView.reload();
            }
        });
    }

    @Override
    public void onAnimationMax() {
        if (displayingState == DisplayingState.WAIT_FOR_ANIMATION_MAX) {
            AppLog.logString("On animation max");
            recordingView.stopPingAnimation();
            displayingState = DisplayingState.DEFAULT;
        }
    }

    @Override
    public void onAnimationMin() {

    }
}
