package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.LicenseCode;
import com.cmg.vrc.data.jdo.LicenseCodeJDO;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by cmg on 5/18/15.
 */
public class LicenseCodeDAO extends DataAccess<LicenseCodeJDO, LicenseCode> {

    public LicenseCodeDAO() {
        super(LicenseCodeJDO.class, LicenseCode.class);
    }

    public List<LicenseCode> listAll(int start, int length) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Transaction tx = pm.currentTransaction();
        List<LicenseCode> list = new ArrayList<LicenseCode>();
        Query q = pm.newQuery("SELECT FROM " + LicenseCodeJDO.class.getCanonicalName());

        q.setRange(start, start + length);

        try {
            tx.begin();
            List<LicenseCodeJDO> tmp = (List<LicenseCodeJDO>) q.execute();
            Iterator<LicenseCodeJDO> iter = tmp.iterator();
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
    public List<LicenseCode> listAll(int start, int length,String account,String code, String acti,String dateFrom,String dateTo) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Transaction tx = pm.currentTransaction();
        List<LicenseCode> list = new ArrayList<LicenseCode>();
        Query q = pm.newQuery("SELECT FROM " + LicenseCodeJDO.class.getCanonicalName());

        q.setRange(start, start + length);

        try {
            tx.begin();
            List<LicenseCodeJDO> tmp = (List<LicenseCodeJDO>) q.execute();
            Iterator<LicenseCodeJDO> iter = tmp.iterator();
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

    public LicenseCode getByCode(String code) throws Exception {
        List<LicenseCode> list = list("WHERE code = :1", code);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }
}
