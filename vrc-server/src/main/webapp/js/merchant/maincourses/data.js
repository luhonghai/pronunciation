/**
 * Created by lantb on 2016-02-02.
 */
function getDivContainCourse(){
    return $("#content-courses");
}

function getProcessBar(){
    return $("#process-bar");
}
function getPoupAdd(){
    return $("#add");
}
function getCourseShare(){
   return $('input[type=checkbox]:checked');
}
function getCourseName(){
    return $('#name');
}
function getCourseDescription(){
    return $('#description');
}

function getMsgAddCourse(){
    return $('#validateCourseMsg');
}

function getBtnHelpAddCourse(){
    return $('#helpAddCourse');
}

function getBtnSaveCourse(){
    return $('#btnSaveCourse');
}


function getDropDownSearch(){
    return $('.dropdown');
}
function getSuggestCourseHeader(){
    return $('#suggestCourseHeader');
}

function getSuggestCompany(){
    return $('#suggestCompany');
}

function getSuggestCourse(){
    return $('#suggestCourse');
}
function getFromDate(){
    return $('#fromdate');
}
function getDateTo(){
    return $('#todate');
}


function initDate(){

    getFromDate().datetimepicker({
        format: 'YYYY-MM-DD',
        keepOpen : true
    });


    getDateTo().datetimepicker({
        format: 'YYYY-MM-DD',
        keepOpen : true
    });

}

function getPopUpHelp(){
    return $("#help-popup");
}

function initHelpTopData(){
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

function initHelpAddCourse(){
    var $html = $("<div>");
    $html.html("<p>1. Enter the name of the course that you wish to create." +
        "This is the title that will be displayed on the app.</p>");
    $html.append("<p>2. Enter a short description for the course (not mandatory).</p>");
    $html.append("<p>3. Sharing your course. By default, when you publish it, " +
        "the course you create will be available for use by other companies. " +
        "You can change the sharing settings by selecting ‘my company’ (to share with other teachers " +
        "in your institution or company) or ‘private’ (not shared with anyone else).</p>");
    $html.append("<p>4. Select ‘save’. A button will be added to the course administration page and the description " +
        "that you enter will be displayed when you mouse over it. </p>");
    $html.append("<p>When you click on the button, " +
        "you will be able to edit or delete the course. " +
        "You will be able to edit the ‘share’ settings. " +
        "However,if you have shared the course previously, it will be removed from the main courses page, " +
        "but if other users have saved it in ‘my courses’, you will not be able to stop them from using it. </p>");
    return $html;
}
