<%@ tag import="com.cmg.merchant.util.SessionUtil" %>
<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<style>
    .header-company {
        color: #A6A6A6;
        font-weight: 200;
        font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
        font-size: 14px;
    }

    .studentMail{
        font-weight: 200;
        font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
    }

    .a-info-student{
        padding:10px;
        border-radius: 3px;
        color:#ffffff;
        cursor: pointer;
        text-decoration: none !important;
    }
    .a-info-student:hover{
        color:#ffffff;
        cursor: pointer;
    }

    .a-info-student img{
        cursor: pointer;
    }
    .a-info-student label{
        cursor: pointer;
    }

    p{
        color: #376092;
        font-weight: 200;
        font-size: 14px;
        font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
        font-size: 14px;
    }
    h2.header{
        color: #376092;
        font-weight: 700;
        font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
        margin-top: 0px;
    }

    .sweet-alert {
        font-weight: 200 !important;
        font-family: "Helvetica Neue",Helvetica,Arial,sans-serif !important;
        font-size: 16px !important;
        width: 400px !important;
        border-radius: 20px;
    }
    .sweet-alert p {
        font-weight: 200 !important;
        font-family: "Helvetica Neue",Helvetica,Arial,sans-serif !important;
        font-size: 14px !important;
        color: inherit !important;
    }

    .modal-content{
        -webkit-border-radius: 20px;
        -moz-border-radius: 20px;
        border-radius: 20px;
    }
    .modal-header {
        border-bottom: transparent;
        padding-bottom: 0px;
        text-align: center;
    }

    .modal-title {
        font-weight: 700;
    }

    #helpMyClassModal{
        color : #957F7F;
    }
    #helpMyClassModal p{
        color : #957F7F !important;
    }
    #helpAddClassModal {
        color : #957F7F;
    }
    #helpAddClassModal p{
        color : #957F7F !important;
    }
    #titleAdd {
        font-weight: 600;
        font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
        font-size: 16px;
        text-align: center;
        padding-top : 20px;
    }

    #add .validateMsg {
        font-weight: 200;
        font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
        font-size: 16px;
        color: red;
    }
    #add label {
        font-weight: 200;
        font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
        font-size: 14px;
    }
    #add .contain-button{
        margin-bottom : 0px;
    }

    #add img{
        cursor: pointer;
    }

    #edits .validateMsg {
        font-weight: 200;
        font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
        font-size: 16px;
        color: red;
    }
    #edits label {
        font-weight: 200;
        font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
        font-size: 14px;
    }
    #edits .contain-button{
        margin-bottom : 0px;
    }

    #edits img{
        cursor: pointer;
    }

    #confirmDelete label{
        font-weight: 200;
        color : #957F7F !important;
        font-weight: 200;
        font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
        font-size: 14px;
    }

    #confirmDelete .form-group{
        margin-bottom: 0px;
    }

</style>
<%String company= StringUtil.isNull(session.getAttribute(SessionUtil.ATT_CPNAME),"").toString();%>
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h4 style="border-bottom: transparent" class="header-company">
                <%=company%> > my classes
            </h4>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-12">
            <h2 class="header">My classes</h2>
        </div>
        <div style="padding-bottom: 10px" class="col-sm-12">
            <button id="addClass" class="btn btn-default" style="background-color: #F7964A;color: white;border-radius: 3px; padding: 5px 10px;">add class <i class="fa fa-plus"></i> </button>
        </div>
        <div class="col-sm-12">
            <p class="textNormal">Select the button above to create a new class.</p>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-12" id="listMyClass">

        </div>
    </div>
</div>
<!-- /#wrapper -->

<div id="helpMyClassModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content modal-lager">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <label class="modal-title" style="font-weight: 700;font-size:18px">my classes</label>
            </div>
            <div class="modal-body">
                <div>
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

