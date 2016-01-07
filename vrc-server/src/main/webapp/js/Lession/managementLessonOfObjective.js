/**
 * Created by CMGT400 on 10/5/2015.
 */
var myTable;
var servletName="ManagementObjective";
var objId;

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
    objId = getUrlVars()["id"];
}

function listLesson(){

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
                id:objId
            }
        },

        "columns": [
            {
                "sWidth": "15%",
                "data": "index",
                "bSortable": false,
                "sDefaultContent": ""
            }, {
                "sWidth": "15%",
                "data": "nameUnique",
                "bSortable": false,
                "sDefaultContent": ""
            }, {
                "sWidth": "25%",
                "data": null,
                "bSortable": false,
                "sDefaultContent": "",
                "mRender": function (data, type, full) {
                    $button = $('<button type="button" style="margin-right:5px" id="change" class="btn btn-info btn-sm" ' + full[0] + '>' + 'Change Index' + '</button>' + '<button style="margin-right:5px" type="button" id="remove" class="btn btn-info btn-sm" ' + full[0] + '>' + ' remove' + '</button>');
                    $button.attr("id-column", data.id);
                    $button.attr("index", data.index);
                    return $("<div/>").append($button).html();
                }
        }]
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
    openPopupDeletes();
    deleteQuestion();
    listLesson();
});


