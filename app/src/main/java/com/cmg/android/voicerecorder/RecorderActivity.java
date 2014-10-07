package com.cmg.android.voicerecorder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cmg.android.voicerecorder.activity.SettingsActivity;
import com.cmg.android.voicerecorder.activity.fragment.Preferences;
import com.cmg.android.voicerecorder.data.UserProfile;
import com.cmg.android.voicerecorder.data.UserVoiceModel;
import com.cmg.android.voicerecorder.dsp.AndroidAudioInputStream;
import com.cmg.android.voicerecorder.http.FileCommon;
import com.cmg.android.voicerecorder.http.UploaderAsync;
import com.cmg.android.voicerecorder.utils.DeviceUuidFactory;
import com.cmg.android.voicerecorder.utils.GPSTracker;
import com.cmg.android.voicerecorder.view.MySurfaceView;
import com.cmg.android.voicerecorder.view.SpectrogramView;
import com.google.gson.Gson;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.Oscilloscope;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;
import be.tarsos.dsp.util.fft.FFT;

public class RecorderActivity extends Activity {
    private static final String TAG = "AudioRecorder";

    //private static final int RECORDER_SAMPLERATE = 44100;
    //private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    //public static final float PCM_MAXIMUM_VALUE = 32768.0f;
    private static final long PITCH_TIMEOUT = 2000;
    private static final long START_TIMEOUT = 2000;

    private AndroidAudioInputStream audioStream;
    private AudioDispatcher dispatcher;
    private Thread runner;
    private double pitch = 0;
    private long lastDetecedPitchTime = -1;
    private int count = 0;
    private MySurfaceView waveView;
    private SpectrogramView specView;
    private TextView txtProfile;
    private TextView txtTime;

    private TextView txtPhonemes;
    private TextView txtHypothesis;
    private TextView txtSelectedWord;

    private AudioRecord audioInputStream;
    private int chanel;
    private int sampleRate;
    private int bufferSize;
            //= AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,RECORDER_CHANNELS,RECORDER_AUDIO_ENCODING);

    private UploaderAsync uploadTask;
    private UserProfile currentProfile;

    private boolean isPrepared = false;

    private Button btnRecord;
    private Button btnPlay;
    private Button btnUpload;

    private boolean isRecording = false;
    private boolean isPlaying = false;
    private boolean isUploading = false;

    private boolean isAutoStop = false;

    private PlayerHelper player;

    private long start;

    private long audioLong;

    private GPSTracker gpsTracker;

    private Spinner spnWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        specView = (SpectrogramView) findViewById(R.id.view_surface_amp);
        waveView = (MySurfaceView) findViewById(R.id.view_surface_wave);
        txtTime = (TextView) findViewById(R.id.txtTime);
        spnWords = (Spinner) findViewById(R.id.spinner_words);

        txtPhonemes = (TextView) findViewById(R.id.txtPhonemes);
        txtHypothesis = (TextView) findViewById(R.id.txtHypothesis);
        txtSelectedWord = (TextView) findViewById(R.id.txtSelectedWord);

        setButtonHandlers();
        enableButtons(false);
        registerReceiver(mHandleMessageReader, new IntentFilter(UploaderAsync.UPLOAD_COMPLETE_INTENT));

