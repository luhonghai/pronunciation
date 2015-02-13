package com.cmg.android.voicerecorder.utils;

import java.util.Random;

/**
 * Created by cmg on 2/13/15.
 */
public class RandomHelper {

    public static int getRandomIndex(int size) {
        int n = new Random().nextInt(size);
        if (n >= size) {
            n = size - 1;
        } else if (n < 0) {
            n = 0;
        }
        return n;
    }
}
