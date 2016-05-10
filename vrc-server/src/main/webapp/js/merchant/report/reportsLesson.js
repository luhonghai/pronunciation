function getDivSelectionFilter(){
    return $("#selection-filter");
}
function getDivNotification(){
    return $("#notification");
}
function getProcessBar(){
    return $("#process-bar");
}
function btnLoadInfo(){
    return $("#loadInfo");
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
                    getDivNotification().hide();
                    getDivSelectionFilter().show();
                    var items=data.list;
                    listStudents(items[0].id);
                    $(items).each(function(){
                        $("#listClass").append('<option title="'+this.className+'" value="' + this.id + '">' + this.className + '</option>');
                    });
                }else{
                    getDivSelectionFilter().hide();
                    getDivNotification().show();
                }
                $('#listClass').multiselect('destroy');
                $('#listClass').multiselect({ enableFiltering: true, maxHeight: 200,buttonWidth: '200px'});
                $('#listClass').multiselect('refresh');
            }else{
                getDivSelectionFilter().hide();
                getDivNotification().show();
                //swalNew("", "an error has been occurred in server", "error");
            }
        },
        error: function () {
            swalNew("", "could not connect to server", "error");
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
                if(data.listSMC!=null && data.listSMC.length>0){
                    var items=data.listSMC;
                    btnLoadInfo().removeAttr("disabled");
                    $(items).each(function(){
                        $("#listUsers").append('<option value="' + this.studentName + '">' + this.studentName + '</option>');
                    });
                }else{
                    btnLoadInfo().attr("disabled","disabled");
                    btnLoadInfo().css("opacity","0.65");
                }
                $('#listUsers').multiselect('destroy');
                $('#listUsers').multiselect({ enableFiltering: true,maxHeight: 200, buttonWidth: '200px'});
                $('#listUsers').multiselect('refresh');
            }else{
                swalNew("", "an error has been occurred in server", "error");
            }
        },
        error: function () {
            swalNew("", "could not connect to server", "error");
        }

    });
}

function onChangeClass(){
    $('#listClass').on('change', function(){
        var idClass=$('#listClass option:selected').val();
        listStudents(idClass);
    });
}
function loadInfo(){
    $(document).on("click","#loadInfo",function(){
        if($(this).attr("disabled")!="disabled"){
            var studentName=$('#listUsers option:selected').val();
            var idClass=$('#listClass option:selected').val();
            var nameClass= $('#listClass option:selected').text();
            window.location = CONTEXT_PATH + "/reports-lessons.jsp?name="+studentName +"&idClass="+idClass;
        }
    })
}
function collapseMenu(){
    $("#li-reports").find('ul').addClass('in');
}
function help(){
    $('#help-icons').show();
    $(document).on("click","#help-icons",function() {
        $("#helpReportModal").modal('show');
    });
}
$(document).ready(function(){
    help();
    onChangeClass();
    loadInfo();
    listClasses();
    collapseMenu();
});