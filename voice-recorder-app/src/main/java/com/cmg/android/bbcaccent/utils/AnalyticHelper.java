package com.cmg.android.bbcaccent.utils;

import android.content.Context;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by luhonghai on 4/17/15.
 */
public class AnalyticHelper {

    public enum Category {
        DEFAULT("Default"),
        ANALYZING("Analyzing"),
        AUTHENTICATION("Authentication"),
        LESSON("Lesson"),
        SUBSCRIPTION("Subscription"),
        HELP("Help"),
        TIP("Tips"),
        FEEDBACK("Feedback")
        ;
        String name;

        Category(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public enum Action {
        SELECT_WORD("Select word"),
        SELECT_WORD_NOT_IN_BEEP("Select word not in beep"),
        ANALYZING_WORD("Analyzing word"),
        LOGIN("User Login"),
        LOGIN_ERROR("User Login Error"),
        USER_RETURN("User Return"),
        LOGIN_TYPE("Login type"),
        USER_LOGOUT("User Logout"),
        SELECT_LEVEL("Select level"),
        SELECT_OBJECTIVE("Select objective"),
        SELECT_TEST("Select test"),
        SELECT_QUESTION("Select question"),
        SELECT_LESSON_COLLECTION("Select lesson collection"),
        LESSON_SUCCESS("Lesson success"),
        LESSON_FAILED("Lesson failed"),
        TEST_SUCCESS("Test success"),
        TEST_FAILED("Test failed"),
        USE_LICENCE("Use new licence"),
        SWITCH_LICENCE("Switch licence"),
        BUY_SUBSCRIPTION("Buy subscription"),
        BLOCK_ACTIVATION("Block activation"),
        SELECT_HELP_ITEM("Select help item"),
        VIEW_TIP("View tip"),
        PLAY_TIP_AUDIO("Play tip audio"),
        SEND_FEEDBACK("Send Feedback"),
        OPEN_FEEDBACK_PAGE("Open Feedback page"),
        SELECT_MENU_ITEM("Select menu item"),
        SELECT_FRAGMENT("Select fragment"),
        RECEIVE_GCM("Receive GCM")
        ;
        String name;

        Action(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static synchronized Tracker getTracker(Context context) {
        try {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
            Tracker t = analytics.newTracker(R.xml.global_tracker);
            t.enableAdvertisingIdCollection(true);
            t.enableAutoActivityTracking(true);
            t.enableExceptionReporting(true);
            t.setEncoding("UTF-8");
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static synchronized void sendLoginType(Context context, String type) {
        sendEvent(Category.AUTHENTICATION.toString(), Action.LOGIN_TYPE.toString(), type);
    }

    public static synchronized void sendUserReturn(Context context, String username) {
        sendEvent(Category.AUTHENTICATION.toString(), Action.USER_RETURN.toString(), username);
    }

    public static synchronized void sendUserLoginError(Context context, String username) {
        sendEvent(Category.AUTHENTICATION.toString(), Action.LOGIN_ERROR.toString(), username);
    }

    public static synchronized void sendUserLogin(Context context, String username) {
        sendEvent(Category.AUTHENTICATION.toString(), Action.LOGIN.toString(), username);
    }

    public static synchronized void sendUserLogout(Context context, String username) {
        sendEvent(Category.AUTHENTICATION.toString(), Action.USER_LOGOUT.toString(), username);
    }

    public static synchronized void sendSelectWord(Context context, String word) {
        sendEvent( Category.ANALYZING.toString(), Action.SELECT_WORD.toString(), word);
    }

    public static synchronized void sendSelectWordNotInBeep(Context context, String word) {
        sendEvent(Category.ANALYZING.toString(), Action.SELECT_WORD_NOT_IN_BEEP.toString(), word);
    }

    public static synchronized void sendAnalyzingWord(Context context, String word, long score) {
        sendEvent(Category.ANALYZING.toString(), Action.ANALYZING_WORD.toString(), word, score);
    }

    public static synchronized void sendEvent(String category, String action, String label) {
        sendEvent(MainApplication.getContext(), category, action, label);
    }

    public static synchronized void sendEvent(Context context, String category, String action, String label) {
        sendEvent(context, category, action, label, 0);
    }

    public static synchronized void sendEvent(String category, String action, String label, long value) {
        sendEvent(MainApplication.getContext(), category, action, label, value);
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

    public static synchronized void sendEvent(Category category, Action action, String label) {
        sendEvent(category.toString(), action.toString(), label, 0);
    }

    public static synchronized void sendEvent(Category category, Action action, String label, long value) {
        sendEvent(category.toString(), action.toString(), label, value);
    }

}
