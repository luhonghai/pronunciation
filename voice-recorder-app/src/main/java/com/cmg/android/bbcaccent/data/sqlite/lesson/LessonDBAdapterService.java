package com.cmg.android.bbcaccent.data.sqlite.lesson;

import android.database.Cursor;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.data.dto.lesson.country.Country;
import com.cmg.android.bbcaccent.data.dto.lesson.lessons.LessonCollection;
import com.cmg.android.bbcaccent.data.dto.lesson.level.LessonLevel;
import com.cmg.android.bbcaccent.data.dto.lesson.objectives.Objective;
import com.cmg.android.bbcaccent.data.sqlite.BaseDatabaseAdapter;
import com.cmg.android.bbcaccent.data.sqlite.QueryHelper;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.luhonghai.litedb.exception.LiteDatabaseException;

import java.util.List;

/**
 * Created by luhonghai on 23/10/2015.
 *
 * The general database adapter of lesson model. No single adapter require for each table.
 */
public class LessonDBAdapterService {

    public List<Country> listAllCountry() {
        BaseDatabaseAdapter<Country> dbAdapter
                = new BaseDatabaseAdapter<Country>(MainApplication.getContext().getLessonDatabaseHelper(), Country.class);
        try {
            return dbAdapter.listAll();
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

    public Cursor cursorAllTest(String countryId, String levelId) throws LiteDatabaseException {
        BaseDatabaseAdapter<Objective> dbAdapter
                = new BaseDatabaseAdapter<Objective>(MainApplication.getContext().getLessonDatabaseHelper(), Objective.class);
        return dbAdapter.rawQuery(
                QueryHelper.select_all_test_by_level.toString(),
                new String[] { countryId, levelId});
    }

    public Cursor cursorLessonCollectionByOjective(String countryId, String levelId, String objectiveId) throws LiteDatabaseException {
        BaseDatabaseAdapter<LessonCollection> dbAdapter
                = new BaseDatabaseAdapter<LessonCollection>(MainApplication.getContext().getLessonDatabaseHelper(), LessonCollection.class);
        return dbAdapter.rawQuery(
                QueryHelper.select_all_lesson_collection_by_objective.toString(),
                new String[] {countryId, levelId, objectiveId});
     }
}
