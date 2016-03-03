

function listMyClasses(){
    $.ajax({
        url: "ClassServlet",
        type: "POST",
        dataType: "json",
        data: {
           action:"listMyClass"
        },
        success: function (data) {
            if(data.message=="success" && data.listclass!=null){
                var listMyClass=data.listclass;
                for(var i=0;i<listMyClass.length;i++){
                    $button = $('<button type="button"  style="display: block; margin-top: 5px;" id="info" class="btn btn-info btn-sm" title='+listMyClass[i].definition+'><img src="/images/teacher/my%20classes48x48.gif" style="width: 24px;height: 24px"> '+listMyClass[i].className+'</button>');
                    $button.attr("id-column", listMyClass[i].id);
                    $button.attr("className", listMyClass[i].className);
                    $button.attr("definition", listMyClass[i].definition);
                    $button.css({"background-color": "#003366","color":"#ffffff"});
                    $("#listMyClass").append($button).html();
                }
            }
        },
        error: function () {
            swal("Error!", "Could not connect to server", "error");
        }

    });

}

function openAdd(){
    $(document).on("click","#addClass", function(){
        $("#add").modal('show');
        $("#addClassName").val("");
        $("#addDefinition").val("");
        $.ajax({
            url: "ClassServlet",
            type: "POST",
            dataType: "json",
            data: {
                action: "openAdd"
            },
            success: function (data) {
                if(data.message=="success"){
                    $("#addCourses").empty();
                    if(data.courses!=null && data.courses.length>0){
                        var items=data.courses;
                        $(items).each(function(){
                            $("#addCourses").append('<option title="'+this.description+'" value="' + this.name + '">' + this.name + '</option>');
                        });
                    }
                    $('#addCourses').multiselect('destroy');
                    $('#addCourses').multiselect({ enableFiltering: true, buttonWidth: '200px'});
                    $('#addCourses').multiselect('refresh');

                    $("#addStudents").empty();
                    if(data.studentMappingTeachers!=null && data.studentMappingTeachers.length>0){
                        var items=data.studentMappingTeachers;
                        $(items).each(function(){
                            $("#addStudents").append('<option value="' + this.studentName + '">' + this.studentName + '</option>');
                        });
                    }
                    $('#addStudents').multiselect('destroy');
                    $('#addStudents').multiselect({ enableFiltering: true, buttonWidth: '200px'});
                    $('#addStudents').multiselect('refresh');
                }else{
                    swal("Error!", data.message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });


    });
}



function addClass(){
    $(document).on("click","#yesadd", function(){
        var courses=[];
        var students = [];
        $('#addCourses option:selected').map(function(a, item){ courses.push(item.value);});
        $('#addStudents option:selected').map(function(a, item){ students.push(item.value);});
        var dto={
            courses:courses,
            students:students
        }
        var valide=validateFormAdd();
        if(valide==true) {
            var classname = $("#addClassName").val();
            var definition = $("#addDefinition").val();
            $.ajax({
                url: "ClassServlet",
                type: "POST",
                dataType: "text",
                data: {
                    action: "addClass",
                    objDto: JSON.stringify(dto),
                    classname: classname,
                    definition: definition
                },
                success: function (data) {
                    if (data == "success") {
                        $("#add").modal('hide');
                        $("#listMyClass").empty();
                        listMyClasses();
                        swal("Success!", "Add class success.", "success");
                    }
                },
                error: function () {
                    swal("Error!", "Could not connect to server", "error");
                }

            });
        }

    });





}


function validateFormAdd(){
    var className=$.trim($("#addClassName").val());
    if(className == "" || className.length == 0 || typeof className ==='underfined'){
        $("#addClassNameExits").show();
        return false;
    }else{
        $("#addClassNameExits").hide();
    }

    return true;
}


function deletes(){
    $(document).on("click","#delete", function(){
        $("#deletes").modal('show');
        var idd=$(this).attr('id-column');
        $("#iddelete").val(idd);
    });
}

function deleteuser(){
    $(document).on("click","#deleteItems", function(){
        var id=$("#iddelete").val();
            $.ajax({
                url: "ClassServlet",
                type: "POST",
                dataType: "text",
                data: {
                    delete: "delete",
                    id: id
                },
                success: function (data) {
                    if (data == "success") {

                        $("tbody").html("");
                        $("#deletes").modal('hide');
                        swal("Success!", "Delete class success", "success");
                    }else{
                        $("#deletes").modal('hide');
                        swal({title: "Warning!", text: "This class has been already deleted!",   type: "warning",timer:"5000" });
                        location.reload();
                    }
                },
                error: function () {
                    swal("Error!", "Could not connect to server", "error");
                }

            });
});
}

function openEdit(){
    $(document).on("click","#edit", function() {
        $("#edits").modal('show');
        var idd = $(this).attr('id-column');
        var classname = $(this).attr('classname');
        var difinition=$(this).attr('definition');
        $("#idedit").val(idd);
        $("#editClassName").val(classname);
        $("#editDefinition").val(difinition);
        $.ajax({
            url: "ClassServlet",
            type: "POST",
            dataType: "json",
            data: {
                action: "openEdit",
                id:idd
            },
            success: function (data) {
                if(data.message=="success"){
                    $("#editCourses").empty();
                    if(data.courses!=null && data.courses.length>0){
                        var items=data.courses;
                        $(items).each(function(){
                            $("#editCourses").append('<option title="'+this.description+'" value="' + this.name + '">' + this.name + '</option>');
                        });
                    }
                    $('#editCourses').multiselect('destroy');
                    $('#editCourses').multiselect({ enableFiltering: true, buttonWidth: '200px'});
                    $('#editCourses').multiselect('refresh');

                    $("#editStudents").empty();
                    if(data.studentMappingTeachers!=null && data.studentMappingTeachers.length>0){
                        var items=data.studentMappingTeachers;
                        $(items).each(function(){
                            $("#editStudents").append('<option value="' + this.studentName + '">' + this.studentName + '</option>');
                        });
                    }
                    $('#editStudents').multiselect('destroy');
                    $('#editStudents').multiselect({ enableFiltering: true, buttonWidth: '200px'});
                    $('#editStudents').multiselect('refresh');
                }else{
                    swal("Error!", data.message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    });

}

function edituser(){
    $(document).on("click","#yesedit", function(){
            var id = $("#idedit").val();
            var difinition = $("#editDefinition").val();


            $.ajax({
                url: "ClassServlet",
                type: "POST",
                dataType: "text",
                data: {
                    edit: "edit",
                    id: id,
                    difinition: difinition
                },
                success: function (data) {
                    if (data == "success") {
                        $("tbody").html("");
                        $("#edits").modal('hide');
                        swal("Success!", "Update class success", "success");
                    }else{
                        $("#deletes").modal('hide');
                        swal({title: "Warning!", text: "This class has been already deleted!",   type: "warning",timer:"5000" });
                        location.reload();
                    }
                },
                error: function () {
                    swal("Error!", "Could not connect to server", "error");
                }

            });

    });
}
function helpMyClass(){
    $(document).on("click","#help-icons",function() {
        $("#helpMyClassModal").modal('show');
    });
}

$(document).ready(function(){
    $('#help-icons').show();
    openEdit();
    openAdd();
    addClass();
    helpMyClass();
    listMyClasses();
});

