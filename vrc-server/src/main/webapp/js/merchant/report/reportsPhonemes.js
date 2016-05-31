var avg;
var servlet = "/ReportsPhonemes";
var dataReport;
function getProcessBar(){
    return $("#process-bar");
}
function showAllParam(){
    $('.param').each(function(){
        $(this).show();
    });
}

function hideAllParam(){
    $('.param').each(function(){
        $(this).hide();
    });
}
function loadData(){
    hideAllParam();
    getProcessBar().show();
    progress = getProcessBar().progressTimer({
        timeLimit: 20,
        onFinish: function () {
            getProcessBar().delay(2000).hide();
            showAllParam();
            progress.progressTimer('destroy');
        }
    });
        $.ajax({
            url: servlet,
            type: "POST",
            dataType: "json",
            data: {
                action: "load"
            },
            success: function (data) {
                if(data.message=="success"){
                    $("#listUsers").empty();
                    $("#listPhonemes").empty();
                    $('#listUsers').removeAttr("disabled");
                    if(data.listStudent!=null && data.listStudent.length>0){
                        var items=data.listStudent;
                        for(var i=0;i<items.length;i++){
                            $("#listUsers").append('<option value="' + items[i] + '">' + items[i] + '</option>');
                        }

                    }
                    $('#listUsers').multiselect('destroy');
                    $('#listUsers').multiselect({ enableFiltering: true, maxHeight: 200,buttonWidth: '200px'});
                    $('#listUsers').multiselect('refresh');

                    if(data.listPhonemes!=null && data.listPhonemes.length>0){
                        var items= data.listPhonemes;
                        for(var i=0;i<items.length;i++){
                            $("#listPhonemes").append('<option value="' + items[i].arpabet + '">' + items[i].ipa + '</option>');
                        }
                        $('#listPhonemes').multiselect('destroy');
                        $('#listPhonemes').multiselect({ enableFiltering: true,maxHeight: 200, buttonWidth: '200px'});
                        $('#listPhonemes').multiselect('refresh');
                    }
                }else{
                    $('#listUsers').attr("disabled","disabled");
                    $('#listUsers').multiselect('destroy');
                    $('#listUsers').multiselect({ enableFiltering: true, maxHeight: 200,buttonWidth: '200px'});
                    $('#listUsers').multiselect('refresh');
                    $('#listPhonemes').attr("disabled","disabled");
                    $('#listPhonemes').multiselect('destroy');
                    $('#listPhonemes').multiselect({ enableFiltering: true, buttonWidth: '50px'});
                    $('#listPhonemes').multiselect('refresh');
                }
            },
            error: function () {
                swalNew("", "could not connect to server", "error");
            }

        }).error(function(){
            progress.progressTimer('error', {
                errorText:'ERROR!',
                onFinish:function(){
                    swalNew("", "could not connect to server", "error");
                }
            });
        }).done(function(){
            progress.progressTimer('complete');
        });
}

function loadInfo(){
    $(document).on("click","#loadInfo",function(){
        var studentName = $('#listUsers option:selected').val();
        var phoneme = $("#listPhonemes").val();
        var dateFrom = $("#dateFrom").val();
        var type = $("#period").val();
        loadDataReport(studentName,phoneme,dateFrom,type);
    })
}

function loadDataReport(studentName,phoneme,dateFrom,type){
    hideAllParam();
    getProcessBar().show();
    progress = getProcessBar().progressTimer({
        timeLimit: 20,
        onFinish: function () {
            getProcessBar().delay(2000).hide();
            showAllParam();
            progress.progressTimer('destroy');
        }
    });
    $.ajax({
        url: servlet,
        type: "POST",
        dataType: "json",
        data: {
            action: "getReportData",
            studentName:studentName,
            phoneme:phoneme,
            dateFrom:dateFrom,
            type : type
        },
        success: function (data) {
            if(data == null){
                swalNew("", "no data found", "error");
                return;
            }
            var arrayTime = [];
            var arrayScore = [];
            var tmp = data.listScorePhonemes;
            var checkInValid = false;
            if(tmp.length == 0) checkInValid = true;
            for(var i=0;i<tmp.length;i++){
                if(tmp[0].serverTime == 0 || typeof tmp[0].serverTime == "undefined"){
                    checkInValid = true;
                }
                arrayTime.push(tmp[i].serverTime);
                arrayScore.push(tmp[i].score);
            }

            if(checkInValid){
                swalNew("", "no data found", "error");
            }else{
                $("#report-popup").attr("sdate",data.dateStart);
                $("#report-popup").attr("edate",data.dateEnd);
                $("#report-popup").attr("times",arrayTime);
                $("#report-popup").attr("scores",arrayScore);
                $("#report-popup").modal('show');
            }
        },
        error: function () {
            swalNew("", "could not connect to server", "error");
        }

    }).error(function(){
        progress.progressTimer('error', {
            errorText:'ERROR!',
            onFinish:function(){
                swalNew("", "could not connect to server", "error");
            }
        });
    }).done(function(){
        progress.progressTimer('complete');
    });
}

function openReportPreview(){
    $("#report-popup").on('shown.bs.modal', function () {
        var times = $("#report-popup").attr("times").split(",");
        var scores = $("#report-popup").attr("scores").split(",");
        var sdate = $("#report-popup").attr("sdate");
        var edate = $("#report-popup").attr("edate");
        var data = [];
        var topScore = 0;
        var ticksDate = [];
        for(var i = 0 ; i < times.length; i++){
            if(times[i] > 0){
                ticksDate.push(times[i]);
                data.push([times[i],scores[i]]);
                if(parseInt(scores[i]) > parseInt(topScore)){
                    topScore = scores[i];
                }
            }
        }
        if(data.length > 0){
            fillDataReport(topScore);
            drawReport(data,ticksDate,sdate,edate);
        }
    });
}

function fillDataReport(topscore){
    var studentName = $('#listUsers option:selected').val();
    var phoneme = $('#listPhonemes option:selected').text();
    $("#report-popup").find(".student-username").html(studentName);
    $("#report-popup").find(".phoneme").html("phoneme : " + phoneme);
    $("#report-popup").find(".student-avg-score").html(topscore);
}
function dateFrom(){
    $('#dateFrom').datetimepicker({
        format: 'DD/MM/YYYY',
        maxDate: 'now'
    }).on('dp.change',function(e){
        var dateF = $(this).val();
        if(dateF!= "" && dateF.length > 0){
            var sName = $('#listUsers option:selected').val();
            if(sName!=null && typeof sName !="undefined"){
                $("#loadInfo").removeAttr("disabled");
            }
        }else{
            $("#loadInfo").attr("disabled","disabled");
        }
        $('#dateFrom').blur();
    });
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

function initPeriod(){
    $('#period').multiselect('destroy');
    $('#period').multiselect({ enableFiltering: true, buttonWidth: '200px'});
    $('#period').multiselect('refresh');
}

function getTimeStartDate(){

}
$(document).ready(function(){
    help();
    dateFrom();
    loadInfo();
    loadData();
    openReportPreview();
    mouseOverChart();
    collapseMenu();
    initPeriod();
});

