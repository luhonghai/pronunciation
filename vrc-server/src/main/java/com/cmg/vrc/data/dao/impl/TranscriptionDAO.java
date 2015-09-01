package com.cmg.vrc.data.dao.impl;

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
 * Created by cmg on 03/07/15.
 */
public class TranscriptionDAO extends DataAccess<Transcription> {

    public TranscriptionDAO() {
        super(Transcription.class);
    }

    public Transcription getBySentence(String sentence) throws Exception {
        List<Transcription> list = list("WHERE sentence == :1", sentence);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }

    public Transcription getByIdSentence(String id) throws Exception {
        List<Transcription> list = list("WHERE id == :1", id);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }


    public List<Transcription> getListByVersion(int ver) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + Transcription.class.getCanonicalName());
        q.setFilter("version>ver");
        q.declareParameters("Integer ver");
        try {
            return detachCopyAllList(pm, q.execute(ver));
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }

    }

    /**
     * get max version
     * @return max version
     */
    public int getLatestVersion(){
        PersistenceManager pm = PersistenceManagerHelper.get();
        int version=0;
        Query q = pm.newQuery("SELECT max(version) FROM " + Transcription.class.getCanonicalName());
        try {
            if (q != null)
                version=(int)q.execute();
            return version;
        } catch (Exception e) {
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
    }





    public List<Transcription> listAll(int start, int length,String search,int column,String order,String senten,Date createDateFrom,Date createDateTo, Date modifiedDateFrom,Date modifiedDateTo) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + Transcription.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="(sentence.toLowerCase().indexOf(search.toLowerCase()) != -1)";
        String b="(sentence == null || sentence.toLowerCase().indexOf(search.toLowerCase()) != -1)";

        if(senten.length()>0){
            string.append("(sentence.toLowerCase().indexOf(senten.toLowerCase()) != -1) &&");
        }
        if(createDateFrom!=null&&createDateTo==null){
            string.append("(createdDate >= CreateDateFrom) &&");
        }
        if(createDateFrom==null&&createDateTo!=null){
            string.append("(createdDate <= CreateDateTo) &&");
        }

        if(createDateFrom!=null&&createDateTo!=null){
            string.append("(createdDate >= CreateDateFrom && createdDate <= CreateDateTo) &&");
        }
        string.append("(isDeleted==0) &&");

        if(modifiedDateFrom!=null&&modifiedDateTo==null){
            string.append("(modifiedDate >= modifiedDateFrom) &&");
        }
        if(modifiedDateFrom==null&&modifiedDateTo!=null){
            string.append("(modifiedDate <= modifiedDateTo) &&");
        }
        if(modifiedDateFrom!=null&&modifiedDateTo!=null){
            string.append("(modifiedDate >= modifiedDateFrom && modifiedDate <= modifiedDateTo) &&");
        }

        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search, String senten, java.util.Date createDateFrom,java.util.Date createDateTo, java.util.Date modifiedDateFrom,java.util.Date modifiedDateTo");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("senten", senten);
        params.put("createDateFrom", createDateFrom);
        params.put("createDateTo", createDateTo);
        params.put("modifiedDateFrom", modifiedDateFrom);
        params.put("modifiedDateTo", modifiedDateTo);

        if (column==0 && order.equals("asc")) {
            q.setOrdering("author asc");
        }else if(column==0 && order.equals("desc")) {
            q.setOrdering("author desc");
        }
        if (column==1 && order.equals("asc")) {
            q.setOrdering("sentence asc");
        }else if(column==1 && order.equals("desc")) {
            q.setOrdering("sentence desc");
        }
        if (column==2 && order.equals("asc")) {
            q.setOrdering("createdDate asc");
        }else if(column==2 && order.equals("desc")) {
            q.setOrdering("createdDate desc");
        }
        if (column==3 && order.equals("asc")) {
            q.setOrdering("modifiedDate asc");
        }else if(column==3 && order.equals("desc")) {
            q.setOrdering("modifiedDate desc");
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

    public double getCountSearch(String search,String senten,Date createDateFrom,Date createDateTo, Date modifiedDateFrom,Date modifiedDateTo) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + Transcription.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="(sentence.toLowerCase().indexOf(search.toLowerCase()) != -1)";
        String b="(sentence == null || sentence.toLowerCase().indexOf(search.toLowerCase()) != -1)";
        if(senten.length()>0){
            string.append("(sentence.toLowerCase().indexOf(senten.toLowerCase()) != -1) &&");
        }

        if(createDateFrom!=null&&createDateTo==null){
            string.append("(createdDate >= CreateDateFrom) &&");
        }
        if(createDateFrom==null&&createDateTo!=null){
            string.append("(createdDate <= CreateDateTo) &&");
        }
        string.append("(isDeleted==0) &&");

        if(createDateFrom!=null&&createDateTo!=null){
            string.append("(createdDate >= CreateDateFrom && createdDate <= CreateDateTo) &&");
        }

        if(modifiedDateFrom!=null&&modifiedDateTo==null){
            string.append("(modifiedDate >= modifiedDateFrom) &&");
        }
        if(modifiedDateFrom==null&&modifiedDateTo!=null){
            string.append("(modifiedDate <= modifiedDateTo) &&");
        }
        if(modifiedDateFrom!=null&&modifiedDateTo!=null){
            string.append("(modifiedDate >= modifiedDateFrom && modifiedDate <= modifiedDateTo) &&");
        }

        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search, String senten, java.util.Date createDateFrom,java.util.Date createDateTo, java.util.Date modifiedDateFrom,java.util.Date modifiedDateTo");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("senten", senten);
        params.put("createDateFrom", createDateFrom);
        params.put("createDateTo", createDateTo);
        params.put("modifiedDateFrom", modifiedDateFrom);
        params.put("modifiedDateTo", modifiedDateTo);

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

    public double getCount() throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + Transcription.class.getCanonicalName());
        q.setFilter(" isDeleted==0");
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


}



