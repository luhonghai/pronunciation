/**
 * Created by CMGT400 on 10/5/2015.
 */
var myTable;
var iphonemes=0;
var iphonemess=0;
var listPhoneme;

function listTranscription(){

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
            "url": "ManagementWordServlet",
            "type": "POST",
            "dataType": "json",
            "data": {
                list: "list"

            }
        },

        "columns": [{
            "sWidth": "20%",
            "data": "word",
            "sDefaultContent": ""

        }, {
            "sWidth": "15%",
            "data": "pronunciation",
            "bSortable": false,
            "sDefaultContent": ""
        }, {
            "sWidth": "35%",
            "data": "definition",
            "bSortable": false,
            "sDefaultContent": ""
        },{
            "sWidth": "3%",
            "data": null,
            "bSortable": false,
            "sDefaultContent":"",
            "mRender": function (data, type, full) {
                if (data.mp3Path!=null) {
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
            "sWidth": "15%",
            "data": null,
            "bSortable": false,
            "sDefaultContent": "",
            "mRender": function (data, type, full) {
                $button = $('<button type="button" style="margin-right:10px" id="edit" class="btn btn-info btn-sm" ' + full[0] + '>' + 'Edit' + '</button>' + '<button type="button" id="delete" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Delete' + '</button>');
                $button.attr("id-column", data.id);
                $button.attr("word", data.word);
                $button.attr("pronunciation", data.pronunciation);
                $button.attr("definition", data.definition);
                $button.attr("mp3Path", data.mp3Path);
                return $("<div/>").append($button).html();
            }
        }]

    });


}


function addWord(){
    $(document).on("click","#yesadd", function(){
        var i;
        var word = {
            word: $("#addWord").val(),
            definition: $("#addDifinition").val(),
            pronunciation: $("#addpronunciation").val(),
            mp3Path : $("#addPath").val(),
            phonemes : []
        };
        for(i=0;i<iphonemes;i++){
            word.phonemes.push({
                index: i,
                phoneme: $("#"+i+"").val()
            });
        }
        $.ajax({
            url: "ManagementWordServlet",
            type: "POST",
            dataType: "text",
            data: {
                add: "add",
                word: JSON.stringify(word)
            },
            success: function (data) {
                var messages=JSON.parse(data);
                if (messages.message.indexOf("success:") !=-1) {
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#add").modal('hide');
                }
                if(messages.message.indexOf("error:")!=-1){
                    alert("Word is existed ");
                    $("#add").modal('hide');
                }
            },
            error: function () {
                alert("error");
            }

        });


    });





}

function add(){
    $(document).on("click","#addUser", function(){
        $("#add").modal('show');
        $("#addphoneme").html("");
        iphonemes=0;
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
            url: "ManagementWordServlet",
            type: "POST",
            dataType: "text",
            data: {
                delete: "delete",
                id: id
            },
            success: function (data) {
                var messages=JSON.parse(data);
                if (messages.message == "success") {
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#deletes").modal('hide');
                }
            },
            error: function () {
                alert("error");
            }

        });
    });
}

function edit(){
    $(document).on("click","#edit", function() {
        $("#edits").modal('show');
        $("#listPhonemes").html("");
        $("#addphonemeEdit").html("");
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
            url: "ManagementWordServlet",
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
                    for (i = 0; i < listPhoneme.phonemes.length; i++) {
                        $("#listPhonemes").append("<div class='col-sm-9 col-sm-offset-3' ><input type='text' id=" + listPhoneme.phonemes[i].index + " class='form-control' value='" + listPhoneme.phonemes[i].phoneme + "'></div>");
                        $("#"+listPhoneme.phonemes[i].index+"").css("padding-left: 0px;");
                    }
                }
            },
            error: function () {
                alert("error");
            }

        });

    });

}

function addPhonemesEdit(){
    $(document).on("click","#addPhonemesEdit", function(){
        if(listPhoneme!=null){
            var size=listPhoneme.phonemes.length;
            iphonemess=size;
        }
        $("#addphonemeEdit").append("<div class='col-sm-9 col-sm-offset-3' ><input type='text' id='"+iphonemess+"' class='form-control'></div>");
        $("#"+iphonemess+"").css("padding-left: 0px;");
        iphonemess=iphonemess+1;

    });
}


function editWord(){
    $(document).on("click","#yesedit", function(){
        var word = {
            id : $("#idedit").val(),
            definition: $("#editDifinition").val(),
            mp3Path : $("#editPath").val(),
            phonemes : []
        };
        var i;
        for(i=0;i<iphonemess;i++){
            word.phonemes.push({
                index: i,
                phoneme: $("#"+i+"").val()
            });
        }

        $.ajax({
            url: "ManagementWordServlet",
            type: "POST",
            dataType: "text",
            data: {
                edit: "edit",
                word: JSON.stringify(word)// to json word,
            },
            success: function (data) {
                if (data == "success") {
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#edits").modal('hide');
                }

            },
            error: function () {
                alert("error");
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
function addPhonemes(){
    $(document).on("click","#addPhonemes", function(){
        $("#addphoneme").append("<div class='col-sm-9 col-sm-offset-3' ><input type='text' id='"+iphonemes+"' class='form-control'></div>");
        $("#"+iphonemes+"").css("padding-left: 0px;");
        iphonemes=iphonemes+1;

    });
}




$(document).ready(function(){
    var roleAdmin=$("#role").val();
    add();
    addWord();
    edit();
    editWord();
    deletes();
    deleteWord();
    addPhonemes();
    addPhonemesEdit();
    listTranscription();
});


