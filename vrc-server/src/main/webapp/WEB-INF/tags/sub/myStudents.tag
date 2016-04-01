<%@ tag import="com.cmg.merchant.util.SessionUtil" %>
<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%String teacherName=session.getAttribute("username").toString();%>
<%String company= StringUtil.isNull(session.getAttribute(SessionUtil.ATT_CPNAME), "").toString();%>
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
  #helpMyStudentModal{
    color : #957F7F;
  }
  #helpMyStudentModal p{
    color : #957F7F !important;
  }

  #inviteModal .modal-content{
    width: 400px;
    border-radius: 20px;
  }
  #inviteModal .modal-header {
    border-bottom: transparent;
    padding-bottom: 0px;
  }

  #inviteModal #titleInvite {
    font-weight: 600;
    font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
    font-size: 16px;
    text-align: center;
  }

  #inviteModal .validateMsg {
    font-weight: 200;
    font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
    font-size: 16px;
    color: red;
  }
  #inviteModal label {
    font-weight: 200;
    font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
    font-size: 14px;
    text-align: left;
  }
</style>
<div id="page-wrapper">
  <input type="hidden" id="teacher" value="<%=teacherName%>">
  <div class="row">
    <div class="col-lg-12">
      <h4 style="border-bottom: transparent" class="header-company">
          <%=company%> > my students
      </h4>
    </div>
  </div>
  <div class="row">
    <div class="col-lg-12">
      <h2 class="header">My students</h2>
    </div>
    <div style="padding-bottom: 10px" class="col-sm-12">
      <button id="inviteStudents" class="btn btn-default" style="background-color: orange;color: white;border-radius: 3px; padding: 1px 5px;"><img src="/images/teacher/invite_students_48x48.gif" style="width: 24px;height: 24px;"> invite students <i class="fa fa-plus"></i> </button>
    </div>
    <div class="col-sm-12">
      <p>Use this page o manage the student that you want to assign to classes.</p>
      <p>Select the button above to invite students that have installed the application without a company licence.</p>
    </div>
  </div>
  <div class="row">
    <div class="col-sm-12" id="listMyStudent">

    </div>
  </div>
</div>
<!-- /#wrapper -->

<div id="helpMyStudentModal" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content"  style="border-radius:20px">
      <div class="modal-header"  style="border-bottom: transparent;padding-bottom: 0px;text-align: center">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h2 class="modal-title" style="font-weight: 700;">my students</h2>

      </div>
      <div class="modal-body">
        <div>
          <p>This page displays a list of students that are available to add to your classes (added or invited by you) and students that have sent a request to join your classes.</p>
          <p>The students are colour coded as follows:</p>
          <div  style="margin-bottom: 5px;">
            <img src="/images/teacher/student48x48.gif" style="width: 24px;height: 24px;background-color: #003366;"> <p style="display: inline;">licensed by your institution</p>
          </div>
          <div style="margin-bottom: 5px;">
            <img src="/images/teacher/student48x48.gif" style="width: 24px;height: 24px;background-color: #33ccff;"> <p style="display: inline;">external (not licensed by your institution)</p>
          </div>
          <div style="margin-bottom: 5px;">
            <img src="/images/teacher/student48x48.gif" style="width: 24px;height: 24px;background-color: #990099;"> <p style="display: inline;">pending approval to join your class/s (select to approve or reject the request)</p>
          </div>
          <div>
            <p>External student have the following status indicators:</p>
          </div>
          <div style="margin-bottom: 5px;">
            <img src="/images/popup/accepted_48x48.gif" style="width: 24px;height: 24px;background-color:#33ccff;"> <p style="display: inline;">invitation has been accepted, the student can be added to your classes.</p>
          </div>
          <div style="margin-bottom: 5px;">
            <img src="/images/teacher/rejected_48x48.gif" style="width: 24px;height: 24px;background-color: #33ccff;"> <p style="display: inline;">invitation has been rejected by the student and they are not available to add to your classes. Note: the invite is still available to accept on the student's phone</p>
          </div>
          <div style="margin-bottom: 5px;">
            <img src="/images/teacher/pending_invite_teacher2student_48x48.gif" style="width: 24px;height: 24px;background-color: #33ccff;"> <p style="display: inline;">your invitation is waiting for a response from the student</p>
          </div>
          <p>If a student's licence key expires or if it is revoked, they will be removed from all lists and will no loger have access to any courses that have been shared with them.</p>
          <p> Select a student and follow the instructions to remove them from your list. Licenced students will still be available on your company list (students page) to add again Later. If you remove external students, the invitation process will need to be used.(Teacher to student or student to teacher)</p>
          <p>Any student that is removed from your 'my students' page will be removed from any classes they have been assigned to any they will no longer have access to any associated courses'.</p>
        </div>
      </div>
    </div>
  </div>
