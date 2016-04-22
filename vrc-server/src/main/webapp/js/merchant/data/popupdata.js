/**
 * Created by lantb on 2016-03-29.
 */
function initHelpDelete(){
    var $html = $("<div>");
    $html.html("<p>When you delete a file, " +
        "any content you have added at lower stages within the structure will also be deleted. </p>");
    $html.append("<p class='delete-header'>lesson</p>");
    $html.append("<p>If you delete a lesson, the associated questions will also be removed</p>");
    $html.append("<p class='delete-header'>objective</p>");
    $html.append("<p>If you delete an objective, any associated lessons " +
        "and their questions will also be removed.</p>");
    $html.append("<p class='delete-header'>test</p>");
    $html.append("<p>If you delete a test, any associated test questions will also be removed.</p>");
    $html.append("<p class='delete-header'>level </p>");
    $html.append("<p>If you delete a level, any associated, objectives, tests, lessons and questions will be removed.</p>");
    $html.append("<p class='delete-header'>course </p>");
    $html.append("<p>If you delete a course, all associated, levels, objectives, lessons, tests and questions will be removed. The course will also be removed from your ‘my courses’ page and from ‘all courses’. If you shared the course, other users will still be able to use it if they added it to their own ‘my courses’ list before you deleted it. </p>");
    return $html;
}

function initHelpAddWord(){
    var $html = $("<div>");
    $html.html("<p>1. Enter a word and load the associated phonemes.</p>");
    $html.append("<p>The phonemes will be displayed in Arpabet and IPA format.</p>");
    $html.append("<p>2. Enter a numeric weight value in the text boxes below each phoneme according to the percentage of the score that you wish to assign.</p>");
    $html.append("<p>3. Select 'save'. </p>");
    $html.append("<p>You will not be able to save the details until you have entered a weight for each of the phonemes.</p>");
    $html.append("<p>The word will be added to the list for the current question and you will be able to add another word if you choose to do so.</p>");
    $html.append("<p>If the word you are trying to add is not available in the accenteasy dictionary an error message will be displayed.</p>");
    $html.append("<p>When you hold the mouse over a question on the course administration page, a pop up will be displayed enabling you to see which words you have added. </p>");
    return $html;
}

