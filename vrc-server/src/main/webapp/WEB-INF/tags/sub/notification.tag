
<div id="studentInvitationModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h2 class="modal-title">student invitation</h2>
            </div>
            <div class="modal-body">
                <p>The following students:</p>
                <div id="listStudentInvitation">
                </div>
                <p>Have sent a request to join your classes.</p>
            </div>
        </div>
    </div>
</div>


<div id="InvitationReject" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h2 class="modal-title">invitations rejected</h2>
            </div>
            <div class="modal-body">
                <p>the following students:</p>
                <div id="listInvitationReject">

                </div>
                <p>Have rejected your invitation.</p>
                <p>They can still accept it at a later date, but they will not be available to add to your classes until they do so.</p>
            </div>
        </div>
    </div>
</div>

<div id="InvitationAccept" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h2 class="modal-title">invitation accept</h2>
            </div>
            <div class="modal-body">
                <p>The following students:</p>
                <div id="listInvitationAccept">
                </div>
                <p>Have accept your invitation.</p>
                <p>They will now be available to add to your classes and you will be able to view reports on their progess.</p>
            </div>
        </div>
    </div>
</div>

<script src="<%=request.getContextPath() %>/js/merchant/notification.js"></script>