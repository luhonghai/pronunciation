package com.cmg.android.bbcaccent.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.cmg.android.bbcaccent.R;

/**
 * Created by luhonghai on 27/10/2015.
 */
public class DefaultDialog extends Dialog {

    public DefaultDialog(Context context, int layoutId) {
        super(context, R.style.Theme_WhiteDialog);
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
                    DefaultDialog.this.dismiss();
                }
            });
        }
        initDialog();
    }

    private void initDialog() {
        int fullWidth;
        int fulHeight;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Display display = ((WindowManager) getContext()
                    .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            fullWidth = size.x;
            fulHeight = size.y;
        } else {
            Display display = ((WindowManager) getContext()
                    .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            fullWidth = display.getWidth();
            fulHeight = display.getHeight();
        }

        final int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getContext().getResources()
                .getDisplayMetrics());

        int w = fullWidth - padding;
        //int h = dialog.getWindow().getAttributes().height;
        int h = fulHeight - padding;
        this.getWindow().setLayout(w, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}
