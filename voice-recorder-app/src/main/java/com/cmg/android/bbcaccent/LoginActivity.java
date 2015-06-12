package com.cmg.android.bbcaccent;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
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

import com.cmg.android.bbcaccent.activity.BaseActivity;
import com.cmg.android.bbcaccent.activity.fragment.Preferences;
import com.cmg.android.bbcaccent.activity.view.RecordingView;
import com.cmg.android.bbcaccent.auth.AccountManager;
import com.cmg.android.bbcaccent.data.UserProfile;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by luhonghai on 3/17/15.
 */
public class LoginActivity extends BaseActivity implements RecordingView.OnAnimationListener, FacebookCallback<LoginResult>,
        GoogleApiClient.ConnectionCallbacks,
        View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    private  CallbackManager callbackManager;

    private ImageButton btnLoginFB;

    private ImageButton btnLoginAccent;

    private ImageButton btnRegister;

    private GoogleApiClient mGoogleApiClient;

    private boolean mIntentInProccess;

    private boolean signedInUser;

    private ConnectionResult mConnectionResult;

    private ImageButton btnLoginGGPlus;

    private ProfileTracker profileTracker;

    private AccountManager accountManager;

    private boolean willShowLicenceWarning = false;

    private SweetAlertDialog dialogProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountManager = new AccountManager(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, this);
        setContentView(R.layout.login);

        (btnLoginFB = (ImageButton) findViewById(R.id.loginButton)).setOnClickListener(this);
        (btnLoginGGPlus = (ImageButton) findViewById(R.id.sign_in_button)).setOnClickListener(this);
        (btnLoginAccent = (ImageButton) findViewById(R.id.btnLoginAccent)).setOnClickListener(this);
        (btnRegister = (ImageButton) findViewById(R.id.btnRegister)).setOnClickListener(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();


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

    private void showProcessDialog() {
        dialogProgress = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialogProgress.setTitleText("processing");
        dialogProgress.setCancelable(false);
        dialogProgress.show();
    }

    private void initAuthDialog() {
        // Reset password dialog
        dialogResetPassword = new Dialog(this, R.style.Theme_WhiteDialog);
        prepareDialog(dialogResetPassword);
        dialogResetPassword.setContentView(R.layout.dialog_reset_password);
        initDialog(dialogResetPassword);
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
                    d.setTitleText("Missing email address");
                    d.setContentText("Please enter email");
                    d.setConfirmText("Ok");
                    d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });
                    d.show();
//                    new AlertDialog.Builder(LoginActivity.this).setTitle(null)
//                            .setMessage("Please enter email")
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            }).show();
                }
            }
        });
        ((TextView)dialogResetPassword.findViewById(R.id.txtTermAndCondition)).setMovementMethod(LinkMovementMethod.getInstance());

        // Login dialog
        dialogLogin = new Dialog(this, R.style.Theme_WhiteDialog);
        prepareDialog(dialogLogin);
        dialogLogin.setContentView(R.layout.dialog_login);
        initDialog(dialogLogin);
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
                    d.setTitleText("Missing data");
                    d.setContentText("Please enter email and password");
                    d.setConfirmText("Ok");
                    d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });
                    d.show();
//                    new AlertDialog.Builder(LoginActivity.this).setTitle(null)
//                            .setMessage("Please enter email and password")
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            }).show();
                }
            }
        });
        ((TextView)dialogLogin.findViewById(R.id.txtLostPassword)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dialogResetPassword.isShowing() && isRunning()) {
                    ((TextView) dialogResetPassword.findViewById(R.id.txtEmail)).setText(((TextView) dialogLogin.findViewById(R.id.txtEmail)).getText());
                    dialogResetPassword.show();
                }
            }
        });
        ((TextView)dialogLogin.findViewById(R.id.txtTermAndCondition)).setMovementMethod(LinkMovementMethod.getInstance());

        // License dialog
        dialogLicense = new Dialog(this, R.style.Theme_WhiteDialog);
        prepareDialog(dialogLicense);
        dialogLicense.setContentView(R.layout.dialog_license);
        initDialog(dialogLicense);
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
                                d.setTitleText("Missing licence code");
                                d.setContentText("Please enter licence code");
                                d.setConfirmText("Ok");
                                d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                });
                                d.show();
