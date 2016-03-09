

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
                    var definition=listMyClass[i].definition;
                    $button = $('<button type="button"  style="display: block; margin-top: 5px;" id="info" class="btn btn-info btn-sm" title="'+definition+'"><img src="/images/teacher/my%20classes48x48.gif" style="width: 24px;height: 24px"> '+listMyClass[i].className+'</button>');
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
                    }else{
                        swal("Warning!", "class exist.", "warning");
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

function cancelDelete(){
    $(document).on("click","#cancel", function(){
        $("#confirmDelete").modal('hide');
    });
}
function deletes(){
    $(document).on("click","#delete", function(){
        var name=$("#classname").val();
        $("#classNameDelete").val(name);
        var id = $("#idedit").val();
        $("#iddelete").val(id);
        $("#confirmDelete").modal('show');
    });
}

function deleteClass(){
    $(document).on("click","#deleteItems", function(){
        var id=$("#iddelete").val();
            $.ajax({
                url: "ClassServlet",
                type: "POST",
                dataType: "text",
                data: {
                    action: "deleteClass",
                    id: id
                },
                success: function (data) {
                    if (data == "success") {
                        $("#confirmDelete").modal('hide');
                        $("#edits").modal('hide');
                        swal("Success!", "Delete class success", "success");
                    }else{
                        $("#confirmDelete").modal('hide');
                        $("#edits").modal('hide');
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
    $(document).on("click","#info", function() {
        var idd = $(this).attr('id-column');
        var classname = $(this).attr('classname');
        var difinition=$(this).attr('definition');
        $("#idedit").val(idd);
        $("#editClassName").val(classname);
        $("#classname").val(classname);
        $("#editDefinition").val(difinition);
        $("#edits").modal('show');
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
                    if((data.courses!=null && data.courses.length>0) || (data.coursesOnClass!=null && data.coursesOnClass.length>0) ){
                        var item=data.coursesOnClass;
                        var items=data.courses;
                        $(item).each(function(){
                            $("#editCourses").append('<option selected=selected title="'+this.description+'" value="' + this.name + '">' + this.name + '</option>');
                        });
                        $(items).each(function(){
                            $("#editCourses").append('<option title="'+this.description+'" value="' + this.name + '">' + this.name + '</option>');
                        });
                    }
                    $('#editCourses').multiselect('destroy');
                    $('#editCourses').multiselect({ enableFiltering: true, buttonWidth: '200px'});
                    $('#editCourses').multiselect('refresh');

                    $("#editStudents").empty();
                    if((data.smt!=null && data.smt.length>0)||(data.smtOnClass!=null && data.smtOnClass.length>0)){
                        var item=data.smtOnClass;
                        var items=data.smt;
                        $(item).each(function(){
                            $("#editStudents").append('<option selected=selected value="' + this.studentName + '">' + this.studentName + '</option>');
                        });
                        $(items).each(function(){
                            $("#editStudents").append('<option value="' + this.studentName + '">' + this.studentName + '</option>');
                        });
                    }
                    $('#editStudents').multiselect('destroy');
                    $('#editStudents').multiselect({ enableFiltering: true, buttonWidth: '200px'});
                    $('#editStudents').multiselect('refresh');
                }else{
                    swal("Error!", "Could not connect to server", "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    });

}

function editClass(){
    $(document).on("click","#yesedit", function(){
        var courses=[];
        var students = [];
        $('#editCourses option:selected').map(function(a, item){ courses.push(item.value);});
        $('#editStudents option:selected').map(function(a, item){ students.push(item.value);});
        var dto={
            courses:courses,
            students:students
        }
        var id = $("#idedit").val();
        var classname = $("#editClassName").val();
        var definition = $("#editDefinition").val();
        $.ajax({
            url: "ClassServlet",
            type: "POST",
            dataType: "text",
            data: {
                action: "editClass",
                id: id,
                difinition: definition,
                classname:classname,
                objDto: JSON.stringify(dto)
            },
            success: function (data) {
                if (data == "success") {
                    $("#listMyClass").empty();
                    listMyClasses();
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
function helpAddMyClass(){
    $(document).on("click","#helpAddClass",function() {
        $("#helpAddClassModal").modal('show');
    });
}

$(document).ready(function(){
    $('#help-icons').show();
    editClass();
    cancelDelete();
    deletes();
    deleteClass();
    helpAddMyClass();
    openEdit();
    openAdd();
    addClass();
    helpMyClass();
    listMyClasses();
});

