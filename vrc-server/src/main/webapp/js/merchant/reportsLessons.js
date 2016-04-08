var idCourse;
var idLevel;
var idObj;
function getStudentName(){
    return $("#studentName").val();
}
function getClassName(){
    return $("#class-name").val();
}
function getClassId(){
    return $("#class-id").val();
}
function getReportPreview(){
    return $("#report-popup");
}


function listStudent() {
    $("#listUser").append('<option value="' + getStudentName() + '">' + getStudentName() + '</option>');
    $('#listUser').multiselect({enableFiltering: true, buttonWidth: '100%'});
}

function listClass() {
    $("#listClass").append('<option value="' + getClassId + '">' + getClassName() + '</option>');
    $('#listClass').multiselect({enableFiltering: true, buttonWidth: '100%'});
}

function listCourse() {
    $.ajax({
        url: "ReportsLessons",
        type: "POST",
        dataType: "json",
        data: {
            action: "listCourse",
            idClass: getClassId()
        },
        success: function (data) {
            if (data.message == "success") {
                $("#listCourse").empty();
                if (data.listCourse != null && data.listCourse.length > 0) {
                    var items = data.listCourse;
                    listLevels(items[0].id);
                    $(items).each(function () {
                        $("#listCourse").append('<option value="' + this.id + '">' + this.name + '</option>');
                    });

                }
                $('#listCourse').multiselect('destroy');
                $('#listCourse').multiselect({enableFiltering: true, buttonWidth: '100%'});
                $('#listCourse').multiselect('refresh');
            } else {
                swal("", data.message.split(":")[1], "error");
            }
        },
        error: function () {
            swal("", "Could not connect to server", "error");
        }

    });
}

function listLevels(idCourse) {
    $.ajax({
        url: "ReportsLessons",
        type: "POST",
        dataType: "json",
        data: {
            action: "listLevel",
            idCourse: idCourse
        },
        success: function (data) {
            if (data.message == "success") {
                $("#listLevel").empty();
                if (data.listLevel != null && data.listLevel.length > 0) {
                    var items = data.listLevel;
                    listObjectives(items[0].id);
                    $(items).each(function () {
                        $("#listLevel").append('<option value="' + this.id + '">' + this.name + '</option>');
                    });
                }

                $('#listLevel').multiselect('destroy');
                $('#listLevel').multiselect({enableFiltering: true, buttonWidth: '200px'});
                $('#listLevel').multiselect('refresh');
            } else {
                swal("", data.message.split(":")[1], "error");
            }
        },
        error: function () {
            swal("", "Could not connect to server", "error");
        }

    });
}

function listObjectives(idLevel) {
    $.ajax({
        url: "ReportsLessons",
        type: "POST",
        dataType: "json",
        data: {
            action: "listObjective",
            idLevel: idLevel
        },
        success: function (data) {
            if (data.message == "success") {
                $("#listObjective").empty();
                if (data.listObj != null && data.listObj.length > 0) {
                    var items = data.listObj;
                    $(items).each(function () {
                        $("#listObj").append('<option value="' + this.id + '">' + this.name + '</option>');
                    });
                }

                $('#listObj').multiselect('destroy');
                $('#listObj').multiselect({enableFiltering: true, buttonWidth: '200px'});
                $('#listObj').multiselect('refresh');
            } else {
                swal("Error!", data.message.split(":")[1], "error");
            }
        },
        error: function () {
            swal("Error!", "Could not connect to server", "error");
        }

    });
}