//                                new AlertDialog.Builder(LoginActivity.this).setTitle(null)
//                                        .setMessage("Please enter licence code")
//                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                dialog.dismiss();
//                                            }
//                                        }).show();
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

        dialogRegister = new Dialog(this, R.style.Theme_WhiteDialog);
        prepareDialog(dialogRegister);
        dialogRegister.setContentView(R.layout.dialog_register);
        initDialog(dialogRegister);
        dialogRegister.findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRegister();
            }
        });
        ((TextView)dialogRegister.findViewById(R.id.txtTermAndCondition)).setMovementMethod(LinkMovementMethod.getInstance());

        dialogValidation = new Dialog(this, R.style.Theme_WhiteDialog);
        prepareDialog(dialogValidation);
        dialogValidation.setContentView(R.layout.dialog_validation);
        initDialog(dialogValidation);
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
                                        if (message.equalsIgnoreCase("Your account has already been activated")) {
                                            dialogValidation.findViewById(R.id.btnConfirmCode).setEnabled(true);
                                            dialogValidation.cancel();
                                            dialogLogin.show();

                                            SweetAlertDialog d = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                            d.setTitleText("Validation successful");
                                            d.setContentText("Your account has already been activated.\n" +
                                                    "Please login with your email and password");
                                            d.setConfirmText("Ok");
                                            d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismissWithAnimation();
                                                }
                                            });
                                            d.show();

//                                            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this)
//                                                    .setTitle("Validation successful")
//                                                    .setMessage("Your account has already been activated.\nPlease login with your email and password")
//                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(DialogInterface dialog, int which) {
//                                                            dialog.dismiss();
//                                                        }
//                                                    })
//                                                    .create();
//                                            alertDialog.show();
                                        } else {
                                            SweetAlertDialog d = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                                            d.setTitleText("Validation failed");
                                            d.setContentText(message);
                                            d.setConfirmText("Ok");
                                            d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    sweetAlertDialog.dismissWithAnimation();
                                                }
                                            });
                                            d.show();
//                                            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this)
//                                                    .setTitle("Validation failed")
//                                                    .setMessage(message)
//                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                                        @Override
//                                                        public void onClick(DialogInterface dialog, int which) {
//                                                            dialog.dismiss();
//                                                        }
//                                                    })
//                                                    .create();
//                                            alertDialog.show();

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
                                        d.setTitleText("Validation successful");
                                        d.setContentText("Please login with your email and password");
                                        d.setConfirmText("Ok");
                                        d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();
                                            }
                                        });
                                        d.show();
//                                        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this)
//                                                .setTitle("Validation successful")
//                                                .setMessage("Please login with your email and password")
//                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        dialog.dismiss();
//                                                    }
//                                                })
//                                                .create();
//                                        alertDialog.show();
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
                                        d.setTitleText("Could not send code");
                                        d.setContentText(message);
                                        d.setConfirmText("Ok");
                                        d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();
                                            }
                                        });
                                        d.show();

//                                        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this)
//                                                .setTitle("Could not send code")
//                                                .setMessage(message)
//                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        dialog.dismiss();
//                                                    }
//                                                })
//                                                .create();
//                                        alertDialog.show();
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
                                        d.setTitleText("Successfully submitted");
                                        d.setContentText("Please check message in your email " + profile.getUsername());
                                        d.setConfirmText("Ok");
                                        d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();
                                            }
                                        });
                                        d.show();

//                                        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this)
//                                                .setTitle("Successfully sent")
//                                                .setMessage("Please check message in your email " + profile.getUsername())
//                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        dialog.dismiss();
//                                                    }
//                                                })
//                                                .create();
//                                        alertDialog.show();
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
                                d.setTitleText("Could not send request");
                                d.setContentText(message);
                                d.setConfirmText("Ok");
                                d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                });
                                d.show();

//                                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this)
//                                        .setTitle("Could not send request")
//                                        .setMessage(message)
//                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                dialog.dismiss();
//                                            }
//                                        })
//                                        .create();
//                                alertDialog.show();
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
                                d.setTitleText("Successfully submitted");
                                d.setContentText("Please check message in your email " + profile.getUsername());
                                d.setConfirmText("Ok");
                                d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    }
                                });
                                d.show();

