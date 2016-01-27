package com.cmg.merchant.dao.mapping;


import com.cmg.merchant.data.jdo.CourseMappingTeacher;
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
     * @param state
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
    public List<CourseMappingTeacher> getAllCourses(String cpId, String tId, String cId,
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

}
