/**
 * Created by lantb on 2016-04-04.
 */
function notification(){
    $.ajax({
        "url": "NotificationServlet",
        "type": "POST",
        "dataType": "json",
        "data": {
            action: "notification"
        },
        success: function (data) {
            if(data!=null){
                var accept=data.accept;
                var reject=data.reject;
                var invitation=data.invitation;
                if(accept!=null && accept.length>0){
                    for(var i=0;i<accept.length;i++){
                        $("#listInvitationAccept").append('<p style="color: blue"><u>'+accept[i].studentName+'</u></p>');
                    }
                    $("#InvitationAccept").modal('show');
                }

                if(reject!=null && reject.length>0){
                    for(var i=0;i<reject.length;i++){
                        $("#listInvitationReject").append('<p style="color: blue"><u>'+reject[i].studentName+'</u></p>');
                    }
                    $("#InvitationReject").modal('show');
                }

                if(invitation!=null && invitation.length>0){
                    for(var i=0;i<invitation.length;i++){
                        $("#listStudentInvitation").append('<p style="color: blue"><u>'+invitation[i].studentName+'</u></p>');
                    }
                    $("#studentInvitationModal").modal('show');
                }


            }

        }
    });
}
function closeModalAccept(){
    $('#InvitationAccept').on('hidden.bs.modal', function () {
        $.ajax({
            "url": "NotificationServlet",
            "type": "POST",
            "dataType": "json",
            "data": {
                action: "closeAccept"
            },
            success: function (data) {

            }
        });
    });
}
function closeModalReject(){
    $('#InvitationReject').on('hidden.bs.modal', function () {
        $.ajax({
            "url": "NotificationServlet",
            "type": "POST",
            "dataType": "json",
            "data": {
                action: "closeReject"
            },
            success: function (data) {

            }
        });
    });
}
function closeModalInvitation(){
    $('#studentInvitationModal').on('hidden.bs.modal', function () {
        $.ajax({
            "url": "NotificationServlet",
            "type": "POST",
            "dataType": "json",
            "data": {
                action: "closeInvitation"
            },
            success: function (data) {

            }
        });
    });
}
function closeConfirmPopUp(){
    $(document).on("click",".cancelLbl",function() {
        var popup = $(this).attr('popup-id');
        $('#'+popup).modal('hide');
    });
}
$(document).ready(function(){
    closeConfirmPopUp();
    closeModalAccept();
    closeModalReject();
    closeModalInvitation();
    notification();
});