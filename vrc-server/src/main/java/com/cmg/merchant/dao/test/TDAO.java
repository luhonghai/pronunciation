package com.cmg.merchant.dao.test;

import com.cmg.lesson.data.jdo.course.CourseMappingDetail;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.lesson.data.jdo.question.Question;
import com.cmg.lesson.data.jdo.test.Test;
import com.cmg.merchant.common.SQL;
import com.cmg.merchant.common.SqlUtil;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lantb on 2016-03-01.
 */
public class TDAO extends DataAccess<Test> {
    public TDAO() {
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
     * @param percentPass
     * @return true is update successfull
     * @throws Exception
     */
    public boolean updateTest(String id, double percentPass) throws Exception{
        boolean isUpdate=false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Test.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET percentPass=? WHERE id=?");
        try {
            q.execute(percentPass,id);
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
     * @return true is deleted successfully
     * @throws Exception
     */
    public boolean deletedTest(String id) throws Exception{
        boolean isUpdate=false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlUtil util = new SqlUtil();
        String sql = util.getSqlDeleteTest(id);
        System.out.println("sql delete test : " + sql);
        Query q = pm.newQuery("javax.jdo.query.SQL", sql);
        try {
            q.execute(true,id);
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
     * @return
     * @throws Exception
     */
    public Test getById(String id) throws Exception{
        List<Test> list = list("WHERE id == :1 && isDeleted == :2 ", id, false);
        if(list!=null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }


    /**
     *
     * @param idLevel
     * @return
     */
    public Test getByIdLevel(String idLevel) throws Exception{
        StringBuffer clause = new StringBuffer();
        TypeMetadata metaObjective = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Test.class.getCanonicalName());
        TypeMetadata metaCourseMappingDetail = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(CourseMappingDetail.class.getCanonicalName());
        String firstQuery = "select test.id, test.name , test.description, test.percentPass from  " + metaObjective.getTable()
                + " test inner join " + metaCourseMappingDetail.getTable()
                + " mapping on mapping.idChild=test.id where ";
        clause.append(firstQuery);
        clause.append(" mapping.idLevel= '"+idLevel+"' and test.isDeleted=false and mapping.isDeleted=false");
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("javax.jdo.query.SQL", clause.toString());
        List<Test> listObjective = new ArrayList<Test>();

        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size() > 0){
                for(Object obj : tmp){
                    Test t = new Test();
                    Object[] array = (Object[]) obj;
                    t.setId(array[0].toString());
                    if(array[1]!=null){
                        t.setName(array[1].toString());
                    }
                    if(array[2]!=null){
                        t.setDescription(array[2].toString());
                    }
                    if(array[3]!=null){
                        t.setPercentPass(Double.parseDouble(array[3].toString()));
                    }
                    return t;
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
        return null;
    }

    /**
     *
     * @param idTest
     * @return
     */
    public ArrayList<Question> getQuestionInTest(String idTest){
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlUtil sqlUtil=new SqlUtil();
        String sql = sqlUtil.getSqlQuestionInTest(idTest);
        System.out.println("sql get all question in test  : " +  sql);
        ArrayList<Question> list = new ArrayList<Question>();
        Query q = pm.newQuery("javax.jdo.query.SQL", sql);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size() > 0){
                for(Object obj : tmp){
                    Question dto = new Question();
                    Object[] array = (Object[]) obj;
                    if(array[0]!=null){
                        dto.setId(array[0].toString());
                    }
                    if(array[1]!=null){
                        dto.setName(array[1].toString());
                    }
                    if(array[2] != null) {
                        dto.setDescription(array[2].toString());
                    }
                    if(array[3] != null) {
                        dto.setType(array[3].toString());
                    }
                    if(array[4] != null) {
                        dto.setIndex(Integer.parseInt(array[4].toString()));
                    }
                    list.add(dto);
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
        return list;
    }

}
