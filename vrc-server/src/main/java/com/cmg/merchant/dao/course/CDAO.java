package com.cmg.merchant.dao.course;

import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
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
}
