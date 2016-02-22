package com.cmg.merchant.util;

import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.merchant.common.Constant;
import com.cmg.merchant.data.dto.TreeNode;
import com.cmg.merchant.services.treeview.ButtonServices;

import java.util.ArrayList;

/**
 * Created by lantb on 2016-02-22.
 */
public class TreeUtil {
    public static String ERROR = "An error has been occurred in server!";
    public static String SUCCESS = "load success";
    public ButtonServices btnService = new ButtonServices();
    /**
     *
     * @return default instance
     */
    public TreeNode getDefaultInstance(Boolean showBtnAction){
        TreeNode node = new TreeNode();
        node.set_textColor(Constant.TEXT_COLOR);
        node.set_backgroundColor(Constant.BG_COLOR);
        node.setInode(true);
        node.set_isButton(false);
        node.set_message(SUCCESS);
        if(showBtnAction){
            node.set_iconLeft(Constant.IC_RIGHT_EDIT);
        }
        return node;
    }


    public TreeNode switchCourseToRoot (Course c, boolean showBtnAction){
        TreeNode node = getDefaultInstance(showBtnAction);
        if(c!=null){
            node.setId(c.getId());
            node.setLabel(c.getName());
            node.set_title(c.getDescription());
            node.set_targetLoad(Constant.TARGET_LOAD_COURSE);
            node.setIcon(Constant.IC_COURSE);
            node.setOpen(true);
        }else{
            node.set_message(ERROR);
        }
        return node;
    }

    public ArrayList<TreeNode> switchLevelToNode(ArrayList<Level> list, boolean showBtnAction){
        ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
        if(showBtnAction){
            nodes.add(btnService.createBtnAddLevel());
        }
        if(list!=null){
            for(Level lv : list){
                TreeNode node = getDefaultInstance(showBtnAction);
                node.setId(lv.getId());
                node.setLabel(lv.getName());
                node.set_title(lv.getDescription());
                node.set_targetLoad(Constant.TARGET_LOAD_LEVEL);
                node.setIcon(Constant.IC_LEVEL);
                node.setOpen(false);
                nodes.add(node);
            }
        }else{
            TreeNode node = getDefaultInstance(showBtnAction);
            node.set_message(ERROR);
            nodes.add(node);
        }
        return nodes;
    }

}
