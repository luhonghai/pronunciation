package com.cmg.merchant.dao.word;

import com.cmg.lesson.data.jdo.question.WeightForPhoneme;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.List;

/**
 * Created by lantb on 2016-03-21.
 */
public class WFPDAO  extends DataAccess<WeightForPhoneme> {

    public WFPDAO() {
        super(WeightForPhoneme.class);
    }

    /**
     * @return max version
     * @throws Exception
     */
    public int getLatestVersion() throws Exception {
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
            if (q != null)
                q.closeAll();
            pm.close();
        }
        return version;
    }

    /**
     * @param idQuestion
     * @param idWord
     * @return list weight for phoneme
     * @throws Exception
     */
    public List<WeightForPhoneme> listBy(String idQuestion, String idWord) throws Exception {
        List<WeightForPhoneme> list = list("WHERE idQuestion == :1 && idWordCollection == :2 && isDeleted == :3",
                idQuestion, idWord, false, "index asc");
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }
}