function initHelpCourseADM(){
    var $html = $("<div class='row' style='padding:0px 10px 0px 10px'>");
    $html.html("<div style='padding-bottom:10px' class='col-sm-12'><strong>course content display</strong></div>");
    $html.append("<div style='padding-bottom:10px' class='col-sm-12'>Course content is displayed in a ‘tree’ type directory structure with the following content:</div>");
    $html.append("<div style='padding-bottom:10px' class='col-sm-12'>" +
        "<div class='col-sm-1'><img width='20px' height='20px' src='/images/popup/course_icon.gif'></div>" +
        "<div class='col-sm-11'>1. course - course name, description and sharing options.</div></div>");
    $html.append("<div style='padding-bottom:10px' class='col-sm-12'>" +
        "<div class='col-sm-1'><img width='20px' height='20px' src='/images/popup/level_icon.gif'></div>" +
        "<div class='col-sm-11'>2. levels – you can add one or more ordered levels to your course. " +
        "Students will need to pass a test in one level before they can progress to the next one.</div></div>");
    $html.append("<div style='padding-bottom:10px' class='col-sm-12'>" +
        "<div class='col-sm-1'><img width='20px' height='20px' src='/images/popup/objective_icon.gif'></div>" +
        "<div class='col-sm-11'>3. objectives – you can add one or more objectives for each level.</div></div>");
    $html.append("<div style='padding-bottom:10px' class='col-sm-12'>" +
        "<div class='col-sm-1'><img width='20px' height='20px' src='/images/popup/lesson_icon.gif'></div>" +
        "<div class='col-sm-11'>4. lessons – you can add one or more lessons for each objective.</div></div>");
    $html.append("<div style='padding-bottom:10px' class='col-sm-12'>" +
        "<div class='col-sm-1'><img width='20px' height='20px' src='images/popup/question_icon.gif'></div>" +
        "<div class='col-sm-11'>5. questions – you can add one or more questions for each lesson.</div></div>");
    $html.append("<div style='padding-bottom:10px' class='col-sm-12'>" +
        "<div class='col-sm-1'><img width='20px' height='20px' src='/images/popup/test_icon.gif'></div>" +
        "<div class='col-sm-11'>6. test – you can add one test for each level.</div></div>");
    $html.append("<div style='padding-bottom:10px' class='col-sm-12'>" +
        "<div class='col-sm-1'><img width='20px' height='20px' src='/images/popup/test_question_icon.gif'></div>" +
        "<div class='col-sm-11'>7. test questions- you can add one or more questions for each test.</div></div>");
    $html.append("<div style='padding-bottom:10px' class='col-sm-12'>You can drag and drop content to move it around within the current level E.g. you can move lessons around to change the order. Questions and test questions will always be numbered in sequence when you move them. (Q1 is the top question in the tree).</div>");
    $html.append("<div style='padding-bottom:10px' class='col-sm-12'>Select the arrows to the left of the buttons to view more or less of the structure as required. </div>");
    $html.append("<div style='padding-bottom:10px' class='col-sm-12'><strong>adding and editing course content</strong></div>");
    $html.append("<div style='padding-bottom:10px' class='col-sm-12'>Select a 'file' button in the tree to: </div>");
    $html.append("<div style='padding-bottom:10px' class='col-sm-12'>" +
        "<label style='border-radius:5px;width: 20px;height: 20px;background-color: #558ED5'>" +
        "</label> view details, edit or delete</div>");
    $html.append("<div style='padding-bottom:10px' class='col-sm-12'>" +
        "<label style='border-radius:5px;width: 20px;height: 20px;background-color: #F7964A'>" +
        "</label> add new details</div>");
    $html.append("<div style='padding-bottom:10px' class='col-sm-12'><strong>Note : </strong> Courses can not be edited while they are assigned to one or more classes. If you need to change course content you will need to remove it from the relevant classes to update, then re-assign. </div>");
    $html.append("<div style='padding-bottom:10px' class='col-sm-12'><strong>Copy/paste</strong></div>");
    $html.append("<div style='padding-bottom:10px' class='col-sm-12'>You can right mouse click on an item to copy and paste" +
        " to an appropriate directory file.</div>");
    $html.append("<div style='padding-bottom:10px' class='col-sm-12'>E.g. Copy a lesson and paste to the same or a different objective, " +
        "but you cannot add a lesson to a level.</div>");
    $html.append("<div style='padding-bottom:10px' class='col-sm-12'>All content will be duplicated, " +
        "so if a lesson has questions added, they will also be copied.</div>");
    $html.append("<div style='padding-bottom:10px' class='col-sm-12'>You will not be able to publish the data you have copied " +
        "until you have made and saved at least one edit.</div>");
    $html.append("<div style='padding-bottom:10px' class='col-sm-12'><strong>preview</strong></div>");
    $html.append("<div style='padding-bottom:10px' class='col-sm-12'>" +
        "<img src='/images/treeview/preview_button.gif' style='width:30px; height: 30px;'></div>");
    $html.append("<div style='padding-bottom:10px' class='col-sm-12'>Select the 'preview' button to view the content as it would appear on the student’s phone. " +
        "Select the relevant buttons on the ‘phone’ to follow through the structure.</div>");
    $html.append("<div style='padding-bottom:10px' class='col-sm-12'><strong>publish</strong></div>");
    $html.append("<div style='padding-bottom:10px' class='col-sm-12'>" +
        "<img style='width:30px; height: 30px;' src='/images/treeview/publish_button.gif'></div>");
    $html.append("<div style='padding-bottom:10px' class='col-sm-12'>Select the 'publish' button " +
        "to make the course available to add " +
        "to your classes and to share with other users (if it is not private).</div>");
    $html.append("<div style='padding-bottom:10px'  class='col-sm-12'>Note: You will not be able to publish your course " +
        "until you have added at least one lesson with at least one question.</div>");
    return $html;
}

function initHelpAddCourse(){
    var $html = $("<div>");
    $html.html("<p>1. Enter the name of the course that you wish to create." +
        "This is the title that will be displayed on the app.</p>");
    $html.append("<p>2. Enter a short description for the course (not mandatory).</p>");
    $html.append("<p>3. Sharing your course. By default, when you publish it, " +
        "the course you create will be available for use by other companies. " +
        "You can change the sharing settings by selecting ‘my company’ (to share with other teachers " +
        "in your institution or company) or ‘private’ (not shared with anyone else).</p>");
    $html.append("<p>4. Select 'save'. A button will be added to the course administration page and the description " +
        "that you enter will be displayed when you mouse over it. </p>");
    $html.append("<p>When you click on the button, " +
        "you will be able to edit or delete the course. " +
        "You will be able to edit the ‘share’ settings. " +
        "However,if you have shared the course previously, it will be removed from the main courses page, " +
        "but if other users have saved it in 'my courses', you will not be able to stop them from using it. </p>");
    return $html;
}

