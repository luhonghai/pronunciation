package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.Feedback;
import com.cmg.vrc.data.jdo.FeedbackJDO;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by luhonghai on 5/8/15.
 */
public class FeedbackDAO extends DataAccess<FeedbackJDO, Feedback> {

    public FeedbackDAO() {
        super(FeedbackJDO.class, Feedback.class);
    }
    public List<Feedback> listAll(int start, int length) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Transaction tx = pm.currentTransaction();
        List<Feedback> list = new ArrayList<Feedback>();
        Query q = pm.newQuery("SELECT FROM " + FeedbackJDO.class.getCanonicalName());

        q.setRange(start, start + length);
        try {
            tx.begin();
            List<FeedbackJDO> tmp = (List<FeedbackJDO>) q.execute();
            Iterator<FeedbackJDO> iter = tmp.iterator();
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
}
