package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.RecordedSentence;
import com.cmg.vrc.data.jdo.RecordedSentenceJDO;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.*;

/**
 * Created by CMGT400 on 6/8/2015.
 */
public class RecorderDAO extends DataAccess<RecordedSentenceJDO,RecordedSentence> {
    public RecorderDAO(){
        super(RecordedSentenceJDO.class,RecordedSentence.class);
    }
    public RecordedSentence getBySentenceIdAndAccount(String sentenceId, String account) throws Exception {
        List<RecordedSentence> list = list("WHERE sentenceId == :1 && account == :2", sentenceId, account);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;

    }

    public RecordedSentence getUserByEmailPassword(String email, String password) throws Exception{
        List<RecordedSentence> userList = list("WHERE userName == :1 && password == :2", email, password);
        if (userList != null && userList.size() > 0)
            return userList.get(0);
        return null;
    }

    public boolean adminUpdate(String idSentence, boolean isDelete, int statu, int ver){
        boolean result=false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Transaction tx = pm.currentTransaction();
        Query q = pm.newQuery("javax.jdo.query.SQL","UPDATE recordedsentencejdo SET status ="+statu+", version="+ver+", isDeleted="+isDelete+" WHERE sentenceId='"+idSentence+"'");
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
        Transaction tx = pm.currentTransaction();
        List<RecordedSentence> list = new ArrayList<RecordedSentence>();
        Query q = pm.newQuery("SELECT FROM " + RecordedSentenceJDO.class.getCanonicalName());
        q.setFilter("account==acc && version>=ver");
        q.declareParameters("String acc, Integer ver");
        try {
            tx.begin();
            List<RecordedSentenceJDO> tmp = (List<RecordedSentenceJDO>)q.execute(acc,ver);
            Iterator<RecordedSentenceJDO> iter = tmp.iterator();
            while (iter.hasNext()) {
                list.add(to(iter.next()));
            }
            tx.commit();
            System.out.println(list.size());
            return list;
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



    public RecordedSentence getUserByEmail(String email) throws Exception {
        List<RecordedSentence> userList = list("WHERE userName == :1", email);
        if (userList != null && userList.size() > 0)
            return userList.get(0);
        return null;
    }

    public int getLatestVersion(){
        PersistenceManager pm = PersistenceManagerHelper.get();
        Transaction tx = pm.currentTransaction();
        int version=0;
        Query q = pm.newQuery("SELECT max(version) FROM " + RecordedSentenceJDO.class.getCanonicalName());
        try {
            tx.begin();
            version=(int)q.execute();
            tx.commit();
            return version;
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



    public List<RecordedSentence> listAll(int start, int length,String search,int column,String order,String sten,String ac,Date dateFrom, Date dateTo, int sta) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        Transaction tx = pm.currentTransaction();
        List<RecordedSentence> list = new ArrayList<RecordedSentence>();
        Query q = pm.newQuery("SELECT FROM " + RecordedSentenceJDO.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="((userName.toLowerCase().indexOf(search.toLowerCase()) != -1)||(firstName.toLowerCase().indexOf(search.toLowerCase()) != -1)||(lastName.toLowerCase().indexOf(search.toLowerCase()) != -1))";
        String b="((userName == null || userName.toLowerCase().indexOf(search.toLowerCase()) != -1)||(firstName == null || firstName.toLowerCase().indexOf(search.toLowerCase()) != -1)||(lastName == null || lastName.toLowerCase().indexOf(search.toLowerCase()) != -1))";

        if(sten.length()>0){
            string.append("(userName.toLowerCase().indexOf(user.toLowerCase()) != -1) &&");
        }
        if(ac.length()>0){
            string.append("(firstName.toLowerCase().indexOf(fisrt.toLowerCase()) != -1) &&");
        }
        if(dateFrom!=null&&dateTo==null){
            string.append("(createdDate >= dateFrom) &&");
        }
        if(dateFrom==null&&dateTo!=null){
            string.append("(createdDate <= dateTo) &&");
        }

        if(dateFrom!=null&&dateTo!=null){
            string.append("(createdDate >= dateFrom && createdDate <= dateTo) &&");
        }
        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search, String user, String fisrt, String last");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("sten", sten);
        params.put("ac", ac);
        params.put("dateFrom", dateFrom);
        params.put("dateTo", dateTo);

        if (column==0 && order.equals("asc")) {
            q.setOrdering("userName asc");
        }else if(column==0 && order.equals("desc")) {
            q.setOrdering("userName desc");
        }
        if (column==1 && order.equals("asc")) {
            q.setOrdering("firstName asc");
        }else if(column==1 && order.equals("desc")) {
            q.setOrdering("firstName desc");
        }
        if (column==2 && order.equals("asc")) {
            q.setOrdering("lastName asc");
        }else if(column==2 && order.equals("desc")) {
            q.setOrdering("lastName desc");
        }
        if (column==3 && order.equals("asc")) {
            q.setOrdering("role asc");
        }else if(column==3 && order.equals("desc")) {
            q.setOrdering("role desc");
        }

        q.setRange(start, start + length);

        try {
            tx.begin();
            List<RecordedSentenceJDO> tmp = (List<RecordedSentenceJDO>)q.executeWithMap(params);
            Iterator<RecordedSentenceJDO> iter = tmp.iterator();
            while (iter.hasNext()) {
                list.add(to(iter.next()));
            }
            tx.commit();
            return list;
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

    public double getCountSearch(String search,String sten,String ac,Date dateFrom, Date dateTo, int sta) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Transaction tx = pm.currentTransaction();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + RecordedSentenceJDO.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="((userName.toLowerCase().indexOf(search.toLowerCase()) != -1)||(account.toLowerCase().indexOf(search.toLowerCase()) != -1))";
        String b="((userName == null || userName.toLowerCase().indexOf(search.toLowerCase()) != -1)||(firstName == null || firstName.toLowerCase().indexOf(search.toLowerCase()) != -1)||(lastName == null || lastName.toLowerCase().indexOf(search.toLowerCase()) != -1))";

        if(sten.length()>0){
            string.append("(userName.toLowerCase().indexOf(user.toLowerCase()) != -1) &&");
        }
        if(ac.length()>0){
            string.append("(firstName.toLowerCase().indexOf(fisrt.toLowerCase()) != -1) &&");
        }
        if(dateFrom!=null&&dateTo==null){
            string.append("(createdDate >= dateFrom) &&");
        }
        if(dateFrom==null&&dateTo!=null){
            string.append("(createdDate <= dateTo) &&");
        }

        if(dateFrom!=null&&dateTo!=null){
            string.append("(createdDate >= dateFrom && createdDate <= dateTo) &&");
        }
        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search, String user, String fisrt, String last");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("sten", sten);
        params.put("ac", ac);
        params.put("dateFrom", dateFrom);
        params.put("dateTo", dateTo);
        try {
            tx.begin();
            count = (Long) q.executeWithMap(params);
            tx.commit();
            return count.doubleValue();
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
