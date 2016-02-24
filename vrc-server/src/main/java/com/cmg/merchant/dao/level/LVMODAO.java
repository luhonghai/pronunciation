package com.cmg.merchant.dao.level;

import com.cmg.lesson.data.jdo.course.CourseMappingDetail;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Created by lantb on 2016-02-24.
 */
public class LVMODAO extends DataAccess<CourseMappingDetail> {
    public LVMODAO(){super(CourseMappingDetail.class);}

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
     * @param idLevel
     * @return
     * @throws Exception
     */
    public int getMaxIndex(String idLevel) throws Exception{
        int index = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT max(index) FROM " + CourseMappingDetail.class.getCanonicalName() + " WHERE isTest==false && idLevel=='" + idLevel +"'");
        try {
            if (q != null) {
                index = (int) q.execute();
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


}
