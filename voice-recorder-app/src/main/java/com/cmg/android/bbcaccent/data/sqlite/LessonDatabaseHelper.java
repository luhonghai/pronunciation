package com.cmg.android.bbcaccent.data.sqlite;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.data.dto.lesson.country.Country;
import com.cmg.android.bbcaccent.data.dto.lesson.country.CountryMappingCourse;
import com.cmg.android.bbcaccent.data.dto.lesson.course.Course;
import com.cmg.android.bbcaccent.data.dto.lesson.course.LevelMappingObjectiveTest;
import com.cmg.android.bbcaccent.data.dto.lesson.course.CourseMappingLevel;
import com.cmg.android.bbcaccent.data.dto.lesson.lessons.LessonCollection;
import com.cmg.android.bbcaccent.data.dto.lesson.lessons.LessonMappingQuestion;
import com.cmg.android.bbcaccent.data.dto.lesson.level.LessonLevel;
import com.cmg.android.bbcaccent.data.dto.lesson.objectives.Objective;
import com.cmg.android.bbcaccent.data.dto.lesson.objectives.ObjectiveMapping;
import com.cmg.android.bbcaccent.data.dto.lesson.question.Question;
import com.cmg.android.bbcaccent.data.dto.lesson.question.WeightForPhoneme;
import com.cmg.android.bbcaccent.data.dto.lesson.question.WordOfQuestion;
import com.cmg.android.bbcaccent.data.dto.lesson.test.LessonTest;
import com.cmg.android.bbcaccent.data.dto.lesson.test.TestMapping;
import com.cmg.android.bbcaccent.data.dto.lesson.word.IPAMapArpabet;
import com.cmg.android.bbcaccent.data.dto.lesson.word.WordCollection;
import com.cmg.android.bbcaccent.data.dto.lesson.word.WordMappingPhonemes;
import com.luhonghai.litedb.LiteDatabaseHelper;
import com.luhonghai.litedb.annotation.LiteDatabase;
import com.luhonghai.litedb.exception.AnnotationNotFound;
import com.luhonghai.litedb.exception.InvalidAnnotationData;

/**
 * Created by luhonghai on 23/10/2015.
 */

@LiteDatabase(
        name = "lesson",
        version = 1,
        nameType = LiteDatabase.NameType.UPPERCASE,
        tables = {
                //country
                Country.class,
                CountryMappingCourse.class,
                //course
                Course.class,
                LevelMappingObjectiveTest.class,
                CourseMappingLevel.class,
                //lesson
                LessonCollection.class,
                LessonMappingQuestion.class,
                //level
                LessonLevel.class,
                //objective
                Objective.class,
                ObjectiveMapping.class,
                //question
                Question.class,
                WeightForPhoneme.class,
                WordOfQuestion.class,
                //test
                LessonTest.class,
                TestMapping.class,
                //word
                WordCollection.class,
                WordMappingPhonemes.class,
                // IPA Arpabet
                IPAMapArpabet.class
        })
public class LessonDatabaseHelper extends LiteDatabaseHelper {

    public LessonDatabaseHelper() throws AnnotationNotFound, InvalidAnnotationData {
        super(MainApplication.getContext());
    }

    public LessonDatabaseHelper(DatabaseListener databaseListener) throws AnnotationNotFound, InvalidAnnotationData {
        super(MainApplication.getContext(), databaseListener);
    }
}
