var idCourse;
var idLevel;
var idObj;
function listCourse(){
    var student=$("#studentName").val();
        $.ajax({
            url: "ReportsLessons",
            type: "POST",
            dataType: "json",
            data: {
                action: "listCourse",
                studentName:student
            },
            success: function (data) {
                if (data.message == "success") {
                    $("#listCourse").empty();
                    if (data.listCourse != null && data.listCourse.length > 0) {
                        var items = data.listCourse;
                        $(items).each(function () {
                            $("#listCourse").append('<option value="' + this.id + '">' + this.name + '</option>');
                        });
                        idCourse=data.listCourse[0].id;
                        listLevels(data.listCourse[0].id);
                    }

                    $('#listCourse').multiselect('destroy');
                    $('#listCourse').multiselect({enableFiltering: true, buttonWidth: '200px'});
                    $('#listCourse').multiselect('refresh');
                    $("#listUsers").empty();
                    if(data.listStudent!=null && data.listStudent.length>0) {
                        var items = data.listStudent;
                        $(items).each(function () {
                            if (this.studentName == student) {
                                $("#listUsers").append('<option value="' + this.studentName + '">' + this.studentName + '</option>');
                            } else {
                                $("#listUsers").append('<option value="' + this.studentName + '" disabled="disabled">' + this.studentName + '</option>');
                            }

                        });
                    }
                    $('#listUsers').multiselect('destroy');
                    $('#listUsers').multiselect({ enableFiltering: true, buttonWidth: '200px'});
                    $('#listUsers').multiselect('refresh');
                } else {
                    swal("Error!", data.message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
}

function listLevels(idCourse){
        $.ajax({
            url: "ReportsLessons",
            type: "POST",
            dataType: "json",
            data: {
                action: "listLevel",
                idCourse:idCourse
            },
            success: function (data) {
                if (data.message == "success") {
                    $("#listLevel").empty();
                    if (data.listLevel != null && data.listLevel.length > 0) {
                        var items = data.listLevel;
                        $(items).each(function () {
                            $("#listLevel").append('<option value="' + this.id + '">' + this.name + '</option>');
                        });
                        $("#divLevel").show();
                        idLevel=data.listLevel[0].id;
                        listObjectives(data.listLevel[0].id);
                    }

                    $('#listLevel').multiselect('destroy');
                    $('#listLevel').multiselect({enableFiltering: true, buttonWidth: '200px'});
                    $('#listLevel').multiselect('refresh');
                } else {
                    swal("Error!", data.message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
}

function listObjectives(idLevel){
    $.ajax({
        url: "ReportsLessons",
        type: "POST",
        dataType: "json",
        data: {
            action: "listObjective",
            idLevel:idLevel
        },
        success: function (data) {
            if (data.message == "success") {
                $("#listObjective").empty();
                if (data.listObj != null && data.listObj.length > 0) {
                    var items = data.listObj;
                    $(items).each(function () {
                        $("#listObjective").append('<option value="' + this.id + '">' + this.name + '</option>');
                    });
                    $("#divObjective").show();
                    idObj= data.listObj[0].id;
                }

                $('#listObjective').multiselect('destroy');
                $('#listObjective').multiselect({enableFiltering: true, buttonWidth: '200px'});
                $('#listObjective').multiselect('refresh');
            } else {
                swal("Error!", data.message.split(":")[1], "error");
            }
        },
        error: function () {
            swal("Error!", "Could not connect to server", "error");
        }

    });
}


function listLevel(){
    $(document).on("change","#listCourse",function() {
        var idCourse=$("#listCourse").val();
        $.ajax({
            url: "ReportsLessons",
            type: "POST",
            dataType: "json",
            data: {
                action: "listLevel",
                idCourse:idCourse
            },
            success: function (data) {
                if (data.message == "success") {
                    $("#divObjective").hide();
                    $("#listLevel").empty();
                    if (data.listLevel != null && data.listLevel.length > 0) {
                        var items = data.listLevel;
                        $(items).each(function () {
                            $("#listLevel").append('<option value="' + this.id + '">' + this.name + '</option>');
                        });
                    }
                    $('#listLevel').multiselect('destroy');
                    $('#listLevel').multiselect({enableFiltering: true, buttonWidth: '200px'});
                    $('#listLevel').multiselect('refresh');
                } else {
                    swal("Error!", data.message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    });
}
function listObjective(){
    $(document).on("change","#listLevel",function() {
        var idLevel=$("#listLevel").val();
        $.ajax({
            url: "ReportsLessons",
            type: "POST",
            dataType: "json",
            data: {
                action: "listObjective",
                idLevel:idLevel
            },
            success: function (data) {
                if (data.message == "success") {
                    $("#divObjective").show();
                    $("#listObjective").empty();
                    if (data.listObj != null && data.listObj.length > 0) {
                        var items = data.listObj;
                        $(items).each(function () {
                            $("#listObjective").append('<option value="' + this.id + '">' + this.name + '</option>');
                        });
                    }
                    $('#listObjective').multiselect('destroy');
                    $('#listObjective').multiselect({enableFiltering: true, buttonWidth: '200px'});
                    $('#listObjective').multiselect('refresh');
                } else {
                    swal("Error!", data.message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    });
}

function loadInfo(){
    $(document).on("click","#loadInfo",function() {
        var idCourse=$("#listCourse").val();
        var idLevel=$("#listLevel").val();
        var idObj=$("#listObjective").val();
        var student=$("#").val();
        $.ajax({
            url: "ReportsLessons",
            type: "POST",
            dataType: "json",
            data: {
                action: "loadInfo",
                idCourse:idCourse,
                idLevel:idLevel,
                idObj:idObj,
                student:student
            },
            success: function (data) {
                if (data.message == "success") {
                    if (data.listLevel != null && data.listLevel.length > 0) {
                        var items = data.listLevel;
                        $(items).each(function () {
                            $("#listLevel").append('<option value="' + this.id + '">' + this.name + '</option>');
                        });
                    }
                } else {
                    swal("Error!", data.message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    });
}

$(document).ready(function(){
    $('#help-icons').show();
    loadInfo();
    listCourse()
    listLevel();
    listObjective();
});

