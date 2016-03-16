package com.cmg.merchant.dao.level;

import com.cmg.lesson.data.jdo.course.CourseMappingDetail;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.List;

/**
 * Created by lantb on 2016-03-01.
 */
public class LVMTDAO extends DataAccess<CourseMappingDetail> {
    public LVMTDAO(){super(CourseMappingDetail.class);}

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
     * @param
     * @return
     */
    public boolean updateDeleted(String idTest,String idLevel){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(CourseMappingDetail.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted = true WHERE idChild=? and idLevel=?");
        try {
            q.execute(idTest,idLevel);
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
     *
     * @param idLevel
     * @param isTest
     * @return
     * @throws Exception
     */
    public CourseMappingDetail getBy(String idLevel, boolean isTest) throws Exception{
        List<CourseMappingDetail> list = list("WHERE idLevel == :1 " +
                "&& isTest ==:2 && isDeleted==false" , idLevel, isTest);
        if(list!=null && list.size()>0){
            return list.get(0);
        }
        return null;
    }
}
