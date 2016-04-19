package com.cmg.merchant.dao.word;

import com.cmg.lesson.data.jdo.question.Question;
import com.cmg.lesson.data.jdo.question.WordOfQuestion;
import com.cmg.lesson.data.jdo.word.WordCollection;
import com.cmg.merchant.common.SQL;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;
import edu.cmu.sphinx.linguist.dictionary.Word;
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
        Query q = pm.newQuery("javax.jdo.query.SQL", "SELECT word.word FROM " + metaWordCollection.getTable() + " word inner join " + metaWordOfQuestion.getTable() + " mapping on word.id=mapping.idWordCollection WHERE word.isDeleted=false and mapping.isDeleted=false and  mapping.idQuestion='" + idQuestion + "'");
        List<String> temp = new ArrayList<>();
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size() > 0) {
                for (Object obj : tmp) {
                    Object array = (Object) obj;
                    if (array != null) {
                        temp.add(array.toString());
                    }
                }
            }

        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
        return temp;
    }


    /**
     *  use for get latest version in table
     * @return latest version
     * @throws Exception
     */
    public List<WordCollection> getWordsByIdQuestion(String idQuestion){
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaWordCollection = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(WordCollection.class.getCanonicalName());
        TypeMetadata metaWordOfQuestion = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(WordOfQuestion.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "SELECT word.id,word.word FROM " + metaWordCollection.getTable() + " word inner join " + metaWordOfQuestion.getTable() + " mapping on word.id=mapping.idWordCollection WHERE word.isDeleted=false and mapping.isDeleted=false and mapping.idQuestion='" + idQuestion + "'");
        List<WordCollection> temp = new ArrayList<>();
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size() > 0) {
                for (Object obj : tmp) {
                    WordCollection word = new WordCollection();
                    Object[] array = (Object[]) obj;
                    if (array[0] != null) {
                        word.setId(array[0].toString());
                    }
                    if(array[1]!=null){
                        word.setWord(array[1].toString());
                    }
                    temp.add(word);
                }
            }

        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
        return temp;
    }

}
