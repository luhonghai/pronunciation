package com.cmg.merchant.dao.level;

import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.merchant.common.SqlUtil;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lantb on 2016-02-22.
 */
public class LvDAO extends DataAccess<Level> {


    public LvDAO(){super(Level.class);}

    /**
     *  use for get latest version in table
     * @return latest version
     * @throws Exception
     */
    public int getLatestVersion() throws Exception{
        int version = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT max(version) FROM " + Level.class.getCanonicalName());
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
     * @param idCourse
     * @return list level
     */
    public List<Level> listIn(String idCourse) throws Exception{
        List<Level> listLv = new ArrayList<Level>();
        PersistenceManager pm = PersistenceManagerHelper.get();
        String join = " as lv inner join COURSEMAPPINGLEVEL as map on map.IDLEVEL = lv.id where map.IDCOURSE = ? and map.ISDELETED=false and lv.ISDELETED=false order by map.`INDEX`";
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Level.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "Select lv.id,lv.name,lv.description,lv.isDemo,lv.color from " + metaRecorderSentence.getTable() + join);
        try {
            List<Object> tmp = (List<Object>) q.execute(idCourse);
            if(tmp!=null && tmp.size() > 0){
                for(Object obj : tmp){
                    Level lv = new Level();
                    Object[] array = (Object[]) obj;
                    lv.setId(array[0].toString());
                    if(array[1]!=null){
                        lv.setName(array[1].toString());
                    }
                    if(array[2] != null) {
                        lv.setDescription(array[2].toString());
                    }
                    if(array[3]!=null){
                        lv.setIsDeleted(Boolean.parseBoolean(array[3].toString()));
                    }
                    if(array[4]!=null){
                        lv.setColor(array[4].toString());
                    }
                    listLv.add(lv);
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
        return listLv;
    }


    /**
     *
     * @param id
     * @param name
     * @return true is update
     * @throws Exception
     */
    public boolean updateLevel(String id, String name, String description) throws Exception{
        boolean isUpdate=false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Level.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET name=? , description=?  WHERE id=?");
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
    public boolean updateDeleted(String id){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Level.class.getCanonicalName());
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
     * @param idLevel
     * @return
     */
    public void deleteStep1(String idLevel){
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlUtil sqlUtil = new SqlUtil();
        String sql = sqlUtil.getSqlDeleteLevel1(idLevel);
        System.out.println("sql delete level 1:  " + sql);
        Query q = pm.newQuery("javax.jdo.query.SQL",sql);
        try {
            q.execute();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
    }

    /**
     *
     * @param idCourse
     * @return
     */
    public void deleteStep2(String idLevel){
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlUtil sqlUtil = new SqlUtil();
        String sql = sqlUtil.getSqlDeleteLevel2(idLevel);
        System.out.println("sql delete level 2 :  " + sql);
        Query q = pm.newQuery("javax.jdo.query.SQL",sql);
        try {
            q.execute();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
    }

    /**
     *
     * @param idLv
     * @return
     */
    public boolean checkQuestionTestInLevel(String idLv){
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlUtil sqlUtil = new SqlUtil();
        String sql = sqlUtil.getSqlCheckQuestionTestInLevel(idLv);
        System.out.println("sql search question of test in level " + sql);
        Query q = pm.newQuery("javax.jdo.query.SQL",sql);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size() > 0){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
        return false;
    }

    /**
     *
     * @param idLv
     * @return
     */
    public boolean checkQuestionObjInLevel(String idLv){
        PersistenceManager pm = PersistenceManagerHelper.get();
        SqlUtil sqlUtil = new SqlUtil();
        String sql = sqlUtil.getSqlCheckQuestionObjInLevel(idLv);
        System.out.println("sql search question of obj in level " + sql);
        Query q = pm.newQuery("javax.jdo.query.SQL",sql);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size() > 0){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
        return false;
    }
}
