package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.PhonemeScoreDB;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.List;

/**
 * Created by lantb on 2015-09-01.
 */
public class PhonemeScoreDAO extends DataAccess<PhonemeScoreDB> {

    public PhonemeScoreDAO() {
        super(PhonemeScoreDB.class);
    }

    /**
     *
     * @param username
     * @return max version of table PhonemeScore filter with username
     */
    public int getMaxVersionByUsername(String username){
        PersistenceManager pm = PersistenceManagerHelper.get();
        int maxVersion = 0;
        Query q = pm.newQuery("SELECT MAX(version) FROM " + PhonemeScoreDB.class.getCanonicalName());
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

    /**
     *
     * @param username
     * @param version
     * @return list phonemeScore filter with username and version
     */
    public List<PhonemeScoreDB> getByUsernameAndVersion(String username, int version){
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + PhonemeScoreDB.class.getCanonicalName());
        q.setFilter("username==paramUsername && version > paramVersion");
        q.declareParameters("String paramUsername, int paramVersion");
        try {
            return detachCopyAllList(pm, q.execute(username,version));
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }


}

