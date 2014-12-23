package com.cmg.android.voicerecorder.activity.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.cmg.android.voicerecorder.AppLog;

public class RecordingView extends View {

    static class ColorState {
        int innerBorder;
        int innerBackground;
        int outerBorder;
        int outerBackground;
        public ColorState(int innerBorder,
                int innerBackground,
                int outerBorder,
                int outerBackground) {
            this.innerBorder = innerBorder;
            this.innerBackground = innerBackground;
            this.outerBorder = outerBorder;
            this.outerBackground = outerBackground;
        }
    }
    private static final ColorState STATE_GREEN_COLOR
            = new ColorState(Color.argb(255,113, 176,56),Color.argb(255, 156, 202, 124),
                                Color.argb(255,164, 234,150),Color.argb(255, 214, 246, 209));
    private static final ColorState STATE_ORANGE_COLOR
            = new ColorState(Color.argb(255,255, 145,106),Color.argb(255, 255, 191, 161),
                                Color.argb(255,255, 201,147),Color.argb(255, 255, 233, 210));
    private static final ColorState STATE_RED_COLOR
            = new ColorState(Color.argb(255,255, 72,72),Color.argb(255, 255, 156, 156),
                                Color.argb(255,255, 217,217),Color.argb(255, 255, 231, 231));

    public enum Stage {
        GREEN,
        ORANGE,
        RED
    }

    /**
     *  Max - min pitch for draw outer cycle wave
     */
    private static final int MIN_PITCH = 10;

    private static final int MAX_PITCH = 300;

    private static final int PITCH_DOWN_RATE = 5;

    private static final int WAVE_LINE_WIDTH = 2;

    private static final int PITCH_TIMEOUT = 2000;

    private float lastPitch = -1;

    private float pitchLeft = -1;

    private long lastPitchTime = -1;


    private static final int BORDER_SIZE = 1;

    private static final float INNER_RADIUS_PERCENT = .7f;

    private static final float INNER_SCORE_TEXT_SIZE = .9f;

    private static final float INNER_PERCENT_TEXT_SIZE = .3f;

    private static final float INNER_SCORE_PERCENT_MARGIN = 2;

    // Update screen 30 time per second
    private static final long INVALIDATE_TIMEOUT = 1000 / 30;

    private static final String TAG = RecordingView.class.getSimpleName();

    /**
     * Paint for drawing main bitmap
     */
    private Paint dPaint;

    protected Paint mPaint;


    private Bitmap bufferedImage;

    private Paint txtPaint;

    private int mWidth;
    private int mHeight;

    private long startTime = -1;

    private long start;

    protected float[] data;

    private float score = 99;

    public RecordingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    public RecordingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public RecordingView(Context context) {
        super(context);
        init(context);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Make view square
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = width > height ? height : width;
        setMeasuredDimension(size, size);
    }

    protected void init(Context ctx) {
        dPaint = new Paint();

        mPaint = new Paint();
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        txtPaint = new Paint();
        txtPaint.setColor(Color.WHITE);
        txtPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

    }

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

    private boolean initBitmap() {
        if (mWidth != getWidth()) {
            recycle();
        }
        mWidth = getWidth();
        mHeight = getHeight();
        if (mHeight <= 0 || mWidth <= 0) return false;
        if (bufferedImage == null) {
            bufferedImage = Bitmap.createBitmap(mWidth,mHeight, Bitmap.Config.ARGB_8888);
        }

        if (startTime == -1) {
            startTime = System.currentTimeMillis();
        }

        return true;
    }

    private void doInvalidate() {
        long now = System.currentTimeMillis();
        if ((now - start) > INVALIDATE_TIMEOUT) {
            start = now;
            invalidate();
        }
    }

    private Canvas drawBackground() {
        Canvas canvas = new Canvas(bufferedImage);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        return canvas;
    }

    private void drawCycle(Stage stage) {
        ColorState colorState = null;
        switch (stage) {
            case RED:
                colorState = STATE_RED_COLOR;
                break;
            case ORANGE:
                colorState = STATE_ORANGE_COLOR;
                break;
            case GREEN:
            default:
                colorState = STATE_GREEN_COLOR;
                 break;
        }
        Canvas canvas = new Canvas(bufferedImage);
        int center = mWidth / 2;
        float innerCycleRadius = INNER_RADIUS_PERCENT * (mWidth / 2);
        // Draw outer border
        mPaint.setColor(colorState.outerBorder);
        canvas.drawCircle(center, center, center, mPaint);

        // Draw outer background
        mPaint.setColor(colorState.outerBackground);
        canvas.drawCircle(center, center , center - BORDER_SIZE, mPaint);

        // Draw inner border
        mPaint.setColor(colorState.innerBorder);
        canvas.drawCircle(center, center, innerCycleRadius, mPaint);

        // Draw inner background
        mPaint.setColor(colorState.innerBackground);
        canvas.drawCircle(center, center , innerCycleRadius - BORDER_SIZE, mPaint);
    }

