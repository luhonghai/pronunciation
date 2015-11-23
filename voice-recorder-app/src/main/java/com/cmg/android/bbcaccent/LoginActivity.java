package com.cmg.android.bbcaccent;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.cmg.android.bbcaccent.fragment.Preferences;
import com.cmg.android.bbcaccent.subscription.IAPFactory;
import com.cmg.android.bbcaccent.utils.AppLog;
import com.cmg.android.bbcaccent.view.RecordingView;
import com.cmg.android.bbcaccent.auth.AccountManager;
import com.cmg.android.bbcaccent.data.dto.UserProfile;
import com.cmg.android.bbcaccent.utils.AnalyticHelper;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
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
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by luhonghai on 3/17/15.
 */
public class LoginActivity extends BaseActivity implements RecordingView.OnAnimationListener, FacebookCallback<LoginResult>,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private CallbackManager callbackManager;

    @Bind(R.id.loginButton)
    ImageButton btnLoginFB;

    @Bind(R.id.btnLoginAccent)
    ImageButton btnLoginAccent;

    @Bind(R.id.sign_in_button)
    ImageButton btnLoginGGPlus;

    @Bind(R.id.txtAlternative)
    TextView txtAlternative;

    private GoogleApiClient mGoogleApiClient;

    private boolean mIntentInProccess;

    private boolean signedInUser;

    private ConnectionResult mConnectionResult;

    private ProfileTracker profileTracker;

    private AccountManager accountManager;

    private boolean willShowLicenceWarning = false;
    private int alternativeStep = 0;

    private BillingProcessor bp;

    private boolean isSubscription = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bp = IAPFactory.getBillingProcessor(this, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(String productId, TransactionDetails details) {

            }

            @Override
            public void onBillingError(int errorCode, Throwable error) {
            }

            @Override
            public void onBillingInitialized() {
                bp.loadOwnedPurchasesFromGoogle();
                for (IAPFactory.Subscription subscription : IAPFactory.Subscription.values()) {
                    if (bp.isSubscribed(subscription.toString())) {
                        isSubscription = true;
                        break;
                    }
                }
            }

            @Override
            public void onPurchaseHistoryRestored() {

            }
        });
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        accountManager = new AccountManager(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, this);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
            }
        };
        initAuthDialog();
        enableForm(false);
    }

    private Dialog dialogLogin;

    private Dialog dialogRegister;

    private Dialog dialogValidation;

    private Dialog dialogLicense;

    private Dialog dialogResetPassword;

    private SweetAlertDialog dialogProgress;

    private void showProcessDialog() {
        dialogProgress = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialogProgress.setTitleText(getString(R.string.processing));
        dialogProgress.setCancelable(false);
        dialogProgress.show();
    }

    private void hideProcessDialog() {
        if (dialogProgress != null && dialogProgress.isShowing())
            dialogProgress.dismissWithAnimation();
    }

    private void initAuthDialog() {
        // Reset password dialog
        dialogResetPassword = createWhiteDialog(R.layout.dialog_reset_password);
        dialogResetPassword.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfile profile = new UserProfile();
                profile.setUsername(((TextView) dialogResetPassword.findViewById(R.id.txtEmail)).getText().toString());
                if (profile.getUsername().length() > 0) {
                    dialogResetPassword.findViewById(R.id.btnReset).setEnabled(false);
                    showProcessDialog();
                    doResetPassword(profile);
                } else {
                    SweetAlertDialog d = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE);
                    d.setTitleText(getString(R.string.missing_email_address));
                    d.setContentText(getString(R.string.please_enter_email));
                    d.setConfirmText(getString(R.string.dialog_ok));
                    d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });
                    d.show();
                }
            }
        });
        ((TextView)dialogResetPassword.findViewById(R.id.txtTermAndCondition)).setMovementMethod(LinkMovementMethod.getInstance());

        // Login dialog
        dialogLogin = createWhiteDialog(R.layout.dialog_login);
        dialogLogin.findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfile profile = new UserProfile();
                profile.setLoginType(UserProfile.TYPE_EASYACCENT);
                profile.setUsername(((TextView) dialogLogin.findViewById(R.id.txtEmail)).getText().toString());
                profile.setPassword(((TextView) dialogLogin.findViewById(R.id.txtPassword)).getText().toString());
                if (profile.getUsername().length() > 0 && profile.getPassword().length() > 0) {
                    dialogLogin.findViewById(R.id.btnLogin).setEnabled(false);
                    showProcessDialog();
                    doAuth(profile);
                } else {
                    SweetAlertDialog d = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE);
                    d.setTitleText(getString(R.string.missing_data));
                    d.setContentText(getString(R.string.please_enter_email_and_password));
                    d.setConfirmText(getString(R.string.dialog_ok));
                    d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });
                    d.show();
                }
            }
        });
        dialogLogin.findViewById(R.id.txtLostPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dialogResetPassword.isShowing() && isRunning()) {
                    ((TextView) dialogResetPassword.findViewById(R.id.txtEmail)).setText(((TextView) dialogLogin.findViewById(R.id.txtEmail)).getText());
                    dialogResetPassword.show();
                }
            }
        });
        dialogLogin.findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkNetwork(false)) {
                    SimpleAppLog.info("show register dialog");
                    dialogRegister.show();
                }
            }
        });
        ((TextView)dialogLogin.findViewById(R.id.txtTermAndCondition)).setMovementMethod(LinkMovementMethod.getInstance());

        // License dialog
        dialogLicense = createWhiteDialog(R.layout.dialog_license);
        dialogLicense.setCancelable(false);
        dialogLicense.setCanceledOnTouchOutside(false);
        dialogLicense.findViewById(R.id.btnActivateLicense).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UserProfile profile = Preferences.getCurrentProfile(LoginActivity.this);
                        if (profile != null) {
                            profile.setLicenseCode(((TextView) dialogLicense.findViewById(R.id.txtCode)).getText().toString());
                            SimpleAppLog.error("Start active license: " + profile.getLicenseCode());
                            if (profile.getLicenseCode().length() > 0) {
                                dialogLicense.findViewById(R.id.btnActivateLicense).setEnabled(false);
                                showProcessDialog();
                                doActivateLicense(profile);
                            } else {
                                SweetAlertDialog d = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE);
                                d.setTitleText(getString(R.string.missing_licence_code));
                                d.setContentText(getString(R.string.please_enter_licence_code));
                                d.setConfirmText(getString(R.string.dialog_ok));
                                d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                });
                                d.show();
                            }
                        } else {
                            SimpleAppLog.error("No profile found");
                        }
                    }
                });

            }
        });
        dialogLicense.findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountManager.logout();
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    mGoogleApiClient.connect();
                }
                dialogLicense.cancel();
            }
        });

