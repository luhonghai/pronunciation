package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.AppDetail;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.List;

/**
 * Created by luhonghai on 4/13/15.
 */
public class AppDetailDAO extends DataAccess<AppDetail> {

    public AppDetailDAO() {
        super(AppDetail.class);
    }

    public List<AppDetail> listAll() throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + AppDetail.class.getCanonicalName());

        try {
            List<AppDetail> tmp = (List<AppDetail>) q.execute();
            pm.detachCopyAll(tmp);
            return tmp;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }
}
