/**
 * Created by CMGT400 on 10/8/2015.
 */
var myTable;
var servletName="ManagementCourseServlet";
function listCourse(){

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
                CreateDateFrom: $("#CreateDateFrom").val(),
                CreateDateTo: $("#CreateDateTo").val()
            }
        },

        "columns": [{
            "sWidth": "25%",
            "data": "name",
            "sDefaultContent": ""
        }, {
            "sWidth": "25%",
            "data": "description",
            "bSortable": false,
            "sDefaultContent": ""
        }, {
            "sWidth": "20%",
            "data": "dateCreated",
            "sDefaultContent": ""
        }, {
            "sWidth": "30%",
            "data": null,
            "bSortable": false,
            "sDefaultContent": "",
            "mRender": function (data, type, full) {
                $button = $('<button type="button" style="margin-right:10px" id="edit" class="btn btn-info btn-sm" ' + full[0] + '>' + 'Edit' + '</button>' + '<button style="margin-right:10px" type="button" id="delete" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Delete' + '</button>' + '<a href="ManagementLevelOfCourse.jsp?id='+ data.id +'" type="button" id="addlevel" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Add Level ' + '</a>');
                $button.attr("id-column", data.id);
                $button.attr("course", data.name);
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
    $(document).on("click","#openAddCourse", function(){
        $("#add").modal('show');
        $("#addCourse").val("");
        $("#addDescription").val("");
    });
}

function addCourse(){
    $(document).on("click","#yesadd", function(){
        var course = $("#addCourse").val();
        var description = $("#addDescription").val();
        if (course == null || typeof course == "undefined" || course.length == 0){
            $("#addCourse").focus();
            swal("Warning!", "Level not null!", "warning");
            return;
        }
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "text",
            data: {
                action: "add",
                course: course,
                description:description
            },
            success: function (data) {
                if (data.indexOf("success") !=-1) {
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#add").modal('hide');
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

function deleteCourse(){
    $(document).on("click","#deleteItems", function(){
        var id=  $("#iddelete").val();
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "text",
            data: {
                action: "delete",
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
        var course = $(this).attr('course');
        var description = $(this).attr('description');
        $("#editCourse").val(course);
        $("#editDescription").val(description);
        $("#idedit").val(idd);
    });

}

function editCourse(){
    $(document).on("click","#yesedit", function(){
        var id = $("#idedit").val();
        var course = $("#editCourse").val();
        var description = $("#editDescription").val();
        if (course == null || typeof course == "undefined" || course.length == 0){
            $("#editCourse").focus();
            swal("Warning!", "Course not null!", "warning");
            return;
        }
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "text",
            data: {
                action: "edit",
                id: id,
                course: course,
                description:description
            },
            success: function (data) {
                if (data.indexOf("success") !=-1) {
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#edits").modal('hide');
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
                action: "list",
                CreateDateFrom: $("#CreateDateFrom").val(),
                CreateDateTo: $("#CreateDateTo").val()
            }
        };
        $("tbody").html("");
        myTable.fnDraw();


    });
}



$(document).ready(function(){
    //$("#ui_normal_dropdown").dropdown({
    //        maxSelections: 3
    //    });

    dateFrom();
    dateTo();
    openPopupAdd();
    addCourse();
    openPopupEdit();
    editCourse();
    openPopupDelete();
    deleteCourse();
    listCourse();
    searchAdvanted();
});


