/**
 * Created by lantb on 2016-02-24.
 */
var servletAdd = "/TreeAddNodeServlet";
var servletEdit = "/TreeEditNodeServlet";
var servletDelete = "/TreeDeleteNodeServlet";
var servletPublish = "/PublishCourseServlet";
var servletCopy = "/CopyServlet";
var progress;
var state;
/**
 * connect to server when edit course
 */
function isNumberKey(evt,e){
    var charCode = (evt.which) ? evt.which : event.keyCode;
    if (charCode != 46 && charCode > 31
        && (charCode < 48 || charCode > 57)){
        return false;
    }
    return true;
}

function editCourse(){
    $.ajax({
        url : servletEdit,
        type : "POST",
        data : {
            action: action_edit_course,
            idCourse : idCourse,
            name: getCourseName().val(),
            description: getCourseDescription().val()
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                //reload the tree
                if(nameOfCourse != getCourseName().val){
                    isEditedTitle = true;
                    UpdateStateCourse();
                }
                currentParent = null;
                firstLoad = true;
                reloadTree();
                currentPopup.modal('hide');
                changeHeaderCourseName(getCourseName().val());
                $("#listWord").empty();
                swal("", "You have updated course successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").html(data);
                currentPopup.find(".validateMsg").show()
            };
        },
        error: function () {
            currentPopup.find(".validateMsg").html("Could not connect to server!");
            currentPopup.find(".validateMsg").show();
        }
    });
}

/**
 * connect to server when delete level
 */
function deleteCourse(){
    $.ajax({
        url : servletDelete,
        type : "POST",
        data : {
            action: action_delete_course,
            idCourse : idCourse
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                //reload the tree
                confirmDeletePopup().modal('hide');
                currentPopup.modal('hide');
                swal("", "You have deleted course successfully", "success");
                window.history.back();
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").html(data);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("Could not connect to server!");
            currentPopup.find(".validateMsg").show();
        }
    });
}



function addLevel(){
    $.ajax({
        url : servletAdd,
        type : "POST",
        data : {
            action: action_add_level,
            idCourse : idCourse,
            name: getLevelName().val(),
            description: getLevelDescription().val()
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                //reload the tree
                isEditedContent = true;
                UpdateStateCourse();
                reloadTree();
                currentPopup.modal('hide');
                swal("", "You have added Level successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").html(data);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("Could not connect to server");
            currentPopup.find(".validateMsg").show();
        }
    });
}

/**
 * connect to server when edit level
 */
function editLevel(){
    $.ajax({
        url : servletEdit,
        type : "POST",
        data : {
            action: action_edit_level,
            idLevel : currentPopup.find(".idHidden").val(),
            idCourse : idCourse,
            name: getLevelName().val(),
            description: getLevelDescription().val()
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                //reload the tree
                isEditedContent = true;
                UpdateStateCourse();
                reloadTree();
                currentPopup.modal('hide');
                swal("", "You have updated Level successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").html(data);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("Could not connect to server");
            currentPopup.find(".validateMsg").show();
        }
    });
}

/**
 * connect to server when delete level
 */
function deleteLevel(){
    $.ajax({
        url : servletDelete,
        type : "POST",
        data : {
            action: action_delete_level,
            idLevel : currentPopup.find(".idHidden").val(),
            idCourse : idCourse,
            name: getLevelName().val(),
            description: getLevelDescription().val()
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                //reload the tree
                isEditedContent = true;
                UpdateStateCourse();
                reloadTree();
                confirmDeletePopup().modal('hide');
                currentPopup.modal('hide');
                swal("", "You have deleted Level successfully", "success");
            }else{
                //add false show the error
                confirmDeletePopup().modal('hide');
                currentPopup.find(".validateMsg").html(data);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            confirmDeletePopup().modal('hide');
            currentPopup.find(".validateMsg").html("Could not connect to server");
            currentPopup.find(".validateMsg").show();
        }
    });
}



/**
 * connect to server when add obj
 */
function addObj(){
    $.ajax({
        url : servletAdd,
        type : "POST",
        data : {
            action: action_add_obj,
            idLevel : getObjName().attr("idLevel"),
            name: getObjName().val(),
            description: getObjDescription().val()
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                //reload the tree
                isEditedContent = true;
                UpdateStateCourse();
                reloadTree();
                currentPopup.modal('hide');
                swal("", "You have added objective successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").html(data);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("Could not connect to server");
            currentPopup.find(".validateMsg").show();
        }
    });
}

/**
 * connect to server when edit obj
 */
