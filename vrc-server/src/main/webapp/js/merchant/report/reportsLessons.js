var idCourse;
var idLevel;
var idObj;
var color_class_score = "#558ED5";
var color_student_score = "#17375E";
var totalReport = 0;
function getStudentName(){
    return $("#listUsers option:selected").val();
}
function getClassName(){
    return $("#class-name").val();
}
function getClassId(){
    return $("#listClass option:selected").val();
}
function getReportPreview(){
    return $("#report-popup");
}
function showDropdown(id){
    $('#'+id).parent().find('.lazyload').hide();
    $('#'+id).show();
    $('#'+id).multiselect('destroy');
    $('#'+id).multiselect({ enableFiltering: true, maxHeight: 300,buttonWidth: '200px'});
    $('#'+id).multiselect('refresh');
}
function showLoading(id){
    $('#'+id).parent().find('.lazyload').show();
    $('#'+id).hide();
}
function checkEmptyReport(){
    var emp = $('#draw').is(':empty');
    if(emp){
        swalNew("", "there are no report", "warning");
    }
}

function enableTick(){
    if(totalReport == 0){
        $('#loadInfo').removeAttr('disabled');
    }
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
                    showDropdown('listClass');
                    listStudents(items[0].id);
                    listCourse(items[0].id);
                }
            }else{
                swalNew("", "", "warning");
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
                    $(items).each(function(){
                        $("#listUsers").append('<option value="' + this.studentName + '">' + this.studentName + '</option>');
                    });
                    showDropdown('listUsers');
                }else{

                }
            }else{
                swalNew("", "an error has been occurred in server", "error");
            }
        },
        error: function () {
            swalNew("", "could not connect to server", "error");
        }

    });
}

function listCourse(idClass) {
    $.ajax({
        url: "ReportsLessons",
        type: "POST",
        dataType: "json",
        data: {
            action: "listCourse",
            idClass: idClass
        },
        success: function (data) {
            if (data.message == "success") {
                $("#listCourse").empty();
                if (data.listCourse != null && data.listCourse.length > 0) {
                    var items = data.listCourse;
                    $(items).each(function () {
                        $("#listCourse").append('<option value="' + this.id + '">' + this.name + '</option>');
                    });
                    showDropdown('listCourse');
                    listLevels(items[0].id);
                }else{
                }
            } else {
                swalNew("", data.message.split(":")[1], "error");
            }
        },
        error: function () {
            swalNew("", "could not connect to server", "error");
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
                    $(items).each(function () {
                        $("#listLevel").append('<option value="' + this.id + '">' + this.name + '</option>');
                    });
                    showDropdown('listLevel');
                    listObjectives(items[0].id);
                }else{

                }
            } else {
                swalNew("", data.message.split(":")[1], "error");
            }
        },
        error: function () {
            swalNew("", "could not connect to server", "error");
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
                $("#listObj").empty();
                if (data.listObj != null && data.listObj.length > 0) {
                    var items = data.listObj;
                    $(items).each(function () {
                        $("#listObj").append('<option value="' + this.id + '">' + this.name + '</option>');
                    });
                    showDropdown('listObj');
                }
            } else {
                swalNew("", data.message.split(":")[1], "error");
            }
        },
        error: function () {
            swalNew("", "could not connect to server", "error");
        }

    });
}

