package com.cmg.android.bbcaccentamt.auth;

import android.content.Context;
import android.os.AsyncTask;

import com.cmg.android.bbcaccentamt.R;
import com.cmg.android.bbcaccentamt.activity.fragment.Preferences;
import com.cmg.android.bbcaccentamt.data.UserProfile;
import com.cmg.android.bbcaccentamt.http.HttpContacter;
import com.cmg.android.bbcaccentamt.http.ResponseData;
import com.cmg.android.bbcaccentamt.utils.AndroidHelper;
import com.cmg.android.bbcaccentamt.utils.DeviceUuidFactory;
import com.cmg.android.bbcaccentamt.utils.SimpleAppLog;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by luhonghai on 4/13/15.
 */
public class AccountManager {

    public static interface AuthListener {

        public void onError(String message, Throwable e);

        public void onSuccess();
    }

    private GoogleApiClient mGoogleApiClient;

    private final Context context;

    public AccountManager(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        mGoogleApiClient = new GoogleApiClient.Builder(context).addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }

    public void start()  {
        mGoogleApiClient.connect();
    }

    public void stop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void submitActivationCode(final String code, final UserProfile profile, final AuthListener authListener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Map<String, String> data = new HashMap<String, String>();
                data.put("acc", code);
                data.put("user", profile.getUsername());
                data.put("lang_prefix", "BE");
                data.put("version_code", AndroidHelper.getVersionCode(context));
                data.put("imei", new DeviceUuidFactory(context).getDeviceUuid().toString());
                try {
                    HttpContacter contacter = new HttpContacter(context);
                    String message = contacter.post(data, context.getResources().getString(R.string.activation_url));
                    if (message.equalsIgnoreCase("success")) {
                        authListener.onSuccess();
                    } else {
                        if (message.toLowerCase().contains("<html>")) {
                            authListener.onError(context.getString(R.string.could_not_connect_server_message), null);
                        } else {
                            authListener.onError(message, null);
                        }
                    }
                } catch (Exception e) {
                    authListener.onError(context.getString(R.string.could_not_connect_server_message), e);
                }
                return null;
            }
        }.execute();
    }

    public void resendActivationCode(final UserProfile profile, final AuthListener authListener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Map<String, String> data = new HashMap<String, String>();
                Gson gson = new Gson();
                data.put("profile", gson.toJson(profile));
                data.put("lang_prefix", "BE");
                data.put("version_code", AndroidHelper.getVersionCode(context));
                data.put("imei", new DeviceUuidFactory(context).getDeviceUuid().toString());
                try {
                    HttpContacter contacter = new HttpContacter(context);
                    String message = contacter.post(data, context.getResources().getString(R.string.activation_url));
                    if (message.equalsIgnoreCase("success")) {
                        authListener.onSuccess();
                    } else {
                        if (message.toLowerCase().contains("<html>")) {
                            authListener.onError(context.getString(R.string.could_not_connect_server_message), null);
                        } else {
                            authListener.onError(message, null);
                        }
                    }
                } catch (Exception e) {
                    authListener.onError(context.getString(R.string.could_not_connect_server_message), e);
                }
                return null;
            }
        }.execute();
    }

    public void register(final UserProfile profile, final AuthListener authListener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Map<String, String> data = new HashMap<String, String>();
                Gson gson = new Gson();
                data.put("version_code", AndroidHelper.getVersionCode(context));
                data.put("profile", gson.toJson(profile));
                data.put("lang_prefix", "BE");
                data.put("imei", new DeviceUuidFactory(context).getDeviceUuid().toString());
                try {
                    HttpContacter contacter = new HttpContacter(context);
                    String message = contacter.post(data, context.getResources().getString(R.string.register_url));

                    ResponseData<UserProfile> responseData = gson.fromJson(message, ResponseData.class);
                    if (authListener != null) {
                        if (responseData.isStatus()) {
                            authListener.onSuccess();
                        } else {
                            if (message.toLowerCase().contains("<html>")) {
                                authListener.onError(context.getString(R.string.could_not_connect_server_message), null);
                            } else {
                                authListener.onError(responseData.getMessage(), null);
                            }
                        }
                    }
                } catch (Exception e) {
                    if (authListener != null)
                        authListener.onError(context.getString(R.string.could_not_connect_server_message), e);
                }
                return null;
            }
        }.execute();
    }

    public void auth(final UserProfile profile, final AuthListener authListener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Map<String, String> data = new HashMap<String, String>();
                Gson gson = new Gson();
                Preferences.updateAdditionalProfile(context, profile);
                data.put("profile", gson.toJson(profile));
                data.put("imei", new DeviceUuidFactory(context).getDeviceUuid().toString());
                try {
                    HttpContacter contacter = new HttpContacter(context);
                    String message = contacter.post(data, context.getResources().getString(R.string.auth_url));
                    if (message.equalsIgnoreCase("success")) {
                        authListener.onSuccess();
                    } else {
                        if (message.toLowerCase().contains("<html>")) {
                            authListener.onError(context.getString(R.string.could_not_connect_server_message), null);
                        } else {
                            if (message.equalsIgnoreCase(context.getString(R.string.invalid_username_or_password))) {
                                message = context.getString(R.string.invalid_email_address_or_password);
                            }
                            authListener.onError(message, null);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    authListener.onError(context.getString(R.string.could_not_connect_server_message), e);
                }
                return null;
            }
        }.execute();
    }

    public void checkLicense(final UserProfile profile, final AuthListener authListener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Map<String, String> data = new HashMap<String, String>();
                Gson gson = new Gson();
                data.put("account", profile.getUsername());
                data.put("action", "check");
                data.put("code", profile.getLicenseCode());
                data.put("imei", new DeviceUuidFactory(context).getDeviceUuid().toString());
                try {
                    HttpContacter contacter = new HttpContacter(context);
                    String message = contacter.post(data, context.getResources().getString(R.string.license_url));
                    SimpleAppLog.info("License check response: " + message);
                    if (message.equalsIgnoreCase("success")) {
                        authListener.onSuccess();
                    } else {
                        if (message.toLowerCase().contains("<html>")) {
                            authListener.onError(context.getString(R.string.could_not_connect_server_message), null);
                        } else {
                            authListener.onError(message, null);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    authListener.onError(context.getString(R.string.could_not_connect_server_message), e);
                }
                return null;
            }
        }.execute();
    }

    public void activeLicense(final UserProfile profile, final AuthListener authListener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Map<String, String> data = new HashMap<String, String>();
                Gson gson = new Gson();
                data.put("account", profile.getUsername());
                data.put("action", "active");
                data.put("code", profile.getLicenseCode());
                data.put("imei", new DeviceUuidFactory(context).getDeviceUuid().toString());
                try {
                    HttpContacter contacter = new HttpContacter(context);
                    String message = contacter.post(data, context.getResources().getString(R.string.license_url));
                    SimpleAppLog.info("License activate response: " + message);
                    if (message.equalsIgnoreCase("success")) {
                        authListener.onSuccess();
                    } else {
                        if (message.toLowerCase().contains("<html>")) {
                            authListener.onError(context.getString(R.string.could_not_connect_server_message), null);
                        } else {
                            authListener.onError(message, null);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    authListener.onError(context.getString(R.string.could_not_connect_server_message), e);
                }
                return null;
            }
        }.execute();
    }

    public void resetPassword(final UserProfile profile, final AuthListener authListener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Map<String, String> data = new HashMap<String, String>();
                Gson gson = new Gson();
                data.put("acc", profile.getUsername());
                data.put("action", "request");
                data.put("imei", new DeviceUuidFactory(context).getDeviceUuid().toString());
                try {
                    HttpContacter contacter = new HttpContacter(context);
                    String message = contacter.post(data, context.getResources().getString(R.string.reset_password_url));
                    SimpleAppLog.info("Reset password response: " + message);
                    if (message.equalsIgnoreCase("success")) {
                        authListener.onSuccess();
                    } else {
                        if (message.toLowerCase().contains("<html>")) {
                            authListener.onError(context.getString(R.string.could_not_connect_server_message), null);
                        } else {
                            authListener.onError(message, null);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    authListener.onError(context.getString(R.string.could_not_connect_server_message), e);
                }
                return null;
            }
        }.execute();
    }

    public void logout() {
        UserProfile profile = Preferences.getCurrentProfile(context);
        if (profile != null) {
            profile.setIsLogin(false);
            Preferences.addProfile(context, profile);
        }
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }
}
