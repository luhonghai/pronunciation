/**
 * Created by lantb on 2016-02-23.
 */
/**
 * clear all data in form
 */
function getDivContainTree(){
    return $("#tree");
}

function getProcessBar(){
    return $("#process-bar");
}

function getCourseName(){
    return $("#courseName");
}

function getCourseDescription(){
    return $("#courseDescription");
}

function clearForm(){
    currentPopup.find("input[type=text]").each(function(){
        //Do stuff here
        $(this).val('');
        $(this).attr('disabled','disabled');
    });
    currentPopup.find("textarea").each(function(){
        //Do stuff here
        $(this).val('');
        $(this).attr('disabled','disabled');
    });

    currentPopup.find("select").each(function(){
        $(this).attr('disabled','disabled');
    });
    currentPopup.find("#btndelete").hide();
}

function getLevelName(){
    var name = $('#lvName');
    return name;
}

function getLevelDescription(){
    var description = $('#lvDesc');
    return description;
}

function getLevelValidateMessage(){
    var validateMsg = $("#validateLvMsg");
    return validateMsg;
}

function getObjName(){
    var name = $('#objName');
    return name;
}

function getObjDescription(){
    var description = $('#objDesc');
    return description;
}

function getObjValidateMessage(){
    var validateMsg = $("#validateObjMsg");
    return validateMsg;
}


function getPercentPass(){
    var percentPass = $("#percent");
    return percentPass;
}

function getTestValidateMessage(){
    var validateMsg = $("#validateTestMsg");
    return validateMsg;
}

function getNameLesson(){
    var lessonName=$('#lessonName');
    return lessonName;
}
function getDescriptionLesson(){
    var lessonDescription=$('#lessonDesc');
    return lessonDescription;
}
function getTypeLesson(){
    var lessonType=$('#lessonType');
    return lessonType;
}
function getDetailLesson(){
    var lessonDetails=$('#lessonDetail');
    return lessonDetails;
}
function getPopUpHelp(){
    return $("#help-popup");
}


function getQuestionListWordEdit(){
    var listWord=$('#listWord');
    return listWord;
}

function getListPhonemes(){
    var listPhonemes=$("#listPhonmes");
    return listPhonemes;
}
function getListIPA(){
    var listIPA=$("#listIpa");
    return listIPA;
}
function getListWeight(){
    var listWeight=$("#listWeight");
    return listWeight;
}
function getWeight(){
    var weight=$("#weight");
    return weight;
}
function getListWord(){
    var listWord=$("#listWord");
    return listWord;
}

function getAddWord(){
    var addWord=$("#addWord");
    return addWord;
}
function getTypeTest(){
    var typeTest=$("#testType");
    return typeTest;
}
function getExplanationTest(){
    var explanationTest=$("#explanation");
    return explanationTest;
}
function getListWordTest(){
    var listWordTest=$("#listWordTest");
    return listWordTest;
}

function getPhonemeLable(){
    var phonemeLable=$(".phoneme-lable");
    return phonemeLable;
}

function getIPAlable(){
    var ipaLable=$(".ipa-lable");
    return ipaLable;
}
function getWeightLable(){
    var weightLable=$(".weight-lable");
    return weightLable;
}

function getLoadPhoneme(){
    var loadPhoneme=$("#loadPhonemes");
    return loadPhoneme;
}

function getListWordForTest(){
    var listWordTest=$("#listWordTests");
    return listWordTest;
}

function getTypeTest(){
    var listWordTest=$("#listWordTests");
    return listWordTest;
}
function getExplanationTest(){
    var explanationTest=$("#explanation");
    return explanationTest;
}
function initHelpTopData(){
    var $html = $("<div>");
    $html.html("<p>1. You can check any of the content by hovering over and selecting " +
        "the appropriate buttons in the tree. </p>");
    $html.append("<p>2. You can see what the course would look like on a phone by selecting ‘preview’. " +
        "<img style='float:right' src='/images/treeview/preview_button.gif' width='30px' height='30px'> </p>");
    $html.append("<p>3. If you would like to duplicate the course, select ‘copy’." +
        "This will add the course to your list of ‘my courses’. " +
        "You can edit the content and you will need to " +
        "publish it to make it available to add to your classes." +
        "<img style='float:right' src='/images/treeview/duplicated_button.gif' width='30px' height='30px'> </p>");
    return $html;
}