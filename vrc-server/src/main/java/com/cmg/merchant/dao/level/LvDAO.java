package com.cmg.merchant.dao.level;

import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lantb on 2016-02-22.
 */
public class LvDAO {
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

}
