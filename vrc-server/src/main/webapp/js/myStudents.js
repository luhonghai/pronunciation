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
                action: "closeAcccept"
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

function listMyStudent(){
    $.ajax({
        "url": "SendMailUser",
        "type": "POST",
        "dataType":"json",
        "data": {
            action: "listMyStudents"
        },
        success:function(data){
            if(data.message=="success" && data.students!=null){
                var listMyStudent=data.students;
                for(var i=0;i<listMyStudent.length;i++){
                    if(listMyStudent[i].licence==true){
                        $button = $('<div style="margin-top: 5px;cursor: pointer;" id="info"><p type="button" style="border-radius: 3px; display: inline;padding: 5px;background-color:#17375E;color:#ffffff;"><img src="/images/teacher/student48x48.gif" style="width: 24px;height: 24px;"> '+listMyStudent[i].studentName+'</p></div>');
                        $button.attr("id-column", listMyStudent[i].id);
                        $button.attr("licence", listMyStudent[i].licence);
                        $button.attr("status", listMyStudent[i].status);
                        $button.attr("mappingBy", listMyStudent[i].mappingBy);
                        $button.attr("studentName", listMyStudent[i].studentName);
                        $("#listMyStudent").append($button).html();

                    }else if(listMyStudent[i].status=='accept'){
                        $button = $('<div style="margin-top: 5px;cursor: pointer;" id="info"><p type="button" style="border-radius: 3px; display: inline;padding: 5px;background-color:#558ED5;color:#ffffff;"><img src="/images/teacher/student48x48.gif" style="width: 24px;height: 24px;"> '+listMyStudent[i].studentName+' <img src="/images/popup/accepted_48x48.gif" style="width: 18px;height: 18px;"></p></div>');
                        $button.attr("id-column", listMyStudent[i].id);
                        $button.attr("licence", listMyStudent[i].licence);
                        $button.attr("status", listMyStudent[i].status);
                        $button.attr("mappingBy", listMyStudent[i].mappingBy);
                        $button.attr("studentName", listMyStudent[i].studentName);
                        $("#listMyStudent").append($button).html();

                    }else if(listMyStudent[i].status=='reject'){
                        $button = $('<div style="margin-top: 5px;cursor: pointer;" id="info"><p type="button" style="border-radius: 3px; display: inline;padding: 5px;background-color:#558ED5;color:#ffffff;"><img src="/images/teacher/student48x48.gif" style="width: 24px;height: 24px;"> '+listMyStudent[i].studentName+' <img src="/images/teacher/rejected_48x48.gif" style="width: 18px;height: 18px;"></p></div>');
                        $button.attr("id-column", listMyStudent[i].id);
                        $button.attr("licence", listMyStudent[i].licence);
                        $button.attr("status", listMyStudent[i].status);
                        $button.attr("mappingBy", listMyStudent[i].mappingBy);
                        $button.attr("studentName", listMyStudent[i].studentName);
                        $("#listMyStudent").append($button).html();

                    }else if(listMyStudent[i].mappingBy=='teacher'){
                        $button = $('<div style="margin-top: 5px;cursor: pointer;" id="info"><p type="button" style="border-radius: 3px; display: inline;padding: 5px;background-color:#558ED5;color:#ffffff;"><img src="/images/teacher/student48x48.gif" style="width: 24px;height: 24px;"> '+listMyStudent[i].studentName+' <img src="/images/teacher/pending_invite_teacher2student_48x48.gif" style="width: 18px;height: 18px;"></p></div>');
                        $button.attr("id-column", listMyStudent[i].id);
                        $button.attr("licence", listMyStudent[i].licence);
                        $button.attr("status", listMyStudent[i].status);
                        $button.attr("mappingBy", listMyStudent[i].mappingBy);
                        $button.attr("studentName", listMyStudent[i].studentName);
                        $("#listMyStudent").append($button).html();

                    }else{
                        $button = $('<div style="margin-top: 5px; cursor: pointer;" id="info"><p type="button" style="border-radius: 3px; display: inline;padding: 5px;background-color:#7330A5;color:#ffffff;"><img src="/images/teacher/student48x48.gif" style="width: 24px;height: 24px;"> '+listMyStudent[i].studentName+' <img src="/images/teacher/pending_invite_48x48.gif" style="width: 18px;height: 18px;"></p></div>');
                        $button.attr("id-column", listMyStudent[i].id);
                        $button.attr("licence", listMyStudent[i].licence);
                        $button.attr("status", listMyStudent[i].status);
                        $button.attr("mappingBy", listMyStudent[i].mappingBy);
                        $button.attr("studentName", listMyStudent[i].studentName);
                        $("#listMyStudent").append($button).html();
                    }
                }
            }
        },
        error:function(e){
            swal("Error!", "Could not connect to server", "error");
        }

    });

}
function info(){
    $(document).on("click","#info",function() {
        var idd=$(this).attr('id-column');
        var licence=$(this).attr('licence');
        var status=$(this).attr('status');
        var mappingBy=$(this).attr('mappingBy');
        var studentName=$(this).attr('studentName');
        $("#student").text("");
        $("#studentInvitation").text("");
        if(licence=="true"){
            $("#text").html("This action will also remove the student from your classes and they will no longer have access to any associated courses.");
            $("#idStudent").val(idd);
            $("#studentName").val(studentName);
            $("#student").text(studentName);
            $("#remove").modal('show');
        }else{
            if(mappingBy=="student" && status=="pending"){
                $("#idStudent").val(idd);
                $("#studentInvitation").text(studentName);
                $("#invitationFromStudent").modal('show');

            }else{
                $("#text").html("This action will also remove the student from your classes and they will no longer have access to any associated courses. The invitation process will need to be used if you wish to add them again in the future.");
                $("#idStudent").val(idd);
                $("#studentName").val(studentName);
                $("#student").text(studentName);
                $("#remove").modal('show');
            }
        }

    });
}

