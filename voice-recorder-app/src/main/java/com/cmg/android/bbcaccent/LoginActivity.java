package com.cmg.android.bbcaccent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cmg.android.bbcaccent.activity.BaseActivity;
import com.cmg.android.bbcaccent.activity.fragment.Preferences;
import com.cmg.android.bbcaccent.activity.view.RecordingView;
import com.cmg.android.bbcaccent.auth.AccountManager;
import com.cmg.android.bbcaccent.data.UserProfile;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by luhonghai on 3/17/15.
 */
public class LoginActivity extends BaseActivity implements RecordingView.OnAnimationListener, FacebookCallback<LoginResult>,
        GoogleApiClient.ConnectionCallbacks,
        View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    private  CallbackManager callbackManager;

    private ImageButton btnLoginFB;

    private GoogleApiClient mGoogleApiClient;

    private boolean mIntentInProccess;

    private boolean signedInUser;

    private ConnectionResult mConnectionResult;

    private ImageButton btnLoginGGPlus;

    private ProfileTracker profileTracker;

    private AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountManager = new AccountManager(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, this);
        setContentView(R.layout.login);

        btnLoginFB = (ImageButton) findViewById(R.id.loginButton);
        btnLoginFB.setOnClickListener(this);

        btnLoginGGPlus = (ImageButton) findViewById(R.id.sign_in_button);
        btnLoginGGPlus.setOnClickListener(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();


        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
            }
        };
        enableForm(true);
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
//        Toast.makeText(this, "Login successfully", Toast.LENGTH_LONG).show();
//        startMainActivity();

        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            AppLog.logString(object.toString());
                            UserProfile profile = new UserProfile();
                            try {
                                profile.setUsername(object.getString("email"));
                            } catch (JSONException e) {

                            }
                            try {
                                profile.setGender(object.getString("gender").equalsIgnoreCase("male"));
                            } catch (JSONException e) {

                            }
                            try {
                                profile.setDob(object.getString("birthday"));
                            } catch (JSONException e) {

                            }
                            try {
                                profile.setName(object.getString("name"));
                            } catch (JSONException e) {

                            }
                            try {
                                profile.setProfileImage("https://graph.facebook.com/"+object.getString("id")+"/picture");
                            } catch (JSONException e) {

                            }
                            profile.setLoginType(UserProfile.TYPE_FACEBOOK);
                            doAuth(profile);
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    enableForm(true);
                                }
                            });
                        }
                    }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields","id,name,email,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onCancel() {
        Toast.makeText(this, "Cancel login", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(FacebookException e) {
        Toast.makeText(this, "Could not login to Facebook", Toast.LENGTH_LONG).show();
    }

    private void doAuth(UserProfile profile) {
        //Toast.makeText(this, "Login with " + profile.getUsername(), Toast.LENGTH_LONG).show();
        Preferences.setSelectedUsername(profile.getUsername(), LoginActivity.this);
        Preferences.addProfile(this, profile);

        UserProfile currentProfile = Preferences.getCurrentProfile(this);
        if (currentProfile == null) {
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Error")
                    .setMessage("Could not create profile")
                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LoginActivity.this.finish();
                        }
                    })
                    .show();
        } else {
            accountManager.auth(profile, new AccountManager.AuthListener() {
                @Override
                public void onError(final String message, Throwable e) {
                    final SpannableString s = new SpannableString(message);
                    Linkify.addLinks(s, Linkify.ALL);
                    if (e != null) e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("Could not login")
                                    .setMessage(s)
                                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            LoginActivity.this.finish();
                                        }
                                    })
                                    .show();
                        }
                    });

                }
                @Override
                public void onSuccess() {
                    startMainActivity();
                }
            });
        }


    }

    private void startMainActivity() {
        Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
        LoginActivity.this.startActivity(mainIntent);
        LoginActivity.this.finish();
    }

    @Override
    public void onConnected(Bundle bundle) {
        enableForm(true);
        signedInUser = false;
        Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        if (person != null) {
            UserProfile profile = new UserProfile();
            profile.setUsername(Plus.AccountApi.getAccountName(mGoogleApiClient));
            profile.setDob(person.getBirthday());
            profile.setProfileImage(person.getImage().getUrl());
            profile.setName(person.getDisplayName());
            profile.setGender(person.getGender() == 1);
            profile.setLoginType(UserProfile.TYPE_GOOGLE_PLUS);
            doAuth(profile);
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
                enableForm(false);
                googlePlusLogin();
                break;
            case R.id.loginButton:
                enableForm(false);
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_birthday"));
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
        enableForm(true);
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

    private void enableForm(boolean enable) {
        btnLoginGGPlus.setEnabled(enable);
        btnLoginFB.setEnabled(enable);
    }
}
