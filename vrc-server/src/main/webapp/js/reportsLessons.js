
function listStudents(){
        $.ajax({
            url: "ReportsLessons",
            type: "POST",
            dataType: "json",
            data: {
                action: "listStudent"
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
        $.ajax({
            url: "ReportsLessons",
            type: "POST",
            dataType: "json",
            data: {
                action: "loadInfo",
                studentName:studentName
            },
            success: function (data) {
                if(data.message=="success"){

                }else{
                    swal("Error!", data.message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    })
}

$(document).ready(function(){
    $('#help-icons').show();
    listStudents();
});

