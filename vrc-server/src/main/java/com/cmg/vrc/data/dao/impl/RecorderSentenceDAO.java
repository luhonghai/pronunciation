package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.RecordedSentence;
import com.cmg.vrc.data.jdo.RecordedSentenceJDO;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CMGT400 on 8/7/2015.
 */
public class RecorderSentenceDAO  extends DataAccess<RecordedSentenceJDO, RecordedSentence> {

    public RecorderSentenceDAO() {
        super(RecordedSentenceJDO.class, RecordedSentence.class);
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
        Transaction tx = pm.currentTransaction();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + RecordedSentenceJDO.class.getCanonicalName());

        try {
            tx.begin();
            count = (Long) q.execute();
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

    public double getCount(String ac, int sta) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Transaction tx = pm.currentTransaction();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + RecordedSentenceJDO.class.getCanonicalName());
        q.setFilter("account==ac && status==sta");
        q.declareParameters("String ac, int sta");
        try {
            tx.begin();
            count = (Long) q.execute(ac,sta);
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

    public double getCount(int sta) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Transaction tx = pm.currentTransaction();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + RecordedSentenceJDO.class.getCanonicalName());
        q.setFilter("status==sta");
        q.declareParameters("int sta");
        try {
            tx.begin();
            count = (Long) q.execute(sta);
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
