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
    //getObjAndTest(idLevel,idCourse);
    getObjAndTest(idLevel);
}

function clickObj(e){
    var idObj   =  $(e).attr("id_obj");
    var idLevel =  $(e).attr("id_lv");
    getLessonsForObj(idLevel,idObj);
}

//function getObjAndTest(idLevel,idCourse){
function getObjAndTest(idLevel){
    $.ajax({
        url: ManagementLevelOfCourseServlet,
        type: "POST",
        dataType: "json",
        data: {
            action: "getObjAndTest",
            idLevel:idLevel
            //idCourse:idCourse
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

function getLessonsForObj(idLevel,idObject){
    $.ajax({
        url: ObjectiveMappingServlet,
        type: "POST",
        dataType: "json",
        data: {
            action: "getLessonsForObj",
            idObj:idObject,
            idLevel:idLevel
        },
        success: function (data) {
            if(data.message.indexOf("success")!=-1){
                $("#"+idObject+idLevel+" #collection_lesson_obj"+idLevel).html("");
                var alertContent;
                $.each(data.data, function (idx, obj) {
                    alertContent = '<div class="alert alert-info">'
                    alertContent += '<a id="delete" action="lesson-obj" class="close '+idObject+obj.id+'" leson-id="'+obj.id+'" obj-child-id="'+idObject+'" title="close" aria-label="close" href="#">×</a>'; /*data-dismiss="alert" */
                    alertContent += '<a title="'+obj.name+'" href="question-of-lesson-management.jsp?id='+obj.id+'">'+obj.nameUnique+'</a>';
                    alertContent += '</div>';
                    $("#"+idObject+idLevel+" #collection_lesson_obj"+idLevel).append(alertContent);
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
        var levelId = $("#deleteItems-obj").attr("id_lv");
        $.ajax({
            url: ObjectiveMappingServlet,
            type: "POST",
            dataType: "json",
            data: {
                action: "deleteObjective",
                objectiveId: objectiveId,
                levelId: levelId
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
                idObjective: idObjective,
                idLevel:idLevel
            },
            success: function (data) {
                if (data.message.indexOf("success") !=-1) {
                    $("#edit-objective-name").val(data.nameObj);
                    $("#edit-description").val(data.descriptionObj);
                    $("#indexOBJedit").val(data.index);
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

//function editObjective(){
//    $(document).on("click","#yesedit", function(){
//        var nameObj = $("#edit-objective-name").val();
//        if (nameObj == null || typeof nameObj == "undefined" || nameObj.length == 0){
//            $("#edit-objective-name").focus();
//            swal("Warning!", "Please enter an objective name!", "warning");
//            return;
//        }
//        var dto = getDtoEditObjective();
//        $.ajax({
//            url: ObjectiveMappingServlet,
//            type: "POST",
//            dataType: "json",
//            data: {
//                action: "updateObj",
//                objDto: JSON.stringify(dto)// to json word,
//            },
//            success: function (data) {
//                if (data.message.indexOf("success") !=-1) {
//                    swal("update success!", data.message.split(":")[1], "info");
//                    $("#edit-objective").modal('hide');
//                    //reload data
//                    getObjAndTest($("#yesedit").attr("level_id"),$("#idCourse").val());
//                    //getLessonsForObj(data.idObjective);
//                }else{
//                    swal("Could not delete lesson!", data.message.split(":")[1], "error");
//                }
//            },
//            error: function () {
//                swal("Error!", "Could not connect to server", "error");
//            }
//
//        });
//    });
//}
var idLessonsEdit = [];
function editObjective(){
    $(document).on("click","#yesedit", function(){
        var number=0;
        $("#nameLessonEdit").empty();
        $("#indexLEdit").empty();
        var idLevel = $(this).attr("level_id");
        var nameObj = $("#edit-objective-name").val();
        var descriptionObj =  $("#edit-description").val();
        var idObjective= $("#yesedit").attr("objtive_id");
        var index = $("#indexOBJedit").val();
        idLessonsEdit = [];
        var LessonsEdit = [];
        if (nameObj == null || typeof nameObj == "undefined" || nameObj.length == 0){
            $("#add-objective-name").focus();
            swal("Warning!", "Please enter an objective name!", "warning");
            return;
        }
        $('#select-lesson-edit option:selected').map(function(a, item){ idLessonsEdit.push(item.value);});
        $('#select-lesson-edit option:selected').map(function(a, item){ LessonsEdit.push(item.text);});
        $("#indexLessonEdit").modal('show');
        $("#idLevelIndexLessonEdit").val(idLevel);
        $("#idObjectiveIndexLessonEdit").val(idObjective);
        $("#nameObjIndexLessonEdit").val(nameObj);
        $("#indexIndexLessonEdit").val(index);
        $("#descriptionObjIndexLessonEdit").val(descriptionObj);
        number=LessonsEdit.length;
        for(var i=0;i<LessonsEdit.length;i++){
            $("#nameLessonEdit").append('<input id="' + i + 'lse" class="LessonEdits" readonly="readonly" value="' + LessonsEdit[i] + '"  type="text">');
            $("#indexLEdit").append('<input id="' + i + 'le" class="indexLEdits" type="text">');
            $(".LessonEdits").css({'width': '200px', 'text-align': 'center'});
            $(".indexLEdits").css({'width': '200px', 'text-align': 'center'});
        }
        $("#nameLessonEdit").css('width',number*200+200);
        $("#indexLEdit").css('width',number*200+200);


    });
}

function editObjectiveAndLessonIndex(){
    $(document).on("click","#okIndexLessonEdit", function(){
        var idLevel = $("#idLevelIndexLessonEdit").val();
        var idObjective = $("#idObjectiveIndexLessonEdit").val();
        var nameObj = $("#nameObjIndexLessonEdit").val();
        var index = $("#indexIndexLessonEdit").val();
        var descriptionObj =  $("#descriptionObjIndexLessonEdit").val();


        var obj = [];
        for (var i = 0; i < idLessonsEdit.length; i++) {
            obj.push({
                indexLesson : $("#" + i + "le").val(),
                idLesson : idLessonsEdit[i]
            });
        }

        var dto={
            idLevel:idLevel,
            idObjective:idObjective,
            nameObj:nameObj,
            index:index,
            descriptionObj:descriptionObj,
            idLessons:obj
        }
        console.log(dto);
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
                    $("#edit-objective").modal('hide');
                    $("#indexLessonEdit").modal('hide');
                    swal("Success!", "You have edit Object success!", "success");
                    getObjAndTest(idLevel);
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



function openPopopAddObjective(){
    $(document).on("click",".createObj", function(){
        $(".loading-lesson").show();
        $("#add-objective").modal('show');
        getAllLesson("Objective");
        $("#add-objective-name").val("");
        $("#indexOBJadd").val("1");
        $("#add-description").val("");
        var idLevel = $(this).attr("id_lv");
        $("#yesadd").attr("id_level",idLevel);
        $("#yesadd").attr("disabled","disabled");
        //alert(idLevel);
    });
}

//function addObjectiveAndLesson(){
//    $(document).on("click","#yesadd", function(){
//        var nameObj = $("#add-objective-name").val();
//
//        if (nameObj == null || typeof nameObj == "undefined" || nameObj.length == 0){
//            $("#add-objective-name").focus();
//            swal("Warning!", "Please enter an objective name!", "warning");
//            return;
//        }
//        var dto = getDtoAddObjective();
//        $.ajax({
//            url: ObjectiveMappingServlet,
//            type: "POST",
//            dataType: "json",
//            data: {
//                action: "addObj",
//                objDto: JSON.stringify(dto)// to json word,
//            },
//            success: function (data) {
//                if (data.message.indexOf("success") !=-1) {
//                    $("#add-objective").modal('hide');
//                    buildPanelObject(data);
//                }else{
//                    swal("Could not add objective!", data.message.split(":")[1], "error");
//                }
//            },
//            error: function () {
//                swal("Error!", "Could not connect to server", "error");
//            }
//
//        });
//    });
//}
var idLessons = [];
function addObjectiveAndLesson(){
    $(document).on("click","#yesadd", function(){
        var number=0;
        $("#nameLesson").empty();
        $("#indexL").empty();
        var idLevel = $("#yesadd").attr("id_level");
        var idCourse = $("#idCourse").val();
        var nameObj = $("#add-objective-name").val();
        var index = $("#indexOBJadd").val();
        var descriptionObj =  $("#add-description").val();
        idLessons = [];
        var Lessons = [];
        if (nameObj == null || typeof nameObj == "undefined" || nameObj.length == 0){
            $("#add-objective-name").focus();
            swal("Warning!", "Please enter an objective name!", "warning");
            return;
        }
        $('#select-lesson option:selected').map(function(a, item){ idLessons.push(item.value);});
        $('#select-lesson option:selected').map(function(a, item){ Lessons.push(item.text);});
        $("#indexLesson").modal('show');
        $("#idLevelIndexLesson").val(idLevel);
        $("#idCourseIndexLesson").val(idCourse);
        $("#nameObjIndexLesson").val(nameObj);
        $("#indexIndexLesson").val(index);
        $("#descriptionObjIndexLesson").val(descriptionObj);
        number=Lessons.length;
        for(var i=0;i<Lessons.length;i++){
            $("#nameLesson").append('<input id="' + i + 'ls" class="nameLessons" readonly="readonly" value="' + Lessons[i] + '"  type="text">');
            $("#indexL").append('<input id="' + i + 'l" class="indexLs" type="text">');
            $(".nameLessons").css({'width': '200px', 'text-align': 'center'});
            $(".indexLs").css({'width': '200px', 'text-align': 'center'});
        }
        $("#nameLesson").css('width',number*200+200);
        $("#indexL").css('width',number*200+200);


    });
}

function addObjectiveAndLessonIndex(){
    $(document).on("click","#okIndexLesson", function(){
        var idLevel = $("#idLevelIndexLesson").val();
        var idCourse = $("#idCourseIndexLesson").val();
        var nameObj = $("#nameObjIndexLesson").val();
        var index = $("#indexIndexLesson").val();
        var descriptionObj =  $("#descriptionObjIndexLesson").val();


        var obj = [];
        for (var i = 0; i < idLessons.length; i++) {
            obj.push({
                indexLesson : $("#" + i + "l").val(),
                idLesson : idLessons[i]
            });
        }

        var dto={
            idLevel:idLevel,
            idCourse:idCourse,
            nameObj:nameObj,
            index:index,
            descriptionObj:descriptionObj,
            idLessons:obj
        }
        console.log(dto);
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
                    $("#indexLesson").modal('hide');
                    swal("Success!", "You have add Object success!", "success");
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


function openPopupObjAvailable(){
    $(document).on("click",".addObjectiveAvailable", function(){
        $(".loading-lesson").show();
        $("#add-obj-available").modal('show');
        var idLevel = $(this).attr("id_lv");
        getAllObjective(idLevel);
        $("#yesadd-obj-available").attr("id_level",idLevel);
        $("#yesadd-obj-available").attr("disabled","disabled");
        //alert(idLevel);
    });
}

//function addObjAvailable(){
//    $(document).on("click","#yesadd-obj-available", function(){
//        var dto = getDtoAddObjAvailable();
//        $.ajax({
//            url: ObjectiveMappingServlet,
//            type: "POST",
//            dataType: "json",
//            data: {
//                action: "addObjAvailable",
//                objDto: JSON.stringify(dto)// to json word,
//            },
//            success: function (data) {
//                if (data.message.indexOf("success") !=-1) {
//                    $("#add-obj-available").modal('hide');
//                    getObjAndTest($("#yesadd-obj-available").attr("id_level"));
//                }else{
//                    swal("Could not add objective!", data.message.split(":")[1], "error");
//                }
//            },
//            error: function () {
//                swal("Error!", "Could not connect to server", "error");
//            }
//
//        });
//    });
//}
var idObjects = [];
function addObjAvailable(){
    $(document).on("click","#yesadd-obj-available", function(){
        var number=0;
        $("#nameObj").empty();
        $("#index").empty();
        idObjects=[];
        var Objects = [];
        var idLevel = $("#yesadd-obj-available").attr("id_level");
        $('#select-obj-available option:selected').map(function(a, item){ Objects.push(item.text);});
        $('#select-obj-available option:selected').map(function(a, item){ idObjects.push(item.value);});
        number=Objects.length;
        $("#indexObject").modal('show');
        $("#idLevelindexObject").val(idLevel);
        for(var i=0;i<Objects.length;i++){
            $("#nameObj").append('<input id="' + i + '" class="nameObjs" readonly="readonly" value="' + Objects[i] + '"  type="text">');
            $("#index").append('<input id="' + i + 't" class="indexs" type="text">');
            $(".nameObjs").css({'width': '200px', 'text-align': 'center'});
            $(".indexs").css({'width': '200px', 'text-align': 'center'});
        }
        $("#nameObj").css('width',number*200+200);
        $("#index").css('width',number*200+200);

    });
}
function addObjAvailableIndex(){
    $(document).on("click","#okIndexObject", function(){
        var idLevel=$("#idLevelindexObject").val();
        var obj = [];
        for (var i = 0; i < idObjects.length; i++) {
            obj.push({
                index : $("#" + i + "t").val(),
                idObjects : idObjects[i]
            });
        }

        var dto={
            idLevel:idLevel,
            obj:obj
        }
        console.log(dto);
        $.ajax({
            url: ObjectiveMappingServlet,
            type: "POST",
            dataType: "json",
            data: {
                action: "addObjAvailable",
                objDto: JSON.stringify(dto)// to json word,
            },
            success: function (data) {
                if (data.message.indexOf("success") !=-1) {
                    $("#add-obj-available").modal('hide');
                    $("#indexObject").modal('hide');
                    swal("Success!", "You have delete lesson success!", "success");
                    getObjAndTest($("#yesadd-obj-available").attr("id_level"));
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

function getAllObjective(idLevel){
    $.ajax({
        url: ManagementLevelOfCourseServlet,
        type: "POST",
        dataType: "json",
        data: {
            action: "getAllObjective",
            idLevel:idLevel
        },
        success: function (data) {
            if(data.message.indexOf("success")!=-1){
                $("#select-obj-available").empty();
                if(typeof data.data !== undefined && data.data != null) {
                    $.each(data.data, function (idx, obj) {
                        $("#select-obj-available").append("<option value='" + obj.id + "' title='"+obj.description+"'>" + obj.name + "</option>");
                    });
                }else{
                    swal("Info!", "Objective unavailable!", "info");
                    $("#add-obj-available").modal('hide');
                    return;
                }
                $(".loading-lesson").hide();
                $('#select-obj-available').multiselect('destroy');
                $('#select-obj-available').multiselect({ enableFiltering: true, buttonWidth: '200px'});
                $("#container-add-obj-available").find(".btn-group").css("padding-left","14px");
                $('#select-obj-available').multiselect('refresh');
                $("#yesadd-obj-available").removeAttr("disabled");
            }else{
                swal("Error!", data.message.split(":")[1], "error");
            }
        },
        error: function () {
            swal("Error!", "Could not connect to server", "error");
        }

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
                    alertContent += '<a title="'+obj.description+'" href="question-of-lesson-management.jsp?id='+obj.id+'">'+obj.nameUnique+'</a>';
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
            swal("Warning!", "Please enter a percent to pass this test!", "warning");
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
        var percentPass =  $("#edit-percen-pass").val();
        if (nameTest == null || typeof nameTest == "undefined" || nameTest.length == 0){
            $("#edit-test-name").focus();
            swal("Warning!", "Please enter a test name!", "warning");
            return;
        }
        if (percentPass == null || typeof percentPass == "undefined" || percentPass.length == 0){
            $("#edit-percen-pass").focus();
            swal("Warning!", "Please enter a percent to pass this test!", "warning");
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
    editObjectiveAndLessonIndex();
    addObjectiveAndLessonIndex();
    addObjAvailableIndex();
    removeLevel();
    addLevel();
    BuildUI();
    openPopopAddObjective();
    addObjectiveAndLesson();
    openPopupDeleteLesson();
    deleteLesson();
    openPopupEditObjtive();
    editObjective();
    openPopupDeleteObjective();
    deleteObjective();
    openPopupObjAvailable();
    addObjAvailable();

    openPopupAddTest();
    addTest();
    openPopupEditTest();
    editTest();
    openPopupDeleteTest();
    deleteTest();
});


