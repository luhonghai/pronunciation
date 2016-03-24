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
    $html.html("<p>Any courses that you have copied or created yourself are listed on this page.</p>");
    $html.append("<p>Courses must be published in order to make them available to add to your classes.</p>");
    $html.append("<p>The courses are colour codes as follows:</p>");
    $html.append("<p><div style='border-radius:5px;float:left;width:20px;height: 20px;background-color: #17375E'></div>" +
        "<div style='padding-left:30px;'>created by you or copied and edited</div></p>");
    $html.append("<p><div style='border-radius:5px;float:left;width:20px;height: 20px;background-color: #558ED5'></div>" +
        "<div style='padding-left:30px;'>copied without editing</div></p>");
    $html.append("<p><div style='border-radius:5px;float:left;width:20px;height: 20px;background-color: #558ED5'></div>" +
        "<div style='padding-left:30px;'>duplicate of a course you already have in your list" +
        "(You will not be able to publish an exact copy of a course that you already have in ‘my courses’ ." +
        "You need to edit something and publish it to make it available to add to your classes).</div></p>");
    html.append("<p><img float:left;width:20px;height: 20px'></img>" +
        "<div style='padding-left:30px;'>This icon indicates that a course has not been published yet</div></p>");
    $html.append("<p>Information about the originating company, share options and date of the last action (created, edited or duplicated) is displayed on the course title button.</p>");
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

function initHelpMyCourse(){
    var $html = $("<div>");
    $html.html("<p>Any courses that you have copied or created yourself are listed on this page.</p>");
    $html.append("<p>Courses must be published in order to make them available to add to your classes.</p>");
    $html.append("<p>The courses are colour codes as follows:</p>");
    $html.append("<p><div style='border-radius:5px;float:left;width:20px;height: 20px;background-color: #17375E'></div>" +
    "<div style='padding-left:30px;'>created by you or copied and edited</div></p>");
    $html.append("<p><div style='border-radius:5px;float:left;width:20px;height: 20px;background-color: #558ED5'></div>" +
    "<div style='padding-left:30px;'>copied without editing</div></p>");
    $html.append("<p><div style='border-radius:5px;float:left;width:20px;height: 20px;background-color: #7330A5'></div>" +
    "<div style='padding-left:30px;'>duplicate of a course you already have in your list " +
    "(You will not be able to publish an exact copy of a course " +
    "that you already have in ‘my courses’ .You need to edit" +
    "something and publish it to make it available to add to your classes).</div></p>");
    $html.html("<p><img src='/images/treeview/unpublished_button.gif'>This icon indicates that a course has not been published yet.</p>");
    $html.html("<p>Information about the originating company, share options and date of the last action (created, edited or duplicated) is displayed on the course title button.</p>");
    return $html;
}
