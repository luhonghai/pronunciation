package com.cmg.android.bbcaccent.data.sqlite;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.data.dto.PronunciationScore;
import com.cmg.android.bbcaccent.data.dto.PronunciationWord;
import com.cmg.android.bbcaccent.data.dto.SphinxResult;
import com.luhonghai.litedb.LiteDatabaseHelper;
import com.luhonghai.litedb.annotation.LiteDatabase;
import com.luhonghai.litedb.exception.AnnotationNotFound;
import com.luhonghai.litedb.exception.InvalidAnnotationData;

/**
 * Created by luhonghai on 09/10/2015.
 */
@LiteDatabase(name = "freestyle",
        tables = {SphinxResult.PhonemeScore.class,PronunciationScore.class,PronunciationWord.class},
        version = 1)
public class FreestyleDatabaseHelper extends LiteDatabaseHelper {

    public FreestyleDatabaseHelper() throws AnnotationNotFound, InvalidAnnotationData {
        super(MainApplication.getContext());
    }

    public FreestyleDatabaseHelper(DatabaseListener databaseListener) throws AnnotationNotFound, InvalidAnnotationData {
        super(MainApplication.getContext(), databaseListener);
    }
}
