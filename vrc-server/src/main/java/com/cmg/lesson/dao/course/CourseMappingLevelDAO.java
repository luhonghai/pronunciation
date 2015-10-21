package com.cmg.lesson.dao.course;

import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.lesson.data.jdo.course.CourseMappingLevel;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.HashMap;

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
    public int getLatestIndex(String idCourse, String idLevel) throws Exception{
        int version = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT max(version) FROM " + CourseMappingLevel.class.getCanonicalName());
        q.setFilter("idCourse == paramCourse && idLevel == paramLevel && isDeleted==false");
        q.declareParameters("String paramCourse , String paramLevel");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("paramCourse",idCourse);
        params.put("paramLevel",idLevel);
        try {
            if (q != null) {
                version = (int) q.execute(params);
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





}
