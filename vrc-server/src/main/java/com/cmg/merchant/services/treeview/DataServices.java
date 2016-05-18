package com.cmg.merchant.services.treeview;

import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.lesson.data.jdo.course.CourseMappingDetail;
import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.lesson.data.jdo.question.Question;
import com.cmg.lesson.data.jdo.test.Test;
import com.cmg.merchant.dao.course.CDAO;
import com.cmg.merchant.dao.level.LVMTDAO;
import com.cmg.merchant.dao.level.LvDAO;
import com.cmg.merchant.dao.mapping.CMTDAO;
import com.cmg.merchant.dao.objective.ODAO;
import com.cmg.merchant.dao.test.TDAO;
import com.cmg.merchant.data.dto.TreeNode;
import com.cmg.merchant.data.jdo.CourseMappingTeacher;
import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * Created by lantb on 2016-02-22.
 */
public class DataServices {
    private static final Logger logger = Logger.getLogger(DataServices.class.getName());

    /**
     *
     * @param idCourse
     * @return get the course
     */
    public Course getRootNode(String idCourse){
        CDAO dao = new CDAO();
        CMTDAO cmtdao = new CMTDAO();
        try {
            Course c = dao.getById(idCourse);
            CourseMappingTeacher cmt = cmtdao.getByIdCourse(idCourse);
            if(cmt!=null)  c.setShare(cmt.getSr());
            return c;
        }catch (Exception e){
            logger.error(e);
        }
        return null;
    }

    /**
     *
     * @param idCourse
     * @return
     */
    public ArrayList<Level> getLevelDB(String idCourse) {
        LvDAO dao = new LvDAO();
        try {
            ArrayList<Level> list = (ArrayList<Level>) dao.listIn(idCourse);
            return list;
        }catch (Exception e){
            logger.error(e);
        }
        return null;
    }

    /**
     *
     * @param idLevel
     * @return
     */
    public ArrayList<Objective> getObjDB(String idLevel){
        ODAO dao = new ODAO();
        try {
            ArrayList<Objective> list = (ArrayList<Objective>) dao.getAllByIdLevel(idLevel);
            return list;
        }catch (Exception e){
            logger.error(e);
        }
        return null;
    }

    /**
     *
     * @param idLevel
     * @return
     */
    public Test getTestDB(String idLevel){
        TDAO dao = new TDAO();
        LVMTDAO mDao = new LVMTDAO();
        try {
            CourseMappingDetail cmd = mDao.getBy(idLevel,true);
            if(cmd!=null){
                String idTest = cmd.getIdChild();
                Test test = dao.getById(idTest);
                if(test!=null){
                    return test;
                }
            }
        }catch (Exception e){
            logger.error(e);
        }
        return null;
    }

    public ArrayList<LessonCollection> getLesson(String idParent){

        return null;
    }

    public ArrayList<Question> getQuestion(String idLesson){
        return null;
    }


}
