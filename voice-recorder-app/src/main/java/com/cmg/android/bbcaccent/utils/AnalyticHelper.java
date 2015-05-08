package com.cmg.android.bbcaccent.utils;

import android.content.Context;

import com.cmg.android.bbcaccent.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by luhonghai on 4/17/15.
 */
public class AnalyticHelper {

    public static final String CATEGORY_ANALYZING = "Analyzing";

    public static final String ACTION_SELECT_WORD = "Select word";

    public static final String ACTION_ANALYZING_WORD = "Analyzing word";

    public static final String CATEGORY_AUTHENTICATION = "Authentication";

    public static final String ACTION_LOGIN = "User Login";

    public static final String ACTION_LOGIN_ERROR = "User Login Error";

    public static final String ACTION_RETURN = "User Return";

    public static final String ACTION_LOGIN_TYPE = "Login type";

    public static final String ACTION_LOGOUT = "User Logout";


    public static synchronized Tracker getTracker(Context context) {
        try {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
            Tracker t = analytics.newTracker(R.xml.global_tracker);
            t.enableAdvertisingIdCollection(true);
            t.enableAutoActivityTracking(true);
            t.enableExceptionReporting(true);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static synchronized void sendLoginType(Context context, String type) {
        sendEvent(context, CATEGORY_AUTHENTICATION, ACTION_LOGIN_TYPE, type);
    }

    public static synchronized void sendUserReturn(Context context, String username) {
        sendEvent(context, CATEGORY_AUTHENTICATION, ACTION_RETURN, username);
    }

    public static synchronized void sendUserLoginError(Context context, String username) {
        sendEvent(context, CATEGORY_AUTHENTICATION, ACTION_LOGIN_ERROR, username);
    }

    public static synchronized void sendUserLogin(Context context, String username) {
        sendEvent(context, CATEGORY_AUTHENTICATION, ACTION_LOGIN, username);
    }

    public static synchronized void sendUserLogout(Context context, String username) {
        sendEvent(context, CATEGORY_AUTHENTICATION, ACTION_LOGOUT, username);
    }

    public static synchronized void sendSelectWord(Context context, String word) {
        sendEvent(context, CATEGORY_ANALYZING, ACTION_SELECT_WORD, word);
    }

    public static synchronized void sendAnalyzingWord(Context context, String word, long score) {
        sendEvent(context, CATEGORY_ANALYZING, ACTION_ANALYZING_WORD, word, score);
    }

    public static synchronized void sendEvent(Context context, String category, String action, String label) {
        try {
            Tracker t = getTracker(context);
            if (t != null)
                t.send(new HitBuilders.EventBuilder()
                        .setCategory(category)
                        .setAction(action)
                        .setLabel(label)
                        .build());
        } catch (Exception e) {

        }
    }

    public static synchronized void sendEvent(Context context, String category, String action, String label, long value) {
        try {
            Tracker t = getTracker(context);
            if (t != null)
                t.send(new HitBuilders.EventBuilder()
                        .setCategory(category)
                        .setAction(action)
                        .setLabel(label)
                        .setValue(value)
                        .build());
        } catch (Exception e) {

        }
    }

}