function editObj(){
    $.ajax({
        url : servletEdit,
        type : "POST",
        data : {
            action: action_edit_obj,
            idObj : currentPopup.find(".idHidden").val(),
            idLevel : getObjName().attr("idLevel"),
            name: getObjName().val(),
            description: getObjDescription().val()
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                //reload the tree
                isEditedContent = true;
                UpdateStateCourse();
                reloadTree();
                currentPopup.modal('hide');
                swal("", "You have updated objective successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").html(data);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("Could not connect to server");
            currentPopup.find(".validateMsg").show();
        }
    });
}

/**
 * connect to server when delete obj
 */
function deleteObj(){
    $.ajax({
        url : servletDelete,
        type : "POST",
        data : {
            action: action_delete_obj,
            idLevel : getObjName().attr("idLevel"),
            idObj : currentPopup.find(".idHidden").val()
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                //reload the tree
                isEditedContent = true;
                UpdateStateCourse();
                reloadTree();
                confirmDeletePopup().modal('hide');
                currentPopup.modal('hide');
                swal("", "You have deleted objective successfully", "success");
            }else{
                //add false show the error
                confirmDeletePopup().modal('hide');
                currentPopup.find(".validateMsg").html(data);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("Could not connect to server");
            currentPopup.find(".validateMsg").show();
        }
    });
}


/**
 * connect to server when add obj
 */
function addTest(){
    $.ajax({
        url : servletAdd,
        type : "POST",
        data : {
            action: action_add_test,
            idLevel : getPercentPass().attr("idLevel"),
            percent: getPercentPass().val()
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                //reload the tree
                isEditedContent = true;
                UpdateStateCourse();
                reloadTree();
                currentPopup.modal('hide');
                swal("", "You have added test successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").html(data);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("Could not connect to server");
            currentPopup.find(".validateMsg").show();
        }
    });
}

/**
 * connect to server when edit obj
 */
function editTest(){
    $.ajax({
        url : servletEdit,
        type : "POST",
        data : {
            action: action_edit_test,
            idTest : currentPopup.find(".idHidden").val(),
            idLevel : getPercentPass().attr("idLevel"),
            percent: getPercentPass().val()
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                //reload the tree
                isEditedContent = true;
                UpdateStateCourse();
                reloadTree();
                currentPopup.modal('hide');
                swal("", "You have updated test successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").html(data);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("Could not connect to server");
            currentPopup.find(".validateMsg").show();
        }
    });
}

/**
 * connect to server when delete obj
 */
function deleteTest(){
    $.ajax({
        url : servletDelete,
        type : "POST",
        data : {
            action: action_delete_test,
            idLevel : getPercentPass().attr("idLevel"),
            idTest : currentPopup.find(".idHidden").val()
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                //reload the tree
                isEditedContent = true;
                UpdateStateCourse();
                reloadTree();
                confirmDeletePopup().modal('hide');
                currentPopup.modal('hide');
                swal("", "You have deleted test successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").html(data);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("Could not connect to server");
            currentPopup.find(".validateMsg").show();
        }
    });
}





