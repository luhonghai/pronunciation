package com.cmg.android.bbcaccent.activity.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.cmg.android.bbcaccent.AppLog;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.auth.AccountManager;
import com.cmg.android.bbcaccent.data.UserProfile;
import com.cmg.android.bbcaccent.preferences.YesNoPreference;
import com.cmg.android.bbcaccent.utils.AndroidHelper;
import com.cmg.android.bbcaccent.utils.DeviceUuidFactory;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.google.gson.Gson;

import com.cmg.android.bbcaccent.preferences.DatePreference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by luhonghai on 9/29/14.
 */
public class Preferences extends PreferenceFragment implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "PREFERENCES";
    public static final String KEY_COUNTRY_OF_BIRTH = "country_of_birth";
    public static final String KEY_ENGLISH_PROFICIENCY  = "english_proficiency";
    public static final String KEY_NATIVE_ENGLISH = "native_english";
    public static final String KEY_GENDER_MALE = "gender_male";
    public static final String KEY_GENDER_FEMALE = "gender_female";
    public static final String KEY_USER_DOB = "dob";
    public static final String KEY_USERNAME_LIST = "username_list";
    public static final String KEY_SCREEN_SELECT_USERNAME = "select_username";
    public static final String KEY_ADD_USERNAME = "add_username";
    public static final String KEY_DELETE_USERNAME = "delete_profile";
    public static final String KEY_AUDIO_CHANEL = "audio_chanel";
    public static final String KEY_AUDIO_SAMPLE_RATE = "audio_sample_rate";
    public static final String KEY_AUDIO_AUTO_STOP_RECORDING = "auto_stop_recording";

    public static final String PREF_USERNAMES = "PREF_USERNAMES";


    private ListPreference listCob;
    private ListPreference listEp;
    private ListPreference listUsername;

    private ListPreference listSampleRate;
    private ListPreference listChanel;

    private CheckBoxPreference cbxMale;
    private CheckBoxPreference cbxFemale;
    private CheckBoxPreference cbxNativeEnglish;
    private DatePreference dateDob;
    private EditTextPreference txtUsername;
    private PreferenceScreen screenSelectUsername;
    private YesNoPreference confirmDelete;
    private Preference prefVersion;
    private Gson gson = new Gson();

    private Set<String> data;

    private boolean isChanged = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        initPreferences();
        initUsername();
        prefVersion = (Preference) getPreferenceScreen().findPreference("version");
        if (prefVersion != null) {
            try {
                prefVersion.setSummary(getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName);
            } catch (PackageManager.NameNotFoundException e) {
                prefVersion.setSummary("Unknown");
            }
        }
    }

    private Set<String> getUserProfiles(final SharedPreferences pref) {
        if (data == null) {
            data = pref.getStringSet(PREF_USERNAMES, null);
            if (data == null) {
                data = new HashSet<String>();
//                UserProfile anonymous = UserProfile.getAnonymouse();
//                anonymous.setCountry(pref.getString(KEY_COUNTRY_OF_BIRTH, ""));
//                anonymous.setDob(pref.getString(KEY_USER_DOB, ""));
//                anonymous.setGender(pref.getBoolean(KEY_GENDER_MALE, true));
//                anonymous.setNativeEnglish(pref.getBoolean(KEY_NATIVE_ENGLISH, true));
//                anonymous.setEnglishProficiency(Integer.parseInt(pref.getString(KEY_ENGLISH_PROFICIENCY, "5")));
//                data.add(gson.toJson(anonymous));
//                SharedPreferences.Editor editor = pref.edit();
//                editor.putStringSet(PREF_USERNAMES, data);
//                editor.apply();
            }
        }
        return data;
    }

    public static void updateAdditionalProfile(final Context context, final UserProfile profile) {
        Location location = AndroidHelper.getLastBestLocation(context);
        if (location != null) {
            UserProfile.UserLocation userLocation = new UserProfile.UserLocation();
            userLocation.setLatitude(location.getLatitude());
            userLocation.setLongitude(location.getLongitude());
            profile.setLocation(userLocation);
        }
        UserProfile.DeviceInfo deviceInfo = new UserProfile.DeviceInfo();
        deviceInfo.setAppVersion(AndroidHelper.getVersionName(context));
        DeviceUuidFactory uIdFac = new DeviceUuidFactory(context);
        deviceInfo.setEmei(uIdFac.getDeviceUuid().toString());
        deviceInfo.setModel(android.os.Build.MODEL);
        deviceInfo.setOsVersion(System.getProperty("os.version"));
        deviceInfo.setOsApiLevel(android.os.Build.VERSION.SDK);
        deviceInfo.setDeviceName(android.os.Build.DEVICE);
        profile.setDeviceInfo(deviceInfo);
    }

    public static void addProfile(Context context, final UserProfile profile) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> userData = pref.getStringSet(PREF_USERNAMES, null);
        if (userData == null) {
            userData = new HashSet<String>();
        }
        Gson gson = new Gson();
        Log.i(TAG, "Add profile " + profile.getUsername());
        Iterator<String> iterator = userData.iterator();
        UserProfile oldProfile = null;
        while (iterator.hasNext()) {
            String raw = iterator.next();
            UserProfile tmp = gson.fromJson(raw, UserProfile.class);
            if (tmp.getUsername().equalsIgnoreCase(profile.getUsername())) {
                oldProfile = tmp;
                userData.remove(raw);
                break;
            }
        }
        if (oldProfile != null) {
            if (profile.getName() == null || profile.getName().length() == 0) {
                profile.setName(oldProfile.getName());
                profile.setFirstName(oldProfile.getFirstName());
                profile.setLastName(oldProfile.getLastName());
            }
            profile.setGender(oldProfile.isGender());
            profile.setDob(oldProfile.getDob());
            profile.setEnglishProficiency(oldProfile.getEnglishProficiency());
            profile.setCountry(oldProfile.getCountry());
            profile.setIsSetup(oldProfile.isSetup());
            profile.setNativeEnglish(oldProfile.isNativeEnglish());
            profile.setHelpStatus(oldProfile.getHelpStatus());
        }
        userData.add(gson.toJson(profile));
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(PREF_USERNAMES, userData);
        //editor.apply();
        editor.commit();
    }

    public static void updateProfile(Context context, final UserProfile profile) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> userData = pref.getStringSet(PREF_USERNAMES, null);
        if (userData == null) {
            userData = new HashSet<String>();
        }
        Gson gson = new Gson();
        Log.i(TAG, "Update profile " + profile.getUsername());
        Iterator<String> iterator = userData.iterator();
        UserProfile oldProfile = null;
        while (iterator.hasNext()) {
            String raw = iterator.next();
            UserProfile tmp = gson.fromJson(raw, UserProfile.class);
            if (tmp.getUsername().equalsIgnoreCase(profile.getUsername())) {
                oldProfile = tmp;
                userData.remove(raw);
                break;
            }
        }
        if (oldProfile != null) {
            profile.setIsLogin(oldProfile.isLogin());
            profile.setIsSetup(oldProfile.isSetup());
            profile.setHelpStatus(oldProfile.getHelpStatus());
        }
        userData.add(gson.toJson(profile));
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet(PREF_USERNAMES, userData);
        //editor.apply();
        editor.commit();
    }


    public static void setHelpStatusProfile(Context context, String username, int status) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> userData = pref.getStringSet(PREF_USERNAMES, null);
        if (userData == null) {
            userData = new HashSet<String>();
        }
        Gson gson = new Gson();
        Iterator<String> iterator = userData.iterator();
        UserProfile profile = null;
        while (iterator.hasNext()) {
            String raw = iterator.next();
            UserProfile tmp = gson.fromJson(raw, UserProfile.class);
            if (tmp.getUsername().equalsIgnoreCase(username)) {
                profile = tmp;
                userData.remove(raw);
                break;
            }
        }

        if (profile != null) {
            profile.setHelpStatus(status);
            AppLog.logString("Set help status to " + status + " username: " + username);
            userData.add(gson.toJson(profile));
            SharedPreferences.Editor editor = pref.edit();
            editor.putStringSet(PREF_USERNAMES, userData);
            editor.commit();
        }
    }

    public static void setIsSetupProfile(Context context, String username) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> userData = pref.getStringSet(PREF_USERNAMES, null);
        if (userData == null) {
            userData = new HashSet<String>();
        }
        Gson gson = new Gson();
        Iterator<String> iterator = userData.iterator();
        UserProfile profile = null;
        while (iterator.hasNext()) {
            String raw = iterator.next();
            UserProfile tmp = gson.fromJson(raw, UserProfile.class);
            if (tmp.getUsername().equalsIgnoreCase(username)) {
                profile = tmp;
                userData.remove(raw);
                break;
            }
        }

        if (profile != null) {
            profile.setIsSetup(true);
            AppLog.logString("Set is setup profile " + username);
            userData.add(gson.toJson(profile));
            SharedPreferences.Editor editor = pref.edit();
            editor.putStringSet(PREF_USERNAMES, userData);
            editor.commit();
        }
    }

    private void initUsername() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        Set<String> userList = getUserProfiles(pref);
        String username = getSelectedUsername(this.getActivity());
        if ((username == null || username.length() == 0) && userList.size() == 1) {
            username = gson.fromJson(userList.iterator().next(), UserProfile.class).getUsername();
        }
        selectUsername(pref, username);
    }

    private void selectUsername(SharedPreferences pref, String username) {
        ArrayList<String> userList = new ArrayList<String>(data);
        Collections.sort(userList);
        CharSequence[] usernames = new CharSequence[userList.size()];
        Iterator<String> iterator = userList.iterator();
        int count = 0;
        UserProfile currentProfile = null;
        while (iterator.hasNext()) {
            String raw = iterator.next();
            UserProfile profile = gson.fromJson(raw, UserProfile.class);
            usernames[count++] = profile.getUsername();
            if (username.equalsIgnoreCase(profile.getUsername())) {
                currentProfile = profile;
            }
        }
        if (screenSelectUsername == null)
            screenSelectUsername = (PreferenceScreen) getPreferenceScreen().findPreference(KEY_SCREEN_SELECT_USERNAME);

        if (listUsername == null)
            listUsername = (ListPreference) getPreferenceScreen().findPreference(KEY_USERNAME_LIST);
        if (listUsername != null) {
            listUsername.setEntries(usernames);
            listUsername.setEntryValues(usernames);
            Log.i(TAG, "Selected username: " + username);
            if (username.length() > 0)
                listUsername.setValue(username.toLowerCase());
            listUsername.setSummary(listUsername.getValue());
            if (txtUsername == null)
                txtUsername = (EditTextPreference) getPreferenceScreen().findPreference(KEY_ADD_USERNAME);
            txtUsername.setText("");
        }
        if (currentProfile != null) {
            listEp.setValue(Integer.toString(currentProfile.getEnglishProficiency()));
            listCob.setValue(currentProfile.getCountry());
            dateDob.setTheDate(currentProfile.getDob());
            cbxFemale.setChecked(!currentProfile.isGender());
            cbxMale.setChecked(currentProfile.isGender());
            if (cbxNativeEnglish != null)
                cbxNativeEnglish.setChecked(currentProfile.isNativeEnglish());
        }
        if (screenSelectUsername != null) {
            Dialog dialog = screenSelectUsername.getDialog();
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
            if (listUsername != null)
                screenSelectUsername.setSummary(listUsername.getValue());

        }
        listEp.setSummary(listEp.getEntry());
        listCob.setSummary(listCob.getEntry());
        dateDob.initSummary();

        ((BaseAdapter)getPreferenceScreen().getRootAdapter()).notifyDataSetChanged();
    }

    private void initPreferences() {
        if (confirmDelete == null)
            confirmDelete = (YesNoPreference) getPreferenceScreen().findPreference(KEY_DELETE_USERNAME);
        if (listCob == null)
            listCob = (ListPreference) getPreferenceScreen().findPreference(KEY_COUNTRY_OF_BIRTH);
        if (listEp == null)
            listEp = (ListPreference) getPreferenceScreen().findPreference(KEY_ENGLISH_PROFICIENCY);
        if (cbxMale == null)
            cbxMale = (CheckBoxPreference) getPreferenceScreen().findPreference(KEY_GENDER_MALE);
        if (cbxFemale == null)
            cbxFemale = (CheckBoxPreference) getPreferenceScreen().findPreference(KEY_GENDER_FEMALE);
        if (dateDob == null)
            dateDob = (DatePreference) getPreferenceScreen().findPreference(KEY_USER_DOB);
        if (cbxNativeEnglish == null)
            cbxNativeEnglish = (CheckBoxPreference) getPreferenceScreen().findPreference(KEY_NATIVE_ENGLISH);
        if (listSampleRate == null)
            listSampleRate = (ListPreference) getPreferenceScreen().findPreference(KEY_AUDIO_SAMPLE_RATE);
        if (listSampleRate != null)
            listSampleRate.setSummary(listSampleRate.getEntry());
        if (listChanel == null)
            listChanel = (ListPreference) getPreferenceScreen().findPreference(KEY_AUDIO_CHANEL);
        if (listChanel != null)
            listChanel.setSummary(listChanel.getEntry());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        isChanged = true;
        initPreferences();
        if (key.equalsIgnoreCase(KEY_ADD_USERNAME))
            addUsername(sharedPreferences);
        else if (key.equalsIgnoreCase(KEY_USERNAME_LIST))
            selectUsername(sharedPreferences, listUsername.getValue());
        else if (key.equalsIgnoreCase(KEY_DELETE_USERNAME)) {
            boolean isDelete = confirmDelete.getValue();
            if (isDelete) {
                Log.i(TAG, "Delete profile " + listUsername.getValue());
                deleteCurrentProfile(sharedPreferences);
            }
            confirmDelete.setValue(false);
        }
        else {
            if (key.equalsIgnoreCase(KEY_GENDER_FEMALE))
                cbxMale.setChecked(!cbxFemale.isChecked());
            else if (key.equalsIgnoreCase(KEY_GENDER_MALE))
                cbxFemale.setChecked(!cbxMale.isChecked());

            UserProfile profile = getCurrentProfile(this.getActivity());
            fillProfile(profile);
            updateProfile(sharedPreferences, profile);
            listEp.setSummary(listEp.getEntry());
            listCob.setSummary(listCob.getEntry());
        }

        this.getActivity().onContentChanged();
    }

    private void addUsername(final SharedPreferences pref) {
        String username = txtUsername.getText();
        if (username != null && username.length() > 0) {
            synchronized (data) {
                Log.i(TAG, "Add new profile " + username);
                boolean existed = false;
                Iterator<String> iterator = data.iterator();
                while (iterator.hasNext()) {
                    String raw = iterator.next();
                    UserProfile profile = gson.fromJson(raw, UserProfile.class);
                    if (profile.getUsername().equalsIgnoreCase(username)) {
                        existed = true;
                        break;
                    }
                }
                if (existed) {
                    //
                } else {
                    UserProfile profile = new UserProfile();
                    profile.setUsername(username);
                    //fillProfile(profile);
                    data.add(gson.toJson(profile));
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putStringSet(PREF_USERNAMES, data);
                    editor.commit();
                    //editor.apply();
                }
                selectUsername(pref, username);
            }
        }
    }

    public void deleteCurrentProfile(final SharedPreferences pref) {
        String username = listUsername.getValue();
        if (data.size() <= 1) {
            Toast.makeText(this.getActivity(), "You must keep at least one profile!", Toast.LENGTH_LONG).show();
            return;
        }
        synchronized (data) {
            Iterator<String> iterator = data.iterator();
            String firstUsername = "";
            while (iterator.hasNext()) {
                String raw = iterator.next();
                UserProfile tmp = gson.fromJson(raw, UserProfile.class);
                if (tmp.getUsername().equalsIgnoreCase(username)) {
                    data.remove(raw);
                    Log.i(TAG, "Remove " + username + " from list.");
                    break;
                }
            }
            firstUsername = gson.fromJson(data.iterator().next(), UserProfile.class).getUsername();
            SharedPreferences.Editor editor = pref.edit();
            editor.putStringSet(PREF_USERNAMES, data);
            editor.apply();
            selectUsername(pref, firstUsername);
        }
    }

    private void updateProfile(final SharedPreferences pref, final UserProfile profile) {
        synchronized (data) {
            Log.i(TAG, "Update profile " + profile.getUsername());
            Iterator<String> iterator = data.iterator();
            while (iterator.hasNext()) {
                String raw = iterator.next();
                UserProfile tmp = gson.fromJson(raw, UserProfile.class);
                if (tmp.getUsername().equalsIgnoreCase(profile.getUsername())) {
                    data.remove(raw);
                    break;
                }
            }
            data.add(gson.toJson(profile));
            SharedPreferences.Editor editor = pref.edit();
            editor.putStringSet(PREF_USERNAMES, data).commit();
            //editor.apply();
            editor.commit();
        }
    }

    private void fillProfile(final UserProfile profile) {
        profile.setEnglishProficiency(Integer.parseInt(listEp.getValue()));
        profile.setCountry(listCob.getValue());
        if (cbxNativeEnglish != null)
            profile.setNativeEnglish(cbxNativeEnglish.isChecked());
        profile.setGender(cbxMale.isChecked());
        profile.setDob(DatePreference.formatter().format(dateDob.getDate().getTime()));
    }

    @Override
    public void onDestroy() {
        if (isChanged) {
            AccountManager accountManager = new AccountManager(getActivity());
            accountManager.updateProfile(getCurrentProfile(getActivity()), new AccountManager.AuthListener() {
                @Override
                public void onError(String message, Throwable e) {
                    SimpleAppLog.error(message, e);
                }

                @Override
                public void onSuccess() {
                    SimpleAppLog.info("send update profile request successfully");
                }
            });
        }
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public static boolean getBoolean(String key, Context context) {
        return getBoolean(key, context, false);
    }

    public static String getSelectedUsername(Context context) {
        return getString(KEY_USERNAME_LIST, context, "");
    }

    public static void setSelectedUsername(String username, Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putString(KEY_USERNAME_LIST, username).commit();
                //.apply();
    }

    public static boolean getBoolean(String key, Context context, boolean defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, defaultValue);
    }

    public static int getInt(String key, Context context) {
        return getInt(key, context, -1);
    }

    public static int getInt(String key, Context context, int defaultValue) {
        String value = PreferenceManager.getDefaultSharedPreferences(context).getString(key, Integer.toString(defaultValue));
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    public static String getString(String key, Context context) {
        return getString(key,context, "");
    }

    public static String getString(String key, Context context, String defaultValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defaultValue);
    }

    public static UserProfile getCurrentProfile(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String username = pref.getString(KEY_USERNAME_LIST, "");
        if (username != null && username.length() > 0) {
            AppLog.logString("Current profile username  " + username);
            Set<String> data = pref.getStringSet(PREF_USERNAMES, null);
            Gson gson = new Gson();
            if (data != null && data.size() > 0) {
                Iterator<String> iterator = data.iterator();
                while (iterator.hasNext()) {
                    String raw = iterator.next();
                    UserProfile tmp = gson.fromJson(raw, UserProfile.class);
                    if (tmp.getUsername().equalsIgnoreCase(username)) {
                        return tmp;
                    }
                }
            }
        }
        return null;
    }
}
