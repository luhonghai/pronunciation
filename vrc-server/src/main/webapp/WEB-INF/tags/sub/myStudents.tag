<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%String teacherName=session.getAttribute("username").toString();%>
<%String company=session.getAttribute("company").toString();%>
<div id="page-wrapper">
  <input type="hidden" id="teacher" value="<%=teacherName%>">
  <div class="row" style="color:lightgrey; margin-left: 0px;">
    <h3 style="float: left;"><%=company%></h3> <p style="margin-top: 25px;">> my students</p>
  </div>
  <div>
    <p style="font-size: 20px;">My students</p>
    <button id="inviteStudents" class="btn btn-default" style="background-color: orange;color: white;border-radius: 5px;"><img src=""> invite students <i class="fa fa-plus"></i> </button>
    <p>Use this page o manage the student that you want to assign to classes.</p>
    <p>Select the button above to invite students that have installed the application without a company licence.</p>

  </div>
  <div id="listMyStudent" style="overflow: auto; height: 350px;">

  </div>

</div>
<!-- /#wrapper -->

<div id="helpMyStudentModal" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <div class="row">
          <div class="col-xs-12 col-md-10 col-md-offset-1">

            <h1 align="center">My Students</h1>
            <p>This page displays a list of students that are available to add to your classes (added or invited by you) and students that have sent a request to join your classes.</p>
            <p>The students are colour coded as follows:</p>
            <img src="/images/teacher/student48x48.gif" style="width: 24px;height: 24px;background-color: #003366;"> <p>licensed by your institution</p>
            <img src="/images/teacher/student48x48.gif" style="width: 24px;height: 24px;background-color: #33ccff;"> <p>external (not licensed by your institution)</p>
            <img src="/images/teacher/student48x48.gif" style="width: 24px;height: 24px;background-color: #990099;"> <p>pending approval to join your class/s (select to approve or reject the request)</p>
            <p>External student have the following status indicators:</p>
            <img src=""> <p>invitation has been accepted, the student can be added to your classes.</p>
            <img src=""> <p>invitation has been rejected by the student and they are not available to add to your classes. Note: the invite is still available to accept on the student's phone</p>
            <img src=""> <p>your invitation is waiting for a response from the student</p>
            <p>If a student's licence key expires or if it is revoked, they will be removed from all lists and will no loger have access to any courses that have been shared with them.</p>
            <p> Select a student and follow the instructions to remove them from your list. Licenced students will still be available on your company list (students page) to add again Later. If you remove external students, the invitation process will need to be used.(Teacher to student or student to teacher)</p>


            <p>Any student that is removed from your 'my students' page will be removed from any classes they have been assigned to any they will no longer have access to any associated courses'.</p>
          </div>
        </div>
      </div>


    </div>
  </div>
</div>



<div id="inviteModal" class="modal fade">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <div class="row">
          <div class="col-xs-12 col-md-10 col-md-offset-1">

            <h1 align="center">Invite Students</h1>
            <p>Enter the email addresses for the students that you wish to add in a comma separated list</p>
            <form name="add" class="form-horizontal"
                  style="margin-top: 25px" id="addform">

              <div class="form-group">
                <label>email:</label>
                <textarea type="text" name="listmail" id="listmail" class="form-control" rows="10" required="required" placeholder="List Mail"></textarea>
              </div>
              <div class="modal-footer">

                <button type="button" name="help" id="helpInvite" class="btn btn-default"><img src="/images/popup/help.gif" style="width: 24px;height: 24px;"> </button>
                <button type="button" name="invite" id="invite" class="btn btn-default" value="yes" ><img src="/images/popup/save.gif" style="width: 24px;height: 24px;"> </button>

              </div>

            </form>
          </div>
        </div>
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

            <h1 align="center">Invite Students</h1>
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

            <h1 align="center">Invalid email address</h1>
            <p>Sorry, the following student/s were not found:</p>
            <div id="listMailError">

            </div>
            <p>Please correct them or delete from the list to continue.</p>

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

            <h1 align="center">Remove Students</h1>
            <u style="color: #0044cc;"> <p align="center" id="student"></p></u>
            <form name="add" class="form-horizontal"
                  style="margin-top: 25px">
                <input type="hidden" id="idStudent">
                <input type="hidden" id="studentName">
                <p>Do you wish to remove this student from yourlist?</p>
                <p>Select the 'delete' button to continue.</p>
                <p id="text"></p>
                <div class="modal-footer">
                  <img src="/images/popup/help.gif" id="helpRemove" style="width: 24px;height: 24px;">
                  <img src="/images/popup/trash.gif" id="removeStudent" style="width: 24px;height: 24px;">


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

            <h1 align="center">Remove Student</h1>
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

            <h1 align="center">Invitation from Student</h1>
            <u style="color: #0044cc;"> <p align="center" id="studentInvitation"></p></u>
            <form name="add" class="form-horizontal"
                  style="margin-top: 25px">

              <p>Select the 'accept' button to make the student available to add to your classes</p>
              <p>If you reject the request the student will be removed from your list on 'my detail'.</p>
              <div class="modal-footer">
                <button type="button" name="reject" id="reject" class="btn btn-default" ><img src="/images/popup/trash.gif" style="width: 24px;height: 24px;"> </button>
                <button type="button" name="accept" id="accept" class="btn btn-default" ><img src=""> </button>

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
            <h1 align="center">Confirm rejection</h1>
            <u style="color: #0044cc;"> <p align="center" id="studentconfirmReject"></p></u>
            <form name="add" class="form-horizontal"
                  style="margin-top: 25px">

              <p>Are you sure that you want to reject the request from this student?</p>
              <div class="modal-footer">
                <button type="button" name="cancel" id="cancel" class="btn btn-default" data-dismiss="modal" value="Close" >Cancel</button>
                <button type="button" name="reject" id="yesReject" class="btn btn-default" value="yes" ><img src=""> </button>

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
            <h1 align="center">Comfirm deletion</h1>
            <%--<u align="center" style="color: #003399;"><label id="studentconfirmRemove"></label></u>--%>
           <u style="color: #0044cc;"> <p align="center" id="studentconfirmRemove"></p></u>
            <form name="add" class="form-horizontal"
                  style="margin-top: 25px">
              <div class="modal-footer">
                <button type="button" name="cancel" id="cancelRemove" class="btn btn-default" data-dismiss="modal" value="Close" >Cancel</button>
                <button type="button" name="reject" id="yesRemove" class="btn btn-default" value="yes" ><img src=""> </button>

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

            <h1 align="center">Student Invitation</h1>
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

            <h1 align="center">Invitations rejected</h1>
            <p>the following student/s:</p>
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
            <h1 align="center">Invitation accept</h1>
            <p>The following student/s:</p>
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






<script src="<%=request.getContextPath() %>/js/myStudents.js"></script>




