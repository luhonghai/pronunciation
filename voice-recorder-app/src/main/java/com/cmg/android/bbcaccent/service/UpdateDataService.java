package com.cmg.android.bbcaccent.service;

import android.app.IntentService;
import android.content.Intent;

import com.cmg.android.bbcaccent.data.dto.lesson.word.IPAMapArpabet;
import com.cmg.android.bbcaccent.data.sqlite.lesson.LessonDBAdapterService;
import com.cmg.android.bbcaccent.utils.FileHelper;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.luhonghai.litedb.exception.LiteDatabaseException;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by luhonghai on 18/11/2015.
 */
public class UpdateDataService extends IntentService {

    public UpdateDataService() {
        super(UpdateDataService.class.getName());
    }

    ExecutorService executorService = Executors.newFixedThreadPool(3);

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Map<String, IPAMapArpabet> mapArpabetMap = LessonDBAdapterService.getInstance().getMapIPAArpabet();
            if (mapArpabetMap != null && mapArpabetMap.size() > 0) {
                for (final IPAMapArpabet arpabet : mapArpabetMap.values()) {
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            String mp3Url = arpabet.getMp3Url();
                            if (mp3Url != null && mp3Url.length() > 0) {
                                FileHelper.getCachedFilePath(mp3Url);
                            }
                        }
                    });
                }
            }
        } catch (LiteDatabaseException e) {
            SimpleAppLog.error("Could not get map arpabet from database",e);
        }
    }
}
