package com.cmg.android.bbcaccentamt.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/*
 *
 * List of musical notes and their frequencies - http://www.phy.mtu.edu/~suits/notefreqs.html
 * Tone generator - http://www.seventhstring.com/tuningfork/tuningfork.html
 *
 */
public class MySurfaceView extends View {

    // Update screen 24 time per second
    private static final long INVALIDATE_TIMEOUT = 1000 / 24;
    private static final int TEXT_SIZE = 100;
    private static final String TAG = MySurfaceView.class.getSimpleName();

    protected float[] data;

    protected Paint mPaint;
    private Paint dPaint;

    private Bitmap bufferedImage;

    private long startTime = -1;

    private Paint txtPaint;


    public MySurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public MySurfaceView(Context context) {
        super(context);
        init(context);
    }

    protected void init(Context ctx) {
        mPaint = new Paint();
        dPaint = new Paint();
        txtPaint = new Paint();
        txtPaint.setColor(Color.WHITE);
        txtPaint.setTextSize(TEXT_SIZE);
    }

    private int mWidth;
    private int mHeight;

    public void recycle() {
        if (bufferedImage != null) {
            try {
                bufferedImage.recycle();
                bufferedImage = null;
            } catch (Exception ex) {

            }

        }
        startTime = -1;
    }

    private long start;

    public void setData(float[] data, double pitch) {
        this.data = data;
        if (mWidth != getWidth()) {
            recycle();
        }
        if (startTime == -1) {
            startTime = System.currentTimeMillis();
        }
        mWidth = getWidth();
        mHeight = getHeight();
        if (mWidth <= 0) return;
        if (bufferedImage == null) {
            bufferedImage = Bitmap.createBitmap(mWidth,mHeight, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas();
        canvas.setBitmap(bufferedImage);
        canvas.drawColor(Color.BLACK);

        if (mWidth <= 0 || mHeight <= 0) return;
        float halfHeight = mHeight / 2;
        if (pitch != -1) {
            mPaint.setColor(Color.RED);
        } else {
            mPaint.setColor(Color.WHITE);
        }
        for(int i=0; i < data.length ; i+=4){
            canvas.drawLine((int) (data[i] * mWidth),
                    (int) (halfHeight - data[i + 1] * mHeight),
                    (int) (data[i + 2] * mWidth),
                    (int) (halfHeight - data[i + 3] * mHeight),
                    mPaint);
        }

        long now = System.currentTimeMillis();
        if ((now - start) > INVALIDATE_TIMEOUT) {
            start = now;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long now = System.currentTimeMillis();
        long length = now - startTime;
        if (length <= 2000) {
                if (length <= 700) {
                    canvas.drawText("1", 0, 1, getWidth() / 2 - TEXT_SIZE / 4, getHeight() / 2, txtPaint);
                } else if (length <= 1300) {
                    canvas.drawText("2", 0, 1, getWidth() / 2 - TEXT_SIZE / 4, getHeight() / 2, txtPaint);
                } else {
                    canvas.drawText("3", 0, 1, getWidth() / 2 - TEXT_SIZE / 4, getHeight() / 2, txtPaint);
                }
            return;
        }
        if (bufferedImage != null) {
            canvas.drawBitmap(bufferedImage,0,0,dPaint);
        } else {
            canvas.drawColor(Color.BLACK);
        }
    }
}