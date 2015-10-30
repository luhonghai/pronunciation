package com.cmg.android.bbcaccent.dictionary;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.utils.FileHelper;

/**
 * Created by luhonghai on 27/10/2015.
 */
public class DictionaryWalkerFactory {

    private static DictionaryWalker instance;

    public static DictionaryWalker getInstance() {
        if (instance == null) {
            instance = new DatabaseDictionaryWalker(FileHelper.getAudioDir(MainApplication.getContext()));
        }
        return instance;
    }
}
