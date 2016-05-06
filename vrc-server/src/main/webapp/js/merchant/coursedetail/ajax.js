/**
 * Created by lantb on 2016-02-24.
 */
var servletAdd = "/TreeAddNodeServlet";
var servletEdit = "/TreeEditNodeServlet";
var servletDelete = "/TreeDeleteNodeServlet";
var servletPublish = "/PublishCourseServlet";
var servletCopy = "/CopyServlet";
var servletDrapDrop = "/DragDropServlet"
var progress;
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
                currentParent = null;
                firstLoad = true;
                reloadTree();
                currentPopup.modal('hide');
                changeHeaderCourseName(getCourseName().val());
                $("#listWord").empty();
                swalNew("", "updated successfully", "success");
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
                currentPopup.modal('hide');
                confirmDeletePopup().modal('hide');
                swalNew("", "deleted successfully", "success");
                window.history.back();
            }else{
                //add false show the error
                confirmDeletePopup().modal('hide');
                currentPopup.find(".validateMsg").html(data);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            confirmDeletePopup().modal('hide');
            currentPopup.find(".validateMsg").html("could not connect to server!");
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
                var id = data.split(":")[1];
                reloadTree(id,"add");
                currentPopup.modal('hide');
                swalNew("", "added successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").html(data.split(":")[1]);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("could not connect to server");
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
                reloadTree();
                currentPopup.modal('hide');
                swalNew("", "updated successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").html(data.split(":")[1]);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("could not connect to server");
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
                reloadTree();
                confirmDeletePopup().modal('hide');
                currentPopup.modal('hide');
                swalNew("", "deleted successfully", "success");
            }else{
                //add false show the error
                confirmDeletePopup().modal('hide');
                currentPopup.find(".validateMsg").html(data.split(":")[1]);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            confirmDeletePopup().modal('hide');
            currentPopup.find(".validateMsg").html("could not connect to server");
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
                var id = data.split(":")[1];
                reloadTree(id,"add");
                currentPopup.modal('hide');
                swalNew("", "added successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").html(data.split(":")[1]);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("could not connect to server");
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
                reloadTree();
                currentPopup.modal('hide');
                swalNew("", "updated successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").html(data.split(":")[1]);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("could not connect to server");
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
                reloadTree();
                confirmDeletePopup().modal('hide');
                currentPopup.modal('hide');
                swalNew("", "deleted successfully", "success");
            }else{
                //add false show the error
                confirmDeletePopup().modal('hide');
                currentPopup.find(".validateMsg").html(data.split(":")[1]);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("could not connect to server");
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
                var id = data.split(":")[1];
                reloadTree(id,"add");
                currentPopup.modal('hide');
                swalNew("", "added successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").html(data.split(":")[1]);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("could not connect to server");
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
                reloadTree();
                currentPopup.modal('hide');
                swalNew("", "updated successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").html(data.split(":")[1]);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("could not connect to server");
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
                reloadTree();
                currentPopup.modal('hide');
                confirmDeletePopup().modal('hide');
                swalNew("", "deleted successfully", "success");
            }else{
                //add false show the error
                confirmDeletePopup().modal('hide');
                currentPopup.find(".validateMsg").html(data.split(":")[1]);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("could not connect to server");
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
                var id = data.split(":")[1];
                reloadTree(id,"add");
                currentPopup.modal('hide');
                swalNew("", "added successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").html(data.split(":")[1]);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("could not connect to server!");
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
                reloadTree();
                currentPopup.modal('hide');
                swalNew("", "updated successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").html(data.split(":")[1]);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("could not connect to server");
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
                reloadTree();
                confirmDeletePopup().modal('hide');
                currentPopup.modal('hide');
                swalNew("", "deleted successfully", "success");
            }else{
                //add false show the error
                confirmDeletePopup().modal('hide');
                currentPopup.find(".validateMsg").html(data.split(":")[1]);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("could not connect to server");
            currentPopup.find(".validateMsg").show();
        }
    });
}

