package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.LanguageModelVersion;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.jdo.metadata.TypeMetadata;

/**
 * Created by cmg on 10/09/15.
 */
public class LanguageModelVersionDAO extends DataAccess<LanguageModelVersion> {

    public LanguageModelVersionDAO() {
        super(LanguageModelVersion.class);
    }

    public int getMaxVersion() {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT MAX(version) FROM " + LanguageModelVersion.class.getCanonicalName());
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
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(LanguageModelVersion.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL","UPDATE " +metaRecorderSentence.getTable()+ " SET selected=0");
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
}
