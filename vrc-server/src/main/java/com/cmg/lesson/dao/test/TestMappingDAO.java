package com.cmg.lesson.dao.test;

import com.cmg.lesson.data.jdo.test.TestMapping;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.List;

/**
 * Created by CMG Dev156 on 10/22/2015.
 */
public class TestMappingDAO extends DataAccess<TestMapping> {

    public TestMappingDAO(){super(TestMapping.class);}

    /**
     *  use for get latest version in table
     * @return latest version
     * @throws Exception
     */
    public int getLatestVersion() throws Exception{
        int version = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT max(version) FROM " + TestMapping.class.getCanonicalName());
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
     * @param idTest
     * @return true if question exist
     * @throws Exception
     */
    public boolean checkExist(String idLessonCollection, String idTest) throws Exception{
        List<TestMapping> list = list("WHERE idLessonCollection==:1 && idTest== :2 && isDeleted==:3", idLessonCollection,idTest,false);
        if(list!=null && list.size() > 0){
            return true;
        }
        return false;
    }

    /**
     *
     * @param idTest
     * @return
     */
    public boolean updateDeleted(String idTest){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(TestMapping.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted= ? WHERE idTest=?");
        try {
            q.execute(true,idTest);
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
     * @param idLesson
     * @return
     */
    public boolean updateDeletedByLesson(String idLesson){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(TestMapping.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted= ? WHERE idLessonCollection=?");
        try {
            q.execute(true,idLesson);
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
    public boolean updateDeleted(String idTest, String idLessonCollection){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(TestMapping.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted= ? WHERE idLessonCollection=? and idTest=?");
        try {
            q.execute(true,idLessonCollection,idTest);
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
     * @param idTest
     * @return
     */
    public List<TestMapping> getAllByIdTest(String idTest) throws Exception{
        List<TestMapping> list = list("WHERE idTest==:1 && isDeleted==:3", idTest,false);
        if(list!=null && list.size() > 0){
            return list;
        }
        return null;
    }

}