</div>



<div id="inviteModal" class="modal fade">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h2 id="titleInvite" class="modal-title">invite students</h2>
        <h4 id="validateLvMsg" class="modal-title validateMsg" style="text-align: center;font-weight: 200;
        color:red;display:none;"></h4>
      </div>
      <div class="modal-body">
          <form name="add" class="form-horizontal">
            <div class="form-group">
              <label>Enter the email addresses for the students that you wish to add in a comma separated list</label>
            </div>
            <div class="form-group">
              <label class="control-label col-md-2">email:</label>
              <div class="col-md-10">
                <textarea type="text" name="listmail" id="listmail" class="form-control" rows="10" required="required" placeholder="List Mail"></textarea>
              </div>
            </div>
            <div class="form-group contain-button">
              <div class="col-md-6">
                <img id="helpAddLevel" class="helpInvite" src="/images/popup/help_50_50.png" width="36px" height="36px">
              </div>
              <div class="col-md-6">
                <img id="invite" style="float: right" src="/images/popup/Save_50x50.gif" width="36px" height="36px">
              </div>
            </div>
          </form>
      </div>
    </div>
  </div>
</div>


<div id="helpInviteModal" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <div class="row">
          <div class="col-xs-12 col-md-10 col-md-offset-1">

            <h3 align="center">invite students</h3>
            <p>Enter the email addresses of the students that you wish to add to your classes separated by a comma in the text box.</p>
            <p>e.g.<u style="color: #0044cc;">a.name@gmail.com</u>, <u style="color: #0044cc;">another.name@btinternet.com</u>, <u style="color: #0044cc;">third.name@tiscati.co.uk</u></p>
            <p>You can leave spaces in between the comma and the next name or you can add them as a list.</p>
            <p>e.g.</p>
            <p><u style="color: #0044cc;">a.name@gmail.com</u>,</p>
            <p><u style="color: #0044cc;">another.name@btinternet.com</u>,</p>
            <p><u style="color: #0044cc;">third.name@tiscati.co.uk</u></p>
            <p>If you do not enter one or more email address in a valid format, you will see an error message and the appropriate addresses will need to be changed or deleted before you can save.</p>
            <p>When you select save, a message will be sent to the student asking them to comfirm that they wish to coonect with you.</p>
          </div>
        </div>
      </div>


    </div>
  </div>
</div>


<div id="errorInviteModal" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <div class="row">
          <div class="col-xs-12 col-md-10 col-md-offset-1">

            <h3 align="center" style="color: red;">invalid email address</h3>
            <p>Sorry, the following student/s were not found:</p>
            <div id="listMailError" style="color: red;">

            </div>
            <p>Please correct them or delete from the list to continue.</p>

          </div>
        </div>
      </div>


    </div>
  </div>
</div>

<div id="existInviteModal" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <div class="row">
          <div class="col-xs-12 col-md-10 col-md-offset-1">

            <h3 align="center">invalid email address</h3>
            <p>Sorry, the following student/s were exist list your student:</p>
            <div id="listMailExist" style="color: orange;">

            </div>
          </div>
        </div>
      </div>


    </div>
  </div>
</div>


<div id="remove" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <div class="row">
          <div class="col-xs-12 col-md-10 col-md-offset-1">

            <h3 align="center">remove students</h3>
            <u style="color: #0044cc;"> <p align="center" id="student"></p></u>
            <form name="add" class="form-horizontal"
                  style="margin-top: 25px">
                <input type="hidden" id="idStudent">
                <input type="hidden" id="studentName">
                <p>Do you wish to remove this student from yourlist?</p>
                <p>Select the 'delete' button to continue.</p>
                <p id="text"></p>
                <div>
                  <img src="/images/popup/Help_50x50.gif" id="helpRemove" style="float: left;">
                  <img src="/images/popup/trash_50x50.gif" id="removeStudent" title="delete" style="width:50px;height: 50px; float: right;">
                </div>
            </form>
          </div>
        </div>
      </div>


    </div>
  </div>
