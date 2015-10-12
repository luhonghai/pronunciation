package com.cmg.android.bbcaccent.data.sqlite;

import android.database.Cursor;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.data.dto.PronunciationWord;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.luhonghai.litedb.LiteBaseDao;
import com.luhonghai.litedb.exception.LiteDatabaseException;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by luhonghai on 12/23/14.
 */
public class WordDBAdapter extends LiteBaseDao<PronunciationWord> {

    /**
     * Constructor
     */
    public WordDBAdapter() {
        super(MainApplication.getContext().getFreestyleDatabaseHelper(), PronunciationWord.class);
    }

    public void checkWord() throws LiteDatabaseException {
        if (count() == 0) {
            String[] words = MainApplication.getContext().getResources().getStringArray(R.array.words_list);
            List<PronunciationWord> list = new ArrayList<PronunciationWord>();
            for (String word : words) {
                String[] tmp = word.split("\\|");
                if (tmp.length == 3) {
                    String w = tmp[0].trim().toLowerCase();
                    String p = tmp[1].trim();
                    boolean b = Boolean.valueOf(tmp[2].trim());
                    list.add(new PronunciationWord(w, p, b));
                }
            }
            insert(list);
        }
    }


    public Cursor search(String s) throws LiteDatabaseException {
        return query("word LIKE ?", new String[] { s + "%"});
    }

    public Cursor getAll() throws LiteDatabaseException {
        return query(null ,null);
    }

    public String getPronunciation(String word) throws LiteDatabaseException {
        Cursor mCursor = query("word = ?",
                new String[]{word.toLowerCase()});
        if (mCursor != null && mCursor.moveToFirst()) {
            PronunciationWord pw = toObject(mCursor);
            mCursor.close();
            return pw.getPronunciation();
        }
        return "";
    }

    public boolean isBeep(String word) throws LiteDatabaseException {
        Cursor mCursor = query("word = ?" ,
                new String[] {word.toLowerCase()});
        if (mCursor != null && mCursor.moveToFirst()) {
            PronunciationWord pw = toObject(mCursor);
            mCursor.close();
            return pw.isBeep();
        }
        return false;
    }

    public Map<String,String> getPhonemeCMUvsIPA() {
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
            SimpleAppLog.error("Could not get phonemes CMU and IPA",e);
        }
        return maps;
    }

    @Override
    @Deprecated
    public void open() {
        //
    }

    @Override
    @Deprecated
    public void close() {
        //
    }
}
