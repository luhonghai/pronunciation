/**
 * Created by lantb on 2016-02-23.
 */

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
    });
    currentPopup.find("textarea").each(function(){
        //Do stuff here
        $(this).val('');
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

function getLessonValidateMessage(){
    return $("#validateLessonMsg");
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

function getAddWord(){
    var addWord=$("#addWord");
    return addWord;
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

function initHelpAddLevel(){
    var $html = $("<div>");
    $html.html("<p>1. Enter the name of the level that you wish to create.</p>");
    $html.append("<p>This is the title that will be displayed on the menu when the student enters the course.</p>");
    $html.append("<p>You may wish to create levels to match semesters in the academic timetable.</p>");
    $html.append("<p>2. Enter a description for the level (not mandatory).</p>");
    $html.append("<p>3. Select ‘save’.</p>");
    $html.append("<p>A button will be added to the administration page and the description that you enter will be displayed when you mouse over it.</p>");
    $html.append("<p>When you click on the button, you will be able to edit or delete the level. </p>");
    $html.append("<p>You will not be able to add any new levels until you have added test details for the previous one. </p>");
    return $html;
}
function initHelpAddObjective(){
    var $html = $("<div>");
    $html.html("<p>1. Enter the name of the objective.</p>");
    $html.append("<p>This is the title that will appear in the menu on the app.</p>");
    $html.append("<p>2. Enter a description for the objective.</p>");
    $html.append("<p>This is the text that will be displayed when the student selects the ‘objective’ button.</p>");
    $html.append("<p>3. Select ‘save’.</p>");
    $html.append("<p>A button will be added to the administration page.</p>");
    $html.append("<p>When you click on the button, you will be able to edit or delete the objective.</p>");
    return $html;
}
function initHelpAddLesson(){
    var $html = $("<div>");
    $html.html("<p>1. Enter the name of the lesson.</p>");
    $html.append("<p>This is the title that will appear in the menu on the app.</p>");
    $html.append("<p>2. Enter a description for the lesson.</p>");
    $html.append("<p>This is the text that will be displayed on the question page for the lesson. (Restricted to 60 characters).</p>");
    $html.append("<p>3. Select the lesson type.</p>");
    $html.append("<p>The scoring functionality differs between lesson types.</p>");
    $html.append("<p>4. Enter the lesson details.</p>");
    $html.append("<p>This is the text that will be displayed when the student selects the ‘teacher’s tips’ button on the question page.(Restricted to 240 characters).</p>");
    $html.append("<p>5. Select ‘save’.</p>");
    $html.append("<p>A button will be added to the administration page.</p>");
    $html.append("<p>When you click on the button, you will be able to edit or delete the lesson.</p>");

    return $html;
}

function initHelpAddQuestion(){
    var $html = $("<div>");
    $html.html("<p>1. When you select the ‘add word’ button, the ‘manage word function will be displayed.</p>");
    $html.append("<p>This will vary slightly according to the lesson type.</p>");
    $html.append("<p>2. You will be guided through some steps to set up the scoring.</p>");
    $html.append("<p>Each question will appear in the order that you add them. </p>");
    $html.append("<p>You can drag and drop them to re-order them and the numbering will change automatically.</p>");
    $html.append("<p>A button will be added to the administration page.</p>");
    $html.append("<p>When you hold the mouse over a question a pop up will be displayed enabling you to see which words you have added.</p>");
    $html.append("<p>When you click on the button, you will be able to edit or delete the question.</p>");

    return $html;
}

function initHelpAddWordForQuestion(){
    var $html = $("<div>");
    $html.html("<p>1. Enter a word and load the associated phonemes.</p>");
    $html.append("<p>The phonemes will be displayed in Arpabet and IPA format.</p>");
    $html.append("<p>2. Enter a numeric weight value in the text boxes below each phoneme according to the percentage of the score that you wish to assign.</p>");
    $html.append("<p>3. Select ‘save’.</p>");
    $html.append("<p>You will not be able to save the details until you have entered a weight for each of the phonemes.</p>");
    $html.append("<p>The word will be added to the list for the current question and you will be able to add another word if you choose to do so.</p>");
    $html.append("<p>If the word you are trying to add is not available in the accenteasy dictionary an error message will be displayed.</p>");
    $html.append("<p>When you hold the mouse over a question on the course administration page, a pop up will be displayed enabling you to see which words you have added. </p>");

    return $html;
}

function initHelpAddTest(){
    var $html = $("<div>");
    $html.html("<p>1. Add an explanation for the question.</p>");
    $html.append("<p>This will be displayed on the test page when the question is selected.</p>");
    $html.append("<p>2. Select the question type.</p>");
    $html.append("<p>This will determine the scoring functionality for the question’.</p>");
    $html.append("<p>You will be guided through some steps to set up the scoring.</p>");
    $html.append("<p>Each question will appear in the order that you add them.</p>");
    $html.append("<p>You can drag and drop them to re-order them and the numbering will change automatically.</p>");
    $html.append("<p>A button will be added to the administration page. </p>");
    $html.append("<p>When you hold the mouse over a test question a pop up will be displayed enabling you to see which test words you have added. </p>");
    $html.append("<p>When you click on the button, you will be able to edit or delete the test question. </p>");

    return $html;
}

function initHelpAddWordForTest(){
    var $html = $("<div>");
    $html.html("<p>1. Enter a word and load the associated phonemes.</p>");
    $html.append("<p>The phonemes will be displayed in Arpabet and IPA format.</p>");
    $html.append("<p>2. Enter a numeric weight value in the text boxes below each phoneme according to the percentage of the score that you wish to assign.</p>");
    $html.append("<p>3. Select ‘save’.</p>");
    $html.append("<p>You will not be able to save the details until you have entered a weight for each of the phonemes.</p>");
    $html.append("<p>The word will be added to the list for the current question and you will be able to add another word if you choose to do so.</p>");
    $html.append("<p>If the word you are trying to add is not available in the accenteasy dictionary an error message will be displayed.</p>");
    $html.append("<p>When you hold the mouse over a question on the course administration page, a pop up will be displayed enabling you to see which words you have added. </p>");

    return $html;
}