package com.cmg.android.voicerecorder;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cmg.android.voicerecorder.activity.BaseActivity;
import com.cmg.android.voicerecorder.activity.view.RecordingView;
import com.cmg.android.voicerecorder.data.DatabasePrepare;
import com.cmg.android.voicerecorder.data.DatabasePrepare.OnPrepraredListener;
import com.cmg.android.voicerecorder.data.PhonemeScoreDBAdapter;
import com.cmg.android.voicerecorder.data.ScoreDBAdapter;
import com.cmg.android.voicerecorder.data.SphinxResult;
import com.cmg.android.voicerecorder.data.UserVoiceModel;
import com.cmg.android.voicerecorder.data.WordDBAdapter;
import com.cmg.android.voicerecorder.utils.AndroidHelper;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by luhonghai on 3/17/15.
 */
public class SplashScreen extends BaseActivity implements
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, OnPrepraredListener {

    private GoogleApiClient mGoogleApiClient;

    private boolean isLogin = false;

    enum LoadItem {
        FACEBOOK,
        GOOGLE_PLUS,
        ACCENT_EASY,
        DATABASE,
        SETTING
    }

    private final List<LoadItem> loadStatus = new ArrayList<LoadItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();

        AppLog.logString("Key hash: " + AndroidHelper.getKeyHash(getApplicationContext()));
        setContentView(R.layout.splashscreen);

        loadStatus.add(LoadItem.FACEBOOK);
        loadStatus.add(LoadItem.GOOGLE_PLUS);
        loadStatus.add(LoadItem.DATABASE);

        new DatabasePrepare(this, this).prepare();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    isLogin = true;
                    AppLog.logString("Login with facebook");
                }
                AppLog.logString("Complete check facebook");
                loadStatus.remove(LoadItem.FACEBOOK);
                validateCallback();
                return null;
            }
        }.execute();
    }

    private void validateCallback() {
        if (loadStatus.isEmpty()) {
            if (isLogin) {
                goToActivity(MainActivity.class);
            } else {
                goToActivity(LoginActivity.class);
            }
        }
    }

    private void goToActivity(Class clazz) {
        Intent mainIntent = new Intent(SplashScreen.this,clazz);
        SplashScreen.this.startActivity(mainIntent);
        SplashScreen.this.finish();
    }

    @Override
    public void onComplete() {
        AppLog.logString("Complete check database");
        loadStatus.remove(LoadItem.DATABASE);
        validateCallback();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle bundle) {
        loadStatus.remove(LoadItem.GOOGLE_PLUS);
        isLogin = true;
        AppLog.logString("Complete check google+");
        AppLog.logString("Login with google+");
        validateCallback();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        AppLog.logString("Complete check google+");
        loadStatus.remove(LoadItem.GOOGLE_PLUS);
        validateCallback();
    }
}