function addQuestions(listWord){
    myObject.listWord = listWord;
    currentPopup.find(".validateMsg").html("Your question is being saved..");
    currentPopup.find(".validateMsg").css("color","#A6A6A6")
    currentPopup.find(".validateMsg").show();
    currentPopup.find("#btnSaveQuestion").attr("disabled","disabled");
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
                reloadTree();
                currentPopup.find(".validateMsg").css("color","red")
                currentPopup.find(".validateMsg").hide();
                currentPopup.modal('hide');
                swalNew("", "added successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").hide();
                currentPopup.find(".validateMsg").css("color","red")
                currentPopup.find(".validateMsg").html(data.split(":")[1]);
                currentPopup.find(".validateMsg").show();
            }
            currentPopup.find("#btnSaveQuestion").removeAttr("disabled");
        },
        error: function () {
            currentPopup.find(".validateMsg").hide();
            currentPopup.find(".validateMsg").css("color","red")
            currentPopup.find(".validateMsg").html("could not connect to server!");
            currentPopup.find(".validateMsg").show();
            currentPopup.find("#btnSaveQuestion").removeAttr("disabled");
        }
    });
}

function editQuestions(listWord){
    myObject.listWord = listWord;
    currentPopup.find(".validateMsg").html("Your question is being edited..");
    currentPopup.find(".validateMsg").css("color","#A6A6A6")
    currentPopup.find(".validateMsg").show();
    currentPopup.find("#btnSaveQuestion").attr("disabled","disabled");
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
                reloadTree();
                currentPopup.find(".validateMsg").css("color","red");
                currentPopup.find(".validateMsg").hide();
                currentPopup.modal('hide');
                swalNew("", "updated successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").hide();
                currentPopup.find(".validateMsg").css("color","red")
                currentPopup.find(".validateMsg").html(data.split(":")[1]);
                currentPopup.find(".validateMsg").show();
            }
            currentPopup.find("#btnSaveQuestion").removeAttr("disabled");
        },
        error: function () {
            currentPopup.find(".validateMsg").hide();
            currentPopup.find(".validateMsg").css("color","red");
            currentPopup.find(".validateMsg").html("could not connect to server!");
            currentPopup.find(".validateMsg").show();
            currentPopup.find("#btnSaveQuestion").removeAttr("disabled");
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
                //reload the tree
                reloadTree();
                confirmDeletePopup().modal('hide');
                currentPopup.modal('hide');
                swalNew("", "deleted successfully", "success");
            }else{
                //add false show the error
                confirmDeletePopup().modal('hide');
                currentPopup.find(".validateMsg").html(data);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("could not connect to server");
            currentPopup.find(".validateMsg").show();
        }
    });
}



function addQuestionsForTest(listWord){
    myObject.listWord = listWord;
    currentPopup.find(".validateMsg").html("Your question is being saved..");
    currentPopup.find(".validateMsg").css("color","#A6A6A6")
    currentPopup.find(".validateMsg").show();
    currentPopup.find("#btnSaveTestWord").attr("disabled","disabled");
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
                reloadTree();
                currentPopup.find(".validateMsg").css("color","red")
                currentPopup.find(".validateMsg").hide();
                currentPopup.modal('hide');
                swalNew("Success!", "added successfully", "success");
            }else{
                currentPopup.find(".validateMsg").hide();
                currentPopup.find(".validateMsg").css("color","red")
                currentPopup.find(".validateMsg").html(data.split(":")[1]);
                currentPopup.find(".validateMsg").show();
            }
            currentPopup.find("#btnSaveTestWord").removeAttr("disabled");
        },
        error: function () {
            currentPopup.find(".validateMsg").hide();
            currentPopup.find(".validateMsg").css("color","red")
            currentPopup.find(".validateMsg").html("could not connect to server!");
            currentPopup.find(".validateMsg").show();
            currentPopup.find("#btnSaveTestWord").removeAttr("disabled");
        }
    });
}

