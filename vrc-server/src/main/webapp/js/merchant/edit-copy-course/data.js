/**
 * Created by lantb on 2016-02-23.
 */


function getDivContainTree(){
    return $("#tree");
}

function getProcessBar(){
    return $("#process-bar");
}




function getPopUpHelp(){
    return $("#help-popup");
}
function getPublishBtn(){
    return $("#publish");
}
/**
 * clear all data in form
 */
function clearForm(){
    currentPopup.find("input[type=text]").each(function(){
        //Do stuff here
        $(this).val('');
        $(this).attr('autocomplete','off');
    });
    currentPopup.find("textarea").each(function(){
        //Do stuff here
        $(this).val('');
        $(this).attr('autocomplete','off');
    });
    currentPopup.find("#btndelete").hide();
}

function getCourseName(){
    return $("#courseName");
}

function getCourseDescription(){
    return $("#courseDescription");
}

function getCourseValidateMessage(){
    var validateMsg = $("#validateMsgCourse");
    return validateMsg;
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

function getLessonValidateMessage(){
    return $("#validateLessonMsg");
}

function getWordValidateMessage(){
    return $("#validateWordMsg");
}
function getQuestionValidateMessage(){
    return $("#validateQuestionMsg");
}
function getQuestionForTestValidateMessage(){
    return $("#validateQuestionForTestMsg");
}
function getNameLesson(){
    var lessonName=$('#lessonName');
    return lessonName;
}
function getQuestionListWordEdit(){
    var listWord=$('#listWord');
    return listWord;
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
function getTypeTest(){
    var listWordTest=$("#listWordTests");
    return listWordTest;
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
function getListWordForTest(){
    var listWordTest=$("#listWordTests");
    return listWordTest;
}
function getListWordTest(){
    var listWordTest=$("#listWordTests");
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

function getLabelDeleted(){
    return $('#lbl_deleted');
}
function confirmDeletePopup(){
    return $("#confirm-delete");
}

function btnConfirmDelete(){
    return $('#ConfirmDeletebtn');
}

function btnHelpConfirmDelete(){
    return $('#helpDeleteBtn');
}



function getHeader(){
    return $('#header-company');
}

function changeHeaderCourseName(cName){
    var html = getHeader().html();
    var change = html.split('>')[0] + " > " + html.split('>')[1] + " > " + cName;
    getHeader().html(html);

}