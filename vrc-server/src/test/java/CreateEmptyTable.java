import com.cmg.lesson.data.jdo.country.Country;
import com.cmg.lesson.data.jdo.country.CountryMappingCourse;
import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.lesson.data.jdo.course.CourseMappingDetail;
import com.cmg.lesson.data.jdo.course.CourseMappingLevel;
import com.cmg.lesson.data.jdo.history.PhonemeLessonScore;
import com.cmg.lesson.data.jdo.history.SessionScore;
import com.cmg.lesson.data.jdo.history.UserLessonHistory;
import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.lessons.LessonMappingQuestion;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.lesson.data.jdo.objectives.ObjectiveMapping;
import com.cmg.lesson.data.jdo.question.Question;
import com.cmg.lesson.data.jdo.question.WeightForPhoneme;
import com.cmg.lesson.data.jdo.question.WordOfQuestion;
import com.cmg.lesson.data.jdo.test.Test;
import com.cmg.lesson.data.jdo.test.TestMapping;
import com.cmg.lesson.data.jdo.word.WordCollection;
import com.cmg.lesson.data.jdo.word.WordMappingPhonemes;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.UUIDGenerator;

import java.util.Date;

/**
 * Created by cmg on 23/10/2015.
 */
public class CreateEmptyTable {

    public static void main(String[] args) {
        createTestAndMapping();
    }

    private static void createTestAndMapping() {
        Test test = new Test();
        test.setId(UUIDGenerator.generateUUID());
        test.setName("Test1");
        test.setPercentPass(75);
        test.setDateCreated(new Date(System.currentTimeMillis()));
        TestMapping testMapping = new TestMapping();
        testMapping.setIdTest(test.getId());
        testMapping.setIdLessonCollection("7aadf6fb-a644-4262-9ea6-094576edc80d");
        insert(test);
        insert(testMapping);
        CourseMappingDetail mappingDetail = new CourseMappingDetail();
        mappingDetail.setIdCourse("001ed657-1466-475c-bd1e-1324c22306c2");
        mappingDetail.setIdLevel("3479e7a6-4590-491f-b3c5-85b4a41f42b2");
        mappingDetail.setIdChild(test.getId());
        mappingDetail.setIsTest(true);
        insert(mappingDetail);


        test = new Test();
        test.setId(UUIDGenerator.generateUUID());
        test.setName("Test2");
        test.setPercentPass(75);
        test.setDateCreated(new Date(System.currentTimeMillis()));
        testMapping = new TestMapping();
        testMapping.setIdTest(test.getId());
        testMapping.setIdLessonCollection("2b811ed9-cb01-48c6-b96f-8e3f77234532");
        insert(test);
        insert(testMapping);
        mappingDetail = new CourseMappingDetail();
        mappingDetail.setIdCourse("001ed657-1466-475c-bd1e-1324c22306c2");
        mappingDetail.setIdLevel("0e11c8f1-77d4-46e9-bdf0-1c96b40ade8d");
        mappingDetail.setIdChild(test.getId());
        mappingDetail.setIsTest(true);
        insert(mappingDetail);

        test = new Test();
        test.setId(UUIDGenerator.generateUUID());
        test.setName("Test3");
        test.setPercentPass(75);
        test.setDateCreated(new Date(System.currentTimeMillis()));
        testMapping = new TestMapping();
        testMapping.setIdTest(test.getId());
        testMapping.setIdLessonCollection("32b9f077-7b5d-4370-bbf6-684b518eb72b");
        insert(test);
        insert(testMapping);
        mappingDetail = new CourseMappingDetail();
        mappingDetail.setIdCourse("001ed657-1466-475c-bd1e-1324c22306c2");
        mappingDetail.setIdLevel("80d86f17-be1d-4b11-a3dd-702127c7e926");
        mappingDetail.setIdChild(test.getId());
        mappingDetail.setIsTest(true);
        insert(mappingDetail);

        test = new Test();
        test.setId(UUIDGenerator.generateUUID());
        test.setName("Test4");
        test.setPercentPass(75);
        test.setDateCreated(new Date(System.currentTimeMillis()));
        testMapping = new TestMapping();
        testMapping.setIdTest(test.getId());
        testMapping.setIdLessonCollection("7889e324-1042-43d9-b772-daac450db10d");
        insert(test);
        insert(testMapping);
        mappingDetail = new CourseMappingDetail();
        mappingDetail.setIdCourse("001ed657-1466-475c-bd1e-1324c22306c2");
        mappingDetail.setIdLevel("406547c1-74cd-43f7-a8d8-b6469bec69a7");
        mappingDetail.setIdChild(test.getId());
        mappingDetail.setIsTest(true);
        insert(mappingDetail);

        test = new Test();
        test.setId(UUIDGenerator.generateUUID());
        test.setName("Test5");
        test.setPercentPass(75);
        test.setDateCreated(new Date(System.currentTimeMillis()));
        testMapping = new TestMapping();
        testMapping.setIdTest(test.getId());
        testMapping.setIdLessonCollection("f9afbbd0-3809-43da-81bd-69d9eaf0305f");
        insert(test);
        insert(testMapping);
        mappingDetail = new CourseMappingDetail();
        mappingDetail.setIdCourse("001ed657-1466-475c-bd1e-1324c22306c2");
        mappingDetail.setIdLevel("9293d73b-2e77-48b2-8fa8-c4e43a6119bb");
        mappingDetail.setIdChild(test.getId());
        mappingDetail.setIsTest(true);
        insert(mappingDetail);

        test = new Test();
        test.setId(UUIDGenerator.generateUUID());
        test.setName("Test6");
        test.setPercentPass(75);
        test.setDateCreated(new Date(System.currentTimeMillis()));
        testMapping = new TestMapping();
        testMapping.setIdTest(test.getId());
        testMapping.setIdLessonCollection("bea4d9d3-0e31-4731-9a7c-6f80ff955a51");
        insert(test);
        insert(testMapping);
        mappingDetail = new CourseMappingDetail();
        mappingDetail.setIdCourse("001ed657-1466-475c-bd1e-1324c22306c2");
        mappingDetail.setIdLevel("a3b0b4aa-7632-4110-83cb-9f25cb86fbf7");
        mappingDetail.setIdChild(test.getId());
        mappingDetail.setIsTest(true);
        insert(mappingDetail);
    }

