package com.cmg.android.bbcaccent.auth;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.data.dto.StudentMappingTeacher;
import com.cmg.android.bbcaccent.fragment.Preferences;
import com.cmg.android.bbcaccent.data.dto.UserProfile;
import com.cmg.android.bbcaccent.http.HttpContacter;
import com.cmg.android.bbcaccent.http.ResponseData;
import com.cmg.android.bbcaccent.utils.AndroidHelper;
import com.cmg.android.bbcaccent.utils.DeviceUuidFactory;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luhonghai on 4/13/15.
 */
public class AccountManager {

    public static interface AuthListener {

        public void onError(String message, Throwable e);

        public void onSuccess();
    }
    public static interface AuthListeners {

        public void onError(String message, Throwable e);

        public void onSuccess(List<StudentMappingTeacher> lists,int number, Throwable e);
    }


    private GoogleApiClient mGoogleApiClient;

    private final Context context;

    public AccountManager(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        FacebookSdk.sdkInitialize(context.getApplicationContext());
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

                    ResponseData<UserProfile> responseData = gson.fromJson(message, new TypeToken<ResponseData<UserProfile>>() {
                    }.getType());
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
        auth(profile, authListener, false);
    }

    public void auth(final UserProfile profile, final AuthListener authListener, final boolean willCheck) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Map<String, String> data = new HashMap<String, String>();
                Gson gson = new Gson();
                Preferences.updateAdditionalProfile(context, profile);
                data.put("profile", gson.toJson(profile));
                if (willCheck)
                    data.put("check", "true");
                    data.put("imei", new DeviceUuidFactory(context).getDeviceUuid().toString());
                try {
                    HttpContacter contacter = new HttpContacter(context);
                    String message = contacter.post(data, context.getResources().getString(R.string.auth_url));
                    try {
                        ResponseData<LoginToken> responseData = MainApplication.fromJson(message, new TypeToken<ResponseData<LoginToken>>(){}.getType());
                        if (responseData.isStatus()) {
                            if (responseData.getData() != null) {
                                profile.setToken(responseData.getData().getToken());
                                SimpleAppLog.debug("login token : " + profile.getToken());
                            }
                            authListener.onSuccess();
                        } else {
                            if (responseData.getMessage().equalsIgnoreCase(context.getString(R.string.invalid_username_or_password))) {
                                responseData.setMessage(context.getString(R.string.invalid_email_address_or_password));
                            }
                            authListener.onError(responseData.getMessage(), null);
                        }
                    } catch (JsonSyntaxException e) {
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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    SimpleAppLog.error("could not connect to server", e);
                    authListener.onError(context.getString(R.string.could_not_connect_server_message), e);
                }
                return null;
            }
        }.execute();
    }

    public void logoutToken(final UserProfile profile, final String token, final AuthListener authListener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Map<String, String> data = new HashMap<String, String>();
                Gson gson = new Gson();
                Preferences.updateAdditionalProfile(context, profile);
                data.put("profile", gson.toJson(profile));
                data.put("imei", new DeviceUuidFactory(context).getDeviceUuid().toString());
                data.put("token", token);
                try {
                    HttpContacter contacter = new HttpContacter(context);
                    String message = contacter.post(data, context.getResources().getString(R.string.logout_url));
                    try {
                        ResponseData<LoginToken> responseData = MainApplication.fromJson(message, new TypeToken<ResponseData<LoginToken>>() {}.getType());
                        if (responseData.isStatus()) {
                            authListener.onSuccess();
                        } else {
                            if (responseData.getMessage().equalsIgnoreCase(context.getString(R.string.invalid_username_or_password))) {
                                responseData.setMessage(context.getString(R.string.invalid_email_address_or_password));
                            }
                            authListener.onError(responseData.getMessage(), null);
                        }
                    } catch (JsonSyntaxException e) {
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
                    }

                } catch (Exception e) {
                    SimpleAppLog.error("could not connect to server", e);
                    authListener.onError(context.getString(R.string.could_not_connect_server_message), e);
                }
                return null;
            }
        }.execute();
    }

    public void getProfile(final UserProfile profile, final AuthListener authListener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Map<String, String> data = new HashMap<String, String>();
                Gson gson = new Gson();
                Preferences.updateAdditionalProfile(context, profile);
                data.put("profile", gson.toJson(profile));
                data.put("action", "get");
                data.put("imei", new DeviceUuidFactory(context).getDeviceUuid().toString());
                try {
                    HttpContacter contacter = new HttpContacter(context);
                    String rawJson = contacter.post(data, context.getResources().getString(R.string.user_profile_url));
                    Type collectionType = new TypeToken<ResponseData<UserProfile>>(){}.getType();
                    ResponseData<UserProfile> rawData = gson.fromJson(rawJson, collectionType);
                    UserProfile mUser = rawData.getData();
                    if (rawData.isStatus() && mUser != null) {
                        profile.setCountry(mUser.getCountry());
                        profile.setDob(mUser.getDob());
                        profile.setFirstName(mUser.getFirstName());
                        profile.setLastName(mUser.getLastName());
                        profile.setEnglishProficiency(mUser.getEnglishProficiency());
                        profile.setGender(mUser.isGender());
                        profile.setName(mUser.getName());
                        profile.setNativeEnglish(mUser.isNativeEnglish());
                        if (mUser.getProfileImage().length() > 0)
                            profile.setProfileImage(mUser.getProfileImage());
                        authListener.onSuccess();
                    } else {
                        authListener.onError(rawData.getMessage(), null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    authListener.onError(context.getString(R.string.could_not_connect_server_message), e);
                }
                return null;
            }
        }.execute();
    }

    public void updateProfile(final UserProfile profile, final AuthListener authListener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Map<String, String> data = new HashMap<String, String>();
                Gson gson = new Gson();
                Preferences.updateAdditionalProfile(context, profile);
                data.put("profile", gson.toJson(profile));
                data.put("action", "update");
                data.put("imei", new DeviceUuidFactory(context).getDeviceUuid().toString());
                try {
                    HttpContacter contacter = new HttpContacter(context);
                    String rawJson = contacter.post(data, context.getResources().getString(R.string.user_profile_url));
                    Type collectionType = new TypeToken<ResponseData<UserProfile>>(){}.getType();
                    ResponseData<UserProfile> rawData = gson.fromJson(rawJson, collectionType);
                    if (rawData.isStatus()) {
                        authListener.onSuccess();
                    } else {
                        authListener.onError(rawData.getMessage(), null);
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

    public void loadLicenseData(final UserProfile profile, final AuthListener authListener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Map<String, String> data = new HashMap<String, String>();
                Gson gson = new Gson();
                data.put("account", profile.getUsername());
                data.put("action", "list");
                data.put("token", profile.getToken());
                data.put("code", profile.getLicenseCode());
                data.put("imei", new DeviceUuidFactory(context).getDeviceUuid().toString());
                try {
                    HttpContacter contacter = new HttpContacter(context);
                    String message = contacter.post(data, context.getResources().getString(R.string.license_url));
                    try {
                        ResponseData<List<UserProfile.LicenseData>> responseData = gson.fromJson(message, new TypeToken<ResponseData<List<UserProfile.LicenseData>>>() {
                        }.getType());
                        if (responseData.isStatus()) {
                            profile.setLicenseData(responseData.getData());
                            authListener.onSuccess();
                        } else {
                            authListener.onError(responseData.getMessage(), null);
                        }
                    } catch (Exception e) {
                        SimpleAppLog.error("Could not list licence data", e);
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

    public void switchLicense(final UserProfile profile, final AuthListener authListener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Map<String, String> data = new HashMap<String, String>();
                Gson gson = new Gson();
                data.put("account", profile.getUsername());
                data.put("action", "switch");
                data.put("token", profile.getToken());
                data.put("code", profile.getLicenseCode());
                data.put("imei", new DeviceUuidFactory(context).getDeviceUuid().toString());
                try {
                    HttpContacter contacter = new HttpContacter(context);
                    String message = contacter.post(data, context.getResources().getString(R.string.license_url));
                    try {
                        ResponseData<Object> responseData = gson.fromJson(message, new TypeToken<ResponseData<Object>>() {
                        }.getType());
                        if (responseData.isStatus()) {
                            authListener.onSuccess();
                        } else {
                            authListener.onError(responseData.getMessage(), null);
                        }
                    } catch (Exception e) {
                        SimpleAppLog.error("Could not switch licence", e);
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

    public void checkActivationDate(final UserProfile profile, final AuthListener authListener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Map<String, String> data = new HashMap<String, String>();
                Gson gson = new Gson();
                Preferences.updateAdditionalProfile(context, profile);
                SimpleAppLog.debug("Profile user name: " + profile.getUsername());
                data.put("profile", gson.toJson(profile));
                data.put("imei", new DeviceUuidFactory(context).getDeviceUuid().toString());
                data.put("token", profile.getToken());
                try {
                    HttpContacter contacter = new HttpContacter(context);
                    String message = contacter.post(data, context.getResources().getString(R.string.check_activation_date_url));
                    SimpleAppLog.debug("Check activation date response: " + message);
                    try {
                        ResponseData<LoginToken> responseData = MainApplication.fromJson(message, new TypeToken<ResponseData<LoginToken>>() {}.getType());
                        if (responseData.isStatus()) {
                            authListener.onSuccess();
                        } else {
                            if (responseData.getMessage().equalsIgnoreCase(context.getString(R.string.invalid_username_or_password))) {
                                responseData.setMessage(context.getString(R.string.invalid_email_address_or_password));
                            }
                            authListener.onError(responseData.getMessage(), null);
                        }
                    } catch (JsonSyntaxException e) {
                        authListener.onSuccess();
                    }

                } catch (Exception e) {
                    SimpleAppLog.error("could not connect to server", e);
                    authListener.onSuccess();
                }
                return null;
            }
        }.execute();
    }


    public void messageTeacher(final UserProfile profile, final AuthListeners authListeners) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Map<String, String> data = new HashMap<String, String>();
                List<StudentMappingTeacher> mappingTeachers=new ArrayList<StudentMappingTeacher>();
                int number=0;
                Gson gson = new Gson();
                Preferences.updateAdditionalProfile(context, profile);
                SimpleAppLog.debug("Profile user name: " + profile.getUsername());
                data.put("username", profile.getUsername());
                data.put("action","infoUser");
                try {
                    HttpContacter contacter = new HttpContacter(context);
                    String message = contacter.post(data, context.getResources().getString(R.string.message_teacher_url));
                    SimpleAppLog.debug("message: " + message);
                    try {
                        StudentMappingTeachers responseData = MainApplication.fromJson(message, StudentMappingTeachers.class);
                        mappingTeachers= responseData.studentMappingTeachers;
                        if(responseData!=null && mappingTeachers!=null && mappingTeachers.size()>0) {
                            for (StudentMappingTeacher studentMappingTeacher : mappingTeachers) {
                                if(studentMappingTeacher.getStatus().equalsIgnoreCase("pending")){
                                    number++;
                                }

                            }
                        }

                        if (responseData.isStatus()) {
                            authListeners.onSuccess(mappingTeachers,number,null);
                        } else {
                            authListeners.onError(responseData.getMessage(), null);
                        }
                    } catch (JsonSyntaxException e) {
                       authListeners.onSuccess(mappingTeachers,number,null);
                    }

                } catch (Exception e) {
                    SimpleAppLog.error("could not connect to server", e);
                    authListeners.onSuccess(mappingTeachers,number,null);
                }
                return null;
            }
        }.execute();
    }
    public void messageTeachers(final UserProfile profile, final AuthListeners authListeners) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Map<String, String> data = new HashMap<String, String>();
                List<StudentMappingTeacher> mappingTeachers=new ArrayList<StudentMappingTeacher>();
                int number=0;
                Gson gson = new Gson();
                Preferences.updateAdditionalProfile(context, profile);
                SimpleAppLog.debug("Profile user name: " + profile.getUsername());
                data.put("username", profile.getUsername());
                data.put("action","infoUser");
                try {
                    HttpContacter contacter = new HttpContacter(context);
                    String message = contacter.post(data, context.getResources().getString(R.string.message_teacher_url));
                    SimpleAppLog.debug("message: " + message);
                    try {
                        StudentMappingTeachers responseData = MainApplication.fromJson(message, StudentMappingTeachers.class);
                        mappingTeachers= responseData.studentMappingTeachers;
                        if(responseData!=null && mappingTeachers!=null && mappingTeachers.size()>0) {
                            for (StudentMappingTeacher studentMappingTeacher : mappingTeachers) {
                                if(studentMappingTeacher.getStatus().equalsIgnoreCase("pending")){
                                    number++;
                                }
                            }
                        }

                        if (responseData.isStatus()) {
                            authListeners.onSuccess(mappingTeachers,number,null);
                        } else {
                            authListeners.onError(responseData.getMessage(), null);
                        }
                    } catch (JsonSyntaxException e) {
                        authListeners.onSuccess(mappingTeachers,number,null);
                    }

                } catch (Exception e) {
                    SimpleAppLog.error("could not connect to server", e);
                    authListeners.onSuccess(mappingTeachers,number,null);
                }
                return null;
            }
        }.execute();
    }
    public void sendStatusToTeacher(final UserProfile profile, final AuthListener authListener, final String status, final String mailTeacher) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Map<String, String> data = new HashMap<String, String>();
                Gson gson = new Gson();
                data.put("username", profile.getUsername());
                data.put("action", "sendStatus");
                data.put("status", status);
                data.put("mailTeacher", mailTeacher);
                try {
                    HttpContacter contacter = new HttpContacter(context);
                    String message = contacter.post(data, context.getResources().getString(R.string.message_teacher_url));
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
    public void searchTeacher(final UserProfile profile, final AuthListener authListener, final String mailTeacher) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Map<String, String> data = new HashMap<String, String>();
                Gson gson = new Gson();
                data.put("username", profile.getUsername());
                data.put("action", "searchTeacher");
                data.put("mailTeacher", mailTeacher);
                try {
                    HttpContacter contacter = new HttpContacter(context);
                    String message = contacter.post(data, context.getResources().getString(R.string.message_teacher_url));
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
        LoginManager.getInstance().logOut();
        UserProfile profile = Preferences.getCurrentProfile(context);
        if (profile != null) {
            logoutToken(profile, profile.getToken(), new AuthListener() {
                @Override
                public void onError(String message, Throwable e) {

                }

                @Override
                public void onSuccess() {

                }
            });
            profile.setIsLogin(false);
            profile.setToken("");
            profile.setAdditionalToken("");
            profile.setIsActivatedLicence(false);
            profile.setIsSubscription(false);
            profile.setLicenseData(null);
            Preferences.updateProfile(context, profile);
        }
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }
}

class LoginToken {
    private String id;

    private String userName;

    private String deviceName;

    private String token;

    private int appVersion;


    private String appName;

    private Date createdDate;


    private Date accessDate;


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id=id;
    }
    public String getUserName(){
        if (userName != null) userName = userName.toLowerCase();
        return userName;
    }
    public  void setUserName(String userName){
        if (userName != null) userName = userName.toLowerCase();
        this.userName=userName;
    }
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(int appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }


    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getAccessDate() {
        return accessDate;
    }

    public void setAccessDate(Date accessDate) {
        this.accessDate = accessDate;
    }
}
class StudentMappingTeachers extends ResponseData<StudentMappingTeacher>{
    public List<StudentMappingTeacher> studentMappingTeachers;
}
//class StudentMappingTeacher{
//    private String id;
//
//    private String studentName;
//
//    private String teacherName;
//
//    private boolean isDeleted;
//
//    private String status;
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id=id;
//    }
//
//    public boolean isDeleted() {
//        return isDeleted;
//    }
//
//    public void setIsDeleted(boolean isDeleted) {
//        this.isDeleted = isDeleted;
//    }
//
//    public String getTeacherName() {
//        return teacherName;
//    }
//
//    public void setTeacherName(String teacherName) {
//        this.teacherName = teacherName;
//    }
//
//    public String getStudentName() {
//        return studentName;
//    }
//
//    public void setStudentName(String studentName) {
//        this.studentName = studentName;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//}



