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
                var $button;
                for(var i=0;i<listMyStudent.length;i++){
                    if(listMyStudent[i].licence==true){
                        $button = $('<div class="col-sm-12" style="padding-bottom: 20px;">' +
                            '<a class="a-info-student" style="background-color:#17375E;">' +
                            '<img src="/images/teacher/student48x48.gif" style="width: 24px;height: 24px;"> '+
                            '<label class="studentMail">' + listMyStudent[i].studentName+'</label> '+'</a></div>');
                        $button.attr("id-column", listMyStudent[i].id);
                        $button.attr("licence", listMyStudent[i].licence);
                        $button.attr("status", listMyStudent[i].status);
                        $button.attr("mappingBy", listMyStudent[i].mappingBy);
                        $button.attr("studentName", listMyStudent[i].studentName);
                        $("#listMyStudent").append($button).html();

                    }else if(listMyStudent[i].status=='accept'){
                        $button = $('<div class="col-sm-12" style="padding-bottom: 20px;">' +
                            '<a class="a-info-student" style="background-color:#558ED5;">' +
                            '<img src="/images/teacher/student48x48.gif" style="width: 24px;height: 24px;"> '+
                            '<label class="studentMail">' + listMyStudent[i].studentName+'</label> ' +
                            '<img src="/images/popup/accepted_48x48.gif" style="width: 18px;height: 18px;"></a></div>');
                        $button.attr("id-column", listMyStudent[i].id);
                        $button.attr("licence", listMyStudent[i].licence);
                        $button.attr("status", listMyStudent[i].status);
                        $button.attr("mappingBy", listMyStudent[i].mappingBy);
                        $button.attr("studentName", listMyStudent[i].studentName);
                        $("#listMyStudent").append($button).html();

                    }else if(listMyStudent[i].status=='reject'){
                        $button = $('<div class="col-sm-12" style="padding-bottom: 20px;">' +
                            '<a class="a-info-student" style="background-color:#558ED5;">' +
                            '<img src="/images/teacher/student48x48.gif" style="width: 24px;height: 24px;"> '+
                            '<label class="studentMail">' + listMyStudent[i].studentName+'</label> ' +
                            '<img src="/images/teacher/rejected_48x48.gif" style="width: 18px;height: 18px;"></a></div>');
                        $button.attr("id-column", listMyStudent[i].id);
                        $button.attr("licence", listMyStudent[i].licence);
                        $button.attr("status", listMyStudent[i].status);
                        $button.attr("mappingBy", listMyStudent[i].mappingBy);
                        $button.attr("studentName", listMyStudent[i].studentName);
                        $("#listMyStudent").append($button).html();

                    }else if(listMyStudent[i].mappingBy=='teacher'){
                        $button = $('<div class="col-sm-12" style="padding-bottom: 20px;">' +
                            '<a class="a-info-student" style="background-color:#558ED5;">' +
                            '<img src="/images/teacher/student48x48.gif" style="width: 24px;height: 24px;"> '+
                            '<label class="studentMail">' + listMyStudent[i].studentName+'</label> ' +
                            '<img src="/images/teacher/pending_invite_teacher2student_48x48.gif" style="width: 18px;height: 18px;"></a></div>');
                        $button.attr("id-column", listMyStudent[i].id);
                        $button.attr("licence", listMyStudent[i].licence);
                        $button.attr("status", listMyStudent[i].status);
                        $button.attr("mappingBy", listMyStudent[i].mappingBy);
                        $button.attr("studentName", listMyStudent[i].studentName);
                        $("#listMyStudent").append($button).html();

                    }else{
                        $button = $('<div class="col-sm-12" style="padding-bottom: 20px;">' +
                            '<a class="a-info-student" style="background-color:#7330A5">' +
                            '<img src="/images/teacher/student48x48.gif" style="width: 24px;height: 24px;"> '+
                             '<label class="studentMail">' + listMyStudent[i].studentName+'</label> ' +
                            '<img src="/images/teacher/pending_invite_48x48.gif" style="width: 18px;height: 18px;"></a>' +
                            '</div>');
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
            swalNew("", "could not connect to server", "error");
        }

    });

}
function info(){
    $(document).on("click",".a-info-student",function() {
        var idd=$(this).parent().attr('id-column');
        var licence=$(this).parent().attr('licence');
        var status=$(this).parent().attr('status');
        var mappingBy=$(this).parent().attr('mappingBy');
        var studentName=$(this).parent().attr('studentName');
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
                    swalNew("", "rejected student successfully", "success");
                }

            },
            error:function(e){
                swalNew("", "could not connect to server", "error");
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
                    swalNew("", "accepted student successfully", "success");
                }

            },
            error:function(e){
                swalNew("", "could not connect to server", "error");
            }

        })

    })
}

function deleted(){
    $(document).on("click","#removeStudent",function() {
        var id = $("#idStudent").val();
        var name=$("#studentName").val();
        $("#idStudentRemove").val(id);
        $("#studentconfirmRemove").text(name);
        $("#studentconfirmRemove").attr("id-remove",id);
        $("#confirmRemove").modal('show');
    })
}

function deleteStudent(){
    $(document).on("click","#yesRemove",function() {
        var id = $("#studentconfirmRemove").attr("id-remove");
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
                    swalNew("", "deleted student successfully", "success");
                }else{
                    $("#remove").modal('hide');
                    $("#confirmRemove").modal('hide');
                    $("#listMyStudent").empty();
                    swalNew("", "deleted student fail", "success");
                }

            },
            error:function(e){
                swalNew("", "could not connect to server", "error");
            }

        })


    });
}
function invite(){
    $(document).on("click","#inviteStudents",function() {
        $("#inviteModal").find("#validateLvMsg").hide();
        $("#inviteModal").modal('show');
        $("#listmail").val("");
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
                        swalNew("", "invited student successfully", "success");
                        $("#inviteModal").modal('hide');
                    }else{
                        var listMailError=data.mailError;
                        if(listMailError!=null && listMailError.length>0){
                            var listError=listMailError.toString();
                            $("#listMailError").html("");
                            $("#listMailError").html(listError);
                            $("#errorInviteModal").modal('show');
                            $("#listMyStudent").empty();
                            listMyStudent();
                        }
                    }
                }
            });
        }else{
            $("#inviteModal").find("#validateLvMsg").html("please enter at least one email address");
            $("#inviteModal").find("#validateLvMsg").show();
        }


    });
}
function readListMail(txt) {
    if (txt == null || typeof txt == 'undefined' || txt.length == 0) return null;
    var data =  txt.split(',');
    var output = [];
    for (var i = 0; i < data.length; i++) {
        if(data[i].length > 0 && data[i]!="" && typeof data[i] !== 'undefined'){
            output.push(data[i]);
        }
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

function closeConfirmPopUp(){
    $(document).on("click",".cancelLbl",function() {
        var popup = $(this).attr('popup-id');
        $('#'+popup).modal('hide');
    });
}

function collapseMenu(){
    /*$("#li-students").find('ul').addClass('in');*/
}
$(document).ready(function(){
    $('#help-icons').show();
    collapseMenu();
    closeConfirmPopUp();
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

