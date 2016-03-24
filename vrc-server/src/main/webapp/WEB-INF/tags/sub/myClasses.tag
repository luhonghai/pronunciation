<%@ tag import="com.cmg.merchant.util.SessionUtil" %>
<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<style>
    .title{
        font-weight : 600;
        font-family : "Helvetica Neue",Helvetica,Arial,sans-serif;
        font-size : 16px;
        text-align: center;
    }
    .textNormal{
        font-size: 14px;
    }
    #helpAddClassModal p{
        font-size: 14px;
        font-family : "Helvetica Neue",Helvetica,Arial,sans-serif;
    }
    #helpMyClassModal p{
        font-size: 14px;
        font-family : "Helvetica Neue",Helvetica,Arial,sans-serif;
    }
    #add label{
        font-size: 14px;
        text-align: left;
        font-weight : 200;
        font-family : "Helvetica Neue",Helvetica,Arial,sans-serif;
    }
    #edits label{
          font-size: 14px;
          text-align: left;
          font-weight : 200;
          font-family : "Helvetica Neue",Helvetica,Arial,sans-serif;
    }

</style>
<%String company= StringUtil.isNull(session.getAttribute(SessionUtil.ATT_CPNAME),"").toString();%>
<div id="page-wrapper">
     <div class="row" style="color:lightgrey; margin-left: 0px;padding-top: 10px;">
         <p class="textNormal" style="float: left;"><%=company%></p> <p class="textNormal">> my classes</p>
     </div>
    <p style="font-size: 20px;">my classes</p>
    <button id="addClass" class="btn btn-default" style="background-color: orange;color: white;border-radius: 3px; padding: 1px 5px;"><img src="/images/teacher/invite_students_48x48.gif" style="width: 24px;height: 24px;"> add class <i class="fa fa-plus"></i> </button>
    <div>
        <p class="textNormal">Select the button above to create a new class.</p>
    </div>
    <div id="listMyClass">

    </div>
</div>
<!-- /#wrapper -->

<div id="helpMyClassModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <div class="row">
                    <div class="col-xs-12 col-md-10 col-md-offset-1">

                        <p class="title" align="center">My classes</p>
                        <p>Use this page to create and manage your classes.</p>
                        <p>Add a class (name and description) and assign courses and students that you have availale in 'my courses' and 'my students' respectively.</p>
                        <p><strong>Note</strong>: You will only be able to add courses that have been published, licenced students and external student that have been linked to you. You will not be able to add external students that are pending acceptence or that have reiected an invitation.</p>
                        <p>Select a class from the list to view or edit the detail or to delete the class completely.</p>
                        <p>When a class has been created (or edited) and saved, the students will receive a notification on their phones.</p>
                        <p>If new courses are added, they will be available for use by the students assigned to class.</p>
                        <p>If you remove a course from your class, it will be removed from the phones of all students that are currently assigned to the class.</p>
                        <p>If you remove a student from your class, all courses that are currently assigned to the class it will be removed from their phone.</p>
                        <p>If you delete the class, it will be removed from your list and any associated students will no longer have access to the courses that were assigned to the class.</p>
                    </div>
                </div>
            </div>


        </div>
    </div>
</div>


<div id="add" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <div class="row">
                    <div class="col-xs-12 col-md-10 col-md-offset-1">

                        <h1 align="center" class="title">Add Class</h1>
                        <form name="add" class="form-horizontal"
                              style="margin-top: 25px" id="addform">

                            <div class="form-group">
                                <p id="addClassNameExits" class="addClassNameExits" style="color:red;margin-left:50px;display: none;">Class name exits</p>
                                <label class="col-xs-4  col-sm-3 control-label">name:</label>
                                <div class="col-xs-8  col-sm-9">
                                    <input  type="text" id="addClassName" name="addClassName" class=" form-control" style="padding-left: 0px;">
                                    <p id="nameadds" class="nameadd" style="color:red; display: none;">Required field to enter data</p>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-4  col-sm-3 control-label">definition:</label>
                                <div class="col-xs-8  col-sm-9">
                                    <input type="text" id="addDefinition" name="addDefinition" class="col-xs-8  col-sm-9 form-control" style="padding-left: 0px;">
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-xs-4  col-sm-3 control-label">add/remove courses:</label>
                                <div class="col-xs-8  col-sm-9">
                                    <select style="display:none;" multiple class="form-control" id="addCourses">
                                        <option>1</option>
                                        <option>2</option>
                                        <option>3</option>
                                        <option>4</option>
                                        <option>5</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-4  col-sm-3 control-label">add/remove students:</label>
                                <div class="col-xs-8  col-sm-9">
                                    <select style="display:none;" multiple class="form-control" id="addStudents">
                                        <option>1</option>
                                        <option>2</option>
                                        <option>3</option>
                                        <option>4</option>
                                        <option>5</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group contain-button">
                                <div class="col-md-5">
                                    <img id="helpAddClass" class="helpInfor" src="/images/popup/help_50_50.png" width="36px"
                                         height="36px"/>
                                </div>
                                <div class="col-md-2">
                                </div>
                                <div class="col-md-5">
                                    <img style="float: right" id="yesadd" src="/images/popup/Save_50x50.gif" width="36px"
                                         height="36px"/>
                                </div>
                            </div>

                        </form>
                    </div>
                </div>
            </div>


        </div>
    </div>
</div>


