/**
 * Created by CMGT400 on 10/5/2015.
 */
var myTable;
var servletName="ManagementLevelOfCourseServlet";

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



function loadDetails(){
    $(".collapse").on('show.bs.collapse', function(){
        var type = $(this).attr(type);
        if(type === "getObjAndTest"){
            var idLevel = $(this).attr("id");
            var idCourse=$("#idCourse").val();
        }else if (type ==="getLessonObj"){

        }else if (type ==="getLessonTest"){

        }
    });
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
                var listObj = data.listObj;
                var test = data.test;
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
        url: servletName,
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
        url: servletName,
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

$(document).ready(function(){
    removeLevel();
    addLevel();
    BuildUI();
    loadDetails();
});


