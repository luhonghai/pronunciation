package com.cmg.merchant.dao.lessons;

import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.objectives.ObjectiveMapping;
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
public class LDAO extends DataAccess<LessonCollection> {
    public LDAO() {
        super(LessonCollection.class);
    }

    /**
     *  use for get latest version in table
     * @return latest version
     * @throws Exception
     */
    public int getLatestVersion() throws Exception{
        int version = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT max(version) FROM " + LessonCollection.class.getCanonicalName());
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
    public LessonCollection getById(String id) throws Exception{
        List<LessonCollection> list = list("WHERE id == :1 && isDeleted == :2 ", id, false);
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
    public boolean deletedLesson(String id){
        boolean isDelete = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlUtil util = new SqlUtil();
        String sql = util.getSqlDeleteLesson(id);
        System.out.println("sql delete lesson : " + sql);
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


    public boolean updateLesson(String id,String name, String description,String type,String detail) throws Exception{
        boolean isUpdate=false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(LessonCollection.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET name=?, description=?, nameUnique=?, type='"+type+"' WHERE id='"+id+"'");
        try {
            q.execute(name,description,detail);
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
     * @param idObjective
     * @param idLesson
     * @return
     */
    public boolean removeMappingLessonWithObj(String idObjective,String idLesson){
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

    public List<LessonCollection> getAllByIdObj(String idObj) throws Exception{
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
        List<LessonCollection> list = new ArrayList<LessonCollection>();

        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size() > 0){
                for(Object obj : tmp){
                    LessonCollection lessonCollection = new LessonCollection();
                    Object[] array = (Object[]) obj;
                    lessonCollection.setId(array[0].toString());
                    if(array[1]!=null){
                        lessonCollection.setName(array[1].toString());
                    }
                    if(array[2]!=null){
                        lessonCollection.setDescription(array[2].toString());
                    }
                    list.add(lessonCollection);
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
    public LessonCollection getAllByIdTest(String testId) throws Exception{
        StringBuffer clause = new StringBuffer();
        TypeMetadata metaLessonCollection = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(LessonCollection.class.getCanonicalName());
        TypeMetadata metaTestMapping = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(TestMapping.class.getCanonicalName());
        String firstQuery = "select lesson.id, lesson.name , lesson.description  from  " + metaLessonCollection.getTable()
                + " lesson inner join " + metaTestMapping.getTable()
                + " mapping on mapping.idLessonCollection=lesson.id where ";
        clause.append(firstQuery);
        clause.append(" mapping.idTest= '"+testId+"' and lesson.isDeleted=false and mapping.isDeleted=false");
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("javax.jdo.query.SQL", clause.toString());
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size() > 0){
                for(Object obj : tmp){
                    LessonCollection lessonCollection = new LessonCollection();
                    Object[] array = (Object[]) obj;
                    lessonCollection.setId(array[0].toString());
                    if(array[1]!=null){
                        lessonCollection.setName(array[1].toString());
                    }
                    if(array[2]!=null){
                        lessonCollection.setDescription(array[2].toString());
                    }
                    return lessonCollection;
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
        return null;
    }




}
