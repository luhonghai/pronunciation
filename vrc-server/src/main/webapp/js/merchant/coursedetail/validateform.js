/**
 * Created by lantb on 2016-02-23.
 */
/**
 *
 * @returns {boolean}
 */
function validateFormLevel(){
    var name = getLevelName().val();
    var description = getLevelDescription().val();
    if(name == '' || typeof name === "undefined"){
        getLevelName().focus();
        getLevelValidateMessage().html("Please enter the name!");
        getLevelValidateMessage().show();
        return false;
    }
    if(description == '' || typeof description === "undefined"){
        getLevelDescription().focus();
        getLevelValidateMessage().html("Please enter the description!");
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
        getObjValidateMessage().html("Please enter the name!");
        getObjValidateMessage().show();
        return false;
    }
    if (description == '' || typeof description === "undefined") {
        getObjDescription().focus();
        getObjValidateMessage().html("Please enter the description!");
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
        getTestValidateMessage().html("Please enter a number!");
        getTestValidateMessage().show();
        return false;
    }
    getTestValidateMessage().hide();
    return true;
}

function validateWord(){
    var word = getAddWord().val();
    if (word == null || typeof word == "undefined" || word.length == 0){
        getLoadPhoneme().attr("disabled",false);
       getAddWord().focus();
        getLessonValidateMessage().html("Word not null!");
        getLessonValidateMessage().show();
        return;
    }
}

function validateFormLesson(){
    var name = getNameLesson().val();
    if(name == '' || typeof name === 'undefined'){
        getNameLesson().focus();
        getLessonValidateMessage().html("Please enter a name");
        getLessonValidateMessage().show();
        return false;
    }

    var description = getDescriptionLesson().val();
    if(description == '' || typeof description === 'undefined'){
        getDescriptionLesson().focus();
        getLessonValidateMessage().html("Please enter a description");
        getLessonValidateMessage().show();
        return false;
    }

    var type = getTypeLesson().val();
    if(type == '' || typeof type === 'undefined'){
        getTypeLesson().focus();
        getLessonValidateMessage().html("Please enter a type");
        getLessonValidateMessage().show();
        return false;
    }
    var detail = getDetailLesson().val();
    if(detail == '' || typeof detail === 'undefined'){
        getDetailLesson().focus();
        getLessonValidateMessage().html("Please enter a details lesson");
        getLessonValidateMessage().show();
        return false;
    }
    getLessonValidateMessage().hide();
    return true;
}