var previousPoint = null;
function draw(Course,Level,Obj,name,date,idLesson){
    var $html=$("<div id='"+idLesson+"' class='col-sm-12 grap'> " +
    "<div id='info' class='col-sm-2'> " +
    "<p><strong>course:</strong></p> " +
    "<label id='lbl_course' style='font-weight: 200;'>"+Course+"</label> " +
    "<p><strong>level:</strong></p> " +
    "<label id='lbl_lv' style='font-weight: 200;'>"+Level+"</label> " +
    "<p><strong>objective:</strong></p> " +
    "<label id='lbl_obj' style='font-weight: 200;'>"+Obj+"</label> " +
    "<p><strong>lesson:</strong></p> " +
    "<label id='lbl_lesson' style='font-weight: 200;'>"+name+"</label> " +
    "<p><strong>completion date:</strong></p> " +
    "<label id='date-completed' style='font-weight: 200;'><label><span class='glyphicon glyphicon-refresh glyphicon-refresh-animate'></span></label> " +
    "</div> " +
    "<div id='score' class='col-sm-2'> " +
    "<div class='row'><div class='col-sm-6 scoreStudent' title='student score'><label><span class='glyphicon glyphicon-refresh glyphicon-refresh-animate'></span></label></div></div>" +
    "<div class='row'><div class='col-sm-8 scoreClass' title='class average score'><label><span class='glyphicon glyphicon-refresh glyphicon-refresh-animate'></span></label></div></div>" +
    "</div>" +
    "<div id='drawPhonemes' class='col-sm-4'> " +
    "<span class='glyphicon glyphicon-refresh glyphicon-refresh-animate'></span>" +
    "</div> " +
    "<div id='drawWord' class='col-sm-4'> " +
    "<span class='glyphicon glyphicon-refresh glyphicon-refresh-animate'></span>" +
    "</div> " +
    "</div>");
    return $html;
}

function fillDataPopUp(idLesson){
    var courseName = $("#"+idLesson).find('#lbl_course').html();
    var levelName = $("#"+idLesson).find('#lbl_lv').html();
    var objName = $("#"+idLesson).find('#lbl_obj').html();
    var lesson  = $("#"+idLesson).find('#lbl_lesson').html();
    var date_completed  = $("#"+idLesson).find('#date-completed').html();
    var sScore =  $("#"+idLesson).find('.scoreStudent label').html();
    var cScore = $("#"+idLesson).find('.scoreClass label').html();

    getReportPreview().find('.student-username').html(getStudentName());
    getReportPreview().find('#lbl_course').html(courseName);
    getReportPreview().find('#lbl_lv').html(levelName);
    getReportPreview().find('#lbl_obj').html(objName);
    getReportPreview().find('#lbl_lesson').html(lesson);
    getReportPreview().find('#lbl_date_completed').html(date_completed);
    getReportPreview().find('.class-avg-score').html(cScore);
    getReportPreview().find('.student-avg-score').html(sScore);
}


function replaceLoadingPhonemes(idLesson){
    var img = $("<img>");
    img.attr("src","/images/teacher/report_thumbnail_phonemes.gif");
    img.attr("id","thumbnail-phonemes");
    img.css("cursor","pointer");
    $("#"+idLesson).find("#drawPhonemes").html(img);
}

function replaceLoadingWord(idLesson){
    var img = $("<img>");
    img.attr("src","/images/teacher/report_thumbnail_words.gif");
    img.attr("id","thumbnail-word");
    img.css("cursor","pointer");
    $("#"+idLesson).find("#drawWord").html(img);
}


function generateArray(listData, listScore){
    var tmp = [];
    if(listData!=null && listData.length > 0){
        for(var i = 0 ; i < listData.length ; i++){
            var label = listData[i];
            //if(listScore[i] > 0){
                tmp.push([label,listScore[i]]);
           // }
        }
    }
    return tmp;
}


function generateWidth(data){
    var width = 0;
    if(data.length > 0 && data.length < 10){
        width = "100%";
    }else if(data.length >= 10 && data.length <= 20){
        width = "200%";
    }else if(data.length > 20 && data.length <=50){
        width = "300%";
    }
    return width;
}

function generateTickArray(listData){
    var tmp = [];
    if(listData!=null && listData.length > 0){
        for(var i = 0 ; i < listData.length ; i++){
            var label = listData[i];
            //if(listScore[i] > 0){
            tmp.push([i,listData[i]]);
            // }
        }
    }
    return tmp;
}

function drawBarChart(listData,studentScores,classScores,type){
    var ssData = generateArray(listData, studentScores);
    var casData = generateArray(listData, classScores);
    var data = [
        { label: "Student Score", data: ssData },
        { label: "Class Average Score", data: casData }
    ];

    $("#placeholder").css("width",generateWidth(ssData));
    $.plot("#placeholder", data, {
        series: {
            bars: {
                show: true,
                barWidth: 0.08,
                lineWidth: 0,
                order: 1,
                fillColor: {
                    colors: [{
                        opacity: 1
                    }, {
                        opacity: 1
                    }]
                }
            }
        },
        xaxis: {
            mode: "categories",
            tickLength: 0,
            min: -0.5,
            //max: 6.5,
            ticks : generateTickArray(listData)
        },
        yaxis : {
            min : 0,
            max : 100,
            axisLabelPadding: 5,
            axisLabel: 'Score percent',
            axisLabelUseCanvas: true,
            axisLabelFontSizePixels: 14,
            axisLabelFontFamily: 'Verdana, Arial, Helvetica, Tahoma, sans-serif',
            axisLabelPadding: 5
        },
        grid: {
            borderWidth: 1,
            hoverable: true
        },
        colors: [color_student_score, color_class_score]
    });

}

function showToolTip(x, y, contents, z){
    $('<div id="flot-tooltip">' + contents + '</div>').css({
        top: y - 100,
        left: x - 280,
        'border-color': z,
    }).appendTo("#holder-chart").show();
}

function mouseOverChart(){
    $("#placeholder").bind("plothover", function (event, pos, item) {
        if (item) {
            if (previousPoint != item.datapoint) {
                y = item.datapoint[1];
                z = item.series.color;
                $("#flot-tooltip").remove();
                showToolTip(item.pageX, item.pageY,
                    "<b>" + item.series.label + "</b><br /> " + y ,
                    z);
            }
        } else {
            $("#flot-tooltip").remove();
            previousPoint = null;
        }
    });
}