package com.cmg.android.bbcaccent.utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

/**
 * Created by hai on 12/16/14.
 */
public class GcmUtil {

    private static final String PROPERTY_REG_ID = "gcm.registration.id";

    private static final String PROPERTY_APP_VERSION = "gcm.app.version";

    private final String SENDER_ID = "11447652953";

    public interface GcmRegisterCallback {
        void onRegistered(String registrationId);
        void onError(String message, Throwable e);
    }

    private static GcmUtil instance;

    private Context context;

    private GoogleCloudMessaging gcm;

    public static GcmUtil getInstance(Context context) {
        if (instance == null)
            instance = new GcmUtil(context);
        return instance;
    }

    private GcmUtil(Context context) {
        this.context = context;
    }

    public void register(final GcmRegisterCallback callback) {
        String registrationId = getRegistrationId();

        if(!registrationId.isEmpty()) {
            callback.onRegistered(registrationId);
            return;
        }

        new AsyncTask<Void, Integer, String>() {
            @Override
            protected String doInBackground(Void... params) {
                if (gcm == null)
                    gcm = GoogleCloudMessaging.getInstance(context);

                try {
                    String registrationId = gcm.register(SENDER_ID);
                    storeRegistrationId(registrationId);
                    return registrationId;
                } catch (IOException e) {
                    callback.onError("Could not get registration id", e);
                }
                return "";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                callback.onRegistered(result);
            }
        }.execute();
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    public String getRegistrationId() {
        final SharedPreferences prefs = getGCMPreferences();
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            SimpleAppLog.error("Registration not found.");
            return "";
        }

        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion();
        if (registeredVersion != currentVersion) {
            SimpleAppLog.debug("App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences() {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param regId registration ID
     */
    private void storeRegistrationId(String regId) {
        final SharedPreferences prefs = getGCMPreferences();
        int appVersion = getAppVersion();
        SimpleAppLog.info("Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private int getAppVersion() {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public void recycle() {
        if (gcm != null) {
            try {
                gcm.close();
            } catch (Exception e) {}
        }
    }

}