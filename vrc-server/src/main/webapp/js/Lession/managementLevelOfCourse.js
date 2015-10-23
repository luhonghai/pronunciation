/**
 * Created by CMGT400 on 10/5/2015.
 */
var attFunction = false;
var myTable;
var servletName="ManagementLevelOfCourseServlet";
var ObjectiveMappingServlet = "ObjectiveMappingServlet";
function BuildUI(){
    var $selected=$("#level");
    var $listLevel=$("#accordion");
    var id=$("#idCourse").val();
    $('#level option[value!="-1"]').remove();
    $.ajax({
        "url": servletName,
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
            url: servletName,
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
        $.ajax({
            url: servletName,
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


    });

}

function clickLevel(e){
    var idLevel = $(e).attr("data-target").split("#")[1];
    var idCourse=$("#idCourse").val();
    getObjAndTest(idLevel,idCourse);
}

function clickObj(e){
    var idObj   =  $(e).attr("id");
    var idLevel =  $(e).attr("id_lv");
    getLessonsForObj(idObj);
}

function getObjAndTest(idLevel,idCourse){
    $.ajax({
        url: servletName,
        type: "POST",
        dataType: "json",
        data: {
            action: "getObjAndTest",
            idLevel:idLevel,
            idCourse:idCourse
        },
        success: function (data) {
            if(data.message.indexOf("success")!=-1){
                var listObj = data.listObjMap;
                if(listObj.length > 0){
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


function openPopopAddObjective(){
    $(document).on("click",".createObj", function(){
        $("#loading-lesson").show();
        $("#add-objective").modal('show');
        getAllLesson();
        $("#addObjective").val("");
        $("#addDescription").val("");
        var idLevel = $(this).attr("id_lv");
        $("#yesadd").attr("id_level",idLevel);
        $("#yesadd").attr("disabled","disabled");
        //alert(idLevel);
    });
}


function getAllLesson(){
    $.ajax({
        url: servletName,
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
                $("#loading-lesson").hide();
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
                $("#add-objective").modal('hide');
                buildPanelObject(data);
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

});


