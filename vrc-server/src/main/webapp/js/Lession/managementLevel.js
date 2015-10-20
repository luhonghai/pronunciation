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
                CreateDateFrom: $("#CreateDateFrom").val(),
                CreateDateTo: $("#CreateDateTo").val()
            }
        },

        "columns": [{
            "sWidth": "20%",
            "data": "name",
            "sDefaultContent": ""
        }, {
            "sWidth": "25%",
            "data": "description",
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
            "sWidth": "30%",
            "data": null,
            "bSortable": false,
            "sDefaultContent": "",
            "mRender": function (data, type, full) {
                $button = $('<button type="button" style="margin-right:10px" id="edit" class="btn btn-info btn-sm" ' + full[0] + '>' + 'Edit' + '</button>' + '<button style="margin-right:10px" type="button" id="delete" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Delete' + '</button>' + '<button type="button" id="addQuestion" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Add Question' + '</button>');
                $button.attr("id-column", data.id);
                $button.attr("level", data.name);
                $button.attr("description", data.description);
                $button.attr("color", data.color);
                $button.attr("isDemo", data.isDemo);
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

    });
}

function addLevel(){
    $(document).on("click","#yesadd", function(){
        var level = $("#addLevel").val();
        var description = $("#addDescription").val();
        var color = $("#addColor").val();
        var isDemo = isDemos;
        if (level == null || typeof level == "undefined" || level.length == 0){
            $("#addLevel").focus();
            swal("Warning!", "Level not null!", "warning");
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
                isDemo:isDemo
            },
            success: function (data) {
                if (data.indexOf("success") !=-1) {
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#add").modal('hide');
                    isDemos=false;
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

function openPopupEdit(){
    $(document).on("click","#edit", function() {
        $("#edits").modal('show');
        var idd = $(this).attr('id-column');
        var level = $(this).attr('level');
        var description = $(this).attr('description');
        var color = $(this).attr('color');
        var isDemo = $(this).attr('isDemo');;
        $("#editLevel").val(level);
        $("#editDescription").val(description);
        $("#editColor").val(color);
        $("#idedit").val(idd);
        if(isDemo=='true'){
            $("#isDemoEdit").prop('checked', true);
        }else{
            $("#isDemoEdit").prop('checked', false);
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
        var isDemo = isDemos;
        if (level == null || typeof level == "undefined" || level.length == 0){
            $("#editLevel").focus();
            swal("Warning!", "Lesson not null!", "warning");
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
                }else{
                    swal("Could not update lesson!", data.split(":")[1], "error");
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
    $(document).on("change","#idDemoAdd", function(){
        var $this = $(this);
        if ($this.is(':checked')) {
           isDemos=true;
        }
    });
}
function isDemoEdit(){
    $(document).on("change","#isDemoEdit", function(){
        var $this = $(this);
        if ($this.is(':checked')) {
            isDemos=true;
        }
    });
}




$(document).ready(function(){
    //$("#ui_normal_dropdown").dropdown({
    //        maxSelections: 3
    //    });
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


