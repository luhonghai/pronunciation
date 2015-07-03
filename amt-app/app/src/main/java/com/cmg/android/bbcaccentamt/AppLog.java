package com.cmg.android.bbcaccentamt;

import android.util.Log;

public class AppLog {
	private static final String APP_TAG = "AudioRecorder";
	
	public static int logString(String message){
		try {
			return Log.i(APP_TAG, message);
		} catch (Exception e) {
			return 0;
		}
	}
}
