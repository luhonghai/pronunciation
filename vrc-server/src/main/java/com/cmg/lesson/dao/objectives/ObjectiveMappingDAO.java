package com.cmg.lesson.dao.objectives;

import com.cmg.lesson.data.jdo.objectives.ObjectiveMapping;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.List;

/**
 * Created by CMG Dev156 on 10/22/2015.
 */
public class ObjectiveMappingDAO extends DataAccess<ObjectiveMapping> {

    public ObjectiveMappingDAO(){super(ObjectiveMapping.class);}

    /**
     *  use for get latest version in table
     * @return latest version
     * @throws Exception
     */
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

    /**
     *
     * @param idLessonCollection
     * @param idObjective
     * @return true if question exist
     * @throws Exception
     */
    public boolean checkExist(String idLessonCollection, String idObjective) throws Exception{
        List<ObjectiveMapping> list = list("WHERE idLessonCollection==:1 && idObjective== :2 && isDeleted==:3", idLessonCollection,idObjective,false);
        if(list!=null && list.size() > 0){
            return true;
        }
        return false;
    }

    /**
     *
     * @param idLessonCollection
     * @return
     */
    public boolean updateDeleted(String idLessonCollection){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(ObjectiveMapping.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted= ? WHERE idLessonCollection=?");
        try {
            q.execute(true,idLessonCollection);
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
     * @param idLessonCollection
     * @return
     */
    public boolean updateDeleted(String idLessonCollection, String idObjective){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(ObjectiveMapping.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted= ? WHERE idLessonCollection=? and idObjective=?");
        try {
            q.execute(true,idLessonCollection,idObjective);
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