package com.cmg.android.voicerecorder.utils;

import android.graphics.Color;

public class ColorHelper {

    public static int COLOR_GRAY = 0xff929292;
    public static int COLOR_GREEN = 0xff579e11;
    public static int COLOR_ORANGE = 0xffff7548;
    public static int COLOR_RED = 0xffff3333;

    public static String COLOR_GRAY_STRING = "#929292";
    public static String COLOR_GREEN_STRING = "#579e11";
    public static String COLOR_ORANGE_STRING = "#ff7548";
    public static String COLOR_RED_STRING = "#ff3333";
    public static String COLOR_DEFAULT_STRING = "#003da7";


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