</div>
<div id="helpRemoveModal" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <div class="row">
          <div class="col-xs-12 col-md-10 col-md-offset-1">

            <h3 align="center">remove student</h3>
            <p>if you remove a licenced student from your 'my students' page, they will still be available on your company list (licensed students page) to add again later.</p>
            <p>If you remove and external student, the invitation process will need to be used to add them again. (Teacher to student or student to teacher).</p>
            <p>Any student that is removed from your 'my students' page will be removed from any classes they have been assigned to any they will no longer have access to any associated courses'.</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<div id="invitationFromStudent" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <div class="row">
          <div class="col-xs-12 col-md-10 col-md-offset-1">

            <h3 align="center">invitation from student</h3>
            <u style="color: #0044cc;"> <p align="center" id="studentInvitation"></p></u>
            <form name="add" class="form-horizontal"
                  style="margin-top: 25px">

              <p>Select the 'accept' button to make the student available to add to your classes</p>
              <p>If you reject the request the student will be removed from your list on 'my detail'.</p>
              <div>
                <img src="/images/popup/trash_50x50.gif" id="reject" title="reject" style="float: left;width: 50px;height: 50px;">
                <span type="button" title="accept" id="accept" style="color:green; float: right;" class="fa fa-check-circle fa-4x"> </span>
              </div>
            </form>
          </div>
        </div>
      </div>


    </div>
  </div>
</div>

<div id="confirmReject" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <div class="row">
          <div class="col-xs-12 col-md-10 col-md-offset-1">
            <input type="hidden" id="idStudentReject">
            <h3 align="center">confirm rejection</h3>
            <u style="color: #0044cc;"> <p align="center" id="studentconfirmReject"></p></u>
            <form name="add" class="form-horizontal"
                  style="margin-top: 25px">

              <p>Are you sure that you want to reject the request from this student?</p>
              <div>
                <p id="cancel" style="float:left;cursor: pointer;"><u>cancel</u></p>
                <span type="button" id="yesReject" style="color:green;float: right;" class="fa fa-check-circle fa-4x"> </span>
              </div>
            </form>
          </div>
        </div>
      </div>


    </div>
  </div>
</div>

<div id="confirmRemove" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <div class="row">
          <div class="col-xs-12 col-md-10 col-md-offset-1">
            <input type="hidden" id="idStudentRemove">
            <h3 align="center">comfirm deletion</h3>
            <%--<u align="center" style="color: #003399;"><label id="studentconfirmRemove"></label></u>--%>
           <u style="color: #0044cc;"> <p align="center" id="studentconfirmRemove"></p></u>
            <form name="add" class="form-horizontal"
                  style="margin-top: 25px">
              <div>
                <p id="cancelRemove" style="float:left;cursor: pointer;"><u>cancel</u></p>
                <span type="button" id="yesRemove" style="color:green;float: right;" class="fa fa-check-circle fa-4x"> </span>
              </div>
            </form>
          </div>
        </div>
      </div>


    </div>
  </div>
</div>


<div id="studentInvitationModal" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <div class="row">
          <div class="col-xs-12 col-md-10 col-md-offset-1">

            <h3 align="center">student invitation</h3>
            <p>The following students:</p>
            <div id="listStudentInvitation">

            </div>
            <p>Have sent a request to join your classes.</p>
             </div>
        </div>
      </div>
    </div>
  </div>
</div>


<div id="InvitationReject" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <div class="row">
          <div class="col-xs-12 col-md-10 col-md-offset-1">

            <h3 align="center">invitations rejected</h3>
            <p>the following students:</p>
            <div id="listInvitationReject">

            </div>
            <p>Have rejected your invitation.</p>
            <p>They can still accept it at a later date, but they will not be available to add to your classes until they do so.</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<div id="InvitationAccept" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <div class="row">
          <div class="col-xs-12 col-md-10 col-md-offset-1">
            <h3 align="center">invitation accept</h3>
            <p>The following students:</p>
            <div id="listInvitationAccept">

            </div>
            <p>Have accept your invitation.</p>
            <p>They will now be available to add to your classes and you will be able to view reports on their progess.</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>






<script src="<%=request.getContextPath() %>/js/merchant/myStudents.js"></script>




