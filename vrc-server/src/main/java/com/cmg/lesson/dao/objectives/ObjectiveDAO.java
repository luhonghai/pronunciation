package com.cmg.lesson.dao.objectives;

import com.cmg.lesson.data.jdo.course.CourseMappingDetail;
import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.*;

/**
 * Created by CMG Dev156 on 10/22/2015.
 */
public class ObjectiveDAO extends DataAccess<Objective> {
    public ObjectiveDAO() {
        super(Objective.class);
    }

    /**
     *  use for get latest version in table
     * @return latest version
     * @throws Exception
     */
    public int getLatestVersion() throws Exception{
        int version = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT max(version) FROM " + Objective.class.getCanonicalName());
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
    public boolean updateObjective(String id, String name, String description) throws Exception{
        boolean isUpdate=false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Objective.class.getCanonicalName());
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
     * @return true is update
     * @throws Exception
     */
    public boolean updateDescription(String id, String description) throws Exception{
        boolean isUpdate=false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Objective.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET description=? WHERE id=?");
        try {
            q.execute(description,id);
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
        List<Objective> list = list("WHERE name == :1 && isDeleted == :2 ", name, false);
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
    public Objective getById(String id) throws Exception{
        List<Objective> list = list("WHERE id == :1 && isDeleted == :2 ", id, false);
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
    public List<Objective> getAll() throws Exception{
        List<Objective> list = list("WHERE isDeleted == :1 ", false);
        if(list!=null && list.size() > 0){
            return list;
        }
        return null;
    }



    /**
     *
     * @param id
     * @return true if update deleted success
     */
    public boolean deletedObjective(String id){
        boolean isDelete = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Objective.class.getCanonicalName());
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
     * @param ids
     * @return
     */
    public List<Objective> listIn(String ids) throws Exception{
//        if(ids!=null && ids.size() > 0){
            StringBuffer clause = new StringBuffer();
            TypeMetadata metaObjective = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Objective.class.getCanonicalName());
            TypeMetadata metaCourseMappingDetail = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(CourseMappingDetail.class.getCanonicalName());
            String firstQuery = "select obj.id, obj.name , obj.description, mapping.index from  " + metaObjective.getTable()
                    + " obj inner join " + metaCourseMappingDetail.getTable()
                    + " mapping on mapping.idChild=obj.id where ";
            clause.append(firstQuery);
            clause.append(" mapping.idLevel= '"+ids+"' and obj.isDeleted=false and mapping.isDeleted=false");
            clause.append(" ORDER BY mapping.index ASC");
            PersistenceManager pm = PersistenceManagerHelper.get();
            Query q = pm.newQuery("javax.jdo.query.SQL", clause.toString());
            List<Objective> listObjective = new ArrayList<Objective>();


//            clause.append(" Where ID in(");
//            for(String id : ids){
//                clause.append("'"+id+"',");
//            }
//            List<Objective> listObjective = new ArrayList<Objective>();
//            String whereClause = clause.toString().substring(0, clause.toString().length() - 1);
//            whereClause = whereClause + ") and isDeleted=false" ;
//            PersistenceManager pm = PersistenceManagerHelper.get();
//            Query q = pm.newQuery("javax.jdo.query.SQL", "Select id,name,description from " + metaObjective.getTable() + whereClause);
            try {
                List<Object> tmp = (List<Object>) q.execute();
                if(tmp!=null && tmp.size() > 0){
                    for(Object obj : tmp){
                        Objective objective = new Objective();
                        Object[] array = (Object[]) obj;
                        objective.setId(array[0].toString());
                        if(array[1]!=null){
                            objective.setName(array[1].toString());
                        }
                        if(array[2]!=null){
                            objective.setDescription(array[2].toString());
                        }
                        listObjective.add(objective);
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
       // }

//        return new ArrayList<Objective>();
    }

    /**
     *
     * @param ids
     * @return
     */
    public List<Objective> listNotIn(List<String> ids) throws Exception{
        if(ids!=null && ids.size() > 0){
            TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Objective.class.getCanonicalName());
            StringBuffer clause = new StringBuffer();
            clause.append(" Where ID NOT IN (");
            for(String id : ids){
                clause.append("'"+id+"',");
            }
            List<Objective> listObjective = new ArrayList<Objective>();
            String whereClause = clause.toString().substring(0, clause.toString().length() - 1);
            whereClause = whereClause + ") and isDeleted=false " ;
            PersistenceManager pm = PersistenceManagerHelper.get();
            Query q = pm.newQuery("javax.jdo.query.SQL", "Select id,name,description from " + metaRecorderSentence.getTable() + whereClause);
            try {
                List<Object> tmp = (List<Object>) q.execute();
                if(tmp!=null && tmp.size() > 0){
                    for(Object obj : tmp){
                        Objective objective = new Objective();
                        Object[] array = (Object[]) obj;
                        objective.setId(array[0].toString());
                        if(array[1]!=null){
                            objective.setName(array[1].toString());
                        }
                        if(array[2]!=null){
                            objective.setDescription(array[2].toString());
                        }
                        listObjective.add(objective);
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

        return new ArrayList<Objective>();
    }



    /**
     *
     * @return total rows in table
     */
    public double getCount() throws  Exception{
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + Objective.class.getCanonicalName());
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
    public double getCountSearch(String search,String description,Date createDateFrom,Date createDateTo, int length, int start) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + Objective.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="(name.toLowerCase().indexOf(search.toLowerCase()) != -1)";
        String b="(name == null || name.toLowerCase().indexOf(search.toLowerCase()) != -1)";
        if(description.length()>0){
            string.append("(description.toLowerCase().indexOf(description.toLowerCase()) != -1) &&");
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
//        q.setRange(start, start +length);
        q.setFilter(string.toString());
        q.declareParameters("String search,String description, java.util.Date createDateFrom,java.util.Date createDateTo");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("description", description);
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
    public List<Objective> listAll(int start, int length,String search,int column,String order,
                                          String description, Date createDateFrom,Date createDateTo) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + Objective.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="(name.toLowerCase().indexOf(search.toLowerCase()) != -1)";
        String b="(name == null || name.toLowerCase().indexOf(search.toLowerCase()) != -1)";
        if(description.length()>0){
            string.append("(description.toLowerCase().indexOf(lesson.toLowerCase()) != -1) &&");
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
        q.declareParameters("String search,String description, java.util.Date createDateFrom,java.util.Date createDateTo");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("description", description);
        params.put("createDateFrom", createDateFrom);
        params.put("createDateTo", createDateTo);
        if (column==0 && order.equals("asc")) {
            q.setOrdering("name asc");
        }else if(column==0 && order.equals("desc")) {
            q.setOrdering("name desc");
        }
        if (column==1 && order.equals("asc")) {
            q.setOrdering("description asc");
        }else if(column==1 && order.equals("desc")) {
            q.setOrdering("description desc");
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
}
