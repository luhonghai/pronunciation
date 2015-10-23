package com.cmg.android.bbcaccent.data.sqlite.freestyle;

import android.database.Cursor;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.data.dto.SphinxResult;
import com.cmg.android.bbcaccent.data.sqlite.BaseDatabaseAdapter;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.luhonghai.litedb.exception.LiteDatabaseException;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by luhonghai on 12/23/14.
 */
public class PhonemeScoreDBAdapter extends BaseDatabaseAdapter<SphinxResult.PhonemeScore> {

    /**
     * Constructor
     */
    public PhonemeScoreDBAdapter() {
        super(MainApplication.getContext().getFreestyleDatabaseHelper(), SphinxResult.PhonemeScore.class);
    }

    public long insert(SphinxResult.PhonemeScore score, String username, int version) throws LiteDatabaseException {
        score.setUsername(username);
        score.setVersion(version);
        return insert(score);
    }


    public Cursor getAll() throws LiteDatabaseException {
        return query(null, null, null, null, "timestamp DESC", "30");
    }

    public List<SphinxResult.PhonemeScore> getByDataID(String dataID) throws SQLException, ParseException, LiteDatabaseException {
        Cursor mCursor =
                query("data_id = ?", new String[] {dataID}, null, null,
                        "index_phoneme ASC",
                        "30");
        return toList(mCursor);
    }

    public Cursor getByPhoneme(String phoneme) throws LiteDatabaseException {
        return query("phoneme = ?", new String[]{phoneme}, null, null,
                "timestamp DESC",
                "30");
    }

    public Cursor getLatestByPhoneme(String phoneme) throws LiteDatabaseException {
        return query("phoneme = ?",
                new String[]{phoneme},
                null,
                null,
                "timestamp DESC",
                "1");
    }

    public int getLastedVersion(String username) throws LiteDatabaseException {
        int version=0;
        Cursor cursor= rawQuery("SELECT MAX(version) FROM " + getTableName() + " WHERE username=?", new String[]{username});
        if (cursor != null && cursor.moveToFirst()) {
            try {
                String val = cursor.getString(0);
                if (val != null && val.length() > 0)
                    version= Integer.parseInt(val);
            }catch (Exception e){
                SimpleAppLog.error("Could not get max version",e);
            } finally {
                cursor.close();
            }
        }
        return version;
    }
}
