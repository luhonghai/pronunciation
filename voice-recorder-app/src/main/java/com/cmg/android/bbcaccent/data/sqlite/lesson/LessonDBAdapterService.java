package com.cmg.android.bbcaccent.data.sqlite.lesson;

import android.database.Cursor;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.data.dto.BaseLessonEntity;
import com.cmg.android.bbcaccent.data.dto.lesson.country.Country;
import com.cmg.android.bbcaccent.data.dto.lesson.lessons.LessonCollection;
import com.cmg.android.bbcaccent.data.dto.lesson.level.LessonLevel;
import com.cmg.android.bbcaccent.data.dto.lesson.objectives.Objective;
import com.cmg.android.bbcaccent.data.dto.lesson.question.Question;
import com.cmg.android.bbcaccent.data.dto.lesson.word.WordCollection;
import com.cmg.android.bbcaccent.data.sqlite.BaseDatabaseAdapter;
import com.cmg.android.bbcaccent.data.sqlite.QueryHelper;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.luhonghai.litedb.LiteBaseDao;
import com.luhonghai.litedb.LiteFieldType;
import com.luhonghai.litedb.exception.LiteDatabaseException;
import com.luhonghai.litedb.meta.LiteColumnMeta;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by luhonghai on 23/10/2015.
 *
 * The general database adapter of lesson model. No single adapter require for each table.
 */
public class LessonDBAdapterService {

