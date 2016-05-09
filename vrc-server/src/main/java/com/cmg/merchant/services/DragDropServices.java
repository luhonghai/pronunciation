package com.cmg.merchant.services;

import com.cmg.lesson.dao.lessons.LessonMappingQuestionDAO;
import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.lessons.LessonMappingQuestion;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.merchant.dao.lessons.LMODAO;
import com.cmg.merchant.dao.level.LvDAO;
import com.cmg.merchant.dao.objective.ODAO;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lantb on 2016-03-28.
 */
public class DragDropServices {

    private static final Logger logger = Logger.getLogger(DragDropServices.class.getName());
    /**
     *
     * @param idCourse
     * @param idLevelUpdate
     * @param index
     */
    public void dragDropLevel(String idCourse, String idLevelUpdate, int index, String move){
        LvDAO dao = new LvDAO();
        try {
            dao.updateIndex(idCourse, idLevelUpdate, index);
            ArrayList<Level> list = (ArrayList<Level>) dao.listIn(idCourse);
            if(list!=null && list.size()>0){
                for(Level lv : list){
                    if(!lv.getId().equalsIgnoreCase(idLevelUpdate)) {
                        if (move.equalsIgnoreCase("up") && lv.getIndex() >= index) {
                            dao.updateIndex(idCourse, lv.getId(), lv.getIndex() + 1);
                        }else if(move.equalsIgnoreCase("down") && lv.getIndex() <= index){
                            dao.updateIndex(idCourse, lv.getId(), lv.getIndex() - 1);
                        }
                    }
                }
            }
        }catch(Exception e){
            logger.error(e);
        }
    }

    /**
     *
     * @param idLevel
     * @param idObjUpdate
     * @param index
     */
    public void dragDropObj(String idLevel, String idObjUpdate, int index,String move){
        ODAO dao = new ODAO();
        try {
            dao.updateIndex(idLevel,idObjUpdate,index);
            ArrayList<Objective> list = (ArrayList<Objective>) dao.getAllByIdLevel(idLevel);
            if(list!=null && list.size() > 0){
                for(Objective obj : list){
                    if(!obj.getId().trim().equalsIgnoreCase(idObjUpdate.trim())){
                        if (move.equalsIgnoreCase("up") && obj.getIndex() >= index) {
                            dao.updateIndex(idLevel, obj.getId(), obj.getIndex() + 1);
                        }else if(move.equalsIgnoreCase("down") && obj.getIndex() <= index){
                            dao.updateIndex(idLevel, obj.getId(), obj.getIndex() - 1);
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.error(e);
        }
    }

    /**
     *
     * @param idObj
     * @param idLessonUpdate
     * @param index
     */
    public void dragDropLesson(String idObj, String idLessonUpdate, int index, String move){
        LMODAO dao = new LMODAO();
        try {
            dao.updateIndex(idObj,idLessonUpdate,index);
            ArrayList<LessonCollection> list = dao.getLessonMappingObjective(idObj);
            if(list!=null && list.size()>0){
                for(LessonCollection lc : list){
                    if(!lc.getId().trim().equalsIgnoreCase(idLessonUpdate.trim())){
                        if (move.equalsIgnoreCase("up") && lc.getIndex() >= index) {
                            dao.updateIndex(idObj, lc.getId(), lc.getIndex() + 1);
                        }else if(move.equalsIgnoreCase("down") && lc.getIndex() <= index){
                            dao.updateIndex(idObj, lc.getId(), lc.getIndex() - 1);
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.error(e);
        }
    }

    /**
     *
     * @param idLesson
     * @param idQuestionUpdate
     * @param index
     */
    public void dragDropQuestion(String idLesson, String idQuestionUpdate, int index, String move){
        LessonMappingQuestionDAO dao = new LessonMappingQuestionDAO();
        try {
            dao.updateIndex(idLesson,idQuestionUpdate,index);
            List<LessonMappingQuestion> list = dao.getAllByIDLesson(idLesson);
            if(list!=null && list.size()>0){
                for(LessonMappingQuestion lc : list){
                    if(!lc.getIdQuestion().trim().equalsIgnoreCase(idQuestionUpdate.trim())){
                        if (move.trim().equalsIgnoreCase("up") && lc.getIndex() >= index) {
                            dao.updateIndex(idLesson, lc.getIdQuestion(), lc.getIndex() + 1);
                        }else if(move.trim().equalsIgnoreCase("down") && lc.getIndex() <= index){
                            dao.updateIndex(idLesson, lc.getIdQuestion(), lc.getIndex() - 1);
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.error(e);
        }
    }
}
