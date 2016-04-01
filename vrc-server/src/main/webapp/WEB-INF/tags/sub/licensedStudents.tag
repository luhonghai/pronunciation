<%@ tag import="com.cmg.merchant.util.SessionUtil" %>
<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%String company= StringUtil.isNull(session.getAttribute(SessionUtil.ATT_CPNAME),"").toString();%>
<style>
    .header-company {
        color: #A6A6A6;
        font-weight: 200;
        font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
        font-size: 14px;
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
    #helpLicensedStudentModal{
        color : #957F7F;
    }
    #helpLicensedStudentModal p{
        color : #957F7F !important;
    }

    #checkAllModal{
        color : #957F7F;
    }
    #checkAllModal p{
        color : #957F7F !important;
    }

</style>
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h4 style="border-bottom: transparent" class="header-company">
                <%=company%> > students
            </h4>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-12">
            <h2 class="header">licensed students</h2>
            <p>Select from the list of students that are licensed to your institution to add to 'my students'.</p>
            <p>(Click in the checkbox to add. Once added, they will need to be removed individually from your list on 'my students').</p>
            <p>Go to 'my students' to invite students that have installed the application directly (without a licence)</p>
        </div>
    </div>
    <div class="row" id="students">
        <div class="col-sm-12">
            <input type="checkbox" id="checkAll" title="select all" style="width: 20px;height: 20px;">
            <i id="addStudents" class="fa fa-plus fa-2x" style="color: #008000;margin-left: 10px;display: inline;" title="Add student(s) to my students"></i>
        </div>
        <div class="col-sm-12" id="listStudent">

        </div>
    </div>
    <div style="padding : 20px 0px 10px 0px" class="row">
        <a href="my-students.jsp" title="go to my students" style="float: right;"><img src="/images/teacher/my%20students48x48.gif" style="background-color: #00e6ac"></a>
    </div>


</div>
<!-- /#wrapper -->
<div id="helpLicensedStudentModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content" style="border-radius:20px">
            <div class="modal-header" style="border-bottom: transparent;padding-bottom: 0px;text-align: center">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h2 class="modal-title" style="font-weight: 700;">licenced students</h2>
            </div>
            <div class="modal-body">
                <div>
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

<div id="checkAllModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header" style="border-bottom: transparent;>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <div>
                    <p>Do you really want to add all licenced students to 'my students' You will need to remove them individually from your list after they have been added?</p>
                </div>
            </div>

        </div>
    </div>
</div>

<script src="<%=request.getContextPath() %>/js/merchant/licensedStudents.js"></script>




