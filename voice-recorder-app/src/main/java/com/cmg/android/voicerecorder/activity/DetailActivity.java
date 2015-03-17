package com.cmg.android.voicerecorder.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.cmg.android.voicerecorder.AppLog;
import com.cmg.android.voicerecorder.PlayerHelper;
import com.cmg.android.voicerecorder.R;
import com.cmg.android.voicerecorder.activity.fragment.FragmentTab;
import com.cmg.android.voicerecorder.activity.fragment.GraphFragment;
import com.cmg.android.voicerecorder.activity.fragment.HistoryFragment;
import com.cmg.android.voicerecorder.activity.fragment.TipFragment;
import com.cmg.android.voicerecorder.activity.view.RecordingView;
import com.cmg.android.voicerecorder.activity.view.adapter.PhoneScoreAdapter;
import com.cmg.android.voicerecorder.activity.view.adapter.PhoneScoreItemAdapter;
import com.cmg.android.voicerecorder.data.SphinxResult;
import com.cmg.android.voicerecorder.data.UserVoiceModel;
import com.cmg.android.voicerecorder.dictionary.DictionaryItem;
import com.cmg.android.voicerecorder.dictionary.OxfordDictionaryWalker;
import com.cmg.android.voicerecorder.utils.ColorHelper;
import com.cmg.android.voicerecorder.view.PopoverView;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import it.sephiroth.android.library.widget.HListView;

/**
 * Created by luhonghai on 12/22/14.
 */
public class DetailActivity extends BaseActivity implements View.OnClickListener, RecordingView.OnAnimationListener {

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

    private TextView txtWord;

    private TextView txtPhonemes;

    private DictionaryItem dictionaryItem;

    private UserVoiceModel model;

    private PlayerHelper player;

    private ButtonState lastState;

    private WebView webView;