//                                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this)
//                                        .setTitle("Successfully sumitted")
//                                        .setMessage("Please check message in your email " + profile.getUsername())
//                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                dialog.dismiss();
//                                            }
//                                        })
//                                        .create();
//                                alertDialog.show();
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
                        d.setTitleText("Could not activate licence code");
                        d.setContentText(message);
                        d.setConfirmText("Ok");
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
                if (e != null)
                    e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialogProgress.dismissWithAnimation();
                        dialogLicense.findViewById(R.id.btnActivateLicense).setEnabled(true);
                        if (profile.isLogin() && isRunning()) {
                            if (!dialogLicense.isShowing())
                                dialogLicense.show();
//                            AlertDialog d = new AlertDialog.Builder(LoginActivity.this)
//                                    .setTitle("Invalid licence code")
//                                    .setMessage(message + "\n Please enter new licence code!")
//                                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            dialog.dismiss();
//                                        }
//                                    }).create();
//                            d.show();
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
                Preferences.addProfile(LoginActivity.this, profile);
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
                                        d.setTitleText("Could not register");
                                        d.setContentText(message);
                                        d.setConfirmText("Ok");
                                        d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismissWithAnimation();
                                            }
                                        });
                                        d.show();

//                                        AlertDialog d = new AlertDialog.Builder(LoginActivity.this)
//                                                .setTitle("Could not register")
//                                                .setMessage(message)
//                                                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        dialog.dismiss();
//                                                    }
//                                                }).create();
//                                        d.show();
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
                        d.setTitleText("Invalid password");
                        d.setContentText("Both passwords must match");
                        d.setConfirmText("Ok");
                        d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                ((TextView) dialogRegister.findViewById(R.id.txtPassword)).setText("");
                                ((TextView) dialogRegister.findViewById(R.id.txtCPassword)).setText("");
                                sweetAlertDialog.dismissWithAnimation();
                            }
                        });
                        d.show();
//                        new AlertDialog.Builder(this).setTitle("Invalid password")
//                                .setMessage("Both passwords must match")
//                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        ((TextView) dialogRegister.findViewById(R.id.txtPassword)).setText("");
//                                        ((TextView) dialogRegister.findViewById(R.id.txtCPassword)).setText("");
//                                        dialog.dismiss();
//                                    }
//                                }).show();

                    }
            } else {
                SweetAlertDialog d = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                d.setTitleText("Invalid password");
                d.setContentText("Passwords must be at least 6 characters in length");
                d.setConfirmText("Ok");
                d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        ((TextView) dialogRegister.findViewById(R.id.txtPassword)).setText("");
                        ((TextView) dialogRegister.findViewById(R.id.txtCPassword)).setText("");
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
                d.show();
//                new AlertDialog.Builder(this).setTitle("Invalid password")
//                        .setMessage("Passwords must be at least 6 characters in length")
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        ((TextView) dialogRegister.findViewById(R.id.txtPassword)).setText("");
//                        ((TextView) dialogRegister.findViewById(R.id.txtCPassword)).setText("");
//                        dialog.dismiss();
//                    }
//                }).show();
            }
        } else {
            SweetAlertDialog d = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            d.setTitleText("Invalid email address");
            d.setContentText("Please enter a valid email address");
            d.setConfirmText("Ok");
            d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
            d.show();
//            new AlertDialog.Builder(this).setTitle("Invalid email address")
//                    .setMessage("Please enter a valid email address")
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    }).show();
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void prepareDialog(final Dialog dialog) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setTitle(null);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
    }

    private void initDialog(final Dialog dialog) {
        int fullWidth;
        int fulHeight;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Display display = this.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            fullWidth = size.x;
            fulHeight = size.y;
        } else {
            Display display = this.getWindowManager().getDefaultDisplay();
            fullWidth = display.getWidth();
            fulHeight = display.getHeight();
        }

        final int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources()
                .getDisplayMetrics());

        int w = fullWidth - padding;
        //int h = dialog.getWindow().getAttributes().height;
        int h = fulHeight - padding;
        dialog.getWindow().setLayout(w, h);

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
                                String birthDay = object.getString("birthday");
                                SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy");
                                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
                                profile.setDob(sdf2.format(sdf1.parse(birthDay)));
                            } catch (Exception e) {

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
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    enableForm(true);
                                    showProcessDialog();
                                }
                            });
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
        enableForm(true);
        //Toast.makeText(this, "Cancel login", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(FacebookException e) {
        enableForm(true);
        //Toast.makeText(this, "Could not login to Facebook", Toast.LENGTH_LONG).show();
        SweetAlertDialog d = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
        d.setTitleText("Could not login");
        d.setContentText("Sorry but your Internet connection does not appear to be working");
        d.setConfirmText("Ok");
        d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
            }
        });
        d.show();
