var myTable;

function listTranscription(){

    myTable = $('#dataTables-example').dataTable({
        "retrieve": true,
        "destroy": true,
        "responsive": true,
        "bProcessing": true,
        "bServerSide": true,

        "ajax": {
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
        },

        "columns": [{
            "sWidth": "20%",
            "data": "author",
            "sDefaultContent": ""

        }, {
            "sWidth": "35%",
            "data": "sentence",
            "sDefaultContent": ""
        }, {
            "sWidth": "15%",
            "data": "createdDate",
            "sDefaultContent": ""
        }, {
            "sWidth": "15%",
            "data": "modifiedDate",
            "sDefaultContent": ""
        },{
            "sWidth": "15%",
            "data": null,
            "bSortable": false,
            "sDefaultContent": "",
            "mRender": function (data, type, full) {
                $button = $('<button type="button" style="margin-right:10px" id="edit" class="btn btn-info btn-sm" ' + full[0] + '>' + 'Edit' + '</button>' + '<button type="button" id="delete" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Delete' + '</button>');
                $button.attr("id-column", data.id);
                $button.attr("first", data.firstName);
                $button.attr("last", data.lastName);
                $button.attr("role", data.role);
                return $("<div/>").append($button).html();
            }
        }]

    });


}

function dateFrom(){
    $('#DateFrom').datetimepicker({
        format: 'DD/MM/YYYY'
    });
}
function dateTo(){
    $('#DateTo').datetimepicker({
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
        var sentence = $(this).attr('company');
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
    add();
    addsentence();
    edit();
    editsentence();
    deletes();
    deletesentence();
    listTranscription();
    searchAdvanted();
});