    private void drawScore(int score, boolean showPercent) {
        String mScore = Integer.toString(score);
        Canvas canvas = new Canvas(bufferedImage);
        float innerCycleRadius = INNER_RADIUS_PERCENT * (mWidth / 2);
        float scoreTextSize = innerCycleRadius * INNER_SCORE_TEXT_SIZE;
        float percentTextSize = scoreTextSize * INNER_PERCENT_TEXT_SIZE;
        float moveLeft = showPercent ? percentTextSize / 4 : 0;
        int center = mWidth / 2;


        txtPaint.setTextAlign(Paint.Align.CENTER);
        txtPaint.setTextSize(scoreTextSize);
        txtPaint.setTypeface(Typeface.DEFAULT_BOLD);

        float scoreWidth = txtPaint.measureText(mScore);
        float scoreHalfHeight = (txtPaint.descent() + txtPaint.ascent()) / 2;
        // Draw center score
        int yPos = (int) (center - scoreHalfHeight);
        canvas.drawText(mScore, center - moveLeft, yPos, txtPaint);
        if (showPercent) {
            // Draw percent char
            txtPaint.setTextSize(percentTextSize);
            txtPaint.setTypeface(Typeface.DEFAULT);
            float percentWidth = txtPaint.measureText("%");
            int xPos = (int) (center + scoreWidth / 2 + percentWidth / 2 + INNER_SCORE_PERCENT_MARGIN);
            yPos = (int) (center - scoreHalfHeight);
            canvas.drawText("%", xPos - moveLeft, yPos, txtPaint);
        }
    }

    private void drawRecording(float[] data, double pitch) {
        Canvas canvas = new Canvas(bufferedImage);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        int center = mWidth / 2;
        float innerCycleRadius = INNER_RADIUS_PERCENT * (mWidth / 2);

        // Draw outer cycle wave
        float pPitch =(float) pitch;
        if (pPitch <= 0) pPitch = MIN_PITCH;
        if (pPitch > MAX_PITCH) pPitch = MAX_PITCH;

        if (pitch == -1) {
            if (pitchLeft >= 0) {
                long timeout = PITCH_TIMEOUT - (System.currentTimeMillis() - lastPitchTime);
                if (timeout > 0) {
                    float rate = (INVALIDATE_TIMEOUT * pitchLeft) / timeout;
                    pitchLeft -= rate;
                    pPitch = pitchLeft;
                } else {
                    pPitch = pitchLeft = lastPitch = -1;
                }
            }
        } else {
            lastPitch = pPitch;
            pitchLeft = lastPitch;
            lastPitchTime = System.currentTimeMillis();
        }

        pPitch = (pPitch / MAX_PITCH);

        RadialGradient gradient = new RadialGradient(center, center, innerCycleRadius, 0xFFffddbe, 0xFFfef5ed,
                Shader.TileMode.CLAMP);
        Paint p = new Paint();
        p.setFlags(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFFffddbe);
        canvas.drawCircle(center, center, pPitch * (center - innerCycleRadius) + innerCycleRadius, p);
        p.setDither(true);
        p.setShader(gradient);
        canvas.drawCircle(center, center, pPitch * (center - innerCycleRadius) + innerCycleRadius - 2, p);

        // Draw inner cycle red
        gradient = new RadialGradient(center, center, innerCycleRadius, 0xFFFFB1AD, 0xFFEC2221,
                Shader.TileMode.CLAMP);
        p = new Paint();
        p.setFlags(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFFEC2221);
        canvas.drawCircle(center, center,innerCycleRadius, p);
        p.setDither(true);
        p.setShader(gradient);
        canvas.drawCircle(center, center,innerCycleRadius-BORDER_SIZE * 3, p);

        // Draw wave
        Paint wavePaint = new Paint();
        wavePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        wavePaint.setColor(Color.WHITE);
        wavePaint.setStrokeWidth(WAVE_LINE_WIDTH);
        // 2 * width^2 = (2 * r) ^ 2 => width = sqrt(2 * r^2)
        int waveSquareWidth = (int) Math.sqrt((innerCycleRadius * innerCycleRadius) * 2);
        int wavePosX = center - waveSquareWidth / 2;
        if (data == null || data.length == 0) {

        } else {
            for (int i = 0; i < data.length; i += 4) {
                int startX = (int) (wavePosX + data[i] * waveSquareWidth);
                int startY = (int) (center - fixWaveDataHeight(data[i+1]) * waveSquareWidth);
                int stopX = (int) (wavePosX + data[i + 2] * waveSquareWidth);
                int stopY = (int) (center - fixWaveDataHeight(data[i + 3]) * waveSquareWidth);
                canvas.drawLine(startX,startY,stopX,stopY,wavePaint);
            }
        }
    }

    private float fixWaveDataHeight(float data) {
        if (data <= -0.5f)
            data = -0.5f;
        else if (data >= 0.5f) {
            data = 0.5f;
        }
        return data;
    }

    public void setData(float[] data, double pitch) {
        if (!initBitmap()) return;

        this.data = data;
        drawRecording(data, pitch);

        doInvalidate();
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void showScore() {
        if (initBitmap()) {
            drawBackground();
            if (score >= 80.0) {
                drawCycle(Stage.GREEN);
            } else if (score >= 45.0) {
                drawCycle(Stage.ORANGE);
            } else {
                drawCycle(Stage.RED);
            }
            drawScore(Math.round(score), true);
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bufferedImage == null) {
            showScore();
        }
        canvas.drawBitmap(bufferedImage,0,0,dPaint);
    }
}
