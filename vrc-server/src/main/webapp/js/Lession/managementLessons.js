/**
 * Created by CMGT400 on 10/8/2015.
 */
var myTable;
var servletName="ManagementLessonsServlet";
var lessonName;
var idLesson;

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
                lesson: $("#lesson").val(),
                CreateDateFrom: $("#CreateDateFrom").val(),
                CreateDateTo: $("#CreateDateTo").val()
            }
        },

        "columns": [
            {
                "sWidth": "15%",
                "data": "nameUnique",
                "sDefaultContent": ""
            },{
                "sWidth": "15%",
                "data": "title",
                "sDefaultContent": ""
            },{
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
                    $button = $('<button type="button" style="margin-right:5px" id="edit" class="btn btn-info btn-sm" ' + full[0] + '>' + 'Edit' + '</button>' + '<button style="margin-right:5px" type="button" id="delete" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Delete' + '</button>' + '<a href="question-of-lesson-management.jsp?id='+ data.id +'" type="button" id="addQuestion" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Add Question ' + '</a>');
                    $button.attr("id-column", data.id);
                    $button.attr("lesson", data.nameUnique);
                    $button.attr("title", data.title);
                    $button.attr("description", data.description);
                    $button.attr("shortDescription", data.name);
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
        $("#addTitle").val("");
        $("#addDescription").val("");
        $("#shortDescription").val("");
    });
}

function addLesson(){
    $(document).on("click","#yesadd", function(){
        var lesson = $("#addLesson").val();
        var title = $("#addTitle").val();
        var shortDescription = $("#shortDescription").val();
        var description = $("#addDescription").val();
        if (lesson == null || typeof lesson == "undefined" || lesson.length == 0){
            $("#addLesson").focus();
            swal("Warning!", "Lesson not null!", "warning");
            return;
        }
        if (title == null || typeof title == "undefined" || title.length == 0){
            $("#addTitle").focus();
            swal("Warning!", "Title not null!", "warning");
            return;
        }
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "text",
            data: {
                add: "add",
                lesson: lesson,
                title: title,
                shortDescription:shortDescription,
                description:description
            },
            success: function (data) {
                if (data.indexOf("success") !=-1) {
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#add").modal('hide');
                    swal("Success!", "You have add lesson success!", "success");
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

function openPopupDelete(){
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
                    swal("Success!", "You have delete lesson success!", "success");
                }else{
                    if(data.indexOf("deleted") !=-1){
                        $("#deletes").modal('hide');
                        swal({title: "Warning!", text: "This lesson has been already deleted!",   type: "warning",timer:"5000" });
                        location.reload();
                    }else{
                        $("#deletes").modal('hide');
                        swal("Could not delete question!", data.split(":")[1], "error");
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
        var lesson = $(this).attr('lesson');
        var title = $(this).attr('title');
        var shortDescription = $(this).attr('shortDescription');
        var description = $(this).attr('description');
        $("#editLesson").val(lesson);
        $("#editLesson").attr("disabled", true);
        $("#editShortDescription").val(shortDescription);
        $("#editDescription").val(description);
        $("#editTitle").val(title);
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
        var shortDescription = $("#editShortDescription").val();
        var title = $("#editTitle").val();
        if (lesson == null || typeof lesson == "undefined" || lesson.length == 0){
            $("#editLesson").focus();
            swal("Warning!", "Lesson not null!", "warning");
            return;
        }
        if (title == null || typeof title == "undefined" || title.length == 0){
            $("#editTitle").focus();
            swal("Warning!", "Title not null!", "warning");
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
                title: title,
                shortDescription:shortDescription,
                description:description,
                isUpdateLessonName: isUpdateLessonName
            },
            success: function (data) {
                if (data.indexOf("success") !=-1) {
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#edits").modal('hide');
                    swal("Success!", "You have update lesson success!", "success");
                }else{
                    if(data.indexOf("deleted") !=-1){
                        $("#edits").modal('hide');
                        swal({title: "Warning!", text: "This lesson has been already deleted!",   type: "warning",timer:"5000" });
                        location.reload();
                    }else{
                        $("#edits").modal('hide');
                        swal("Could not update lesson!", data.split(":")[1], "error");
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
                lesson: $("#lesson").val(),
                CreateDateFrom: $("#CreateDateFrom").val(),
                CreateDateTo: $("#CreateDateTo").val()
            }
        };
        $("tbody").html("");
        myTable.fnDraw();


    });
}

function addQuestionForLesson(){
    $(document).on("click","#addQuestion", function(){
        idLesson=$(this).attr('id-column');
        $("#addQuestionToLesson").modal('show');

        //alert(idd);

    });

}

function addJsForDropdown(listSelected){
    var jsContent= "<script>$('.ui.dropdown').dropdown('set selected',["+listSelected+"]);</script>";
    $("#js-dropdow").html(jsContent);
}

function initModal(){
    $('#addQuestionToLesson').on('shown.bs.modal', function (e) {
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "json",
            data: {
                listQuestionOfLesson: servletName,
                idLesson: idLesson
            },
            success: function (data) {
                var message = data.message;
                if(message.indexOf("success") != -1){
                    var listSelected = [];
                    $("#select_question").empty();
                    $.each(data.data, function (idx, obj) {
                        $("#select_question").append("<option value='"+obj.id+"'>"+obj.name+"</option>");
                        listSelected.push(obj.name);
                    });

                    listSelected = listSelected.substring(0,listSelected.length-1);
                    addJsForDropdown(listSelected);

                }else{
                    swal("Error!",message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });


        //alert(idd);

    });

}

function addJsForDropdown(listSelected){
    var jsContent= "<script>$('.ui.dropdown').dropdown('set selected',["+listSelected+"]);</script>";
    $("#js-dropdow").html(jsContent);
}

function initModal(){
    $('#addQuestionToLesson').on('show.bs.modal', function (e) {
        $('#select_question').dropdown();
    })
}

$(document).ready(function(){
    //$("#ui_normal_dropdown").dropdown({
    //        maxSelections: 3
    //    });
    dateFrom();
    dateTo();
    //initModal();
    openPopupAdd();
    addLesson();
    openPopupEdit();
    editLesson();
    openPopupDelete();
    deleteLesson();
    //addQuestionForLesson();
    listLessons();
    searchAdvanted();
});


