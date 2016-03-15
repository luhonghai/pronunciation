package com.cmg.merchant.util;

import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.lesson.data.jdo.question.Question;
import com.cmg.lesson.data.jdo.test.Test;
import com.cmg.lesson.data.jdo.word.WordCollection;
import com.cmg.merchant.common.Constant;
import com.cmg.merchant.dao.test.TMLDAO;
import com.cmg.merchant.dao.word.WDAO;
import com.cmg.merchant.data.dto.TreeNode;
import com.cmg.merchant.services.treeview.ButtonServices;
import edu.cmu.sphinx.linguist.dictionary.Word;
import org.apache.commons.lang.StringUtils;
import org.apache.http.util.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lantb on 2016-02-22.
 */
public class TreeUtil {
    public static String ERROR = "An error has been occurred in server!";
    public static String SUCCESS = "load success";
    public ButtonServices btnService = new ButtonServices();
    WDAO wdao=new WDAO();
    /**
     *
     * @return default instance of the node
     */
    public TreeNode getDefaultInstance(Boolean showBtnAction){
        TreeNode node = new TreeNode();
        node.set_textColor(Constant.TEXT_COLOR);
        node.set_backgroundColor(Constant.BG_COLOR);
        node.setInode(true);
        node.set_isButton(false);
        node.set_message(SUCCESS);
        node.set_actionClick(Constant.ACTION_EDIT_LEVEL);
        if(showBtnAction){
            node.set_iconLeft(Constant.IC_RIGHT_EDIT);
        }
        return node;
    }

    /**
     *
     * @param c
     * @param showBtnAction
     * @return root of the tree view
     */
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


    /**
     *
     * @param list
     * @param showBtnAction
     * @return all level nodes in the tree view
     */
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
                node.set_popupId(Constant.POPUP_LEVEL);
                node.set_actionClick(Constant.ACTION_EDIT_LEVEL);
                nodes.add(node);
            }
        }else{
            TreeNode node = getDefaultInstance(showBtnAction);
            node.set_message(ERROR);
            nodes.add(node);
        }
        return nodes;
    }

    /**
     *
     * @param list
     * @param showBtnAction
     * @return
     */
    public ArrayList<TreeNode> switchObjToNode(ArrayList<Objective> list, boolean showBtnAction){
        ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
        if(showBtnAction){
            nodes.add(btnService.createBtnAddObj());
        }
        if(list!=null){
            for(Objective obj : list){
                TreeNode node = getDefaultInstance(showBtnAction);
                node.setId(obj.getId());
                node.setLabel(obj.getName());
                node.set_title(obj.getDescription());
                node.setIcon(Constant.IC_OBJ);
                node.set_targetLoad(Constant.TARGET_LOAD_OBJECTIVE);
                node.set_popupId(Constant.POPUP_OBJ);
                node.set_actionClick(Constant.ACTION_EDIT_OBJ);
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

    /**
     *
     * @param test
     * @param showBtnAction
     * @return
     */
    public TreeNode switchTestToNode(Test test, boolean showBtnAction){
        ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
        if(test != null){
            String idLessons = "";
            try {
                TMLDAO dao = new TMLDAO();
                idLessons = dao.getByIdTest(test.getId()).getIdLessonCollection();
            }catch (Exception e){
            }
            TreeNode node = getDefaultInstance(false);
            node.setId(test.getId());
            node.set_idLessonForTest(idLessons);
            node.setLabel(test.getName());
            node.set_title(test.getPercentPass()+"%");
            node.setIcon(Constant.IC_TEST);
            node.set_targetLoad(Constant.TARGET_LOAD_TEST);
            node.set_popupId(Constant.POPUP_TEST);
            node.set_actionClick(Constant.ACTION_EDIT_TEST);
            node.setOpen(false);
            nodes.add(node);
        }else{
            if(showBtnAction){
                nodes.add(btnService.createBtnAddTest());
            }
        }
        return nodes.get(0);
    }


    /**
     *
     * @param list
     * @param showBtnAction
     * @return
     */
    public ArrayList<TreeNode> switchLessonToNode(ArrayList<LessonCollection> list, boolean showBtnAction){
        ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
        if(showBtnAction){
            nodes.add(btnService.createBtnAddLesson());
        }
        if(list!=null){
            for(LessonCollection lesson : list){
                TreeNode node = getDefaultInstance(showBtnAction);
                node.setId(lesson.getId());
                node.setLabel(lesson.getName());
                node.set_type(lesson.getType());
                node.set_description(lesson.getDescription());
                node.set_details(lesson.getNameUnique());
                node.setIcon(Constant.IC_LESSON);
                node.set_targetLoad(Constant.TARGET_LOAD_LESSONS);
                node.set_popupId(Constant.POPUP_LESSON);
                node.set_actionClick(Constant.ACTION_EDIT_LESSON);
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

    public ArrayList<TreeNode> switchQuestionToNode(ArrayList<Question> list, boolean showBtnAction){
        ArrayList<TreeNode> nodes = new ArrayList<TreeNode>();
        if(showBtnAction){
            nodes.add(btnService.createBtnAddLesson());
        }
        if(list!=null){
            for(Question question : list){
                TreeNode node = getDefaultInstance(showBtnAction);
                node.setId(question.getId());
                node.setLabel(question.getName());
                node.set_description(listWordOnQuestion(question.getId()));
                node.setIcon(Constant.IC_QUESTION);
                node.set_targetLoad(Constant.TARGET_LOAD_QUESTION);
                node.set_popupId(Constant.POPUP_QUESTION);
                node.set_actionClick(Constant.ACTION_EDIT_QUESTION);
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
    public String listWordOnQuestion(String idQuestion){
        String listWord="";
        List<String> lists=wdao.getWordByIdQuestion(idQuestion);
        listWord= StringUtils.join(lists,',');
        return listWord;
    }



}
