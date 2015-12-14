package com.cmg.android.bbcaccent.data.sqlite.lesson;

import android.database.Cursor;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.data.dto.BaseLessonEntity;
import com.cmg.android.bbcaccent.data.dto.SphinxResult;
import com.cmg.android.bbcaccent.data.dto.UserVoiceModel;
import com.cmg.android.bbcaccent.data.dto.lesson.country.Country;
import com.cmg.android.bbcaccent.data.dto.lesson.lessons.LessonCollection;
import com.cmg.android.bbcaccent.data.dto.lesson.level.LessonLevel;
import com.cmg.android.bbcaccent.data.dto.lesson.objectives.Objective;
import com.cmg.android.bbcaccent.data.dto.lesson.question.Question;
import com.cmg.android.bbcaccent.data.dto.lesson.word.IPAMapArpabet;
import com.cmg.android.bbcaccent.data.dto.lesson.word.WordCollection;
import com.cmg.android.bbcaccent.data.sqlite.BaseDatabaseAdapter;
import com.cmg.android.bbcaccent.data.sqlite.QueryHelper;
import com.cmg.android.bbcaccent.utils.RandomHelper;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.luhonghai.litedb.LiteBaseDao;
import com.luhonghai.litedb.LiteFieldType;
import com.luhonghai.litedb.exception.LiteDatabaseException;
import com.luhonghai.litedb.meta.LiteColumnMeta;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
                new String[]{countryId});
    }

    public Cursor cursorAllObjective(String countryId, String levelId) throws LiteDatabaseException {
        BaseDatabaseAdapter<Objective> dbAdapter
                = new BaseDatabaseAdapter<Objective>(MainApplication.getContext().getLessonDatabaseHelper(), Objective.class);
        return dbAdapter.rawQuery(
                QueryHelper.select_all_objective_by_level.toString(),
                new String[]{countryId, levelId});
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
                new String[]{countryId, levelId});
    }

    public Cursor cursorLessonCollectionByObjective(String countryId, String levelId, String objectiveId) throws LiteDatabaseException {
        BaseDatabaseAdapter<LessonCollection> dbAdapter
                = new BaseDatabaseAdapter<LessonCollection>(MainApplication.getContext().getLessonDatabaseHelper(), LessonCollection.class);
        return dbAdapter.rawQuery(
                QueryHelper.select_all_lesson_collection_by_objective.toString(),
                new String[]{countryId, levelId, objectiveId});
    }

    public Cursor cursorLessonCollectionByTest(String countryId, String levelId, String testId) throws LiteDatabaseException {
        BaseDatabaseAdapter<LessonCollection> dbAdapter
                = new BaseDatabaseAdapter<LessonCollection>(MainApplication.getContext().getLessonDatabaseHelper(), LessonCollection.class);
        return dbAdapter.rawQuery(
                QueryHelper.select_all_lesson_collection_by_test.toString(),
                new String[]{countryId, levelId, testId});
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

    public Map<String, IPAMapArpabet> getMapIPAArpabet() throws LiteDatabaseException {
        List<IPAMapArpabet> list =  findObjects(null, null, IPAMapArpabet.class);
        if (list != null && list.size() > 0) {
            Map<String, IPAMapArpabet> map = new HashMap<>();
            for (IPAMapArpabet item : list) {
                String arpabet = item.getArpabet().toUpperCase();
                if (!map.containsKey(arpabet)) {
                    map.put(arpabet, item);
                }
            }
            return map;
        }
        return null;
    }

    public IPAMapArpabet getIPAArpabetTip(UserVoiceModel model) throws LiteDatabaseException {
        List<IPAMapArpabet> list =  findObjects(null, null, IPAMapArpabet.class);
        try {
            if (list == null || list.size() == 0) return null;
            if (model != null) {
                SphinxResult result = model.getResult();
                int index = RandomHelper.getRandomIndex(list.size());
                float lastScore = 1703.89f;
                if (result != null) {
                    List<SphinxResult.PhonemeScore> scores = result.getPhonemeScores();
                    if (scores != null && scores.size() > 0) {
                        for (SphinxResult.PhonemeScore score : scores) {
                            if (score.getTotalScore() < lastScore) {
                                String phoneme = score.getName().toLowerCase();
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i).getArpabet().equalsIgnoreCase(phoneme)) {
                                        index = i;
                                        lastScore = score.getTotalScore();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                return list.get(index);
            } else {
                return list.get(RandomHelper.getRandomIndex(list.size()));
            }
        } catch (Exception e) {
            SimpleAppLog.error("Could not get tip for phoneme",e);
            return list.get(RandomHelper.getRandomIndex(list.size()));
        }
    }

    public Cursor cursorAllIPAMapArpabetByType(IPAMapArpabet.IPAType type) throws LiteDatabaseException {
        BaseDatabaseAdapter<IPAMapArpabet> dbAdapter
                = new BaseDatabaseAdapter<>(MainApplication.getContext().getLessonDatabaseHelper(), IPAMapArpabet.class);
        return dbAdapter.query(
                "[TYPE] = ?", new String[] {type.toString()}
                , null, null, "[INDEXINGTYPE] ASC");
    }

    public LessonLevel getPrevLevelOfLevel(String countryId, String levelId) throws LiteDatabaseException {
        BaseDatabaseAdapter<LessonLevel> dbAdapter = new BaseDatabaseAdapter<>(MainApplication.getContext().getLessonDatabaseHelper(), LessonLevel.class);
        Cursor cursor = dbAdapter.rawQuery(QueryHelper.select_prev_level_of_level.toString(), new String[] {countryId, countryId, levelId});
        try {
            if (cursor != null && cursor.moveToFirst()) {
                return dbAdapter.toObject(cursor, fieldValueParser);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public Objective getNextObjectiveOnCurrentLevel(String countryId, String levelId, String objectiveId) throws LiteDatabaseException {
        BaseDatabaseAdapter<Objective> dbAdapter = new BaseDatabaseAdapter<Objective>(MainApplication.getContext().getLessonDatabaseHelper(), Objective.class);
        Cursor cursor = dbAdapter.rawQuery(QueryHelper.select_next_objective_on_current_level.toString(), new String[] {countryId, levelId, objectiveId});
        try {
            if (cursor != null && cursor.moveToFirst()) {
                return dbAdapter.toObject(cursor, fieldValueParser);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public LessonCollection getFirstLessonOfObjective(String countryId, String levelId, String objectiveId) throws LiteDatabaseException {
        BaseDatabaseAdapter<LessonCollection> dbAdapter = new BaseDatabaseAdapter<LessonCollection>(MainApplication.getContext().getLessonDatabaseHelper(), LessonCollection.class);
        Cursor cursor = dbAdapter.rawQuery(QueryHelper.select_all_lesson_collection_by_objective.toString(), new String[] {countryId, levelId, objectiveId});
        try {
            if (cursor != null && cursor.moveToFirst()) {
                return dbAdapter.toObject(cursor, fieldValueParser);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public LessonCollection getNextLessonOnCurrentObjective(String countryId, String levelId, String objectiveId, String lessonId) throws LiteDatabaseException {
        BaseDatabaseAdapter<LessonCollection> dbAdapter = new BaseDatabaseAdapter<LessonCollection>(MainApplication.getContext().getLessonDatabaseHelper(), LessonCollection.class);
        Cursor cursor = dbAdapter.rawQuery(QueryHelper.select_next_lesson_on_current_objective.toString(), new String[] {countryId, levelId, objectiveId, lessonId});
        try {
            if (cursor != null && cursor.moveToFirst()) {
                return dbAdapter.toObject(cursor, fieldValueParser);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
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
                // ignore date value
                return new Date();
            }
            return null;
        }
    };
}
