

function listLicensedStudent(){
    $.ajax({
        "url": "SendMailUser",
        "type": "POST",
        "dataType":"json",
        "data": {
            action: "listLicensedStudents"
        },
        success:function(data){
            if(data.message=="success"){
                var listStudent=data.students;
                for(var i=0;i<listStudent.length;i++){
                   // $("#listStudent").append('<input type="checkbox" id="check" id-column='+listStudent[i].id+'>');
                    $("#listStudent").append('<div style="display: block; margin-top: 5px;"><input type="checkbox" id="check" style="width: 20px;height: 20px;" id-column='+listStudent[i].id+'> <button class="btn btn-default" style="background-color: #003366;color: #ffffff;border-radius: 5px;"><img src="/images/teacher/student48x48.gif" style="width: 24px;height: 24px;"> '+listStudent[i].studentName+'</button></div>');
                }
            }
        },
        error:function(e){
            swal("Error!", "Could not connect to server", "error");
        }

    });
}


function addStudent(){
    $(document).on("click","#addStudents",function(){
        var listStudent = [];
        $("input[type=checkbox]:checked").each(function(i){
            listStudent[i] = $(this).attr('id-column');
        });

        var obj={
            listStudent:listStudent
        }
        if(listStudent!=null && listStudent.length>0) {
            $.ajax({
                "url": "SendMailUser",
                "type": "POST",
                "dataType": "text",
                "data": {
                    action: "addStudents",
                    listStudent: JSON.stringify(obj)
                },
                success: function (data) {
                    if (data == "success") {
                        $("#listStudent").empty();
                        listLicensedStudent();
                        swal("Success!", "Add success", "success");
                    }
                },
                error: function (e) {
                    swal("Error!", "Could not connect to server", "error");
                }

            });
        }
    })

}

function helpLicenceStudent(){
    $(document).on("click","#help-icons",function() {
        $("#helpLicensedStudentModal").modal('show');
    });
}


$(document).ready(function(){
    $('#checkAll').click(function (e) {
        if ($(this).is(':checked')) {
            $("#checkAllModal").modal('show');
        }
        $('input:checkbox').prop('checked', this.checked);
    });
    $('#help-icons').show();
    helpLicenceStudent();
    addStudent();
    listLicensedStudent();
});

