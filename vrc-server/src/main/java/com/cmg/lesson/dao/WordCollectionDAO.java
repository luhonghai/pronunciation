package com.cmg.lesson.dao;

import com.cmg.lesson.data.jdo.WordCollection;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.Admin;
import com.cmg.vrc.data.jdo.PhonemeScoreDB;
import com.cmg.vrc.data.jdo.Transcription;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lantb on 2015-10-01.
 */
public class WordCollectionDAO extends DataAccess<WordCollection> {

    public WordCollectionDAO() {
        super(WordCollection.class);
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
     * @param word
     * @return word filter by column word
     * @throws Exception
     */
    public WordCollection getByWord(String word) throws Exception {
        List<WordCollection> list = list("WHERE word == :1", word);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }

    /**
     *
     * @param id
     * @return word filter by id
     * @throws Exception
     */
    public WordCollection getByID(String id) throws Exception{
        List<WordCollection> list = list("WHERE id == :1", id);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }

    /**
     *
     * @param word
     * @return return true if word exist in database
     * @throws Exception
     */
    public boolean checkWordExist(String word) throws Exception {
        List<WordCollection> list = list("WHERE word == :1", word);
        if (list != null && list.size() > 0)
            return true;
        return false;
    }

    /**
     *
     * @param isDeleted
     * @return list contains words filter by column isDeleted
     * @throws Exception
     */
    public List<WordCollection> listAll(boolean isDeleted) throws Exception{
        List<WordCollection> list =  list("WHERE isDeleted == :1", isDeleted);
        if (list != null && list.size() > 0){
            return list;
        }
        return null;
    }


}
