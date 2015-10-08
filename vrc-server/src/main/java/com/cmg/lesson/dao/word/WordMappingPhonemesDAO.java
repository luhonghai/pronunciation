package com.cmg.lesson.dao.word;

import com.cmg.lesson.data.jdo.word.WordCollection;
import com.cmg.lesson.data.jdo.word.WordMappingPhonemes;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.jdo.metadata.TypeMetadata;
import java.util.List;

/**
 * Created by lantb on 2015-10-01.
 */
public class WordMappingPhonemesDAO extends DataAccess<WordMappingPhonemes> {
    public WordMappingPhonemesDAO(){
        super(WordMappingPhonemes.class);
    }


    /**
     *
     * @return latest version
     */
    public int getLatestVersion(){
        PersistenceManager pm = PersistenceManagerHelper.get();
        int version=0;
        Query q = pm.newQuery("SELECT max(version) FROM " + WordCollection.class.getCanonicalName());
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
     * @param idWord
     * @return
     * @throws Exception
     */
    public List<WordMappingPhonemes> getByWordID(String idWord) throws Exception{
        List<WordMappingPhonemes> list = list("WHERE wordID == :1 && isDeleted == :2", idWord, false, "index asc");
        if (list != null && list.size() > 0)
            return list;
        return null;
    }


    /**
     *
     * @param idWord
     * @return true if idWord is existed
     */
    public boolean checkExisted(String idWord)throws Exception{
        List<WordMappingPhonemes> list = list("WHERE wordID == :1", idWord);
        if (list != null && list.size() > 0)
            return true;
        return false;
    }

    /**
     *
     * @param idWord
     * @return
     */
    public boolean updateDeleted(String idWord, boolean isDeleted) {
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Transaction tx = pm.currentTransaction();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(WordMappingPhonemes.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL","UPDATE " +metaRecorderSentence.getTable()+ " SET isDeleted="+isDeleted+" WHERE wordID='"+idWord+"'");
        try {
            tx.begin();
            q.execute();
            tx.commit();
            check=true;
        } catch (Exception e) {
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
        return check;
    }






}
