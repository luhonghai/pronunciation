package com.cmg.android.bbcaccentamt;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import com.cmg.android.bbcaccentamt.activity.BaseActivity;
import com.cmg.android.bbcaccentamt.activity.fragment.Preferences;
import com.cmg.android.bbcaccentamt.auth.AccountManager;
import com.cmg.android.bbcaccentamt.data.DatabasePrepare;
import com.cmg.android.bbcaccentamt.data.UserProfile;
import com.cmg.android.bbcaccentamt.utils.AnalyticHelper;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by luhonghai on 3/17/15.
 */
public class LoginActivity extends BaseActivity {


    private AccountManager accountManager;

    private SweetAlertDialog dialogProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountManager = new AccountManager(this);
        setContentView(R.layout.dialog_login);

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfile profile = new UserProfile();
                profile.setLoginType(UserProfile.TYPE_EASYACCENT);
                profile.setUsername(((TextView) findViewById(R.id.txtEmail)).getText().toString());
                profile.setPassword(((TextView) findViewById(R.id.txtPassword)).getText().toString());
                if (profile.getUsername().length() > 0 && profile.getPassword().length() > 0) {
                    findViewById(R.id.btnLogin).setEnabled(false);
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
    }

    private void showProcessDialog() {
        dialogProgress = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialogProgress.setTitleText(getString(R.string.processing));
        dialogProgress.setCancelable(false);
        dialogProgress.show();
    }

    private void doAuth(final UserProfile profile) {
        //Toast.makeText(this, "Login with " + profile.getUsername(), Toast.LENGTH_LONG).show();
        Preferences.setSelectedUsername(profile.getUsername(), LoginActivity.this);
        Preferences.addProfile(this, profile);
        final UserProfile currentProfile = Preferences.getCurrentProfile(this);
        if (currentProfile == null) {
            findViewById(R.id.btnLogin).setEnabled(true);
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
                            findViewById(R.id.btnLogin).setEnabled(true);
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
                    DatabasePrepare databasePrepare = new DatabasePrepare(LoginActivity.this, new DatabasePrepare.OnPrepraredListener() {
                        @Override
                        public void onComplete() {

                        }
                    });
                    databasePrepare.prepare();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialogProgress.dismissWithAnimation();
                        }
                    });
                    startMainActivity();
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

    private void hideProcessDialog() {
            if (dialogProgress != null && dialogProgress.isShowing())
                dialogProgress.dismissWithAnimation();
    }
}
