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
        getCourseValidateMessage().html("please enter name");
        getCourseValidateMessage().show();
        return false;
    }
    var description = getCourseDescription().val();
    if(description == '' || typeof description === "undefined"){
        getLevelDescription().focus();
        getCourseDescription().html("please enter description");
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
        getLevelValidateMessage().html("please enter name");
        getLevelValidateMessage().show();
        return false;
    }
    if(description == '' || typeof description === "undefined"){
        getLevelDescription().focus();
        getLevelValidateMessage().html("please enter description");
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
        getObjValidateMessage().html("please enter name");
        getObjValidateMessage().show();
        return false;
    }
    if (description == '' || typeof description === "undefined") {
        getObjDescription().focus();
        getObjValidateMessage().html("please enter description");
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
        getTestValidateMessage().html("please enter number");
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
        getWordValidateMessage().html("please enter a word");
        getWordValidateMessage().show();
        return false;
    }
    return true;
}

function validateSaveQuestion(listWord){
    var word = getAddWord().val();
    if (listWord == null || listWord.length == 0){
        getQuestionValidateMessage().html("please add word");
        getQuestionValidateMessage().show();
        return false;
    }
    return true;
}

function validateSaveQuestionForTest(listWord){
    var word = getAddWord().val();
    if (listWord == null || listWord.length == 0){
        getQuestionForTestValidateMessage().html("please add word");
        getQuestionForTestValidateMessage().show();
        return false;
    }
    return true;
}
function validateFormLesson(){
    var name = getNameLesson().val();
    if(name == '' || typeof name === 'undefined'){
        getNameLesson().focus();
        getLessonValidateMessage().html("please enter name");
        getLessonValidateMessage().show();
        return false;
    }

    var description = getDescriptionLesson().val();
    if(description == '' || typeof description === 'undefined'){
        getDescriptionLesson().focus();
        getLessonValidateMessage().html("please enter description");
        getLessonValidateMessage().show();
        return false;
    }

    var type = getTypeLesson().val();
    if(type == '' || typeof type === 'undefined'){
        getTypeLesson().focus();
        getLessonValidateMessage().html("please enter type");
        getLessonValidateMessage().show();
        return false;
    }
    var detail = getDetailLesson().val();
    if(detail == '' || typeof detail === 'undefined'){
        getDetailLesson().focus();
        getLessonValidateMessage().html("please enter lesson details");
        getLessonValidateMessage().show();
        return false;
    }
    getLessonValidateMessage().hide();
    return true;
}

//validate form for page main course

function validateFormAddCourse(){
    var name = getCourseName().val();
    var description = getCourseDescription().val();
    if(name == '' || typeof name === "undefined"){
        getCourseName().focus();
        getMsgAddCourse().html('please enter a name');
        getMsgAddCourse().show();
        return false;
    }
    if(description == '' || typeof description === "undefined"){
        getCourseDescription().focus();
        getMsgAddCourse().html('please enter a description');
        getMsgAddCourse().show();
        return false;
    }
    var share = getCourseShare().val();
    if(share == '' || typeof share ==="undefined"){
        getMsgAddCourse().html("please select a share option");
        getMsgAddCourse().show();
        return false;
    }
    getMsgAddCourse().hide();
    return true;
}

function validateFormSearchHeader(){
    var course = getSuggestCourseHeader().val();
    if(course == '' || typeof course === "undefined"){
        getSuggestCourseHeader().focus();
        return false;
    }

    return true;
}


function clearFormAdd(){
    getMsgAddCourse().hide();
    getPoupAdd().find("input[type=text]").each(function(){
        //Do stuff here
        $(this).val('');
        $(this).attr('autocomplete','off');
    });
    getPoupAdd().find("textarea").each(function(){
        //Do stuff here
        $(this).val('');
        $(this).attr('autocomplete','off');
    });

    $('#shareall').prop('checked', true);
    $('input[type="checkbox"]').not($('#shareall')).prop('checked', false);
}

function clearFormSearch(){
    getDropDownSearch().find("input[type=text]").each(function(){
        $(this).val('');
    });
}

