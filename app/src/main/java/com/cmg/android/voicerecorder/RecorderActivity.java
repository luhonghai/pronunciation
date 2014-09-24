package com.cmg.android.voicerecorder;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cmg.android.voicerecorder.http.FileCommon;
import com.cmg.android.voicerecorder.http.UploaderAsync;
import com.cmg.android.voicerecorder.utils.DeviceUuidFactory;
import com.cmg.android.voicerecorder.view.MySurfaceView;

public class RecorderActivity extends Activity implements RecorderHelper.OnUpdateListener {
    private RecorderHelper recorderHelper;
    private MySurfaceView surfaceView;
	private TextView txtTime;
    private String mUUID;
    private Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        setButtonHandlers();
        enableButtons(false);

        surfaceView = (MySurfaceView) findViewById(R.id.view_surface_amp);
        recorderHelper = new RecorderHelper(this);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtTime.setText(recorderHelper.getRecordTime());
        mUUID = new DeviceUuidFactory(this).getDeviceUuid().toString();
        ((TextView) findViewById(R.id.txtYourId)).setText("Your ID: " + mUUID);
        this.mContext = this;

        registerReceiver(mHandleMessageReader, new IntentFilter(UploaderAsync.UPLOAD_COMPLETE_INTENT));
    }

	private void setButtonHandlers() {
		((Button)findViewById(R.id.btnStart)).setOnClickListener(btnClick);
        ((Button)findViewById(R.id.btnStop)).setOnClickListener(btnClick);
        ((Button)findViewById(R.id.btnUpload)).setOnClickListener(btnClick);
	}
	
	private void enableButton(int id,boolean isEnable){
		((Button)findViewById(id)).setEnabled(isEnable);
	}
	
	private void enableButtons(boolean isRecording) {
		enableButton(R.id.btnStart,!isRecording);
		enableButton(R.id.btnStop,isRecording);
        enableButton(R.id.btnUpload,isRecording);
	}

	
	private View.OnClickListener btnClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch(v.getId()){
				case R.id.btnStart:{
					AppLog.logString("Start Recording");

                    enableButton(R.id.btnStart,false);
                    enableButton(R.id.btnStop,true);
                    enableButton(R.id.btnUpload, false);
                    recorderHelper.startRecording();
							
					break;
				}
				case R.id.btnStop:{
					AppLog.logString("Stop Recording");
                    enableButton(R.id.btnStart,true);
                    enableButton(R.id.btnStop,false);
                    enableButton(R.id.btnUpload, true);
                    recorderHelper.stopRecording();
					
					break;
				}
                case R.id.btnUpload:{
                    AppLog.logString("Start Uploading");
                    ((Button) findViewById(R.id.btnUpload)).setText("Uploading...");
                    enableButton(R.id.btnStart, false);
                    enableButton(R.id.btnStop,false);
                    enableButton(R.id.btnUpload,false);

                    UploaderAsync async  = new UploaderAsync(mContext, getResources().getString(R.string.upload_url));
                    Map<String, String> params = new HashMap<String,String>();
                    String fileName = recorderHelper.getFilename();
                    File tmp = new File(fileName);
                    if (tmp.exists()) {
                        params.put(FileCommon.PARA_FILE_NAME, tmp.getName());
                        params.put(FileCommon.PARA_FILE_PATH,tmp.getAbsolutePath());
                        params.put(FileCommon.PARA_FILE_TYPE, "audio/wav");
                        params.put("uuid", mUUID);
                        async.execute(params);
                    }
                    break;
                }
			}
		}
	};

    @Override
    public void update(final short[] bytes, final int length, final float sampleLength) {
        runOnUiThread(new Runnable() {
            public void run() {
                surfaceView.setData(bytes, length, sampleLength);
                txtTime.setText(recorderHelper.getRecordTime());
            }
        });
    }

    /**
     *
     */
    private final BroadcastReceiver mHandleMessageReader = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras().containsKey(UploaderAsync.UPLOAD_COMPLETE_INTENT)) {
                enableButton(R.id.btnStart, true);
                enableButton(R.id.btnStop,false);
                ((Button) findViewById(R.id.btnUpload)).setText("Upload");
                enableButton(R.id.btnUpload,false);
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

    }
}