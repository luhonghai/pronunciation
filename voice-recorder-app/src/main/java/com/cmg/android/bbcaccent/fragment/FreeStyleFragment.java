package com.cmg.android.bbcaccent.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTabHost;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.cmg.android.bbcaccent.LoginActivity;
import com.cmg.android.bbcaccent.MainActivity;
import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.auth.AccountManager;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.data.dto.PronunciationScore;
import com.cmg.android.bbcaccent.data.dto.SphinxResult;
import com.cmg.android.bbcaccent.data.dto.UserProfile;
import com.cmg.android.bbcaccent.data.dto.UserVoiceModel;
import com.cmg.android.bbcaccent.data.sqlite.freestyle.PhonemeScoreDBAdapter;
import com.cmg.android.bbcaccent.data.sqlite.freestyle.ScoreDBAdapter;
import com.cmg.android.bbcaccent.data.sqlite.freestyle.WordDBAdapter;
import com.cmg.android.bbcaccent.dictionary.DictionaryItem;
import com.cmg.android.bbcaccent.dictionary.DictionaryListener;
import com.cmg.android.bbcaccent.dictionary.DictionaryWalker;
import com.cmg.android.bbcaccent.dictionary.DictionaryWalkerFactory;
import com.cmg.android.bbcaccent.dsp.AndroidAudioInputStream;
import com.cmg.android.bbcaccent.extra.SwitchFragmentParameter;
import com.cmg.android.bbcaccent.fragment.tab.FragmentTab;
import com.cmg.android.bbcaccent.fragment.tab.GraphFragmentParent;
import com.cmg.android.bbcaccent.fragment.tab.HistoryFragment;
import com.cmg.android.bbcaccent.fragment.tab.TipFragment;
import com.cmg.android.bbcaccent.helper.PlayerHelper;
import com.cmg.android.bbcaccent.helper.PopupShowcaseHelper;
import com.cmg.android.bbcaccent.http.ResponseData;
import com.cmg.android.bbcaccent.http.UploaderAsync;
import com.cmg.android.bbcaccent.http.common.FileCommon;
import com.cmg.android.bbcaccent.utils.AnalyticHelper;
import com.cmg.android.bbcaccent.utils.AndroidHelper;
import com.cmg.android.bbcaccent.utils.AppLog;
import com.cmg.android.bbcaccent.utils.ColorHelper;
import com.cmg.android.bbcaccent.utils.DeviceUuidFactory;
import com.cmg.android.bbcaccent.utils.FileHelper;
import com.cmg.android.bbcaccent.utils.RandomHelper;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.cmg.android.bbcaccent.view.AlwaysMarqueeTextView;
import com.cmg.android.bbcaccent.view.RecordingView;
import com.cmg.android.bbcaccent.view.ShowcaseHelper;
import com.cmg.android.bbcaccent.view.SlidingUpPanelLayout;
import com.cmg.android.bbcaccent.view.cardview.CircleCardView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.nineoldandroids.animation.Animator;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.Oscilloscope;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;
import uk.co.deanwild.materialshowcaseview.shape.RectangleShape;
import uk.co.deanwild.materialshowcaseview.target.ActionItemTarget;
import uk.co.deanwild.materialshowcaseview.target.ViewTarget;

/**
 * Created by luhonghai on 12/10/2015.
 */
public class FreeStyleFragment extends BaseFragment implements RecordingView.OnAnimationListener, LocationListener {

    /**
     * Recording
     */
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private static final long PITCH_TIMEOUT = 1000;

    private static final long START_TIMEOUT = 1000;

    private static final long RECORD_MAX_LENGTH = 6000;

    private AnalyzingState analyzingState = AnalyzingState.DEFAULT;

    private ButtonState lastState;

    @Bind(android.R.id.tabhost)
    FragmentTabHost mTabHost;

    @Bind(R.id.main_recording_view)
    RecordingView recordingView;

    @Bind(R.id.btnAnalyzing)
    CircleCardView btnAnalyzing;

    @Bind(R.id.btnAudio)
    CircleCardView btnAudio;

    @Bind(R.id.txtWord)
    AlwaysMarqueeTextView txtWord;

    @Bind(R.id.txtPhoneme)
    AlwaysMarqueeTextView txtPhonemes;

    @Bind(R.id.rlVoiceExample)
    View rlVoiceExample;

    @Bind(R.id.imgHourGlass)
    ImageButton imgHourGlass;

    @Bind(R.id.imgHelpHand)
    ImageButton imgHelpHand;

    @Bind(R.id.panelSlider)
    SlidingUpPanelLayout panelSlider;

    @Bind(R.id.txtDefinition)
    TextView txtDefinition;

    @Bind(R.id.rlSliderContent)
    LinearLayout rlSliderContent;

    private AndroidAudioInputStream audioStream;
    private AudioDispatcher dispatcher;
    private Thread runner;
    private double pitch = 0;
    private long lastDetectedPitchTime = -1;

    private AudioRecord audioInputStream;
    private int chanel;
    private int sampleRate;
    private int bufferSize;

    private boolean isRecording = false;
    private boolean isPlaying = false;
    private boolean isAutoStop = false;

    private PlayerHelper player;

    private long start;

    private String selectedWord;

    private UploaderAsync uploadTask;

    /**
     * Score
     */
    private ScoreDBAdapter scoreDBAdapter;

    private WordDBAdapter dbAdapter;

    private AccountManager accountManager;

    private int receiverListenerId;

    private ViewState viewState;

    private ShowcaseHelper showcaseHelper;

    private PopupShowcaseHelper popupShowcaseHelper;