//        dialogLicense.findViewById(R.id.btnExit).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LoginActivity.this.finish();
//            }
//        });

        ((TextView)dialogLicense.findViewById(R.id.txtTermAndCondition)).setMovementMethod(LinkMovementMethod.getInstance());

        dialogRegister = createWhiteDialog(R.layout.dialog_register);
        dialogRegister.findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRegister();
            }
        });
        ((TextView)dialogRegister.findViewById(R.id.txtTermAndCondition)).setMovementMethod(LinkMovementMethod.getInstance());

        dialogValidation = createWhiteDialog(R.layout.dialog_validation);
        dialogValidation.findViewById(R.id.btnConfirmCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogValidation.findViewById(R.id.btnConfirmCode).setEnabled(false);
                showProcessDialog();
                UserProfile profile = Preferences.getCurrentProfile(LoginActivity.this);
                accountManager.submitActivationCode(
                        ((TextView) dialogValidation.findViewById(R.id.txtCode)).getText().toString().trim(),
                        profile,
                        new AccountManager.AuthListener() {
                            @Override
                            public void onError(final String message, Throwable e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialogProgress.dismissWithAnimation();
                                        if (message.equalsIgnoreCase(getString(R.string.activated_success_title))) {
                                            dialogValidation.findViewById(R.id.btnConfirmCode).setEnabled(true);
                                            dialogValidation.cancel();
                                            dialogLogin.show();

                                            SweetAlertDialog d = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                            d.setTitleText(getString(R.string.validation_successful));
                                            d.setContentText(getString(R.string.validation_success_message));
                                            d.setConfirmText(getString(R.string.dialog_ok));
                                            d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismissWithAnimation();
                                                }
                                            });
                                            d.show();

                                        } else {
                                            SweetAlertDialog d = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                                            d.setTitleText(getString(R.string.validation_failed));
                                            d.setContentText(message);
                                            d.setConfirmText(getString(R.string.dialog_ok));
                                            d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismissWithAnimation();
                                                }
                                            });
                                            d.show();
                                        }
                                        dialogValidation.findViewById(R.id.btnConfirmCode).setEnabled(true);
                                    }
                                });
                            }

                            @Override
                            public void onSuccess() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialogProgress.dismissWithAnimation();
                                        dialogValidation.findViewById(R.id.btnConfirmCode).setEnabled(true);
                                        dialogValidation.cancel();
                                        dialogLogin.show();
                                        SweetAlertDialog d = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                        d.setTitleText(getString(R.string.validation_successful));
                                        d.setContentText(getString(R.string.validation_success_message));
                                        d.setConfirmText(getString(R.string.dialog_ok));
                                        d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();
                                            }
                                        });
                                        d.show();
                                    }
                                });

                            }
                        });

            }
        });


        dialogValidation.findViewById(R.id.btnSendCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogValidation.findViewById(R.id.btnSendCode).setEnabled(false);
                showProcessDialog();
                final UserProfile profile = Preferences.getCurrentProfile(LoginActivity.this);
                accountManager.resendActivationCode(profile,
                        new AccountManager.AuthListener() {
                            @Override
                            public void onError(final String message, Throwable e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialogProgress.dismissWithAnimation();
                                        dialogValidation.findViewById(R.id.btnSendCode).setEnabled(true);

                                        SweetAlertDialog d = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                                        d.setTitleText(getString(R.string.could_not_send_code));
                                        d.setContentText(message);
                                        d.setConfirmText(getString(R.string.dialog_ok));
                                        d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();
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
                                        dialogProgress.dismissWithAnimation();
                                        dialogValidation.findViewById(R.id.btnSendCode).setEnabled(true);

                                        SweetAlertDialog d = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                        d.setTitleText(getString(R.string.successfully_submitted));
                                        d.setContentText(getString(R.string.please_check_your_message_at_email_address, profile.getUsername()));
                                        d.setConfirmText(getString(R.string.dialog_ok));
                                        d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();
                                            }
                                        });
                                        d.show();
                                    }
                                });
                            }
                        });
            }
        });

        dialogValidation.findViewById(R.id.btnLoginAccent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogValidation.cancel();
                dialogLogin.show();
            }
        });

        dialogValidation.findViewById(R.id.btnChangeEmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogValidation.cancel();
                dialogRegister.show();
            }
        });
        ((TextView)dialogValidation.findViewById(R.id.txtTermAndCondition)).setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void doResetPassword(final UserProfile profile) {
        accountManager.resetPassword(profile,
                new AccountManager.AuthListener() {
                    @Override
                    public void onError(final String message, Throwable e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialogProgress.dismissWithAnimation();
                                dialogResetPassword.findViewById(R.id.btnReset).setEnabled(true);
                                SweetAlertDialog d = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                                d.setTitleText(getString(R.string.could_not_send_request));
                                d.setContentText(message);
                                d.setConfirmText(getString(R.string.dialog_ok));
                                d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
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
                                dialogProgress.dismissWithAnimation();
                                dialogResetPassword.findViewById(R.id.btnReset).setEnabled(true);

                                SweetAlertDialog d = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                d.setTitleText(getString(R.string.successfully_submitted));
                                d.setContentText(getString(R.string.please_check_your_message_at_email_address, profile.getUsername()));
                                d.setConfirmText(getString(R.string.dialog_ok));
                                d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                });
                                d.show();
                            }
                        });
                    }
                });
    }

    private void doActivateLicense(final UserProfile profile) {
        accountManager.activeLicense(profile, new AccountManager.AuthListener() {
            @Override
            public void onError(final String message, Throwable e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialogProgress.dismissWithAnimation();
                        dialogLicense.findViewById(R.id.btnActivateLicense).setEnabled(true);
                        SweetAlertDialog d = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                        d.setTitleText(getString(R.string.could_not_activate_licence_code));
                        d.setContentText(message);
                        d.setConfirmText(getString(R.string.dialog_ok));
                        d.show();
                    }
                });
            }

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialogLicense.findViewById(R.id.btnActivateLicense).setEnabled(true);
                        doCheckLicense(profile);
                    }
                });
            }
        });
    }

    private void doCheckLicense(final UserProfile profile) {
        accountManager.checkLicense(profile, new AccountManager.AuthListener() {
            @Override
            public void onError(final String message, Throwable e) {
//                if (e != null)
//                    e.printStackTrace();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        dialogProgress.dismissWithAnimation();
//                        dialogLicense.findViewById(R.id.btnActivateLicense).setEnabled(true);
//                        if (profile.isLogin() && isRunning()) {
//                            if (!dialogLicense.isShowing())
//                                dialogLicense.show();
//                        }
//                    }
//                });
                profile.setIsActivatedLicence(false);
                Preferences.addProfile(LoginActivity.this, profile);
                doGetProfile(profile);
            }

            @Override
            public void onSuccess() {
                profile.setIsActivatedLicence(true);
                Preferences.addProfile(LoginActivity.this, profile);
                doGetProfile(profile);
            }
        });
    }

    private void doGetProfile(final UserProfile profile) {
        accountManager.getProfile(profile, new AccountManager.AuthListener() {
            @Override
            public void onError(final String message, Throwable e) {
                AnalyticHelper.sendUserLoginError(LoginActivity.this, profile.getUsername());
                final SpannableString s = new SpannableString(message);
                Linkify.addLinks(s, Linkify.ALL);
                if (e != null) e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialogProgress.dismissWithAnimation();
                        if (isRunning()) {
                            SweetAlertDialog d = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                            d.setTitleText(getString(R.string.could_not_fetch_profile));
                            d.setCancelText(getString(R.string.dialog_close));
                            d.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    LoginActivity.this.finish();
                                }
                            });
                            d.setContentText(message);
                            d.setConfirmText(getString(R.string.logout));
                            d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    accountManager.logout();
                                    if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                                        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                                        mGoogleApiClient.disconnect();
                                        mGoogleApiClient.connect();
                                    }
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });
                            d.show();
                        }
                    }
                });
            }

            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialogProgress.dismissWithAnimation();
                    }
                });
                profile.setIsSubscription(isSubscription);
                Preferences.updateProfile(LoginActivity.this, profile);
                //call services sync data here DENP-238
                startMainActivity();
            }
        });
    }

    private void doRegister() {
        final UserProfile profile = new UserProfile();
        profile.setUsername(((TextView) dialogRegister.findViewById(R.id.txtEmail)).getText().toString());
        profile.setFirstName(((TextView) dialogRegister.findViewById(R.id.txtFirstname)).getText().toString());
        profile.setLastName(((TextView) dialogRegister.findViewById(R.id.txtLastname)).getText().toString());
        profile.setName(profile.getFirstName() + " " + profile.getLastName());
        String p1 = ((TextView) dialogRegister.findViewById(R.id.txtPassword)).getText().toString();
        String p2 = ((TextView) dialogRegister.findViewById(R.id.txtCPassword)).getText().toString();
        if (isValidEmail(profile.getUsername())) {
            if (p1.length() >= 6 && p2.length() >= 6) {
                if (p1.equals(p2)) {
                    profile.setPassword(p1);
                    profile.setLoginType(UserProfile.TYPE_EASYACCENT);
                    dialogRegister.findViewById(R.id.btnRegister).setEnabled(false);
                    showProcessDialog();
                    accountManager.register(profile, new AccountManager.AuthListener() {
                        @Override
                        public void onError(final String message, Throwable e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialogProgress.dismissWithAnimation();
                                    dialogRegister.findViewById(R.id.btnRegister).setEnabled(true);

                                    SweetAlertDialog d = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                                    d.setTitleText(getString(R.string.could_not_register));
                                    d.setContentText(message);
                                    d.setConfirmText(getString(R.string.dialog_ok));
                                    d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
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
                                    Preferences.addProfile(LoginActivity.this, profile);
                                    Preferences.setSelectedUsername(profile.getUsername(), LoginActivity.this);
                                    dialogRegister.findViewById(R.id.btnRegister).setEnabled(true);
                                    dialogRegister.cancel();
                                    dialogProgress.dismissWithAnimation();
                                    ((TextView) dialogValidation.findViewById(R.id.txtCode)).setHint(profile.getUsername());
                                    dialogValidation.show();
                                }
                            });

                        }
                    });
                } else {
                    SweetAlertDialog d = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                    d.setTitleText(getString(R.string.invalid_password));
                    d.setContentText(getString(R.string.both_passwords_must_match));
                    d.setConfirmText(getString(R.string.dialog_ok));
                    d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            ((TextView) dialogRegister.findViewById(R.id.txtPassword)).setText("");
                            ((TextView) dialogRegister.findViewById(R.id.txtCPassword)).setText("");
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });
                    d.show();

                }
            } else {
                SweetAlertDialog d = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                d.setTitleText(getString(R.string.invalid_password));
                d.setContentText(getString(R.string.error_password_length));
                d.setConfirmText(getString(R.string.dialog_ok));
                d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        ((TextView) dialogRegister.findViewById(R.id.txtPassword)).setText("");
                        ((TextView) dialogRegister.findViewById(R.id.txtCPassword)).setText("");
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
                d.show();
            }
        } else {
            SweetAlertDialog d = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            d.setTitleText(getString(R.string.invalid_email_address));
            d.setContentText(getString(R.string.error_email_message));
            d.setConfirmText(getString(R.string.dialog_ok));
            d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
            d.show();
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
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
        if (mConnectionResult != null && mConnectionResult.hasResolution()) {

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
        if (bp != null) bp.release();
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
    public void onSuccess(final LoginResult loginResult) {

        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            AppLog.logString(object.toString());
                            final UserProfile profile = new UserProfile();
                            profile.setAdditionalToken(loginResult.getAccessToken().getToken());
                            SimpleAppLog.debug("Facebook access token: " + profile.getAdditionalToken());
                            try {
                                profile.setUsername(object.getString("email"));
                            } catch (JSONException e) {

                            }
                            try {
                                profile.setGender(object.getString("gender").equalsIgnoreCase("male"));
                            } catch (JSONException e) {

                            }
                            try {
                                String birthDay = object.getString("birthday");
                                SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                profile.setDob(sdf2.format(sdf1.parse(birthDay)));
                            } catch (Exception e) {

                            }
                            try {
                                profile.setName(new String(object.getString("name").getBytes(), "UTF-8"));
                            } catch (JSONException e) {

                            }
                            try {
                                profile.setProfileImage("https://graph.facebook.com/"+object.getString("id")+"/picture?width=320&height=320&type=square");
                            } catch (JSONException e) {

                            }
                            profile.setLoginType(UserProfile.TYPE_FACEBOOK);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    enableForm(true);
                                    // showProcessDialog();
                                }
                            });
                            accountManager.register(profile, new AccountManager.AuthListener() {
                                @Override
                                public void onError(String message, Throwable e) {
                                    SimpleAppLog.error("could not register account " + profile.getUsername() + ". Message: " + message,e);
                                    doAuth(profile);
                                }

                                @Override
                                public void onSuccess() {
                                    doAuth(profile);
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    enableForm(true);
                                    hideProcessDialog();
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
        enableForm(true);
        hideProcessDialog();
        //Toast.makeText(this, "Cancel login", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(FacebookException e) {
        enableForm(true);
        hideProcessDialog();
        //Toast.makeText(this, "Could not login to Facebook", Toast.LENGTH_LONG).show();
        showErrorNetworkMessage(null);
    }

    private void doAuth(final UserProfile profile) {
        //Toast.makeText(this, "Login with " + profile.getUsername(), Toast.LENGTH_LONG).show();
        Preferences.setSelectedUsername(profile.getUsername(), LoginActivity.this);
        Preferences.addProfile(this, profile);
        final UserProfile currentProfile = Preferences.getCurrentProfile(this);
        if (currentProfile == null) {
            dialogLogin.findViewById(R.id.btnLogin).setEnabled(true);
            dialogProgress.dismissWithAnimation();
            SweetAlertDialog d = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
            d.setTitleText(getString(R.string.error));
            d.setContentText(getString(R.string.error_create_profile_message));
            d.setConfirmText(getString(R.string.dialog_ok));
            d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    LoginActivity.this.finish();
                }
            });
            d.show();
        } else {
            if (!profile.getLoginType().equalsIgnoreCase(UserProfile.TYPE_EASYACCENT)) {
                accountManager.register(profile, null);
            }
            accountManager.auth(profile, new AccountManager.AuthListener() {
                @Override
                public void onError(final String message, Throwable e) {
                    AnalyticHelper.sendUserLoginError(LoginActivity.this, profile.getUsername());
                    final SpannableString s = new SpannableString(message);
                    Linkify.addLinks(s, Linkify.ALL);
                    if (e != null) e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialogLogin.findViewById(R.id.btnLogin).setEnabled(true);
                            dialogProgress.dismissWithAnimation();
                            accountManager.logout();
                            if (mGoogleApiClient.isConnected()) {
                                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                                mGoogleApiClient.disconnect();
                                mGoogleApiClient.connect();
                            }
                            final SweetAlertDialog d = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                            d.setTitleText(getString(R.string.could_not_login));
                            d.setContentText(message);
                            d.setConfirmText(getString(R.string.dialog_ok));
                            d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });
                            d.show();

                        }
                    });

                }
                @Override
                public void onSuccess() {
                    currentProfile.setIsLogin(true);
                    Preferences.addProfile(LoginActivity.this, currentProfile);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //dialogProgress.dismissWithAnimation();
                            dialogLogin.findViewById(R.id.btnLogin).setEnabled(true);
                            if (dialogLogin.isShowing()) {
                                dialogLogin.cancel();
                            }
                            //showProcessDialog();
                        }
                    });
                    AnalyticHelper.sendLoginType(LoginActivity.this, profile.getLoginType());
                    AnalyticHelper.sendUserLogin(LoginActivity.this, profile.getUsername());
                    doCheckLicense(currentProfile);
                }
            });
        }


    }

    private void startActivity(Class clazz) {
        Intent mainIntent = new Intent(LoginActivity.this,clazz);
        LoginActivity.this.startActivity(mainIntent);
        LoginActivity.this.finish();
    }

    private void startMainActivity() {
        startActivity(MainActivity.class);
    }

    @Override
    public void onConnected(Bundle bundle) {
        new AsyncTask<Void,Void,Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                final UserProfile profile = new UserProfile();
                String scope = "oauth2:" + Scopes.PLUS_LOGIN + " " + "https://www.googleapis.com/auth/userinfo.email" + " https://www.googleapis.com/auth/plus.profile.agerange.read";
                try {
                    String token = GoogleAuthUtil.getToken(LoginActivity.this,
                            Plus.AccountApi.getAccountName(mGoogleApiClient), scope);
                    profile.setAdditionalToken(token);
                } catch (Exception e) {
                    SimpleAppLog.error("Could not get additional token",e);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        enableForm(true);
                    }
                });
                SimpleAppLog.debug("Google+ access token: " + profile.getAdditionalToken());
                signedInUser = false;
                Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                if (person != null) {
                    profile.setUsername(Plus.AccountApi.getAccountName(mGoogleApiClient));
                    profile.setDob(person.getBirthday());
                    String imageUrl = person.getImage().getUrl();
                    if (imageUrl.contains("?sz=")) {
                        imageUrl = imageUrl.substring(0, imageUrl.lastIndexOf("?sz=")) + "?sz=320";
                    }
                    SimpleAppLog.debug("Google plus avatar " + imageUrl);
                    profile.setProfileImage(imageUrl);
                    profile.setName(person.getDisplayName());
                    profile.setGender(person.getGender() == 1);
                    profile.setLoginType(UserProfile.TYPE_GOOGLE_PLUS);
                    //showProcessDialog();
                    accountManager.register(profile, new AccountManager.AuthListener() {
                        @Override
                        public void onError(String message, Throwable e) {
                            SimpleAppLog.error("could not register account " + profile.getUsername() + ". Message: " + message,e);
                            doAuth(profile);
                        }

                        @Override
                        public void onSuccess() {
                            doAuth(profile);
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideProcessDialog();
                        }
                    });
                }
                return null;
            }
        }.execute();
    }



    @Override
    public void onConnectionSuspended(int i) {
        SimpleAppLog.info("Connection Suspended. Code: " + i);
        showErrorNetworkMessage(null);
        mGoogleApiClient.connect();
    }

    private void googlePlusLogin() {
        if (!mGoogleApiClient.isConnecting()) {
            signedInUser = true;
            resolveSignInError();
        } else {
            enableForm(true);
            hideProcessDialog();
        }
    }
    @OnClick(R.id.sign_in_button)
    public void signinGooglePlus() {
        if (checkNetwork(false)) {
            enableForm(false);
            googlePlusLogin();
        }
    }

    @OnClick(R.id.loginButton)
    public void signinFacebook() {
        if (checkNetwork(false)) {
            enableForm(false);
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_birthday"));
        }
    }

    @OnClick(R.id.btnLoginAccent)
    public void signinAccenteasy() {
        if (checkNetwork(false)) {
            SimpleAppLog.info("show login dialog");
            dialogLogin.show();
        }
    }

    @OnClick(R.id.txtAlternative)
    public void doAlternativeLogin() {
        switch (alternativeStep) {
            case 0:
                btnLoginFB.setVisibility(View.VISIBLE);
                break;
            case 1:
                btnLoginAccent.setVisibility(View.VISIBLE);
                txtAlternative.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
        alternativeStep++;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        SimpleAppLog.info("onActivityResult. requestCode: " + requestCode + ". resultCode: " + resultCode);
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
        if (!mIntentInProccess) {
            hideProcessDialog();
        }
        SimpleAppLog.info("Could not connect to Google plus. Error code: " + connectionResult.getErrorCode()
                + ". Read more at: http://developer.android.com/reference/com/google/android/gms/common/ConnectionResult.html");
        if (connectionResult.getErrorCode() == ConnectionResult.NETWORK_ERROR) {
            showErrorNetworkMessage(null);
        }

        if (!connectionResult.hasResolution()) {
            // Skip error message
            //GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
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
        if (enable) {

        } else {
            showProcessDialog();
        }
        btnLoginGGPlus.setEnabled(enable);
        btnLoginFB.setEnabled(enable);
        txtAlternative.setEnabled(enable);
        btnLoginAccent.setEnabled(enable);
    }
}
