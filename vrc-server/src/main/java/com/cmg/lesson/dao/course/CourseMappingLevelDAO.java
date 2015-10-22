package com.cmg.lesson.dao.course;

import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.lesson.data.jdo.course.CourseMappingLevel;
import com.cmg.lesson.data.jdo.word.WordCollection;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lantb on 2015-10-21.
 */
public class CourseMappingLevelDAO extends DataAccess<CourseMappingLevel> {
    public CourseMappingLevelDAO(){super(CourseMappingLevel.class);}

    /**
     *  use for get latest version in table
     * @return latest version
     * @throws Exception
     */
    public int getLatestVersion() throws Exception{
        int version = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT max(version) FROM " + CourseMappingLevel.class.getCanonicalName());
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
     *  use for get latest version in table
     * @return latest version
     * @throws Exception
     */
    public int getLatestIndex(String idCourse) throws Exception{
        int index = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT max(index) FROM " + CourseMappingLevel.class.getCanonicalName());
        q.setFilter("idCourse == paramCourse && isDeleted==false");
        q.declareParameters("String paramCourse");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("paramCourse",idCourse);
        try {
            if (q != null) {
                index = (int) q.execute(idCourse);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
                pm.close();
        }
        return index;
    }



    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    public boolean checkExist(String idCourse,String idLevel) throws Exception{
        boolean isExist = false;
        List<CourseMappingLevel> list = list("WHERE idCourse==:1 && idLevel == :2 && isDeleted == :3 ", idCourse, idLevel, false);
        if(list!=null && list.size() > 0){
            isExist = true;
        }
        return isExist;
    }

    /**
     *
     * @param idCourse
     * @return
     * @throws Exception
     */
    public List<CourseMappingLevel> getByIdCourse(String idCourse) throws Exception{
        List<CourseMappingLevel> list = list("WHERE idCourse==:1 && isDeleted == :3", idCourse, false, "index asc");
        if(list!=null && list.size() > 0){
           return list;
        }
        return null;
    }

    /**
     *
     * @param idCourse
     * @param idLevel
     * @return
     * @throws Exception
     */
    public boolean removeLevel(String idCourse, String idLevel) throws Exception{
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(CourseMappingLevel.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted= ? WHERE idCourse=? and idLevel=?");
        try {
            q.execute(true,idCourse,idLevel);
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
     * @param idCourse
     * @param idLevel
     * @return
     * @throws Exception
     */
    public boolean removeLevel(String idLevel) throws Exception{
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(CourseMappingLevel.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted= ? WHERE idLevel=?");
        try {
            q.execute(true,idLevel);
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
