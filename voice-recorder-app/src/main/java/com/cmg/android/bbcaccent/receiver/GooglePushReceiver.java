package com.cmg.android.bbcaccent.receiver;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.cmg.android.bbcaccent.service.GooglePushService;

/**
 * Created by Hai on 12/16/14.
 */
public class GooglePushReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Handle gcm message
        ComponentName comp = new ComponentName(context.getPackageName(),
                GooglePushService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}