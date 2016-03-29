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

