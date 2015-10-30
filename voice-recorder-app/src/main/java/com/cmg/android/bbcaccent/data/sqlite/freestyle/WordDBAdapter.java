package com.cmg.android.bbcaccent.data.sqlite.freestyle;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by luhonghai on 12/23/14.
 */
public class WordDBAdapter {

    private static Map<String, String> PHONEME_CMU_IPA;

    public Map<String,String> getPhonemeCMUvsIPA() {
        if (PHONEME_CMU_IPA == null) {
            Map<String, String> maps = new Hashtable<String, String>();
            try {
                String source = IOUtils.toString(MainApplication.getContext().getResources().openRawResource(R.raw.phoneme_cmu_ipa), "UTF-8");
                String[] lines = source.split("\n");
                for (String line : lines) {
                    if (line.trim().length() > 0) {
                        String[] data = line.split(" ");
                        if (data.length == 2) {
                            maps.put(data[0].trim().toUpperCase(), data[1].trim());
                        }
                    }
                }
            } catch (IOException e) {
                SimpleAppLog.error("Could not get phonemes CMU and IPA", e);
            }
            PHONEME_CMU_IPA = maps;
        }
        return PHONEME_CMU_IPA;
    }
}
