/**
 * Created by CMGT400 on 10/8/2015.
 */
var myTable;
var servletName="ManagementLessonsServlet";
var lessonName;

function listLessons(){

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
            "sWidth": "25%",
            "data": "name",
            "sDefaultContent": ""
        }, {
            "sWidth": "30%",
            "data": "description",
            "sDefaultContent": ""
        }, {
            "sWidth": "20%",
            "data": "dateCreated",
            "sDefaultContent": ""
        }, {
            "sWidth": "25%",
            "data": null,
            "bSortable": false,
            "sDefaultContent": "",
            "mRender": function (data, type, full) {
                $button = $('<button type="button" style="margin-right:10px" id="edit" class="btn btn-info btn-sm" ' + full[0] + '>' + 'Edit' + '</button>' + '<button style="margin-right:10px" type="button" id="delete" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Delete' + '</button>' + '<a href="ManagementLessonMappingQuestion.jsp?id='+ data.id +'" type="button" id="addword" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Add Question' + '</a>');
                $button.attr("id-column", data.id);
                $button.attr("lesson", data.name);
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
    $(document).on("click","#openAddLesson", function(){
        $("#add").modal('show');
        $("#addLesson").val("");
        $("#addDescription").val("");
    });
}

function addLesson(){
    $(document).on("click","#yesadd", function(){
        var lesson = $("#addLesson").val();
        var description = $("#addDescription").val();
        if (lesson == null || typeof lesson == "undefined" || lesson.length == 0){
            $("#addLesson").focus();
            swal("Warning!", "Lesson not null!", "warning");
            return;
        }
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "text",
            data: {
                add: "add",
                lesson: lesson,
                description:description
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

function openPopupDeletes(){
    $(document).on("click","#delete", function(){
        $("#deletes").modal('show');
        var idd=$(this).attr('id-column');
        $("#iddelete").val(idd);
    });
}

function deleteLesson(){
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
        var lesson = $(this).attr('lesson');
        var description = $(this).attr('description');
        $("#editLesson").val(lesson);
        $("#editDescription").val(description);
        $("#idedit").val(idd);
        lessonName = lesson;
    });

}

function editLesson(){
    $(document).on("click","#yesedit", function(){

        var isUpdateLessonName=true;
        var id = $("#idedit").val();
        var lesson = $("#editLesson").val();
        var description = $("#editDescription").val();
        if (lesson == null || typeof lesson == "undefined" || lesson.length == 0){
            $("#addquestion").focus();
            swal("Warning!", "Lesson not null!", "warning");
            return;
        }
        if(lesson == lessonName){
            isUpdateLessonName = false;
        }
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "text",
            data: {
                edit: "edit",
                id: id,
                lesson: lesson,
                description:description,
                isUpdateLessonName: isUpdateLessonName
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
    openPopupAdd();
    addLesson();
    openPopupEdit();
    editLesson();
    openPopupDeletes();
    deleteLesson();
    listLessons();
    searchAdvanted();
});

