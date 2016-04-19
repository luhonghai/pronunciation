var avg;
var servlet = "/ReportsPhonemes";
var dataReport;
function listStudents(){
        $.ajax({
            url: servlet,
            type: "POST",
            dataType: "json",
            data: {
                action: "loadStudent"
            },
            success: function (data) {
                if(data.message=="success"){
                    $("#listUsers").empty();
                    if(data.listStudent!=null && data.listStudent.length>0){
                        var items=data.listStudent;
                        for(var i=0;i<items.length;i++){
                            $("#listUsers").append('<option value="' + items[i] + '">' + items[i] + '</option>');
                        }

                    }
                    $('#listUsers').multiselect('destroy');
                    $('#listUsers').multiselect({ enableFiltering: true, maxHeight: 200,buttonWidth: '200px'});
                    $('#listUsers').multiselect('refresh');
                }else{
                    swal("", "an error has been occurred in server", "error");
                    $('.row').hide();
                }
            },
            error: function () {
                swal("", "Could not connect to server", "error");
            }

        });
}
function listPhonemes(){
    $.ajax({
        url: servlet,
        type: "POST",
        dataType: "json",
        data: {
            action: "loadPhonemes"
        },
        success: function (data) {
            $("#listPhonemes").empty();
            if(data.listPhonemes!=null && data.listPhonemes.length>0){
                    var items=data.listPhonemes;
                    for(var i=0;i<items.length;i++){
                        $("#listPhonemes").append('<option value="' + items[i].arpabet + '">' + items[i].ipa + '</option>');
                    }
                    $('#listPhonemes').multiselect('destroy');
                    $('#listPhonemes').multiselect({ enableFiltering: true,maxHeight: 200, buttonWidth: '50px'});
                    $('#listPhonemes').multiselect('refresh');
            }else{
                $('#listPhonemes').attr("disabled","disabled");
                $('#listPhonemes').multiselect('destroy');
                $('#listPhonemes').multiselect({ enableFiltering: true, buttonWidth: '50px'});
                $('#listPhonemes').multiselect('refresh');
                swal("", "an error has been occurred in server", "error");
                $('.row').hide();
            }

        },
        error: function () {
            swal("", "Could not connect to server", "error");
        }

    });
}
function loadInfo(){
    $(document).on("click","#loadInfo",function(){
        var studentName = $('#listUsers option:selected').val();
        var phoneme = $("#listPhonemes").val();
        var dateFrom = $("#dateFrom").val();
        var dateTo = $("#dateTo").val();
        loadDataReport(studentName,phoneme,dateFrom,dateTo);
    })
}

function loadDataReport(studentName,phoneme,dateFrom,dateTo){
    $.ajax({
        url: servlet,
        type: "POST",
        dataType: "json",
        data: {
            action: "getReportData",
            studentName:studentName,
            phoneme:phoneme,
            dateFrom:dateFrom,
            dateTo:dateTo
        },
        success: function (data) {
            var arrayTime = [];
            var arrayScore = [];
            var tmp = data.listScorePhonemes;
            var checkInValid = false;
            for(var i=0;i<tmp.length;i++){
                if(tmp[0].serverTime == 0){
                    checkInValid = true;
                }
                arrayTime.push(tmp[i].serverTime);
                arrayScore.push(tmp[i].score);
            }

            if(checkInValid){
                swal("", "We could not found any data to build the report", "error");
            }else{
                $("#report-popup").attr("times",arrayTime);
                $("#report-popup").attr("scores",arrayScore);
                $("#report-popup").modal('show');
            }
        },
        error: function () {
            swal("", "Could not connect to server", "error");
        }

    });
}

function openReportPreview(){
    $("#report-popup").on('shown.bs.modal', function () {
        var times = $("#report-popup").attr("times").split(",");
        var scores = $("#report-popup").attr("scores").split(",");
        var data = [];
        var topScore = 0;
        for(var i = 0 ; i < times.length; i++){
            if(times[i] > 0){
                data.push([times[i],scores[i]]);
                if(scores[i] > topScore){
                    topScore = scores[i];
                }
            }
        }
        if(data.length > 0){
            fillDataReport(topScore);
            drawReport(data);
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
        format: 'DD/MM/YYYY'
    }).on('dp.change',function(e){
        console.log(e);
        var dateF = $(this).val();
        var dateT = $('#dateTo').val();
        if(dateF!= "" && dateF.length > 0 && dateT!="" && dateT.length > 0){
            $("#loadInfo").removeAttr("disabled");
        }else{
            $("#loadInfo").attr("disabled","disabled");
        }
    });
}
function dateTo(){
    $('#dateTo').datetimepicker({
        format: 'DD/MM/YYYY'
    }).on('dp.change',function(e){
        var dateF =  $('#dateFrom').val();
        var dateT = $(this).val();
        if(dateF!= "" && dateF.length > 0 && dateT!="" && dateT.length > 0){
            $("#loadInfo").removeAttr("disabled");
        }else{
            $("#loadInfo").attr("disabled","disabled");
        }
    });
}

$(document).ready(function(){
    $('#help-icons').show();
    dateFrom();
    dateTo();
    loadInfo();
    listStudents();
    listPhonemes();
    openReportPreview();
    mouseOverChart();
});

