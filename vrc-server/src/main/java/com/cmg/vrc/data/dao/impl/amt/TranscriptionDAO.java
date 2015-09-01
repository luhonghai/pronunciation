package com.cmg.vrc.data.dao.impl.amt;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.Transcription;
import com.cmg.vrc.data.jdo.TranscriptionJDO;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by cmg on 03/07/15.
 */
public class TranscriptionDAO extends DataAccess<TranscriptionJDO, Transcription> {

    public TranscriptionDAO() {
        super(TranscriptionJDO.class, Transcription.class);
    }

    public Transcription getBySentence(String sentence) throws Exception {
        List<Transcription> list = list("WHERE sentence == :1", sentence);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }

    /**
     * get max version
     * @return max version
     */
    public int getLatestVersion(){
        PersistenceManager pm = PersistenceManagerHelper.get();
        //Transaction tx = pm.currentTransaction();
        int version=0;
        Query q = pm.newQuery("SELECT max(version) FROM " + TranscriptionJDO.class.getCanonicalName());
        try {
          //  tx.begin();
             version=(int)q.execute();
          //  tx.commit();
            return version;
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

    public List<Transcription> listAll(int limit) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        //Transaction tx = pm.currentTransaction();
        List<Transcription> list = new ArrayList<Transcription>();
        Query q = pm.newQuery("SELECT FROM " + TranscriptionJDO.class.getCanonicalName());
        q.setRange(0, limit);
        try {
            //tx.begin();
            List<TranscriptionJDO> tmp = (List<TranscriptionJDO>) q.execute();
            Iterator<TranscriptionJDO> iter = tmp.iterator();
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
