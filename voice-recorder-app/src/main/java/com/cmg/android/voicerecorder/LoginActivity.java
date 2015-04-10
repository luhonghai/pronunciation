package com.cmg.android.voicerecorder;

import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cmg.android.voicerecorder.activity.BaseActivity;
import com.cmg.android.voicerecorder.activity.view.RecordingView;
import com.cmg.android.voicerecorder.data.PhonemeScoreDBAdapter;
import com.cmg.android.voicerecorder.data.ScoreDBAdapter;
import com.cmg.android.voicerecorder.data.WordDBAdapter;
import com.cmg.android.voicerecorder.utils.AndroidHelper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.util.Arrays;

/**
 * Created by luhonghai on 3/17/15.
 */
public class LoginActivity extends BaseActivity implements RecordingView.OnAnimationListener, FacebookCallback<LoginResult>,
        GoogleApiClient.ConnectionCallbacks,
        View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    private  CallbackManager callbackManager;

    private ImageButton loginButton;

    private GoogleApiClient mGoogleApiClient;

    private boolean mIntentInProccess;

    private boolean signedInUser;

    private ConnectionResult mConnectionResult;

    private ImageButton signInButton;

    private ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, this);
        setContentView(R.layout.login);

        loginButton = (ImageButton) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        signInButton = (ImageButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

            }
        };
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

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProccess = true;
                mConnectionResult.startResolutionForResult(this, 0);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProccess = false;
                mGoogleApiClient.connect();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

    @Override
    public void onAnimationMax() {

    }

    @Override
    public void onAnimationMin() {

    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        Toast.makeText(this, "Login successfully", Toast.LENGTH_LONG).show();
        startMainActivity();
    }

    @Override
    public void onCancel() {
        Toast.makeText(this, "Cancel login", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(FacebookException e) {
        Toast.makeText(this, "Could not login to Facebook", Toast.LENGTH_LONG).show();
    }

    private void doAuth(String email) {
        Toast.makeText(this, "Login with " + email, Toast.LENGTH_LONG).show();
        startMainActivity();
    }

    private void startMainActivity() {
        Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
        LoginActivity.this.startActivity(mainIntent);
        LoginActivity.this.finish();
    }

    @Override
    public void onConnected(Bundle bundle) {
        signedInUser = false;

        Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        if (person != null) {
            doAuth(Plus.AccountApi.getAccountName(mGoogleApiClient));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    private void googlePlusLogin() {
        if (!mGoogleApiClient.isConnecting()) {
            signedInUser = true;
            resolveSignInError();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                googlePlusLogin();
                break;
            case R.id.loginButton:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    signedInUser = false;
                }
                mIntentInProccess = false;
                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                }
                break;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!connectionResult.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
            return;
        }
        if (!mIntentInProccess) {
            mConnectionResult = connectionResult;
            if (signedInUser) {
                //resolveSignInError();
            }
        }
    }
}
