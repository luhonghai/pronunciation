package com.cmg.merchant.data.dto;

import java.util.List;

/**
 * Created by lantb on 2016-02-16.
 */
public class TreeNode {
    /*
        populate id of the node
     */
    private String id;

    /*
        populate name of the node
     */
    private String label;

    /*
        populate the node has child or not
     */
    private boolean inode;

    /*
        populate the icon of the node
     */
    private String icon;

    /*
        populate this node is open or not
     */
    private boolean open;

    /*
        populate the child item of this node
     */
    private List<TreeNode> branch;
    /*
        populate the background color for this node
     */
    private String _backgroundColor;

    /*
        populate the image action for  this node
     */
    private String _imageActionUrl;

    /*
        populate the special case for load all the question in the test
     */
    private String _idLessonForTest;

    /*
        populate if the node is the button or not
     */
    private boolean _isButton;

    /*
        populate the target load when click on the node
     */
    private String _targetLoad;

    private String _textColor;

    public String get_textColor() {
        return _textColor;
    }

    public void set_textColor(String _textColor) {
        this._textColor = _textColor;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public String get_imageActionUrl() {
        return _imageActionUrl;
    }

    public void set_imageActionUrl(String _imageActionUrl) {
        this._imageActionUrl = _imageActionUrl;
    }
    public String get_targetLoad() {
        return _targetLoad;
    }

    public void set_targetLoad(String _targetLoad) {
        this._targetLoad = _targetLoad;
    }

    public boolean is_isButton() {
        return _isButton;
    }

    public void set_isButton(boolean _isButton) {
        this._isButton = _isButton;
    }

    public String get_idLessonForTest() {
        return _idLessonForTest;
    }

    public void set_idLessonForTest(String _idLessonForTest) {
        this._idLessonForTest = _idLessonForTest;
    }

    public String get_backgroundColor() {
        return _backgroundColor;
    }

    public void set_backgroundColor(String _backgroundColor) {
        this._backgroundColor = _backgroundColor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isInode() {
        return inode;
    }

    public void setInode(boolean inode) {
        this.inode = inode;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<TreeNode> getBranch() {
        return branch;
    }

    public void setBranch(List<TreeNode> branch) {
        this.branch = branch;
    }
}
