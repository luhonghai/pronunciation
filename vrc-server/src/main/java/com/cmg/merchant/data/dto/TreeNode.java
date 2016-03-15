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
    private String _iconLeft;

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

    private String _message;

    private String _title;

    private String _popupId;

    private String _actionClick;

    private String _description;

    private String _details;

    private String _type;



    public String get_actionClick() {
        return _actionClick;
    }

    public void set_actionClick(String _actionClick) {
        this._actionClick = _actionClick;
    }

    public String get_popupId() {
        return _popupId;
    }

    public void set_popupId(String _popupId) {
        this._popupId = _popupId;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public String get_message() {
        return _message;
    }

    public void set_message(String _message) {
        this._message = _message;
    }

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

    public String get_iconLeft() {
        return _iconLeft;
    }

    public void set_iconLeft(String _iconLeft) {
        this._iconLeft = _iconLeft;
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

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public String get_details() {
        return _details;
    }

    public void set_details(String _details) {
        this._details = _details;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }
}
