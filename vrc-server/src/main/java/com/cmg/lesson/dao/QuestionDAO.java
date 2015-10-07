package com.cmg.lesson.dao;

import com.cmg.lesson.data.jdo.Question;
import com.cmg.lesson.data.jdo.WordCollection;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.Transcription;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lantb on 2015-10-07.
 */
public class QuestionDAO extends DataAccess<Question> {
    public QuestionDAO() {
        super(Question.class);
    }

    /**
     *  use for get latest version in table
     * @return latest version
     * @throws Exception
     */
    public int getLatestVersion() throws Exception{
        int version = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT max(version) FROM " + Question.class.getCanonicalName());
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
     * @param name
     * @return true is exist word name.
     * @throws Exception
     */
    public boolean checkExist(String name) throws Exception{
        boolean isExist = false;
        List<Question> list = list("Where name==:1 && isDeleted==:2", name, false);
        if(list!=null && list.size() > 0){
            isExist = true;
        }
        return isExist;
    }

    /**
     *
     * @return total rows in table
     */
    public double getCount() throws  Exception{
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + Question.class.getCanonicalName());
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
     * @param search
     * @param createDateFrom
     * @param createDateTo
     * @return
     * @throws Exception
     */
    public double getCountSearch(String search,Date createDateFrom,Date createDateTo, int length, int start) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + Question.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="(name.toLowerCase().indexOf(search.toLowerCase()) != -1)";
        String b="(name == null || name.toLowerCase().indexOf(search.toLowerCase()) != -1)";
        if(createDateFrom!=null&&createDateTo==null){
            string.append("(createdDate >= createDateFrom) &&");
        }
        if(createDateFrom==null&&createDateTo!=null){
            string.append("(createdDate <= createDateTo) &&");
        }
        if(createDateFrom!=null&&createDateTo!=null){
            string.append("(createdDate >= createDateFrom && createdDate <= createDateTo) &&");
        }
        string.append("(isDeleted==0) &&");

        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setRange(start, start +length);
        q.setFilter(string.toString());
        q.declareParameters("String search, java.util.Date createDateFrom,java.util.Date createDateTo");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("createDateFrom", createDateFrom);
        params.put("createDateTo", createDateTo);
        try {
            count = (Long) q.executeWithMap(params);
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
     * @param start
     * @param length
     * @param search
     * @param column
     * @param order
     * @param createDateFrom
     * @param createDateTo
     * @return
     * @throws Exception
     */
    public List<Question> listAll(int start, int length,String search,int column,String order,Date createDateFrom,Date createDateTo) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + Question.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="(name.toLowerCase().indexOf(search.toLowerCase()) != -1)";
        String b="(name == null || name.toLowerCase().indexOf(search.toLowerCase()) != -1)";

        if(createDateFrom!=null&&createDateTo==null){
            string.append("(createdDate >= createDateFrom) &&");
        }
        if(createDateFrom==null&&createDateTo!=null){
            string.append("(createdDate <= createDateTo) &&");
        }
        if(createDateFrom!=null&&createDateTo!=null){
            string.append("(createdDate >= createDateFrom && createdDate <= createDateTo) &&");
        }
        string.append("(isDeleted==0) &&");

        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search,java.util.Date createDateFrom,java.util.Date createDateTo");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("createDateFrom", createDateFrom);
        params.put("createDateTo", createDateTo);
        if (column==1 && order.equals("asc")) {
            q.setOrdering("name asc");
        }else if(column==1 && order.equals("desc")) {
            q.setOrdering("name desc");
        }
        if (column==2 && order.equals("asc")) {
            q.setOrdering("createdDate asc");
        }else if(column==2 && order.equals("desc")) {
            q.setOrdering("createdDate desc");
        }

        q.setRange(start, start + length);

        try {
            return detachCopyAllList(pm, q.executeWithMap(params));
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }



}
