package com.cmg.android.bbcaccent.fragment;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TableLayout;

import com.cmg.android.bbcaccent.MainActivity;
import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.auth.AccountManager;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.data.dto.PronunciationScore;
import com.cmg.android.bbcaccent.data.dto.SphinxResult;
import com.cmg.android.bbcaccent.data.dto.UserProfile;
import com.cmg.android.bbcaccent.data.dto.UserVoiceModel;
import com.cmg.android.bbcaccent.data.sqlite.PhonemeScoreDBAdapter;
import com.cmg.android.bbcaccent.data.sqlite.ScoreDBAdapter;
import com.cmg.android.bbcaccent.data.sqlite.WordDBAdapter;
import com.cmg.android.bbcaccent.dictionary.DictionaryItem;
import com.cmg.android.bbcaccent.dictionary.DictionaryListener;
import com.cmg.android.bbcaccent.dictionary.DictionaryWalker;
import com.cmg.android.bbcaccent.dictionary.OxfordDictionaryWalker;
import com.cmg.android.bbcaccent.dsp.AndroidAudioInputStream;
import com.cmg.android.bbcaccent.fragment.tab.FragmentTab;
import com.cmg.android.bbcaccent.fragment.tab.GraphFragment;
import com.cmg.android.bbcaccent.fragment.tab.HistoryFragment;
import com.cmg.android.bbcaccent.fragment.tab.Preferences;
import com.cmg.android.bbcaccent.fragment.tab.TipFragment;
import com.cmg.android.bbcaccent.helper.PlayerHelper;
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
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by luhonghai on 12/10/2015.
 */
public class FreeStyleFragment extends BaseFragment implements View.OnClickListener, RecordingView.OnAnimationListener, LocationListener {
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

    /**
     * Recording
     */
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private static final long PITCH_TIMEOUT = 1000;
    private static final long START_TIMEOUT = 1000;

    private static final long RECORD_MAX_LENGTH = 6000;

    private ButtonState lastState;

    private View root;

    private FragmentTabHost mTabHost;

    private RecordingView recordingView;

    private AnalyzingState analyzingState = AnalyzingState.DEFAULT;

    private ImageButton btnAnalyzing;
    private ImageButton btnAudio;

    private AlwaysMarqueeTextView txtWord;
    private AlwaysMarqueeTextView txtPhonemes;

    private RelativeLayout rlVoiceExample;

    private ImageButton imgHourGlass;
    private ImageButton imgHelpHand;


    private AndroidAudioInputStream audioStream;
    private AudioDispatcher dispatcher;
    private Thread runner;
    private double pitch = 0;
    private long lastDetectedPitchTime = -1;

    private AudioRecord audioInputStream;
    private int chanel;
    private int sampleRate;
    private int bufferSize;

    private boolean isPrepared = false;
    private boolean isRecording = false;
    private boolean isPlaying = false;
    private boolean isUploading = false;
    private boolean isAutoStop = false;

    private PlayerHelper player;

    private long start;

    private String selectedWord;

    private UserVoiceModel currentModel;

    private DictionaryItem dictionaryItem;


    /**
     * Animation
     */
    private Animation fadeIn;
    private Animation fadeOut;

    private UploaderAsync uploadTask;

    /**
     * Score
     */
    private ScoreDBAdapter scoreDBAdapter;

    private WordDBAdapter dbAdapter;

    private AccountManager accountManager;

