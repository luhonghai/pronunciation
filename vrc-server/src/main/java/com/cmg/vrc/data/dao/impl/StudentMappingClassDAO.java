package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.ClassMappingTeacher;
import com.cmg.vrc.data.jdo.Student;
import com.cmg.vrc.data.jdo.StudentMappingClass;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.jdo.metadata.TypeMetadata;
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



    public StudentMappingClass getByClassAndStudent(String idClass, String studentName) throws Exception{
        List<StudentMappingClass> userList = list("WHERE idClass == :1 && studentName == :2", idClass, studentName);
        if (userList != null && userList.size() > 0)
            return userList.get(0);
        return null;
    }
    public List<StudentMappingClass> listAll(int start, int length,String search,int column,String order,String idClasst, String student, Date dateFrom,Date dateTo) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + StudentMappingClass.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="((studentName.toLowerCase().indexOf(search.toLowerCase()) != -1))";
        String b="((studentName == null || studentName.toLowerCase().indexOf(search.toLowerCase()) != -1))";
        if(idClasst.length()>0){
            string.append(" idClass==idClasst &&");
        }
        if(student.length()>0){
            string.append(" (studentName.toLowerCase().indexOf(student.toLowerCase()) != -1) &&");
        }
        if(dateFrom!=null&&dateTo==null){
            string.append(" (createdDate >= dateFrom) &&");
        }
        if(dateFrom==null&&dateTo!=null){
            string.append(" (createdDate <= dateTo) &&");
        }


        if(dateFrom!=null&&dateTo!=null){
            string.append(" (createdDate >= dateFrom && createdDate <= dateTo) &&");
        }
        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search, String idClasst, String student, java.util.Date dateFrom,java.util.Date dateTo");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("idClasst", idClasst);
        params.put("student", student);
        params.put("dateFrom", dateFrom);
        params.put("dateTo", dateTo);
        if (column==0 && order.equals("asc")) {
            q.setOrdering("studentName asc");
        }else if(column==0 && order.equals("desc")) {
            q.setOrdering("studentName desc");
        }
        if (column==1 && order.equals("asc")) {
            q.setOrdering("createdDate asc");
        }else if(column==1 && order.equals("desc")) {
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

    public double getCountSearch(String search,String idClasst, String student, Date dateFrom,Date dateTo) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + StudentMappingClass.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="((studentName.toLowerCase().indexOf(search.toLowerCase()) != -1))";
        String b="((studentName == null || studentName.toLowerCase().indexOf(search.toLowerCase()) != -1))";
        if(idClasst.length()>0){
            string.append(" idClass == idClasst &&");
        }
        if(student.length()>0){
            string.append(" (studentName.toLowerCase().indexOf(student.toLowerCase()) != -1) &&");
        }
        if(dateFrom!=null&&dateTo==null){
            string.append(" (createdDate >= dateFrom) &&");
        }
        if(dateFrom==null&&dateTo!=null){
            string.append(" (createdDate <= dateTo) &&");
        }


        if(dateFrom!=null&&dateTo!=null){
            string.append(" (createdDate >= dateFrom && createdDate <= dateTo) &&");
        }
        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search,String idClasst, String student, java.util.Date dateFrom,java.util.Date dateTo");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("idClasst", idClasst);
        params.put("student", student);
        params.put("dateFrom", dateFrom);
        params.put("dateTo", dateTo);


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
    public void updateDelete(String student, String teacher) {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Transaction tx = pm.currentTransaction();
        TypeMetadata metadata = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(StudentMappingClass.class.getCanonicalName());
        TypeMetadata metaClassMappingTeacher = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(ClassMappingTeacher.class.getCanonicalName());
        String inner="(select idClass from "+metaClassMappingTeacher.getTable()+" where teacherName="+teacher+")";
        Query q = pm.newQuery("javax.jdo.query.SQL","UPDATE " +metadata.getTable()+ " SET isDeleted=true WHERE idClass in "+inner+" and studentName="+student+"");
        try {
            tx.begin();
            q.execute();
            tx.commit();
        } catch (Exception e) {
            throw e;
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            q.closeAll();
            pm.close();
        }
    }
}
