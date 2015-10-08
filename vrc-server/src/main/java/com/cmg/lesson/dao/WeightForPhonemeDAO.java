package com.cmg.lesson.dao;


import com.cmg.lesson.data.jdo.Question;
import com.cmg.lesson.data.jdo.WeightForPhoneme;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.List;

/**
 * Created by lantb on 2015-10-08.
 */
public class WeightForPhonemeDAO extends DataAccess<WeightForPhoneme> {

    public WeightForPhonemeDAO() {
        super(WeightForPhoneme.class);
    }

    /**
     *
     * @return max version
     * @throws Exception
     */
    public int getLatestVersion() throws Exception{
        int version = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT max(version) FROM " + WeightForPhoneme.class.getCanonicalName());
        try {
            if (q != null) {
                version = (int) q.execute();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
                pm.close();
        }
        return version;
    }

    /**
     *
     * @param idQuestion
     * @param idWord
     * @return list weight for phoneme
     * @throws Exception
     */
    public List<WeightForPhoneme> listBy(String idQuestion, String idWord) throws Exception{
        List<WeightForPhoneme> list = list("WHERE idQuestion == :1 && idWordCollection == :2 && isDeleted == :3",idQuestion,idWord,false);
        if(list!=null && list.size() > 0){
            return list;
        }
        return null;
    }

    /**
     *
     * @param idQuestion
     * @return
     * @throws Exception
     */
    public boolean updateDeletedByIdQuestion(String idQuestion) throws Exception{
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(WeightForPhoneme.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted= ? WHERE idQuestion=?");
        try {
            q.execute(true,idQuestion);
            check=true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
        return check;
    }

    /**
     *
     * @param idWord
     * @return
     * @throws Exception
     */
    public boolean updateDeletedByIdWord(String idWord) throws Exception{
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(WeightForPhoneme.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted= ? WHERE idWordCollection=?");
        try {
            q.execute(true,idWord);
            check=true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
        return check;
    }

    /**
     *
     * @param idQuestion
     * @param idWord
     * @return
     * @throws Exception
     */
    public boolean updateDeletedBy(String idQuestion, String idWord) throws Exception{
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(WeightForPhoneme.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted= ? WHERE idWordCollection=? and idQuestion=?");
        try {
            q.execute(true,idWord,idQuestion);
            check=true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
        return check;
    }
}
