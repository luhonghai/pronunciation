package com.cmg.android.voicerecorder;

import android.app.Activity;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.cmg.android.voicerecorder.activity.BaseActivity;
import com.cmg.android.voicerecorder.activity.DetailActivity;
import com.cmg.android.voicerecorder.activity.SettingsActivity;
import com.cmg.android.voicerecorder.activity.fragment.FragmentTab;
import com.cmg.android.voicerecorder.activity.fragment.Preferences;
import com.cmg.android.voicerecorder.activity.view.RecordingView;
import com.cmg.android.voicerecorder.data.ScoreDBAdapter;
import com.cmg.android.voicerecorder.data.WordDBAdapter;
import com.cmg.android.voicerecorder.data.UserProfile;
import com.cmg.android.voicerecorder.data.UserVoiceModel;
import com.cmg.android.voicerecorder.dictionary.DictionaryItem;
import com.cmg.android.voicerecorder.dictionary.DictionaryListener;
import com.cmg.android.voicerecorder.dictionary.DictionaryWalker;
import com.cmg.android.voicerecorder.dictionary.OxfordDictionaryWalker;
import com.cmg.android.voicerecorder.dsp.AndroidAudioInputStream;
import com.cmg.android.voicerecorder.http.FileCommon;
import com.cmg.android.voicerecorder.http.UploaderAsync;
import com.cmg.android.voicerecorder.utils.ColorHelper;
import com.cmg.android.voicerecorder.utils.DeviceUuidFactory;
import com.cmg.android.voicerecorder.utils.FileHelper;
import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.Oscilloscope;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class MainActivity extends BaseActivity implements SearchView.OnQueryTextListener,
                                                            View.OnClickListener,
                                                            Animation.AnimationListener,
                                                            LocationListener,
                                                            SearchView.OnSuggestionListener,
                                                            RecordingView.OnAnimationListener {



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

    private DrawerLayout drawerLayout;
    private boolean isDrawerOpened;
    private MaterialMenuView materialMenu;

    private FragmentTabHost mTabHost;


    /**
     * Recording
     */
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private static final long PITCH_TIMEOUT = 1000;
    private static final long START_TIMEOUT = 1000;

    private static final long RECORD_MAX_LENGTH = 6000;

    private ButtonState lastState;

    private RecordingView recordingView;

    private AnalyzingState analyzingState = AnalyzingState.DEFAULT;

    private ImageButton btnAnalyzing;
    private ImageButton btnAudio;

    private TextView txtWord;
    private TextView txtPhonemes;

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
    Animation fadeIn;
    Animation fadeOut;

    private UploaderAsync uploadTask;

    /**
     *  Location
     */
    private LocationManager locationManager;
    private String provider;
    private Location location;

    /**
     *  Search word
     */
    private WordDBAdapter dbAdapter;
    private CursorAdapter adapter;
    private SearchView searchView;

    /**
     * Score
     */
    private ScoreDBAdapter scoreDBAdapter;


    /**
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        initCustomActionBar();
        initTabHost();
        initRecordingView();
        initAnimation();
        switchButtonStage(ButtonState.DISABLED);
        initLocation();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                materialMenu.setTransformationOffset(
                        MaterialMenuDrawable.AnimationState.BURGER_ARROW,
                        isDrawerOpened ? 2 - slideOffset : slideOffset
                );
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                isDrawerOpened = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isDrawerOpened = false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_IDLE) {
                    if (isDrawerOpened) materialMenu.setState(MaterialMenuDrawable.IconState.ARROW);
                    else materialMenu.setState(MaterialMenuDrawable.IconState.BURGER);
                }
            }
        });

        registerReceiver(mHandleMessageReader, new IntentFilter(UploaderAsync.UPLOAD_COMPLETE_INTENT));
        getWord("necessarily");
        scoreDBAdapter = new ScoreDBAdapter(this);
    }

    private void openSettings() {
        Intent settings = new Intent();
        settings.setClass(this, SettingsActivity.class);
        this.startActivity(settings);
    }


    private void initLocation() {
        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            onLocationChanged(location);
        } else {

        }
    }

    private void initAnimation() {
        fadeIn = AnimationUtils.loadAnimation(this.getApplicationContext(), android.R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(this.getApplicationContext(), android.R.anim.fade_out);
    }

    private void initRecordingView() {
        recordingView = (RecordingView) findViewById(R.id.main_recording_view);
        btnAnalyzing = (ImageButton) findViewById(R.id.btnAnalyzing);
        btnAnalyzing.setOnClickListener(this);
        btnAudio = (ImageButton) findViewById(R.id.btnAudio);
        btnAudio.setOnClickListener(this);
        txtPhonemes = (TextView) findViewById(R.id.txtPhoneme);
        txtPhonemes.setText("");
        txtWord = (TextView) findViewById(R.id.txtWord);
        txtWord.setText("");
        txtPhonemes.setOnClickListener(this);
        txtWord.setOnClickListener(this);
        recordingView.setOnClickListener(this);
        recordingView.setAnimationListener(this);
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
        materialMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDrawerOpened) {
                    materialMenu.setState(MaterialMenuDrawable.IconState.ARROW);
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    materialMenu.setState(MaterialMenuDrawable.IconState.BURGER);
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        isDrawerOpened = drawerLayout.isDrawerOpen(Gravity.START); // or END, LEFT, RIGHT
        //materialMenu.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //materialMenu.onSaveInstanceState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        if (null != searchView) {
            dbAdapter = new WordDBAdapter(this);
            try {
                dbAdapter.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            searchView.setQueryHint("Search word");
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
          //  searchView.setIconifiedByDefault(false);
            searchView.setOnQueryTextListener(this);
            searchView.setOnSuggestionListener(this);
            adapter = new SimpleCursorAdapter(this, R.layout.search_word_item,
                    dbAdapter.getAll(),
                    new String[] {WordDBAdapter.KEY_WORD, WordDBAdapter.KEY_PRONUNCIATION},
                    new int[] {R.id.txtWord, R.id.txtPhoneme},
                    CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            searchView.setSuggestionsAdapter(adapter);

        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                if (isDrawerOpened) {
                    materialMenu.setState(MaterialMenuDrawable.IconState.ARROW);
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    materialMenu.setState(MaterialMenuDrawable.IconState.BURGER);
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.menu_setting:
                openSettings();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void completeGetWord(DictionaryItem item, ButtonState state) {
        if (item != null) {
            dictionaryItem = item;
        } else {
            dictionaryItem = null;
        }
        analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MIN;
        lastState = state;
        if (audioStream!=null)
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
            DictionaryWalker walker = new OxfordDictionaryWalker(FileHelper.getAudioDir(MainActivity.this.getApplicationContext()));
            walker.setListener(new DictionaryListener() {
                @Override
                public void onDetectWord(final DictionaryItem dItem) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            completeGetWord(dItem, ButtonState.DEFAULT);
                        }
                    });
                }

                @Override
                public void onWordNotFound(DictionaryItem dItem, final FileNotFoundException ex) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ex.printStackTrace();
                            completeGetWord(null, ButtonState.DISABLED);
                        }
                    });
                }

                @Override
                public void onError(DictionaryItem dItem, final Exception ex) {
                    runOnUiThread(new Runnable() {
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
        switchButtonStage(ButtonState.DISABLED);
        recordingView.startPingAnimation(this);
        txtWord.setText("Searching");
        txtPhonemes.setText("Please wait ...");
        dictionaryItem = null;
        currentModel = null;
        if (getWordAsync != null) {
            try {
                while (!getWordAsync.isCancelled() && getWordAsync.cancel(true));
            } catch (Exception ex) {

            }
        }
        getWordAsync = new GetWordAsync(word);
        getWordAsync.execute();
    }

    @Override
    public boolean onQueryTextSubmit(final String s) {
        if (s.length() > 0) {
            hideSoftKeyboard();
            getWord(s);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (s.length() > 0) {
            Cursor c = dbAdapter.search(s);
            if (c.getCount() > 0) {
                searchView.getSuggestionsAdapter().changeCursor(c);
                searchView.getSuggestionsAdapter().notifyDataSetChanged();
                return true;
            }
        }
        return false;
    }

    private void stopPlay() {
        switchButtonStage();
        isPlaying = false;
        player.stop();
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
                    AppLog.logString("Stop sound");
                    runOnUiThread(new Runnable() {
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
                while(!uploadTask.isCancelled() && uploadTask.cancel(true));
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

            audioStream = new AndroidAudioInputStream(this.getApplicationContext(), audioInputStream, format, bufferSize);
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recordingView.startPingAnimation(MainActivity.this);
                            }
                        });
                    }

                }
            }));
            dispatcher.addAudioProcessor(new Oscilloscope(new Oscilloscope.OscilloscopeEventHandler() {
                @Override
                public void handleEvent(final float[] floats, final AudioEvent audioEvent) {
                    runOnUiThread(new Runnable() {
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
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(mHandleMessageReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        stop();
        try {
            dbAdapter.close();
        } catch (Exception e) {

        }
        try {
            recordingView.recycle();
        } catch (Exception ex) {

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
        if (currentModel != null) {
            recordingView.stopPingAnimation();
            recordingView.recycle();
            recordingView.invalidate();
            // Null response
            analyzingState = AnalyzingState.DEFAULT;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
        fetchSetting();
        isPrepared = false;
        if (currentModel != null) {
            analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MAX;
            recordingView.startPingAnimation(this, 2000, currentModel.getScore(), true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAnalyzing:
                if (isRecording) {
                    stop();
                    switchButtonStage(ButtonState.DISABLED);
                    if (currentModel != null) {
                        analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MAX;
                        recordingView.startPingAnimation(this, 2000, currentModel.getScore(), true);
                    } else {
                        analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MIN;
                        recordingView.startPingAnimation(this, 1000, 100.0f, false);
                    }
                } else {
                    analyze();
                }
                break;
            case R.id.btnAudio:
                if (isPlaying) {
                    stopPlay();
                } else {
                    play();
                }
                break;
            case R.id.txtPhoneme:
            case R.id.txtWord:
                if (dictionaryItem != null) {
                    play(dictionaryItem.getAudioFile());
                }
                break;
            case R.id.main_recording_view:
                if (currentModel != null && dictionaryItem != null) {
                    Gson gson = new Gson();
                    Intent intent = new Intent(this, DetailActivity.class);
                    intent.putExtra(DetailActivity.DICTIONARY_ITEM, gson.toJson(dictionaryItem));
                    intent.putExtra(DetailActivity.USER_VOICE_MODEL, gson.toJson(currentModel));
                    startActivity(intent);
                }
        }
    }

    private void switchButtonStage() {
        if (lastState == null) lastState = ButtonState.DEFAULT;
        switchButtonStage(lastState);
    }

    private void switchButtonStage(ButtonState state) {
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
                break;
        }
        if (!checkAudioExist()) {
            btnAudio.setEnabled(false);
            btnAudio.setImageResource(R.drawable.p_audio_gray);
        }
    }

    private void fetchSetting() {
        String username = Preferences.getString(Preferences.KEY_USERNAME_LIST, this.getApplicationContext());
        if (username.length() == 0) {
            openSettings();
        }
        String mChanel = Preferences.getString(Preferences.KEY_AUDIO_CHANEL, this.getApplicationContext());
        if (mChanel.equalsIgnoreCase("mono")) {
            chanel = AudioFormat.CHANNEL_IN_MONO;
        } else {
            chanel = AudioFormat.CHANNEL_IN_STEREO;
        }
        sampleRate = Preferences.getInt(Preferences.KEY_AUDIO_SAMPLE_RATE, this.getApplicationContext());
        isAutoStop = Preferences.getBoolean(Preferences.KEY_AUDIO_AUTO_STOP_RECORDING, this.getApplicationContext());
        bufferSize = AudioRecord.getMinBufferSize(sampleRate,chanel,RECORDER_AUDIO_ENCODING);

        isPrepared = false;
    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private final BroadcastReceiver mHandleMessageReader = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle.containsKey(UploaderAsync.UPLOAD_COMPLETE_INTENT)) {
                String data = bundle.getString(UploaderAsync.UPLOAD_COMPLETE_INTENT);
                Gson gson = new Gson();
                try {
                    currentModel = gson.fromJson(data, UserVoiceModel.class);
                } catch (Exception ex) {
                    //switchButtonStage(ButtonState.RED);
                }
                try {
                    saveToDatabase();
                } catch (IOException e) {
                    e.printStackTrace();
                    currentModel = null;
                } catch (SQLException e) {
                    e.printStackTrace();
                    currentModel = null;
                }
                AppLog.logString("Start score animation");
                // Waiting for animation complete
                analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MIN;
            }
        }
    };

    private void uploadRecord() {
        AppLog.logString("Start Uploading");
        analyzingState = AnalyzingState.ANALYZING;
        uploadTask = new UploaderAsync(this, getResources().getString(R.string.upload_url));
        Map<String, String> params = new HashMap<String, String>();
        String fileName = audioStream.getFilename();
        File tmp = new File(fileName);
        if (tmp.exists()) {
            UserProfile profile = Preferences.getCurrentProfile(this);
            if (profile != null) {
                Gson gson = new Gson();
                profile.setUuid(new DeviceUuidFactory(this).getDeviceUuid().toString());
                UserProfile.UserLocation lc = new UserProfile.UserLocation();
                if (locationManager.isProviderEnabled(provider) && location != null) {
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
    }


    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
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

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
                .getWindowToken(), 0);
    }

    @Override
    public boolean onSuggestionSelect(int i) {
        selectSuggestionWord(i);
        return true;
    }

    private void selectSuggestionWord(int index) {
        AppLog.logString("Select suggestion: " + index);
        Cursor cursor = (Cursor) adapter.getItem(index);
        String s = cursor.getString(cursor.getColumnIndex(WordDBAdapter.KEY_WORD));
        searchView.setQuery(s, true);
    }

    @Override
    public boolean onSuggestionClick(int i) {
        selectSuggestionWord(i);
        return true;
    }

    @Override
    public void onAnimationMax() {
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
                switchButtonStage();

            } else {
                switchButtonStage(ButtonState.RED);
            }
            analyzingState = AnalyzingState.DEFAULT;
        }
    }

    @Override
    public void onAnimationMin() {
        if (analyzingState == AnalyzingState.WAIT_FOR_ANIMATION_MIN) {
            AppLog.logString("On animation min");
            recordingView.setScore(0.0f);
            recordingView.stopPingAnimation();
            recordingView.recycle();
            recordingView.invalidate();
            if (currentModel != null) {
                analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MAX;
                recordingView.startPingAnimation(this, 3000, currentModel.getScore(), true);
            } else {
                if (dictionaryItem != null) {
                    txtWord.setText(dictionaryItem.getWord());
                    txtPhonemes.setText(dictionaryItem.getPronunciation());
                } else {
                    txtWord.setText("Not found");
                    txtPhonemes.setText("Please try again!");
                }
                recordingView.drawEmptyCycle();
                // Null response
                analyzingState = AnalyzingState.DEFAULT;
                switchButtonStage();
            }
        }
    }

    private void saveToDatabase() throws IOException, SQLException {
        String tmpFile = audioStream.getFilename();
        File recordedFile = new File(tmpFile);
        if (recordedFile.exists() && currentModel != null) {
            File pronScoreDir = FileHelper.getPronunciationScoreDir(this.getApplicationContext());
            ScoreDBAdapter.PronunciationScore score = new ScoreDBAdapter.PronunciationScore();
            // Get ID from server
            String dataId = currentModel.getId();
            score.setDataId(dataId);
            score.setScore(currentModel.getScore());
            score.setWord(currentModel.getWord());

            score.setTimestamp(new Date(System.currentTimeMillis()));

            // Save recorded file
            File savedFile = new File(pronScoreDir, dataId + FileHelper.WAV_EXTENSION);
            FileUtils.copyFile(recordedFile,savedFile);
            // Save json data
            Gson gson = new Gson();
            currentModel.setAudioFile(savedFile.getAbsolutePath());
            FileUtils.writeStringToFile(new File(pronScoreDir, dataId + FileHelper.JSON_EXTENSION), gson.toJson(currentModel), "UTF-8");
            scoreDBAdapter.open();
            scoreDBAdapter.insert(score);
            scoreDBAdapter.close();
        }
    }

}
