package com.cmg.android.bbcaccent.data.sqlite.lesson;

import android.database.Cursor;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.data.dto.PronunciationScore;
import com.cmg.android.bbcaccent.data.dto.SphinxResult;
import com.cmg.android.bbcaccent.data.dto.lesson.history.LessonScore;
import com.cmg.android.bbcaccent.data.dto.lesson.history.LevelScore;
import com.cmg.android.bbcaccent.data.sqlite.BaseDatabaseAdapter;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.luhonghai.litedb.exception.LiteDatabaseException;

/**
 * Created by luhonghai on 29/10/2015.
 */
public class LessonHistoryDBAdapterService {

    private static LessonHistoryDBAdapterService instance;

    public static LessonHistoryDBAdapterService getInstance() {
        if (instance == null) {
            instance = new LessonHistoryDBAdapterService();
        }
        return instance;
    }

    public int getObjectiveScore(String username,
                                 String countryId,
                                 String levelId,
                                 String objectiveId) throws LiteDatabaseException {
        BaseDatabaseAdapter<LessonScore> adapter
                = new BaseDatabaseAdapter<LessonScore>(MainApplication.getContext().getLessonHistoryDatabaseHelper(), LessonScore.class);
        Cursor cursor = adapter.rawQuery("select count(*) from " + adapter.getTableName() + " where username = ? and countryId = ? and levelId = ? and objectiveId = ?",
                new String[]{username, countryId, levelId, objectiveId});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        if (count == 0) {
            return -1;
        }
        cursor = adapter.rawQuery("select avg(score) from " + adapter.getTableName() + " where username = ? and countryId = ? and levelId = ? and objectiveId = ?",
                new String[]{username, countryId, levelId, objectiveId});
        cursor.moveToFirst();
        int avgScore = Math.round(cursor.getFloat(0));
        cursor.close();
        return avgScore;
    }

    public LessonScore getLessonScore(String username,
                               String countryId,
                               String levelId,
                               String objectiveId,
                               String lessonId) throws LiteDatabaseException {
        BaseDatabaseAdapter<LessonScore> adapter
                = new BaseDatabaseAdapter<LessonScore>(MainApplication.getContext().getLessonHistoryDatabaseHelper(), LessonScore.class);
        Cursor cursor = adapter.query("username = ? and countryId = ? and levelId = ? and objectiveId = ? and lessonCollectionId = ?",
                new String[]{username, countryId, levelId, objectiveId, lessonId});
        try {
            if (cursor != null && cursor.moveToFirst()) {
                return adapter.toObject(cursor);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    public void addLessonScore(
                                String username,
                                String countryId,
                               String levelId,
                               String objectiveId,
                               String lessonId, int score) throws LiteDatabaseException {
        username = username.toLowerCase();
        BaseDatabaseAdapter<LessonScore> adapter
                = new BaseDatabaseAdapter<LessonScore>(MainApplication.getContext().getLessonHistoryDatabaseHelper(), LessonScore.class);
        LessonScore lessonScore = getLessonScore(username, countryId, levelId, objectiveId, lessonId);
        if (lessonScore != null) {
            lessonScore.setScore(score);
            adapter.update(lessonScore);
        } else {
            lessonScore = new LessonScore();
            lessonScore.setScore(score);
            lessonScore.setLevelId(levelId);
            lessonScore.setCountryId(countryId);
            lessonScore.setObjectiveId(objectiveId);
            lessonScore.setLessonCollectionId(lessonId);
            lessonScore.setUsername(username);
            adapter.insert(lessonScore);
        }
    }

    public void addLevelScore(String username,
                             String countryId,
                             String levelId,
                             int score, boolean enable) throws LiteDatabaseException {
        username = username.toLowerCase();
        BaseDatabaseAdapter<LevelScore> adapter
                = new BaseDatabaseAdapter<LevelScore>(MainApplication.getContext().getLessonHistoryDatabaseHelper(), LevelScore.class);
        LevelScore levelScore = getLevelScore(username, countryId, levelId);
        if (levelScore != null) {
            levelScore.setScore(score);
            if (enable) {
                levelScore.setEnable(true);
            }
            adapter.update(levelScore);
        } else {
            levelScore = new LevelScore();
            levelScore.setScore(score);
            levelScore.setLevelId(levelId);
            levelScore.setCountryId(countryId);
            levelScore.setUsername(username);
            if (enable) {
                levelScore.setEnable(true);
            }
            adapter.insert(levelScore);
        }
    }

    public LevelScore getLevelScore(String username, String countryId, String levelId) throws LiteDatabaseException {
        BaseDatabaseAdapter<LevelScore> adapter
                = new BaseDatabaseAdapter<LevelScore>(MainApplication.getContext().getLessonHistoryDatabaseHelper(), LevelScore.class);
        Cursor cursor = adapter.query("username = ? and countryId = ? and levelId = ?",
                new String[]{username, countryId, levelId});
        try {
            if (cursor != null && cursor.moveToFirst()) {
                return adapter.toObject(cursor);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public void emptyHistory() {
        BaseDatabaseAdapter<PronunciationScore> adapter
                = new BaseDatabaseAdapter<PronunciationScore>(MainApplication.getContext().getLessonHistoryDatabaseHelper(), PronunciationScore.class);
        try {
            adapter.deleteAll();
        } catch (LiteDatabaseException e) {
            SimpleAppLog.error("Could not delete all lesson pronunciation score",e);
        }
        BaseDatabaseAdapter<SphinxResult.PhonemeScore> wAdapter
                = new BaseDatabaseAdapter<SphinxResult.PhonemeScore>(MainApplication.getContext().getLessonHistoryDatabaseHelper(), SphinxResult.PhonemeScore.class);
        try {
            wAdapter.deleteAll();
        } catch (LiteDatabaseException e) {
            SimpleAppLog.error("Could not delete all lesson phoneme score", e);
        }
    }
}