    private PopupShowcaseHelper.HelpItem currentHelpItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Gson gson = new Gson();
        View root = inflater.inflate(R.layout.fragment_free_style, null);
        ButterKnife.bind(this, root);
        accountManager = new AccountManager(getActivity());
        initTabHost();
        initRecordingView();
        switchButtonStage(ButtonState.DISABLED);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(MainBroadcaster.Filler.Key.VIEW_STATE.toString())) {
                viewState = gson.fromJson(savedInstanceState.getString(MainBroadcaster.Filler.Key.VIEW_STATE.toString()), ViewState.class);
            }
        }
        if (viewState == null) {
            viewState = new ViewState();
        }
        scoreDBAdapter = new ScoreDBAdapter();
        dbAdapter = new WordDBAdapter();
        receiverListenerId = MainBroadcaster.getInstance().register(new MainBroadcaster.ReceiverListener() {

            @Override
            public void onReceiveMessage(MainBroadcaster.Filler filler, Bundle bundle) {
                if (filler == MainBroadcaster.Filler.RESET_TIMING_HELP) {
                    if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
                }
            }

            @Override
            public void onUserModelFetched(ResponseData<UserVoiceModel> model) {
                if (model != null && model.isStatus()) {
                    viewState.currentModel = model.getData();
                    viewState.errorMessage = null;
                    //if (viewState.currentModel != null) viewState.currentModel.setScore(99.0f);
                    try {
                        saveToDatabase();
                    } catch (Exception e) {
                        SimpleAppLog.error("Could not save data to database", e);
                        viewState.currentModel = null;
                    }
                } else {
                    viewState.errorMessage = (model == null) ? "" : model.getMessage();
                    viewState.currentModel = null;
                }
                AppLog.logString("Start score animation");
                analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MIN;
            }
            @Override
            public void onHistoryAction(final UserVoiceModel model, String word, int type) {
                switch (type) {
                    case HistoryFragment.CLICK_LIST_ITEM:
                        if (word == null || word.length() == 0) {
                            Bundle bundle = new Bundle();
                            bundle.putString(MainBroadcaster.Filler.USER_VOICE_MODEL.toString(), gson.toJson(model));
                            //MainApplication.getContext().setSelectedWord(model.getWord());
                            MainBroadcaster.getInstance().getSender().sendSwitchFragment(
                                    DetailFragment.class,
                                    new SwitchFragmentParameter(true, true, true),
                                    bundle);
                        }
                        break;
                    case HistoryFragment.CLICK_PLAY_BUTTON:
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                play(model.getAudioFile());
                            }
                        });
                        break;
                    case HistoryFragment.CLICK_RECORD_BUTTON:
                        getWord(model.getWord());
                        break;
                }
            }

            @Override
            public void onSearchWord(String word) {
                getWord(word);
            }
        });
        if (viewState.currentModel == null) {
            if (viewState.willSearchRandomWord || viewState.dictionaryItem == null) {
                String[] words = getResources().getStringArray(R.array.random_words);
                if (words != null && words.length > 0) {
                    getWord(words[RandomHelper.getRandomIndex(words.length)].trim());
                } else {
                    getWord(getString(R.string.example_word));
                }
                viewState.willSearchRandomWord = false;
            } else {
                displayDictionaryItem();
                switchButtonStage(ButtonState.DEFAULT);
                recordingView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recordingView.recycleView();
                    }
                }, 100);
            }
        } else {
            displayDictionaryItem();
            recordingView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recordingView.recycleView();
                    analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MAX;
                    recordingView.startPingAnimation(getActivity(), 0, viewState.currentModel.getScore(), true, false);
                }
            }, 100);
        }

        initSlider(root);
        showcaseHelper = new ShowcaseHelper(getActivity());

        popupShowcaseHelper = new PopupShowcaseHelper(getActivity(),
                new PopupShowcaseHelper.HelpItem[] {
                        PopupShowcaseHelper.HelpItem.ANALYZE_A_WORD,
                        PopupShowcaseHelper.HelpItem.PROGRESS,
                        PopupShowcaseHelper.HelpItem.HISTORY,
                        PopupShowcaseHelper.HelpItem.TIPS,
                },
                new PopupShowcaseHelper.OnSelectHelpItem() {
                    @Override
                    public void onSelectHelpItem(PopupShowcaseHelper.HelpItem helpItem) {
                        if (showcaseHelper == null) return;
                        switch (helpItem) {
                            case ANALYZE_A_WORD:
                                showcaseHelper.showHelp(new ShowcaseHelper.HelpState(txtWord, getString(R.string.help_press_the_word)),
                                        new ShowcaseHelper.HelpState(btnAnalyzing, getString(R.string.help_test_pronunciation)),
                                        new ShowcaseHelper.HelpState(btnAudio, getString(R.string.help_hear_last_attempt)));
                                break;
                            case TIPS:
                            case PROGRESS:
                            case HISTORY:
                                currentHelpItem = helpItem;
                                if (panelSlider != null && panelSlider.getPanelState() != SlidingUpPanelLayout.PanelState.EXPANDED) {
                                    panelSlider.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                                } else {
                                    showRemainHelpItem();
                                }
                                break;
                        }
                    }
                });
        popupShowcaseHelper.resetTiming();

        if (viewState.willShowHelpSearchWordAndSlider) {
            showcaseHelper.showHelp(ShowcaseHelper.HelpKey.SEARCH_WORD,
                    new ShowcaseHelper.HelpState(new ActionItemTarget(getActivity(), R.id.menu_search),
                            "Don't forget to try new words by searching for them by pressing the magnifying glass")
//                    new ShowcaseHelper.HelpState(root.findViewById(R.id.btnSlider),
//                            "Swipe up or tap to track progress, view past words and tips")
                    );
//            viewState.willShowHelpSearchWordAndSlider = false;
        }
        return root;
    }

    private void showRemainHelpItem() {
        if (currentHelpItem != null && showcaseHelper != null && rlSliderContent != null) {
            if (mTabHost != null) {
                int currentTab = mTabHost.getCurrentTab();
                if (currentHelpItem == PopupShowcaseHelper.HelpItem.PROGRESS && currentTab != 0) {
                    mTabHost.setCurrentTab(0);
                } else if (currentHelpItem == PopupShowcaseHelper.HelpItem.HISTORY && currentTab != 1) {
                    mTabHost.setCurrentTab(1);
                } else if (currentHelpItem == PopupShowcaseHelper.HelpItem.TIPS && currentTab != 2) {
                    mTabHost.setCurrentTab(2);
                }
                ShowcaseConfig showcaseConfig = new ShowcaseConfig();
                showcaseConfig.setShape(new RectangleShape(new ViewTarget(rlSliderContent).getBounds(), false));
                switch (currentHelpItem) {
                    case PROGRESS:
                        showcaseHelper.showHelp(showcaseConfig,new ShowcaseHelper.HelpState(rlSliderContent,
                                getString(R.string.help_track_progress)));
                        break;
                    case HISTORY:
                        showcaseHelper.showHelp(showcaseConfig,new ShowcaseHelper.HelpState(rlSliderContent,
                                getString(R.string.help_view_past_words)));
                        break;
                    case TIPS:
                        showcaseHelper.showHelp(showcaseConfig,new ShowcaseHelper.HelpState(rlSliderContent,
                                getString(R.string.help_view_tips)));
                        break;
                }
            }
            currentHelpItem = null;
        }
    }



    private void initRecordingView() {
        recordingView.setAnimationListener(this);
        txtPhonemes.setText("");
        txtWord.setText("");
        txtDefinition.setText("");
    }

    private void initTabHost() {
        if (mTabHost == null) return;
        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);
        addTabImage(R.drawable.tab_graph,
                GraphFragmentParent.class, getString(R.string.tab_graph));
        addTabImage(R.drawable.tab_history,
                HistoryFragment.class, getString(R.string.tab_history));
        addTabImage(R.drawable.tab_tip,
                TipFragment.class, getString(R.string.tab_tip));
    }

    private void displayDictionaryItem() {
        if (viewState.dictionaryItem != null) {
            txtPhonemes.setText(viewState.dictionaryItem.getPronunciation());
            txtWord.setText(viewState.dictionaryItem.getWord());
            txtDefinition.setText(viewState.dictionaryItem.getDefinition());
        }
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
        txtPhonemes.setVisibility(View.INVISIBLE);
        panelSlider.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {

            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                float test = (halfHeight - root.findViewById(R.id.btnSlider).getHeight()) / (float) halfHeight;
                //SimpleAppLog.debug("Offset: " + slideOffset + ". Test: " + test);
                if (slideOffset > test) {
                    txtPhonemes.setVisibility(View.INVISIBLE);
                } else {
                    txtPhonemes.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPanelCollapsed(View panel) {
                if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
            }

            @Override
            public void onPanelExpanded(View panel) {
                if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
                showRemainHelpItem();
            }

            @Override
            public void onPanelAnchored(View panel) {
                if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
            }

            @Override
            public void onPanelHidden(View panel) {
                if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
            }
        });
        if (viewState.willCollapseSlider) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (panelSlider != null) {
                        panelSlider.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        viewState.willCollapseSlider = false;
                    }
                }
            }, 2000);
        }

    }
    private void addTabImage(int drawableId, Class<?> c, String labelId) {
        TabHost.TabSpec spec = mTabHost.newTabSpec(labelId).setIndicator(null, getResources().getDrawable(drawableId));
        mTabHost.addTab(spec, c, null);
    }

    private void completeGetWord(DictionaryItem item, ButtonState state) {
        if (item != null) {
            viewState.dictionaryItem = item;
        } else {
            viewState.dictionaryItem = null;
        }
        analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MIN;
        SimpleAppLog.debug("Complete get word. Wait for animation min");
        lastState = state;
        if (audioStream != null)
            audioStream.clearOldRecord();
    }

    private class GetWordAsync extends AsyncTask {

        private final String word;

        private GetWordAsync(String word) {
            this.word = word;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            selectedWord = word;
            DictionaryWalker walker = DictionaryWalkerFactory.getInstance();
            walker.setListener(new DictionaryListener() {
                @Override
                public void onDetectWord(final DictionaryItem dItem) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            completeGetWord(dItem, ButtonState.DEFAULT);
                        }
                    });
                }

                @Override
                public void onWordNotFound(DictionaryItem dItem, final FileNotFoundException ex) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ex.printStackTrace();
                            completeGetWord(null, ButtonState.DISABLED);
                        }
                    });
                }

                @Override
                public void onError(DictionaryItem dItem, final Exception ex) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ex.printStackTrace();
                            completeGetWord(null, ButtonState.DISABLED);
                        }
                    });
                }
            });
            walker.execute(word);
            return null;
        }
    }

    private GetWordAsync getWordAsync;

    private void getWord(final String word) {
        if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
        if (isRecording) return;
        try {
            if (dbAdapter == null) dbAdapter = new WordDBAdapter();
//            if (!dbAdapter.isBeep(word)) {
//                AnalyticHelper.sendSelectWordNotInBeep(MainApplication.getContext(), word);
//                SweetAlertDialog d = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
//                d.setTitleText(getString(R.string.word_not_in_beep_title));
//                d.setContentText(getString(R.string.word_not_in_beep_message));
//                d.setConfirmText(getString(R.string.dialog_ok));
//                d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                        sweetAlertDialog.dismissWithAnimation();
//                    }
//                });
//                d.show();
//                return;
//            }
        } catch (Exception e) {
            SimpleAppLog.error("Could not check word " + word + " is beep or not", e);
        }
        try {
            if (((MainActivity)getActivity()).checkNetwork(false)) {
                viewState.currentModel = null;
                recordingView.recycleView();
                analyzingState = AnalyzingState.DEFAULT;
                AnalyticHelper.sendSelectWord(MainApplication.getContext(), word);
                switchButtonStage(ButtonState.DISABLED);
                recordingView.startPingAnimation(getActivity());

                YoYo.with(Techniques.FadeIn).duration(700).withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        imgHourGlass.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).playOn(imgHourGlass);

                txtWord.setText(getString(R.string.searching));
                txtPhonemes.setText(getString(R.string.please_wait));
                txtDefinition.setText("");
                viewState.dictionaryItem = null;
                viewState.currentModel = null;
                if (getWordAsync != null) {
                    try {
                        while (!getWordAsync.isCancelled() && getWordAsync.cancel(true)) ;
                    } catch (Exception ex) {

                    }
                }
                getWordAsync = new GetWordAsync(word);
                getWordAsync.execute();
            }
        } catch (Exception e) {

        }
    }

    private void stopPlay() {
        if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
        switchButtonStage();
        isPlaying = false;
        player.stop();
    }

    private void play(String file) {
        if (file == null || file.length() == 0) return;
        play(new File(file));
    }

    private void play(File file) {
        if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
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
                }

            });
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkAudioExist() {
        if (audioStream == null || audioStream.getFilename() == null) return false;
        return new File(audioStream.getFilename()).exists();
    }

    private void play() {
        if (!checkAudioExist()) return;
        try {
            String fileName = audioStream.getFilename();
            File file = new File(fileName);
            if (!file.exists()) return;
            switchButtonStage(ButtonState.PLAYING);
            isPlaying = true;
            if (player != null) {
                try {
                    player.stop();
                } catch (Exception ex) {

                }
            }
            player = new PlayerHelper(new File(fileName), new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            stopPlay();
                        }
                    });

                }

            });
            start = System.currentTimeMillis();
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stop() {
        if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
        stopRecording();
        if (uploadTask != null) {
            try {
                while (!uploadTask.isCancelled() && uploadTask.cancel(true)) ;
                uploadTask = null;
            } catch (Exception ex) {

            }
        }
    }

    private void analyze() {
        try {
            if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
            // Clear old data
            viewState.currentModel = null;
            analyzingState = AnalyzingState.RECORDING;
            switchButtonStage(ButtonState.RECORDING);
            isRecording = true;
            if (recordingView != null)
                recordingView.recycle();
            audioInputStream = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    sampleRate, chanel, RECORDER_AUDIO_ENCODING, bufferSize);

            if (audioInputStream.getState() == AudioRecord.STATE_UNINITIALIZED) {
                String configResume = "initRecorderParameters(sRates) has found recorder settings supported by the device:"
                        + "\nSource   = MICROPHONE"
                        + "\nsRate    = " + sampleRate + "Hz"
                        + "\nChannel  = " + ((chanel == AudioFormat.CHANNEL_IN_MONO) ? "MONO" : "STEREO")
                        + "\nEncoding = 16BIT";
                AppLog.logString(configResume);
                audioInputStream.release();
                audioInputStream = null;
                return;
            }
            //start recording ! Opens the stream
            audioInputStream.startRecording();

            TarsosDSPAudioFormat format = new TarsosDSPAudioFormat(sampleRate, 16, (chanel == AudioFormat.CHANNEL_IN_MONO) ? 1 : 2, true, false);

            audioStream = new AndroidAudioInputStream(MainApplication.getContext(), audioInputStream, format, bufferSize);
            dispatcher = new AudioDispatcher(audioStream, bufferSize / 2, 0);
            dispatcher.addAudioProcessor(new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, sampleRate, bufferSize / 2, new PitchDetectionHandler() {

                @Override
                public void handlePitch(PitchDetectionResult pitchDetectionResult,
                                        AudioEvent audioEvent) {
                    pitch = pitchDetectionResult.getPitch();
                    if (pitch != -1) {
                        AppLog.logString("Detect pitch " + pitch);
                        lastDetectedPitchTime = System.currentTimeMillis();
                    }
                    long length = System.currentTimeMillis() - start;
                    if (length > RECORD_MAX_LENGTH || ((System.currentTimeMillis() - lastDetectedPitchTime) > PITCH_TIMEOUT)
                            && isAutoStop
                            && (length > (START_TIMEOUT + PITCH_TIMEOUT))) {
                        stopRecording(false);
                        uploadRecord();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recordingView.startPingAnimation(getActivity(), 2000, 100.0f, true, false);
                            }
                        });
                    }
                }
            }));
            dispatcher.addAudioProcessor(new Oscilloscope(new Oscilloscope.OscilloscopeEventHandler() {
                @Override
                public void handleEvent(final float[] floats, final AudioEvent audioEvent) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (recordingView != null)
                                recordingView.setData(floats, pitch);
                        }
                    });

                }
            }));
            start = System.currentTimeMillis();
            runner = new Thread(dispatcher, "Audio Dispatcher");
            lastDetectedPitchTime = System.currentTimeMillis();
            runner.start();
        } catch (Exception ex) {
            SimpleAppLog.error("Could not start recording", ex);
        }
    }

    private void stopRecording() {
        stopRecording(true);
    }

    private void stopRecording(boolean changeStatus) {
        if (isRecording) {
            if (changeStatus) {
                isRecording = false;
            }
            AppLog.logString("Stop Recording");
            try {
                if (audioInputStream != null) {
                    audioInputStream.stop();
                    audioInputStream.release();
                }
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
            try {
                if (dispatcher != null)
                    dispatcher.stop();
            } catch (Exception ex) {
                // ex.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handlerStartDetail.removeCallbacks(runnableStartDetail);
        if (popupShowcaseHelper != null) popupShowcaseHelper.recycle();
        stop();
        try {
            recordingView.recycle();
        } catch (Exception ex) {

        }
        MainBroadcaster.getInstance().unregister(receiverListenerId);
        ButterKnife.unbind(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTabHost != null) {
            mTabHost.getTabWidget().setEnabled(false);
        }
        stopRequestLocation();
    }

    protected void stopRequestLocation() {
        SimpleAppLog.info("Request location update");
        LocationManager lm = (LocationManager) MainApplication.getContext().getSystemService(Context.LOCATION_SERVICE);
        try {
            lm.removeUpdates(this);
        } catch (SecurityException e) {
            SimpleAppLog.error("Error location", e);
        } catch (Exception ex) {
            SimpleAppLog.error("Could not stop request location", ex);
        }
    }

    protected void requestLocation() {
        SimpleAppLog.info("Request location update");
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5 * 60 * 1000, 1000, this);
        } catch (SecurityException e) {
            SimpleAppLog.error("Error location", e);
        } catch (Exception e) {
            SimpleAppLog.error("Could not request GPS provider location", e);
        }
        try {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5 * 60 * 1000, 1000, this);
        } catch (SecurityException e) {
            SimpleAppLog.error("Error location", e);
        } catch (Exception e) {
            SimpleAppLog.error("Could not request Network provider location", e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mTabHost != null) {
            mTabHost.getTabWidget().setEnabled(true);
        }
        requestLocation();
        fetchSetting();
    }

    @OnClick({R.id.txtPhoneme, R.id.txtWord, R.id.rlVoiceExample})
    void playIPAVoice() {
        if (viewState.dictionaryItem != null) {
            play(viewState.dictionaryItem.getAudioFile());
        }
    }
    @OnClick(R.id.main_recording_view)
    void clickModelScore() {
        viewState.willShowHelpSearchWordAndSlider = true;
        handlerStartDetail.post(runnableStartDetail);
    }

    @OnClick(R.id.btnAudio)
    void playRecordedVoice() {
        if (isPlaying) {
            stopPlay();
        } else {
            play();
        }
    }

    @OnClick(R.id.btnAnalyzing)
    void clickAnalyzeVoice() {
        if (((MainActivity) getActivity()).checkNetwork(false)) {
            if (isRecording) {
                stop();
                switchButtonStage(ButtonState.DISABLED);
                if (viewState.currentModel != null) {
                    analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MAX;
                    recordingView.startPingAnimation(getActivity(), 2000, viewState.currentModel.getScore(), true, true);
                } else {
                    analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MIN;
                    recordingView.startPingAnimation(getActivity(), 1000, 100.0f, false, false);
                }
            } else {
                analyze();
            }
        }
    }
    private Handler handlerStartDetail = new Handler();

    private Runnable runnableStartDetail = new Runnable() {
        @Override
        public void run() {
            if (viewState.currentModel != null && viewState.dictionaryItem != null) {
                Gson gson = new Gson();
                Bundle bundle = new Bundle();
                bundle.putString(MainBroadcaster.Filler.USER_VOICE_MODEL.toString(), gson.toJson(viewState.currentModel));
                //MainApplication.getContext().setSelectedWord(viewState.currentModel.getWord());
                MainBroadcaster.getInstance().getSender().sendSwitchFragment(
                        DetailFragment.class,
                        new SwitchFragmentParameter(true, true, true),
                        bundle);
            }
        }
    };

    private void switchButtonStage() {
        if (lastState == null) lastState = ButtonState.DEFAULT;
        switchButtonStage(lastState);
    }

    private void switchButtonStage(ButtonState state) {
        try {
            boolean isProcess = true;
            imgHelpHand.setVisibility(View.GONE);
            switch (state) {
                case RECORDING:
                    ((ImageView)btnAudio.getChildAt(0)).setImageResource(R.drawable.ic_play);
                    btnAudio.setCardBackgroundColor(ColorHelper.getColor(R.color.app_gray));
                    btnAudio.setEnabled(false);
                    btnAnalyzing.setCardBackgroundColor(ColorHelper.getColor(R.color.app_red));
                    ((ImageView)btnAnalyzing.getChildAt(0)).setImageResource(R.drawable.ic_close);
                    txtPhonemes.setTextColor(ColorHelper.getColor(R.color.app_gray));
                    txtDefinition.setTextColor(ColorHelper.getColor(R.color.app_gray));
                    txtWord.setTextColor(ColorHelper.getColor(R.color.app_gray));
                    txtPhonemes.setEnabled(false);
                    txtWord.setEnabled(false);
                    rlVoiceExample.setEnabled(false);
                    break;
                case PLAYING:
                    btnAudio.setCardBackgroundColor(ColorHelper.getColor(R.color.app_red));
                    ((ImageView)btnAudio.getChildAt(0)).setImageResource(R.drawable.ic_close);
                    ((ImageView)btnAnalyzing.getChildAt(0)).setImageResource(R.drawable.ic_record);
                    btnAnalyzing.setCardBackgroundColor(ColorHelper.getColor(R.color.app_gray));
                    btnAnalyzing.setEnabled(false);
                    txtPhonemes.setTextColor(ColorHelper.getColor(R.color.app_gray));
                    txtWord.setTextColor(ColorHelper.getColor(R.color.app_gray));
                    txtDefinition.setTextColor(ColorHelper.getColor(R.color.app_gray));
                    txtPhonemes.setEnabled(false);
                    txtWord.setEnabled(false);
                    rlVoiceExample.setEnabled(false);
                    break;
                case GREEN:
                    btnAudio.setEnabled(true);
                    btnAnalyzing.setEnabled(true);
                    ((ImageView)btnAudio.getChildAt(0)).setImageResource(R.drawable.ic_play);
                    btnAudio.setCardBackgroundColor(ColorHelper.getColor(R.color.app_green));
                    ((ImageView)btnAnalyzing.getChildAt(0)).setImageResource(R.drawable.ic_record);
                    btnAnalyzing.setCardBackgroundColor(ColorHelper.getColor(R.color.app_green));
                    txtPhonemes.setTextColor(ColorHelper.getColor(R.color.app_green));
                    txtWord.setTextColor(ColorHelper.getColor(R.color.app_green));
                    txtDefinition.setTextColor(ColorHelper.getColor(R.color.app_green));
                    txtPhonemes.setEnabled(true);
                    txtWord.setEnabled(true);
                    rlVoiceExample.setEnabled(true);
                    isProcess = false;
                    break;
                case ORANGE:
                    btnAudio.setEnabled(true);
                    btnAnalyzing.setEnabled(true);
                    ((ImageView)btnAudio.getChildAt(0)).setImageResource(R.drawable.ic_play);
                    btnAudio.setCardBackgroundColor(ColorHelper.getColor(R.color.app_orange));
                    ((ImageView)btnAnalyzing.getChildAt(0)).setImageResource(R.drawable.ic_record);
                    btnAnalyzing.setCardBackgroundColor(ColorHelper.getColor(R.color.app_orange));
                    txtPhonemes.setTextColor(ColorHelper.getColor(R.color.app_orange));
                    txtWord.setTextColor(ColorHelper.getColor(R.color.app_orange));
                    txtDefinition.setTextColor(ColorHelper.getColor(R.color.app_orange));
                    txtPhonemes.setEnabled(true);
                    txtWord.setEnabled(true);
                    rlVoiceExample.setEnabled(true);
                    isProcess = false;
                    break;
                case RED:
                    btnAudio.setEnabled(true);
                    btnAnalyzing.setEnabled(true);
                    ((ImageView)btnAudio.getChildAt(0)).setImageResource(R.drawable.ic_play);
                    btnAudio.setCardBackgroundColor(ColorHelper.getColor(R.color.app_red));
                    ((ImageView)btnAnalyzing.getChildAt(0)).setImageResource(R.drawable.ic_record);
                    btnAnalyzing.setCardBackgroundColor(ColorHelper.getColor(R.color.app_red));
                    txtPhonemes.setTextColor(ColorHelper.getColor(R.color.app_red));
                    txtWord.setTextColor(ColorHelper.getColor(R.color.app_red));
                    txtDefinition.setTextColor(ColorHelper.getColor(R.color.app_red));
                    txtPhonemes.setEnabled(true);
                    txtWord.setEnabled(true);
                    rlVoiceExample.setEnabled(true);
                    isProcess = false;
                    break;
                case DISABLED:
                    btnAudio.setEnabled(false);
                    btnAnalyzing.setEnabled(false);
                    ((ImageView)btnAudio.getChildAt(0)).setImageResource(R.drawable.ic_play);
                    btnAudio.setCardBackgroundColor(ColorHelper.getColor(R.color.app_gray));
                    ((ImageView)btnAnalyzing.getChildAt(0)).setImageResource(R.drawable.ic_record);
                    btnAnalyzing.setCardBackgroundColor(ColorHelper.getColor(R.color.app_gray));
                    txtPhonemes.setTextColor(ColorHelper.getColor(R.color.app_gray));
                    txtWord.setTextColor(ColorHelper.getColor(R.color.app_gray));
                    txtDefinition.setTextColor(ColorHelper.getColor(R.color.app_gray));
                    txtPhonemes.setEnabled(false);
                    txtWord.setEnabled(false);
                    break;
                case DEFAULT:
                default:
                    btnAudio.setEnabled(true);
                    btnAnalyzing.setEnabled(true);
                    ((ImageView)btnAudio.getChildAt(0)).setImageResource(R.drawable.ic_play);
                    btnAudio.setCardBackgroundColor(ColorHelper.getColor(R.color.app_green));
                    ((ImageView)btnAnalyzing.getChildAt(0)).setImageResource(R.drawable.ic_record);
                    btnAnalyzing.setCardBackgroundColor(ColorHelper.getColor(R.color.app_green));
                    txtPhonemes.setTextColor(ColorHelper.getColor(R.color.app_green));
                    txtWord.setTextColor(ColorHelper.getColor(R.color.app_green));
                    txtDefinition.setTextColor(ColorHelper.getColor(R.color.app_green));
                    txtPhonemes.setEnabled(true);
                    txtWord.setEnabled(true);
                    rlVoiceExample.setEnabled(true);
                    isProcess = false;
                    break;
            }
            // Call other view update
            MainBroadcaster.getInstance().getSender().sendUpdateData(null, isProcess ? FragmentTab.TYPE_DISABLE_VIEW : FragmentTab.TYPE_ENABLE_VIEW);
            if (!checkAudioExist()) {
                btnAudio.setEnabled(false);
                btnAudio.setCardBackgroundColor(ColorHelper.getColor(R.color.app_gray));
                ((ImageView)btnAudio.getChildAt(0)).setImageResource(R.drawable.ic_play);
            }
        } catch (Exception e) {
            SimpleAppLog.error("Could not update screen state", e);
        }
    }

    private void fetchSetting() {
        try {
            String mChanel = Preferences.getString(Preferences.KEY_AUDIO_CHANEL, MainApplication.getContext(), "mono");
            if (mChanel.equalsIgnoreCase("mono")) {
                chanel = AudioFormat.CHANNEL_IN_MONO;
            } else {
                chanel = AudioFormat.CHANNEL_IN_STEREO;
            }
            sampleRate = Preferences.getInt(Preferences.KEY_AUDIO_SAMPLE_RATE, MainApplication.getContext(), 16000);
            isAutoStop = Preferences.getBoolean(Preferences.KEY_AUDIO_AUTO_STOP_RECORDING, MainApplication.getContext(), true);

            bufferSize = AudioRecord.getMinBufferSize(sampleRate, chanel, RECORDER_AUDIO_ENCODING);
        } catch (Exception e) {
            SimpleAppLog.error("Could not fetch setting", e);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        accountManager.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        accountManager.stop();
    }

    private void uploadRecord() {
        try {
            AppLog.logString("Start Uploading");
            analyzingState = AnalyzingState.ANALYZING;
            uploadTask = new UploaderAsync(getActivity(), getActivity().getResources().getString(R.string.upload_url));
            Map<String, String> params = new HashMap<String, String>();
            String fileName = audioStream.getFilename();
            File tmp = new File(fileName);
            if (tmp.exists()) {
                UserProfile profile = Preferences.getCurrentProfile(MainApplication.getContext());
                if (profile != null) {
                    Gson gson = new Gson();
                    profile.setUuid(new DeviceUuidFactory(MainApplication.getContext()).getDeviceUuid().toString());
                    UserProfile.UserLocation lc = new UserProfile.UserLocation();
                    Location location = AndroidHelper.getLastBestLocation(MainApplication.getContext());
                    if (location != null) {
                        lc.setLongitude(location.getLongitude());
                        lc.setLatitude(location.getLatitude());
                        AppLog.logString("Lat: " + lc.getLatitude() + ". Lon: " + lc.getLongitude());
                        profile.setLocation(lc);
                    }
                    profile.setTime(System.currentTimeMillis());
                    params.put(FileCommon.PARA_FILE_NAME, tmp.getName());
                    params.put(FileCommon.PARA_FILE_PATH, tmp.getAbsolutePath());
                    params.put(FileCommon.PARA_FILE_TYPE, "audio/wav");
                    params.put("country", Preferences.getCurrentProfile().getSelectedCountry().getId());
                    params.put("profile", gson.toJson(profile));
                    params.put("word", selectedWord);
                    uploadTask.execute(params);
                } else {
                    AppLog.logString("Could not get user profile");
                }
            }
        } catch (Exception e) {
            SimpleAppLog.error("Could not upload recording", e);
        }
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onAnimationMax() {
        try {
            if (analyzingState == AnalyzingState.WAIT_FOR_ANIMATION_MAX) {
                AppLog.logString("On animation max");
                recordingView.stopPingAnimation();

                if (viewState.currentModel != null) {
                    float score = viewState.currentModel.getScore();
                    if (score >= 80.0) {
                        lastState = ButtonState.GREEN;
                    } else if (score >= 45.0) {
                        lastState = ButtonState.ORANGE;
                    } else {
                        lastState = ButtonState.RED;
                    }
                    switchButtonStage();
                    if (isRecording) {
                        viewState.currentModel.setAudioFile(audioStream.getFilename());
                        // Call other view update
                        Gson gson = new Gson();
                        MainBroadcaster.getInstance().getSender().sendUpdateData(gson.toJson(viewState.currentModel), FragmentTab.TYPE_RELOAD_DATA);
//                        showcaseHelper.showHelp(ShowcaseHelper.HelpKey.SELECT_SCORE,
//                                new ShowcaseHelper.HelpState(btnAudio, "<b>Press</b> to <b>hear</b> your last attempt"),
//                                new ShowcaseHelper.HelpState(recordingView, "<b>Press</b> for more detail"));
                        isRecording = false;
                        handlerStartDetail.postDelayed(runnableStartDetail, 1000);
                        if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
                    }
                } else {
                    switchButtonStage(ButtonState.RED);
                    SweetAlertDialog d = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
                    d.setTitleText(getString(R.string.could_not_analyze_word_title));
                    d.setContentText(getString(R.string.could_not_analyze_word_message));
                    d.setConfirmText(getString(R.string.dialog_ok));
                    d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
                        }
                    });
                    d.show();
                }
                analyzingState = AnalyzingState.DEFAULT;
            }
        } catch (Exception e) {
            SimpleAppLog.error("Could not complete animation", e);
            isRecording = false;
        }
    }

    @Override
    public void onAnimationMin() {
        try {
            if (analyzingState == AnalyzingState.WAIT_FOR_ANIMATION_MIN) {
                if (recordingView == null) {
                    analyzingState = AnalyzingState.DEFAULT;
                    return;
                }
                AppLog.logString("On animation min");
                recordingView.setScore(0.0f);
                recordingView.stopPingAnimation();
                recordingView.recycle();
                recordingView.invalidate();
                if (viewState.currentModel != null) {
                    analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MAX;
                    recordingView.startPingAnimation(getActivity(), 2000, viewState.currentModel.getScore(), true, true);
                } else if (isRecording) {
                    if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
                    SweetAlertDialog d = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
                    d.setTitleText(getString(R.string.could_not_analyze_word_title));
                    if (viewState.errorMessage != null && viewState.errorMessage.equalsIgnoreCase("invalid token")) {
                        d.setContentText("invalid login token. please logout and login again!");
                        d.setConfirmText(getString(R.string.logout));
                        d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                MainActivity activity = (MainActivity) getActivity();
                                if (activity != null) {
                                    UserProfile profile = Preferences.getCurrentProfile();
                                    if (profile != null) {
                                        AnalyticHelper.sendUserLogout(activity, profile.getUsername());
                                    }
                                    accountManager.logout();
                                    activity.finish();
                                    Intent intent = new Intent(activity, LoginActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });
                    } else {
                        d.setContentText(getString(R.string.could_not_analyze_word_message));
                        d.setConfirmText(getString(R.string.dialog_ok));
                        d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });
                    }

                    d.show();
                    switchButtonStage(ButtonState.RED);
                    recordingView.drawEmptyCycle();
                    analyzingState = AnalyzingState.DEFAULT;
                    isRecording = false;

                } else {
                    YoYo.with(Techniques.FadeOut).duration(700).withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (imgHourGlass != null)
                                imgHourGlass.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).playOn(imgHourGlass);

                    recordingView.drawEmptyCycle();
                    // Null response
                    analyzingState = AnalyzingState.DEFAULT;
                    switchButtonStage();

                    if (viewState.dictionaryItem != null) {
                        txtWord.setText(viewState.dictionaryItem.getWord());
                        txtPhonemes.setText(viewState.dictionaryItem.getPronunciation());
                        txtDefinition.setText(viewState.dictionaryItem.getDefinition());
                        txtWord.setEnabled(true);
                        txtPhonemes.setEnabled(true);
                        rlVoiceExample.setEnabled(true);
                        AndroidHelper.updateMarqueeTextView(txtWord, !AndroidHelper.isCorrectWidth(txtWord, viewState.dictionaryItem.getWord()));
                        AndroidHelper.updateMarqueeTextView(txtPhonemes, !AndroidHelper.isCorrectWidth(txtPhonemes, viewState.dictionaryItem.getPronunciation()));
                        //txtPhonemes.setSelected(true);
                        //txtWord.setSelected(true);
                        showcaseHelper.showHelp(ShowcaseHelper.HelpKey.ANALYZING_WORD,
                                new ShowcaseHelper.HelpState(txtWord, "<b>Press</b> the word to hear it"),
                                new ShowcaseHelper.HelpState(btnAnalyzing, "<b>Press</b> to <b>test</b> your pronunciation"));
                        MainApplication.getContext().setSelectedWord(viewState.dictionaryItem.getWord());
                        MainBroadcaster.getInstance().getSender().sendUpdateData(viewState.dictionaryItem.getWord(), FragmentTab.TYPE_CHANGE_SELECTED_WORD);
                    } else {
                        txtWord.setText(getString(R.string.not_found));
                        txtPhonemes.setText(getString(R.string.please_try_again));
                        txtDefinition.setText("");
                        MainApplication.getContext().setSelectedWord(null);
                        MainBroadcaster.getInstance().getSender().sendUpdateData(null, FragmentTab.TYPE_CHANGE_SELECTED_WORD);
                    }
                    if (popupShowcaseHelper != null) popupShowcaseHelper.resetTiming();
                }
            }
        } catch (Exception e) {
            SimpleAppLog.error("Could not complete animation", e);
        }
    }


    private void saveToDatabase() throws Exception {
        if (audioStream == null) return;
        final String tmpFile = audioStream.getFilename();
        final File recordedFile = new File(tmpFile);
        if (recordedFile.exists() && viewState.currentModel != null) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        AnalyticHelper.sendAnalyzingWord(getActivity(), viewState.currentModel.getWord(), Math.round(viewState.currentModel.getScore()));
                        File pronScoreDir = FileHelper.getPronunciationScoreDir(MainApplication.getContext());
                        PronunciationScore score = new PronunciationScore();
                        // Get ID from server
                        String dataId = viewState.currentModel.getId();
                        score.setDataId(dataId);
                        score.setScore(viewState.currentModel.getScore());
                        score.setWord(viewState.currentModel.getWord());
                        score.setTimestamp(new Date(System.currentTimeMillis()));
                        //DENP-238
                        score.setUsername(viewState.currentModel.getUsername());
                        score.setVersion(viewState.currentModel.getVersion());
                        // Save recorded file
                        File savedFile = new File(pronScoreDir, dataId + FileHelper.WAV_EXTENSION);
                        FileUtils.copyFile(recordedFile, savedFile);
                        // Save json data
                        Gson gson = new Gson();
                        viewState.currentModel.setAudioFile(savedFile.getAbsolutePath());
                        FileUtils.writeStringToFile(new File(pronScoreDir, dataId + FileHelper.JSON_EXTENSION), gson.toJson(viewState.currentModel), "UTF-8");
                        scoreDBAdapter.open();
                        scoreDBAdapter.insert(score);
                        scoreDBAdapter.close();
                        if (viewState.currentModel.getResult() != null) {
                            PhonemeScoreDBAdapter phonemeScoreDBAdapter = new PhonemeScoreDBAdapter();
                            phonemeScoreDBAdapter.open();
                            List<SphinxResult.PhonemeScore> phonemeScoreList = viewState.currentModel.getResult().getPhonemeScores();
                            if (phonemeScoreList != null && phonemeScoreList.size() > 0) {
                                for (SphinxResult.PhonemeScore phonemeScore : phonemeScoreList) {
                                    phonemeScore.setTime(System.currentTimeMillis());
                                    phonemeScore.setTimestamp(new Date(System.currentTimeMillis()));
                                    phonemeScore.setUserVoiceId(dataId);
                                    phonemeScoreDBAdapter.insert(phonemeScore, viewState.currentModel.getUsername(), viewState.currentModel.getVersionPhoneme());
                                }
                            }
                            phonemeScoreDBAdapter.close();
                        }
                    } catch (Exception e) {
                        SimpleAppLog.error("Could not save model to database",e);
                    }
                    return null;
                }
            }.execute();

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Gson gson = new Gson();
        if (outState != null) {
            // Save last user voice model
            if (viewState != null) {
                outState.putString(MainBroadcaster.Filler.Key.VIEW_STATE.toString(), gson.toJson(viewState));
            }
        }
    }

    /**
     * Define all button state
     */
    enum ButtonState {
        DEFAULT,
        RECORDING,
        PLAYING,
        ORANGE,
        RED,
        GREEN,
        DISABLED
    }

    enum AnalyzingState {
        DEFAULT,
        RECORDING,
        ANALYZING,
        WAIT_FOR_ANIMATION_MIN,
        WAIT_FOR_ANIMATION_MAX
    }

    class ViewState {

        UserVoiceModel currentModel;

        DictionaryItem dictionaryItem;

        boolean willCollapseSlider = true;

        boolean willSearchRandomWord = true;

        boolean willShowHelpSearchWordAndSlider = false;

        String errorMessage;
    }
}
