package com.cmg.lesson.dao;

import com.cmg.lesson.data.jdo.WordCollection;
import com.cmg.lesson.data.jdo.WordMappingPhonemes;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.ArrayList;
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
        Query q = pm.newQuery("Update WordMappingPhonemes SET isDeleted="+isDeleted +" where idWord='"+idWord+"'");
        try {
            q.execute();
            check = true;
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
