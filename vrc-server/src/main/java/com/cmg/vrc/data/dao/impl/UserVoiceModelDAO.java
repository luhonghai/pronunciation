package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.UserVoiceModel;
import com.cmg.vrc.data.jdo.UserVoiceModelJDO;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.*;

/**
 * Created by luhonghai on 9/30/14.
 */
public class UserVoiceModelDAO extends DataAccess<UserVoiceModelJDO, UserVoiceModel> {

    public UserVoiceModelDAO() {
        super(UserVoiceModelJDO.class, UserVoiceModel.class);
    }
    public List<UserVoiceModel> listAll(int start, int length,String search,int column,String order,String username1,String word1,String uuid1) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        Transaction tx = pm.currentTransaction();
        List<UserVoiceModel> list = new ArrayList<UserVoiceModel>();
        Query q = pm.newQuery("SELECT FROM " + UserVoiceModelJDO.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="((username.toLowerCase().indexOf(search.toLowerCase()) != -1)||" +
                "(word.toLowerCase().indexOf(search.toLowerCase()) != -1)||" +
                "(uuid.toLowerCase().indexOf(search.toLowerCase()) != -1))";
        String b="((username == null || username.toLowerCase().indexOf(search.toLowerCase()) != -1)||" +
                "(word == null || word.toLowerCase().indexOf(search.toLowerCase()) != -1)||" +
                "(uuid == null || uuid.toLowerCase().indexOf(search.toLowerCase()) != -1))";


        if(username1.length()>0){
            string.append("(username.toLowerCase().indexOf(username1.toLowerCase()) != -1) &&");
        }
        if(word1.length()>0){
            string.append("(word.toLowerCase().indexOf(word1.toLowerCase()) != -1) &&");
        }
        if(uuid1.length()>0){
            string.append("(uuid.toLowerCase().indexOf(uuid1.toLowerCase()) != -1) &&");
        }
        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search, String username1,String word1,String uuid1");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("username1", username1);
        params.put("word1", word1);
        params.put("uuid1", uuid1);

        if (column==0 && order.equals("asc")) {
            q.setOrdering("username asc");
        }else if(column==0 && order.equals("desc")) {
            q.setOrdering("username desc");
        }
        if (column==1 && order.equals("asc")) {
            q.setOrdering("word asc");
        }else if(column==1 && order.equals("desc")) {
            q.setOrdering("word desc");
        }
        if (column==2 && order.equals("asc")) {
            q.setOrdering("score asc");
        }else if(column==2 && order.equals("desc")) {
            q.setOrdering("score desc");
        }
        if (column==3 && order.equals("asc")) {
            q.setOrdering("uuid asc");
        }else if(column==3 && order.equals("desc")) {
            q.setOrdering("uuid desc");
        }

        q.setRange(start, start + length);

        try {
            tx.begin();
            List<UserVoiceModelJDO> tmp = (List<UserVoiceModelJDO>)q.executeWithMap(params);
            Iterator<UserVoiceModelJDO> iter = tmp.iterator();
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

    public double getCountSearch(String search,String username1,String word1,String uuid1) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Transaction tx = pm.currentTransaction();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + UserVoiceModelJDO.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="((username.toLowerCase().indexOf(search.toLowerCase()) != -1)||" +
                "(word.toLowerCase().indexOf(search.toLowerCase()) != -1)||" +
                "(uuid.toLowerCase().indexOf(search.toLowerCase()) != -1))";
        String b="((username == null || username.toLowerCase().indexOf(search.toLowerCase()) != -1)||" +
                "(word == null || word.toLowerCase().indexOf(search.toLowerCase()) != -1)||" +
                "(uuid == null || uuid.toLowerCase().indexOf(search.toLowerCase()) != -1))";


        if(username1.length()>0){
            string.append("(username.toLowerCase().indexOf(username1.toLowerCase()) != -1) &&");
        }
        if(word1.length()>0){
            string.append("(word.toLowerCase().indexOf(word1.toLowerCase()) != -1) &&");
        }
        if(uuid1.length()>0){
            string.append("(uuid.toLowerCase().indexOf(uuid1.toLowerCase()) != -1) &&");
        }
        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search, String username1,String word1,String uuid1");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("username1", username1);
        params.put("word1", word1);
        params.put("uuid1", uuid1);
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
