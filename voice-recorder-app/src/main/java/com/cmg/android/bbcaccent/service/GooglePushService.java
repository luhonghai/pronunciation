package com.cmg.android.bbcaccent.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.SplashActivity;
import com.cmg.android.bbcaccent.data.dto.GcmMessage;
import com.cmg.android.bbcaccent.data.dto.UserProfile;
import com.cmg.android.bbcaccent.fragment.Preferences;
import com.cmg.android.bbcaccent.receiver.GooglePushReceiver;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by hai on 12/16/14.
 */
public class GooglePushService extends IntentService {

    public GooglePushService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        SimpleAppLog.info("Receive push message. MessageType: " + messageType);
        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                SimpleAppLog.error("message send error");
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                SimpleAppLog.error("message deleted");
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                String data = "";
                if (extras.containsKey("data")) {
                    data = extras.getString("data");
                    SimpleAppLog.debug("Message data: " + data);
                }
                if (data != null && data.length() > 0) {
                    try {
                        GcmMessage gcmMessage = MainApplication.fromJson(data, GcmMessage.class);
                        SimpleAppLog.info("Gcm message type: " + gcmMessage.getType());
                        switch (gcmMessage.getType()) {
                            case GcmMessage.TYPE_DATABASE:
                                UserProfile userProfile = Preferences.getCurrentProfile();
                                if (userProfile != null && userProfile.getSelectedCountry() != null) {
                                    String countryId = userProfile.getSelectedCountry().getId();
                                    for (GcmMessage.Language language : gcmMessage.getLanguages()) {
                                        SimpleAppLog.debug("Found message for language id " + language.getId());
                                        if (language.getId().equals(countryId)) {
                                            SimpleAppLog.debug("Matched with current selected language");
                                            showNotification(gcmMessage.getType(), "New lesson database", language.getMessage());
                                        }
                                    }
                                }
                                break;
                        }
                    } catch (Exception e) {
                        SimpleAppLog.error("Could not handle gcm message", e);
                    }
                }
            }
        }
        GooglePushReceiver.completeWakefulIntent(intent);
    }

    private void showNotification(int id, String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.app_icon);
        Intent nIntent =
                Intent.makeMainActivity(new ComponentName(this, SplashActivity.class));
        nIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        nIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(notifyIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, builder.build());
    }
}