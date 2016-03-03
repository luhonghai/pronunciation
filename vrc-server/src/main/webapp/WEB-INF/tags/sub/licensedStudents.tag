<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%String company=session.getAttribute("company").toString();%>
<div id="page-wrapper">
     <div class="row">
         <h3 style="float: left;"><%=company%></h3> <p style="margin-top: 25px;">> studnets</p>
         <a href="my-students.jsp" title="go to my students" style="float: right;"><img src="/images/teacher/my%20students48x48.gif" style="background-color: #00e6ac"></a>
     </div>
    <div>
        <p style="font-size: 20px;">Licensed students</p>
        <p>Select from the list of students that are licensed to your institution to add to 'my students'.</p>
        <p>(Click in the checkbox to add. Once added, they will need to be removed individually from your list on 'my students').</p>
        <p>Go to 'my students' to invite students that have installed the application directly (without a licence)</p>

    </div>
    <div id="students">
        <div>
            <input type="checkbox" id="checkAll" title="select all" style="width: 20px;height: 20px;">
            <i id="addStudents" class="fa fa-plus fa-2x" style="color: #008000;margin-left: 10px;display: inline;" title="Add student(s) to my students"></i>
        </div>

        <div id="listStudent">

        </div>
    </div>


</div>
<!-- /#wrapper -->
<div id="helpLicensedStudentModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <div class="row">
                    <div class="col-xs-12 col-md-10 col-md-offset-1">

                        <h1 align="center">licenced Students</h1>
                        <p>This page displays a list of all students that are associated with your institution with a licence key (by username).</p>
                        <p>You will need create your own subset of students on your 'my students' page to enable you to add them to classes.</p>
                        <p>Check the boxes next to the students you want to add to your classes.</p>
                        <p>Selecting students here will automatically update your list in 'my students'.</p>
                        <p>Once students have been added here, you will need to select them individually from your list on 'my students' if you wish to remove them.</p>
                        <p>To add students that did not install the app with a licence key, (installed directly from the Google Play store), you will need to invite them using the button on your 'my students' page.</p>
                        <p>If a student's licence key expires or it is revoked they will be removed from all lists and will no longer have access to any courses that have been shared with them.</p>
                    </div>
                </div>
            </div>


        </div>
    </div>
</div>
<script src="<%=request.getContextPath() %>/js/licensedStudents.js"></script>




