package com.cmg.merchant.services.treeview;

import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.merchant.data.dto.TreeNode;
import com.cmg.merchant.util.TreeUtil;

import java.util.ArrayList;

/**
 * Created by lantb on 2016-02-22.
 */
public class NodeServices {
    TreeUtil util = new TreeUtil();
    DataServices dataServices = new DataServices();
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
        ArrayList<TreeNode> listObj = util.switchObjToNode(dataServices.getObjDB(idLevel),showBtnAction);
        list = listObj;
        list.add(util.switchTestToNode(dataServices.getTestDB(idLevel),showBtnAction));
        return list;
    }

    public ArrayList<TreeNode> loadObjective (String idObj, boolean showBtnAction){
        return null;
    }

    public ArrayList<TreeNode> loadTest(String idTest, boolean showBtnAction){
        return null;
    }







}