function onChangeSelect() {
    $(document).on("change", "#listClass", function () {
        var idClass = $("#listClass").val();
        showLoading('listUser');
        showLoading('listCourse');
        showLoading('listLevel');
        showLoading('listObj');
        listStudents(idClass);
        listCourse(idClass);

    });

    $(document).on("change", "#listCourse", function () {
        var idCourse = $("#listCourse").val();
        showLoading('listLevel');
        showLoading('listObj');
        listLevels(idCourse);
    });

    $(document).on("change", "#listLevel", function () {
        var idLevel = $("#listLevel").val();
        showLoading('listObj');
        listObjectives(idLevel);
    });
    $(document).on("change", "select", function () {
        $("#draw").html("");
    });
}
function loadInfo() {
    $(document).on("click", "#loadInfo", function () {
        $("#draw").html("");
        var idObj = $("#listObj").val();
        $("#loadInfo").attr('disabled');
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
                    totalReport = items.length;
                    $(items).each(function () {
                        var idLesson = this.id;
                        var name = this.title;
                        var date = this.dateCreated;
                        var Course = $("#listCourse option:selected").text();
                        var Level = $("#listLevel option:selected").text();
                        var Obj = $("#listObj option:selected").text();
                        $("#draw").append(draw(Course, Level, Obj, name, date,idLesson));
                        drawCircle(idLesson,getStudentName(),getClassId(),name);
                    });
                }
            } else {
                swalNew("", data.message.split(":")[1], "error");
            }
        },
        error: function () {
            swalNew("", "could not connect to server", "error");
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
                loadAdditionInfo(idLesson,getStudentName(),getClassId(),name,date_complete);
            } else {
                $("#"+idLesson).remove();
                totalReport  = totalReport -1;
            }
            checkEmptyReport();
            enableTick();
        },
        error: function () {
        }
    });
}

function loadAdditionInfo(idLesson,student,idClass,date) {
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
                $("#"+idLesson).find('#thumbnail-word').attr("idLesson",idLesson);
                $("#"+idLesson).find('#thumbnail-word').attr("list-word",report.word);
                $("#"+idLesson).find('#thumbnail-word').attr("list-student-score",report.wordStudentScore);
                $("#"+idLesson).find('#thumbnail-word').attr("list-class-score",report.wordClassScore);

                $("#"+idLesson).find('#thumbnail-phonemes').attr("date-complete",date);
                $("#"+idLesson).find('#thumbnail-phonemes').attr("idLesson",idLesson);
                $("#"+idLesson).find('#thumbnail-phonemes').attr("list-phonemes",report.phonemes);
                $("#"+idLesson).find('#thumbnail-phonemes').attr("list-student-phonemes-score",report.phonemesStudentScore);
                $("#"+idLesson).find('#thumbnail-phonemes').attr("list-class-phonemes-score",report.phonemesClassScore);
            } else {
                $("#"+idLesson).remove();
            }
            totalReport  = totalReport -1;
            checkEmptyReport();
            enableTick();
        },
        error: function () {
            swalNew("", "could not connect to server", "error");
        }
    });
}

function clickToThumbnail(){
    $(document).on("click","#drawWord img",function(){
        getReportPreview().attr("list-data",$(this).attr("list-word"));
        getReportPreview().attr("list-student-score",$(this).attr("list-student-score"));
        getReportPreview().attr("list-class-score",$(this).attr("list-class-score"));
        getReportPreview().attr("idLesson",$(this).attr("idLesson"));
        getReportPreview().attr("type","word");
        getReportPreview().modal('show');
    });
    $(document).on("click","#drawPhonemes img",function(){
        getReportPreview().attr("list-data",$(this).attr("list-phonemes"));
        getReportPreview().attr("list-student-score",$(this).attr("list-student-phonemes-score"));
        getReportPreview().attr("list-class-score",$(this).attr("list-class-phonemes-score"));
        getReportPreview().attr("idLesson",$(this).attr("idLesson"));
        getReportPreview().attr("type","phonemes");
        getReportPreview().modal('show');
    });
}
function openReportPreview(){
    $("#report-popup").on('shown.bs.modal', function () {
        var listData = parseToArray($(this).attr("list-data"));
        var studentScores = parseToArray($(this).attr("list-student-score"));
        var classScores = parseToArray($(this).attr("list-class-score"));
        var idLesson = $(this).attr("idLesson");
        var type = $(this).attr("type");
        fillDataPopUp(idLesson);
        drawBarChart(listData,studentScores,classScores,type);
    });
}



function parseToArray(str){
    var array = str.split(",");
    return array;
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
$(document).ready(function () {
    mouseOverChart();
    clickToThumbnail();
    openReportPreview();
    help();
    loadInfo();
    onChangeSelect();
    collapseMenu();
    listClasses();
});

