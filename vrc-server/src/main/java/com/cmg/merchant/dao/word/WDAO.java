package com.cmg.merchant.dao.word;

import com.cmg.lesson.data.jdo.question.Question;
import com.cmg.lesson.data.jdo.question.WordOfQuestion;
import com.cmg.lesson.data.jdo.word.WordCollection;
import com.cmg.merchant.common.SQL;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;
import org.apache.commons.lang.StringUtils;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lantb on 2016-03-01.
 */
public class WDAO extends DataAccess<WordCollection> {
    public WDAO() {
        super(WordCollection.class);
    }

    /**
     *  use for get latest version in table
     * @return latest version
     * @throws Exception
     */
    public List<String> getWordByIdQuestion(String idQuestion){
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaWordCollection = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(WordCollection.class.getCanonicalName());
        TypeMetadata metaWordOfQuestion = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(WordOfQuestion.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "SELECT word.word FROM " + metaWordCollection.getTable() + " word inner join " + metaWordOfQuestion.getTable() + " mapping on word.id=mapping.idWordCollection WHERE mapping.idQuestion='" + idQuestion + "'");
        try {
            List<String> list=(List<String>) q.execute();
            return list;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }

}