function editQuestionsForTest(listWord){
    myObject.listWord = listWord;
    currentPopup.find(".validateMsg").html("Your question is being edited..");
    currentPopup.find(".validateMsg").css("color","#A6A6A6")
    currentPopup.find(".validateMsg").show();
    currentPopup.find("#btnSaveTestWord").attr("disabled","disabled");
    $.ajax({
        url : servletEdit,
        type : "POST",
        data : {
            action: action_edit_question_test,
            listWord: JSON.stringify(myObject),
            idQuestion: currentPopup.find(".idHidden").val(),
            type:$("#testType").val(),
            description:$("#explanation").val()
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                reloadTree();
                currentPopup.find(".validateMsg").css("color","red")
                currentPopup.find(".validateMsg").hide();
                currentPopup.modal('hide');
                swalNew("Success!", "updated successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").hide();
                currentPopup.find(".validateMsg").css("color","red")
                currentPopup.find(".validateMsg").html(data.split(":")[1]);
                currentPopup.find(".validateMsg").show();
            }
            currentPopup.find("#btnSaveTestWord").removeAttr("disabled");
        },
        error: function () {
            currentPopup.find(".validateMsg").hide();
            currentPopup.find(".validateMsg").css("color","red")
            currentPopup.find(".validateMsg").html("Could not connect to server");
            currentPopup.find(".validateMsg").show();
            currentPopup.find("#btnSaveTestWord").removeAttr("disabled");
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
                reloadTree();
                confirmDeletePopup().modal('hide');
                currentPopup.modal('hide');
                swalNew("", "deleted successfully", "success");
            }else{
                //add false show the error
                confirmDeletePopup().modal('hide');
                currentPopup.find(".validateMsg").html(data.split(":")[1]);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("could not connect to server");
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
                if(currentPopup.find(".action").val() == action_edit_question){
                    getListWord().find("div:contains('"+word+"')").remove();
                }else if(currentPopup.find(".action").val() == action_edit_question_test){
                    getListWordForTest().find("div:contains('"+word+"')").remove();
                }
            }else{
                currentPopup.find(".validateMsg").html(data.split(":")[1]);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").html("could not connect to server");
            currentPopup.find(".validateMsg").show();
        }
    });

}


function loadWeightForWordEdit(word){
    //$("#addWordModal").modal('show');
    //getAddWord().val(word);
    //$("#loadPhonemes").hide();
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
                //drawWord(data);
                var idWord = data.idWord;
                var output = [];
                $.each(data.listWeightPhoneme, function (idx, obj) {
                    var phonemeName = obj.phoneme;
                    var weightOfPhoneme = obj.weight;
                    var ipa = obj.ipa;
                    var index = obj.index;
                    output.push({
                        index: parseInt(index),
                        phoneme: phonemeName,
                        ipa: ipa,
                        weight: parseFloat(weightOfPhoneme)
                    });
                });
                listWord.push({
                    idWord: idWord,
                    nameWord: word,
                    listWeightPhoneme: output
                });
            }else{
               // swalNew("",message.split(":")[1], "error");
            }
        },
        error: function () {
            //swalNew("", "Could not connect to server", "error");
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
                    getWordValidateMessage().hide();
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
                    $("#addWordModal").find('#btnSaveWord').attr("disabled", false);
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
                    getWordValidateMessage().html("sorry this word is not in our dictionary, please try a different word");
                    getWordValidateMessage().show();
                }
            },
            error: function () {
                swalNew("", "could not connect to server", "error");
            }

        });
}

/**
 *
 */
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
                getPublishBtn().attr('src',"/images/treeview/publish_button.gif");
                getPublishBtn().css("opacity","inherit");
            }else{
                getPublishBtn().attr("disabled","disabled");
                getPublishBtn().attr('src',"/images/treeview/publish_button_disable.gif");
                getPublishBtn().css("opacity","inherit");
            }
        },
        error: function () {

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
                swalNew("","could not connect to server","error");
            }
        },
        error: function () {
            swalNew("","could not connect to server","error");
        }
    });
}
/**
 *
 */
function enableAddLevel(){
    $.ajax({
        url : servletPublish,
        type : "POST",
        data : {
            action: "checkButtonAddLv",
            idCourse : idCourse
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                $("#addlevel").find('.aciTreeItem').removeAttr('disabled');
            }else{
                $("#addlevel").find('.aciTreeItem').attr('disabled','disabled');
            }
        },
        error: function () {
            swalNew("","could not connect to server","error");
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
