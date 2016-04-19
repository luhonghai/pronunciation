var avg;
var servlet = "/ReportsPhonemes"
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
                    $('#listUsers').multiselect({ enableFiltering: true, buttonWidth: '200px'});
                    $('#listUsers').multiselect('refresh');
                }else{
                    swal("", data.message.split(":")[1], "error");
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
                    $('#listPhonemes').multiselect({ enableFiltering: true, buttonWidth: '50px'});
                    $('#listPhonemes').multiselect('refresh');
            }else{
                $('#listPhonemes').attr("disabled","disabled");
                $('#listPhonemes').multiselect('destroy');
                $('#listPhonemes').multiselect({ enableFiltering: true, buttonWidth: '50px'});
                $('#listPhonemes').multiselect('refresh');
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
        url: "ReportsLessons",
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

        },
        error: function () {
            swal("", "Could not connect to server", "error");
        }

    });
}

function openReportPreview(){
    $("#report-popup").on('shown.bs.modal', function () {

    });
}
function dateFrom(){
    $('#dateFrom').datetimepicker({
        format: 'DD/MM/YYYY'
    });
}
function dateTo(){
    $('#dateTo').datetimepicker({
        format: 'DD/MM/YYYY'
    });
}

$(document).ready(function(){
    $('#help-icons').show();
    dateFrom();
    dateTo();
    loadInfo();
    listStudents();
    listPhonemes();
});