//        AlertDialog d = new AlertDialog.Builder(LoginActivity.this)
//                .setTitle("Could not login")
//                .setMessage("Sorry but your Internet connection does not appear to be working")
//                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }).create();
//        d.show();
        ((TextView)d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
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
            d.setTitleText("Error");
            d.setContentText("Could not create new profile");
            d.setConfirmText("Ok");
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

                            final SweetAlertDialog d = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                            d.setTitleText("Could not login");
                            d.setContentText(message);
                            d.setConfirmText("Ok");
                            d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    if (profile.getLoginType().equalsIgnoreCase(UserProfile.TYPE_EASYACCENT)) {
                                        sweetAlertDialog.dismissWithAnimation();
                                    } else {
                                        LoginActivity.this.finish();
                                    }
                                }
                            });
                            d.show();

//                            AlertDialog d = new AlertDialog.Builder(LoginActivity.this)
//                                    .setTitle("Could not login")
//                                    .setMessage(s)
//                                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            if (profile.getLoginType().equalsIgnoreCase(UserProfile.TYPE_EASYACCENT)) {
//                                                dialog.dismiss();
//                                            } else {
//                                                LoginActivity.this.finish();
//                                            }
//                                        }
//                                    }).create();
//                            d.show();
//                            ((TextView)d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
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
            showProcessDialog();
            doAuth(profile);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        SimpleAppLog.info("Connection Suspended. Code: " + i);
        SweetAlertDialog d = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
        d.setTitleText("Could not login");
        d.setContentText("Sorry but your Internet connection does not appear to be working");
        d.setConfirmText("Ok");
        d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();
            }
        });
        d.show();
//        AlertDialog d = new AlertDialog.Builder(LoginActivity.this)
//                .setTitle("Could not login")
//                .setMessage("Sorry but your Internet connection does not appear to be working")
//                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }).create();
//        d.show();
        ((TextView)d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
        mGoogleApiClient.connect();
    }

    private void googlePlusLogin() {
        if (!mGoogleApiClient.isConnecting()) {
            signedInUser = true;
            resolveSignInError();
        } else {
            enableForm(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                if (checkNetwork(false)) {
                    enableForm(false);
                    googlePlusLogin();
                }
                break;
            case R.id.loginButton:
                if (checkNetwork(false)) {
                    enableForm(false);
                    LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_birthday"));
                }
                break;
            case R.id.btnLoginAccent:
                if (checkNetwork(false)) {
                    SimpleAppLog.info("show login dialog");
                    dialogLogin.show();
                }
                break;
            case R.id.btnRegister:
                if (checkNetwork(false)) {
                    SimpleAppLog.info("show register dialog");
                    dialogRegister.show();
                }
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
        SimpleAppLog.info("Could not connect to Google plus. Error code: " + connectionResult.getErrorCode()
                + ". Read more at: http://developer.android.com/reference/com/google/android/gms/common/ConnectionResult.html");
        if (connectionResult.getErrorCode() == ConnectionResult.NETWORK_ERROR) {
            SweetAlertDialog d = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
            d.setTitleText("Could not login");
            d.setContentText("Sorry but your Internet connection does not appear to be working");
            d.setConfirmText("Ok");
            d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
            d.show();

//            AlertDialog d = new AlertDialog.Builder(LoginActivity.this)
//                    .setTitle("Could not login")
//                    .setMessage("Sorry but your Internet connection does not appear to be working")
//                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    }).create();
//            d.show();
//            ((TextView)d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
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
            if (dialogProgress != null && dialogProgress.isShowing())
                dialogProgress.dismissWithAnimation();
        } else {
            showProcessDialog();
        }
        btnLoginGGPlus.setEnabled(enable);
        btnLoginFB.setEnabled(enable);
        btnRegister.setEnabled(enable);
        btnLoginAccent.setEnabled(enable);
    }
}
