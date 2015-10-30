package com.cmg.android.bbcaccent.data.sqlite;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.data.dto.PronunciationScore;
import com.cmg.android.bbcaccent.data.dto.PronunciationWord;
import com.cmg.android.bbcaccent.data.dto.SphinxResult;
import com.cmg.android.bbcaccent.data.dto.lesson.history.LessonScore;
import com.cmg.android.bbcaccent.data.dto.lesson.history.LevelScore;
import com.luhonghai.litedb.LiteDatabaseHelper;
import com.luhonghai.litedb.annotation.LiteDatabase;
import com.luhonghai.litedb.exception.AnnotationNotFound;
import com.luhonghai.litedb.exception.InvalidAnnotationData;

/**
 * Created by luhonghai on 09/10/2015.
 */
@LiteDatabase(name = "lesson_history",
        tables = {SphinxResult.PhonemeScore.class,PronunciationScore.class, LessonScore.class, LevelScore.class},
        version = 2)
public class LessonHistoryDatabaseHelper extends LiteDatabaseHelper {

    public LessonHistoryDatabaseHelper() throws AnnotationNotFound, InvalidAnnotationData {
        super(MainApplication.getContext());
    }

    public LessonHistoryDatabaseHelper(DatabaseListener databaseListener) throws AnnotationNotFound, InvalidAnnotationData {
        super(MainApplication.getContext(), databaseListener);
    }
}
