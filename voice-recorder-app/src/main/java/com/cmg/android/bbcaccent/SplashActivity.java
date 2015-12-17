package com.cmg.android.bbcaccent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.widget.ImageView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.cmg.android.bbcaccent.auth.AccountManager;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.data.DatabasePrepare;
import com.cmg.android.bbcaccent.data.DatabasePrepare.OnPrepraredListener;
import com.cmg.android.bbcaccent.data.dto.UserProfile;
import com.cmg.android.bbcaccent.fragment.Preferences;
import com.cmg.android.bbcaccent.subscription.IAPFactory;
import com.cmg.android.bbcaccent.utils.AnalyticHelper;
import com.cmg.android.bbcaccent.utils.AndroidHelper;
import com.cmg.android.bbcaccent.utils.AppLog;
import com.cmg.android.bbcaccent.utils.GcmUtil;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.fabric.sdk.android.Fabric;

/**
 * Created by luhonghai on 3/17/15.
 */
public class SplashActivity extends BaseActivity {


    private static final long MIN_SPLASH_TIME = 3000;

    enum LoadItem {
        ACCENT_EASY,
        DATABASE,
        SETTING,
        SUBSCRIPTION,
        LICENSE_DATA,
        GCM
    }

    private final List<LoadItem> loadStatus = new ArrayList<LoadItem>();

    private AccountManager accountManager;

    @Bind(R.id.imgDog)
    ImageView imgDog;

    private int currentDogFrame = 1;

    private GcmUtil mGcmUtil;

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

    private BillingProcessor bp;

    private boolean isSubscription = false;