        (txtProfile = (TextView) findViewById(R.id.txtProfile)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();
            }
        });
        gpsTracker = new GPSTracker(this);
        //analyze();
    }

    private void stop() {
        try  {
            if (audioInputStream != null) {
                audioInputStream.stop();
                audioInputStream.release();
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
//        try {
//            if (audioStream != null)
//                audioStream.close();
//        } catch (Exception ex) {
//            // ex.printStackTrace();
//        }
        try {
            if (dispatcher != null)
                dispatcher.stop();
        } catch (Exception ex) {
            // ex.printStackTrace();
        }
        if (waveView != null) {
            waveView.recycle();
            waveView.invalidate();
        }

    }

    private void play() {
        try {
            player = new PlayerHelper(new File(audioStream.getFilename()), new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    AppLog.logString("Stop sound");
                    enableButton(R.id.btnStart,true);
                    enableButton(R.id.btnPlay,true);
                    enableButton(R.id.btnUpload, true);
                    btnPlay.setText("Play");
                    isPlaying = false;
                }

            });
            start = System.currentTimeMillis();
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void analyze() {
       // stop();
        if (waveView != null)
            waveView.recycle();
        if (specView != null)
            specView.recycle();
        audioInputStream = new AudioRecord(MediaRecorder.AudioSource.MIC,
                sampleRate, chanel,RECORDER_AUDIO_ENCODING, bufferSize);

        if(audioInputStream.getState() == AudioRecord.STATE_UNINITIALIZED){
            String configResume = "initRecorderParameters(sRates) has found recorder settings supported by the device:"
                    + "\nSource   = MICROPHONE"
                    + "\nsRate    = "+ sampleRate +"Hz"
                    + "\nChannel  = " + ((chanel == AudioFormat.CHANNEL_IN_MONO) ? "MONO" : "STEREO")
                    + "\nEncoding = 16BIT";
            Log.i(TAG, configResume);
            //+++Release temporary recorder resources and leave.
            audioInputStream.release();
            audioInputStream = null;
            return;
        }
        //start recording ! Opens the stream
        audioInputStream.startRecording();


        TarsosDSPAudioFormat format = new TarsosDSPAudioFormat(sampleRate, 16 ,(chanel == AudioFormat.CHANNEL_IN_MONO) ? 1 : 2, true, false);

        audioStream = new AndroidAudioInputStream(this.getApplicationContext(),audioInputStream, format, bufferSize);
        dispatcher = new AudioDispatcher(audioStream,bufferSize / 2, 0);
//        if (btnFilter.isChecked()) {
//            dispatcher.addAudioProcessor(new LowPassFS(90f, RECORDER_SAMPLERATE));
//            dispatcher.addAudioProcessor(new HighPass(300f, RECORDER_SAMPLERATE));
//        }
        dispatcher.addAudioProcessor(new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, sampleRate, bufferSize / 2, new PitchDetectionHandler() {

            @Override
            public void handlePitch(PitchDetectionResult pitchDetectionResult,
                                    AudioEvent audioEvent) {
                pitch = pitchDetectionResult.getPitch();
                if (pitch != -1) {
                    AppLog.logString("Detect pitch " + pitch);
                    lastDetecedPitchTime = System.currentTimeMillis();
                }
                if (((System.currentTimeMillis() - lastDetecedPitchTime) > PITCH_TIMEOUT)
                        && isAutoStop
                        && ((System.currentTimeMillis() - start) > (START_TIMEOUT + PITCH_TIMEOUT))) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AppLog.logString("Force stop recording. No pitch found after " + PITCH_TIMEOUT + "ms");
                            stopRecording();
                        }
                    });
                }

            }
        }));
        dispatcher.addAudioProcessor(fftProcessor);
        dispatcher.addAudioProcessor(new Oscilloscope(new Oscilloscope.OscilloscopeEventHandler() {
            @Override
            public void handleEvent(final float[] floats, final AudioEvent audioEvent) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateTime();
                        if (waveView != null)
                            waveView.setData(floats, pitch);
                    }
                });

            }
        }));
        //dispatcher.addAudioProcessor();
        start = System.currentTimeMillis();
        runner = new Thread(dispatcher,"Audio Dispatcher");
        lastDetecedPitchTime = System.currentTimeMillis();
        runner.start();
    }

    private void updateTime() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                audioLong = System.currentTimeMillis() - start;
                txtTime.setText(getRecordTime(start, System.currentTimeMillis()));
            }
        });
    }

    AudioProcessor fftProcessor = new AudioProcessor(){
        int bs;
        FFT fft;
        float[] amplitudes;
        private void init() {
            if (!isPrepared) {
                bs = bufferSize / 2;
                fft = new FFT(bs);
                amplitudes = new float[bs / 2];
                isPrepared = true;
            }
        }

        @Override
        public void processingFinished() {

        }

        @Override
        public boolean process(AudioEvent audioEvent) {
            init();
            final float[] audioFloatBuffer = audioEvent.getFloatBuffer();
            float[] transformbuffer = new float[bs*2];
            System.arraycopy(audioFloatBuffer, 0, transformbuffer, 0, audioFloatBuffer.length);
            fft.forwardTransform(transformbuffer);
            fft.modulus(transformbuffer, amplitudes);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (specView != null)
                        specView.drawFFT(pitch, amplitudes, fft);

                }
            });
            return true;
        }

    };

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (audioInputStream != null && audioInputStream.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING)
                audioInputStream.stop();
        } catch (Exception ex) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (txtProfile != null) {
            String username = Preferences.getString(Preferences.KEY_USERNAME_LIST, this.getApplicationContext());
            if (username.length() == 0) {
                openSettings();
            } else {
                txtProfile.setText("Current profile: " + username);
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
            Log.i(TAG, "Buffered size: " + bufferSize);
            Log.i(TAG, "Sample rate: " + sampleRate);
            Log.i(TAG, "Auto stop recording: " + isAutoStop);
            isPrepared = false;
        }
        try {
            if (audioInputStream != null && audioInputStream.getRecordingState() == AudioRecord.RECORDSTATE_STOPPED)
                audioInputStream.startRecording();
        } catch (Exception ex) {

        }
    }
    /**
     *
     */
    private final BroadcastReceiver mHandleMessageReader = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle.containsKey(UploaderAsync.UPLOAD_COMPLETE_INTENT)) {
                Toast.makeText(RecorderActivity.this, "Completed uploading process", Toast.LENGTH_LONG).show();
                enableButton(R.id.btnStart, true);
                enableButton(R.id.btnPlay,true);
                ((Button) findViewById(R.id.btnUpload)).setText("Upload");
                enableButton(R.id.btnUpload,false);
                String data = bundle.getString(UploaderAsync.UPLOAD_COMPLETE_INTENT);
                //Toast.makeText(RecorderActivity.this, data, Toast.LENGTH_LONG).show();
                Gson gson = new Gson();
                try {
                    UserVoiceModel model = gson.fromJson(data, UserVoiceModel.class);
                    txtPhonemes.setText( "Phonemes: " + model.getPhonemes());
                    txtHypothesis.setText( "Hypothesis: " + model.getHypothesis());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    };

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
            if (specView != null)
                specView.recycle();
        } catch (Exception ex) {

        }

        try {
            if (waveView != null)
                waveView.recycle();
        } catch (Exception ex) {

        }

    }

    private void setButtonHandlers() {
        (btnRecord = (Button)findViewById(R.id.btnStart)).setOnClickListener(btnClick);
        (btnPlay = (Button)findViewById(R.id.btnPlay)).setOnClickListener(btnClick);
        (btnUpload = (Button)findViewById(R.id.btnUpload)).setOnClickListener(btnClick);
    }

    private void enableButton(int id,boolean isEnable){
        ((Button)findViewById(id)).setEnabled(isEnable);
    }

    private void enableButtons(boolean isRecording) {
        enableButton(R.id.btnStart,!isRecording);
        enableButton(R.id.btnPlay,isRecording);
        enableButton(R.id.btnUpload,isRecording);
    }

    private void stopRecording() {
        if (isRecording) {
            isRecording = false;
            AppLog.logString("Stop Recording");
            enableButton(R.id.btnStart, false);
            stop();
            btnRecord.setText("Record");
            enableButton(R.id.btnPlay, true);
            enableButton(R.id.btnUpload, true);
            enableButton(R.id.btnStart, true);
        }
    }

    private View.OnClickListener btnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btnStart:{
                    if (isRecording) {
                        stopRecording();
                    } else {
                        AppLog.logString("Start Recording");
                        btnRecord.setText("Stop");
                        enableButton(R.id.btnStart, false);
                        enableButton(R.id.btnPlay, false);
                        enableButton(R.id.btnUpload, false);
                        analyze();
                        enableButton(R.id.btnStart, true);
                        isRecording = true;
                    }
                    break;
                }
                case R.id.btnPlay:{
                    if (isPlaying) {
                        AppLog.logString("Stop sound");
                        enableButton(R.id.btnPlay,false);
                        if (player != null) {
                            try {
                                player.stop();
                            } catch (Exception ex) {

                            }
                        }
                        enableButton(R.id.btnStart,true);
                        enableButton(R.id.btnPlay,true);
                        enableButton(R.id.btnUpload, true);
                        btnPlay.setText("Play");
                        isPlaying = false;
                    } else {
                        isPlaying = true;
                        AppLog.logString("Play sound");
                        play();
                        btnPlay.setText("Stop");
                        enableButton(R.id.btnStart, false);
                        enableButton(R.id.btnPlay,true);
                        enableButton(R.id.btnUpload, false);
                    }
                    break;
                }
                case R.id.btnUpload:{
                    if (isUploading) {
                        isUploading = false;
                        AppLog.logString("Stop uploading");
                        if (uploadTask.isCancelled())
                            uploadTask.cancel(true);
                        uploadTask = null;
                        enableButton(R.id.btnStart, true);
                        enableButton(R.id.btnPlay,true);
                        btnUpload.setText("Upload");
                        enableButton(R.id.btnUpload,true);
                    } else {
                        txtPhonemes.setText( "Phonemes: ...");
                        txtHypothesis.setText( "Hypothesis: ...");
                        isUploading = true;
                        AppLog.logString("Start Uploading");
                        Toast.makeText(RecorderActivity.this, "Please wait while data is uploading", Toast.LENGTH_LONG).show();
                        btnUpload.setText("Cancel");
                        enableButton(R.id.btnStart, false);
                        enableButton(R.id.btnPlay, false);
                        enableButton(R.id.btnUpload, true);

                        uploadTask = new UploaderAsync(RecorderActivity.this, getResources().getString(R.string.upload_url));
                        Map<String, String> params = new HashMap<String, String>();
                        String fileName = audioStream.getFilename();
                        File tmp = new File(fileName);
                        if (tmp.exists()) {
                            UserProfile profile = Preferences.getCurrentProfile(RecorderActivity.this);
                            if (profile != null) {
                                Gson gson = new Gson();
                                profile.setUuid(new DeviceUuidFactory(RecorderActivity.this).getDeviceUuid().toString());
                                UserProfile.UserLocation lc = new UserProfile.UserLocation();
                                if (gpsTracker.canGetLocation()) {
                                    lc.setLongitude(gpsTracker.getLongitude());
                                    lc.setLatitude(gpsTracker.getLatitude());
                                    AppLog.logString("Lat: " + lc.getLatitude() + ". Lon: " + lc.getLongitude());
                                    profile.setLocation(lc);
                                }

                                profile.setDuration(audioLong);
                                profile.setTime(System.currentTimeMillis());
                                params.put(FileCommon.PARA_FILE_NAME, tmp.getName());
                                params.put(FileCommon.PARA_FILE_PATH, tmp.getAbsolutePath());
                                params.put(FileCommon.PARA_FILE_TYPE, "audio/wav");
                                params.put("profile", gson.toJson(profile));
                                String word =spnWords.getSelectedItem().toString();
                                txtSelectedWord.setText("Last selected word: " + word);
                                params.put("word", word);
                                uploadTask.execute(params);
                            } else {
                                AppLog.logString("Could not get user profile");
                            }
                        }
                    }
                    break;
                }
            }
        }
    };


    private static final int MENU_SETTINGS = 0;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        MenuItem item = menu.add(0, MENU_SETTINGS, Menu.NONE, "Settings");
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setIcon(android.R.drawable.ic_menu_info_details);
        return super.onPrepareOptionsMenu(menu);
    }

    private void openSettings() {
        Intent settings = new Intent();
        settings.setClass(this, SettingsActivity.class);
        this.startActivity(settings);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_SETTINGS) {
            openSettings();
        }
        return super.onOptionsItemSelected(item);
    }

    public String getRecordTime(long startTime, long endTime) {
        if (startTime == 0) {
            return "00:00:00";
        }
        long millis = endTime - startTime;
        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60)) % 24;

        return String.format("%02d:%02d:%02d", hour, minute, second);
    }
}