    private static void createCountryMappingCourse(){
        CountryMappingCourse countryMappingCourse = new CountryMappingCourse();
        countryMappingCourse.setIdCountry("28c63e5e-b5cd-4213-8926-df9210c2b011");
        countryMappingCourse.setIdCourse("38fb2a27-ea20-4154-a7be-4f11b39d22d7");
        insert(countryMappingCourse);

        countryMappingCourse = new CountryMappingCourse();
        countryMappingCourse.setIdCountry("52728334-18ef-4d94-bfde-1c342afb0392");
        countryMappingCourse.setIdCourse("001ed657-1466-475c-bd1e-1324c22306c2");
        insert(countryMappingCourse);

        countryMappingCourse = new CountryMappingCourse();
        countryMappingCourse.setIdCountry("d2668e14-5c27-4c72-a63e-d5de02b20173");
        countryMappingCourse.setIdCourse("ee5c5ecd-a0a0-4a97-a727-fec99ca69129");
        insert(countryMappingCourse);
    }

    private static void createCountry() {
        Country country = new Country();
        country.setName("Thailand");
        country.setDescription("");
        insert(country);
        country = new Country();
        country.setName("Vietnamese");
        insert(country);
        country = new Country();
        country.setName("English");
        insert(country);
    }

    private static <T> void insert(T obj) {
        DataAccess<T> dataAccess = (DataAccess<T>) new DataAccess<>(obj.getClass());
        try {
            dataAccess.put(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createEmptyTable() {
        Class<?>[] jdoClasses = new Class[] {
                Country.class,
                CountryMappingCourse.class,
                Course.class,
                CourseMappingDetail.class,
                CourseMappingLevel.class,
                PhonemeLessonScore.class,
                SessionScore.class,
                UserLessonHistory.class,
                LessonCollection.class,
                LessonMappingQuestion.class,
                Level.class,
                Objective.class,
                ObjectiveMapping.class,
                Question.class,
                WeightForPhoneme.class,
                WordOfQuestion.class,
                Test.class,
                TestMapping.class,
                WordCollection.class,
                WordMappingPhonemes.class
        };
        for (Class<?> jdoClass : jdoClasses) {
            try {
                System.out.println("Create an empty object of class " + jdoClass);
                Object obj = jdoClass.newInstance();
                DataAccess da = new DataAccess(jdoClass);
                da.put(obj);
                System.out.println("DONE");
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
