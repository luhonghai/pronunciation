package com.cmg.merchant.dao.course;

import com.cmg.lesson.data.jdo.course.CourseMappingLevel;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.HashMap;

/**
 * Created by lantb on 2016-02-23.
 */
public class CMLDAO extends DataAccess<CourseMappingLevel> {
    public CMLDAO(){super(CourseMappingLevel.class);}
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
     * @param idCourse
     * @param idLevel
     * @return true if update deleted success
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
}
