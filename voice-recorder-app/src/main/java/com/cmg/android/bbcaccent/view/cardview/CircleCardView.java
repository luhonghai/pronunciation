package com.cmg.android.bbcaccent.view.cardview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.cmg.android.bbcaccent.utils.SimpleAppLog;

/**
 * Created by luhonghai on 12/11/2015.
 */
public class CircleCardView extends FrameLayout {

    private Paint paint;

    private int mWidth;

    public CircleCardView(Context context) {
        super(context);
        init(context, null);
    }

    public CircleCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs) {
        paint = new Paint();
        paint.setAntiAlias(true);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, new int[]{
                    android.R.attr.layout_width, android.support.v7.cardview.R.styleable.CardView[android.support.v7.cardview.R.styleable.CardView_cardBackgroundColor]
            });
            mWidth = ta.getDimensionPixelOffset(0, 0);
            paint.setColor(ta.getColor(1, 0));
            SimpleAppLog.debug("CardView configuration width: " + mWidth);
            ta.recycle();
        }
    }

    public void setCardBackgroundColor(int color) {
        paint.setColor(color);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth / 2, paint);
    }

}
