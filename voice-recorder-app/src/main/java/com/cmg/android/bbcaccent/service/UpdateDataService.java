package com.cmg.android.bbcaccent.service;

import android.app.IntentService;
import android.content.Intent;

import com.cmg.android.bbcaccent.data.dto.lesson.country.Country;
import com.cmg.android.bbcaccent.data.dto.lesson.word.IPAMapArpabet;
import com.cmg.android.bbcaccent.data.sqlite.lesson.LessonDBAdapterService;
import com.cmg.android.bbcaccent.utils.FileHelper;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.luhonghai.litedb.exception.LiteDatabaseException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
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
            final DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder().cacheOnDisk(true).build();
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
                            mp3Url = arpabet.getMp3UrlShort();
                            if (mp3Url != null && mp3Url.length() > 0) {
                                FileHelper.getCachedFilePath(mp3Url);
                            }
                            String tongueImage = arpabet.getImgTongue();
                            if (tongueImage != null && tongueImage.length() > 0) {
                                ImageLoader.getInstance().loadImage(tongueImage, displayImageOptions, null);
                            }
                        }
                    });
                }
            }
            List<Country> countryList = LessonDBAdapterService.getInstance().toList(
                    LessonDBAdapterService.getInstance().cursorAllCountry(), Country.class);
            if (countryList != null&& countryList.size() > 0) {

                for (Country country : countryList) {
                    String imgUrl = country.getImageUrl();
                    if (imgUrl != null && imgUrl.length() > 0) {
                        SimpleAppLog.debug("Preload country image url " + imgUrl);
                        ImageLoader.getInstance().loadImage(imgUrl, displayImageOptions, null);
                    }
                }
            }
        } catch (LiteDatabaseException e) {
            SimpleAppLog.error("Could not get map arpabet from database",e);
        }
    }
}
