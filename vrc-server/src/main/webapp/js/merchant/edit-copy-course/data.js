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
    });
    currentPopup.find("textarea").each(function(){
        //Do stuff here
        $(this).val('');
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


function initHelpCourseADM(){
    var $html = $("<div>");
    $html.html("<p><strong>course content display</strong></p>");
    $html.append("<p>Course content is displayed in a ‘tree’ type directory structure with the following content:</p>");
    $html.append("<p><img src='/images/treeview/course_icon.gif'>1. course - course name, description and sharing options.</p>");
    $html.append("<p><img src='/images/treeview/level_icon.gif'>2. levels – you can add one or more ordered levels to your course. Students will need to pass a test in one level before they can progress to the next one.</p>");
    $html.append("<p><img src='/images/treeview/objective_icon.gif'>3. objectives – you can add one or more objectives for each level.</p>");
    $html.append("<p><img src='/images/treeview/lesson_icon.gif'>4. lessons – you can add one or more lessons for each objective.</p>");
    $html.append("<p><img src=''>5. questions – you can add one or more questions for each lesson.</p>");
    $html.append("<p><img src='/images/treeview/test_icon.gif'>6. test – you can add one test for each level. </p>");
    $html.append("<p><img src='/images/treeview/test_question48x48.gif'>7. test questions- you can add one or more questions for each test. </p>");
    $html.append("<p>You can drag and drop content to move it within the defined structure. E.g. you can move a lesson from one objective to another, but you cannot place it directly below a level. Questions and test questions will always be numbered in sequence when you move them. (Q1 is the top question in the tree).</p>");
    $html.append("<p>Select the arrows to the left of the buttons to view more or less of the structure as required. </p>");
    $html.append("<p><strong>adding and editing course content</strong></p>");

    $html.append("<p>Select a ‘file’ button in the tree to: </p>");
    $html.append("<p><label style='width: 30px;height: 30px;background-color: #558ED5'></label> view details, edit or delete</p>");
    $html.append("<p><label style='width: 30px;height: 30px;background-color: #F7964A'></label> add new details</p>");
    $html.append("<p><strong>Copy/paste</strong></p>");
    $html.append("<p>You can right mouse click on an item to copy and paste to an appropriate directory file.</p>");
    $html.append("<p>E.g. Copy a lesson and paste to the same or a different objective, but you cannot add a lesson to a level.</p>");
    $html.append("<p>All content will be duplicated, so if a lesson has questions added, they will also be copied.</p>");

    $html.append("<p>You will not be able to publish the data you have copied until you have made and saved at least one edit.</p>");
    $html.append("<p><strong>preview</strong></p>");
    $html.append("<p><img src='/images/treeview/preview_button.gif' style='width:50px; height: 50px;'></p>");
    $html.append("<p>Select the ‘preview’ button to view the content as it would appear on the student’s phone. Select the relevant buttons on the ‘phone’ to follow through the structure.</p>");
    $html.append("<p><strong>publish</strong></p>");
    $html.append("<p><img src='/images/treeview/publish_button.gif'></p>");
    $html.append("<p>Select the ‘publish’ button to make the course available to add to your classes and to share with other users (if it is not private).</p>");
    $html.append("<p>Note: You will not be able to publish your course until you have added at least one lesson with at least one question.</p>");

    return $html;
}
