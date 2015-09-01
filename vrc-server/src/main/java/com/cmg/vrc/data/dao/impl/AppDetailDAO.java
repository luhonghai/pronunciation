package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.AppDetail;
import com.cmg.vrc.data.jdo.AppDetailJDO;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by luhonghai on 4/13/15.
 */
public class AppDetailDAO extends DataAccess<AppDetailJDO, AppDetail> {

    public AppDetailDAO() {
        super(AppDetailJDO.class, AppDetail.class);
    }

    public List<AppDetail> listAll() throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        //Transaction tx = pm.currentTransaction();
        List<AppDetail> list = new ArrayList<AppDetail>();
        Query q = pm.newQuery("SELECT FROM " + AppDetailJDO.class.getCanonicalName());

        try {
            //tx.begin();
            List<AppDetailJDO> tmp = (List<AppDetailJDO>) q.execute();
            Iterator<AppDetailJDO> iter = tmp.iterator();
            while (iter.hasNext()) {
                list.add(to(iter.next()));
            }
            //tx.commit();

            return list;

        } catch (Exception e) {
            throw e;
        } finally {
//            if (tx.isActive()) {
//                tx.rollback();
//            }
            q.closeAll();
            pm.close();
        }
    }
}
