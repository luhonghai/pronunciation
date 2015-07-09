package com.cmg.android.bbcaccentamt;

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

import com.cmg.android.bbcaccentamt.activity.BaseActivity;
import com.cmg.android.bbcaccentamt.activity.fragment.Preferences;
import com.cmg.android.bbcaccentamt.activity.view.RecordingView;
import com.cmg.android.bbcaccentamt.auth.AccountManager;
import com.cmg.android.bbcaccentamt.data.UserProfile;
import com.cmg.android.bbcaccentamt.utils.AnalyticHelper;
import com.cmg.android.bbcaccentamt.utils.SimpleAppLog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by luhonghai on 3/17/15.
 */
public class LoginActivity extends BaseActivity implements RecordingView.OnAnimationListener,
        GoogleApiClient.ConnectionCallbacks,
        View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{


    private ImageButton btnLoginFB;

    private ImageButton btnLoginAccent;

    private ImageButton btnRegister;

    private GoogleApiClient mGoogleApiClient;

    private boolean mIntentInProccess;

    private boolean signedInUser;

    private ConnectionResult mConnectionResult;

    private ImageButton btnLoginGGPlus;

    private AccountManager accountManager;

    private boolean willShowLicenceWarning = false;

    private SweetAlertDialog dialogProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountManager = new AccountManager(this);
        setContentView(R.layout.login);

        (btnLoginFB = (ImageButton) findViewById(R.id.loginButton)).setOnClickListener(this);
        (btnLoginGGPlus = (ImageButton) findViewById(R.id.sign_in_button)).setOnClickListener(this);
        (btnLoginAccent = (ImageButton) findViewById(R.id.btnLoginAccent)).setOnClickListener(this);
        (btnRegister = (ImageButton) findViewById(R.id.btnRegister)).setOnClickListener(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();

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
        dialogProgress.setTitleText(getString(R.string.processing));
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
    }

    @Override
    public void onAnimationMax() {

    }

    @Override
    public void onAnimationMin() {

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

                            final SweetAlertDialog d = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                            d.setTitleText(getString(R.string.could_not_login));
                            d.setContentText(message);
                            d.setConfirmText(getString(R.string.dialog_ok));
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
            //showProcessDialog();
            doAuth(profile);
        } else {
            hideProcessDialog();
        }
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

    private void hideProcessDialog() {
            if (dialogProgress != null && dialogProgress.isShowing())
                dialogProgress.dismissWithAnimation();
    }

    private void enableForm(boolean enable) {
        if (enable) {

        } else {
            showProcessDialog();
        }
        btnLoginGGPlus.setEnabled(enable);
        btnLoginFB.setEnabled(enable);
        btnRegister.setEnabled(enable);
        btnLoginAccent.setEnabled(enable);
    }
}