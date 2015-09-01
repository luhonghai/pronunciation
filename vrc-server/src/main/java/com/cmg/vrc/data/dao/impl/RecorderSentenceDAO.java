package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.RecordedSentence;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CMGT400 on 8/7/2015.
 */
public class RecorderSentenceDAO  extends DataAccess<RecordedSentence> {

    public RecorderSentenceDAO() {
        super(RecordedSentence.class);
    }
    public RecordedSentence getBySentence(String sentence) throws Exception {
        List<RecordedSentence> list = list("WHERE sentence == :1", sentence);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }

    public RecordedSentence getByIdSentence(String id) throws Exception {
        List<RecordedSentence> list = list("WHERE id == :1", id);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }



    public double getCount() throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + RecordedSentence.class.getCanonicalName());

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

    public double getCount(String ac, int sta) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + RecordedSentence.class.getCanonicalName());
        q.setFilter("account==ac && status==sta");
        q.declareParameters("String ac, int sta");
        try {
            count = (Long) q.execute(ac,sta);
            return count.doubleValue();
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }

    public double getCount(int sta) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + RecordedSentence.class.getCanonicalName());
        q.setFilter("status==sta");
        q.declareParameters("int sta");
        try {
            count = (Long) q.execute(sta);
            return count.doubleValue();
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }

    public RecordedSentence getBySentenceIdAndAccount(String sentenceId, String account) throws Exception {
        List<RecordedSentence> list = list("WHERE sentenceId == :1 && account == :2", sentenceId, account);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }

    public Map<String, RecordedSentence> getByAccount(String account) throws Exception {
        List<RecordedSentence> list = list("WHERE account == :1", account);
        if (list != null && list.size() > 0) {
            Map<String, RecordedSentence> map = new HashMap<String, RecordedSentence>();
            for (RecordedSentence r : list) {
                map.put(r.getSentenceId(), r);
            }
            return map;
        }
        return null;
    }
}
