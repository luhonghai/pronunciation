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
        var listphonemes=$("#addPhoneme").val();
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
                    alert("add success!");
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

    //load phonemes click
    $("#loadPhonemes").click(function(){
        var word = $("#addWord").val();
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "json",
            data: {
                listPhonemes: "listPhonemes",
                word: word
            },
            success: function (data) {
                if(typeof data!="undefined") {
                    $("#addWord").attr("idWord", data.id);
                    $("#listPhonmes").html("");
                    $("#listWeight").html("");
                    $.each(data.phonemes, function (idx, obj) {
                        var phonmeName = obj.phoneme;
                        //alert(jsonItem);
                        //$("#listPhonmes").append('<input index="'+obj.index+'" value="'+phonmeName+'"  type="text" style="padding-left: 0px;margin-bottom: 5px;width: 30px;">');
                        //$("#listWeight").append('<input id="weight'+obj.index+'"   type="text" style="padding-left: 0px;margin-bottom: 5px;width: 30px;">');
                    });
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
        $("#addPhoneme").val("");
        $("#addWord").val("");
        $("#addpronunciation").val("");
        $("#addDifinition").val("");
        $("#addPath").val("");
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

function edit(){
    $(document).on("click","#edit", function() {
        $("#edits").modal('show');
        var idd = $(this).attr('id-column');
        var definition = $(this).attr('definition');
        var mp3Path = $(this).attr('mp3Path');
        var word = $(this).attr('word');
        var pronunciation = $(this).attr('pronunciation');
        $("#idedit").val(idd);
        $("#editDifinition").val(definition);
        $("#editPath").val(mp3Path);
        $("#editWord").val(word);
        $("#editPronunciation").val(pronunciation);


        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "json",
            data: {
                listPhonemes: "listPhonemes",
                id: idd
            },
            success: function (data) {
                listPhoneme=data;
                if(typeof listPhoneme!="undefined") {
                    var i;
                    var phones = [];
                    for (i = 0; i < listPhoneme.phonemes.length; i++) {
                        phones.push(listPhoneme.phonemes[i].phoneme);
                        //$("#listPhonemes").append("<div class='col-sm-8 col-sm-offset-4' ><input type='text' id=" + listPhoneme.phonemes[i].index + " class='form-control' value='" + listPhoneme.phonemes[i].phoneme + "'></div>");
                        //$("#"+listPhoneme.phonemes[i].index+"").css({"padding-left": "0px", "margin-bottom": "5px"});
                    }
                    var txtPhones = phones.join(" ");
                    $("#editPhoneme").val(txtPhones);

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
    edit();
    editWord();
    deletes();
    deleteWord();
    listWordOfQuestion(questionId);
});


