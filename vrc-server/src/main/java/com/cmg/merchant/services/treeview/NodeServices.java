package com.cmg.merchant.services.treeview;

import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.merchant.dao.lessons.LMODAO;
import com.cmg.merchant.dao.questions.QMLDAO;
import com.cmg.merchant.dao.test.TDAO;
import com.cmg.merchant.data.dto.TreeNode;
import com.cmg.merchant.util.TreeUtil;

import java.util.ArrayList;

/**
 * Created by lantb on 2016-02-22.
 */
public class NodeServices {
    TreeUtil util = new TreeUtil();
    DataServices dataServices = new DataServices();
    LMODAO lmodao=new LMODAO();
    QMLDAO qmldao=new QMLDAO();
    TDAO tdao = new TDAO();
    public ArrayList<TreeNode> loadRoot(String idCourse, boolean firstLoad, boolean showBtnAction) {
        ArrayList<TreeNode> list = new ArrayList<>();
        if(firstLoad){
            TreeNode root = util.switchCourseToRoot(dataServices.getRootNode(idCourse),showBtnAction);
            root.setBranch(util.switchLevelToNode(dataServices.getLevelDB(idCourse),showBtnAction));
            list.add(root);
        }else{
            list = util.switchLevelToNode(dataServices.getLevelDB(idCourse),showBtnAction);
        }
        return list;
    }

    /**
     *
     * @param idLevel
     * @param showBtnAction
     * @return all child in specific level
     */
    public ArrayList<TreeNode> loadLevel(String idLevel, boolean showBtnAction){
        ArrayList<TreeNode> list = new ArrayList<>();
        //load all obj
        ArrayList<TreeNode> listObj = util.switchObjToNode(dataServices.getObjDB(idLevel), showBtnAction);
        list = listObj;
        list.add(util.switchTestToNode(dataServices.getTestDB(idLevel),showBtnAction));
        return list;
    }

    public ArrayList<TreeNode> loadObjective (String idObj, boolean showBtnAction){
        ArrayList<TreeNode> list = new ArrayList<>();
        ArrayList<TreeNode> listObj = util.switchLessonToNode(lmodao.getLessonMappingObjective(idObj), showBtnAction);
        list = listObj;
        return list;
    }


    public ArrayList<TreeNode> loadLesson (String idLesson, boolean showBtnAction){
        ArrayList<TreeNode> list = new ArrayList<>();
        ArrayList<TreeNode> listObj = util.switchQuestionToNode(qmldao.getQuestionByIdLesson(idLesson),showBtnAction);
        list = listObj;
        return list;
    }


    public ArrayList<TreeNode> loadTest(String idTest, boolean showBtnAction){
        ArrayList<TreeNode> list = new ArrayList<>();
        ArrayList<TreeNode> listQuestion = util.switchQuestionTestToNode(tdao.getQuestionInTest(idTest), showBtnAction);
        list = listQuestion;
        return list;
    }







}


