<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%String company=session.getAttribute("company").toString();%>
<div id="page-wrapper">
     <div class="row" style="margin-top: 20px;">
         <h3><%=company%></h3>
     </div>
    <p style="font-size: 20px;">my classes</p>
    <button id="addClass">add class<i class="fa fa-plus"></i></button>
    <div>
        <p>Select the button above to create a new class.</p>
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

                        <h1 align="center">My classes</h1>
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
                <div class="row">
                    <div class="col-xs-12 col-md-10 col-md-offset-1">

                        <h1 align="center">Add Class</h1>
                        <form name="add" class="form-horizontal"
                              style="margin-top: 25px" id="addform">

                            <div class="form-group">
                                <p id="addClassNameExits" class="addClassNameExits" style="color:red;margin-left:50px;display: none;">Class name exits</p>
                                <label class="col-xs-4  col-sm-3 control-label ">name:</label>
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
                                <label class="col-xs-4  col-sm-3 control-label ">add/remove courses:</label>
                                <div class="col-xs-8  col-sm-9">

                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-4  col-sm-3 control-label ">add/remove students:</label>
                                <div class="col-xs-8  col-sm-9">

                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" name="closeadd" id="closeadd" class="btn btn-default" data-dismiss="modal" value="Close" >Close</button>
                                <button type="button" name="yesadd" id="yesadd" class="btn btn-default" value="yes" >Yes</button>

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
                <div class="row">
                    <div class="col-xs-12 col-md-10 col-md-offset-1">

                        <h1 align="center">Edit Class</h1>
                        <form name="Edit" class="form-horizontal"
                              style="margin-top: 25px" id="editform">
                            <input type="hidden" id="idedit" name="idedit">

                            <div class="form-group">
                                <label class="col-xs-4  col-sm-3 control-label ">Class Name:</label>
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
                                <label class="col-xs-4  col-sm-3 control-label ">add/remove courses:</label>
                                <div class="col-xs-8  col-sm-9">

                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-xs-4  col-sm-3 control-label ">add/remove students:</label>
                                <div class="col-xs-8  col-sm-9">

                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" name="closeedit" id="closeedit" class="btn btn-default" data-dismiss="modal" value="Close" >Close</button>
                                <button type="button" name="delete" id="delete" class="btn btn-default" value="yes" >Yes</button>
                                <button type="button" name="yesedit" id="yesedit" class="btn btn-default" value="yes" >Yes</button>

                            </div>

                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


<script src="<%=request.getContextPath() %>/js/class.js"></script>




