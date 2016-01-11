package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.ClassMappingTeacher;
import com.cmg.vrc.data.jdo.Student;
import com.cmg.vrc.data.jdo.StudentMappingClass;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luhonghai on 4/13/15.
 */
public class StudentMappingClassDAO extends DataAccess<StudentMappingClass> {

    public StudentMappingClassDAO() {
        super(StudentMappingClass.class);
    }


    public List<StudentMappingClass> listAll(int start, int length,String search,int column,String order,String idClasst) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + StudentMappingClass.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="((studentName.toLowerCase().indexOf(search.toLowerCase()) != -1))";
        String b="((studentName == null || studentName.toLowerCase().indexOf(search.toLowerCase()) != -1))";
        if(idClasst.length()>0){
            string.append(" idClass=idClasst &&");
        }
        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search, String idClasst");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("idClasst", idClasst);
        if (column==0 && order.equals("asc")) {
            q.setOrdering("studentName asc");
        }else if(column==0 && order.equals("desc")) {
            q.setOrdering("studentName desc");
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

    public double getCountSearch(String search) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + StudentMappingClass.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="((studentName.toLowerCase().indexOf(search.toLowerCase()) != -1))";
        String b="((studentName == null || studentName.toLowerCase().indexOf(search.toLowerCase()) != -1))";

        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
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
}
