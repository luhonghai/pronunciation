package com.cmg.android.voicerecorder.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import be.tarsos.dsp.util.PitchConverter;
import be.tarsos.dsp.util.fft.FFT;

/**
 * Created by luhonghai on 9/24/14.
 */
public class SpectrogramView extends View {
    // Update screen 24 time per second
    private static final long INVALIDATE_TIMEOUT = 1000 / 24;

    private static final String TAG = "Spectrogram";
    private Paint mPaint;
    private Paint dPaint;
    private Bitmap bufferedImage;

    public SpectrogramView(Context context) {
        super(context);
        init();
    }

    public SpectrogramView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();


    }
    public SpectrogramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        dPaint = new Paint();
    }

    private int position;
    private int mWidth = -1;
    private int mHeight = -1;

    private float[] amplitudes;
    private double pitch;

    @Override
    protected void onDraw(Canvas canvas) {
        if (bufferedImage != null) {
            canvas.drawBitmap(bufferedImage, 0, 0, dPaint);
        } else {
            canvas.drawColor(Color.BLACK);
        }
    }

    private int frequencyToBin(final double frequency) {
        final double minFrequency = 50; // Hz
        final double maxFrequency = 11000; // Hz
        int bin = 0;
        final boolean logaritmic = true;
        if (frequency != 0 && frequency > minFrequency && frequency < maxFrequency) {
            double binEstimate = 0;
            if (logaritmic) {
                final double minCent = PitchConverter.hertzToAbsoluteCent(minFrequency);
                final double maxCent = PitchConverter.hertzToAbsoluteCent(maxFrequency);
                final double absCent = PitchConverter.hertzToAbsoluteCent(frequency * 2);
                binEstimate = (absCent - minCent) / maxCent * mHeight;
            } else {
                binEstimate = (frequency - minFrequency) / maxFrequency * mHeight;
            }
            if (binEstimate > 700) {
                //System.out.println(binEstimate + "");
            }
            bin = mHeight - 1 - (int) binEstimate;
        }
        return bin;
    }

    private long start;

    public void drawFFT(double pitch,float[] amplitudes,FFT fft){
        this.pitch = pitch;
        this.amplitudes = amplitudes;
        drawToMem();
        long now = System.currentTimeMillis();
        if ((now - start) > INVALIDATE_TIMEOUT) {
            start = now;
            invalidate();
        }
    }

    public void recycle() {
        try {
            if (bufferedImage != null) {
                bufferedImage.recycle();
                bufferedImage = null;
            }
            amplitudes = null;
            pitch = -1;
            position = 0;
        } catch (Exception ex) {
            // ignore
        }
    }


    private void drawToMem() {
        Canvas canvas = new Canvas();
        if (getWidth() != mWidth) {
            recycle();
        }
        mWidth = getWidth();
        mHeight = getHeight();
        if (mWidth <= 0 || mHeight <= 0)
            return;
        boolean isNew = false;
        if (bufferedImage == null) {
            bufferedImage = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
            isNew = true;
        }
        if (mPaint == null) {
            mPaint = new Paint();
        }
        canvas.setBitmap(bufferedImage);
        if (isNew)
            canvas.drawColor(Color.BLACK);
        double maxAmplitude = 0;
        // Log.i(TAG, "Pos: " + position);
        //for every pixel calculate an amplitude
        if (amplitudes != null) {
            float[] pixeledAmplitudes = new float[mHeight];
            //iterate the lage arrray and map to pixels
            for (int i = amplitudes.length / 800; i < amplitudes.length; i++) {
                int pixelY = frequencyToBin(i * 44100 / (amplitudes.length * 8));
                pixeledAmplitudes[pixelY] += amplitudes[i];
                maxAmplitude = Math.max(pixeledAmplitudes[pixelY], maxAmplitude);
            }
            //draw the pixels
            for (int i = 0; i < pixeledAmplitudes.length; i++) {
                int color = Color.BLACK;
                if (maxAmplitude != 0) {
                    final int greyValue = (int) (Math.log1p(pixeledAmplitudes[i] / maxAmplitude) / Math.log1p(1.0000001) * 255);
                    color = Color.rgb(greyValue, greyValue, greyValue);
                    //Log.i(TAG, "Grey value: " + color);
                }
                mPaint.setColor(color);
                canvas.drawRect(position, i, position + 3, i + 1, mPaint);
                //bufferedGraphics.setColor(color);
                //bufferedGraphics.fillRect(position, i, 3, 1);
            }
        }


        if (pitch != -1) {
            int pitchIndex = frequencyToBin(pitch);
            mPaint.setColor(Color.RED);
            //Log.i(TAG, "PITCH: " + pitchIndex);
            canvas.drawRect(position, pitchIndex - 1, position + 3, pitchIndex + 2, mPaint);
            //bufferedGraphics.setColor(Color.RED);
            //bufferedGraphics.fillRect(position, pitchIndex, 1, 1);
            // currentPitch = new StringBuilder("Current frequency: ").append((int) pitch).append("Hz").toString();
        }


        // bufferedGraphics.clearRect(0,0, 190,30);
        // bufferedGraphics.setColor(Color.WHITE);
        // bufferedGraphics.drawString(currentPitch, 20, 20);
        mPaint.setColor(Color.WHITE);
        for(int i = 100 ; i < 500; i += 100){
            int bin = frequencyToBin(i);
            canvas.drawLine(0, bin, 5, bin, mPaint);
            // bufferedGraphics.drawLine(0, bin, 5, bin);
        }

        for(int i = 500 ; i <= 20000; i += 500){
            int bin = frequencyToBin(i);
            canvas.drawLine(0, bin, 5, bin, mPaint);
            // bufferedGraphics.drawLine(0, bin, 5, bin);
        }

        for(int i = 100 ; i <= 20000; i*=10){
            int bin = frequencyToBin(i);
            canvas.drawText(String.valueOf(i), 10, bin, mPaint);
            //  bufferedGraphics.drawString(String.valueOf(i), 10, bin);
        }

        position+=3;
        position = position % mWidth;
    }

}
