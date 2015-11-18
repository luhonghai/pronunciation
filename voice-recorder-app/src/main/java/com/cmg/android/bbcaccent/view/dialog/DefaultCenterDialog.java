package com.cmg.android.bbcaccent.view.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.Window;

/**
 * Created by luhonghai on 28/10/2015.
 */
public class DefaultCenterDialog extends DefaultDialog {
    public DefaultCenterDialog(Context context, int layoutId) {
        super(context, layoutId);
        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }
}
