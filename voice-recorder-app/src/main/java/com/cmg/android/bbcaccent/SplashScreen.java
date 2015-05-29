package com.cmg.android.bbcaccent;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmg.android.bbcaccent.activity.BaseActivity;
import com.cmg.android.bbcaccent.activity.fragment.Preferences;
import com.cmg.android.bbcaccent.auth.AccountManager;
import com.cmg.android.bbcaccent.data.DatabasePrepare;
import com.cmg.android.bbcaccent.data.DatabasePrepare.OnPrepraredListener;
import com.cmg.android.bbcaccent.data.UserProfile;
import com.cmg.android.bbcaccent.utils.AnalyticHelper;
import com.cmg.android.bbcaccent.utils.AndroidHelper;
import com.crashlytics.android.Crashlytics;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.nostra13.universalimageloader.core.ImageLoader;

import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luhonghai on 3/17/15.
 */
public class SplashScreen extends BaseActivity implements
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, OnPrepraredListener {

    private GoogleApiClient mGoogleApiClient;

    private static final long MIN_SPLASH_TIME = 5000;

    private boolean isLogin = false;

    enum LoadItem {
        FACEBOOK,
        GOOGLE_PLUS,
        ACCENT_EASY,
        DATABASE,
        SETTING
    }

    private final List<LoadItem> loadStatus = new ArrayList<LoadItem>();

    private AccountManager accountManager;

    private ImageView imgDog;

    private int currentDogFrame = 1;

    private Runnable runnableDogAnimation = new Runnable() {
        @Override
        public void run() {
            if (imgDog == null) return;
            int dogFrameId = R.drawable.sl_dog;
            if (currentDogFrame == 1) {
                dogFrameId = R.drawable.sl_dog_1;
            } else if (currentDogFrame == 2) {
                dogFrameId = R.drawable.sl_dog_2;
            } else if (currentDogFrame == 3) {
                dogFrameId = R.drawable.sl_dog_3;
            }

            imgDog.setImageResource(dogFrameId);
            //ImageLoader.getInstance().displayImage("drawable://" + dogFrameId, imgDog);
            currentDogFrame++;
            if (currentDogFrame > 3) {
                currentDogFrame = 1;
            }
            handlerDogAnimation.postDelayed(runnableDogAnimation, 1000 / 2);
        }
    };

    private Handler handlerDogAnimation = new Handler();

    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        startTime = System.currentTimeMillis();
        AppLog.logString("Key hash: " + AndroidHelper.getKeyHash(getApplicationContext()));
        setContentView(R.layout.splashscreen);
        imgDog = (ImageView) findViewById(R.id.imgDog);
        if (checkNetwork()) {
            accountManager = new AccountManager(this);
            FacebookSdk.sdkInitialize(getApplicationContext());
            mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                    .addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();
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
            handlerDogAnimation.post(runnableDogAnimation);
            UserProfile profile = Preferences.getCurrentProfile(this);
            if (profile != null && profile.getLoginType().equalsIgnoreCase(UserProfile.TYPE_EASYACCENT)
                    && profile.isLogin()) {
                isLogin = true;
            }
        }
    }




    private void validateCallback() {
        if (loadStatus.isEmpty()) {
            final UserProfile profile = Preferences.getCurrentProfile(this);
            if (isLogin) {
                if (profile == null) {
                    goToActivity(LoginActivity.class);
                } else {
                    accountManager.auth(profile, new AccountManager.AuthListener() {
                        @Override
                        public void onError(final String message, Throwable e) {
                            AnalyticHelper.sendUserLoginError(SplashScreen.this, profile.getUsername());
                            final SpannableString s = new SpannableString(message);
                            Linkify.addLinks(s, Linkify.ALL);
                            if (e != null) e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog d;
                                    if (profile.getLoginType().equalsIgnoreCase(UserProfile.TYPE_EASYACCENT)
                                            && (message.equalsIgnoreCase("Invalid username or password")
                                                || message.endsWith("is not activated"))) {
                                        d = new AlertDialog.Builder(SplashScreen.this)
                                                .setTitle("Could not login")
                                                .setMessage(s)
                                                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        SplashScreen.this.finish();
                                                    }
                                                })
                                                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        goToActivity(LoginActivity.class);
                                                    }
                                                })
                                                .create();
                                    } else {
                                       d = new AlertDialog.Builder(SplashScreen.this)
                                                .setTitle("Could not login")
                                                .setMessage(s)
                                                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        SplashScreen.this.finish();
                                                    }
                                                }).create();
                                    }
                                    d.show();
                                    ((TextView)d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                                }
                            });
                        }
                        @Override
                        public void onSuccess() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    doLicenseCheck(profile);
                                }
                            });

                        }
                    });
                }
            } else {
                goToActivity(LoginActivity.class);
            }
        }
    }

    private void doLicenseCheck(final UserProfile profile) {
        accountManager.checkLicense(profile, new AccountManager.AuthListener() {
            @Override
            public void onError(final String message, Throwable e) {
                AnalyticHelper.sendUserLoginError(SplashScreen.this, profile.getUsername());
                final SpannableString s = new SpannableString(message);
                Linkify.addLinks(s, Linkify.ALL);
                if (e != null) e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isRunning()) {
                            if (profile.getLicenseCode() != null && profile.getLicenseCode().length() > 0) {
                                AlertDialog d;
                                d = new AlertDialog.Builder(SplashScreen.this)
                                        .setTitle("Invalid licence")
                                        .setMessage("You need a valid licence code")
                                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                SplashScreen.this.finish();
                                            }
                                        })
                                        .setPositiveButton("Enter licence code", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                goToActivity(LoginActivity.class);
                                            }
                                        })
                                        .create();
                                d.show();
                                ((TextView) d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                            } else {
                                goToActivity(LoginActivity.class);
                            }
                        }
                    }
                });
            }

            @Override
            public void onSuccess() {
                AnalyticHelper.sendUserReturn(SplashScreen.this, profile.getUsername());
                goToActivity(MainActivity.class);
            }
        });
    }

    private void goToActivity(final Class clazz) {
        final long end = System.currentTimeMillis();
        final long loadTime = end - startTime;
        if (loadTime >= MIN_SPLASH_TIME) {
            startActivity(clazz);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(clazz);
                        }
                    }, MIN_SPLASH_TIME - loadTime);
                }
            });
        }
    }

    private void startActivity(Class clazz) {
        Intent mainIntent = new Intent(SplashScreen.this, clazz);
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
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
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
        handlerDogAnimation.removeCallbacks(runnableDogAnimation);
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
