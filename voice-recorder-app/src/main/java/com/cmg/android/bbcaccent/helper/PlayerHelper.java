package com.cmg.android.bbcaccent.helper;

import android.media.MediaPlayer;

import com.cmg.android.bbcaccent.utils.AppLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by luhonghai on 9/30/14.
 */
public class PlayerHelper {
    private final File target;
    private final MediaPlayer.OnCompletionListener listener;

    private MediaPlayer player;

    private FileInputStream fis;

    public PlayerHelper(File target, MediaPlayer.OnCompletionListener listener) {
        this.target = target;
        this.listener = listener;
    }

    public void play() {
        try {

            AppLog.logString("Play file " + target);
            if (!target.exists()) {
                AppLog.logString("Could not found " + target);
                return;
            }
            fis = new FileInputStream(target);
            player = new MediaPlayer();
            player.setDataSource(fis.getFD());
            player.prepare();
            player.start();
            player.setOnCompletionListener(listener);
            player.setLooping(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            if (player != null)
                player.release();
        } catch (Exception ex) {

        }
        try {
            if (fis != null)
                fis.close();
        } catch (Exception ex) {

        }
    }
}
