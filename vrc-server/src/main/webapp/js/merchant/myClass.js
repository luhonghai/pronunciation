function listMyClasses(){
    $.ajax({
        url: "ClassServlet",
        type: "POST",
        dataType: "json",
        data: {
           action:"listMyClass"
        },
        success: function (data) {
            if(data.message=="success" && data.list!=null){
                var listMyClass=data.list;
                for(var i=0;i<listMyClass.length;i++){
                    var definition=listMyClass[i].definition;
                    var $button = $('<div class="col-sm-12" style="padding-bottom: 20px;padding-left:0px">' +
                        '<a id="info" title="'+definition+'" class="a-info-student" style="background-color:#17375E;">' +
                        '<img src="/images/teacher/my%20classes48x48.gif" style="width: 24px;height: 24px;"> '+
                        '<label class="studentMail">' + listMyClass[i].className + '</label></a></div>');
                    $button.attr("id-column", listMyClass[i].id);
                    $button.attr("className", listMyClass[i].className);
                    $button.attr("definition", listMyClass[i].definition);
                    $("#listMyClass").append($button).html();
                }
            }
        },
        error: function () {
            swal("", "Could not connect to server", "error");
        }

    });

}

function openAdd(){
    $(document).on("click","#addClass", function(){
        $("#add").find(".validateMsg").hide();
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
                            var name = this.name;
                            if(name.length > 40){
                                name = name.substring(0, 40) + '...';
                            }
                            $("#addCourses").append('<option title="'+this.name+'" value="' + this.id + '">' + name + '</option>');
                        });
                    }
                    $('#addCourses').multiselect('destroy');
                    $('#addCourses').multiselect({ enableFiltering: true,maxHeight: 200, buttonWidth: '100%'});
                    $('#addCourses').multiselect('refresh');

                    $("#addStudents").empty();
                    if(data.studentMappingTeachers!=null && data.studentMappingTeachers.length>0){
                        var items=data.studentMappingTeachers;
                        $(items).each(function(){
                            $("#addStudents").append('<option value="' + this.studentName + '">' + this.studentName + '</option>');
                        });
                    }
                    $('#addStudents').multiselect('destroy');
                    $('#addStudents').multiselect({ enableFiltering: true,maxHeight: 200, buttonWidth: '100%'});
                    $('#addStudents').multiselect('refresh');
                }else{
                    swal("Error!", data.message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("", "Could not connect to server", "error");
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
        if(validateFormAdd()) {
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
                        swal("", "You have added class successfully", "success");
                    }else{
                        $("#classExits").find("#invalidClass").html(classname);
                        $("#classExits").modal('show');
                    }
                },
                error: function () {
                    swal("", "Could not connect to server", "error");
                }

            });
        }

    });





}


function validateFormAdd(){
    var className= $.trim($("#addClassName").val());
    if(className == "" || className.length == 0 || typeof className ==='undefined'){
        $("#add").find(".validateMsg").html("please enter a class name");
        $("#add").find(".validateMsg").show();
        return false;
    }else{
        $("#add").find(".validateMsg").hide();
    }
    var courses=[];
    var students = [];
    $('#addCourses option:selected').map(function(a, item){ courses.push(item.value);});
    $('#addStudents option:selected').map(function(a, item){ students.push(item.value);});
    if(courses.length == 0 ){
        $("#add").find(".validateMsg").html("please choose a course");
        $("#add").find(".validateMsg").show();
        return false;
    }
    if(students.length == 0 ){
        $("#add").find(".validateMsg").html("please choose a student");
        $("#add").find(".validateMsg").show();
        return false;
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
        var name=$(this).attr("name");
        $("#confirmDelete").find("#classNameDelete").html(name);
        var id = $(this).attr("idClass");
        $("#confirmDelete").find("#deleteItems").attr("idClass",id);
        $("#confirmDelete").modal('show');
    });
}

function deleteClass(){
    $(document).on("click","#deleteItems", function(){
        var id= $(this).attr("idClass");
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
                        $("#listMyClass").empty();
                        listMyClasses();
                        $("#confirmDelete").modal('hide');
                        $("#edits").modal('hide');
                        swal("", "You have deleted class successfully", "success");
                    }else if (data == "not exist"){
                        $("#confirmDelete").modal('hide');
                        $("#edits").modal('hide');
                        swal({title: "Warning!", text: "This class has been already deleted!",   type: "warning",timer:"5000" });
                        location.reload();
                    }else {
                        swal("", "Could not connect to server", "error");
                    }
                },
                error: function () {
                    swal("", "Could not connect to server", "error");
                }

            });
});
}

function openEdit(){
    $(document).on("click","#info", function() {
        var idd = $(this).parent().attr('id-column');
        var classname = $(this).parent().attr('classname');
        var difinition=$(this).parent().attr('definition');
        $("#idedit").val(idd);
        $("#editClassName").val(classname);
        $("#classname").val(classname);
        $("#editDefinition").val(difinition);
        $("#edits").find("#delete").attr("name", classname);
        $("#edits").find("#delete").attr("idClass", idd);
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
                            $("#editCourses").append('<option selected=selected title="'+this.description+'" value="' + this.id + '">' + this.name + '</option>');
                        });
                        $(items).each(function(){
                            $("#editCourses").append('<option title="'+this.description+'" value="' + this.id + '">' + this.name + '</option>');
                        });
                    }
                    $('#editCourses').multiselect('destroy');
                    $('#editCourses').multiselect({ enableFiltering: true, buttonWidth: '100%'});
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
                    $('#editStudents').multiselect({ enableFiltering: true, buttonWidth: '100%'});
                    $('#editStudents').multiselect('refresh');
                    $("#edits").modal('show');
                }else{
                    swal("", "an error has been occured in server", "error");
                }
            },
            error: function () {
                swal("", "Could not connect to server", "error");
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
                    swal("", "You have updated class successfully", "success");
                }else if (data == "not exist"){
                    $("#deletes").modal('hide');
                    swal({title: "Warning!", text: "This class has been already deleted!",   type: "warning",timer:"5000" });
                    location.reload();
                }else if(data == "name existed"){
                    $("#classExits").find("#invalidClass").html(classname);
                    $("#classExits").modal('show');
                }else{
                    swal("", "Could not connect to server", "error");
                }
            },
            error: function () {
                swal("", "Could not connect to server", "error");
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

function helpEditMyClass(){
    $(document).on("click","#helpEditClass",function() {
        $("#helpAddClassModal").modal('show');
    });
}

function collapseMenu(){
    $("#li-class").find('ul').addClass('in');
}

$(document).ready(function(){
    $('#help-icons').show();
    collapseMenu();
    editClass();
    cancelDelete();
    deletes();
    deleteClass();
    helpAddMyClass();
    openEdit();
    openAdd();
    addClass();
    helpMyClass();
    helpEditMyClass();
    listMyClasses();
});

