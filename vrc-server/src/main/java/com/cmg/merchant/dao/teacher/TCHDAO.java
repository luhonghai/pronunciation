package com.cmg.merchant.dao.teacher;

import com.cmg.lesson.data.jdo.country.Country;
import com.cmg.lesson.data.jdo.question.Question;
import com.cmg.merchant.common.SqlReport;
import com.cmg.merchant.data.jdo.TeacherCourseHistory;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.Admin;
import com.cmg.vrc.data.jdo.TeacherMappingCompany;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lantb on 2016-04-20.
 */
public class TCHDAO extends DataAccess<TeacherCourseHistory> {
    public TCHDAO(){
        super(TeacherCourseHistory.class);
    }

    /**
     *  use for get latest version in table
     * @return latest version
     * @throws Exception
     */
    public int getLatestVersion(String idCourse){
        int version = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT max(version) FROM " + TeacherCourseHistory.class.getCanonicalName());
        q.setFilter("idCourse == paramIdCourse");
        q.declareParameters("String paramIdCourse");
        try {
            version = (int) q.execute(idCourse);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
        return version;
    }


    /**
     *  use for get latest version in table
     * @return latest version
     * @throws Exception
     */
    public TeacherCourseHistory getLatestFile(String idCourse){
        int version = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("javax.jdo.query.SQL", "Select pathAws,version from TEACHERCOURSEHISTORY " +
                "where idCourse='" + idCourse +"' order by version desc limit 1");
        try {
            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                if(data[0]!=null && data[1] !=null ){
                    TeacherCourseHistory tch = new TeacherCourseHistory();
                    tch.setPathAws(data[0].toString());
                    tch.setVersion((int) data[1]);
                    return tch;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
        return null;
    }
    /**
     *
     * @param idCourse
     * @param version
     * @return
     */
    public TeacherCourseHistory getByVersion(String idCourse, int version){
        try {
            List<TeacherCourseHistory> list = list("WHERE idCourse == :1 && version == :2", idCourse, version);
            if(list!=null && list.size() > 0){
                return list.get(0);
            }
        }catch (Exception e){}
        return null;
    }
    /**
     *
     *
     * @param studentName
     * @return
     */
    public ArrayList<TeacherCourseHistory> getListCourseByStudent(String studentName){
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlReport sql=new SqlReport();
        String query=sql.getSqlListCourseByStudent(studentName);
        System.out.println("query get course by student : " + query);
        Query q = pm.newQuery("javax.jdo.query.SQL", query);
        ArrayList<TeacherCourseHistory> list = new ArrayList<>();
        try {
            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                TeacherCourseHistory tch = new TeacherCourseHistory();
                if (data[0] != null) {
                    tch.setIdCourse(data[0].toString());
                }
                if (data[1] != null) {
                    tch.setName(data[1].toString());
                }
                list.add(tch);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
        return list;
    }
}
