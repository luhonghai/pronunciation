/**
 * Created by CMGT400 on 10/8/2015.
 */
var myTable;
var servletName="ManagementObjective";

function searchAdvanted(){
    $(document).on("click","#button-filter", function(){
        myTable.fnSettings().ajax = {
            "url": servletName,
            "type": "POST",
            "dataType": "json",
            "data": {
                action: "listall",
                description: $("#description").val(),
                CreateDateFrom: $("#CreateDateFrom").val(),
                CreateDateTo: $("#CreateDateTo").val()
            }
        };
        $("tbody").html("");
        myTable.fnDraw();
    });
}

function listObj(){

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
                action: "listall",
                description: $("#description").val(),
                CreateDateFrom: $("#CreateDateFrom").val(),
                CreateDateTo: $("#CreateDateTo").val()
            }
        },

        "columns": [
           {
                "sWidth": "15%",
                "data": "name",
                "bSortable": false,
                "sDefaultContent": ""
            }, {
                "sWidth": "15%",
                "data": "description",
                "bSortable": false,
                "sDefaultContent": ""
             }, {
                "sWidth": "15%",
                "data": "dateCreated",
                "sDefaultContent": ""
            }, {
                "sWidth": "25%",
                "data": null,
                "bSortable": false,
                "sDefaultContent": "",
                "mRender": function (data, type, full) {
                    $button = $('<button type="button" style="margin-right:5px" id="edit" class="btn btn-info btn-sm" ' + full[0] + '>' + 'Edit' + '</button>' + '<button style="margin-right:5px" type="button" id="delete" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Delete' + '</button>'
                        + '<a href="question-of-lesson-management.jsp?id='+ data.id +'" type="button" id="addQuestion" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Add Lessons ' + '</a>');
                    $button.attr("id-column", data.id);
                    $button.attr("name", data.name);
                    $button.attr("description", data.description);
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

function openPopupAdd(){
    $(document).on("click","#openAddObjective", function(){
        $("#add-objective").modal('show');
        $("#add-objective-name").val("");
        $("#add-description").val("");
        $("#action").val("Add");
    });
}

function openPopupEdit(){
    $(document).on("click","#edit", function(){
        $("#add-objective").modal('show');
        var name = $(this).attr('name');
        var description = $(this).attr('description');
        var id = $(this).attr("id-column");
        $("#add-objective-name").val(name);
        $("#add-description").val(description);
        $("#id-obj").val(id);
        $("#action").val("Edit");
    });

}

function openPopupDelete(){
    $(document).on("click","#delete", function(){
        var id = $(this).attr("id-column");
        $("#deletes").modal('show');
        $("#label_check_delete").html("Please wait when we check the mapping data of this objective!");
        $("#deleteItems").attr("disabled","disabled");
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "text",
            data: {
                action: "checkmapping",
                id: id
            },
            success: function (data) {
                $("#deleteItems").removeAttr("disabled");
                $("#label_check_delete").html(data);
                $("#deleteItems").attr('id',id);
            },
            error: function () {
                $("#deletes").modal('hide');
                swal("Error!", "Could not connect to server", "error");
            }

        });
    });
}

function deleteObj(){
    $(document).on("click","#deleteItems", function(){
        var id = $(this).attr("id");
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "text",
            data: {
                action: "Delete",
                id: id
            },
            success: function (data) {
                if (data.indexOf("success") !=-1) {
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#deletes").modal('hide');
                    swal("Success!", action+" Objective success!", "success");
                }else{
                    swal("Could not "+action+" Objective!", data.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    });
}


function doAction(){
    $(document).on("click","#yesadd", function(){
        var name = $("#add-objective-name").val();
        var description = $("#add-description").val();
        var action = $("#action").val();
        var id = $("#id-obj").val();
        if (name == null || typeof name == "undefined" || name.length == 0){
            $("#add-objective-name").focus();
            swal("Warning!", "Please enter a Objective name!", "warning");
            return;
        }
        if (description == null || typeof description == "undefined" || description.length == 0){
            $("#add-description").focus();
            swal("Warning!", "Please enter a Objective description!", "warning");
            return;
        }

        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "text",
            data: {
                action: action,
                id : id,
                name: name,
                description:description
            },
            success: function (data) {
                if (data.indexOf("success") !=-1) {
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#add-objective").modal('hide');
                    swal("Success!", action+" Objective success!", "success");
                }else{
                    swal("Could not "+action+" Objective!", data.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });

    });

}




$(document).ready(function(){
    dateFrom();
    dateTo();
    listObj();
    searchAdvanted();
    openPopupDelete();
    openPopupAdd();
    openPopupEdit();
    deleteObj();
    doAction();
});


