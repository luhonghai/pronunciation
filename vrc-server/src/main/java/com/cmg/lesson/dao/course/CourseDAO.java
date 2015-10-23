package com.cmg.lesson.dao.course;

import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lantb on 2015-10-20.
 */
public class CourseDAO extends DataAccess<Course> {
    public CourseDAO(){super(Course.class);}

    /**
     *  use for get latest version in table
     * @return latest version
     * @throws Exception
     */
    public int getLatestVersion() throws Exception{
        int version = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT max(version) FROM " + Course.class.getCanonicalName());
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
     * @return
     * @throws Exception
     */
    public boolean checkExist(String name) throws Exception{
        boolean isExist = false;
        List<Course> list = list("WHERE name == :1 && isDeleted == :2 ", name, false);
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
    public Course getById(String id) throws Exception{
        boolean isExist = false;
        List<Course> list = list("WHERE id == :1 && isDeleted == :2 ", id, false);
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
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + Course.class.getCanonicalName());
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
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + Course.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="(name.toLowerCase().indexOf(search.toLowerCase()) != -1)";
        String b="(name == null || name.toLowerCase().indexOf(search.toLowerCase()) != -1)";
        if(createDateFrom!=null&&createDateTo==null){
            string.append("(timeCreated >= createDateFrom) &&");
        }
        if(createDateFrom==null&&createDateTo!=null){
            string.append("(timeCreated <= createDateTo) &&");
        }
        if(createDateFrom!=null&&createDateTo!=null){
            string.append("(timeCreated >= createDateFrom && timeCreated <= createDateTo) &&");
        }
        string.append("(isDeleted==false) &&");

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
    public List<Course> listAll(int start, int length,String search,int column,String order,Date createDateFrom,Date createDateTo) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + Course.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="(name.toLowerCase().indexOf(search.toLowerCase()) != -1)";
        String b="(name == null || name.toLowerCase().indexOf(search.toLowerCase()) != -1)";

        if(createDateFrom!=null&&createDateTo==null){
            string.append("(timeCreated >= createDateFrom) &&");
        }
        if(createDateFrom==null&&createDateTo!=null){
            string.append("(timeCreated <= createDateTo) &&");
        }
        if(createDateFrom!=null&&createDateTo!=null){
            string.append("(timeCreated >= createDateFrom && timeCreated <= createDateTo) &&");
        }
        string.append("(isDeleted==false) &&");

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
        if (column==0 && order.equals("asc")) {
            q.setOrdering("name asc");
        }else if(column==0 && order.equals("desc")) {
            q.setOrdering("name desc");
        }
        if (column==1 && order.equals("asc")) {
            q.setOrdering("timeCreated asc");
        }else if(column==1 && order.equals("desc")) {
            q.setOrdering("timeCreated desc");
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


    /**
     *
     * @param id
     * @return true if update deleted success
     */
    public boolean updateDeleted(String id){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Course.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted= ? WHERE id=?");
        try {
            q.execute(true,id);
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
     * @param id
     * @param description
     * @param color
     * @param isDemo
     * @return
     * @throws Exception
     */
    public boolean updateCourse(String id, String name,String description) throws Exception{
        boolean isUpdate=false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Level.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET description=?, name=?  WHERE id=?");
        try {
            q.execute(description,name,id);
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
}
