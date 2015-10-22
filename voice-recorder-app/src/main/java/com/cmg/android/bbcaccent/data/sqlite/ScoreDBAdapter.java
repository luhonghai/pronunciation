package com.cmg.android.bbcaccent.data.sqlite;

import android.database.Cursor;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.data.dto.PronunciationScore;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.luhonghai.litedb.LiteBaseDao;
import com.luhonghai.litedb.exception.LiteDatabaseException;

/**
 * Created by luhonghai on 12/23/14.
 */
public class ScoreDBAdapter extends BaseDatabaseAdapter<PronunciationScore> {

    /**
     * Constructor
     *
     */
    public ScoreDBAdapter() {
        super(MainApplication.getContext().getFreestyleDatabaseHelper(), PronunciationScore.class);
    }

    public Cursor getAllTime() throws LiteDatabaseException {
        return query(
                null,
                null,
                null,
                null,
                "timestamp DESC",
                null);
    }

    public Cursor getAll(String username) throws LiteDatabaseException {
        return query(
                "username = ?",
                new String[]{username},
                null,
                null,
                "timestamp DESC",
                "30");
    }


    public Cursor getByWord(String word, String username) throws LiteDatabaseException {
        return query(
                "word = ? and username = ?",
                new String[]{word, username},
                null,
                null,
                "timestamp DESC",
                "30");
    }

    public Cursor getLatestByWord(String word) throws LiteDatabaseException {
        return query(
                "word = ?",
                new String[]{word},
                null,
                null,
                "timestamp DESC",
                "1");
    }

    public PronunciationScore getByDataID(String dataID) throws LiteDatabaseException {
        Cursor mCursor =
                query("data_id = ?",
                        new String[]{dataID},
                        null,
                        null,
                        null,
                        null);

        if (mCursor != null && mCursor.moveToFirst()) {
            PronunciationScore score = toObject(mCursor);
            mCursor.close();
            return score;
        }
        return null;
    }

    public int getLastedVersion(String username) throws LiteDatabaseException {
        int version=0;
        Cursor cursor= rawQuery("SELECT MAX(version) FROM " + getTableName() + " where username= ? ", new String[] {username});
        if (cursor != null && cursor.moveToFirst()) {
            try {
                String val = cursor.getString(0);
                if (val != null && val.length() > 0)
                    version = Integer.parseInt(val);
            } catch (Exception e) {
                SimpleAppLog.error("Could not get max version",e);
            }
        }
        return version;
    }
}
