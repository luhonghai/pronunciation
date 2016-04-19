package com.cmg.merchant.dao.questions;

import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.lessons.LessonMappingQuestion;
import com.cmg.lesson.data.jdo.objectives.ObjectiveMapping;
import com.cmg.lesson.data.jdo.question.Question;
import com.cmg.lesson.data.jdo.question.WeightForPhoneme;
import com.cmg.lesson.data.jdo.question.WordOfQuestion;
import com.cmg.lesson.data.jdo.test.TestMapping;
import com.cmg.merchant.common.SqlUtil;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lantb on 2016-03-01.
 */
public class QDAO extends DataAccess<Question> {
    public QDAO() {
        super(Question.class);
    }

    /**
     *  use for get latest version in table
     * @return latest version
     * @throws Exception
     */
    public int getLatestVersion() throws Exception{
        int version = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT max(version) FROM " + Question.class.getCanonicalName());
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
     * @param id
     * @return
     * @throws Exception
     */

    public Question getById(String id) throws Exception{
        List<Question> list = list("WHERE id == :1 && isDeleted == :2 ", id, false);
        if(list!=null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    /**
     *
     * @param id
     * @return true if deleted success
     */
    public boolean deletedQuestion(String id){
        boolean isDelete = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlUtil util = new SqlUtil();
        String sql = util.getSqlDeleteQuestion(id);
        System.out.println("sql delete question : " + sql);
        Query q = pm.newQuery("javax.jdo.query.SQL", sql);
        try {
            q.execute(true,id);
            isDelete=true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }

        return isDelete;
    }


    public boolean updateQuestion(String id,String name, String description,String type,String detail) throws Exception{
        boolean isUpdate=false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaQuestion = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Question.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaQuestion.getTable() + " SET name='"+name+"', description='"+description+"', nameUnique='"+detail+"', type='"+type+"' WHERE id='"+id+"'");
        try {
            q.execute();
            isUpdate=true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
        return isUpdate;
    }

    /**
     *
     * @param idLesson
     * @param idQuestion
     * @return
     */
    public boolean removeMappingQuestionWithLesson(String idLesson,String idQuestion){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaLessonMappingQuestion = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(LessonMappingQuestion.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaLessonMappingQuestion.getTable() + " SET isDeleted = true WHERE idLesson=? and idQuestion=?");
        try {
            q.execute(idLesson,idQuestion);
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
     * @param idWord
     * @param idQuestion
     * @return
     */
    public boolean updateDeletedWordOfQuestion(String idWord,String idQuestion){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaWordOfQuestion = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(WordOfQuestion.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaWordOfQuestion.getTable() + " SET isDeleted=? WHERE idWordCollection=? and idQuestion=?");
        try {
            q.execute(true,idWord,idQuestion);
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

    public boolean updateDeletedWordOfQuestionForWeight(String idWord,String idQuestion){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaWeightForPhoneme = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(WeightForPhoneme.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaWeightForPhoneme.getTable() + " SET isDeleted = true WHERE idWordCollection=? and idQuestion=?");
        try {
            q.execute(idWord,idQuestion);
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


    public List<WordOfQuestion> getWordByIdQuestion(String idQuestion) throws Exception{
        StringBuffer clause = new StringBuffer();
        TypeMetadata metaWordOfQuestion = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(WordOfQuestion.class.getCanonicalName());
        String firstQuery = "select id, idQuestion , idWordCollection from  " + metaWordOfQuestion.getTable()+ " where isDeleted=false and idQuestion='"+idQuestion+"' ";
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("javax.jdo.query.SQL", clause.toString());
        List<WordOfQuestion> list = new ArrayList<WordOfQuestion>();

        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size() > 0){
                for(Object obj : tmp){
                    WordOfQuestion wordOfQuestion = new WordOfQuestion();
                    Object[] array = (Object[]) obj;
                    wordOfQuestion.setId(array[0].toString());
                    if(array[1]!=null){
                        wordOfQuestion.setIdQuestion(array[1].toString());
                    }
                    if(array[2]!=null){
                        wordOfQuestion.setIdWordCollection(array[2].toString());
                    }
                    list.add(wordOfQuestion);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
        return list;
    }

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */


}