    private boolean isActivatedLicence = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainBroadcaster.getInstance().getSender().sendMessage(MainBroadcaster.Filler.CLOSE_MAIN_ACTIVITY, null);
        Fabric.with(this, new Crashlytics());
        startTime = System.currentTimeMillis();
        AppLog.logString("Key hash: " + AndroidHelper.getKeyHash(getApplicationContext()));
        setContentView(R.layout.splashscreen);
        ButterKnife.bind(this);
        if (checkNetwork()) {
            accountManager = new AccountManager(this);
            loadStatus.add(LoadItem.DATABASE);
            loadStatus.add(LoadItem.SUBSCRIPTION);
            loadStatus.add(LoadItem.LICENSE_DATA);
            new DatabasePrepare(this, new OnPrepraredListener() {
                @Override
                public void onComplete() {
                    AppLog.logString("Complete check database");
                    loadStatus.remove(LoadItem.DATABASE);
                    validateCallback();
                }

                @Override
                public void onError(final String message, Throwable e) {
                    SplashActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            handlerDogAnimation.removeCallbacks(runnableDogAnimation);
                            SweetAlertDialog d = new SweetAlertDialog(SplashActivity.this, SweetAlertDialog.ERROR_TYPE);
                            d.setTitleText("not enough information");
                            d.setContentText(message);
                            d.setConfirmText(getString(R.string.dialog_close));
                            d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    SplashActivity.this.finish();
                                }
                            });
                            d.show();
                        }
                    });

                }
            }).prepare();
            handlerDogAnimation.postDelayed(runnableDogAnimation, 500);
            if (AndroidHelper.checkGooglePlayServiceAvailability(getApplicationContext(), -1)) {
                bp = IAPFactory.getBillingProcessor(this, new BillingProcessor.IBillingHandler() {
                    @Override
                    public void onProductPurchased(String productId, TransactionDetails details) {

                    }

                    @Override
                    public void onBillingError(int errorCode, Throwable error) {
                        SimpleAppLog.error("Error with IAP. code: " + error, error);
                        loadStatus.remove(LoadItem.SUBSCRIPTION);
                        validateCallback();
                    }

                    @Override
                    public void onBillingInitialized() {
                        SimpleAppLog.debug("IAP init completed");
                        bp.loadOwnedPurchasesFromGoogle();
                        for (IAPFactory.Subscription subscription : IAPFactory.Subscription.values()) {
                            if (bp.isSubscribed(subscription.toString())) {
                                isSubscription = true;
                                break;
                            }
                        }
                        SimpleAppLog.debug("Is subscription: " + isSubscription);
                        loadStatus.remove(LoadItem.SUBSCRIPTION);
                        validateCallback();
                    }

                    @Override
                    public void onPurchaseHistoryRestored() {

                    }
                });
                mGcmUtil = GcmUtil.getInstance(this);
                if (mGcmUtil.getRegistrationId().isEmpty()) {
                    loadStatus.add(LoadItem.GCM);
                    mGcmUtil.register(new GcmUtil.GcmRegisterCallback() {
                        @Override
                        public void onRegistered(String registrationId) {
                            SimpleAppLog.info("gcm id: " + registrationId);
                            loadStatus.remove(LoadItem.GCM);
                            validateCallback();
                        }

                        @Override
                        public void onError(String message, Throwable e) {
                            SimpleAppLog.error(message, e);
                            loadStatus.remove(LoadItem.GCM);
                            validateCallback();
                        }
                    });
                } else {
                    SimpleAppLog.info("gcm id: " + mGcmUtil.getRegistrationId());
                }
            } else {
                loadStatus.remove(LoadItem.SUBSCRIPTION);
            }
            final UserProfile profile = Preferences.getCurrentProfile();
            if (profile != null && profile.isLogin()) {
                accountManager.loadLicenseData(profile, new AccountManager.AuthListener() {
                    @Override
                    public void onError(String message, Throwable e) {
                        SimpleAppLog.error("Could not load license data. Message: " + message, e);
                        loadStatus.remove(LoadItem.LICENSE_DATA);
                        validateCallback();
                    }

                    @Override
                    public void onSuccess() {
                        Preferences.updateProfile(SplashActivity.this, profile);
                        SimpleAppLog.debug("Load license data successfully. License data size: "
                                + (profile.getLicenseData() == null ? 0 : profile.getLicenseData().size()));
                        loadStatus.remove(LoadItem.LICENSE_DATA);
                        validateCallback();
                    }
                });
            } else {
                loadStatus.remove(LoadItem.LICENSE_DATA);
            }
        }
    }

    private void validateCallback() {
        if (loadStatus.isEmpty()) {
            final UserProfile profile = Preferences.getCurrentProfile();
            if (profile == null || !profile.isLogin()) {
                SimpleAppLog.debug("Account is not login. Go to login activity");
                goToActivity(LoginActivity.class);
            } else {
                SimpleAppLog.logJson("Detect old profile: ", profile);
                accountManager.auth(profile, new AccountManager.AuthListener() {
                    @Override
                    public void onError(final String message, Throwable e) {
                        AnalyticHelper.sendUserLoginError(SplashActivity.this, profile.getUsername());
                        final SpannableString s = new SpannableString(message);
                        Linkify.addLinks(s, Linkify.ALL);
                        if (e != null) e.printStackTrace();
                        handlerDogAnimation.removeCallbacks(runnableDogAnimation);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SweetAlertDialog d = new SweetAlertDialog(SplashActivity.this, SweetAlertDialog.ERROR_TYPE);
                                d.setTitleText(getString(R.string.could_not_login));
                                d.setContentText(message);
                                d.setCancelText(getString(R.string.dialog_close));
                                d.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        SplashActivity.this.finish();
                                    }
                                });
                                d.setConfirmText(getString(R.string.logout));
                                d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        accountManager.logout();
                                        goToActivity(LoginActivity.class);
                                    }
                                });
                                d.show();
                            }
                        });
                    }
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Preferences.updateProfile(MainApplication.getContext(), profile);
                                doLicenseCheck(Preferences.getCurrentProfile());
                            }
                        });
                    }
                }, true);
            }
        }
    }

    private void doGetProfile(final UserProfile profile) {
        accountManager.getProfile(profile, new AccountManager.AuthListener() {
            @Override
            public void onError(final String message, Throwable e) {
                AnalyticHelper.sendUserLoginError(SplashActivity.this, profile.getUsername());
                final SpannableString s = new SpannableString(message);
                Linkify.addLinks(s, Linkify.ALL);
                if (e != null) e.printStackTrace();
                handlerDogAnimation.removeCallbacks(runnableDogAnimation);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isRunning()) {
                            SweetAlertDialog d = new SweetAlertDialog(SplashActivity.this, SweetAlertDialog.ERROR_TYPE);
                            d.setTitleText(getString(R.string.could_not_fetch_profile));
                            d.setCancelText(getString(R.string.dialog_close));
                            d.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    SplashActivity.this.finish();
                                }
                            });
                            d.setContentText(message);
                            d.setConfirmText(getString(R.string.logout));
                            d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    accountManager.logout();
                                    startActivity(LoginActivity.class);
                                }
                            });
                            d.show();
                        }
                    }
                });
            }

            @Override
            public void onSuccess() {
                AnalyticHelper.sendUserReturn(SplashActivity.this, profile.getUsername());
                profile.setIsSubscription(isSubscription);
                profile.setIsActivatedLicence(isActivatedLicence);
                Preferences.updateProfile(SplashActivity.this, profile);
                goToActivity(MainActivity.class);
            }
        });
    }

    private void doLicenseCheck(final UserProfile profile) {
        accountManager.checkLicense(profile, new AccountManager.AuthListener() {
            @Override
            public void onError(final String message, Throwable e) {
//                AnalyticHelper.sendUserLoginError(SplashActivity.this, profile.getUsername());
//                final SpannableString s = new SpannableString(message);
//                Linkify.addLinks(s, Linkify.ALL);
//                if (e != null) e.printStackTrace();
//                handlerDogAnimation.removeCallbacks(runnableDogAnimation);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (isRunning()) {
//                            if (profile.getLicenseCode() != null && profile.getLicenseCode().length() > 0) {
//
//                                SweetAlertDialog d = new SweetAlertDialog(SplashActivity.this, SweetAlertDialog.WARNING_TYPE);
//                                d.setTitleText(getString(R.string.invalid_licence));
//                                d.setContentText(message);
//                                d.setConfirmText(getString(R.string.enter_licence_code));
//                                d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                        accountManager.logout();
//                                        goToActivity(LoginActivity.class);
//                                    }
//                                });
//                                d.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                        SplashActivity.this.finish();
//                                    }
//                                });
//                                d.setCancelText(getString(R.string.dialog_close));
//                                d.show();
//                            } else {
//                                goToActivity(LoginActivity.class);
//                            }
//                        }
//                    }
//                });
                isActivatedLicence = false;
                doGetProfile(profile);
            }

            @Override
            public void onSuccess() {
                isActivatedLicence = true;
                doGetProfile(profile);
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
        Intent mainIntent = new Intent(SplashActivity.this, clazz);
        SplashActivity.this.startActivity(mainIntent);
        SplashActivity.this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        if (bp != null) bp.release();
        handlerDogAnimation.removeCallbacks(runnableDogAnimation);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (bp != null && !bp.handleActivityResult(requestCode, resultCode, data))
        super.onActivityResult(requestCode, resultCode, data);
    }
}
