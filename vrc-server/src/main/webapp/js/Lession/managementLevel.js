/**
 * Created by CMGT400 on 10/8/2015.
 */
var myTable;
var servletName="ManagementLevelServlet";
var lessonName;
var isDemos=false;
function listLevels(){

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
                list: "list",
                description: $("#description").val(),
                CreateDateFrom: $("#CreateDateFrom").val(),
                CreateDateTo: $("#CreateDateTo").val()
            }
        },

        "columns": [{
            "sWidth": "25%",
            "data": "name",
            "sDefaultContent": ""
        }, {
            "sWidth": "30%",
            "data": "description",
            "bSortable": false,
            "sDefaultContent": ""
        }, {
            "sWidth": "20%",
            "data": "dateCreated",
            "sDefaultContent": ""
        }, {
            "sWidth": "5%",
            "data": null,
            "bSortable": false,
            "sDefaultContent": "",
            "mRender": function (data, type, full) {
            return '<label type="text" style="background-color: ' + data.color + '; margin-right:10px; width:100px; height:30px;">'+'</label>';
            }
        }, {
            "sWidth": "20%",
            "data": null,
            "bSortable": false,
            "sDefaultContent": "",
            "mRender": function (data, type, full) {
                $button = $('<button type="button" style="margin-right:10px" id="edit" class="btn btn-info btn-sm" ' + full[0] + '>' + 'Edit' + '</button>' + '<button style="margin-right:10px" type="button" id="delete" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Delete' + '</button>');
                $button.attr("id-column", data.id);
                $button.attr("level", data.name);
                $button.attr("description", data.description);
                $button.attr("color", data.color);
                $button.attr("isDemo", data.isDemo);
                $button.attr("isDefaultActivated", data.isDefaultActivated);
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
    $(document).on("click","#openAddLevel", function(){
        $("#add").modal('show');
        $("#addLevel").val("");
        $("#addDescription").val("");
        $("#addColor").val("");
        $("#idDemoAdd").prop('checked', false);
        $("#isDefaultActivatedadd").prop('checked', false);
    });
}

function addLevel(){
    $(document).on("click","#yesadd", function(){
        var level = $("#addLevel").val();
        var description = $("#addDescription").val();
        var color = $("#addColor").val();
        var isDemo = isDemoAdd();
        var isDefaultActivated=isDefaultActivatedADD();
        if (level == null || typeof level == "undefined" || level.length == 0){
            $("#addLevel").focus();
            swal("Warning!", "Please enter a level name!", "warning");
            return;
        }
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "text",
            data: {
                add: "add",
                level: level,
                description:description,
                color:color,
                isDemo:isDemo,
                isDefaultActivated:isDefaultActivated
            },
            success: function (data) {
                if (data.indexOf("success") !=-1) {
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#add").modal('hide');
                    isDemos=false;
                    swal("Success!", "You have add level success!", "success");
                }else{
                    swal("Could not add Level!", data.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });


    });

}

function openPopupDelete(){
    $(document).on("click","#delete", function(){
        $("#deletes").modal('show');
        var idd=$(this).attr('id-column');
        $("#iddelete").val(idd);
    });
}

function deleteLevel(){
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
                if (data.indexOf("success") !=-1) {
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#deletes").modal('hide');
                    swal("Success!", "You have delete level success!", "success");
                }else{
                    if(data.indexOf("deleted") !=-1){
                        $("#deletes").modal('hide');
                        swal({title: "Warning!", text: "This level has been already deleted!",   type: "warning",timer:"5000" });
                        location.reload();
                    }else{
                        $("#deletes").modal('hide');
                        swal("Could not delete level!", data.split(":")[1], "error");
                    }
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    });
}

function openPopupEdit(){
    $(document).on("click","#edit", function() {
        $("#edits").modal('show');
        var idd = $(this).attr('id-column');
        var level = $(this).attr('level');
        var description = $(this).attr('description');
        var color = $(this).attr('color');
        var isDemo = $(this).attr('isDemo');
        $("#editLevel").attr("disabled", true);
        var isDefaultActivated=$(this).attr('isDefaultActivated');
        $("#editLevel").val(level);
        $("#editDescription").val(description);
        $("#editColor").val(color);
        $("#idedit").val(idd);
        if(isDemo=='true'){
            $("#isDemoEdit").prop('checked', true);
            isDemos=true;
        }else{
            $("#isDemoEdit").prop('checked', false);
            isDemos=false;
        }
        if(isDefaultActivated=='true'){
            $("#isDefaultActivatededit").prop('checked', true);
            isDemos=true;
        }else{
            $("#isDefaultActivatededit").prop('checked', false);
            isDemos=false;
        }
        lessonName = level;
    });

}

function editLevel(){
    $(document).on("click","#yesedit", function(){

        var isUpdateLessonName=true;
        var id = $("#idedit").val();
        var level = $("#editLevel").val();
        var description = $("#editDescription").val();
        var color = $("#editColor").val();
        var isDemo = isDemoEdit();
        var isDefaultActivated=isDefaultActivatedEDIT();
        if (level == null || typeof level == "undefined" || level.length == 0){
            $("#editLevel").focus();
            swal("Warning!", "Please enter level name!", "warning");
            return;
        }
        if(level == lessonName){
            isUpdateLessonName = false;
        }
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "text",
            data: {
                edit: "edit",
                id: id,
                color:color,
                isDemo:isDemo,
                isDefaultActivated:isDefaultActivated,
                level: level,
                description:description,
                isUpdateLessonName: isUpdateLessonName
            },
            success: function (data) {
                if (data.indexOf("success") !=-1) {
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#edits").modal('hide');
                    isDemos=false;
                    swal("Success!", "You have update level success!", "success");
                }else{
                    if(data.indexOf("deleted") !=-1){
                        $("#edits").modal('hide');
                        swal({title: "Warning!", text: "This level has been already deleted!",   type: "warning",timer:"5000" });
                        location.reload();
                    }else{
                        $("#edits").modal('hide');
                        swal("Could not update level!", data.split(":")[1], "error");
                    }
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
            "url": servletName,
            "type": "POST",
            "dataType": "json",
            "data": {
                list: "list",
                description: $("#description").val(),
                CreateDateFrom: $("#CreateDateFrom").val(),
                CreateDateTo: $("#CreateDateTo").val()
            }
        };
        $("tbody").html("");
        myTable.fnDraw();


    });
}
function addcolor(){
    $("#addColor").colorpicker();
}
function editcolor(){
    $("#editColor").colorpicker();
}
function isDemoAdd(){
    if($("#idDemoAdd").is(":checked")){
        return true;
    }
    return false;

}
function isDemoEdit(){
    if($("#isDemoEdit").is(":checked")){
        return true;
    }
    return false;
}
function isDefaultActivatedADD(){
    if($("#isDefaultActivatedadd").is(":checked")){
        return true;
    }
    return false;

}
function isDefaultActivatedEDIT(){
    if($("#isDefaultActivatededit").is(":checked")){
        return true;
    }
    return false;
}





$(document).ready(function(){
    //$("#ui_normal_dropdown").dropdown({
    //        maxSelections: 3
    //    });
    isDefaultActivatedADD();
    isDefaultActivatedEDIT();
    isDemoAdd();
    isDemoEdit();
    dateFrom();
    dateTo();
    openPopupAdd();
    addLevel();
    openPopupEdit();
    editLevel();
    openPopupDelete();
    deleteLevel();
    listLevels();
    editcolor();
    addcolor();
    searchAdvanted();
});


