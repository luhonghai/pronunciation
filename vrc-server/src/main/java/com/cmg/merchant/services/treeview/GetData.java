package com.cmg.merchant.services.treeview;

import com.cmg.merchant.common.Constant;
import com.cmg.merchant.data.dto.TreeNode;

import java.util.ArrayList;

/**
 * Created by lantb on 2016-02-16.
 */
public class GetData {
    public GenerateButton grtBtn = new GenerateButton();

    public ArrayList<TreeNode> getAllLevelInCourse(String idCourse, boolean generateButton){
        ArrayList<TreeNode> list = new ArrayList<TreeNode>();
        if(generateButton){
            list.add(grtBtn.ButtonAddLevel());
        }
        TreeNode lv1 = new TreeNode();
        lv1.setId("lv1");
        lv1.setLabel("Level 1");
        lv1.set_targetLoad(Constant.TARGET_LOAD_LEVEL);
        lv1.setInode(true);
        lv1.setOpen(false);
        lv1.setIcon(Constant.IC_LEVEL);
        lv1.set_backgroundColor(Constant.BG_COLOR);
        lv1.set_textColor(Constant.TEXT_COLOR);
        lv1.setBranch(new ArrayList<TreeNode>());
        list.add(lv1);
        return list;
    }

    public ArrayList<TreeNode> getAllObjAndTestInLevel(String idLevel, boolean generateButton){
        ArrayList<TreeNode> list = new ArrayList<TreeNode>();
        if(generateButton){
            list.add(grtBtn.ButtonAddTest());
            list.add(grtBtn.ButtonAddObj());
        }

        TreeNode obj1 = new TreeNode();
        obj1.setId("obj1");
        obj1.setLabel("obj 1");
        obj1.set_targetLoad(Constant.TARGET_LOAD_OBJECTIVE);
        obj1.setInode(true);
        obj1.setOpen(false);
        obj1.setIcon(Constant.IC_OBJ);
        obj1.set_backgroundColor(Constant.BG_COLOR);
        obj1.set_textColor(Constant.TEXT_COLOR);
        obj1.setBranch(new ArrayList<TreeNode>());
        list.add(obj1);

        TreeNode test1 = new TreeNode();
        test1.setId("test1");
        test1.setLabel("test1");
        test1.set_targetLoad(Constant.TARGET_LOAD_TEST);
        test1.setInode(true);
        test1.setOpen(false);
        test1.setIcon(Constant.IC_TEST);
        test1.set_backgroundColor(Constant.BG_COLOR);
        test1.set_textColor(Constant.TEXT_COLOR);
        test1.setBranch(new ArrayList<TreeNode>());
        list.add(test1);
        return list;
    }
}
