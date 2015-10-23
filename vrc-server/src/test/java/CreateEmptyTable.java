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

/**
 * Created by cmg on 23/10/2015.
 */
public class CreateEmptyTable {

    public static void main(String[] args) {
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
