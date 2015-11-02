package com.cmg.lesson.dao.question;

import com.cmg.lesson.data.jdo.question.WordOfQuestion;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.List;

/**
 * Created by lantb on 2015-10-07.
 */
public class WordOfQuestionDAO extends DataAccess<WordOfQuestion> {

    public WordOfQuestionDAO(){
        super(WordOfQuestion.class);
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public int getLatestVersion() throws Exception{
        int version =0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT max(version) FROM " + WordOfQuestion.class.getCanonicalName());
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
     * @return true if update deleted success
     */
    public boolean deleteWordofQuestion(String idQuestion, String idWord){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(WordOfQuestion.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted= ? WHERE idQuestion=? and idWordCollection=?");
        try {
            q.execute(true,idQuestion,idWord);
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
     * @return true if update deleted success
     */
    public boolean deleteQuestion(String idQuestion){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(WordOfQuestion.class.getCanonicalName());
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
     * @param idQuestion
     * @return true if update deleted success
     */
    public boolean deleteMappingWord(String idWord){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(WordOfQuestion.class.getCanonicalName());
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
     * @return
     */
    public List<WordOfQuestion> listByIdQuestion(String idQuestion) throws Exception{
        List<WordOfQuestion> list = list("WHERE idQuestion==:1 && isDeleted==:2", idQuestion,false);
        if(list!=null && list.size() > 0){
            return list;
        }
        return null;
    }

    /**
     *
     * @param idQuestion
     * @param idWord
     * @return
     */
    public boolean checkExistedWord(String idQuestion, String idWord) throws Exception{
        List<WordOfQuestion> list = list("WHERE idQuestion==:1 && idWordCollection== :2 && isDeleted==:3", idQuestion,idWord,false);
        if(list!=null && list.size() > 0){
            return true;
        }
        return false;

    }

}
