package com.cmg.android.bbcaccent.view.imageview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.cmg.android.bbcaccent.utils.SimpleAppLog;

/**
 * Created by luhonghai on 17/11/2015.
 */
public class PointingTitleImageView extends ImageView {

    private int lastWidth = 0;

    public PointingTitleImageView(Context context) {
        super(context);
    }

    public PointingTitleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PointingTitleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec) / 3;
        if (lastWidth == 0)
            lastWidth = width;
        if (width < lastWidth) {
            width = lastWidth;
        }
        int height = Math.round((float) width * 433 / 381);
        SimpleAppLog.debug("Pointing title width " + width + ". height: " + height);
        setMeasuredDimension(width, height);
    }
}
