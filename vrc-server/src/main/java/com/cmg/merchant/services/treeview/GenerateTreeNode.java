package com.cmg.merchant.services.treeview;

import com.cmg.merchant.common.Constant;
import com.cmg.merchant.data.dto.TreeNode;

import java.util.ArrayList;

/**
 * Created by lantb on 2016-02-16.
 */
public class GenerateTreeNode {
    public GetData gData = new GetData();

    public ArrayList<TreeNode> getCourseNode(String idCourse, boolean generateButton){
        ArrayList<TreeNode> list = new ArrayList<>();
        TreeNode cNode = new TreeNode();
        cNode.setId(idCourse);
        cNode.setLabel("Course Example");
        cNode.setBranch(gData.getAllLevelInCourse(idCourse, generateButton));
        cNode.setInode(true);
        cNode.setOpen(true);
        cNode.setIcon(Constant.IC_COURSE);
        cNode.set_backgroundColor(Constant.BG_COLOR);
        cNode.set_textColor(Constant.TEXT_COLOR);
        cNode.set_targetLoad(Constant.TARGET_LOAD_COURSE);
        list.add(cNode);
        return list;
    }

    public ArrayList<TreeNode> getChildInLevel(String idLevel, boolean generateButton){
        ArrayList<TreeNode> cNode = gData.getAllObjAndTestInLevel(idLevel,false);
        return cNode;
    }


}