<div id="edits" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <div class="row">
                    <div class="col-xs-12 col-md-10 col-md-offset-1">
                        <input type="hidden" id="classname">
                        <h1 align="center" class="title">Edit Class</h1>
                        <form name="Edit" class="form-horizontal"
                              style="margin-top: 25px" id="editform">
                            <input type="hidden" id="idedit" name="idedit">

                            <div class="form-group">
                                <label class="col-xs-4  col-sm-3 control-label">Class Name:</label>
                                <div class="col-xs-8  col-sm-9">
                                    <input  type="text" id="editClassName" name="editClassName" class=" form-control" style="padding-left: 0px;" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-4  col-sm-3 control-label">Definition:</label>
                                <div class="col-xs-8  col-sm-9">
                                    <input type="text" id="editDefinition" name="editDefinition" class="col-xs-8  col-sm-9 form-control" style="padding-left: 0px;">
                                </div>

                            </div>
                            <div class="form-group">
                                <label class="col-xs-4  col-sm-3 control-label">add/remove courses:</label>
                                <div class="col-xs-8  col-sm-9">
                                    <select style="display:none;" multiple class="form-control" id="editCourses">
                                        <option>1</option>
                                        <option>2</option>
                                        <option>3</option>
                                        <option>4</option>
                                        <option>5</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-4  col-sm-3 control-label">add/remove students:</label>
                                <div class="col-xs-8  col-sm-9">
                                    <select style="display:none;" multiple class="form-control" id="editStudents">
                                        <option>1</option>
                                        <option>2</option>
                                        <option>3</option>
                                        <option>4</option>
                                        <option>5</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group contain-button">
                                <div class="col-md-5">
                                    <img id="helpEditClass" class="helpInfor" src="/images/popup/help_50_50.png" width="36px"
                                         height="36px"/>
                                </div>
                                <div class="col-md-2">
                                    <img id="delete" src="/images/popup/trash_50x50.gif" width="36px" height="36px"/>
                                </div>
                                <div class="col-md-5">
                                    <img style="float: right" id="yesedit" src="/images/popup/Save_50x50.gif" width="36px"
                                         height="36px"/>
                                </div>
                            </div>

                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<div id="helpAddClassModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <div class="row">
                    <div class="col-xs-12 col-md-10 col-md-offset-1">

                        <h1 align="center" class="title">class management</h1>
                        <p><strong>Add a class:</strong></p>
                        <p>1. Enter the name of the class you wish to add.</p>
                        <p>   This name is for your reference only.</p>
                        <p>2. Enter a description (not mandatory).</p>
                        <p>3. Select the course/s that you wish to make available for your class from the drop down list.</p>
                        <p>4. Select the students that you wish to add to your class from the drop down list.</p>
                        <p>5. Select 'save'.</p>
                        <p><strong>Note:</strong> You will only be able to add courses that have been published, licenced students and external students that have been linked to you. You will not be able to add external students that are pending acceptance or that have rejected an invitation.</p>
                        <p><strong>Edit or delete a class:</strong></p>
                        <p>You can add and remove students and courses to and from your class at any time. You will need to save your changes. Select the class title bar to view, edit or delete.</p>
                        <p>Selecting the delete button will remove the class from your list and any associated students will no longer have access to the courses that were assigned to the class.</p>
                    </div>
                </div>
            </div>


        </div>
    </div>
</div>


<div id="confirmDelete" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <div class="row">
                    <div class="col-xs-12 col-md-10 col-md-offset-1">
                        <input type="hidden" id="iddelete">
                        <h1 align="center" class="title">Confirm deletion</h1>
                        <p align="center" id="classNameDelete" class="textNormal"></p>
                        <form name="add" class="form-horizontal"
                              style="margin-top: 25px">

                            <p class="textNormal">If you delete this class, the associated students will no longer have access to the courses assigned.</p>
                            <p class="textNormal">Do you wish to continue?</p>
                            <div class="row">
                                <p id="cancel" style="float:left;cursor: pointer;"><u>cancel</u></p>
                                <img style="float: right" id="deleteItems" src="/images/popup/Save_50x50.gif" width="36px" height="36px"/>
                            </div>
                        </form>
                    </div>
                </div>
            </div>


        </div>
    </div>
</div>
<div id="confirmDelete" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <div class="row">
                    <div class="col-xs-12 col-md-10 col-md-offset-1">
                        <input type="hidden" id="iddelete">
                        <h1 align="center" class="title">Confirm deletion</h1>
                        <p align="center" id="classNameDelete" class="textNormal"></p>
                        <form name="add" class="form-horizontal"
                              style="margin-top: 25px">

                            <p class="textNormal">If you delete this class, the associated students will no longer have access to the courses assigned.</p>
                            <p class="textNormal">Do you wish to continue?</p>
                            <div class="row">
                                <p id="cancel" style="float:left;cursor: pointer;"><u>cancel</u></p>
                                <img style="float: right" id="deleteItems" src="/images/popup/Save_50x50.gif" width="36px" height="36px"/>
                            </div>
                        </form>
                    </div>
                </div>
            </div>


        </div>
    </div>
</div>
<div id="classExits" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <div class="row">
                    <div class="col-xs-12 col-md-10 col-md-offset-1">
                        <h1 align="center" class="title" style="color: red">invalid class name</h1>
                        <form name="add" class="form-horizontal"
                              style="margin-top: 25px">

                            <p class="textNormal">You already have a class with this name:</p>
                            <p class="textNormal" style="color: red">Private students</p>
                            <p class="textNormal">Please check and rename if you wish to continue.</p>
                        </form>
                    </div>
                </div>
            </div>


        </div>
    </div>
</div>

<script src="<%=request.getContextPath() %>/js/class.js"></script>




