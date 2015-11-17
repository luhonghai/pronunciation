package com.cmg.android.bbcaccent.view.dialog;

import android.content.Context;
import android.graphics.Point;
import android.util.TypedValue;

/**
 * Created by luhonghai on 17/11/2015.
 */
public class CenterFullPaddingDialog extends DefaultCenterDialog {

    public CenterFullPaddingDialog(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    protected void initDialog() {
        Point size = getSize();
        final int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getContext().getResources()
                .getDisplayMetrics());
        int w = size.x - padding;
        int h = size.y - Math.round(padding * 1.5f);
        this.getWindow().setLayout(w, h);
    }
}
