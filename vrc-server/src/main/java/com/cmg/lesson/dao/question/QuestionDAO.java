package com.cmg.lesson.dao.question;

import com.cmg.lesson.data.jdo.question.Question;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.*;

/**
 * Created by lantb on 2015-10-07.
 */
public class QuestionDAO extends DataAccess<Question> {
    public QuestionDAO() {
        super(Question.class);
    }

    /**
     *  use for get latest version in table
     * @return latest version
     * @throws Exception
     */
    public int getLatestVersion() throws Exception{
        int version = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT max(version) FROM " + Question.class.getCanonicalName());
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
     * @param id
     * @param name
     * @return true is update
     * @throws Exception
     */
    public boolean updateQuestion(String id, String name, String description) throws Exception{
        boolean isUpdate=false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Question.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET name=?, description=? WHERE id=?");
        try {
            q.execute(name,description,id);
            isUpdate=true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
        return isUpdate;
    }

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    public boolean deteleQuestion(String id) throws Exception{
        boolean isDelete=false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Question.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted= ? WHERE id=?");
        try {
            q.execute(true,id);
            isDelete=true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
        return isDelete;
    }

    /**
     *
     * @param name
     * @return true is exist word name.
     * @throws Exception
     */
    public boolean checkExist(String name) throws Exception{
        boolean isExist = false;
        List<Question> list = list("WHERE name == :1 && isDeleted == :2 ", name, false);
        if(list!=null && list.size() > 0){
            isExist = true;
        }
        return isExist;
    }

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    public Question getById(String id) throws Exception{
        boolean isExist = false;
        List<Question> list = list("WHERE id == :1 && isDeleted == :2 ", id, false);
        if(list!=null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    /**
     *
     * @return total rows in table
     */
    public double getCount() throws  Exception{
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + Question.class.getCanonicalName());
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
     * @param createDateFrom
     * @param createDateTo
     * @return
     * @throws Exception
     */
    public double getCountSearch(String search,Date createDateFrom,Date createDateTo, int length, int start) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + Question.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a=" (name.toLowerCase().indexOf(search.toLowerCase()) != -1)";
        String b=" (name == null || name.toLowerCase().indexOf(search.toLowerCase()) != -1)";
        if(createDateFrom!=null&&createDateTo==null){
            string.append("(timeCreated >= createDateFrom) &&");
        }
        if(createDateFrom==null&&createDateTo!=null){
            string.append("(timeCreated <= createDateTo) &&");
        }
        if(createDateFrom!=null&&createDateTo!=null){
            string.append("(timeCreated >= createDateFrom && timeCreated <= createDateTo) &&");
        }
        string.append("(isDeleted==false) &&");

        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search, java.util.Date createDateFrom,java.util.Date createDateTo");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("createDateFrom", createDateFrom);
        params.put("createDateTo", createDateTo);
        try {
            count = (Long) q.executeWithMap(params);
            return count.doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }

    /**
     *
     * @param start
     * @param length
     * @param search
     * @param column
     * @param order
     * @param createDateFrom
     * @param createDateTo
     * @return
     * @throws Exception
     */
    public List<Question> listAll(int start, int length,String search,int column,String order,Date createDateFrom,Date createDateTo) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + Question.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="(name.toLowerCase().indexOf(search.toLowerCase()) != -1)";
        String b="(name == null || name.toLowerCase().indexOf(search.toLowerCase()) != -1)";

        if(createDateFrom!=null&&createDateTo==null){
            string.append("(timeCreated >= createDateFrom) &&");
        }
        if(createDateFrom==null&&createDateTo!=null){
            string.append("(timeCreated <= createDateTo) &&");
        }
        if(createDateFrom!=null&&createDateTo!=null){
            string.append("(timeCreated >= createDateFrom && timeCreated <= createDateTo) &&");
        }
        string.append("(isDeleted==false) &&");

        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search,java.util.Date createDateFrom,java.util.Date createDateTo");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("createDateFrom", createDateFrom);
        params.put("createDateTo", createDateTo);
        if (column==0 && order.equals("asc")) {
            q.setOrdering("name asc");
        }else if(column==0 && order.equals("desc")) {
            q.setOrdering("name desc");
        }
        if (column==2 && order.equals("asc")) {
            q.setOrdering("timeCreated asc");
        }else if(column==2 && order.equals("desc")) {
            q.setOrdering("timeCreated desc");
        }

        q.setRange(start, start + length);

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
     * @param id
     * @return true if update deleted success
     */
    public boolean updateDeleted(String id){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Question.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted= ? WHERE id=?");
        try {
            q.execute(true,id);
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
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Question.class.getCanonicalName());
        clause.append(" Where "+metaRecorderSentence.getTable()+".ID in(");
        for(String id : ids){
            clause.append("'"+id+"',");
        }
        List<Question> listWord = new ArrayList<Question>();
        String whereClause = clause.toString().substring(0, clause.toString().length() - 1);
        if(wordSearch!=null && wordSearch.trim().length() > 0){
            whereClause = whereClause + ") and name like '%"+wordSearch.toLowerCase()+"%' and isDeleted=false ";
        }else{
            whereClause = whereClause + ") and isDeleted=false " ;
        }
        if(order!=null && order.length() >0 ){
            whereClause = whereClause + "order by name " + order;
        }else{
            whereClause = whereClause + "order by name asc" ;
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
    public List<Question> listIn(List<String> ids, String wordSearch,String order, int start, int length) throws Exception{
    //public List<Question> listIn(List<String> ids, String wordSearch) throws Exception{
        wordSearch = "%" + wordSearch + "%";
        StringBuffer clause = new StringBuffer();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Question.class.getCanonicalName());
        clause.append(" Where "+metaRecorderSentence.getTable()+".ID in(");
        for(String id : ids){
            clause.append("'"+id+"',");
        }
        List<Question> listQuestions = new ArrayList<Question>();
        String whereClause = clause.toString().substring(0, clause.toString().length() - 1);
        if(wordSearch!=null && wordSearch.trim().length() > 0 && wordSearch!=""){
            whereClause = whereClause + ") and UPPER(QUESTION.NAME) like UPPER(?) and isDeleted=false ";
        }else{
            whereClause = whereClause + ") and isDeleted=false " ;
        }

        if(order!=null && order.length() >0 ){
            whereClause = whereClause + "order by name " + order;
        }else{
            whereClause = whereClause + "order by name asc" ;
        }
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("javax.jdo.query.SQL", "Select id,name,version from " + metaRecorderSentence.getTable() + whereClause);
        q.setRange(start, start + length);
        try {
            List<Object> tmp = (List<Object>) q.execute(wordSearch);
            if(tmp!=null && tmp.size() > 0){
                for(Object obj : tmp){
                    Question question = new Question();
                    Object[] array = (Object[]) obj;
                    question.setId(array[0].toString());
                    if(array[1]!=null){
                        question.setName(array[1].toString());
                    }
                    if(array[2]!=null){
                        question.setVersion(Integer.parseInt(array[2].toString()));
                    }
                    listQuestions.add(question);
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
        return listQuestions;

    }


    /**
     *
     * @param questionName
     * @return
     */
    public List<Question> searchName(List<String> ids, String questionName) throws Exception{
        questionName = "%" + questionName +"%";
        //public List<Question> listIn(List<String> ids, String wordSearch) throws Exception{
        StringBuffer clause = new StringBuffer();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Question.class.getCanonicalName());
        List<Question> listQuestions = new ArrayList<Question>();
        String whereClause;
        if (ids == null || ids.isEmpty()){
            clause.append(" Where ");
            whereClause = clause.toString();//.substring(0, clause.toString().length() - 1);
            if(questionName!=null && questionName.trim().length() > 0 && questionName!=""){
                whereClause = whereClause + " UPPER(QUESTION.NAME) like UPPER(?) and isDeleted=false order by name asc";
            }else{
                whereClause = whereClause + " isDeleted=false order by name asc" ;
            }
        }
        else {
            clause.append(" Where "+metaRecorderSentence.getTable()+".ID NOT IN(");
            for(String id : ids){
                clause.append("'"+id+"',");
            }
            whereClause = clause.toString().substring(0, clause.toString().length() - 1);
            if(questionName!=null && questionName.trim().length() > 0 && questionName!=""){
                whereClause = whereClause + ") and UPPER(QUESTION.NAME) like UPPER(?) and isDeleted=false order by name asc";
            }else{
                whereClause = whereClause + ") and isDeleted=false order by name asc" ;
            }
        }

        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("javax.jdo.query.SQL", "Select id,name,version from " + metaRecorderSentence.getTable() + whereClause);
        q.setRange(0,10);
        try {
            List<Object> tmp = (List<Object>) q.execute(questionName);
            if(tmp!=null && tmp.size() > 0){
                for(Object obj : tmp){
                    Question question = new Question();
                    Object[] array = (Object[]) obj;
                    question.setId(array[0].toString());
                    if(array[1]!=null){
                        question.setName(array[1].toString());
                    }
                    if(array[2]!=null){
                        question.setVersion(Integer.parseInt(array[2].toString()));
                    }
                    listQuestions.add(question);
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

        return listQuestions;

    }

}
