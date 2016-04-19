package com.cmg.merchant.dao.objective;

import com.cmg.lesson.data.jdo.course.CourseMappingDetail;
import com.cmg.lesson.data.jdo.objectives.ObjectiveMapping;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;

/**
 * Created by lantb on 2016-02-24.
 */
public class OMLDAO extends DataAccess<ObjectiveMapping> {
    public OMLDAO(){super(ObjectiveMapping.class);}


    public int getLatestVersion() throws Exception{
        int version = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT max(version) FROM " + ObjectiveMapping.class.getCanonicalName());
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

    public int getMaxIndex(String idObj) throws Exception{
        int index = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT max(index) FROM " + ObjectiveMapping.class.getCanonicalName() + " WHERE idObjective=='" + idObj +"'");
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
