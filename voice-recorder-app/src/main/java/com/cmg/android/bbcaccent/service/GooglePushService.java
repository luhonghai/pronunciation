package com.cmg.android.bbcaccent.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.cmg.android.bbcaccent.SplashActivity;
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
                Intent homeIntent = new Intent(this, SplashActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                homeIntent.putExtra("from", "GcmIntentService");
                startActivity(homeIntent);
            }
        }
        GooglePushReceiver.completeWakefulIntent(intent);
    }
}