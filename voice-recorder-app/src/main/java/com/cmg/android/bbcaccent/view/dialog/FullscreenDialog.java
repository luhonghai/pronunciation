package com.cmg.android.bbcaccent.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;

import com.cmg.android.bbcaccent.R;

/**
 * Created by luhonghai on 17/11/2015.
 */
public class FullscreenDialog extends Dialog {

    public FullscreenDialog(Context context, int layoutId) {
        super(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.setTitle(null);
        this.setCanceledOnTouchOutside(true);
        this.setCancelable(true);
        this.setContentView(layoutId);
        View dismissButton = this.findViewById(R.id.tv_dismiss);
        if (dismissButton != null) {
            dismissButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FullscreenDialog.this.dismiss();
                }
            });
        }
    }

}