    private HListView hListView;

    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new Gson();
        model = gson.fromJson(this.getIntent().getExtras().get(USER_VOICE_MODEL).toString(), UserVoiceModel.class);
        setContentView(R.layout.detail);
        initCustomActionBar();
        initTabHost();
        initDetailView();
        showData(model, false);
        registerReceiver(mHandleHistoryAction, new IntentFilter(HistoryFragment.ON_HISTORY_LIST_CLICK));
    }

    private void showData(UserVoiceModel userVoiceModel, boolean showScore) {
        model = userVoiceModel;
        dictionaryItem = OxfordDictionaryWalker.getExistingDictionary(this, model.getWord());
        recordingView.setScore(model.getScore());
        txtPhonemes.setText(dictionaryItem.getPronunciation());
        txtWord.setText(dictionaryItem.getWord());
        if (model.getScore() >= 80.0) {
            lastState = ButtonState.GREEN;
        } else if (model.getScore() >= 45.0) {
            lastState = ButtonState.ORANGE;
        } else {
            lastState = ButtonState.RED;
        }
        switchButtonState();
        showPhonemes();
        showPhonemesListView();
        if (showScore) {
            displayingState = DisplayingState.WAIT_FOR_ANIMATION_MAX;
            recordingView.setScore(0.0f);
            recordingView.recycle();
            recordingView.invalidate();
            recordingView.startPingAnimation(DetailActivity.this, 2000, model.getScore(), true);
        }
    }

    private void showPhonemesListView() {
        if (model == null || model.getResult() == null) return;
        List<SphinxResult.PhonemeScore> phonemeScores = model.getResult().getPhonemeScores();
        if (phonemeScores == null || phonemeScores.size() == 0) return;
        SphinxResult.PhonemeScore[] scores = new SphinxResult.PhonemeScore[phonemeScores.size()];
        phonemeScores.toArray(scores);
        PhoneScoreAdapter scoreAdapter = new PhoneScoreAdapter(this, scores, this);
        hListView.setAdapter(scoreAdapter);
        scoreAdapter.notifyDataSetChanged();
    }

    private void initDetailView() {
        hListView = (HListView) findViewById(R.id.listViewScore);
        webView = (WebView) findViewById(R.id.webview_score);
        recordingView = (RecordingView) findViewById(R.id.main_recording_view);
        recordingView.setAnimationListener(this);
        btnAudio = (ImageButton) findViewById(R.id.btnAudio);
        btnAudio.setOnClickListener(this);
        txtPhonemes = (TextView) findViewById(R.id.txtPhoneme);
        txtPhonemes.setOnClickListener(this);
        txtWord = (TextView) findViewById(R.id.txtWord);
        txtWord.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (model != null) {
            displayingState = DisplayingState.WAIT_FOR_ANIMATION_MAX;
            recordingView.setScore(0.0f);
            recordingView.recycle();
            recordingView.invalidate();
            recordingView.startPingAnimation(DetailActivity.this, 2000, model.getScore(), true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (model != null) {
            recordingView.stopPingAnimation();
            displayingState = DisplayingState.DEFAULT;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(mHandleHistoryAction);
        } catch (Exception ex) {

        }
        try {
            recordingView.stopPingAnimation();
        } catch (Exception ex) {

        }
        try {
            recordingView.recycle();
        } catch (Exception ex) {

        }
    }

    private void initTabHost() {
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        if (mTabHost == null || model == null) return;

        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        Bundle bundle = new Bundle();
        bundle.putString(FragmentTab.ARG_WORD, model.getWord());
        Gson gson = new Gson();
        bundle.putString(DetailActivity.USER_VOICE_MODEL, gson.toJson(model));

        mTabHost.addTab(
                mTabHost.newTabSpec("graph").setIndicator("Graph", null),
                GraphFragment.class, bundle);
        mTabHost.addTab(
                mTabHost.newTabSpec("history").setIndicator("History", null),
                HistoryFragment.class, bundle);
        mTabHost.addTab(
                mTabHost.newTabSpec("tips").setIndicator("Tips", null),
                TipFragment.class, bundle);
    }

    private void initCustomActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.main_action_bar);
        materialMenu = (MaterialMenuView) actionBar.getCustomView().findViewById(R.id.action_bar_menu);
        materialMenu.setState(MaterialMenuDrawable.IconState.ARROW);
        materialMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailActivity.this.onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtPhoneme:
            case R.id.txtWord:
                play(dictionaryItem.getAudioFile());
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
            case R.id.txtPhonemeDefault:
                showPopup(v);
                break;
            case R.id.txtPhonemeScore:
                showPopup(v);
                break;
        }
    }

    private void showPopup(View v) {
        final SphinxResult.PhonemeScore score = (SphinxResult.PhonemeScore) v.getTag();
        final float totalScore = score.getTotalScore();
        RelativeLayout rootView = (RelativeLayout)findViewById(R.id.content);
        PopoverView popoverView = new PopoverView(this, R.layout.popover_showed_view);
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
                    PhoneScoreItemAdapter adapter = new PhoneScoreItemAdapter(DetailActivity.this, scoreItemArray);
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
        play(new File(file));
    }

    private void play(File file) {
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
    }

    private void playAudio() {
        isPlaying = true;
        switchButtonState(ButtonState.PLAYING);
        try {
            if (player != null) {
                try {
                    player.stop();
                } catch (Exception ex) {

                }
            }
            player = new PlayerHelper(new File(model.getAudioFile()), new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    isPlaying = false;
                    runOnUiThread(new Runnable() {
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
        }
    }

    private void showPhonemes() {
        if (model == null || model.getResult() == null || webView == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StringBuffer sb1 = new StringBuffer();
                StringBuffer sb2 = new StringBuffer();
                sb1.append("<table style=\"margin: 0 auto;\" cellpadding=\"3\" cellspacing=\"3\"><tr>");
                sb2.append("<table style=\"margin: 0 auto;\" cellpadding=\"3\" cellspacing=\"3\"><tr>");
                List<SphinxResult.PhonemeScore> phonemeScores = model.getResult().getPhonemeScores();
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

    private final BroadcastReceiver mHandleHistoryAction = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle.containsKey(BaseActivity.USER_VOICE_MODEL)) {
                String modelSource = bundle.getString(BaseActivity.USER_VOICE_MODEL);
                String word = bundle.getString(FragmentTab.ARG_WORD);
                int type = bundle.getInt(FragmentTab.ACTION_TYPE);
                if (word != null && word.length() > 0) {
                    Gson gson = new Gson();
                    final UserVoiceModel model = gson.fromJson(modelSource, UserVoiceModel.class);
                    if (model != null) {
                        switch (type) {
                            case HistoryFragment.CLICK_LIST_ITEM:
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showData(model, true);
                                    }
                                });
                                break;
                        }
                    }
                } else {
                    if (type == HistoryFragment.CLICK_RECORD_BUTTON)
                        onBackPressed();
                }
            }
        }
    };
}
