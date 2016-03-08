package com.cmg.merchant.dao.mapping;


import com.cmg.lesson.data.jdo.course.Course;;
import com.cmg.merchant.common.SQL;
import com.cmg.merchant.data.dto.CourseDTO;
import com.cmg.merchant.data.jdo.CourseMappingTeacher;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.*;

/**
 * Created by lantb on 2016-01-26.
 */
public class CMTDAO extends DataAccess<CourseMappingTeacher> {
    public CMTDAO(){super(CourseMappingTeacher.class);}

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    public CourseMappingTeacher getById(String id) throws Exception{
        List<CourseMappingTeacher> list = list("WHERE id == :1 && isDeleted == :2 ", id, false);
        if(list!=null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    /**
     *
     * @param id
     * @param status
     * @return true if update success
     * @throws Exception
     */
    public boolean updateStatus(String id, String status) throws Exception{
        boolean isUpdate=false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(CourseMappingTeacher.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET status=?  WHERE id=?");
        try {
            q.execute(status,id);
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
     * @param state
     * @return true if update success
     * @throws Exception
     */
    public boolean updateState(String id, String state) throws Exception{
        boolean isUpdate=false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(CourseMappingTeacher.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET state=?  WHERE id=?");
        try {
            q.execute(state,id);
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
     * @param share
     * @return true if update success
     * @throws Exception
     */
    public boolean updateShare(String id, String share) throws Exception{
        boolean isUpdate=false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(CourseMappingTeacher.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET sr=?  WHERE id=?");
        try {
            q.execute(share,id);
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
     * @return true if update success
     * @throws Exception
     */
    public boolean updateDateTime(String id) throws Exception{
        boolean isUpdate=false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(CourseMappingTeacher.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET dateCreated=?  WHERE id=?");
        try {
            q.execute(new Date(System.currentTimeMillis()),id);
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
     * @return true if update success
     * @throws Exception
     */
    public boolean updateDeleted(String id) throws Exception{
        boolean isUpdate=false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(CourseMappingTeacher.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted=?  WHERE id=?");
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
     * @param cpId
     * @param tId
     * @param cId
     * @param status
     * @param state
     * @param sr
     * @return list if have record and null if not
     * @throws Exception
     */
    public List<CourseMappingTeacher> getCourses(String cpId, String tId, String cId,
                                                    String status, String state, String sr,Date createDateFrom,Date createDateTo) throws Exception{
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + CourseMappingTeacher.class.getCanonicalName());
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuffer listParam = new StringBuffer();
        StringBuffer filter=new StringBuffer();
        filter.append("isDeleted==false &&");
        if(cpId!=null && cpId!="" && cpId.length() >0){
            filter.append("cpID == companyId &&");
            params.put("companyId", cpId);
            listParam.append("String companyId,");
        }
        if(tId!=null && tId!="" && tId.length() >0){
            filter.append("tID == teacherId &&");
            params.put("teacherId", tId);
            listParam.append("String teacherId,");
        }

        if(cId!=null && cId!="" && cId.length() >0){
            filter.append("cID == courseId &&");
            params.put("courseId", tId);
            listParam.append("String courseId,");
        }

        if(status!=null && status!="" && status.length() >0){
            filter.append("status = statusParam &&");
            params.put("statusParam", status);
            listParam.append("String statusParam,");
        }

        if(state!=null && state!="" && state.length() >0){
            filter.append("state == stateParam &&");
            params.put("stateParam",state);
            listParam.append("String stateParam,");
        }

        if(sr!=null && sr!="" && sr.length() >0){
            filter.append("sr == shareParam &&");
            params.put("shareParam",sr);
            listParam.append("String shareParam,");
        }
        if(createDateFrom!=null && createDateTo!=null){
            filter.append("(dateCreated >= createDateFrom && dateCreated <= createDateTo) &&");
            params.put("createDateFrom", createDateFrom);
            params.put("createDateTo", createDateTo);
            listParam.append("java.util.Date createDateFrom,");
            listParam.append("java.util.Date createDateTo,");
        }

        if(createDateFrom!=null&&createDateTo==null){
            filter.append("(dateCreated >= createDateFrom) &&");
            params.put("createDateFrom", createDateFrom);
            listParam.append("java.util.Date createDateFrom,");
        }

        if(createDateFrom==null&&createDateTo!=null){
            filter.append("(dateCreated <= createDateTo) &&");
            params.put("createDateTo", createDateTo);
            listParam.append("java.util.Date createDateTo,");
        }

        String filterSql = filter.toString();
        filterSql = cutString(filterSql,"&&");
        String defineParams = listParam.toString();
        defineParams = cutString(defineParams,",");
        q.setFilter(filterSql);
        q.declareParameters(defineParams);
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
     * @param a
     * @param needToCut
     * @return
     */
    public String cutString(String a, String needToCut){
        if(a!=null && a.endsWith(needToCut)){
            return a.substring(0,a.length() - needToCut.length());
        }
        return a;
    }
    public List<Course> getMyCourses(String teacherID){
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaCourse = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Course.class.getCanonicalName());
        TypeMetadata metaCourseMappingTeacher = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(CourseMappingTeacher.class.getCanonicalName());
        StringBuffer query = new StringBuffer();
        String firstQuery = "select course.id, course.name,course.description from  " + metaCourse.getTable() + " course inner join " + metaCourseMappingTeacher.getTable()+ " mapping on course.id=mapping.cID where mapping.tID='"+teacherID+"'";
        query.append(firstQuery);
        Query q = pm.newQuery("javax.jdo.query.SQL", query.toString());
        try {
            List<Course> courses = new ArrayList<>();
            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                Course course = new Course();
                if (data[0] != null) {
                    course.setId(data[0].toString());
                }else{
                    course.setId(null);
                }
                if (data[1] != null) {
                    course.setName(data[1].toString());
                }else{
                    course.setName(null);
                }
                if (data[2] != null) {
                    course.setDescription(data[2].toString());
                }else{
                    course.setDescription(null);
                }
                courses.add(course);
            }
            return courses;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }




    /**
     *
     * @param cpId
     * @param tId
     * @param status
     * @param sr
     * @return list if have record and null if not
     * @throws Exception
     */
    public List<CourseDTO> getCoursesShareInCompany(String cpId, String tId,
                                                 String status, String sr) throws Exception{
        PersistenceManager pm = PersistenceManagerHelper.get();
        SQL sqlUtil = new SQL();
        String sql = sqlUtil.getSqlShareIN(cpId,tId,status,sr);
        List<CourseDTO> list = new ArrayList<CourseDTO>();
        Query q = pm.newQuery("javax.jdo.query.SQL", sql);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size() > 0){
                for(Object obj : tmp){
                    CourseDTO dto = new CourseDTO();
                    Object[] array = (Object[]) obj;
                    if(array[0]!=null){
                        dto.setIdCourse(array[0].toString());
                    }
                    if(array[1]!=null){
                        dto.setNameCourse(array[1].toString());
                    }
                    if(array[2] != null) {
                        dto.setDescriptionCourse(array[2].toString());
                    }
                    if(array[3]!=null){
                        dto.setCompanyName(array[3].toString());
                    }
                    if(array[4]!=null){
                        dto.setState(array[4].toString());
                    }
                    if(array[5]!=null){
                        dto.setDateCreated(array[5].toString());
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

    /**
     *
     * @param cpId
     * @param tId
     * @param status
     * @param sr
     * @return list if have record and null if not
     * @throws Exception
     */
    public List<CourseDTO> getCoursesCreateByTeacher(String cpId, String tId,
                                                               String status, String sr) throws Exception{
        PersistenceManager pm = PersistenceManagerHelper.get();
        SQL sqlUtil = new SQL();
        String sql = sqlUtil.getSqlCreatedByTeacher(cpId, tId, status, sr);
        return null;
    }

    /**
     *
     * @param cpId
     * @param tId
     * @param status
     * @param sr
     * @return list if have record and null if not
     * @throws Exception
     */
    public List<CourseDTO> getCoursesShareAll(String cpId, String tId,
                                                                String status, String sr) throws Exception{
        PersistenceManager pm = PersistenceManagerHelper.get();
        SQL sqlUtil = new SQL();
        String sql = sqlUtil.getSqlShareAll(cpId, tId, status, sr);
        return null;
    }


}
