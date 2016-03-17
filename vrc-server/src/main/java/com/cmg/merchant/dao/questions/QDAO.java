package com.cmg.merchant.dao.questions;

import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.objectives.ObjectiveMapping;
import com.cmg.lesson.data.jdo.question.Question;
import com.cmg.lesson.data.jdo.test.TestMapping;
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
        TypeMetadata metaQuestion = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Question.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaQuestion.getTable() + " SET isDeleted= ? WHERE id=?");
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
    public boolean updateDeleted(String idObjective,String idLesson){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(ObjectiveMapping.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted = true WHERE idObjective=? and idLessonCollection=?");
        try {
            q.execute(idObjective,idLesson);
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

    public List<Question> getAllByIdObj(String idObj) throws Exception{
        StringBuffer clause = new StringBuffer();
        TypeMetadata metaLessonCollection = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(LessonCollection.class.getCanonicalName());
        TypeMetadata metaObjectiveMapping = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(ObjectiveMapping.class.getCanonicalName());
        String firstQuery = "select lesson.id, lesson.name , lesson.description, mapping.index from  " + metaLessonCollection.getTable()
                + " lesson inner join " + metaObjectiveMapping.getTable()
                + " mapping on mapping.idLessonCollection=lesson.id where ";
        clause.append(firstQuery);
        clause.append(" mapping.idObjective= '"+idObj+"' and lesson.isDeleted=false and mapping.isDeleted=false");
        clause.append(" ORDER BY mapping.index ASC");
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("javax.jdo.query.SQL", clause.toString());
        List<Question> list = new ArrayList<Question>();

        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size() > 0){
                for(Object obj : tmp){
                    Question question = new Question();
                    Object[] array = (Object[]) obj;
                    question.setId(array[0].toString());
                    if(array[1]!=null){
                        question.setName(array[1].toString());
                    }
                    if(array[2]!=null){
                        question.setDescription(array[2].toString());
                    }
                    list.add(question);
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
     * @param testId
     * @return
     * @throws Exception
     */



}
