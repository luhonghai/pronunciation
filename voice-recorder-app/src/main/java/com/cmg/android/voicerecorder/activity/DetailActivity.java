package com.cmg.android.voicerecorder.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.cmg.android.voicerecorder.AppLog;
import com.cmg.android.voicerecorder.PlayerHelper;
import com.cmg.android.voicerecorder.R;
import com.cmg.android.voicerecorder.activity.fragment.FragmentTab;
import com.cmg.android.voicerecorder.activity.view.RecordingView;
import com.cmg.android.voicerecorder.data.SphinxResult;
import com.cmg.android.voicerecorder.data.UserVoiceModel;
import com.cmg.android.voicerecorder.dictionary.DictionaryItem;
import com.cmg.android.voicerecorder.utils.ColorHelper;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

/**
 * Created by luhonghai on 12/22/14.
 */
public class DetailActivity extends BaseActivity implements View.OnClickListener{

    enum ButtonState {
        RED,
        ORANGE,
        GREEN,
        PLAYING
    }

    public static final String DICTIONARY_ITEM = "DICTIONARY_ITEM";

    public static final String USER_VOICE_MODEL = "USER_VOICE_MODEL";

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

    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        initCustomActionBar();
        initTabHost();

        Gson gson = new Gson();
        model = gson.fromJson(this.getIntent().getExtras().get(USER_VOICE_MODEL).toString(), UserVoiceModel.class);
        dictionaryItem = gson.fromJson(this.getIntent().getExtras().get(DICTIONARY_ITEM).toString(), DictionaryItem.class);
        initDetailView();
        showPhonemes();
    }

    private void initDetailView() {
        webView = (WebView) findViewById(R.id.webview_score);
        recordingView = (RecordingView) findViewById(R.id.main_recording_view);
        btnAudio = (ImageButton) findViewById(R.id.btnAudio);
        btnAudio.setOnClickListener(this);
        txtPhonemes = (TextView) findViewById(R.id.txtPhoneme);
        txtPhonemes.setText(dictionaryItem.getPronunciation());
        txtPhonemes.setOnClickListener(this);
        txtWord = (TextView) findViewById(R.id.txtWord);
        txtWord.setText(dictionaryItem.getWord());
        txtWord.setOnClickListener(this);

        recordingView.setScore(model.getScore());
        if (model.getScore() >= 80.0) {
            lastState = ButtonState.GREEN;
        } else if (model.getScore() >= 45.0) {
            lastState = ButtonState.ORANGE;
        } else {
            lastState = ButtonState.RED;
        }
        switchButtonState();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initTabHost() {
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        if (mTabHost == null) return;

        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(
                mTabHost.newTabSpec("graph").setIndicator("Graph", null),
                FragmentTab.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("history").setIndicator("History", null),
                FragmentTab.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("tips").setIndicator("Tips", null),
                FragmentTab.class, null);
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
        }
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
                    AppLog.logString("Stop playing");
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
            }
        });
    }
}
