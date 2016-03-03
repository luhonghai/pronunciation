package com.cmg.merchant.services.treeview;

import com.cmg.merchant.common.Constant;
import com.cmg.merchant.data.dto.TreeNode;

/**
 * Created by lantb on 2016-02-22.
 */
public class ButtonServices {
    /**
     *
     * @return default instance
     */
    public TreeNode getDefaultBtn(){
        TreeNode node = new TreeNode();
        node.set_textColor(Constant.TEXT_COLOR);
        node.set_backgroundColor(Constant.BG_COLOR_BUTTON);
        node.setInode(false);
        node.setBranch(null);
        node.setOpen(false);
        node.set_iconLeft(Constant.IC_LEFT_ADD);
        node.set_isButton(true);
        return node;
    }

    /**
     *
     * @return btn add level
     */
    public TreeNode createBtnAddLevel(){
        TreeNode node = getDefaultBtn();
        node.setId(Constant.ID_ADD_LEVEL_BTN);
        node.setLabel(Constant.LABEL_ADD_LEVEL);
        node.setIcon(Constant.IC_BTN_ADD_LEVEL);
        node.set_popupId(Constant.POPUP_LEVEL);
        node.set_actionClick(Constant.ACTION_ADD_LEVEL);
        return node;
    }

    /**
     *
     * @return btn add obj
     */
    public TreeNode createBtnAddObj(){
        TreeNode node = getDefaultBtn();
        node.setId(Constant.ID_ADD_OBJ_BTN);
        node.setLabel(Constant.LABEL_ADD_OBJ);
        node.setIcon(Constant.IC_BTN_ADD_OBJ);
        node.set_popupId(Constant.POPUP_OBJ);
        node.set_actionClick(Constant.ACTION_ADD_OBJ);
        return node;
    }

    /**
     *
     * @return btn add test
     */
    public TreeNode createBtnAddTest(){
        TreeNode node = getDefaultBtn();
        node.setId(Constant.ID_ADD_TEST_BTN);
        node.setLabel(Constant.LABEL_ADD_TEST);
        node.setIcon(Constant.IC_BTN_ADD_TEST);
        node.set_popupId(Constant.POPUP_TEST);
        node.set_actionClick(Constant.ACTION_ADD_TEST);
        return node;
    }

    public TreeNode createBtnAddLesson(){
        TreeNode node = getDefaultBtn();
        return node;
    }

    public TreeNode createBtnAddQuestion(){
        TreeNode node = getDefaultBtn();
        return node;
    }




}