function getDivSelectionFilter(){
    return $("#selection-filter");
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
                    var items=data.list;
                    $(items).each(function(){
                        $("#listClass").append('<option title="'+this.className+'" value="' + this.id + '">' + this.className + '</option>');
                    });
                }
                $('#listClass').multiselect('destroy');
                $('#listClass').multiselect({ enableFiltering: true, buttonWidth: '200px'});
                $('#listClass').multiselect('refresh');
                $('#listClass').multiselect({
                    onChange: function(element, checked) {
                        console.log("on change value");
                        if (checked === true) {
                            listStudents(element.val());
                        }
                    }
                });
                $('#listClass').multiselect('refresh');
                listStudents(items[0].id);
            }else{
                swal("", "an error has been occurred in server", "error");
            }
        },
        error: function () {
            swal("", "Could not connect to server", "error");
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
                    $(items).each(function(){
                        $("#listUsers").append('<option value="' + this.studentName + '">' + this.studentName + '</option>');
                    });
                }
                $('#listUsers').multiselect('destroy');
                $('#listUsers').multiselect({ enableFiltering: true, buttonWidth: '200px'});
                $('#listUsers').multiselect('refresh');
            }else{
                swal("", "an error has been occurred in server", "error");
            }
        },
        error: function () {
            swal("", "Could not connect to server", "error");
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
        var studentName=$('#listUsers option:selected').val();
        window.location =CONTEXT_PATH + "/reports-lessons.jsp?name="+studentName;
    })
}

$(document).ready(function(){
    $('#help-icons').show();
    onChangeClass();
    loadInfo();
    listClasses();

});

