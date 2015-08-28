package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.LicenseCodeCompany;
import com.cmg.vrc.data.jdo.LicenseCodeCompanyJDO;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.*;

/**
 * Created by cmg on 5/18/15.
 */
public class LicenseCodeCompanyDAO extends DataAccess<LicenseCodeCompanyJDO, LicenseCodeCompany> {

    public LicenseCodeCompanyDAO() {
        super(LicenseCodeCompanyJDO.class, LicenseCodeCompany.class);
    }


    public List<LicenseCodeCompany> listByCompany(String com) throws Exception {
        return list("WHERE company == :1", com);
    }

    public List<LicenseCodeCompany> listAll() throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        Transaction tx = pm.currentTransaction();
        List<LicenseCodeCompany> list = new ArrayList<LicenseCodeCompany>();
        Query q = pm.newQuery("SELECT FROM " + LicenseCodeCompanyJDO.class.getCanonicalName());
        q.setFilter("isDeleted==false");
        try {
            tx.begin();
            List<LicenseCodeCompanyJDO> tmp = (List<LicenseCodeCompanyJDO>)q.execute();
            Iterator<LicenseCodeCompanyJDO> iter = tmp.iterator();
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

