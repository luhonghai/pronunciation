package com.cmg.android.bbcaccentamt;

import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
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
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.cmg.android.bbcaccentamt.activity.BaseActivity;
import com.cmg.android.bbcaccentamt.activity.DetailActivity;
import com.cmg.android.bbcaccentamt.activity.FeedbackActivity;
import com.cmg.android.bbcaccentamt.activity.SettingsActivity;
import com.cmg.android.bbcaccentamt.activity.fragment.FragmentTab;
import com.cmg.android.bbcaccentamt.activity.fragment.GraphFragment;
import com.cmg.android.bbcaccentamt.activity.fragment.HistoryFragment;
import com.cmg.android.bbcaccentamt.activity.fragment.Preferences;
import com.cmg.android.bbcaccentamt.activity.fragment.TipFragment;
import com.cmg.android.bbcaccentamt.activity.info.AboutActivity;
import com.cmg.android.bbcaccentamt.activity.info.HelpActivity;
import com.cmg.android.bbcaccentamt.activity.info.LicenceActivity;
import com.cmg.android.bbcaccentamt.activity.view.RecordingView;
import com.cmg.android.bbcaccentamt.adapter.ListMenuAdapter;
import com.cmg.android.bbcaccentamt.auth.AccountManager;
import com.cmg.android.bbcaccentamt.common.FileCommon;
import com.cmg.android.bbcaccentamt.data.PhonemeScoreDBAdapter;
import com.cmg.android.bbcaccentamt.data.ScoreDBAdapter;
import com.cmg.android.bbcaccentamt.data.SphinxResult;
import com.cmg.android.bbcaccentamt.data.UserProfile;
import com.cmg.android.bbcaccentamt.data.UserVoiceModel;
import com.cmg.android.bbcaccentamt.data.WordDBAdapter;
import com.cmg.android.bbcaccentamt.dictionary.DictionaryItem;
import com.cmg.android.bbcaccentamt.dictionary.DictionaryListener;
import com.cmg.android.bbcaccentamt.dictionary.DictionaryWalker;
import com.cmg.android.bbcaccentamt.dictionary.OxfordDictionaryWalker;
import com.cmg.android.bbcaccentamt.dsp.AndroidAudioInputStream;
import com.cmg.android.bbcaccentamt.http.UploaderAsync;
import com.cmg.android.bbcaccentamt.utils.AnalyticHelper;
import com.cmg.android.bbcaccentamt.utils.AndroidHelper;
import com.cmg.android.bbcaccentamt.utils.ColorHelper;
import com.cmg.android.bbcaccentamt.utils.DeviceUuidFactory;
import com.cmg.android.bbcaccentamt.utils.FileHelper;
import com.cmg.android.bbcaccentamt.utils.SimpleAppLog;
import com.cmg.android.bbcaccentamt.view.AlwaysMarqueeTextView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.nineoldandroids.animation.Animator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
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
    private ListView listMenu;


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

    private ListView lvItem;
    private TextView textrecord;


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
    Animation fadeIn;
    Animation fadeOut;

    private UploaderAsync uploadTask;

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


    private AccountManager accountManager;

    /**
     *  User profile
     */

    private  ImageView imgAvatar;
    private TextView txtUserName;
    private TextView txtUserEmail;
    private boolean isInitTabHost = false;
    /**
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountManager = new AccountManager(this);
        setContentView(R.layout.main);
        initListMenu();
        initCustomActionBar();
        if (savedInstanceState != null) {
            isInitTabHost = true;
        }
        initRecordingView();
        initAnimation();

        switchButtonStage(ButtonState.DISABLED);
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
        registerReceiver(mHandleHistoryAction, new IntentFilter(HistoryFragment.ON_HISTORY_LIST_CLICK));
        getWord(getString(R.string.example_word));
        scoreDBAdapter = new ScoreDBAdapter(this);
        checkProfile();
       ListAllItem();

    }

    private void openSettings() {
        startActivity(SettingsActivity.class);
    }

    private void ListAllItem(){
        lvItem=(ListView)findViewById(R.id.lvItem);
        textrecord=(TextView)findViewById(R.id.textrecord);
        String[] item={"WE DIDN'T LIKE THAT",
                "THE COMPANY DECLINED TO IDENTIFY THE BIDDERS BUT SAID IT RECEIVED OFFERS IN THE HIGH FORTY DOLLARS PER SHARE",
        "THERE WERE TWO HUNDRED FIFTY SIX ISSUES ADVANCING THREE HUNDRED THREE DECLINING AND TWO HUNDRED NINETY TWO UNCHANGED",
        "HOWEVER INVESTMENT INCOME WHICH REPRESENTS THIRTEEN PERCENT OF THE INDUSTRY'S REVENUES ROSE ELEVEN PERCENT IN THE QUARTER REFLECTING GAINS FROM THE RISING STOCK MARKET",
        "NO ONE AT THE STATE DEPARTMENT WANTS TO LET SPIES IN",
        "A LENGTHY FIGHT IS LIKELY",
        "THE JURY AWARDED MR. SCHARENBERG ONE HUNDRED FIVE MILLION DOLLARS A FIGURE BASED ON TEN YEARS OF PROFITS HAD HIS PROJECT BEEN COMPLETED",
        "TO MAKE THEM DIRECTLY COMPARABLE EACH INDEX IS BASED ON THE CLOSE OF NINETEEN SIXTY NINE EQUALING ONE HUNDRED",
        "HE DECLINED TO NAME SPECIFIC PRODUCTS",
        "IF THE DOLLAR STARTS TO PLUNGE THE FED MAY STEP UP ITS DEFENSE OF THE CURRENCY"};
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this, R.layout.lv_statement,R.id.textStatement, item);
        lvItem.setAdapter(arrayAdapter);
        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = (String) lvItem.getItemAtPosition(position);
                textrecord.setText(itemValue);

            }
        });
    }



    private void initAnimation() {
        fadeIn = AnimationUtils.loadAnimation(this.getApplicationContext(), android.R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(this.getApplicationContext(), android.R.anim.fade_out);
    }

    private void initListMenu() {
        listMenu = (ListView) findViewById(R.id.listMenu);
        ListMenuAdapter adapter = new ListMenuAdapter(this);
        listMenu.setAdapter(adapter);
        listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(HelpActivity.class);
                        break;
                    case 1:
                        startActivity(SettingsActivity.class);
                        break;
                    case 2:
                        startActivity(AboutActivity.class);
                        break;
                    case 3:
                        startActivity(LicenceActivity.class);
                        break;
                    case 4:
                        startActivity(FeedbackActivity.class);
                        break;
                    case 5:
                        SweetAlertDialog d = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
                        d.setTitleText(getString(R.string.logout_account_message_title));
                        d.setContentText(getString(R.string.logout_account_message_content));
                        d.setConfirmText(getString(R.string.logout));
                        d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                UserProfile profile = Preferences.getCurrentProfile(MainActivity.this);
                                if (profile != null) {
                                    AnalyticHelper.sendUserLogout(MainActivity.this, profile.getUsername());
                                }
                                accountManager.logout();
                                MainActivity.this.finish();
                                startActivity(LoginActivity.class);
                            }
                        });
                        d.setCancelText(getString(R.string.dialog_no));
                        d.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });
                        d.show();
                        break;
                }
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void startActivity(Class clazz) {
        Intent activity = new Intent();
        activity.setClass(this, clazz);
        this.startActivity(activity);
    }

    private void initRecordingView() {
        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtUserEmail = (TextView) findViewById(R.id.txtUserEmail);

        imgHelpHand = (ImageButton) findViewById(R.id.imgHelpHand);

        imgHourGlass = (ImageButton) findViewById(R.id.imgHourGlass);
        recordingView = (RecordingView) findViewById(R.id.main_recording_view);
        btnAnalyzing = (ImageButton) findViewById(R.id.btnAnalyzing);
        btnAnalyzing.setOnClickListener(this);
        btnAudio = (ImageButton) findViewById(R.id.btnAudio);
        btnAudio.setOnClickListener(this);

        recordingView.setOnClickListener(this);
        recordingView.setAnimationListener(this);
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
            searchView.setFocusable(true);
            searchView.performClick();
            searchView.requestFocus();
            searchView.setIconified(true);
            dbAdapter = WordDBAdapter.getInstance(this.getApplicationContext());
            try {
                dbAdapter.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            searchView.setQueryHint(getString(R.string.tint_search_word));
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
          //  searchView.setIconifiedByDefault(false);
            searchView.setOnQueryTextListener(this);
            searchView.setOnSuggestionListener(this);
            adapter = new SimpleCursorAdapter(this, R.layout.search_word_item,
                    dbAdapter.getAll(),
                    new String[] {WordDBAdapter.KEY_WORD, WordDBAdapter.KEY_PRONUNCIATION},
                    new int[] {},
                    CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            searchView.setSuggestionsAdapter(adapter);
        }
        //return super.onCreateOptionsMenu(menu);
        return true;
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
            case R.id.menu_feedback:
                startActivity(FeedbackActivity.class);
                break;
        }
        //return super.onOptionsItemSelected(item);
        return true;
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
        if (isRecording) return;
        if (checkNetwork(false)) {
            AnalyticHelper.sendSelectWord(this, word);
            switchButtonStage(ButtonState.DISABLED);
            recordingView.startPingAnimation(this);

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
    }

    @Override
    public boolean onQueryTextSubmit(final String s) {
        if (s.length() > 0) {
            hideSoftKeyboard();
            View v = getCurrentFocus();
            if (v != null)
                v.clearFocus();
            getWord(s);
        }
        return false;
    }

    private Runnable updateQueryRunnable = new Runnable() {
        @Override
        public void run() {
            updateQuery();
        }
    };

    private void updateQuery() {
        try {
            dbAdapter.close();
            dbAdapter.open();
        } catch (Exception e) {

        }
        final Cursor c = dbAdapter.search(searchText);
        if (c.getCount() > 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    searchView.getSuggestionsAdapter().changeCursor(c);
                    //searchView.getSuggestionsAdapter().notifyDataSetChanged();
                }
            });
        }
    }

    private Handler updateQueryHandler = new Handler();

    private String searchText;

    @Override
    public boolean onQueryTextChange(String s) {
        if (s.length() > 0) {
            searchText = s;
            updateQueryHandler.removeCallbacks(updateQueryRunnable);
            updateQueryHandler.postDelayed(updateQueryRunnable, 200);
            //updateQuery();
            return true;
        }
        return false;
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
                                recordingView.startPingAnimation(MainActivity.this, 2000, 100.0f, true, false);
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
        try {
            unregisterReceiver(mHandleHistoryAction);
        } catch (Exception e) {

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
        SimpleAppLog.info("Request location update");
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {
            lm.removeUpdates(this);
        } catch (Exception e) {
            SimpleAppLog.error("Could not stop request location",e);
        }
    }

    protected void requestLocation() {
        SimpleAppLog.info("Request location update");
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5 * 60 * 1000, 1000, this);
        } catch (Exception e) {
            SimpleAppLog.error("Could not request GPS provider location", e);
        }
        try {
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5 * 60 * 1000, 1000, this);
        } catch (Exception e) {
            SimpleAppLog.error("Could not request Network provider location", e);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!isInitTabHost) {
            isInitTabHost = true;


        }
        requestLocation();
        fetchSetting();
        isPrepared = false;
        if (currentModel != null) {
            analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MAX;
            recordingView.startPingAnimation(this, 2000, currentModel.getScore(), true, true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAnalyzing:
                if (checkNetwork(false)) {
                    if (isRecording) {
                        stop();
                        switchButtonStage(ButtonState.DISABLED);
                        if (currentModel != null) {
                            analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MAX;
                            recordingView.startPingAnimation(this, 2000, currentModel.getScore(), true, true);
                        } else {
                            analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MIN;
                            recordingView.startPingAnimation(this, 1000, 100.0f, false, false);
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

            case R.id.main_recording_view:
                if (currentModel != null && dictionaryItem != null) {
                    Gson gson = new Gson();
                    Intent intent = new Intent(this, DetailActivity.class);
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

                break;
            case PLAYING:
                btnAudio.startAnimation(fadeOut);
                btnAudio.setImageResource(R.drawable.p_close_red);
                btnAudio.startAnimation(fadeIn);
                btnAnalyzing.setImageResource(R.drawable.p_record_gray);
                btnAnalyzing.setEnabled(false);

                break;
            case GREEN:
                btnAudio.setEnabled(true);
                btnAnalyzing.setEnabled(true);
                btnAudio.setImageResource(R.drawable.p_audio_green);
                btnAnalyzing.setImageResource(R.drawable.p_record_green);

                isProcess = false;
                break;
            case ORANGE:
                btnAudio.setEnabled(true);
                btnAnalyzing.setEnabled(true);
                btnAudio.setImageResource(R.drawable.p_audio_orange);
                btnAnalyzing.setImageResource(R.drawable.p_record_orange);

                isProcess = false;
                break;
            case RED:
                btnAudio.setEnabled(true);
                btnAnalyzing.setEnabled(true);
                btnAudio.setImageResource(R.drawable.p_audio_red);
                btnAnalyzing.setImageResource(R.drawable.p_record_red);
                isProcess = false;
                break;
            case DISABLED:
                btnAudio.setEnabled(false);
                btnAnalyzing.setEnabled(false);
                btnAudio.setImageResource(R.drawable.p_audio_gray);
                btnAnalyzing.setImageResource(R.drawable.p_record_gray);
                break;
            case DEFAULT:
            default:
                btnAudio.setEnabled(true);
                btnAnalyzing.setEnabled(true);
                btnAudio.setImageResource(R.drawable.p_audio_green);
                btnAnalyzing.setImageResource(R.drawable.p_record_green);

                isProcess = false;
                break;
        }
        // Call other view update
        Intent notifyUpdateIntent = new Intent(FragmentTab.ON_UPDATE_DATA);
        notifyUpdateIntent.putExtra(FragmentTab.ACTION_TYPE, isProcess ? FragmentTab.TYPE_DISABLE_VIEW : FragmentTab.TYPE_ENABLE_VIEW);
        sendBroadcast(notifyUpdateIntent);
        if (!checkAudioExist()) {
            btnAudio.setEnabled(false);
            btnAudio.setImageResource(R.drawable.p_audio_gray);
        }
        } catch (Exception e) {
            SimpleAppLog.error("Could not update screen state",e);
        }
    }

    private void checkProfile() {
        UserProfile profile = Preferences.getCurrentProfile(this);
        if (profile == null) {
            AppLog.logString("No profile found");
            //openSettings();
            //startActivity(HelpActivity.class);
        } else if (profile.getHelpStatus() == UserProfile.HELP_INIT) {
            AppLog.logString("Profile is not setup: " + profile.getUsername());
            //openSettings();
            //profile.setHelpStatus(UserProfile.HELP_SKIP);
            Preferences.setHelpStatusProfile(this, profile.getUsername(), UserProfile.HELP_SKIP);
            startActivity(HelpActivity.class);
        } else if (profile.getHelpStatus() == UserProfile.HELP_SKIP) {
            AppLog.logString("Display help dialog");
            showHelpDialog();
        } else {
            SimpleAppLog.info("Help status: " + profile.getHelpStatus());
            //showHelpDialog();
        }
    }

    private void fetchSetting() {
        try {
            UserProfile profile = Preferences.getCurrentProfile(this);
            if (profile != null) {
                txtUserName.setText(profile.getName());
                txtUserEmail.setText(profile.getUsername());
                if (profile.getProfileImage().length() > 0) {
                    if (!ImageLoader.getInstance().isInited()) {
                        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
                    }
                    ImageLoader.getInstance().displayImage(profile.getProfileImage(), imgAvatar);
                }
            }

            String mChanel = Preferences.getString(Preferences.KEY_AUDIO_CHANEL, this.getApplicationContext(), "mono");
            if (mChanel.equalsIgnoreCase("mono")) {
                chanel = AudioFormat.CHANNEL_IN_MONO;
            } else {
                chanel = AudioFormat.CHANNEL_IN_STEREO;
            }
            sampleRate = Preferences.getInt(Preferences.KEY_AUDIO_SAMPLE_RATE, this.getApplicationContext(), 16000);
            isAutoStop = Preferences.getBoolean(Preferences.KEY_AUDIO_AUTO_STOP_RECORDING, this.getApplicationContext(), true);

            bufferSize = AudioRecord.getMinBufferSize(sampleRate, chanel, RECORDER_AUDIO_ENCODING);

            isPrepared = false;
        } catch (Exception e) {
            SimpleAppLog.error("Could not fetch setting",e);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        accountManager.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        accountManager.stop();
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
                }catch (Exception e) {
                    SimpleAppLog.error("Could not save data to database", e);
                    e.printStackTrace();
                    currentModel = null;
                }
                AppLog.logString("Start score animation");
                // Waiting for animation complete
                analyzingState = AnalyzingState.WAIT_FOR_ANIMATION_MIN;
            }
        }
    };

    private final BroadcastReceiver mHandleHistoryAction = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle.containsKey(BaseActivity.USER_VOICE_MODEL)) {
                String modelSource = bundle.getString(BaseActivity.USER_VOICE_MODEL);
                String word = bundle.getString(FragmentTab.ARG_WORD);
                int type = bundle.getInt(FragmentTab.ACTION_TYPE);
                if (word == null || word.length() == 0) {
                    Gson gson = new Gson();
                    final UserVoiceModel model = gson.fromJson(modelSource, UserVoiceModel.class);
                    if (model != null) {
                        switch (type) {
                            case HistoryFragment.CLICK_LIST_ITEM:
                                Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
                                detailIntent.putExtra(DetailActivity.USER_VOICE_MODEL, gson.toJson(model));
                                startActivity(detailIntent);
                                break;
                            case HistoryFragment.CLICK_PLAY_BUTTON:
                                runOnUiThread(new Runnable() {
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
                }
            }
        }
    };

    private void uploadRecord() {
        try {
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
                    Location location = AndroidHelper.getLastBestLocation(this);
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
        try {
            AppLog.logString("Select suggestion: " + index);
            Cursor cursor = (Cursor) adapter.getItem(index);
            String s = cursor.getString(cursor.getColumnIndex(WordDBAdapter.KEY_WORD));
            searchView.setQuery(s, true);
        } catch (Exception e) {
            SimpleAppLog.error("Could not select suggestion word",e);
        }
    }

    @Override
    public boolean onSuggestionClick(int i) {
        selectSuggestionWord(i);
        return true;
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
                    Intent notifyUpdateIntent = new Intent(FragmentTab.ON_UPDATE_DATA);
                    notifyUpdateIntent.putExtra(FragmentTab.ACTION_TYPE, FragmentTab.TYPE_RELOAD_DATA);
                    notifyUpdateIntent.putExtra(FragmentTab.ACTION_DATA, gson.toJson(currentModel));
                    sendBroadcast(notifyUpdateIntent);
                    switchButtonStage();

                    YoYo.with(Techniques.FadeIn).duration(500).withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            imgHelpHand.setVisibility(View.VISIBLE);
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
                    }).playOn(imgHelpHand);
                } else {
                    switchButtonStage(ButtonState.RED);
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
                    recordingView.startPingAnimation(this, 2000, currentModel.getScore(), true, true);
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


                }
            }
        } catch (Exception e) {
            SimpleAppLog.error("Could not complete animation",e);
        }
    }



    private void saveToDatabase() throws IOException, SQLException {
        if (audioStream == null) return;
        String tmpFile = audioStream.getFilename();
        File recordedFile = new File(tmpFile);
        if (recordedFile.exists() && currentModel != null) {
            AnalyticHelper.sendAnalyzingWord(this, currentModel.getWord(), Math.round(currentModel.getScore()));
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
            if (currentModel.getResult() != null) {
                PhonemeScoreDBAdapter phonemeScoreDBAdapter = new PhonemeScoreDBAdapter(this);
                phonemeScoreDBAdapter.open();
                List<SphinxResult.PhonemeScore> phonemeScoreList = currentModel.getResult().getPhonemeScores();
                if (phonemeScoreList != null && phonemeScoreList.size() > 0) {
                    for (SphinxResult.PhonemeScore phonemeScore : phonemeScoreList) {
                        phonemeScoreDBAdapter.insert(phonemeScore);
                    }
                }
                phonemeScoreDBAdapter.close();
            }
        }
    }


    private void showHelpDialog() {
        final Dialog dialog = new Dialog(this, R.style.Theme_WhiteDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.help_dialog);

        dialog.findViewById(R.id.btnNever).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfile userProfile = Preferences.getCurrentProfile(MainActivity.this);
                if (userProfile != null) {
                    Preferences.setHelpStatusProfile(MainActivity.this, userProfile.getUsername(), UserProfile.HELP_NEVER);
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
                startActivity(HelpActivity.class);
                dialog.dismiss();
            }
        });

        dialog.setTitle(null);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.show();
    }

}