function reject(){
    $(document).on("click","#reject",function() {
        var id=$("#idStudent").val();
        $("#idStudentReject").val(id);
        $("#confirmReject").modal('show');
    })
}

function rejects(){
    $(document).on("click","#yesReject",function() {
        var id=$("#idStudent").val();
        $.ajax({
            url: "SendMailUser",
            type: "POST",
            dataType: "text",
            data: {
                action: "rejectStudent",
                id:id
            },
            success: function (data) {
                if (data == "success") {
                    $("#invitationFromStudent").modal('hide');
                    ("#confirmReject").modal('hide');
                    $("#listMyStudent").empty();
                    listMyStudent();
                    swal("Success!", "Reject student success!", "success");
                }

            },
            error:function(e){
                swal("Error!", "Could not connect to server", "error");
            }

        })

    })
}

function accept(){
    $(document).on("click","#accept",function() {
        var id=$("#idStudent").val();
        $.ajax({
            url: "SendMailUser",
            type: "POST",
            dataType: "text",
            data: {
                action: "acceptStudent",
                id:id
            },
            success: function (data) {
                if (data == "success") {
                    $("#invitationFromStudent").modal('hide');
                    $("#listMyStudent").empty();
                    listMyStudent();
                    swal("Success!", "Accept student success!", "success");
                }

            },
            error:function(e){
                swal("Error!", "Could not connect to server", "error");
            }

        })

    })
}

function deleted(){
    $(document).on("click","#removeStudent",function() {
        var id=$("#idStudent").val();
        var name=$("#studentName").val();
        $("#idStudentRemove").val(id);
        $("#studentconfirmRemove").text(name);
        $("#confirmRemove").modal('show');
    })
}

function deleteStudent(){
    $(document).on("click","#yesRemove",function() {
        var id=$("#idStudentRemove").val();
        $.ajax({
            url: "SendMailUser",
            type: "POST",
            dataType: "text",
            data: {
                action: "deletedStudentInMyStudent",
                id:id
            },
            success: function (data) {
                if (data == "success") {
                    $("#remove").modal('hide');
                    $("#confirmRemove").modal('hide');
                    $("#listMyStudent").empty();
                    listMyStudent();
                    swal("Success!", "Delete student success!", "success");
                }

            },
            error:function(e){
                swal("Error!", "Could not connect to server", "error");
            }

        })


    });
}
function invite(){
    $(document).on("click","#inviteStudents",function() {
        $("#inviteModal").modal('show');
    });
}

function inviteStudents(){
    $(document).on("click","#invite",function(){
        $("#listMailError").empty();
        var mail=$("#listmail").val();
        var teacher=$("#teacher").val();
        var obj= {
            listmail : readListMail(mail),
            teacher : teacher
        }
        if(mail!=null && mail.length>0) {
            $.ajax({
                url: "SendMailUser",
                type: "POST",
                dataType: "json",
                data: {
                    action: "send",
                    listmail: JSON.stringify(obj)
                },
                success: function (data) {
                    if (data.message == "success") {
                        $("#listMyStudent").empty();
                        listMyStudent();
                        swal("Success!", "Send success!", "success");
                    }else{
                        var listMailError=data.mailError;
                        var listMailExist=data.mailExist;
                        if(listMailError!=null && listMailError.length>0 && listMailExist!=null && listMailExist.length>0){
                            var listError=listMailError.toString();
                            var listExist=listMailExist.toString();
                            $("#listMailError").append(listError);
                            $("#errorInviteModal").modal('show');
                            $("#listMailExist").append(listExist);
                            $("#existInviteModal").modal('show');
                            $("#inviteModal").modal('hide');
                            $("#listMyStudent").empty();
                            listMyStudent();
                        }else if(listMailError!=null && listMailError.length>0){
                            var listError=listMailError.toString();
                            $("#listMailError").append(listError);
                            $("#inviteModal").modal('hide');
                            $("#errorInviteModal").modal('show');
                            $("#listMyStudent").empty();
                            listMyStudent();
                        }else if(listMailExist!=null && listMailExist.length>0){
                            var listExist=listMailExist.toString();
                            $("#listMailExist").append(listExist);
                            $("#inviteModal").modal('hide');
                            $("#existInviteModal").modal('show');
                            $("#listMyStudent").empty();
                            listMyStudent();
                        }

                        //swal("Warning!", "User: "+listmail+" have on list your student", "warning");
                    }

                }

            });
        }else{
            swal("Warning!", "List email not null!", "warning");
        }


    });
}
function readListMail(txt) {
    if (txt == null || typeof txt == 'undefined' || txt.length == 0) return null;
    var data =  txt.split(',');
    var output = [];
    for (var i = 0; i < data.length; i++) {
        output.push(data[i]);

    }
    return output;
}



function helpInvite(){
    $(document).on("click","#helpInvite",function() {
        $("#helpInviteModal").modal('show');
    });
}
function helpRemove(){
    $(document).on("click","#helpRemove",function() {
        $("#helpRemoveModal").modal('show');
    });
}
function helpMyStudent(){
    $(document).on("click","#help-icons",function() {
        $("#helpMyStudentModal").modal('show');
    });
}
$(document).ready(function(){
    $('#help-icons').show();
    closeModalAccept();
    closeModalReject();
    closeModalInvitation();
    notification();
    info();
    rejects();
    accept();
    reject();
    helpMyStudent();
    helpRemove();
    helpInvite();
    invite();
    inviteStudents();
    deleted();
    deleteStudent();
    listMyStudent();
});
