/**
 * Created by CMG Dev156 on 2015-10-22.
 */

/**
 *
 * @returns {{idLevel: (*|jQuery), idCourse: (*|jQuery), nameObj: (*|jQuery), descriptionObj: (*|jQuery), idLessons: Array}}
 */
function getDtoAddObjective(){
    var idLevel = $("#yesadd").attr("id_level");
    var idCourse = $("#idCourse").val();
    var nameObj = $("#add-objective-name").val();
    var index = $("#indexOBJadd").val();
    var descriptionObj =  $("#add-description").val();
    var idLessons = [];
    $('#select-lesson option:selected').map(function(a, item){ idLessons.push(item.value);});
    var dto = {
        idLevel : idLevel,
        idCourse: idCourse,
        nameObj : nameObj,
        index : index,
        descriptionObj : descriptionObj,
        idLessons : idLessons
    };
    return dto;
}

function getDtoAddObjAvailable(){
    var idLevel = $("#yesadd-obj-available").attr("id_level");
    var idObjects = [];
    $('#select-obj-available option:selected').map(function(a, item){ idObjects.push(item.value);});

    var dto = {
        idLevel : idLevel,
        lstIdObjective : idObjects
    };
    return dto;
}

function getDtoEditObjective(){
    //var idLevel = $("#yesadd").attr("id_level");
    //var idCourse = $("#idCourse").val();
    var nameObj = $("#edit-objective-name").val();
    var descriptionObj =  $("#edit-description").val();
    var idObjective= $("#yesedit").attr("objtive_id");
    var index = $("#indexOBJedit").val();
    var idLessons = [];
    $('#select-lesson-edit option:selected').map(function(a, item){ idLessons.push(item.value);});
    var dto = {
        idObjective: idObjective,
        nameObj : nameObj,
        index:index,
        descriptionObj : descriptionObj,
        idLessons : idLessons
    };
    return dto;
}

function getDtoAddTest(){
    var idLevel = $("#yesadd-test").attr("id_level");
    var idCourse = $("#idCourse").val();
    var nameTest = $("#add-test-name").val();
    var descriptionTest =  $("#add-test-description").val();
    var percentPass =  $("#add-percen-pass").val();
    var idLessons = [];
    $('#select-test-lesson option:selected').map(function(a, item){ idLessons.push(item.value);});
    var dto = {
        idLevel : idLevel,
        idCourse: idCourse,
        nameTest : nameTest,
        descriptionTest : descriptionTest,
        percentPass : percentPass,
        idLessons : idLessons
    };
    return dto;
}

function getDtoEditTest(){
    //var idLevel = $("#yesedit-test").attr("id_level");
    //var idCourse = $("#idCourse").val();
    var nameTest = $("#edit-test-name").val();
    var descriptionTest =  $("#edit-test-description").val();
    var percentPass =  $("#edit-percen-pass").val();
    var idTest= $("#yesedit-test").attr("test_id");
    var idLessons = [];
    $('#select-edit-test-lesson option:selected').map(function(a, item){ idLessons.push(item.value);});
    var dto = {
        idTest : idTest,
        nameTest:nameTest,
        descriptionTest : descriptionTest,
        percentPass : percentPass,
        idLessons : idLessons
    };
    return dto;
}
