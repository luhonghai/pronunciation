package com.cmg.android.bbcaccentamt.activity.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.cmg.android.bbcaccentamt.R;
import com.jjoe64.graphview.GraphView;

/**
 * Created by luhonghai on 4/2/15.
 */
public class CustomGraphView extends GraphView {

    public CustomGraphView(Context context) {
        super(context);
        initPaint();
    }

    public CustomGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public CustomGraphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPaint();
    }

    private String tintText;

    private Paint tintPaint;

    private Point tintTextPoint;

    private void initPaint() {
        tintPaint = new Paint();
        tintPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        tintPaint.setTextAlign(Paint.Align.RIGHT);
        tintPaint.setTypeface(Typeface.DEFAULT_BOLD);

        tintPaint.setColor(getContext().getResources().getColor(R.color.app_tint));
    }

    public void setTintTextPoint(Point tintTextPoint) {
        this.tintTextPoint = tintTextPoint;
    }

    public void setTintText(String tintText) {
        this.tintText = tintText;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (tintText != null && tintText.length() > 0) {
            float tSize = getHeight() / 5;
            int paddingRight = (tintTextPoint != null) ? tintTextPoint.x : 50;
            do  {
                tintPaint.setTextSize(tSize);
                tSize -= 5;
            } while (tintPaint.measureText(tintText) > getWidth() - paddingRight);

            if (tintTextPoint != null) {
                canvas.drawText(tintText, getWidth() - tintTextPoint.x, getHeight() - tintTextPoint.y, tintPaint);
            } else {
                canvas.drawText(tintText, getWidth() - 50, getHeight() - 60, tintPaint);
            }
        }
        super.onDraw(canvas);
    }


}
