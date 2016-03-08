<%@ tag import="com.cmg.merchant.util.SessionUtil" %>
<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<% String company= StringUtil.isNull(session.getAttribute(SessionUtil.ATT_CPNAME),"").toString();%>
<div id="page-wrapper">
     <div class="row" style="color:lightgrey; margin-left: 0px;">
         <h3><%=company%></h3>
     </div>
    <div>
        <p>Welcome to the accenteasy teacher's console.</p>
        <p>Choose your option fro the menu to create, anage and share courses, set up student access and classes or to view reports</p>
    </div>
</div>
<!-- /#wrapper -->


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

<script src="<%=request.getContextPath() %>/js/welcomeTeacher.js"></script>




