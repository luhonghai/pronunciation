package com.cmg.merchant.dao.course;

import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.merchant.common.SqlUtil;
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
 * Created by lantb on 2016-01-26.
 */
public class CDAO extends DataAccess<Course> {
    public CDAO(){super(Course.class);}


    /**
     *
     * @return
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
     * @param id
     * @return
     * @throws Exception
     */
    public Course getByName(String name )throws Exception{
        boolean isExist = false;
        List<Course> list = list("WHERE name == :1 && isDeleted == :2 ", name, false);
        if(list!=null && list.size() > 0){
            return list.get(0);
        }
        return null;
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
     * @param start
     * @param length
     * @param course
     * @return
     * @throws Exception
     */
    public List<Course> suggestionCourse(int start, int length,String course) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + Course.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        string.append("(name.toLowerCase().indexOf(course.toLowerCase()) != -1) &&");
        string.append("(isDeleted==false)");
        q.setFilter(string.toString());
        q.declareParameters("String course");
        q.setOrdering("name asc");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("course", course);
        q.setRange(0, 3);
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
     * @param idCourse
     * @param name
     * @param description
     * @return
     */
    public boolean updateCourse(String idCourse, String name, String description){
        boolean isUpdate=false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Course.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET name=? , description=?  WHERE id=?");
        try {
            q.execute(name,description,idCourse);
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
     * @param idCourse
     * @return
     */
    public void deleteCourseStep1(String idCourse){
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlUtil sqlUtil = new SqlUtil();
        String sql = sqlUtil.getSqlDeleteCourse1(idCourse);
        System.out.println("sql delete course 1:  " + sql);
        Query q = pm.newQuery("javax.jdo.query.SQL",sql);
        try {
            q.execute();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
    }

    /**
     *
     * @param idCourse
     * @return
     */
    public void deleteCourseStep2(String idCourse){
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlUtil sqlUtil = new SqlUtil();
        String sql = sqlUtil.getSqlDeleteCourse2(idCourse);
        System.out.println("sql delete course 2 :  " + sql);
        Query q = pm.newQuery("javax.jdo.query.SQL",sql);
        try {
            q.execute();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
    }



    /**
     *
     * @param idCourse
     * @return
     */
    public void deleteCopiedDataStep1(String idCourse){
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlUtil sqlUtil = new SqlUtil();
        String sql = sqlUtil.getSqlDeleteCourse1(idCourse);
        System.out.println("sql delete course 1:  " + sql);
        Query q = pm.newQuery("javax.jdo.query.SQL",sql);
        try {
            q.execute();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
    }

    /**
     *
     * @param idCourse
     * @return
     */
    public void deleteCopiedDataStep2(String idCourse){
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlUtil sqlUtil = new SqlUtil();
        String sql = sqlUtil.getSqlDeleteCourse2(idCourse);
        System.out.println("sql delete course 2 :  " + sql);
        Query q = pm.newQuery("javax.jdo.query.SQL",sql);
        try {
            q.execute();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
    }

    /**
     *
     * @param idCourse
     * @return
     */
    public boolean isAssignToClass(String idCourse, String teacherName){
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlUtil sqlUtil = new SqlUtil();
        String sql = sqlUtil.getSqlCheckCourseAssignClass(idCourse, teacherName);
        System.out.println("check course assign :  " + sql);
        Query q = pm.newQuery("javax.jdo.query.SQL",sql);
        try {
           List<Object> tmp = (List<Object>) q.execute();
            if(tmp!= null && tmp.size() > 0){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
        return false;
    }

    /**
     *
     * @param idCourse
     * @return
     */
    public boolean checkData1(String idCourse){
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlUtil sqlUtil = new SqlUtil();
        String sql = sqlUtil.getSqlCheckDuplicateData1(idCourse);
        System.out.println("check course data copied :  " + sql);
        Query q = pm.newQuery("javax.jdo.query.SQL",sql);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!= null && tmp.size() > 0){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
        return false;
    }

    /**
     *
     * @param idCourse
     * @return
     */
    public boolean checkData2(String idCourse){
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlUtil sqlUtil = new SqlUtil();
        String sql = sqlUtil.getSqlCheckDuplicateData2(idCourse);
        System.out.println("check course data copied :  " + sql);
        Query q = pm.newQuery("javax.jdo.query.SQL",sql);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!= null && tmp.size() > 0){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
        return false;
    }
}
