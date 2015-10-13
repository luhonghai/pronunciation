/**
 * Created by CMGT400 on 10/5/2015.
 */
var myTable;
var iphonemes=0;
var iphonemess=0;
var listPhoneme;
var servletName="ManagementWordOfQuestionServlet";

function getUrlVars()
{
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



function listWordOfQuestion(questionId){

    myTable = $('#dataTables-example').dataTable({
        "retrieve": true,
        "destroy": true,
        "responsive": true,
        "bProcessing": true,
        "bServerSide": true,
        "fnDrawCallback": function( oSettings ) {
            loadAudio();
        },

        "ajax": {
            "url": servletName,
            "type": "POST",
            "dataType": "json",
            "data": {
                list: "list",
                questionId:questionId
            }
        },

        "columns": [{
            "sWidth": "40%",
            "data": "word",
            "sDefaultContent": ""

        }, {
            "sWidth": "30%",
            "data": "pronunciation",
            "bSortable": false,
            "sDefaultContent": ""
        }, {
            "sWidth": "10%",
            "data": null,
            "bSortable": false,
            "sDefaultContent":"",
            "mRender": function (data, type, full) {
                if (typeof data.mp3Path!='undefined' && data.mp3Path!=null && data.mp3Path.length>0) {
                    var audioUrl = data.mp3Path;
                    $divs = $('<div id="' + data.id + '" class="cp-jplayer">' + '</div>' +
                    '<div class="prototype-wrapper"> ' +
                    '<div id="' + data.id + "s" + '" class="cp-container">' +
                    '<div class="cp-buffer-holder"> ' +
                    '<div class="cp-buffer-1">' + '</div>' +
                    '<div class="cp-buffer-2">' + '</div>' +
                    '</div>' +
                    '<div class="cp-progress-holder">' +
                    '<div class="cp-progress-1">' + '</div>' +
                    '<div class="cp-progress-2">' + '</div>' +
                    '</div>' +
                    '<div class="cp-circle-control">' + '</div>' +
                    '<ul class="cp-controls">' +
                    '<li>' + '<a class="cp-play" tabindex="1">' + 'play' + '</a>' + '</li>' +
                    '<li>' + '<a class="cp-pause" style="display:none;" tabindex="1">' + 'pause' + '</a>' + '</li>' +
                    '</ul>' +
                    '</div>');
                    $divs.attr("audioUrl", audioUrl);
                    return $("<div/>").append($divs).html();
                    //return '<i class="fa fa-file-audio-o fa-2x"></i>';
                }
            }
        },{
            "sWidth": "20%",
            "data": null,
            "bSortable": false,
            "sDefaultContent": "",
            "mRender": function (data, type, full) {
                $button = $('<button type="button" style="margin-right:10px" id="edit" class="btn btn-info btn-sm" ' + full[0] + '>' + 'Edit' + '</button>' + '<button type="button" id="delete" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Delete' + '</button>');
                $button.attr("id-column", data.id);
                $button.attr("word", data.word);
                $button.attr("pronunciation", data.pronunciation);
                $button.attr("mp3Path", data.mp3Path);
                return $("<div/>").append($button).html();
            }
        }]

    });


}

function getWeightAndPhoneme(){
    var output = [];
    $("#listPhonmes").find('input').each(function(e){
        var value = $(this).val();
        var index = $(this).attr("index");
        var weight = $('#weight' + index).val();
        output.push({
            index : parseInt(index),
            phoneme : value,
            weight : parseInt(weight)
        });
    });
    return output;
}


function addWord(questionId){
    //add word click
    $(document).on("click","#yesadd", function(){
        var txtWord=$("#addWord").val();
        if (txtWord == null || typeof txtWord == "undefined" || txtWord.length == 0){
            swal("Warning!", "Word not null!", "warning");
            return;
        }
        var word = {
            idWord: $("#addWord").attr("idWord"),
            idQuestion: questionId,
            data:getWeightAndPhoneme()
        };
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "json",
            data: {
                add: "add",
                word: JSON.stringify(word)
            },
            success: function (data) {
                var message = data.message;
                if(message.indexOf("success")!=-1){
                    //set successfull leen
                    //alert("add success!");
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#add").modal('hide');
                }else{
                    swal("Error!",message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });


    });

    $('.phoneme-weight').on("input",function(){
        //alert('is number');
    });

    //load phonemes click
    $("#loadPhonemes").click(function(){
        var word = $("#addWord").val();
        if (word == null || typeof word == "undefined" || word.length == 0){
            swal("Warning!", "Word not null!", "warning");
            return;
        }
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "json",
            data: {
                listPhonemes: "listPhonemes",
                word: word
            },
            success: function (data) {
                var message = data.message;
                if(message.indexOf("success") != -1){
                    $("#addWord").attr("idWord", data.id);
                    $("#listPhonmes").html("");
                    $("#listWeight").html("");
                    $.each(data.phonemes, function (idx, obj) {
                        var phonmeName = obj.phoneme;
                        //alert(jsonItem);
                        $("#listPhonmes").append('<input index="'+obj.index+'" value="'+phonmeName+'"  type="text">');
                        $("#listWeight").append('<input id="weight'+obj.index+'" class="phoneme-weight" type="text">');
                        $("#listPhonmes").css({"width":(idx+1)*35});
                        $("#listWeight").css({"width":(idx+1)*35});
                    });
                    $("#yesadd").show();
                }else{
                    swal("Error!",message.split(":")[1], "error");
                    $("#listPhonmes").html("");
                    $("#listWeight").html("");
                    $("#yesadd").hide();
                }
                /*
                var messages=JSON.parse(data);
                if (messages.message.indexOf("success") !=-1) {
                    $("tbody").html("");
                    myTable.fnDraw();
                    //$("#add").modal('hide');
                }
                if(messages.message.indexOf("error")!=-1){
                    swal("Error!", messages.message, "error");
                    $("#add").modal('hide');
                }*/
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    });
}



function add(){
    $(document).on("click","#addUser", function(){
        $("#add").modal('show');
        $("#addWord").val("");
        $("#listPhonmes").html("");
        $("#listWeight").html("");
        $("#yesadd").hide();
        //$("#addWord").val("");
        //$("#addpronunciation").val("");
        //$("#addDifinition").val("");
        //$("#addPath").val("");
    });
}



function deletes(){
    $(document).on("click","#delete", function(){
        $("#deletes").modal('show');
        var idd=$(this).attr('id-column');
        $("#iddelete").val(idd);
    });
}

function deleteWord(){
    $(document).on("click","#deleteItems", function(){
        var id=  $("#iddelete").val();
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "text",
            data: {
                delete: "delete",
                id: id
            },
            success: function (data) {
                var messages=JSON.parse(data);
                if (messages.message.indexOf("success") !=-1) {
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#deletes").modal('hide');
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    });
}

function edit(questionId){
    $(document).on("click","#edit", function() {
        $("#edits").modal('show');
        var idWord = $(this).attr('id-column');
        var word = $(this).attr('word');
        $("#idedit").val(idWord);
        $("#editWord").val(word);

        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "json",
            data: {
                listPhonemesEdit: "listPhonemesEdit",
                idWord: idWord,
                idQuestion: questionId
            },
            success: function (data) {
                var message = data.message;
                if(message.indexOf("success") != -1){
                    //$("#addWord").attr("idWord", data.id);
                    $("#listPhonmesEdit").html("");
                    $("#listWeightEdit").html("");
                    $.each(data.listWeightPhoneme, function (idx, obj) {
                        var phonemeName = obj.phoneme;
                        var weightOfPhoneme = obj.weight;
                        //alert(jsonItem);
                        $("#listPhonmesEdit").append('<input index="'+obj.index+'" value="'+phonemeName+'"  type="text">');
                        $("#listWeightEdit").append('<input id="weight'+obj.index+'" class="phoneme-weight" value="'+weightOfPhoneme+'" type="text">');
                        $("#listPhonmesEdit").css({"width":(idx+1)*35});
                        $("#listWeightEdit").css({"width":(idx+1)*35});
                    });
                    //$("#yesadd").show();
                }else{
                    swal("Error!",message.split(":")[1], "error");
                    //$("#listPhonmes").html("");
                    //$("#listWeight").html("");
                    //$("#yesadd").hide();
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });

    });

}

function readPhones(txt) {
    if (txt == null || typeof txt == 'undefined' || txt.length == 0) return null;
    var txt1=txt.toLocaleUpperCase();
    var data =  txt1.split(" ");
    var output = [];
    for (var i = 0; i < data.length; i++) {
        output.push({
           index : i,
            phoneme : data[i]
        });
    }
    return output;
}

function editWord(){
    $(document).on("click","#yesedit", function(){
        var listphones=$("#editPhoneme").val();
        var word = {
            id : $("#idedit").val(),
            definition: $("#editDifinition").val(),
            mp3Path : $("#editPath").val(),
            phonemes : readPhones(listphones)
        };
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "text",
            data: {
                edit: "edit",
                word: JSON.stringify(word)// to json word,
            },
            success: function (data) {
                var messages=JSON.parse(data);
                if (messages.message.indexOf("success") !=-1) {
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#edits").modal('hide');
                }else{
                    swal("Error!", "Could not connect to server", "error");
                }

            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });


    });
}
function loadAudio(){
    $('.cp-jplayer').each(function() {
        var id = $(this).attr('id');
        var audioUrl = $(this).attr('audioUrl');
        new CirclePlayer("#" + id,
            {
                mp3: audioUrl
               // wav: audioUrl + "&type=wav"
            }, {
                cssSelectorAncestor: '#' + id + 's'
            });

    });

}
//function addPhonemes(){
//    $(document).on("click","#addPhonemes", function(){
//        $("#addphoneme").append("<div class='col-sm-9 col-sm-offset-3' ><input type='text' id='"+iphonemes+"' class='form-control'></div>");
//        $("#"+iphonemes+"").css({"padding-left": "0px", "margin-bottom": "5px"});
//        iphonemes=iphonemes+1;
//
//    });
//}




$(document).ready(function(){
    var roleAdmin=$("#role").val();
    var questionId = getUrlVars()["id"];
    add();
    addWord(questionId);
    edit(questionId);
    editWord();
    deletes();
    deleteWord();
    listWordOfQuestion(questionId);
});


