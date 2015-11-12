package com.cmg.android.bbcaccent.view.cardview;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.cmg.android.bbcaccent.utils.SimpleAppLog;

/**
 * Created by luhonghai on 12/11/2015.
 */
public class GestureDetectorCardView extends CardView {

    private static final int LONG_PRESS_TIMEOUT = 500;

    private static final int SHORT_PRESS_TIMEOUT = 400;

    public interface GestureDetector {

        void onShortPressed();

        void onLongPressed();
    }

    private GestureDetector gestureDetector;

    public GestureDetectorCardView(Context context) {
        super(context);
    }

    public GestureDetectorCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GestureDetectorCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private long lastActionDown;

    private float lastX;

    private float lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastActionDown = System.currentTimeMillis();
                    lastX = event.getX();
                    lastY = event.getY();
                    handler.postDelayed(runnable, LONG_PRESS_TIMEOUT);
                    break;
                case MotionEvent.ACTION_UP:
                    if (lastActionDown != 0) {
                        handler.removeCallbacks(runnable);
                        long now = System.currentTimeMillis();
                        long test = now - lastActionDown;
                        SimpleAppLog.debug("Test press time " + test);
                        if (test <= SHORT_PRESS_TIMEOUT) {
                            gestureDetector.onShortPressed();
                        }
                        lastActionDown = 0;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
//                    if (Math.abs(event.getY() - lastY) > 3 || Math.abs(event.getX() - lastX) > 3) {
//                        handler.removeCallbacks(runnable);
//                    }
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    public void setGestureDetector(GestureDetector gestureDetector) {
        this.gestureDetector = gestureDetector;
    }

    private Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            gestureDetector.onLongPressed();
        }
    };
}
