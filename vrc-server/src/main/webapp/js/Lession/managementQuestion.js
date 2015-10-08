/**
 * Created by CMGT400 on 10/8/2015.
 */
var myTable;

function listQuestion(){

    myTable = $('#dataTables-example').dataTable({
        "retrieve": true,
        "destroy": true,
        "responsive": true,
        "bProcessing": true,
        "bServerSide": true,

        "ajax": {
            "url": "ManagementQuestionServlet",
            "type": "POST",
            "dataType": "json",
            "data": {
                list: "list",
                question: $("#question").val(),
                CreateDateFrom: $("#CreateDateFrom").val(),
                CreateDateTo: $("#CreateDateTo").val()
            }
        },

        "columns": [{
            "sWidth": "50%",
            "data": "name",
            "sDefaultContent": ""
        }, {
            "sWidth": "25%",
            "data": "timeCreated",
            "sDefaultContent": ""
        }, {
            "sWidth": "25%",
            "data": null,
            "bSortable": false,
            "sDefaultContent": "",
            "mRender": function (data, type, full) {
                $button = $('<button type="button" style="margin-right:10px" id="edit" class="btn btn-info btn-sm" ' + full[0] + '>' + 'Edit' + '</button>' + '<button type="button" id="delete" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Delete' + '</button>' + '<button type="button" id="addword" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Add Word' + '</button>');
                $button.attr("id-column", data.id);
                $button.attr("question", data.name);
                return $("<div/>").append($button).html();
            }
        }]

    });


}

function dateFrom(){
    $('#CreateDateFrom').datetimepicker({
        format: 'DD/MM/YYYY'
    });
}
function dateTo(){
    $('#CreateDateTo').datetimepicker({
        format: 'DD/MM/YYYY'
    });
}

function dateFrom1(){
    $('#ModifiedDateFrom').datetimepicker({
        format: 'DD/MM/YYYY'
    });
}
function dateTo1(){
    $('#ModifiedDateTo').datetimepicker({
        format: 'DD/MM/YYYY'
    });
}

function addsentence(){
    $(document).on("click","#yesadd", function(){
        var sentence = $("#addsentence").val();
        $.ajax({
            url: "TranscriptionServlet",
            type: "POST",
            dataType: "text",
            data: {
                add: "add",
                sentence: sentence
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
        $("#addusername").val("");
    });
}



function deletes(){
    $(document).on("click","#delete", function(){
        $("#deletes").modal('show');
        var idd=$(this).attr('id-column');
        $("#iddelete").val(idd);
    });
}

function deletesentence(){
    $(document).on("click","#deleteItems", function(){
        var id=  $("#iddelete").val();
        $.ajax({
            url: "TranscriptionServlet",
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
        var sentence = $(this).attr('sentence');
        $("#editsentence").val(sentence);
        $("#idedit").val(idd);
    });

}

function editsentence(){
    $(document).on("click","#yesedit", function(){

        var id = $("#idedit").val();
        var sentence = $("#editsentence").val();

        $.ajax({
            url: "TranscriptionServlet",
            type: "POST",
            dataType: "text",
            data: {
                edit: "edit",
                id: id,
                sentence: sentence
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


function searchAdvanted(){
    $(document).on("click","#button-filter", function(){
        myTable.fnSettings().ajax = {
            "url": "TranscriptionServlet",
            "type": "POST",
            "dataType": "json",
            "data": {
                list: "list",
                sentence: $("#sentence").val(),
                CreateDateFrom: $("#CreateDateFrom").val(),
                CreateDateTo: $("#CreateDateTo").val(),
                ModifiedDateFrom: $("#ModifiedDateFrom").val(),
                ModifiedDateTo: $("#ModifiedDateTo").val()
            }
        };
        $("tbody").html("");
        myTable.fnDraw();


    });
}


$(document).ready(function(){
    var roleAdmin=$("#role").val();
    dateFrom();
    dateTo();
    dateFrom1();
    dateTo1();
    add();
    addsentence();
    edit();
    editsentence();
    deletes();
    deletesentence();
    listQuestion();
    searchAdvanted();
});


