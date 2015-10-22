/**
 * Created by CMGT400 on 10/5/2015.
 */
var myTable;
var servletName="ManagementQuestionOfLessonServlet";
var lessonId;

function getUrlVars() {
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for(var i = 0; i < hashes.length; i++)
    {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars;
}

function getIdLesson(){
    lessonId = getUrlVars()["id"];
}

function listWordOfQuestion(){

    myTable = $('#dataTables-example').dataTable({
        "retrieve": true,
        "destroy": true,
        "responsive": true,
        "bProcessing": true,
        "bServerSide": true,

        "ajax": {
            "url": servletName,
            "type": "POST",
            "dataType": "json",
            "data": {
                action: "list",
                lessonId:lessonId
            }
        },

        "columns": [{
            "sWidth": "80%",
            "data": "name",
            "sDefaultContent": ""

        },{
            "sWidth": "20%",
            "data": null,
            "bSortable": false,
            "sDefaultContent": "",
            "mRender": function (data, type, full) {
                //$button = $('<button type="button" style="margin-right:10px" id="edit" class="btn btn-info btn-sm" ' + full[0] + '>' + 'Edit' + '</button>' + '<button type="button" id="delete" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Delete' + '</button>');
                $button = $('<button type="button" id="delete" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Delete' + '</button>');
                $button.attr("id-column", data.id);
                $button.attr("questiom", data.name);
                return $("<div/>").append($button).html();
            }
        }]

    });


}

function getWeightAndPhoneme(listPhonemeName, listWeightName){
    var output = [];
    $(listPhonemeName).find('input').each(function(e){
        var value = $(this).val();
        var index = $(this).attr("index");
        var weight = $(listWeightName + index).val();
        output.push({
            index : parseInt(index),
            phoneme : value,
            weight : parseInt(weight)
        });
    });
    return output;
}

function openPopupAdd(){
    $("#add-word-of-question").click(function(){
        $("#add").modal('show');
        $("#addQuestion").val("");
        $("#select-question").empty();
        $("#yesadd").attr("disabled", true);
        $("#loadQuestion").attr("disabled",false)
        $("#box-select-question").hide();
        //$("#addWord").attr("disabled",false);
    });
}

function addQuestion(){
    //add word click
    $(document).on("click","#yesadd", function(){
        var questionId = $("#select-question option:selected").val();
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "json",
            data: {
                action: "add",
                questionId: questionId,
                lessonId: lessonId
            },
            success: function (data) {
                var message = data.message;
                if(message.indexOf("success")!=-1){
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#add").modal('hide');
                }else{
                    //$("#yesadd").attr("disabled", true);
                    //$("#loadPhonemes").attr("disabled",false)
                    //$("#addWord").attr("disabled",false)
                    swal("Error!",message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });


    });

    //load phonemes click
    $("#loadQuestion").click(function(){
        $("#loadQuestion").attr("disabled",true);
        var question = $("#addQuestion").val();
        if (question == null || typeof question == "undefined" || question.length == 0){
            $("#loadQuestion").attr("disabled",false);
            $("#addQuestion").focus();
            swal("Warning!", "Question not null!", "warning");
            return;
        }
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "json",
            data: {
                action: "listQuestion",
                questionSearch: question,
                lessonId: lessonId
            },
            success: function (data) {
                var message = data.message;
                if(message.indexOf("success") != -1){
                    $("#select-question").empty();
                    $.each(data.data, function (idx, obj) {
                        var phonmeName = obj.phoneme;
                        $("#select-question").append("<option value='"+ obj.id +"'>"+ obj.name+ "</option>");
                    });
                    $("#box-select-question").show();
                    //$("#addWord").attr("idWord", data.id);
                    //$("#loadPhonemes").attr("disabled",true);
                    //$("#addWord").attr("readonly","readonly");
                    //$("#addQuestion").attr("disabled",true);
                    $("#yesadd").attr("disabled", false);
                    $("#loadQuestion").attr("disabled",false);
                }else{
                    $("#yesadd").attr("disabled", true);
                    $("#loadQuestion").attr("disabled",false);
                    $("#addQuestion").focus();
                    swal("Error!",message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    });
}

function openPopupDeletes(){
    $(document).on("click","#delete", function(){
        $("#deletes").modal('show');
        var idd=$(this).attr('id-column');
        $("#iddelete").val(idd);
    });
}

function deleteQuestion(){
    $(document).on("click","#deleteItems", function(){
        var questionId =  $("#iddelete").val();
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "json",
            data: {
                action: "delete",
                questionId: questionId,
                lessonId: lessonId
            },
            success: function (data) {
                var messages=data.message;
                if (messages.indexOf("success") != -1) {
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#deletes").modal('hide');
                }else{
                    swal("Error!",message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    });
}

$(document).ready(function(){
    getIdLesson()
    openPopupAdd();
    addQuestion();
    openPopupDeletes();
    deleteQuestion();
    listWordOfQuestion();
});


