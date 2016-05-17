package com.cmg.vrc.data.dao.impl;

import com.cmg.lesson.data.jdo.country.CountryMappingCourse;
import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.PhonemeScoreDB;
import com.cmg.vrc.util.FileHelper;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     *
     * @param uvID
     * @return check user voice id is existed or not
     */
    public boolean userVoiceIDExisted(String uvID){
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + PhonemeScoreDB.class.getCanonicalName());
        q.setFilter("userVoiceId==paraID");
        q.declareParameters("String paraID");
        try {
            List<PhonemeScoreDB> list = detachCopyAllList(pm,q.execute(uvID));
            if(list!=null && list.size()>0){
                return true;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
        return false;
    }

    /**
     * Added by Hai to calculate avg score phoneme
     * @param username
     * @return
     */
    public Map<String, Integer> getAvgPhonemeScore(String username) {
        Map<String, Integer> map = new HashMap<>();
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("javax.jdo.query.SQL", FileHelper.readQuery("select_avg_phoneme_score.sql"));
        try {
            List<Object> tmp = (List<Object>) q.execute(username, username);
            if(tmp!=null && tmp.size() > 0){
                for(Object obj : tmp){
                    Object[] array = (Object[]) obj;
                    map.put(array[0].toString(), array[1] == null ? -1 : Math.round(Float.parseFloat(array[1].toString())));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
        return map;
    }
}