function addLesson(){
    $.ajax({
        url : servletAdd,
        type : "POST",
        data : {
            action: action_add_lesson,
            idObj : getNameLesson().attr("objID"),
            name: getNameLesson().val(),
            description:getDescriptionLesson().val(),
            type:getTypeLesson().val(),
            details:getDetailLesson().val()
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                //reload the tree
                isEditedContent = true;
                UpdateStateCourse();
                reloadTree();
                currentPopup.modal('hide');
                swal("", "You have added lesson successfully!", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").html(data);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("Could not connect to server!");
            currentPopup.find(".validateMsg").show();
        }
    });
}


function editLesson(){
    $.ajax({
        url : servletEdit,
        type : "POST",
        data : {
            action: action_edit_lesson,
            idObj : getNameLesson().attr("objID"),
            idLesson : currentPopup.find(".idHidden").val(),
            name: getNameLesson().val(),
            description:getDescriptionLesson().val(),
            type:getTypeLesson().val(),
            details:getDetailLesson().val()
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                //reload the tree
                isEditedContent = true;
                UpdateStateCourse();
                reloadTree();
                currentPopup.modal('hide');
                swal("", "You have updated lesson successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").html(data);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("Could not connect to server");
            currentPopup.find(".validateMsg").show();
        }
    });
}
function deleteLesson(){
    $.ajax({
        url : servletDelete,
        type : "POST",
        data : {
            action: action_delete_lesson,
            idObj : getNameLesson().attr("objID"),
            idLesson : currentPopup.find(".idHidden").val()
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                //reload the tree
                isEditedContent = true;
                UpdateStateCourse();
                reloadTree();
                confirmDeletePopup().modal('hide');
                currentPopup.modal('hide');
                swal("", "You have deleted lesson successfully", "success");
            }else{
                //add false show the error
                confirmDeletePopup().modal('hide');
                currentPopup.find(".validateMsg").html(data);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            confirmDeletePopup().modal('hide');
            currentPopup.find(".validateMsg").html("Could not connect to server");
            currentPopup.find(".validateMsg").show();
        }
    });
}

function addQuestions(listWord){
    myObject.listWord = listWord;
    $.ajax({
        url : servletAdd,
        type : "POST",
        data : {
            action: action_add_question,
            listWord: JSON.stringify(myObject),
            idLesson: $("#idLesson").val()
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                //reload the tree
                isEditedContent = true;
                UpdateStateCourse();
                reloadTree();
                currentPopup.modal('hide');
                swal("Success!", "You have add question successfully!", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").html(data);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("Could not connect to server!");
            currentPopup.find(".validateMsg").show();
        }
    });
}

function editQuestions(listWord){
    myObject.listWord = listWord;
    $.ajax({
        url : servletEdit,
        type : "POST",
        data : {
            action: action_edit_question,
            listWord: JSON.stringify(myObject),
            idQuestion: currentPopup.find(".idHidden").val()
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                isEditedContent = true;
                UpdateStateCourse();
                reloadTree();
                currentPopup.modal('hide');
                swal("Success!", "You have edit question successfully!", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").html(data);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("Could not connect to server!");
            currentPopup.find(".validateMsg").show();
        }
    });
}


function deleteQuestion(){
    $.ajax({
        url : servletDelete,
        type : "POST",
        data : {
            action: action_delete_question,
            idLesson : getQuestionListWordEdit().attr("idLesson"),
            idQuestion: currentPopup.find(".idHidden").val()
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                isEditedContent = true;
                UpdateStateCourse();
                //reload the tree
                reloadTree();
                confirmDeletePopup().modal('hide');
                currentPopup.modal('hide');
                swal("", "You have deleted question successfully", "success");
            }else{
                //add false show the error
                confirmDeletePopup().modal('hide');
                currentPopup.find(".validateMsg").html(data);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            confirmDeletePopup().modal('hide');
            currentPopup.find(".validateMsg").html("Could not connect to server");
            currentPopup.find(".validateMsg").show();
        }
    });
}



function addQuestionsForTest(listWord){
    myObject.listWord = listWord;
    $.ajax({
        url : servletAdd,
        type : "POST",
        data : {
            action: action_add_question_test,
            listWord: JSON.stringify(myObject),
            idLesson: $("#idLesson").val(),
            type:$("#testType").val(),
            description:$("#explanation").val()

        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                //reload the tree
                isEditedContent = true;
                UpdateStateCourse();
                reloadTree();
                currentPopup.modal('hide');
                swal("Success!", "You have add question for test successfully!", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").html(data);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("Could not connect to server!");
            currentPopup.find(".validateMsg").show();
        }
    });
}

function editQuestionsForTest(listWord){
    myObject.listWord = listWord;
    $.ajax({
        url : servletEdit,
        type : "POST",
        data : {
            action: action_edit_question,
            listWord: JSON.stringify(myObject),
            idQuestion: currentPopup.find(".idHidden").val(),
            type:$("#testType").val(),
            description:$("#explanation").val()
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                isEditedContent = true;
                UpdateStateCourse();
                reloadTree();
                currentPopup.modal('hide');
                swal("Success!", "You have edit question for test successfully!", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").html(data);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("Could not connect to server!");
            currentPopup.find(".validateMsg").show();
        }
    });
}


function deleteQuestionForTest(){
    $.ajax({
        url : servletDelete,
        type : "POST",
        data : {
            action: action_delete_question_test,
            idLesson : getExplanationTest().attr("idLesson"),
            idQuestion: currentPopup.find(".idHidden").val()
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                //reload the tree
                isEditedContent = true;
                UpdateStateCourse();
                reloadTree();
                confirmDeletePopup().modal('hide');
                currentPopup.modal('hide');
                swal("", "You have deleted question for test successfully", "success");
            }else{
                //add false show the error
                confirmDeletePopup().modal('hide');
                currentPopup.find(".validateMsg").html(data);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("Could not connect to server");
            currentPopup.find(".validateMsg").show();
        }
    });
}
function removeWords(word){

    $.ajax({
        url : servletDelete,
        type : "POST",
        data : {
            action: action_delete_word,
            word:word,
            idQuestion: currentPopup.find(".idHidden").val()
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                isEditedContent = true;
                if(currentPopup.find(".action").val() == action_edit_question){
                    getListWord().find("div:contains('"+word+"')").remove();
                }else if(currentPopup.find(".action").val() == action_edit_question_test){
                    getListWordForTest().find("div:contains('"+word+"')").remove();
                }
            }else{
                currentPopup.find(".validateMsg").html(data);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("Could not connect to server");
            currentPopup.find(".validateMsg").show();
        }
    });

}


function loadWeightForWordEdit(word){
    $("#addWordModal").modal('show');
    getAddWord().val(word);
    $("#loadPhonemes").hide();
    $.ajax({
        url: "ManagementWordOfQuestionServlet",
        type: "POST",
        dataType: "json",
        data: {
            getPhonemeForWord: "getPhonemeForWord",
            word: word,
            idQuestion: currentPopup.find(".idHidden").val()
        },
        success: function (data) {
            var message = data.message;
            if(message.indexOf("success") != -1){
                drawWord(data);
            }else{
                swal("Error!",message.split(":")[1], "error");
            }
        },
        error: function () {
            swal("Error!", "Could not connect to server", "error");
        }

    });
}



function loadPhonemes(){
        $.ajax({
            url: "ManagementWordOfQuestionServlet",
            type: "POST",
            dataType: "json",
            data: {
                listPhonemes: "listPhonemes",
                word: getAddWord().val()
            },
            success: function (data) {
                var message = data.message;
                if(message.indexOf("success") != -1){
                    getAddWord().attr("idWord", data.id);
                    getAddWord().attr("nameWord", data.word);
                    //$("#loadPhonemes").attr("disabled",true);
                    getPhonemeLable().html("Arpabet:");
                    getWeightLable().html("Weight:");
                    getIPAlable().html("Ipa:");
                    getListPhonemes().html("");
                    getListWeight().html("");
                    getListIPA().html("");
                    //$("#addWord").attr("readonly","readonly");
                    getAddWord().attr("disabled",true);
                    drawPhonemeOfWord(data);
                    $("#yesadd").attr("disabled", false);
                    $("#wordModal1").show();
                    $("#wordModal2").show();
                }else{
                    getLoadPhoneme().attr("disabled",false);
                    getListPhonemes().html("");
                    getListWeight().html("");
                    getPhonemeLable().html("");
                    getWeightLable().html("");
                    $("#yesadd").attr("disabled", true);
                    getAddWord().focus();
                    getWordValidateMessage().html("<i>Sorry this word is not in our dictionary, please try a different word</i>");
                }
            },
            error: function () {
                swal("", "Could not connect to server", "error");
            }

        });
}


function enablePublishBtn(){
    $.ajax({
        url : servletPublish,
        type : "POST",
        data : {
            action: "checkButton",
            idCourse : idCourse
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                getPublishBtn().removeAttr("disabled");
            }else{
                getPublishBtn().attr("disabled","disabled");
            }
        },
        error: function () {

        }
    });
}
/**
 *
 */
function UpdateStateCourse(){
    if(typeof isEditedContent == "undefined"){
        return false;
    }
    if(typeof isEditedTitle == "undefined"){
        return false;
    }
    if(isEditedContent){
        state = "edited";
    }else{
        state = "duplicated";
    }
    $.ajax({
        url : servletPublish,
        type : "POST",
        data : {
            action: "updateState",
            idCourse : idCourse,
            state : state
        },
        dataType : "text",
        success : function(data){

        },
        error: function () {
            swal("","Could not connect to server","error");
        }
    });
}
/**
 *
 */
function publishCourse(){
    $.ajax({
        url : servletPublish,
        type : "POST",
        data : {
            action: "publish",
            idCourse : idCourse
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                window.location.href = "/my-courses.jsp";
            }else{
                swal("","Could not connect to server","error");
            }
        },
        error: function () {
            swal("","Could not connect to server","error");
        }
    });
}

/**
 *
 * @param action
 * @param parentId
 * @param childId
 * @param index
 * @constructor
 */
function DragDrop(action,parentId,childId,index,move){
    $.ajax({
        url : servletDrapDrop,
        type : "POST",
        data : {
            action: action,
            parentId : parentId,
            childId : childId,
            index : index,
            move : move
        },
        dataType : "text",
        success : function(data){

        },
        error: function () {

        }
    });
}