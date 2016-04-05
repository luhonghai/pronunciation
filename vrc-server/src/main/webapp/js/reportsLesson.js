function getDivSelectionFilter(){
    return $("#selection-filter");
}
function getProcessBar(){
    return $("#process-bar");
}

function listClasses(){
    $.ajax({
        url: "ReportsLessons",
        type: "POST",
        dataType: "json",
        data: {
            action: "listClass"
        },
        success: function (data) {
            if(data.message=="success"){
                $("#listClass").empty();
                if(data.list!=null && data.list.length>0){
                    var items=data.list;
                    $(items).each(function(){
                        $("#listClass").append('<option value="' + this.id + '">' + this.className + '</option>');
                    });
                }
                $('#listClass').multiselect('destroy');
                $('#listClass').multiselect({ enableFiltering: true, buttonWidth: '200px'});
                $('#listClass').multiselect('refresh');
            }else{
                swal("Error!", data.message.split(":")[1], "error");
            }
        },
        error: function () {
            swal("Error!", "Could not connect to server", "error");
        }

    });
}

function listStudents(idClass){
    $.ajax({
        url: "ReportsLessons",
        type: "POST",
        dataType: "json",
        data: {
            action: "listStudent",
            idClass : idClass
        },
        success: function (data) {
            if(data.message=="success"){
                $("#listUsers").empty();
                if(data.listStudent!=null && data.listStudent.length>0){
                    var items=data.listStudent;
                    $(items).each(function(){
                        $("#listUsers").append('<option value="' + this.studentName + '">' + this.studentName + '</option>');
                    });
                }
                $('#listUsers').multiselect('destroy');
                $('#listUsers').multiselect({ enableFiltering: true, buttonWidth: '200px'});
                $('#listUsers').multiselect('refresh');
            }else{
                swal("Error!", data.message.split(":")[1], "error");
            }
        },
        error: function () {
            swal("Error!", "Could not connect to server", "error");
        }

    });
}
function loadInfo(){
    $(document).on("click","#loadInfo",function(){
        var studentName=$('#listUsers option:selected').val();
        window.location =CONTEXT_PATH + "/reports-lessons.jsp?name="+studentName;
    })
}

$(document).ready(function(){
    $('#help-icons').show();
    loadInfo();
    listStudents();
});

