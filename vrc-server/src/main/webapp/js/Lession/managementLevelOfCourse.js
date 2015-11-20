/**
 * Created by CMGT400 on 10/5/2015.
 */
var attFunction = false;
var myTable;
var ManagementLevelOfCourseServlet="ManagementLevelOfCourseServlet";
var ObjectiveMappingServlet = "ObjectiveMappingServlet";
var maxlengthWeight=2;

function isNumberKey(evt,e){
    var charCode = (evt.which) ? evt.which : event.keyCode;
    if (charCode != 46 && charCode > 31
        && (charCode < 48 || charCode > 57)){
        return false;
    }
    var inputValue = $(e).val();
    //var maxlength = parseFloatCMG($(e).attr("length"));
    if(inputValue.length >= maxlengthWeight){
        return false;
    }
    return true;
}

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
                try{
                    if(typeof listObj !== undefined && listObj.length >= 0){
                        $("#"+idLevel).find("#collection_objective").empty();
                        $(listObj).each(function(){
                            buildPanelObject(this);
                        });
                    }
                }catch(e){
                    $("#"+idLevel).find("#collection_objective").empty();
                }



                try{
                    var listTest = data.listTestMap;
                    if(typeof listTest !== undefined && listTest.length >= 0){
                        $("#"+idLevel).find("#collection_test").empty();
                        $(listTest).each(function(){
                            buildPanelTest(this);
                        });
                        //$("button.createTest[id_lv='"+idLevel+"']").hide();
                    }else{
                       // $("button.createTest[id_lv='"+idLevel+"']").show();
                    }
                }catch(e){
                    $("#"+idLevel).find("#collection_test").empty();
                   // $("button.createTest[id_lv='"+idLevel+"']").show();
                }

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
                var alertContent;
                $.each(data.data, function (idx, obj) {
                    alertContent = '<div class="alert alert-info">'
                    alertContent += '<a id="delete" action="lesson-obj" class="close '+idObject+obj.id+'" leson-id="'+obj.id+'" obj-child-id="'+idObject+'" title="close" aria-label="close" href="#">×</a>'; /*data-dismiss="alert" */
                    alertContent += '<a title="'+obj.name+'" href="ManagementQuestionOfLesson.jsp?id='+obj.id+'">'+obj.nameUnique+'</a>';
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

function getAllLesson(ObjType){
    $.ajax({
        url: ManagementLevelOfCourseServlet,
        type: "POST",
        dataType: "json",
        data: {
            action: "getAllLesson"
        },
        success: function (data) {
            if(data.message.indexOf("success")!=-1){
                if(ObjType.indexOf("Test") != -1){
                    $("#select-test-lesson").empty();
                    try{
                        if(typeof data.data !== undefined ) {
                            $("#select-test-lesson").append("<option value='null'>None selected</option>");
                            $.each(data.data, function (idx, obj) {
                                $("#select-test-lesson").append("<option value='" + obj.id + "'>" + obj.nameUnique + "</option>");
                            });
                        }
                    }catch(e){}
                    $(".loading-lesson").hide();
                    $('#select-test-lesson').multiselect({ enableFiltering: true, buttonWidth: '200px'});
                    $("#container-test-lesson").find(".btn-group").css("padding-left","14px");
                    $('#select-test-lesson').multiselect('refresh');
                    $("#yesadd-test").removeAttr("disabled");
                }else {
                    $("#select-lesson").empty();
                    try{
                        if(typeof data.data !== undefined ) {
                            $.each(data.data, function (idx, obj) {
                                $("#select-lesson").append("<option value='" + obj.id + "'>" + obj.nameUnique + "</option>");
                            });
                        }
                    }catch(e){}
                    $(".loading-lesson").hide();
                    $('#select-lesson').multiselect({ enableFiltering: true, buttonWidth: '200px'});
                    $("#container-add-lesson").find(".btn-group").css("padding-left","14px");
                    $('#select-lesson').multiselect('refresh');
                    $("#yesadd").removeAttr("disabled");
                }
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
        var objChildId=$(this).attr('obj-child-id');
        $("#id-lesson-delete").val(lessonId);
        $("#id-obj-child-delete").val(objChildId);
        $("#deleteItems").attr("action",$(this).attr('action'));
    });
}

function deleteLesson(){
    $(document).on("click","#deleteItems", function(){
        var lessonId =  $("#id-lesson-delete").val();
        var objChildId =  $("#id-obj-child-delete").val();
        if ($("#deleteItems").attr("action").indexOf("lesson-test") !=-1){
            $.ajax({
                url: ObjectiveMappingServlet,
                type: "POST",
                dataType: "json",
                data: {
                    action: "deleteLessonForTest",
                    lessonId: lessonId,
                    testId: objChildId
                },
                success: function (data) {
                    if (data.message.indexOf("success") !=-1) {
                        $("tbody").html("");
                        $("#deletes").modal('hide');
                        $("."+objChildId+lessonId).parent().hide();
                    }else{
                        swal("Could not delete lesson!", data.message.split(":")[1], "error");
                    }
                },
                error: function () {
                    swal("Error!", "Could not connect to server", "error");
                }

            });
        }else{
            $.ajax({
                url: ObjectiveMappingServlet,
                type: "POST",
                dataType: "json",
                data: {
                    action: "deleteLesson",
                    lessonId: lessonId,
                    objectiveId: objChildId
                },
                success: function (data) {
                    if (data.message.indexOf("success") !=-1) {
                        $("tbody").html("");
                        $("#deletes").modal('hide');
                        $("."+objChildId+lessonId).parent().hide();
                    }else{
                        swal("Could not delete lesson!", data.message.split(":")[1], "error");
                    }
                },
                error: function () {
                    swal("Error!", "Could not connect to server", "error");
                }

            });
        }
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
                    try{
                        $.each(data.data, function (idx, obj) {
                            if (obj.idChecked){
                                $("#select-lesson-edit").append("<option selected='selected' value='"+obj.id+"'>"+obj.nameUnique+"</option>");
                            }else{
                                $("#select-lesson-edit").append("<option value='"+obj.id+"'>"+obj.nameUnique+"</option>");
                            }
                        });
                    }catch(e){}
                    $('#select-lesson-edit').multiselect({ enableFiltering: true, buttonWidth: '200px'});
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
        var nameObj = $("#edit-objective-name").val();
        if (nameObj == null || typeof nameObj == "undefined" || nameObj.length == 0){
            $("#edit-objective-name").focus();
            swal("Warning!", "Please enter an objective name!", "warning");
            return;
        }
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
        getAllLesson("Objective");
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
        var nameObj = $("#add-objective-name").val();
        if (nameObj == null || typeof nameObj == "undefined" || nameObj.length == 0){
            $("#add-objective-name").focus();
            swal("Warning!", "Please enter an objective name!", "warning");
            return;
        }
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

/*function for Test
*******************************************************************************************************************/
function clickTest(e){
    var idTest   =  $(e).attr("id_test");
    var idLevel =  $(e).attr("id_lv");
    getLessonForTest(idTest);
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
                $("#"+idTest+" #collection_lesson_test").html("");
                var alertContent;
                $.each(data.data, function (idx, obj) {
                    alertContent = '<div class="alert alert-info">'
                    alertContent += '<a id="delete" action="lesson-test" class="close '+idTest+obj.id+'" leson-id="'+obj.id+'" obj-child-id="'+idTest+'" title="close" aria-label="close" href="#">×</a>'; /*data-dismiss="alert" */
                    alertContent += '<a title="'+obj.description+'" href="ManagementQuestionOfLesson.jsp?id='+obj.id+'">'+obj.nameUnique+'</a>';
                    alertContent += '</div>';
                    $("#"+idTest+" #collection_lesson_test").append(alertContent);
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

function openPopupAddTest(){
    $(document).on("click",".createTest", function(){
        $(".loading-lesson").show();
        $("#add-test").modal('show');
        getAllLesson("Test");
        $("#add-test-name").val("");
        $("#add-test-description").val("");
        $("#add-percen-pass").val("");
        var idLevel = $(this).attr("id_lv");
        $("#yesadd-test").attr("id_level",idLevel);
        //alert(idLevel);
    });
}

function addTest(){
    $(document).on("click","#yesadd-test", function(){
        var nameTest = $("#add-test-name").val();
        var percentPass =  $("#add-percen-pass").val();
        if (nameTest == null || typeof nameTest == "undefined" || nameTest.length == 0){
            $("#add-test-name").focus();
            swal("Warning!", "Please enter a test name!", "warning");
            return;
        }
        if (percentPass == null || typeof percentPass == "undefined" || percentPass.length == 0){
            $("#add-percen-pass").focus();
            swal("Warning!", "Please enter a percent pass!", "warning");
            return;
        }
        var dto = getDtoAddTest();
        $.ajax({
            url: ObjectiveMappingServlet,
            type: "POST",
            dataType: "json",
            data: {
                action: "addTest",
                testDto: JSON.stringify(dto)// to json word,
            },
            success: function (data) {
                if (data.message.indexOf("success") !=-1) {
                    $("#add-test").modal('hide');
                    buildPanelTest(data);
                }else{
                    swal("Could not add test!", data.message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    });
}

function openPopupEditTest(){
    $(document).on("click",".editTest", function() {
        $("#edit-test").modal('show');
        $('#select-edit-test-lesson').multiselect('destroy');
        $("#select-edit-test-lesson").empty();
        $("#select-edit-test-lesson").hide();
        $(".loading-lesson").show();
        var idTest = $(this).attr('id_test');
        var idLevel = $(this).attr('id_lv');
        $("#yesedit-test").attr("test_id",idTest);
        $("#yesedit-test").attr("id_level",idLevel);
        $.ajax({
            url: ObjectiveMappingServlet,
            type: "POST",
            dataType: "json",
            data: {
                action: "loadUpdateTest",
                idTest: idTest
            },
            success: function (data) {
                if (data.message.indexOf("success") !=-1) {
                    $("#edit-test-name").val(data.nameTest);
                    $("#edit-test-description").val(data.descriptionTest);
                    $("#edit-percen-pass").val(data.percentPass);
                    $("#select-edit-test-lesson").append("<option value='null'>None selected</option>");
                    $.each(data.data, function (idx, obj) {
                        if (obj.idChecked){
                            $("#select-edit-test-lesson").append("<option selected='selected' value='"+obj.id+"'>"+obj.nameUnique+"</option>");
                        }else{
                            $("#select-edit-test-lesson").append("<option value='"+obj.id+"'>"+obj.nameUnique+"</option>");
                        }
                    });
                    $("#select-edit-test-lesson").show();
                    $('#select-edit-test-lesson').multiselect({ enableFiltering: true, buttonWidth: '200px'});
                    $('#select-edit-test-lesson').multiselect('refresh');
                    $("#container-edit-test-lesson").find(".btn-group").css("padding-left","14px");
                    $(".loading-lesson").hide();
                }else{
                    swal("Could not load edit test data!", data.message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    });

}

function editTest(){
    $(document).on("click","#yesedit-test", function(){
        var nameTest = $("#edit-test-name").val();
        if (nameTest == null || typeof nameTest == "undefined" || nameTest.length == 0){
            $("#edit-test-name").focus();
            swal("Warning!", "Please enter a test name!", "warning");
            return;
        }
        var dto = getDtoEditTest();
        $.ajax({
            url: ObjectiveMappingServlet,
            type: "POST",
            dataType: "json",
            data: {
                action: "updateTest",
                objDto: JSON.stringify(dto)// to json word,
            },
            success: function (data) {
                if (data.message.indexOf("success") !=-1) {
                    swal("update success!", data.message.split(":")[1], "info");
                    $("#edit-test").modal('hide');
                    //reload data
                    getObjAndTest($("#yesedit-test").attr("id_level"),$("#idCourse").val());
                    //getLessonsForObj(data.idObjective);
                }else{
                    swal("Could not edit test!", data.message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    });
}

function openPopupDeleteTest(){
    $(document).on("click",".removeTest", function(){
        var testId=$(this).attr('id_test');
        var levelId=$(this).attr('id_lv');
        $("#delete-test-id").val(testId);
        $("#deleteItems-test").attr("id_lv",levelId);
        $("#delete-test").modal('show');
    });
}

function deleteTest(){
    $(document).on("click","#deleteItems-test", function(){
        var testId =  $("#delete-test-id").val();
        $.ajax({
            url: ObjectiveMappingServlet,
            type: "POST",
            dataType: "json",
            data: {
                action: "deleteTest",
                testId: testId
            },
            success: function (data) {
                if (data.message.indexOf("success") !=-1) {
                    swal("delete success!", data.message.split(":")[1], "info");
                    $("#delete-test").modal('hide');
                    //reload data
                    getObjAndTest($("#deleteItems-test").attr("id_lv"),$("#idCourse").val());
                }else{
                    swal("Could not delete test!", data.message.split(":")[1], "error");
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

    openPopupAddTest();
    addTest();
    openPopupEditTest();
    editTest();
    openPopupDeleteTest();
    deleteTest();
});


