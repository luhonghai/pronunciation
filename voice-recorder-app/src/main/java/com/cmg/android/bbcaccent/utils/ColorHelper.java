package com.cmg.android.bbcaccent.utils;

import android.graphics.Color;

import com.cmg.android.bbcaccent.MainApplication;

public class ColorHelper {

    public static int getColor(int colorId) {
        return MainApplication.getContext().getResources().getColor(colorId);
    }


    public static int generateGradient(float radio, int to, int from) {
        int red1 = from >> 16;
        int green1 = (from >> 8) & 0xFF;
        int blue1  = from & 0xFF;

        int red2 = to >> 16;
        int green2 = (to >> 8) & 0xFF;
        int blue2  = to & 0xFF;

        int outred = (int) (radio * red1 + (1-radio) * red2);
        int outgreen = (int) (radio * green1 + (1-radio) * green2);
        int outblue = (int) (radio * blue1 + (1-radio) * blue2);
        return Color.argb(255, outred, outgreen, outblue);
    }
}
