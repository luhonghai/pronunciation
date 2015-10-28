/**
 * Created by CMGT400 on 10/5/2015.
 */
var attFunction = false;
var myTable;
var ManagementLevelOfCourseServlet="ManagementLevelOfCourseServlet";
var ObjectiveMappingServlet = "ObjectiveMappingServlet";

function BuildUI(){
    var $selected=$("#level");
    var $listLevel=$("#accordion");
    var id=$("#idCourse").val();
    $('#level option[value!="-1"]').remove();
    $.ajax({
        "url": ManagementLevelOfCourseServlet,
        "type": "POST",
        "dataType":"json",
        "data": {
            action: "listLevel",
            id:id
        },
        success:function(data){
            var items= data.dataforDropdown;
            var listLevel= data.data;
            $("#accordion").empty();
            if(items.length > 0 ){
                $selected.empty();
                $(items).each(function(){
                    buildDropdown(this);
                });
                $("#contain_level_add").show();
            }else{
                $("#contain_level_add").hide();
            }

            $(listLevel).each(function(){
                buildPanelLevel(this);
            });
        }

    });
}

function addLevel(){
    $(document).on("click","#addlevel", function(){
        var idLevel=$("#level option:selected").attr('id');
        var idCourse=$("#idCourse").val();

        $.ajax({
            url: ManagementLevelOfCourseServlet,
            type: "POST",
            dataType: "json",
            data: {
                action: "addLevel",
                idLevel:idLevel,
                idCourse:idCourse
            },
            success: function (data) {
                if(data.message.indexOf("success")!=-1){
                    BuildUI();
                }else{
                    swal("Error!", data.message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });


    });

}

function removeLevel(){
    $(document).on("click",".removelv", function(){
        var idLevel=$(this).attr('id_lv');
        var idCourse=$("#idCourse").val();
        swal({
                title: "Remove Level, Are you sure?",
                text: "",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "Yes, remove it!",
                closeOnConfirm: true
            },
            function(){
                $.ajax({
                    url: ManagementLevelOfCourseServlet,
                    type: "POST",
                    dataType: "json",
                    data: {
                        action: "delete",
                        idLevel:idLevel,
                        idCourse:idCourse
                    },
                    success: function (data) {
                        if(data.message.indexOf("success")!=-1){
                            BuildUI();
                        }else{
                            swal("Error!", data.message.split(":")[1], "error");
                        }
                    },
                    error: function () {
                        swal("Error!", "Could not connect to server", "error");
                    }

                });
            }
        );
    });

}

function clickLevel(e){
    var idLevel = $(e).attr("data-target").split("#")[1];
    var idCourse=$("#idCourse").val();
    getObjAndTest(idLevel,idCourse);
}

function clickObj(e){
    var idObj   =  $(e).attr("id_obj");
    var idLevel =  $(e).attr("id_lv");
    getLessonsForObj(idObj);
}

function getObjAndTest(idLevel,idCourse){
    $.ajax({
        url: ManagementLevelOfCourseServlet,
        type: "POST",
        dataType: "json",
        data: {
            action: "getObjAndTest",
            idLevel:idLevel,
            idCourse:idCourse
        },
        success: function (data) {
            if(data.message.indexOf("success") != -1){
                var listObj = data.listObjMap;
                if(listObj.length >= 0){
                    $("#"+idLevel).find("#collection_objective").empty();
                    $(listObj).each(function(){
                        buildPanelObject(this);
                    });

                }
                /*var test = data.test;
                if(test!=""){
                    $("#"+idLevel).find("#collection_test").empty();
                }*/
            }else{
                swal("Error!", data.message.split(":")[1], "error");
            }
        },
        error: function () {
            swal("Error!", "Could not connect to server", "error");
        }

    });
}

function getLessonsForObj(idObject){
    $.ajax({
        url: ObjectiveMappingServlet,
        type: "POST",
        dataType: "json",
        data: {
            action: "getLessonsForObj",
            idObj:idObject
        },
        success: function (data) {
            if(data.message.indexOf("success")!=-1){
                $("#"+idObject+" #collection_lesson_obj").html("");
                var alertContent
                $.each(data.data, function (idx, obj) {
                    alertContent = '<div class="alert alert-info">'
                    alertContent += '<a id="delete" class="close '+idObject+obj.id+'" leson-id="'+obj.id+'" objective-id="'+idObject+'" title="close" aria-label="close" href="#">×</a>'; /*data-dismiss="alert" */
                    alertContent += '<a title="'+obj.description+'" href="ManagementQuestionOfLesson.jsp?id='+obj.id+'">'+obj.name+'</a>';
                    alertContent += '</div>';
                    $("#"+idObject+" #collection_lesson_obj").append(alertContent);
                });
            }else{
                swal("Error!", data.message.split(":")[1], "error");
            }
        },
        error: function () {
            swal("Error!", "Could not connect to server", "error");
        }

    });
}

function getLessonForTest(idTest){
    $.ajax({
        url: ObjectiveMappingServlet,
        type: "POST",
        dataType: "json",
        data: {
            action: "getLessonForTest",
            idTest:idTest
        },
        success: function (data) {
            if(data.message.indexOf("success")!=-1){

            }else{
                swal("Error!", data.message.split(":")[1], "error");
            }
        },
        error: function () {
            swal("Error!", "Could not connect to server", "error");
        }

    });
}

function getAllLesson(){
    $.ajax({
        url: ManagementLevelOfCourseServlet,
        type: "POST",
        dataType: "json",
        data: {
            action: "getAllLesson"
        },
        success: function (data) {
            if(data.message.indexOf("success")!=-1){
                $("#select-lesson").empty();
                $.each(data.data, function (idx, obj) {
                    $("#select-lesson").append("<option value='"+obj.id+"'>"+obj.name+"</option>");
                });
                $(".loading-lesson").hide();
                $('#select-lesson').multiselect({ enableFiltering: true});
                $("#container-add-lesson").find(".btn-group").css("padding-left","14px");
                $('#select-lesson').multiselect('refresh');
                $("#yesadd").removeAttr("disabled");

            }else{
                swal("Error!", data.message.split(":")[1], "error");
            }
        },
        error: function () {
            swal("Error!", "Could not connect to server", "error");
        }

    });
}

function openPopupDeleteLesson(){
    $(document).on("click","#delete", function(){
        $("#deletes").modal('show');
        var lessonId=$(this).attr('leson-id');
        var objectiveId=$(this).attr('objective-id');
        $("#id-lesson-delete").val(lessonId);
        $("#id-objective-delete").val(objectiveId);
    });
}

function deleteLesson(){
    $(document).on("click","#deleteItems", function(){
        var lessonId =  $("#id-lesson-delete").val();
        var objectiveId =  $("#id-objective-delete").val();
        $.ajax({
            url: ObjectiveMappingServlet,
            type: "POST",
            dataType: "json",
            data: {
                action: "deleteLesson",
                lessonId: lessonId,
                objectiveId: objectiveId
            },
            success: function (data) {
                if (data.message.indexOf("success") !=-1) {
                    $("tbody").html("");
                    $("#deletes").modal('hide');
                    $("."+objectiveId+lessonId).parent().hide();
                }else{
                    swal("Could not delete lesson!", data.message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    });
}

function openPopupDeleteObjective(){
    $(document).on("click",".removeObj", function(){
        var objectiveId=$(this).attr('id_obj');
        var levelId=$(this).attr('id_lv');
        $("#delete-objective-id").val(objectiveId);
        $("#deleteItems-obj").attr("id_lv",levelId);
        $("#delete-objective").modal('show');
    });
}

function deleteObjective(){
    $(document).on("click","#deleteItems-obj", function(){
        var objectiveId =  $("#delete-objective-id").val();
        $.ajax({
            url: ObjectiveMappingServlet,
            type: "POST",
            dataType: "json",
            data: {
                action: "deleteObjective",
                objectiveId: objectiveId
            },
            success: function (data) {
                if (data.message.indexOf("success") !=-1) {
                    swal("delete success!", data.message.split(":")[1], "info");
                    $("#delete-objective").modal('hide');
                    //reload data
                    getObjAndTest($("#deleteItems-obj").attr("id_lv"),$("#idCourse").val());
                }else{
                    swal("Could not delete lesson!", data.message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    });
}

function openPopupEditObjtive(){
    $(document).on("click",".editObj", function() {
        $("#edit-objective").modal('show');
        $('#select-lesson-edit').multiselect('destroy');
        $(".loading-lesson").show();
        var idObjective = $(this).attr('id_obj');
        var idLevel = $(this).attr('id_lv');
        $("#yesedit").attr("objtive_id",idObjective);
        $("#yesedit").attr("level_id",idLevel);
        $.ajax({
            url: ObjectiveMappingServlet,
            type: "POST",
            dataType: "json",
            data: {
                action: "loadUpdateObj",
                idObjective: idObjective
            },
            success: function (data) {
                if (data.message.indexOf("success") !=-1) {
                    $("#edit-objective-name").val(data.nameObj);
                    $("#edit-description").val(data.descriptionObj);

                    $("#select-lesson-edit").empty();
                    $.each(data.data, function (idx, obj) {
                        if (obj.idChecked){
                            $("#select-lesson-edit").append("<option selected='selected' value='"+obj.id+"'>"+obj.name+"</option>");
                        }else{
                            $("#select-lesson-edit").append("<option value='"+obj.id+"'>"+obj.name+"</option>");
                        }
                    });
                    $('#select-lesson-edit').multiselect({ enableFiltering: true});
                    $('#select-lesson-edit').multiselect('refresh');
                    $("#container-edit-lesson").find(".btn-group").css("padding-left","14px");
                    $(".loading-lesson").hide();
                }else{
                    swal("Could not update lesson!", data.message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    });

}

function editObjective(){
    $(document).on("click","#yesedit", function(){
        var dto = getDtoEditObjective();
        $.ajax({
            url: ObjectiveMappingServlet,
            type: "POST",
            dataType: "json",
            data: {
                action: "updateObj",
                objDto: JSON.stringify(dto)// to json word,
            },
            success: function (data) {
                if (data.message.indexOf("success") !=-1) {
                    swal("update success!", data.message.split(":")[1], "info");
                    $("#edit-objective").modal('hide');
                    //reload data
                    getObjAndTest($("#yesedit").attr("level_id"),$("#idCourse").val());
                    //getLessonsForObj(data.idObjective);
                }else{
                    swal("Could not delete lesson!", data.message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    });
}

function openPopopAddObjective(){
    $(document).on("click",".createObj", function(){
        $(".loading-lesson").show();
        $("#add-objective").modal('show');
        getAllLesson();
        $("#add-objective-name").val("");
        $("#add-description").val("");
        var idLevel = $(this).attr("id_lv");
        $("#yesadd").attr("id_level",idLevel);
        $("#yesadd").attr("disabled","disabled");
        //alert(idLevel);
    });
}

function addObjectiveToLesson(){
    $(document).on("click","#yesadd", function(){
        var dto = getDtoAddObjective();
        $.ajax({
            url: ObjectiveMappingServlet,
            type: "POST",
            dataType: "json",
            data: {
                action: "addObj",
                objDto: JSON.stringify(dto)// to json word,
            },
            success: function (data) {
                if (data.message.indexOf("success") !=-1) {
                    $("#add-objective").modal('hide');
                    buildPanelObject(data);
                }else{
                    swal("Could not add objective!", data.message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    });
}



$(document).ready(function(){
    removeLevel();
    addLevel();
    BuildUI();
    openPopopAddObjective();
    addObjectiveToLesson();
    openPopupDeleteLesson();
    deleteLesson();
    openPopupEditObjtive();
    editObjective();
    openPopupDeleteObjective();
    deleteObjective();

});


