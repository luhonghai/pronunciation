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
                $button = $('<button type="button" style="margin-right:10px" id="edit" class="btn btn-info btn-sm" ' + full[0] + '>' + 'Edit' + '</button>' + '<button style="margin-right:10px" type="button" id="delete" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Delete' + '</button>' + '<a href="ManagementWordOfQuestion.jsp?id='+ data.id +'" type="button" id="addword" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Add Word' + '</a>');
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

function addquestion(){
    $(document).on("click","#yesadd", function(){
        var question = $("#addquestion").val();
        $.ajax({
            url: "ManagementQuestionServlet",
            type: "POST",
            dataType: "text",
            data: {
                add: "add",
                question: question
            },
            success: function (data) {
                if (data.indexOf("success") !=-1) {
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#add").modal('hide');
                }else{
                    swal("Could not add question!", data.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });


    });





}

function add(){
    $(document).on("click","#openAddQuestion", function(){
        $("#add").modal('show');
        $("#addquestion").val("");
    });
}



function deletes(){
    $(document).on("click","#delete", function(){
        $("#deletes").modal('show');
        var idd=$(this).attr('id-column');
        $("#iddelete").val(idd);
    });
}

function DeleteQuestion(){
    $(document).on("click","#deleteItems", function(){
        var id=  $("#iddelete").val();
        $.ajax({
            url: "ManagementQuestionServlet",
            type: "POST",
            dataType: "text",
            data: {
                delete: "delete",
                id: id
            },
            success: function (data) {
                if (data.indexOf("success") !=-1) {
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#deletes").modal('hide');
                }else{
                    swal("Could not delete question!", data.split(":")[1], "error");
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
        var question = $(this).attr('question');
        $("#editquestion").val(question);
        $("#idedit").val(idd);
    });

}

function EditQuestion(){
    $(document).on("click","#yesedit", function(){

        var id = $("#idedit").val();
        var question = $("#editquestion").val();

        $.ajax({
            url: "ManagementQuestionServlet",
            type: "POST",
            dataType: "text",
            data: {
                edit: "edit",
                id: id,
                question: question
            },
            success: function (data) {
                if (data.indexOf("success") !=-1) {
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#edits").modal('hide');
                }else{
                    swal("Could not update question!", data.split(":")[1], "error");
                }

            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });


    });
}


function searchAdvanted(){
    $(document).on("click","#button-filter", function(){
        myTable.fnSettings().ajax = {
            "url": "ManagementQuestionServlet",
            "type": "POST",
            "dataType": "json",
            "data": {
                list: "list",
                CreateDateFrom: $("#CreateDateFrom").val(),
                CreateDateTo: $("#CreateDateTo").val()
            }
        };
        $("tbody").html("");
        myTable.fnDraw();


    });
}


$(document).ready(function(){
    dateFrom();
    dateTo();
    add();
    addquestion();
    edit();
    EditQuestion();
    deletes();
    DeleteQuestion();
    listQuestion();
    searchAdvanted();
});


