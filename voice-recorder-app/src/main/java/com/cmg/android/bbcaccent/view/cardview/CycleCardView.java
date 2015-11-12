package com.cmg.android.bbcaccent.view.cardview;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

/**
 * Created by luhonghai on 12/11/2015.
 */
public class CycleCardView extends CardView {

    public CycleCardView(Context context) {
        super(context);
    }

    public CycleCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CycleCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(width, height);
        setMeasuredDimension(size, size);
        setRadius(size / 2);
    }
}
