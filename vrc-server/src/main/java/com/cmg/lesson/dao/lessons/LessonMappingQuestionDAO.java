package com.cmg.lesson.dao.lessons;

import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.lessons.LessonMappingQuestion;
import com.cmg.lesson.data.jdo.question.WordOfQuestion;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.List;

/**
 * Created by lantb on 2015-10-13.
 */
public class LessonMappingQuestionDAO extends DataAccess<LessonMappingQuestion> {

    public LessonMappingQuestionDAO(){super(LessonMappingQuestion.class);}



    /**
     *  use for get latest version in table
     * @return latest version
     * @throws Exception
     */
    public int getLatestVersion() throws Exception{
        int version = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT max(version) FROM " + LessonMappingQuestion.class.getCanonicalName());
        try {
            if (q != null) {
                version = (int) q.execute();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
        return version;
    }

    /**
     *
     * @param idLessonCollection
     * @param idQuestion
     * @return true if question exist
     * @throws Exception
     */
    public boolean checkExist(String idLessonCollection, String idQuestion) throws Exception{
        List<LessonMappingQuestion> list = list("WHERE idLesson==:1 && idQuestion== :2 && isDeleted==:3", idLessonCollection,idQuestion,false);
        if(list!=null && list.size() > 0){
            return true;
        }
        return false;
    }

    /**
     *
     * @param idLessonCollection
     * @return
     */
    public List<LessonMappingQuestion> getAllByIDLesson(String idLessonCollection) throws Exception{
        List<LessonMappingQuestion> list = list("WHERE idLesson==:1 && isDeleted==:2", idLessonCollection,false);
        if(list!=null && list.size() > 0){
            return list;
        }
        return null;
    }

    /**
     *
     * @param idLesson
     * @return
     */
    public boolean updateDeleted(String idLesson){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(LessonMappingQuestion.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted= ? WHERE idLesson=?");
        try {
            q.execute(true,idLesson);
            check=true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }

        return check;
    }

    /**
     *
     * @param idQuestion
     * @return
     */
    public boolean updateDeletedByQuestion(String idQuestion){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(LessonMappingQuestion.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted= ? WHERE idQuestion=?");
        try {
            q.execute(true,idQuestion);
            check=true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }

        return check;
    }

    /**
     *
     * @param idLesson
     * @return
     */
    public boolean updateDeleted(String idLesson, String idQuestion){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(LessonMappingQuestion.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted= ? WHERE idLesson=? and idQuestion=?");
        try {
            q.execute(true,idLesson,idQuestion);
            check=true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }

        return check;
    }



}