function initHelpAddWordForTest(){
    var $html = $("<div>");
    $html.html("<p>1. Enter a word and load the associated phonemes.</p>");
    $html.append("<p>The phonemes will be displayed in Arpabet and IPA format.</p>");
    $html.append("<p>2. Enter a numeric weight value in the text boxes below each phoneme according to the percentage of the score that you wish to assign.</p>");
    $html.append("<p>3. Select 'save'.</p>");
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

function initHelpAddWordForQuestion(){
    var $html = $("<div>");
    $html.html("<p>1. Enter a word and load the associated phonemes.</p>");
    $html.append("<p>The phonemes will be displayed in Arpabet and IPA format.</p>");
    $html.append("<p>2. Enter a numeric weight value in the text boxes below each phoneme according to the percentage of the score that you wish to assign.</p>");
    $html.append("<p>3. Select 'save'.</p>");
    $html.append("<p>You will not be able to save the details until you have entered a weight for each of the phonemes.</p>");
    $html.append("<p>The word will be added to the list for the current question and you will be able to add another word if you choose to do so.</p>");
    $html.append("<p>If the word you are trying to add is not available in the accenteasy dictionary an error message will be displayed.</p>");
    $html.append("<p>When you hold the mouse over a question on the course administration page, a pop up will be displayed enabling you to see which words you have added. </p>");

    return $html;
}

function initHelpAddLevel(){
    var $html = $("<div>");
    $html.html("<p>1. Enter the name of the level that you wish to create.</p>");
    $html.append("<p>This is the title that will be displayed on the menu when the student enters the course.</p>");
    $html.append("<p>You may wish to create levels to match semesters in the academic timetable.</p>");
    $html.append("<p>2. Enter a description for the level (not mandatory).</p>");
    $html.append("<p>3. Select 'save'.</p>");
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
    $html.append("<p>3. Select 'save'.</p>");
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
    $html.append("<p>5. Select 'save'.</p>");
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

function initHelpTopDataMainCourse(){
    var $html = $("<div>");
    $html.html("<p>A selection of courses that have been created and " +
        "are available for you to use are listed on this page.</p>");
    $html.append("<p>The courses in the list may have been created by you or by someone " +
        "else that has chosen to share them, either from within or from outside your organisation.</p>");
    $html.append("<p>The courses are colour codes as follows:</p>");
    $html.append("<p><div style='border-radius:5px;float:left;width:20px;height: 20px;background-color: #17375E'></div>" +
        "<div style='padding-left:30px;'>created by you or someone in your organisation</div></p>");
    $html.append("<p><div style='border-radius:5px;float:left;width:20px;height: 20px;background-color: #558ED5'></div>" +
        "<div style='padding-left:30px;'>created by someone in a different organisation</div></p>");
    $html.append("<p>The name of the organisation that originally " +
        "created the course is displayed together with the date that it was published. </p>");
    $html.append("<p>You can find courses from the company name, course title " +
        "or by the created date using the search function and drop down filter at the top of the page.</p>");
    $html.append("<p>If you hover over the course title button you will be able " +
        "to view a short description if it was added by the originator. </p>");
    $html.append("<p>If you double click on a course title button, you can view the content and decide if you would like to add it to your own list of courses." +
        "If you choose to do this, you will create a duplicate copy and " +
        "you will be able to edit the content to suit your own requirements</p>");
    return $html;
}


function initHelpTopDataMyCourse(){
    var $html = $("<div>");
    $html.html("<p>Any courses that you have copied or created yourself are listed on this page.</p>");
    $html.append("<p>Courses must be published in order to make them available to add to your classes.</p>");
    $html.append("<p>The courses are colour codes as follows:</p>");
    $html.append("<p><div style='border-radius:5px;float:left;width:20px;height: 20px;background-color: #17375E'></div>" +
        "<div style='padding-left:30px;'>created by you or copied and edited</div></p>");
    $html.append("<p><div style='border-radius:5px;float:left;width:20px;height: 20px;background-color: #558ED5'></div>" +
        "<div style='padding-left:30px;'>copied without editing</div></p>");
    $html.append("<p><div style='border-radius:5px;float:left;width:20px;height: 20px;background-color: #7030A0'></div>" +
        "<div style='padding-left:30px;'>duplicate of a course you already have in your list" +
        "(You will not be able to publish an exact copy of a course that you already have in ‘my courses’ ." +
        "You need to edit something and publish it to make it available to add to your classes).</div></p>");
    $html.append("<p><img src='/images/treeview/unpublished_button.gif' style='float:left;width:24px;height: 24px'>" +
        "This icon indicates that a course has not been published yet</p>");
    $html.append("<p>Information about the originating company, share options and date of the last action (created, edited or duplicated) is displayed on the course title button.</p>");
    return $html;
}

function initHelpTopDataReviewCourse(){
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
    $html.append("<p><strong>Note : </strong>  When you duplicate a course, you will ned to edit something to be able to publish it. " +
        "The course name will initially be prefixed with the words “copy of”. If you do not want to change any content, you can simply edit the name to remove the extra text.</p>");
    return $html;
}
