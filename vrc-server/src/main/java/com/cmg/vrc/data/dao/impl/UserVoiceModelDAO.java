package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.UserVoiceModel;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luhonghai on 9/30/14.
 */
public class UserVoiceModelDAO extends DataAccess<UserVoiceModel> {

    public UserVoiceModelDAO() {
        super(UserVoiceModel.class);
    }
    public List<UserVoiceModel> listAll(int start, int length,String search,int column,String order,String username1,String word1,String uuid1) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + UserVoiceModel.class.getCanonicalName());
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
            q.setOrdering("imei asc");
        }else if(column==3 && order.equals("desc")) {
            q.setOrdering("imei desc");
        }
        if (column==4 && order.equals("asc")) {
            q.setOrdering("time asc");
        }else if(column==4 && order.equals("desc")) {
            q.setOrdering("time desc");
        }

        q.setRange(start, start + length);

        try {
            List<UserVoiceModel> tmp = (List<UserVoiceModel>)q.executeWithMap(params);
            pm.detachCopyAll(tmp);
            return tmp;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }

    public List<UserVoiceModel> listAllScore(String search,String username1,String word1,String uuid1) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + UserVoiceModel.class.getCanonicalName());
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
        q.setOrdering("serverTime asc");


        try {
            List<UserVoiceModel> tmp = (List<UserVoiceModel>)q.executeWithMap(params);
            pm.detachCopyAll(tmp);
            return tmp;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }


    public List<UserVoiceModel> listAllScore() throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + UserVoiceModel.class.getCanonicalName());
        q.setRange(0, 10000);
        q.setOrdering("serverTime asc");
        try {
            List<UserVoiceModel> tmp = (List<UserVoiceModel>)q.execute();
            pm.detachCopyAll(tmp);
            return tmp;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }



    public double getCountSearch(String search,String username1,String word1,String uuid1) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + UserVoiceModel.class.getCanonicalName());
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
            count = (Long) q.executeWithMap(params);
            return count.doubleValue();
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }

    /**
     *
     * @param username
     * @return max version for table user voice jdo with filter username
     */
    public int getMaxVersion(String username){
        PersistenceManager pm = PersistenceManagerHelper.get();
        int maxVersion = 1;
        Query q = pm.newQuery("SELECT Max(version) FROM " + UserVoiceModel.class.getCanonicalName());
        q.setFilter("username==paramUsername");
        q.declareParameters("String paramUsername");
        try {
            maxVersion = (int) q.execute(username);
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
        return maxVersion;
    }
}