function changeCourse() {
    $(document).on("change", "#listCourse", function () {
        var idCourse = $("#listCourse").val();
        $.ajax({
            url: "ReportsLessons",
            type: "POST",
            dataType: "json",
            data: {
                action: "listLevel",
                idCourse: idCourse
            },
            success: function (data) {
                if (data.message == "success") {
                    $("#divObjective").hide();
                    $("#listLevel").empty();
                    $("#draw").empty();
                    if (data.listLevel != null && data.listLevel.length > 0) {
                        var items = data.listLevel;
                        listObjectives(items[0].id);
                        $(items).each(function () {
                            $("#listLevel").append('<option value="' + this.id + '">' + this.name + '</option>');
                        });
                    }
                    $('#listLevel').multiselect('destroy');
                    $('#listLevel').multiselect({enableFiltering: true, buttonWidth: '200px'});
                    $('#listLevel').multiselect('refresh');
                } else {
                    swal("", data.message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("", "Could not connect to server", "error");
            }

        });
    });
}
function changeLevel() {
    $(document).on("change", "#listLevel", function () {
        var idLevel = $("#listLevel").val();
        $.ajax({
            url: "ReportsLessons",
            type: "POST",
            dataType: "json",
            data: {
                action: "listObjective",
                idLevel: idLevel
            },
            success: function (data) {
                if (data.message == "success") {
                    $("#listObj").empty();
                    if (data.listObj != null && data.listObj.length > 0) {
                        var items = data.listObj;
                        $(items).each(function () {
                            $("#listObj").append('<option value="' + this.id + '">' + this.name + '</option>');
                        });
                    }
                    $('#listObj').multiselect('destroy');
                    $('#listObj').multiselect({enableFiltering: true, buttonWidth: '200px'});
                    $('#listObj').multiselect('refresh');
                } else {
                    swal("", data.message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("", "Could not connect to server", "error");
            }

        });
    });
}

function loadInfo() {
    $(document).on("click", "#loadInfo", function () {
        $("#draw").html("");
        var idObj = $("#listObj").val();
        loadLesson(idObj);
    });
}
function loadLesson(idObj) {
    $.ajax({
        url: "ReportsLessons",
        type: "POST",
        dataType: "json",
        data: {
            action: "loadLesson",
            idObj: idObj
        },
        success: function (data) {
            if (data.message == "success") {
                if (data.listLesson != null && data.listLesson.length > 0) {
                    var items = data.listLesson;
                    $(items).each(function () {
                        var idLesson = this.id;
                        var name = this.name;
                        var date = this.dateCreated;
                        var Course = $("#listCourse option:selected").text();
                        var Level = $("#listLevel option:selected").text();
                        var Obj = $("#listObj option:selected").text();
                        $("#draw").append(draw(Course, Level, Obj, name, date,idLesson));
                        drawCircle(idLesson,getStudentName(),getClassId(),name);
                    });
                }
            } else {
                swal("", data.message.split(":")[1], "error");
            }
        },
        error: function () {
            swal("", "Could not connect to server", "error");
        }

    });
}
function drawCircle(idLesson,student,idClass,name){
    $.ajax({
        url: "ReportsLessons",
        type: "POST",
        dataType: "json",
        data: {
            action: "drawCircle",
            idLesson: idLesson,
            student : student,
            idClass : idClass
        },
        success: function (data) {
            if (data.message == "success") {
                var studentScore = data.reports.studentScoreLesson;
                var classScore = data.reports.classAvgScoreLesson;
                var date_complete = data.reports.dateCreated;
                var sessionId = data.reports.sessionId;
                if(studentScore == 0 && date_complete == null){
                    $("#"+idLesson).hide();
                }
                $("#"+idLesson).attr("session-id",sessionId);
                $("#"+idLesson).find('#date-completed').html(date_complete);
                $("#"+idLesson).find('.scoreStudent label').html(studentScore);
                $("#"+idLesson).find('.scoreClass label').html(classScore);
                drawChart(idLesson,getStudentName(),getClassId(),name,date_complete);
            } else {
                $("#"+idLesson).hide();
            }
        },
        error: function () {
        }
    });
}

function drawChart(idLesson,student,idClass,date) {
    $.ajax({
        url: "ReportsLessons",
        type: "POST",
        dataType: "json",
        data: {
            action: "drawChart",
            idLesson: idLesson,
            student : student,
            idClass : idClass
        },
        success: function (data) {
            if (data.message == "success") {
                var report = data.reports;
                replaceLoadingPhonemes(idLesson);
                replaceLoadingWord(idLesson);
                $("#"+idLesson).find('#thumbnail-word').attr("date-complete",date);
                $("#"+idLesson).find('#thumbnail-word').attr("lesson-name",name);
                $("#"+idLesson).find('#thumbnail-word').attr("list-word",report.word);
                $("#"+idLesson).find('#thumbnail-word').attr("list-student-score",report.wordStudentScore);
                $("#"+idLesson).find('#thumbnail-word').attr("list-class-score",report.wordClassScore);
            } else {
                replaceLoadingPhonemes(idLesson);
                replaceLoadingWord(idLesson);
                $("#"+idLesson).find('#thumbnail-word').attr("date-complete",date);
                $("#"+idLesson).find('#thumbnail-word').attr("lesson-name",name);
                $("#"+idLesson).find('#thumbnail-word').attr("list-word","error");
                $("#"+idLesson).find('#thumbnail-word').attr("list-student-score","error");
                $("#"+idLesson).find('#thumbnail-word').attr("list-class-score","error");
            }
        },
        error: function () {
            swal("", "Could not connect to server", "error");
        }
    });
}

function openPreviewReport(){
    $(document).on("click","#drawWord img",function(){
        getReportPreview().modal('show');
        var listWord = $(this).attr("list-word");
        var studentScore = $(this).attr("list-student-score");
        var classScore = $(this).attr("list-class-score");
        getReportPreview().find("#canvas").remove();
        getReportPreview().find("#container-canvas").append(" <canvas height='400px' width='900px' id='canvas'></canvas>");
        init(listWord.split(","),studentScore,classScore,"word");
    });
}

function parseToArray(str){
    var array = str.split(",");
    return array;
}

$(document).ready(function () {
    openPreviewReport();
    $('#help-icons').show();
    loadInfo();
    listStudent();
    listClass();
    listCourse();
    changeCourse();
    changeLevel();
});

