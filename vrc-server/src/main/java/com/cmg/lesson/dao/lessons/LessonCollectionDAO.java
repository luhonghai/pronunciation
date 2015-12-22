package com.cmg.lesson.dao.lessons;

import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.question.Question;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.*;

/**
 * Created by lantb on 2015-10-13.
 */
public class LessonCollectionDAO extends DataAccess<LessonCollection> {

    public LessonCollectionDAO() {
        super(LessonCollection.class);
    }


    /**
     *  use for get latest version in table
     * @return latest version
     * @throws Exception
     */
    public int getLatestVersion() throws Exception{
        int version = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT max(version) FROM " + LessonCollection.class.getCanonicalName());
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
    public boolean updateLesson(String id, String name,String title,String shortDescription, String description) throws Exception{
        boolean isUpdate=false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(LessonCollection.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET nameUnique=? ,title=?, name=?, description='"+description+"' WHERE id='"+id+"'");
        try {
            q.execute(name,title,shortDescription);
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
     * @return true is update
     * @throws Exception
     */
    public boolean updateDescription(String id,String title, String shortDescription,String description) throws Exception{
        boolean isUpdate=false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(LessonCollection.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET title=?, name=?, description=?  WHERE id='"+id+"'");
        try {
            q.execute(title,shortDescription,description);
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
     * @param name
     * @return true is exist word name.
     * @throws Exception
     */
    public boolean checkExist(String name) throws Exception{
        boolean isExist = false;
        List<LessonCollection> list = list("WHERE nameUnique == :1 && isDeleted == :2 ", name, false);
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
    public LessonCollection getById(String id) throws Exception{
        List<LessonCollection> list = list("WHERE id == :1 && isDeleted == :2 ", id, false);
        if(list!=null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public List<LessonCollection> getAll() throws Exception{
        List<LessonCollection> list = list("WHERE isDeleted == :1 ", false);
        if(list!=null && list.size() > 0){
            return list;
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
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + LessonCollection.class.getCanonicalName());
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
    public double getCountSearch(String search,String lesson,Date createDateFrom,Date createDateTo, int length, int start) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + LessonCollection.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="(nameUnique.toLowerCase().indexOf(search.toLowerCase()) != -1)";
        String b="(nameUnique == null || nameUnique.toLowerCase().indexOf(search.toLowerCase()) != -1)";
        if(lesson.length()>0){
            string.append("(title.toLowerCase().indexOf(lesson.toLowerCase()) != -1) &&");
        }
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
//        q.setRange(start, start +length);
        q.setFilter(string.toString());
        q.declareParameters("String search,String lesson, java.util.Date createDateFrom,java.util.Date createDateTo");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("lesson", lesson);
        params.put("createDateFrom", createDateFrom);
        params.put("createDateTo", createDateTo);
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
    public List<LessonCollection> listAll(int start, int length,String search,int column,String order, String lesson, Date createDateFrom,Date createDateTo) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + LessonCollection.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="(nameUnique.toLowerCase().indexOf(search.toLowerCase()) != -1)";
        String b="(nameUnique == null || nameUnique.toLowerCase().indexOf(search.toLowerCase()) != -1)";
        if(lesson.length()>0){
            string.append("(title.toLowerCase().indexOf(lesson.toLowerCase()) != -1) &&");
        }

        if(createDateFrom!=null&&createDateTo==null){
            string.append("(dateCreated >= createDateFrom) &&");
        }
        if(createDateFrom==null&&createDateTo!=null){
            string.append("(dateCreated <= createDateTo) &&");
        }
        if(createDateFrom!=null&&createDateTo!=null){
            string.append("(dateCreated >= createDateFrom && dateCreated <= createDateTo) &&");
        }
        string.append("(isDeleted==false) &&");

        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search,String lesson, java.util.Date createDateFrom,java.util.Date createDateTo");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("lesson", lesson);
        params.put("createDateFrom", createDateFrom);
        params.put("createDateTo", createDateTo);
        if (column==0 && order.equals("asc")) {
            q.setOrdering("nameUnique asc");
        }else if(column==0 && order.equals("desc")) {
            q.setOrdering("nameUnique desc");
        }
        if (column==1 && order.equals("asc")) {
            q.setOrdering("title asc");
        }else if(column==1 && order.equals("desc")) {
            q.setOrdering("title desc");
        }
        if (column==4 && order.equals("asc")) {
            q.setOrdering("dateCreated asc");
        }else if(column==4 && order.equals("desc")) {
            q.setOrdering("dateCreated desc");
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
     * @param ids
     * @return
     */
    public List<LessonCollection> listIn(List<String> ids) throws Exception{
        StringBuffer clause = new StringBuffer();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(LessonCollection.class.getCanonicalName());
        clause.append(" Where LESSONCOLLECTION.ID in(");
        for(String id : ids){
            clause.append("'"+id+"',");
        }
        List<LessonCollection> listObjective = new ArrayList<LessonCollection>();
        String whereClause = clause.toString().substring(0, clause.toString().length() - 1);
        whereClause = whereClause + ") and isDeleted=false" ;

        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("javax.jdo.query.SQL", "Select id,name,description,nameUnique,title from " + metaRecorderSentence.getTable() + whereClause);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size() > 0){
                for(Object obj : tmp){
                    LessonCollection lessonCollection = new LessonCollection();
                    Object[] array = (Object[]) obj;
                    lessonCollection.setId(array[0].toString());
                    if(array[1]!=null){
                        lessonCollection.setName(array[1].toString());
                    }
                    if(array[2]!=null){
                        lessonCollection.setDescription(array[2].toString());
                    }
                    if(array[3]!=null){
                        lessonCollection.setNameUnique(array[3].toString());
                    }
                    if(array[4]!=null){
                        lessonCollection.setTitle(array[4].toString());
                    }
                    lessonCollection.setIdChecked(true);
                    listObjective.add(lessonCollection);
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
        return listObjective;

    }

    /**
     *
     * @param ids
     * @return
     */
    public List<LessonCollection> listNotIn(List<String> ids) throws Exception{
        StringBuffer clause = new StringBuffer();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(LessonCollection.class.getCanonicalName());
        clause.append(" Where LESSONCOLLECTION.ID NOT IN(");
        for(String id : ids){
            clause.append("'"+id+"',");
        }
        List<LessonCollection> listObjective = new ArrayList<LessonCollection>();
        String whereClause = clause.toString().substring(0, clause.toString().length() - 1);
        whereClause = whereClause + ") and isDeleted=false " ;

        PersistenceManager pm = PersistenceManagerHelper.get();

        Query q = pm.newQuery("javax.jdo.query.SQL", "Select id,name,description,nameUnique,title from " + metaRecorderSentence.getTable() + whereClause);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size() > 0){
                for(Object obj : tmp){
                    LessonCollection lessonCollection = new LessonCollection();
                    Object[] array = (Object[]) obj;
                    lessonCollection.setId(array[0].toString());
                    if(array[1]!=null){
                        lessonCollection.setName(array[1].toString());
                    }
                    if(array[2]!=null){
                        lessonCollection.setDescription(array[2].toString());
                    }
                    if(array[3]!=null){
                        lessonCollection.setNameUnique(array[3].toString());
                    }
                    if(array[4]!=null){
                        lessonCollection.setTitle(array[4].toString());
                    }
                    listObjective.add(lessonCollection);
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
        return listObjective;

    }

    /**
     *
     * @param id
     * @return true if update deleted success
     */
    public boolean deletedLesson(String id){
        boolean isDelete = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(LessonCollection.class.getCanonicalName());
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
}