    private int receiverListenerId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Gson gson = new Gson();
        root = inflater.inflate(R.layout.fragment_free_style, null);
        accountManager = new AccountManager(getActivity());
        initTabHost();
        initRecordingView();
        initAnimation();
        switchButtonStage(ButtonState.DISABLED);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(MainBroadcaster.Filler.USER_VOICE_MODEL.toString())) {
                currentModel = gson.fromJson(savedInstanceState.getString(MainBroadcaster.Filler.USER_VOICE_MODEL.toString()), UserVoiceModel.class);
            }
            if (savedInstanceState.containsKey(MainBroadcaster.Filler.Key.DICTIONARY_ITEM.toString())) {
                dictionaryItem = gson.fromJson(savedInstanceState.getString(MainBroadcaster.Filler.Key.DICTIONARY_ITEM.toString()), DictionaryItem.class);
            }
        }
        scoreDBAdapter = new ScoreDBAdapter();
        dbAdapter = new WordDBAdapter();
        receiverListenerId = MainBroadcaster.getInstance().register(new MainBroadcaster.ReceiverListener() {

            @Override
            public void onUserModelFetched(UserVoiceModel model) {
                currentModel = model;
                try {
                    saveToDatabase();
                } catch (Exception e) {
                    SimpleAppLog.error("Could not save data to database", e);
                    currentModel = null;
                }
                AppLog.logString("Start score animation");
                analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MIN;
                willMoveToDetail = true;
            }
            @Override
            public void onHistoryAction(final UserVoiceModel model, String word, int type) {
                switch (type) {
                    case HistoryFragment.CLICK_LIST_ITEM:
                        if (word == null || word.length() == 0) {
                            Bundle bundle = new Bundle();
                            bundle.putString(MainBroadcaster.Filler.USER_VOICE_MODEL.toString(), gson.toJson(model));
                            MainBroadcaster.getInstance().getSender().sendSwitchFragment(
                                    DetailFragment.class,
                                    new MainActivity.SwitchFragmentParameter(true, true, true),
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
        if (currentModel == null) {
            String[] words = getResources().getStringArray(R.array.random_words);
            if (words != null && words.length > 0) {
                getWord(words[RandomHelper.getRandomIndex(words.length)].trim());
            } else {
                getWord(getString(R.string.example_word));
            }
        } else {
            if (dictionaryItem != null) {
                txtPhonemes.setText(dictionaryItem.getPronunciation());
                txtWord.setText(dictionaryItem.getWord());
            }
        }
        return root;

    }

    private void initAnimation() {
        fadeIn = AnimationUtils.loadAnimation(MainApplication.getContext(), android.R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(MainApplication.getContext(), android.R.anim.fade_out);
    }

    private void initRecordingView() {
        if (root == null) return;
        rlVoiceExample = (RelativeLayout) root.findViewById(R.id.rlVoiceExample);
        imgHelpHand = (ImageButton) root.findViewById(R.id.imgHelpHand);
        imgHourGlass = (ImageButton) root.findViewById(R.id.imgHourGlass);
        recordingView = (RecordingView) root.findViewById(R.id.main_recording_view);
        btnAnalyzing = (ImageButton) root.findViewById(R.id.btnAnalyzing);
        btnAnalyzing.setOnClickListener(this);
        btnAudio = (ImageButton) root.findViewById(R.id.btnAudio);
        btnAudio.setOnClickListener(this);
        txtPhonemes = (AlwaysMarqueeTextView) root.findViewById(R.id.txtPhoneme);
        txtPhonemes.setText("");
        txtWord = (AlwaysMarqueeTextView) root.findViewById(R.id.txtWord);
        txtWord.setText("");
        txtPhonemes.setOnClickListener(this);
        txtWord.setOnClickListener(this);
        rlVoiceExample.setOnClickListener(this);
        recordingView.setOnClickListener(this);
        recordingView.setAnimationListener(this);
    }

    private void initTabHost() {
        mTabHost = (FragmentTabHost) root.findViewById(android.R.id.tabhost);
        if (mTabHost == null) return;
        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);
        addTabImage(R.drawable.tab_graph,
                GraphFragment.class, getString(R.string.tab_graph));
        addTabImage(R.drawable.tab_history,
                HistoryFragment.class, getString(R.string.tab_history));
        addTabImage(R.drawable.tab_tip,
                TipFragment.class, getString(R.string.tab_tip));
    }

    private void addTabImage(int drawableId, Class<?> c, String labelId) {
        TabHost.TabSpec spec = mTabHost.newTabSpec(labelId).setIndicator(null, getResources().getDrawable(drawableId));
        mTabHost.addTab(spec, c, null);

    }

    private void addTab(int drawableId, Class<?> c, String labelId) {
        TabHost.TabSpec spec = mTabHost.newTabSpec(labelId);
        View tabIndicator = LayoutInflater.from(MainApplication.getContext()).inflate(R.layout.tab_indicator, (TableLayout) root.findViewById(R.id.content), false);
        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
        icon.setImageResource(drawableId);
        spec.setIndicator(tabIndicator);
        mTabHost.addTab(spec, c, null);

    }

    private void completeGetWord(DictionaryItem item, ButtonState state) {
        if (item != null) {
            dictionaryItem = item;
        } else {
            dictionaryItem = null;
        }
        analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MIN;
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
            DictionaryWalker walker = new OxfordDictionaryWalker(FileHelper.getAudioDir(MainApplication.getContext()));
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
        if (isRecording) return;
        try {
            if (dbAdapter == null) dbAdapter = new WordDBAdapter();
            if (!dbAdapter.isBeep(word)) {
                AnalyticHelper.sendSelectWordNotInBeep(MainApplication.getContext(), word);
                SweetAlertDialog d = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
                d.setTitleText(getString(R.string.word_not_in_beep_title));
                d.setContentText(getString(R.string.word_not_in_beep_message));
                d.setConfirmText(getString(R.string.dialog_ok));
                d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
                d.show();
                return;
            }
        } catch (Exception e) {
            SimpleAppLog.error("Could not check word " + word + " is beep or not", e);
        }
        try {
            if (((MainActivity)getActivity()).checkNetwork(false)) {
                currentModel = null;
                recordingView.setScore(0.0f);
                recordingView.stopPingAnimation();
                recordingView.recycle();
                recordingView.invalidate();
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
                dictionaryItem = null;
                currentModel = null;
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
        switchButtonStage();
        isPlaying = false;
        player.stop();
    }

    private void play(String file) {
        if (file == null || file.length() == 0) return;
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
            // Clear old data
            currentModel = null;
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
            ex.printStackTrace();
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
        stop();
        try {
            dbAdapter.close();
        } catch (Exception e) {

        }
        try {
            recordingView.recycle();
        } catch (Exception ex) {

        }
        MainBroadcaster.getInstance().unregister(receiverListenerId);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTabHost != null) {
            mTabHost.getTabWidget().setEnabled(false);
        }
        stopRequestLocation();
        if (currentModel != null) {
            recordingView.stopPingAnimation();
            recordingView.recycle();
            recordingView.invalidate();
            // Null response
            analyzingState = AnalyzingState.DEFAULT;
        }
    }

    protected void stopRequestLocation() {
//        SimpleAppLog.info("Request location update");
//        LocationManager lm = (LocationManager) MainApplication.getContext().getSystemService(Context.LOCATION_SERVICE);
//        try {
//            lm.removeUpdates(this);
//        } catch (Exception e) {
//            SimpleAppLog.error("Could not stop request location", e);
//        }
    }

    protected void requestLocation() {
//        SimpleAppLog.info("Request location update");
//        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        try {
//            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5 * 60 * 1000, 1000, this);
//        } catch (Exception e) {
//            SimpleAppLog.error("Could not request GPS provider location", e);
//        }
//        try {
//            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5 * 60 * 1000, 1000, this);
//        } catch (Exception e) {
//            SimpleAppLog.error("Could not request Network provider location", e);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mTabHost != null) {
            mTabHost.getTabWidget().setEnabled(true);
        }
        //requestLocation();
        fetchSetting();
        isPrepared = false;
        if (currentModel != null) {
            analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MAX;
            recordingView.startPingAnimation(getActivity(), 1000, currentModel.getScore(), true, true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAnalyzing:
                if (((MainActivity) getActivity()).checkNetwork(false)) {
                    if (isRecording) {
                        stop();
                        switchButtonStage(ButtonState.DISABLED);
                        if (currentModel != null) {
                            analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MAX;
                            recordingView.startPingAnimation(getActivity(), 2000, currentModel.getScore(), true, true);
                        } else {
                            analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MIN;
                            recordingView.startPingAnimation(getActivity(), 1000, 100.0f, false, false);
                        }
                    } else {
                        analyze();
                    }
                }
                break;
            case R.id.btnAudio:
                if (isPlaying) {
                    stopPlay();
                } else {
                    play();
                }
                break;
            //case R.id.txtPhoneme:
            //case R.id.txtWord:
            case R.id.rlVoiceExample:
                if (dictionaryItem != null) {
                    play(dictionaryItem.getAudioFile());
                }
                break;
            case R.id.main_recording_view:
                handlerStartDetail.post(runnableStartDetail);
        }
    }

    private Handler handlerStartDetail = new Handler();

    private Runnable runnableStartDetail = new Runnable() {
        @Override
        public void run() {
            willMoveToDetail = false;
            if (currentModel != null && dictionaryItem != null) {
                Gson gson = new Gson();
                Bundle bundle = new Bundle();
                bundle.putString(MainBroadcaster.Filler.USER_VOICE_MODEL.toString(), gson.toJson(currentModel));
                MainBroadcaster.getInstance().getSender().sendSwitchFragment(
                        DetailFragment.class,
                        new MainActivity.SwitchFragmentParameter(true, true, true),
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
                    btnAudio.setImageResource(R.drawable.p_audio_gray);
                    btnAudio.setEnabled(false);
                    btnAnalyzing.startAnimation(fadeOut);
                    btnAnalyzing.setImageResource(R.drawable.p_close_red);
                    btnAnalyzing.startAnimation(fadeIn);
                    txtPhonemes.setTextColor(ColorHelper.COLOR_GRAY);

                    txtWord.setTextColor(ColorHelper.COLOR_GRAY);
                    txtPhonemes.setEnabled(false);
                    txtWord.setEnabled(false);
                    rlVoiceExample.setEnabled(false);
                    break;
                case PLAYING:
                    btnAudio.startAnimation(fadeOut);
                    btnAudio.setImageResource(R.drawable.p_close_red);
                    btnAudio.startAnimation(fadeIn);
                    btnAnalyzing.setImageResource(R.drawable.p_record_gray);
                    btnAnalyzing.setEnabled(false);
                    txtPhonemes.setTextColor(ColorHelper.COLOR_GRAY);
                    txtWord.setTextColor(ColorHelper.COLOR_GRAY);
                    txtPhonemes.setEnabled(false);
                    txtWord.setEnabled(false);
                    rlVoiceExample.setEnabled(false);
                    break;
                case GREEN:
                    btnAudio.setEnabled(true);
                    btnAnalyzing.setEnabled(true);
                    btnAudio.setImageResource(R.drawable.p_audio_green);
                    btnAnalyzing.setImageResource(R.drawable.p_record_green);
                    txtPhonemes.setTextColor(ColorHelper.COLOR_GREEN);
                    txtWord.setTextColor(ColorHelper.COLOR_GREEN);
                    txtPhonemes.setEnabled(true);
                    txtWord.setEnabled(true);
                    rlVoiceExample.setEnabled(true);
                    isProcess = false;
                    break;
                case ORANGE:
                    btnAudio.setEnabled(true);
                    btnAnalyzing.setEnabled(true);
                    btnAudio.setImageResource(R.drawable.p_audio_orange);
                    btnAnalyzing.setImageResource(R.drawable.p_record_orange);
                    txtPhonemes.setTextColor(ColorHelper.COLOR_ORANGE);
                    txtWord.setTextColor(ColorHelper.COLOR_ORANGE);
                    txtPhonemes.setEnabled(true);
                    txtWord.setEnabled(true);
                    rlVoiceExample.setEnabled(true);
                    isProcess = false;
                    break;
                case RED:
                    btnAudio.setEnabled(true);
                    btnAnalyzing.setEnabled(true);
                    btnAudio.setImageResource(R.drawable.p_audio_red);
                    btnAnalyzing.setImageResource(R.drawable.p_record_red);
                    txtPhonemes.setTextColor(ColorHelper.COLOR_RED);
                    txtWord.setTextColor(ColorHelper.COLOR_RED);
                    txtPhonemes.setEnabled(true);
                    txtWord.setEnabled(true);
                    rlVoiceExample.setEnabled(true);
                    isProcess = false;
                    break;
                case DISABLED:
                    btnAudio.setEnabled(false);
                    btnAnalyzing.setEnabled(false);
                    btnAudio.setImageResource(R.drawable.p_audio_gray);
                    btnAnalyzing.setImageResource(R.drawable.p_record_gray);
                    txtPhonemes.setTextColor(ColorHelper.COLOR_GRAY);
                    txtWord.setTextColor(ColorHelper.COLOR_GRAY);
                    txtPhonemes.setEnabled(false);
                    txtWord.setEnabled(false);
                    break;
                case DEFAULT:
                default:
                    btnAudio.setEnabled(true);
                    btnAnalyzing.setEnabled(true);
                    btnAudio.setImageResource(R.drawable.p_audio_green);
                    btnAnalyzing.setImageResource(R.drawable.p_record_green);
                    txtPhonemes.setTextColor(ColorHelper.COLOR_GREEN);
                    txtWord.setTextColor(ColorHelper.COLOR_GREEN);
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
                btnAudio.setImageResource(R.drawable.p_audio_gray);
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

            isPrepared = false;
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

    private boolean willMoveToDetail = false;

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
                isRecording = false;
                if (currentModel != null) {
                    currentModel.setAudioFile(audioStream.getFilename());
                    float score = currentModel.getScore();
                    if (score >= 80.0) {
                        lastState = ButtonState.GREEN;
                    } else if (score >= 45.0) {
                        lastState = ButtonState.ORANGE;
                    } else {
                        lastState = ButtonState.RED;
                    }
                    // Call other view update
                    Gson gson = new Gson();
                    MainBroadcaster.getInstance().getSender().sendUpdateData(gson.toJson(currentModel), FragmentTab.TYPE_RELOAD_DATA);
                    switchButtonStage();

                    YoYo.with(Techniques.FadeIn).duration(500).withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            imgHelpHand.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (willMoveToDetail) {
                                handlerStartDetail.postDelayed(runnableStartDetail, 2000);
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).playOn(imgHelpHand);

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
                        }
                    });
                    d.show();
                }
                analyzingState = AnalyzingState.DEFAULT;
            }
        } catch (Exception e) {
            SimpleAppLog.error("Could not complete animation", e);
        }
    }

    @Override
    public void onAnimationMin() {
        try {
            if (analyzingState == AnalyzingState.WAIT_FOR_ANIMATION_MIN) {
                AppLog.logString("On animation min");
                recordingView.setScore(0.0f);
                recordingView.stopPingAnimation();
                recordingView.recycle();
                recordingView.invalidate();
                if (currentModel != null) {
                    analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MAX;
                    recordingView.startPingAnimation(getActivity(), 2000, currentModel.getScore(), true, true);
                } else if (isRecording) {
                    SweetAlertDialog d = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
                    d.setTitleText(getString(R.string.could_not_analyze_word_title));
                    d.setContentText(getString(R.string.could_not_analyze_word_message));
                    d.setConfirmText(getString(R.string.dialog_ok));
                    d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });
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

                    if (dictionaryItem != null) {
                        txtWord.setText(dictionaryItem.getWord());
                        txtPhonemes.setText(dictionaryItem.getPronunciation());
                        txtWord.setEnabled(true);
                        txtPhonemes.setEnabled(true);
                        rlVoiceExample.setEnabled(true);
                        AndroidHelper.updateMarqueeTextView(txtWord, !AndroidHelper.isCorrectWidth(txtWord, dictionaryItem.getWord()));
                        AndroidHelper.updateMarqueeTextView(txtPhonemes, !AndroidHelper.isCorrectWidth(txtWord, dictionaryItem.getPronunciation()));
                        //txtPhonemes.setSelected(true);
                        //txtWord.setSelected(true);

                    } else {
                        txtWord.setText(getString(R.string.not_found));
                        txtPhonemes.setText(getString(R.string.please_try_again));
                    }
                }
            }
        } catch (Exception e) {
            SimpleAppLog.error("Could not complete animation", e);
        }
    }


    private void saveToDatabase() throws Exception {
        if (audioStream == null) return;
        String tmpFile = audioStream.getFilename();
        File recordedFile = new File(tmpFile);
        if (recordedFile.exists() && currentModel != null) {
            AnalyticHelper.sendAnalyzingWord(getActivity(), currentModel.getWord(), Math.round(currentModel.getScore()));
            File pronScoreDir = FileHelper.getPronunciationScoreDir(MainApplication.getContext());
            PronunciationScore score = new PronunciationScore();
            // Get ID from server
            String dataId = currentModel.getId();
            score.setDataId(dataId);
            score.setScore(currentModel.getScore());
            score.setWord(currentModel.getWord());
            score.setTimestamp(new Date(System.currentTimeMillis()));
            //DENP-238
            score.setUsername(currentModel.getUsername());
            score.setVersion(currentModel.getVersion());
            // Save recorded file
            File savedFile = new File(pronScoreDir, dataId + FileHelper.WAV_EXTENSION);
            FileUtils.copyFile(recordedFile, savedFile);
            // Save json data
            Gson gson = new Gson();
            currentModel.setAudioFile(savedFile.getAbsolutePath());
            FileUtils.writeStringToFile(new File(pronScoreDir, dataId + FileHelper.JSON_EXTENSION), gson.toJson(currentModel), "UTF-8");
            scoreDBAdapter.open();
            scoreDBAdapter.insert(score);
            scoreDBAdapter.close();
            if (currentModel.getResult() != null) {
                PhonemeScoreDBAdapter phonemeScoreDBAdapter = new PhonemeScoreDBAdapter();
                phonemeScoreDBAdapter.open();
                List<SphinxResult.PhonemeScore> phonemeScoreList = currentModel.getResult().getPhonemeScores();
                if (phonemeScoreList != null && phonemeScoreList.size() > 0) {
                    for (SphinxResult.PhonemeScore phonemeScore : phonemeScoreList) {
                        phonemeScore.setTime(System.currentTimeMillis());
                        phonemeScore.setTimestamp(new Date(System.currentTimeMillis()));
                        phonemeScore.setUserVoiceId(dataId);
                        phonemeScoreDBAdapter.insert(phonemeScore, currentModel.getUsername(),currentModel.getVersionPhoneme());
                    }
                }
                phonemeScoreDBAdapter.close();
            }

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Gson gson = new Gson();
        if (outState != null) {
            // Save last user voice model
            if (currentModel != null) {
                outState.putString(MainBroadcaster.Filler.USER_VOICE_MODEL.toString(), gson.toJson(currentModel));
            }
            if (dictionaryItem != null) {
                outState.putString(MainBroadcaster.Filler.Key.DICTIONARY_ITEM.toString(), gson.toJson(dictionaryItem));
            }
        }
    }

    private void showHelpDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.Theme_WhiteDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.help_dialog);

        dialog.findViewById(R.id.btnNever).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfile userProfile = Preferences.getCurrentProfile(MainApplication.getContext());
                if (userProfile != null) {
                    Preferences.setHelpStatusProfile(MainApplication.getContext(), userProfile.getUsername(), UserProfile.HELP_NEVER);
                }
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.btnSkip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(HelpActivity.class);
                dialog.dismiss();
            }
        });

        dialog.setTitle(null);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
    }
}
