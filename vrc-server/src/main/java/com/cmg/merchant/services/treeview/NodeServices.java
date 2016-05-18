package com.cmg.merchant.services.treeview;

import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.merchant.dao.lessons.LMODAO;
import com.cmg.merchant.dao.questions.QMLDAO;
import com.cmg.merchant.dao.test.TDAO;
import com.cmg.merchant.dao.test.TMLDAO;
import com.cmg.merchant.data.dto.TreeNode;
import com.cmg.merchant.services.CourseServices;
import com.cmg.merchant.util.TreeUtil;

import java.util.ArrayList;

/**
 * Created by lantb on 2016-02-22.
 */
public class NodeServices {
    TreeUtil util = new TreeUtil();
    CourseServices courseServices = new CourseServices();
    DataServices dataServices = new DataServices();
    LMODAO lmodao=new LMODAO();
    QMLDAO qmldao=new QMLDAO();
    TDAO tdao = new TDAO();
    public ArrayList<TreeNode> loadRoot(String idCourse, boolean firstLoad, boolean showBtnAction) {
        ArrayList<TreeNode> list = new ArrayList<>();
        boolean isPublishCourse = courseServices.isPublishCourse(idCourse);
        if(firstLoad){
            TreeNode root = util.switchCourseToRoot(dataServices.getRootNode(idCourse),showBtnAction);
            root.setBranch(util.switchLevelToNode(dataServices.getLevelDB(idCourse),showBtnAction,idCourse,isPublishCourse));
            list.add(root);
        }else{
            list = util.switchLevelToNode(dataServices.getLevelDB(idCourse),showBtnAction,idCourse,isPublishCourse);
        }
        return list;
    }

    /**
     *
     * @param idLevel
     * @param showBtnAction
     * @return all child in specific level
     */
    public ArrayList<TreeNode> loadLevel(String idLevel, boolean showBtnAction, String idCourse){
        ArrayList<TreeNode> list = new ArrayList<>();
        boolean isPublishCourse = courseServices.isPublishCourse(idCourse);
        //load all obj
        ArrayList<TreeNode> listObj = util.switchObjToNode(dataServices.getObjDB(idLevel), showBtnAction,idLevel,isPublishCourse);
        list = listObj;
        TreeNode test = util.switchTestToNode(dataServices.getTestDB(idLevel), showBtnAction, idLevel, isPublishCourse);
        if(test!=null){
            list.add(test);
        }
        return list;
    }

    public ArrayList<TreeNode> loadObjective (String idObj, boolean showBtnAction, String idCourse){
        ArrayList<TreeNode> list = new ArrayList<>();
        boolean isPublishCourse = courseServices.isPublishCourse(idCourse);
        ArrayList<TreeNode> listObj = util.switchLessonToNode(lmodao.getLessonMappingObjective(idObj), showBtnAction, idObj, isPublishCourse);
        list = listObj;
        return list;
    }


    public ArrayList<TreeNode> loadLesson (String idLesson, boolean showBtnAction, String idCourse){
        ArrayList<TreeNode> list = new ArrayList<>();
        boolean isPublishCourse = courseServices.isPublishCourse(idCourse);
        ArrayList<TreeNode> listObj = util.switchQuestionToNode(qmldao.getQuestionByIdLesson(idLesson), showBtnAction, idLesson, isPublishCourse);
        list = listObj;
        return list;
    }


    public ArrayList<TreeNode> loadTest(String idTest, boolean showBtnAction, String idCourse){
        ArrayList<TreeNode> list = new ArrayList<>();
        String idLesson = getIdLessonByTest(idTest);
        boolean isPublishCourse = courseServices.isPublishCourse(idCourse);
        ArrayList<TreeNode> listQuestion = util.switchQuestionTestToNode(tdao.getQuestionInTest(idTest), showBtnAction, idLesson, isPublishCourse);
        list = listQuestion;
        return list;
    }

    public String getIdLessonByTest(String idTest){
        TMLDAO dao = new TMLDAO();
        try {
            String idLesson = dao.getLessonId(idTest);
            return idLesson;
        }catch (Exception e){

        }
        return "not found";
    }





}


