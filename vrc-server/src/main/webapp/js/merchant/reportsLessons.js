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

function clickToThumbnail(){
    $(document).on("click","#drawWord img",function(){
        getReportPreview().attr("list-word",$(this).attr("list-word"));
        getReportPreview().attr("list-student-score",$(this).attr("list-student-score"));
        getReportPreview().attr("list-class-score",$(this).attr("list-class-score"));
        getReportPreview().modal('show');
    });
}
function openReportPreview(){
    $("#report-popup").on('shown.bs.modal', function () {
        var listWord = parseToArray($(this).attr("list-word"));
        var studentScores = parseToArray($(this).attr("list-student-score"));
        var classScores = parseToArray($(this).attr("list-class-score"));
        drawStackedChart(listWord,studentScores,classScores);
    });
}
var color_class_score = "#558ED5";
var color_student_score = "#17375E";
var color_blank = "#FFFFFF";

function generateDataChart(studentScores,classScores){
    var arrayDataBottom =[];
    var arrayColorBottom=[];
    var arrayDataTop=[];
    var arrayColorTop=[];
    for(i=0;i<studentScores.length;i++){
        var sScore = studentScores[i];
        var cScore = classScores[i];
        if(sScore == 0){
            arrayDataBottom.push([i,0]);
            arrayColorBottom.push([i,color_blank]);
            arrayDataTop.push([i,0]);
            arrayColorBottom.push([i,color_blank]);
            continue;
        }
        if(sScore > cScore){
            arrayDataBottom.push([i,cScore]);
            arrayColorBottom.push([i,color_class_score]);
            arrayDataTop.push([i,sScore]);
            arrayColorBottom.push([i,color_student_score]);
            continue;
        }
        if(cScore > sScore){
            arrayDataBottom.push([i,sScore]);
            arrayColorBottom.push([i,color_student_score]);
            arrayDataTop.push([i,cScore]);
            arrayColorBottom.push([i,color_class_score]);
            continue;
        }
        if(sScore == cScore){
            arrayDataBottom.push([i,sScore]);
            arrayColorBottom.push([i,color_student_score]);
            arrayDataTop.push([i,0]);
            arrayColorBottom.push([i,color_student_score]);
            continue;
        }
    }
    var data = [
        {data: arrayDataBottom,color : arrayColorBottom,temp : "temp"},
        {data: arrayDataTop,color :arrayColorTop ,temp : "temp1"}
    ];
    return data;
}

function generateYaxis(){
    var yaxis = [];
    for (i = 0; i < 20; i++) {
        if(i == 0){
            yaxis.push([i,5]);
            continue;
        }
        yaxis.push([i,i*5]);
    }
    return yaxis;
}

function generateXasis(listWord){
    var xaxis = [];
    for(i=0;i< listWord.length;i++){
        xaxis.push([i,listWord[i]]);
    }
    return xaxis;
}

function drawStackedChart(listWord,studentScores,classScores){
    var data = [
        {data: [[1,3], [2,4], [3,5], [4,6], [5,7]],color : [[1,"black"], [2,"red"], [3,"black"], [4,"red"], [5,"black"]],temp : "temp"},
        {data: [[1,8], [2,6], [3,4], [4,2], [5,0]],color : [[1,"red"], [2,"black"], [3,"red"], [4,"black"], [5,"red"]],temp : "temp1"}
    ];
    var tick_labels = generateXasis(listWord);
    var options = {
        series: {stack: 0,
            lines: {show: false, steps: false },
            bars: {show: true, barWidth: 0.5, align: 'center',},},
        xaxis: {ticks: tick_labels},
        yaxis: {
            min: 0,
            max: 100
        }
    };
    var data1 = generateDataChart(studentScores,classScores);
    $.plot($("#placeholder"), data1, options);
}
function parseToArray(str){
    var array = str.split(",");
    return array;
}

$(document).ready(function () {
    clickToThumbnail();
    openReportPreview();
    $('#help-icons').show();
    loadInfo();
    listStudent();
    listClass();
    listCourse();
    changeCourse();
    changeLevel();

});

