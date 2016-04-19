package com.cmg.merchant.dao.lessons;

import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.objectives.ObjectiveMapping;
import com.cmg.merchant.common.SQL;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lantb on 2016-03-01.
 */
public class LMODAO extends DataAccess<LessonCollection> {
    public LMODAO() {
        super(LessonCollection.class);
    }

    /**
     *  use for get latest version in table
     * @return latest version
     * @throws Exception
     */
    public ArrayList<LessonCollection> getLessonMappingObjective(String idObjective){
        PersistenceManager pm = PersistenceManagerHelper.get();
        SQL sqlUtil = new SQL();
        String sql = sqlUtil.getSqlLessonMappingObjective(idObjective);
        System.out.println("sql lesson mapping obj  : " +  sql);
        ArrayList<LessonCollection> list = new ArrayList<LessonCollection>();
        Query q = pm.newQuery("javax.jdo.query.SQL", sql);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size() > 0){
                for(Object obj : tmp){
                    LessonCollection dto = new LessonCollection();
                    Object[] array = (Object[]) obj;
                    if(array[0]!=null){
                        dto.setId(array[0].toString());
                    }
                    if(array[1]!=null){
                        dto.setName(array[1].toString());
                    }
                    if(array[2] != null) {
                        dto.setDescription(array[2].toString());
                    }
                    if(array[3] != null) {
                        dto.setNameUnique(array[3].toString());
                    }
                    if(array[4] != null) {
                        dto.setType(array[4].toString());
                    }
                    if(array[5] != null) {
                        dto.setIndex(Integer.parseInt(array[5].toString()));
                    }
                    list.add(dto);
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
        return list;
    }

    /**
     *
     * @param idObj
     * @param idLesson
     * @param index
     * @return
     * @throws Exception
     */
    public boolean updateIndex(String idObj, String idLesson, int index) throws Exception{
        boolean isUpdate=false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(ObjectiveMapping.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET OBJECTIVEMAPPING.index=?  WHERE idObjective=? && idLessonCollection=?");
        try {
            q.execute(index,idObj,idLesson);
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
}