<div id="add" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h2 style="font-weight: 700;font-size:18px" id="titleAdd" class="modal-title">add class</h2>
                <h4 id="validateLvMsg" class="modal-title validateMsg" style="text-align: center;
                font-weight: 200;font-size:14px color: red;"></h4>
            </div>
            <div class="modal-body">
                <form name="add" class="form-horizontal">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">name:</label>
                        <div class="col-sm-8">
                            <input  type="text" id="addClassName" name="addClassName" placeholder="class name" class=" form-control">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">description:</label>
                        <div class="col-sm-8">
                             <textarea rows="2" class="form-control" id="addDefinition" name="addDefinition" placeholder="class description"></textarea>
                        </div>

                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">add/remove courses:</label>
                        <div class="col-sm-8">
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
                        <label class="col-sm-4 control-label">add/remove students:</label>
                        <div class="col-sm-8">
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
            <!-- End of Modal body -->
        </div>
        <!-- End of Modal content -->
    </div>
    <!-- End of Modal dialog -->
</div>

<div id="edits" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h2 style="font-weight: 700;font-size:18px" class="modal-title">class management</h2>
                <h4 id="validateLvMsgEdit" class="modal-title validateMsg" style="text-align: center;
                font-weight: 200;font-size:14px color: red;"></h4>
            </div>
            <div class="modal-body">
                <form name="Edit" class="form-horizontal" id="editform">
                    <input type="hidden" id="idedit" name="idedit">
                    <div class="form-group">
                        <label class="col-sm-4 control-label">name:</label>
                        <div class="col-sm-8">
                            <input  type="text" id="editClassName" name="editClassName" placeholder="class name" class=" form-control">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">description:</label>
                        <div class="col-sm-8">
                            <textarea rows="2" class="form-control" id="editDefinition" name="editDefinition" placeholder="class description"></textarea>
                        </div>

                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label">add/remove courses:</label>
                        <div class="col-sm-8">
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
                        <label class="col-sm-4 control-label">add/remove students:</label>
                        <div class="col-sm-8">
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
                        <div class="col-md-4">
                            <img id="helpEditClass" class="helpInfor" src="/images/popup/help_50_50.png" width="36px"
                                 height="36px"/>
                        </div>
                        <div style="text-align: center" class="col-md-4">
                            <img id="delete"  src="/images/popup/trash_50x50.gif" width="36px" height="36px"/>
                        </div>
                        <div class="col-md-4">
                            <img style="float: right" id="yesedit" src="/images/popup/Save_50x50.gif" width="36px"
                                 height="36px"/>
                        </div>
                    </div>

                </form>
            </div>
            <!-- End of Modal body -->
        </div>
        <!-- End of Modal content -->
    </div>
    <!-- End of Modal dialog -->
</div>



<div id="helpAddClassModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content modal-lager">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h2 style="font-weight: 700;font-size:18px" class="modal-title">class management</h2>
            </div>
            <div class="modal-body">
                <div>
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


<div id="confirmDelete" class="modal fade">
    <div style="width:400px" class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 style="font-weight: 700;font-size:18px" class="modal-title">confirm deletion</h4>
            </div>
            <div class="modal-body">
                <form name="add" class="form-horizontal">
                    <div class="form-group">
                        <div style="text-align: center" class="col-sm-12">
                            <label style="color : red !important;" id="classNameDelete" class="textNormal"></label>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-12">
                            <label class="textNormal">If you delete this class, the associated
                            students will no longer have access to the courses assigned.</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-12">
                            <label class="textNormal">Do you wish to continue?</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-6">
                            <label id="cancel" style="float:left;cursor: pointer;padding-top:15px"><u>cancel</u></label>
                        </div>
                        <div class="col-sm-6">
                            <img style="float: right;cursor: pointer" id="deleteItems"
                                 src="/images/popup/trash_50x50.gif" width="36px" height="36px"/>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<div id="classExits" class="modal fade">
    <div style="width:350px" class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h2 align="center" class="modal-title" style="color: red;font-weight: 700;font-size:18px">invalid class name</h2>
            </div>
            <div class="modal-body">
                <form name="add" class="form-horizontal"
                      style="margin-bottom: 10px;">
                    <p class="textNormal">You already have a class with this name:</p>
                    <p id="invalidClass" class="textNormal" style="color: red">Private students</p>
                    <p class="textNormal">Please check and rename if you wish to continue.</p>
                </form>
            </div>


        </div>
    </div>
</div>

<script src="<%=request.getContextPath() %>/js/merchant/myClass.js"></script>




