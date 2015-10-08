package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.AcousticModelVersion;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.jdo.metadata.TypeMetadata;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cmg on 05/10/2015.
 */
public class AcousticModelVersionDAO extends DataAccess<AcousticModelVersion> {

    public AcousticModelVersionDAO() {
        super(AcousticModelVersion.class);
    }

    public List<AcousticModelVersion> listAll(int start, int length,String search,int column,String order) {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + AcousticModelVersion.class.getCanonicalName());
        if(search.length()>0){
            q.setFilter("(admin.toLowerCase().indexOf(search.toLowerCase()) != -1) || (fileName.toLowerCase().indexOf(search.toLowerCase()) != -1)");
        } else {
            q.setFilter("(admin == null || admin.toLowerCase().indexOf(search.toLowerCase()) != -1) " +
                    "|| (fileName == null || fileName.toLowerCase().indexOf(search.toLowerCase()) != -1)");
        }
        q.declareParameters("String search");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        String orderColumn = "version";
        switch (column) {
            case 0:
                orderColumn = "version";
                break;
            case 1:
                orderColumn = "admin";
                break;
            case 2:
                orderColumn = "fileName";
                break;
            case 3:
                orderColumn = "cfgFileName";
                break;
            case 4:
                orderColumn = "logResultName";
                break;
            case 5:
                orderColumn = "logFileName";
                break;
            case 6:
                orderColumn = "createdDate";
                break;
        }
        q.setOrdering(orderColumn + " " + order);
        q.setRange(start, start + length);
        try {
            return detachCopyAllList(pm, q.executeWithMap(params));
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }

    public int getCountSearch(String search) {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + AcousticModelVersion.class.getCanonicalName());
        if(search.length()>0){
            q.setFilter("(admin.toLowerCase().indexOf(search.toLowerCase()) != -1) || (fileName.toLowerCase().indexOf(search.toLowerCase()) != -1)");
        } else {
            q.setFilter("(admin == null || admin.toLowerCase().indexOf(search.toLowerCase()) != -1) " +
                    "|| (fileName == null || fileName.toLowerCase().indexOf(search.toLowerCase()) != -1)");
        }
        q.declareParameters("String search");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        try {
            return ((Long) q.executeWithMap(params)).intValue();
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }

    public int getMaxVersion() {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT MAX(version) FROM " + AcousticModelVersion.class.getCanonicalName());
        try {
            if (q != null)
                return (Integer) q.execute();
        } catch (Exception e) {
            //
        } finally {
            if (q != null)
                q.closeAll();
            pm.close();
        }
        return 0;

    }

    public void removeSelected() {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Transaction tx = pm.currentTransaction();
        TypeMetadata metadata = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(AcousticModelVersion.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL","UPDATE " +metadata.getTable()+ " SET selected=0");
        try {
            tx.begin();
            q.execute();
            tx.commit();
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

    public AcousticModelVersion getSelectedVersion() throws Exception {
        List<AcousticModelVersion> list = list(" WHERE selected == :1", true);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }
}
