/**
 * Created by CMGT400 on 10/5/2015.
 */
var myTable;

function listTranscription(){

    myTable = $('#dataTables-example').dataTable({
        "retrieve": true,
        "destroy": true,
        "responsive": true,
        "bProcessing": true,
        "bServerSide": true,

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
            "sWidth": "35%",
            "data": "definition",
            "sDefaultContent": ""
        }, {
            "sWidth": "15%",
            "data": "pronunciation",
            "sDefaultContent": ""
        },{
            "sWidth": "3%",
            "data": null,
            "bSortable": false,
            "sDefaultContent":"",
            "mRender": function (data, type, full) {
                var audioUrl =data.mp3Path
                $divs=$('<div id="'+data.id+'" class="cp-jplayer">' + '</div>' +
                '<div class="prototype-wrapper"> ' +
                '<div id="'+data.id+"s"+'" class="cp-container">' +
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
                '<li>'+'<a class="cp-play" tabindex="1">' + 'play' + '</a>' + '</li>' +
                '<li>'+'<a class="cp-pause" style="display:none;" tabindex="1">' + 'pause' + '</a>' + '</li>' +
                '</ul>' +
                '</div>');
                $divs.attr("audioUrl", audioUrl);
                return $("<div/>").append($divs).html();
                //return '<i class="fa fa-file-audio-o fa-2x"></i>';
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
                $button.attr("phonemes", data.phonemes);
                return $("<div/>").append($button).html();
            }
        }]

    });


}


function addWord(){
    $(document).on("click","#yesadd", function(){
        var word = $("#addWord").val();
        var definition = $("#addDifinition").val();
        var pronunciation = $("#addpronunciation").val();
        var mp3Url = $("#addPath").val();

        $.ajax({
            url: "ManagementWordServlet",
            type: "POST",
            dataType: "text",
            data: {
                add: "add",
                word: word,
                definition:definition,
                pronunciation:pronunciation,
                mp3Url:mp3Url
            },
            success: function (data) {
                if (data == "success") {
                    $("tbody").html("");
                    myTable.fnDraw();
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
                if (data == "success") {
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
        var idd = $(this).attr('id-column');
        var word = $(this).attr('word');
        var pronunciation = $(this).attr('pronunciation');
        var definition = $(this).attr('definition');
        var mp3Path = $(this).attr('mp3Path');
        $("#editWord").val(word);
        $("#idedit").val(idd);
        $("#editpronunciation").val(pronunciation);
        $("#editDifinition").val(definition);
        $("#editPath").val(mp3Path);
        $.ajax({
            url: "ManagementWordServlet",
            type: "POST",
            dataType: "json",
            data: {
                listPhonemes: "listPhonemes",
                id: id
            },
            success: function (data) {

            },
            error: function () {
                alert("error");
            }

        });

    });

}

function editWord(){
    $(document).on("click","#yesedit", function(){

        var id = $("#idedit").val();
        var word = $("#editWord").val();
        var pronunciation = $("#editpronunciation").val();
        var definition = $("#editDifinition").val();
        var mp3Path = $("#editPath").val();

        $.ajax({
            url: "ManagementWordServlet",
            type: "POST",
            dataType: "text",
            data: {
                edit: "edit",
                id: id,
                word: word,
                pronunciation:pronunciation,
                definition:definition,
                mp3Path:mp3Path
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




$(document).ready(function(){
    var roleAdmin=$("#role").val();
    add();
    addWord();
    edit();
    editWord();
    deletes();
    deleteWord();
    listTranscription();
});