    private static LessonDBAdapterService instance;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());

    public static LessonDBAdapterService getInstance() {
        if (instance == null)
            instance = new LessonDBAdapterService();
        return instance;
    }

    protected LessonDBAdapterService() {}

    public Cursor cursorAllCountry() {
        BaseDatabaseAdapter<Country> dbAdapter
                = new BaseDatabaseAdapter<Country>(MainApplication.getContext().getLessonDatabaseHelper(), Country.class);
        try {
            return dbAdapter.rawQuery("SELECT ROWID as _id, * FROM " + dbAdapter.getTableName() + " ORDER BY [NAME] ASC", null);
        } catch (LiteDatabaseException e) {
            SimpleAppLog.error("Could not list all country",e);
        }
        return null;
    }

    public Cursor cursorAllLevel(String countryId) throws LiteDatabaseException {
        BaseDatabaseAdapter<LessonLevel> dbAdapter
                = new BaseDatabaseAdapter<LessonLevel>(MainApplication.getContext().getLessonDatabaseHelper(), LessonLevel.class);
        return dbAdapter.rawQuery(
                QueryHelper.select_all_level_by_country.toString(),
                new String[] { countryId });
    }

    public Cursor cursorAllObjective(String countryId, String levelId) throws LiteDatabaseException {
        BaseDatabaseAdapter<Objective> dbAdapter
                = new BaseDatabaseAdapter<Objective>(MainApplication.getContext().getLessonDatabaseHelper(), Objective.class);
        return dbAdapter.rawQuery(
                QueryHelper.select_all_objective_by_level.toString(),
                new String[] { countryId, levelId});
    }

    public List<Question> listAllQuestionByLessonCollection(String lessonCollectionId) throws LiteDatabaseException {
        BaseDatabaseAdapter<Question> dbAdapter
                = new BaseDatabaseAdapter<Question>(MainApplication.getContext().getLessonDatabaseHelper(), Question.class);
        return dbAdapter.toList(dbAdapter.rawQuery(
                QueryHelper.select_all_question_by_lesson_collection.toString(),
                new String[]{lessonCollectionId}), fieldValueParser);
    }

    public Cursor cursorAllTest(String countryId, String levelId) throws LiteDatabaseException {
        BaseDatabaseAdapter<Objective> dbAdapter
                = new BaseDatabaseAdapter<Objective>(MainApplication.getContext().getLessonDatabaseHelper(), Objective.class);
        return dbAdapter.rawQuery(
                QueryHelper.select_all_test_by_level.toString(),
                new String[] { countryId, levelId});
    }

    public Cursor cursorLessonCollectionByObjective(String countryId, String levelId, String objectiveId) throws LiteDatabaseException {
        BaseDatabaseAdapter<LessonCollection> dbAdapter
                = new BaseDatabaseAdapter<LessonCollection>(MainApplication.getContext().getLessonDatabaseHelper(), LessonCollection.class);
        return dbAdapter.rawQuery(
                QueryHelper.select_all_lesson_collection_by_objective.toString(),
                new String[] {countryId, levelId, objectiveId});
    }

    public Cursor cursorLessonCollectionByTest(String countryId, String levelId, String testId) throws LiteDatabaseException {
        BaseDatabaseAdapter<LessonCollection> dbAdapter
                = new BaseDatabaseAdapter<LessonCollection>(MainApplication.getContext().getLessonDatabaseHelper(), LessonCollection.class);
        return dbAdapter.rawQuery(
                QueryHelper.select_all_lesson_collection_by_test.toString(),
                new String[] {countryId, levelId, testId});
    }

    public List<WordCollection> getAllWordsOfQuestion(String questionId) throws LiteDatabaseException {
        BaseDatabaseAdapter<WordCollection> dbAdapter
                = new BaseDatabaseAdapter<WordCollection>(MainApplication.getContext().getLessonDatabaseHelper(), WordCollection.class);
        return dbAdapter.toList(dbAdapter.rawQuery(QueryHelper.select_all_words_by_question.toString(), new String[]{questionId}), fieldValueParser);
    }

    public Cursor searchWord(String search) throws LiteDatabaseException {
        BaseDatabaseAdapter<WordCollection> dbAdapter
                = new BaseDatabaseAdapter<WordCollection>(MainApplication.getContext().getLessonDatabaseHelper(), WordCollection.class);
        return dbAdapter.rawQuery(QueryHelper.search_word.toString(), new String[]{search + "%"});
    }

    public <T extends BaseLessonEntity> T findObject(String selection, String[] args, Class<T> clazz) throws LiteDatabaseException {
        BaseDatabaseAdapter<T> dbAdapter
                = new BaseDatabaseAdapter<T>(MainApplication.getContext().getLessonDatabaseHelper(), clazz);
        Cursor cursor = dbAdapter.query(selection, args);
        try {
            if (cursor.moveToFirst())
                return dbAdapter.toObject(cursor, fieldValueParser);
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    public <T extends BaseLessonEntity> List<T> findObjects(String selection, String[] args, Class<T> clazz) throws LiteDatabaseException {
        BaseDatabaseAdapter<T> dbAdapter
                = new BaseDatabaseAdapter<T>(MainApplication.getContext().getLessonDatabaseHelper(), clazz);
        Cursor cursor = dbAdapter.query(selection, args);
        try {
            if (cursor.moveToFirst())
                return dbAdapter.toList(cursor, fieldValueParser);
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    public <T extends BaseLessonEntity> T toObject(Cursor cursor, Class<T> clazz) throws LiteDatabaseException {
        BaseDatabaseAdapter<T> dbAdapter = new BaseDatabaseAdapter<T>(MainApplication.getContext().getLessonDatabaseHelper(), clazz);
        return dbAdapter.toObject(cursor, fieldValueParser);
    }

    public <T extends BaseLessonEntity> List<T> toList(Cursor cursor, Class<T> clazz) throws LiteDatabaseException {
        if (cursor != null && cursor.getCount() > 0) {
            BaseDatabaseAdapter<T> dbAdapter = new BaseDatabaseAdapter<T>(MainApplication.getContext().getLessonDatabaseHelper(), clazz);
            return dbAdapter.toList(cursor, fieldValueParser);
        } else {
            return null;
        }
    }

    private LiteBaseDao.FieldValueParser fieldValueParser = new LiteBaseDao.FieldValueParser() {
        @Override
        public Object parse(Cursor cursor, String fieldName, int index, LiteColumnMeta columnMeta) {
            if (columnMeta.getFieldType() == LiteFieldType.BOOLEAN) {
                // \0 = false, empty or 1 = true
                String rawValue = cursor.getString(index);
                SimpleAppLog.debug("Field name " + fieldName + ". Raw value = '" + rawValue +"'");
                return (rawValue == null || rawValue.trim().length() == 0 || rawValue.trim().equalsIgnoreCase("1"));
            } else if (columnMeta.getFieldType() == LiteFieldType.DATE) {
                // date
                return new Date();
            }
            return null;
        }
    };
}
