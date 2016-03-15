package com.cmg.merchant.dao.questions;

import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.question.Question;
import com.cmg.merchant.common.SQL;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lantb on 2016-03-01.
 */
public class QMLDAO extends DataAccess<Question> {
    public QMLDAO() {
        super(Question.class);
    }

    /**
     *  use for get latest version in table
     * @return latest version
     * @throws Exception
     */
    public ArrayList<Question> getLessonMappingObjective(String idLesson){
        PersistenceManager pm = PersistenceManagerHelper.get();
        SQL sqlUtil = new SQL();
        String sql = sqlUtil.getSqlQuestionMappingLesson(idLesson);
        System.out.println("sql question mapping lesson  : " +  sql);
        ArrayList<Question> list = new ArrayList<Question>();
        Query q = pm.newQuery("javax.jdo.query.SQL", sql);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size() > 0){
                for(Object obj : tmp){
                    Question dto = new Question();
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
}
