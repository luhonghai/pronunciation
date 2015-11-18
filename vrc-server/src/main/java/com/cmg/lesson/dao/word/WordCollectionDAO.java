package com.cmg.lesson.dao.word;

import com.cmg.lesson.data.jdo.word.WordCollection;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.*;
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
        List<WordCollection> list = list("WHERE word == :1  && isDeleted==:2", word,false);
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
        List<WordCollection> list =  list(" WHERE isDeleted == :1", isDeleted);
        if (list != null && list.size() > 0){
            return list;
        }
        return null;
    }

    /**
     *
     * @return total rows in table
     */
    public double getCount(){
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + WordCollection.class.getCanonicalName());
        q.setFilter("isDeleted==false");
        try {
            count = (Long) q.execute();
            return count.doubleValue();
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }

    /**
     *
     * @param search
     * @param order
     * @param length
     * @param start
     * @return count rows in search
     */
    public double countSearch(String search, String order,int length, int start){
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        StringBuffer filter = new StringBuffer();
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + WordCollection.class.getCanonicalName());
        String a ="(word.toLowerCase().indexOf(search.toLowerCase()) != -1)";
        String b ="(word == null || word.toLowerCase().indexOf(search.toLowerCase()) != -1)";
        if(search.length() == 0){
            filter.append(b);
        }else if(search.length() > 0){
            filter.append(a);
        }
        filter.append(" && (isDeleted==false)");
        q.setOrdering("word " + order);
        q.setRange(start, start + length);
        q.setFilter(filter.toString());
        q.declareParameters("String search");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        try {
            count = (Long) q.executeWithMap(params);
            return count.doubleValue();
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }

    }

    /**
     *
     * @param search
     * @return
     */
    public List<WordCollection> search(String search, String order,int length, int start){
        PersistenceManager pm = PersistenceManagerHelper.get();
        StringBuffer filter = new StringBuffer();
        Query q = pm.newQuery("SELECT FROM " + WordCollection.class.getCanonicalName());
        String a ="(word.toLowerCase().indexOf(search.toLowerCase()) != -1)";
        String b ="(word == null || word.toLowerCase().indexOf(search.toLowerCase()) != -1)";
        if(search.length() == 0){
            filter.append(b);
        }else if(search.length() > 0){
            filter.append(a);
        }
        filter.append(" && (isDeleted==false)");
        q.setOrdering("word " + order);
        q.setRange(start, start + length);
        q.setFilter(filter.toString());
        q.declareParameters("String search");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        try {
            return detachCopyAllList(pm, q.executeWithMap(params));
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }

    }


    /**
     *
     * @param idWord
     * @param definition
     * @param mp3Path
     * @return true if update success
     */
    public boolean updateWordInformation(String idWord,String definition,String mp3Path, String arpabet){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(WordCollection.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() +
                " SET definition= ?, mp3Path=?, arpabet=? WHERE id='" + idWord + "'");
        try {
            q.execute(definition,mp3Path,arpabet);
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
     * @param
     * @param
     * @return true if update success
     */
    public boolean updateArpabet(String idWord,String arpabet){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(WordCollection.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET arpabet= ? WHERE id = ?");
        try {
            q.execute(arpabet,idWord);
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
     * @return use for deleted word
     */
    public boolean deleteWord(String idWord){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(WordCollection.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted= ? WHERE id='" + idWord + "'");
        try {
            q.execute(true);
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
     * @param ids
     * @param wordSearch
     * @param order
     * @param start
     * @param length
     * @return
     * @throws Exception
     */
    public int getCountListIn(List<String> ids, String wordSearch,String order, int start, int length) throws Exception{
        int count = 0;
        StringBuffer clause = new StringBuffer();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(WordCollection.class.getCanonicalName());
        clause.append(" Where ID in(");
        for(String id : ids){
            clause.append("'"+id+"',");
        }
        List<WordCollection> listWord = new ArrayList<WordCollection>();
        String whereClause = clause.toString().substring(0, clause.toString().length() - 1);
        if(wordSearch!=null && wordSearch.trim().length() > 0){
            whereClause = whereClause + ") and word like '%"+wordSearch.toLowerCase()+"%' and isDeleted=false ";
        }else{
            whereClause = whereClause + ") and isDeleted=false " ;
        }
        if(order!=null && order.length() >0 ){
            whereClause = whereClause + "order by word " + order;
        }else{
            whereClause = whereClause + "order by word asc" ;
        }
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("javax.jdo.query.SQL", "Select COUNT(id) from " + metaRecorderSentence.getTable() + whereClause);
        q.setRange(start, start + length);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size() > 0){
                count = Integer.parseInt(tmp.get(0).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
        return count;

    }

    /**
     *
     * @param ids
     * @return
     */
    public List<WordCollection> listIn(List<String> ids, String wordSearch,String order, int start, int length) throws Exception{
        StringBuffer clause = new StringBuffer();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(WordCollection.class.getCanonicalName());
        clause.append(" Where WORDCOLLECTION.ID in(");
        for(String id : ids){
            clause.append("'"+id+"',");
        }
        List<WordCollection> listWord = new ArrayList<WordCollection>();
        String whereClause = clause.toString().substring(0, clause.toString().length() - 1);
        if(wordSearch!=null && wordSearch.trim().length() > 0 && wordSearch!=""){
            whereClause = whereClause + ") and word like '%"+wordSearch.toLowerCase()+"%' and isDeleted=false ";
        }else{
            whereClause = whereClause + ") and isDeleted=false " ;
        }
        if(order!=null && order.length() >0 ){
            whereClause = whereClause + "order by word " + order;
        }else{
            whereClause = whereClause + "order by word asc" ;
        }
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("javax.jdo.query.SQL", "Select id,word,mp3Path,pronunciation from " + metaRecorderSentence.getTable() + whereClause);
        q.setRange(start, start + length);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size() > 0){
                for(Object obj : tmp){
                    WordCollection word = new WordCollection();
                    Object[] array = (Object[]) obj;
                    word.setId(array[0].toString());
                    if(array[1]!=null){
                        word.setWord(array[1].toString());
                    }
                    if(array[2] != null) {
                        word.setMp3Path(array[2].toString());
                    }
                    if(array[3]!=null){
                        word.setPronunciation(array[3].toString());
                    }
                    listWord.add(word);
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
        return listWord;

    }


}
