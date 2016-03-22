/**
 * Created by lantb on 2016-02-23.
 */

function isNumberKey(evt,e){
    var charCode = (evt.which) ? evt.which : event.keyCode;
    if (charCode != 46 && charCode > 31
        && (charCode < 48 || charCode > 57)){
        return false;
    }
    return true;
}

function validateFormCourse(){
    var name = getCourseName().val();
    if(name == '' || typeof name === "undefined"){
        getCourseName().focus();
        getCourseValidateMessage().html("Please enter name");
        getCourseValidateMessage().show();
        return false;
    }
    var description = getCourseDescription().val();
    if(description == '' || typeof description === "undefined"){
        getLevelDescription().focus();
        getCourseDescription().html("Please enter description");
        getCourseDescription().show();
        return false;
    }
    getCourseValidateMessage().hide();
    return true;
}



/**
 *
 * @returns {boolean}
 */
function validateFormLevel(){
    var name = getLevelName().val();
    var description = getLevelDescription().val();
    if(name == '' || typeof name === "undefined"){
        getLevelName().focus();
        getLevelValidateMessage().html("Please enter name");
        getLevelValidateMessage().show();
        return false;
    }
    if(description == '' || typeof description === "undefined"){
        getLevelDescription().focus();
        getLevelValidateMessage().html("Please enter description");
        getLevelValidateMessage().show();
        return false;
    }
    getLevelValidateMessage().hide();
    return true;
}

/**
 *
 * @returns {boolean}
 */

function validateFormObj() {
    var name = getObjName().val();
    var description = getObjDescription().val();
    if (name == '' || typeof name === "undefined") {
        getObjName().focus();
        getObjValidateMessage().html("Please enter name");
        getObjValidateMessage().show();
        return false;
    }
    if (description == '' || typeof description === "undefined") {
        getObjDescription().focus();
        getObjValidateMessage().html("Please enter description");
        getObjValidateMessage().show();
        return false;
    }
    getObjValidateMessage().hide();
    return true;
}

/**
 *
 * @returns {boolean}
 */
function validateFormTest(){
    var percent = getPercentPass().val();
    if (percent == '' || typeof percent === "undefined") {
        getPercentPass().focus();
        getTestValidateMessage().html("Please enter number");
        getTestValidateMessage().show();
        return false;
    }
    if (percent.indexOf(".")!= -1){
        var p = percent.split(".")[0];
        if(p.length > 2){
            getPercentPass().focus();
            getTestValidateMessage().html("please enter percent smaller than 100 percent");
            getTestValidateMessage().show();
            return false;
        }
    }else{
        if(percent.length > 2){
            getPercentPass().focus();
            getTestValidateMessage().html("please enter percent smaller than 100 percent");
            getTestValidateMessage().show();
            return false;
        }
    }
    getTestValidateMessage().hide();
    return true;
}

function validateWord(){
    var word = getAddWord().val();
    if (word == null || typeof word == "undefined" || word.length == 0){
        getLoadPhoneme().attr("disabled",false);
        getAddWord().focus();
        getWordValidateMessage().html("Word not null");
        getWordValidateMessage().show();
        return false;
    }
    return true;
}

function validateFormLesson(){
    var name = getNameLesson().val();
    if(name == '' || typeof name === 'undefined'){
        getNameLesson().focus();
        getLessonValidateMessage().html("Please enter name");
        getLessonValidateMessage().show();
        return false;
    }

    var description = getDescriptionLesson().val();
    if(description == '' || typeof description === 'undefined'){
        getDescriptionLesson().focus();
        getLessonValidateMessage().html("Please enter description");
        getLessonValidateMessage().show();
        return false;
    }

    var type = getTypeLesson().val();
    if(type == '' || typeof type === 'undefined'){
        getTypeLesson().focus();
        getLessonValidateMessage().html("Please enter type");
        getLessonValidateMessage().show();
        return false;
    }
    var detail = getDetailLesson().val();
    if(detail == '' || typeof detail === 'undefined'){
        getDetailLesson().focus();
        getLessonValidateMessage().html("Please enter lesson details");
        getLessonValidateMessage().show();
        return false;
    }
    getLessonValidateMessage().hide();
    return true;
}