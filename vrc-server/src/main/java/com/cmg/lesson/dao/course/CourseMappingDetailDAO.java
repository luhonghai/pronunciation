package com.cmg.lesson.dao.course;

import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.lesson.data.jdo.course.CourseMappingDetail;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.List;

/**
 * Created by lantb on 2015-10-22.
 */
public class CourseMappingDetailDAO extends DataAccess<CourseMappingDetail> {
    public CourseMappingDetailDAO(){super(CourseMappingDetail.class);}

    /**
     *  use for get latest version in table
     * @return latest version
     * @throws Exception
     */
    public int getLatestVersion() throws Exception{
        int version = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT max(version) FROM " + CourseMappingDetail.class.getCanonicalName());
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
     * @param idCourse
     * @param idObjectiveOrTest
     * * @param idLevel
     * @return true if question exist
     * @throws Exception
     */
    public boolean checkExist(String idCourse, String idObjectiveOrTest, String idLevel) throws Exception{
        List<CourseMappingDetail> list = list("WHERE idCourse==:1 && idChild== :2 && idLevel==:3 && isDeleted==:4", idCourse,idObjectiveOrTest,idLevel,false);
        if(list!=null && list.size() > 0){
            return true;
        }
        return false;
    }

    /**
     *
     * @param idLevel
     * @return
     */
    public boolean updateDeleted(String idLevel){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(CourseMappingDetail.class.getCanonicalName());
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

    /**
     *
     * @param idChild
     * @return
     */
    public boolean updateDeletedByIdidChild(String idChild){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(CourseMappingDetail.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted= ? WHERE idChild=?");
        try {
            q.execute(true,idChild);
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
     * @param
     * @return
     */
    public boolean updateDeleted(String idCourse,String idObjective, String idLevel){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(CourseMappingDetail.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted = true WHERE idCourse=? and idChild=? and idLevel=?");
        try {
            q.execute(idCourse,idObjective, idLevel);
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
     * @param
     * @return
     */
    public boolean updateDeleted(String idCourse,String idObjective){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(CourseMappingDetail.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted = true WHERE idCourse=? and idLevel=?");
        try {
            q.execute(idCourse,idObjective);
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
     * @param isTest
     * @return
     * @throws Exception
     */
    public List<CourseMappingDetail> getBy(String idCourse, String idLevel, boolean isTest) throws Exception{
        List<CourseMappingDetail> list = list("WHERE idCourse == :1 && idLevel == :2 " +
                "&& isTest ==:3 && isDeleted==false", idCourse, idLevel, isTest);
        if(list!=null && list.size()>0){
            return list;
        }
        return null;
    }

    /**
     *
     * @param idLevel
     * @return
     */
    public List<CourseMappingDetail> getAllByLevel(String idCourse, String idLevel) throws Exception{
        List<CourseMappingDetail> list = list("WHERE idCourse==:1 && idLevel==:2 && isDeleted==:3", idCourse,idLevel,false);
        if(list!=null && list.size() > 0){
            return list;
        }
        return null;
    }
}
