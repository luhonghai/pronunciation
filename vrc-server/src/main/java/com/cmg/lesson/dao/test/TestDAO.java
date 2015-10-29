package com.cmg.lesson.dao.test;

import com.cmg.lesson.data.jdo.test.Test;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CMG Dev156 on 10/22/2015.
 */
public class TestDAO extends DataAccess<Test> {
    public TestDAO() {
        super(Test.class);
    }

    /**
     *  use for get latest version in table
     * @return latest version
     * @throws Exception
     */
    public int getLatestVersion() throws Exception{
        int version = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT max(version) FROM " + Test.class.getCanonicalName());
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
     * @param name
     * @return true is update
     * @throws Exception
     */
    public boolean updateTest(String id, String name, String description, double percentPass) throws Exception{
        boolean isUpdate=false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Test.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET name=?, description=?, percentPass="+percentPass+" WHERE id=?");
        try {
            q.execute(name,description,id);
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
     * @param id
     * @return true is update
     * @throws Exception
     */
    public boolean updateDescription(String id, String description) throws Exception{
        boolean isUpdate=false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Test.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET description=? WHERE id=?");
        try {
            q.execute(description,id);
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
     * @param name
     * @return true is exist word name.
     * @throws Exception
     */
    public boolean checkExist(String name) throws Exception{
        boolean isExist = false;
        List<Test> list = list("WHERE name == :1 && isDeleted == :2 ", name, false);
        if(list!=null && list.size() > 0){
            isExist = true;
        }
        return isExist;
    }

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    public Test getById(String id) throws Exception{
        boolean isExist = false;
        List<Test> list = list("WHERE id == :1 && isDeleted == :2 ", id, false);
        if(list!=null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    /**
     *
     * @return total rows in table
     */
    public double getCount() throws  Exception{
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + Test.class.getCanonicalName());
        q.setFilter("isDeleted==false");
        try {
            count = (Long) q.execute();
            return count.doubleValue();
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }


    /**
     *
     * @param id
     * @return true if update deleted success
     */
    public boolean deletedTest(String id){
        boolean isDelete = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Test.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted= ? WHERE id=?");
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

    /**
     *
     * @param ids
     * @return
     */
    public List<Test> listIn(List<String> ids) throws Exception{
        StringBuffer clause = new StringBuffer();
        clause.append(" Where Test.ID in(");
        for(String id : ids){
            clause.append("'"+id+"',");
        }
        List<Test> listObjective = new ArrayList<Test>();
        String whereClause = clause.toString().substring(0, clause.toString().length() - 1);
        whereClause = whereClause + ") and isDeleted=false " ;

        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Test.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "Select id,name,description,percentPass from " + metaRecorderSentence.getTable() + whereClause);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size() > 0){
                for(Object obj : tmp){
                    Test objTest = new Test();
                    Object[] array = (Object[]) obj;
                    objTest.setId(array[0].toString());
                    if(array[1]!=null){
                        objTest.setName(array[1].toString());
                    }
                    if(array[2]!=null){
                        objTest.setDescription(array[2].toString());
                    }
                    if(array[3]!=null){
                        objTest.setPercentPass(Double.parseDouble(array[3].toString()));
                    }
                    listObjective.add(objTest);
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
        return listObjective;

    }
}
