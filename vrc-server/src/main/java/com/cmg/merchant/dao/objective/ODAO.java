package com.cmg.merchant.dao.objective;

import com.cmg.lesson.data.jdo.course.CourseMappingDetail;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.lesson.data.jdo.question.Question;
import com.cmg.merchant.common.SqlUtil;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lantb on 2016-02-24.
 */
public class ODAO extends DataAccess<Objective> {

    public ODAO() {
        super(Objective.class);
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
     * @param idLevel
     * @return
     */
    public List<Objective> getAllByIdLevel(String idLevel) throws Exception{
        StringBuffer clause = new StringBuffer();
        TypeMetadata metaObjective = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Objective.class.getCanonicalName());
        TypeMetadata metaCourseMappingDetail = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(CourseMappingDetail.class.getCanonicalName());
        String firstQuery = "select obj.id, obj.name , obj.description, mapping.index,obj.isCopied from  " + metaObjective.getTable()
                + " obj inner join " + metaCourseMappingDetail.getTable()
                + " mapping on mapping.idChild=obj.id where ";
        clause.append(firstQuery);
        clause.append(" mapping.idLevel= '"+idLevel+"' and obj.isDeleted=false and mapping.isDeleted=false");
        clause.append(" ORDER BY mapping.index ASC");
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("javax.jdo.query.SQL", clause.toString());
        List<Objective> listObjective = new ArrayList<Objective>();

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
                    if(array[3]!=null){
                        objective.setIndex(Integer.parseInt(array[3].toString()));
                    }
                    if(array[4]!=null){
                        objective.setIsCopied(Boolean.parseBoolean(array[4].toString()));
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


    /**
     *
     * @param idObj
     * @param idLevel
     * @return true is update
     * @throws Exception
     */
    public boolean updateIndex(String idLevel, String idObj, int index) throws Exception{
        boolean isUpdate=false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(CourseMappingDetail.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET CourseMappingDetail.index=?  WHERE idLevel=? && idChild=?");
        try {
            q.execute(index,idLevel,idObj);
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
     * @param name
     * @return true is update
     * @throws Exception
     */
    public boolean updateObjective(String id, String name, String description) throws Exception{
        boolean isUpdate=false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Objective.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET name=?, description=?,isCopied=false WHERE id=?");
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
     * @return true if update deleted success
     */
    public boolean deletedObjective(String id){
        boolean isDelete = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlUtil sqlUtil = new SqlUtil();
        String sql = sqlUtil.getSqlDeleteObj(id);
        System.out.println("sql delete objective : " + sql);
        Query q = pm.newQuery("javax.jdo.query.SQL", sql);
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
