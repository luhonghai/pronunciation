package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.RecordedSentence;
import com.cmg.vrc.data.jdo.RecorderClient;
import com.cmg.vrc.data.jdo.Transcription;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.jdo.metadata.TypeMetadata;
import java.util.*;

/**
 * Created by CMGT400 on 6/8/2015.
 */
public class RecorderDAO extends DataAccess<RecordedSentence> {
    public RecorderDAO(){
        super(RecordedSentence.class);
    }
    public RecordedSentence getBySentenceIdAndAccount(String sentenceId, String account) throws Exception {
        List<RecordedSentence> list = list("WHERE sentenceId == :1 && account == :2", sentenceId, account);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;

    }

    public List<RecordedSentence> listByIdSentence(String id) throws Exception {
        return list("WHERE sentenceId == :1", id);
    }

    public RecordedSentence getUserByEmailPassword(String email, String password) throws Exception{
        List<RecordedSentence> userList = list("WHERE userName == :1 && password == :2", email, password);
        if (userList != null && userList.size() > 0)
            return userList.get(0);
        return null;
    }


    public boolean adminUpdate(String idSentence, int isDelete, int statu, int ver){
        boolean result=false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Transaction tx = pm.currentTransaction();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(RecordedSentence.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL","UPDATE " +metaRecorderSentence.getTable()+ " SET status ="+statu+", version="+ver+", isDeleted="+isDelete+" WHERE sentenceId='"+idSentence+"'");
        try {
            tx.begin();
            q.execute();
            tx.commit();
            result=true;
        } catch (Exception e) {
            throw e;
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            q.closeAll();
            pm.close();
        }
        return  result;
    }

    public List<RecordedSentence> getListByVersion(int ver, String acc) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + RecordedSentence.class.getCanonicalName());
        q.setFilter("account==acc && version>ver");
        q.declareParameters("String acc, Integer ver");
        try {
            return detachCopyAllList(pm, q.execute(acc,ver));
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }

    }



    public RecordedSentence getUserByEmail(String email) throws Exception {
        List<RecordedSentence> userList = list("WHERE userName == :1", email);
        if (userList != null && userList.size() > 0)
            return userList.get(0);
        return null;
    }

    public int getLatestVersion(){
        PersistenceManager pm = PersistenceManagerHelper.get();
        int version=0;
        Query q = pm.newQuery("SELECT max(version) FROM " + RecordedSentence.class.getCanonicalName());
        try {
            if (q != null)
                version=(int)q.execute();
            return version;
        } catch (Exception e) {
            throw e;
        } finally {
            if (q != null)
                q.closeAll();
            pm.close();
        }
    }


    public double getCount() throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + RecordedSentence.class.getCanonicalName());
        q.setFilter(" status!=0");
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



    public List<RecorderClient> listRecoder(int start, int length,String search,int column,String order,String ac,String dateFrom, String dateTo, int sta, String acs, String sentence) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        StringBuffer query=new StringBuffer();
        TypeMetadata metaRecorder = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(RecordedSentence.class.getCanonicalName());
        TypeMetadata metaTranscription= PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Transcription.class.getCanonicalName());
        String firstQuery = "SELECT R.id, R.account, R.admin, T.sentence, R.status, R.isDeleted, R.version, R.createdDate, R.modifiedDate, R.sentenceId, R.fileName FROM " +  metaRecorder.getTable()
                + " as R INNER join "  + metaTranscription.getTable()
                + " as T on R.SENTENCEID = T.ID WHERE ";
        query.append(firstQuery);
        query.append(" (R.account LIKE '%" + search + "%' or T.sentence LIKE '%" + search.toUpperCase() + "%')");
        if(ac.length()>0){
            query.append(" and R.account LIKE '%" + ac + "%'");
        }
        if(acs.length()>0){
            query.append(" and R.account LIKE '%" + acs + "%'");
        }
        if(sentence.length()>0){
            query.append(" and T.sentence LIKE '%" + sentence + "%'");
        }
        if(sta!=6) {
            query.append(" and R.status='" + sta + "'");
        }
        if(sta==6){
            query.append(" and R.status!=0");
        }
        if(dateFrom.length()>0 && dateTo.equalsIgnoreCase("")){
            query.append(" and R.modifiedDate >= '" + dateFrom + "'");
        }
        if(dateFrom.equalsIgnoreCase("") && dateTo.length()>0){
            query.append(" and R.modifiedDate <= '" + dateTo + "'");
        }

        if(dateFrom.length()>0 && dateTo.length()>0){
            query.append(" and R.modifiedDate >= '" + dateFrom + "' and R.modifiedDate <= '" + dateTo + "'");
        }
        if (column==0 && order.equals("asc")) {
            query.append(" ORDER BY R.account ASC");
        }else if(column==0 && order.equals("desc")) {
            query.append(" ORDER BY R.account DESC");
        }
        if (column==1 && order.equals("asc")) {
            query.append(" ORDER BY T.sentence ASC");
        }else if(column==1 && order.equals("desc")) {
            query.append(" ORDER BY T.sentence DESC");
        }
        if (column==3 && order.equals("asc")) {
            query.append(" ORDER BY R.modifiedDate ASC");
        }else if(column==3 && order.equals("desc")) {
            query.append(" ORDER BY R.modifiedDate DESC");
        }
        if (column==4 && order.equals("asc")) {
            query.append(" ORDER BY R.status ASC");
        }else if(column==4 && order.equals("desc")) {
            query.append(" ORDER BY R.status DESC");
        }
        query.append(" limit " + start + "," + length);


        Query q = pm.newQuery("javax.jdo.query.SQL",query.toString());
        List<RecorderClient> list = new ArrayList<RecorderClient>();
        try {
            List<Object> tmp = (List<Object>)q.execute();
            for(Object obj : tmp){
                RecorderClient recorderClient=new RecorderClient();
                Object[] array =(Object[]) obj;
                if(array[0].toString().length()>0) {
                    recorderClient.setId(array[0].toString());
                }
                else{
                    recorderClient.setId(null);
                }
                if(array[1]!=null) {
                    recorderClient.setAccount(array[1].toString());
                }
                else{
                    recorderClient.setAccount(null);
                }
                if(array[2]!=null) {
                    recorderClient.setAdmin(array[2].toString());
                }
                else {
                    recorderClient.setAdmin(null);
                }
                if(array[3]!=null) {
                    recorderClient.setSentence(array[3].toString());
                }else{
                    recorderClient.setSentence(null);
                }
                if(array[4]!=null) {
                    recorderClient.setStatus(Integer.parseInt(array[4].toString()));
                }else {
                    recorderClient.setStatus(0);
                }
                if(array[5]!=null) {
                    recorderClient.setIsDeleted(Integer.parseInt(array[5].toString()));
                }else{
                    recorderClient.setIsDeleted(0);
                }

                if(array[6]!=null) {
                    recorderClient.setVersion(Integer.parseInt(array[6].toString()));
                }else {
                    recorderClient.setVersion(0);
                }
                if(array[7]!=null) {
                    recorderClient.setCreatedDate((Date) array[7]);
                }
                else {
                    recorderClient.setCreatedDate(null);
                }
                if(array[8]!=null) {
                    recorderClient.setModifiedDate((Date) array[8]);
                }
                else {
                    recorderClient.setModifiedDate(null);
                }
                if(array[9]!=null) {
                    recorderClient.setSentenceId(array[9].toString());
                } else {
                    recorderClient.setSentenceId(null);
                }
                if(array[10]!=null) {
                    recorderClient.setFileName(array[10].toString());
                } else {
                    recorderClient.setFileName(null);
                }

                list.add(recorderClient);

            }

            return list;
        } catch (Exception e) {
            throw e;
        } finally {

            q.closeAll();
            pm.close();
        }
    }

    public List<RecorderClient> listRecoderCount(String search,int column,String order,String ac,String dateFrom, String dateTo, int sta, String acs, String sentence) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        StringBuffer query=new StringBuffer();
        TypeMetadata metaRecorder = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(RecordedSentence.class.getCanonicalName());
        TypeMetadata metaTranscription= PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Transcription.class.getCanonicalName());
        String firstQuery = "SELECT R.id, R.account, R.admin, T.sentence, R.status, R.isDeleted, R.version, R.createdDate, R.modifiedDate, R.sentenceId, R.fileName FROM " +  metaRecorder.getTable()
                + " as R INNER join "  + metaTranscription.getTable()
                + " as T on R.SENTENCEID = T.ID WHERE ";
        query.append(firstQuery);
        query.append(" (R.account LIKE '%" + search + "%' or T.sentence LIKE '%" + search.toUpperCase() + "%')");
        if(ac.length()>0){
            query.append(" and R.account LIKE '%" + ac + "%'");
        }
        if(acs.length()>0){
            query.append(" and R.account LIKE '%" + acs + "%'");
        }
        if(sentence.length()>0){
            query.append(" and T.sentence LIKE '%" + sentence + "%'");
        }
        if(sta!=6) {
            query.append(" and R.status='" + sta + "'");
        }
        if(sta==6){
            query.append(" and R.status!=0");
        }
        if(dateFrom.length()>0 && dateTo.equalsIgnoreCase("")){
            query.append(" and R.modifiedDate >= '" + dateFrom + "'");
        }
        if(dateFrom.equalsIgnoreCase("") && dateTo.length()>0){
            query.append(" and R.modifiedDate <= '" + dateTo + "'");
        }

        if(dateFrom.length()>0 && dateTo.length()>0){
            query.append(" and R.modifiedDate >= '" + dateFrom + "' and R.modifiedDate <= '" + dateTo + "'");
        }
        if (column==0 && order.equals("asc")) {
            query.append(" ORDER BY R.account ASC");
        }else if(column==0 && order.equals("desc")) {
            query.append(" ORDER BY R.account DESC");
        }
        if (column==1 && order.equals("asc")) {
            query.append(" ORDER BY T.sentence ASC");
        }else if(column==1 && order.equals("desc")) {
            query.append(" ORDER BY T.sentence DESC");
        }
        if (column==3 && order.equals("asc")) {
            query.append(" ORDER BY R.modifiedDate ASC");
        }else if(column==3 && order.equals("desc")) {
            query.append(" ORDER BY R.modifiedDate DESC");
        }
        if (column==4 && order.equals("asc")) {
            query.append(" ORDER BY R.status ASC");
        }else if(column==4 && order.equals("desc")) {
            query.append(" ORDER BY R.status DESC");
        }

        Query q = pm.newQuery("javax.jdo.query.SQL",query.toString());
        List<RecorderClient> list = new ArrayList<RecorderClient>();
        try {
            List<Object> tmp = (List<Object>)q.execute();
            for(Object obj : tmp){
                RecorderClient recorderClient=new RecorderClient();
                Object[] array =(Object[]) obj;
                if(array[0].toString().length()>0) {
                    recorderClient.setId(array[0].toString());
                }
                else{
                    recorderClient.setId(null);
                }
                if(array[1]!=null) {
                    recorderClient.setAccount(array[1].toString());
                }
                else{
                    recorderClient.setAccount(null);
                }
                if(array[2]!=null) {
                    recorderClient.setAdmin(array[2].toString());
                }
                else {
                    recorderClient.setAdmin(null);
                }
                if(array[3]!=null) {
                    recorderClient.setSentence(array[3].toString());
                }else{
                    recorderClient.setSentence(null);
                }
                if(array[4]!=null) {
                    recorderClient.setStatus(Integer.parseInt(array[4].toString()));
                }else {
                    recorderClient.setStatus(0);
                }
                if(array[5]!=null) {
                    recorderClient.setIsDeleted(Integer.parseInt(array[5].toString()));
                }else{
                    recorderClient.setIsDeleted(0);
                }

                if(array[6]!=null) {
                    recorderClient.setVersion(Integer.parseInt(array[6].toString()));
                }else {
                    recorderClient.setVersion(0);
                }
                if(array[7]!=null) {
                    recorderClient.setCreatedDate((Date) array[7]);
                }
                else {
                    recorderClient.setCreatedDate(null);
                }
                if(array[8]!=null) {
                    recorderClient.setModifiedDate((Date) array[8]);
                }
                else {
                    recorderClient.setModifiedDate(null);
                }
                if(array[9]!=null) {
                    recorderClient.setSentenceId(array[9].toString());
                } else {
                    recorderClient.setSentenceId(null);
                }
                if(array[10]!=null) {
                    recorderClient.setFileName(array[10].toString());
                } else {
                    recorderClient.setFileName(null);
                }

                list.add(recorderClient);

            }

            return list;
        } catch (Exception e) {
            throw e;
        } finally {

            q.closeAll();
            pm.close();
        }
    }



}
