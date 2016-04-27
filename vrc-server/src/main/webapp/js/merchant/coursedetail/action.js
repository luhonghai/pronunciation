/**
 * Created by lantb on 2016-02-22.
 */
var servletAdd = "/TreeAddNodeServlet";
var currentPopup;
var listWord=[];
var myObject = new Object();
function openPopup(itemData){
    currentPopup = $('#'+ itemData._popupId);
    currentPopup.find(".validateMsg").hide();
    currentPopup.find(".action").val(itemData._actionClick);
    currentPopup.find(".idHidden").val(itemData.id);
    if (itemData._actionClick == action_edit_course){
        currentPopup.find("#titlePopupCourse").html("course management");
        getCourseName().val(itemData.label);
        getCourseDescription().val(itemData._title);
        currentPopup.find("#btnDeleteLevel").show();
        currentPopup.find("#arrowCourse").html(nameOfCourse);
    }else
    if (itemData._actionClick == action_add_level){
        clearForm();
        currentPopup.find("#arrow").html(nameOfCourse);
        currentPopup.find("#titlePopup").html("add level");
        currentPopup.find("#btnDeleteLevel").hide();
    }else if(itemData._actionClick == action_edit_level){
        currentPopup.find("#titlePopup").html("level management");
        getLevelName().val(itemData.label);
        getLevelDescription().val(itemData._title);
        currentPopup.find("#btnDeleteLevel").show();
        currentPopup.find("#arrow").html(nameOfCourse + " > " + itemData.label);
    }else if(itemData._actionClick == action_add_obj){
        clearForm();
        var level = treeAPI.itemData(currentParent);
        getObjName().attr("idLevel", level.id);
        currentPopup.find("#titlePopupObj").html("add objective");
        currentPopup.find("#btnDeleteObj").hide();
        currentPopup.find("#arrowObj").html(nameOfCourse+" > " + level.label);
    }else if(itemData._actionClick == action_edit_obj){
        currentPopup.find("#titlePopupObj").html("objective management");
        getObjName().val(itemData.label);
        getObjDescription().val(itemData._title);
        var level = treeAPI.itemData(currentParent);
        getObjName().attr("idLevel", level.id);
        currentPopup.find("#btnDeleteObj").show();
        currentPopup.find("#arrowObj").html(nameOfCourse+" > " + level.label + " > " + itemData.label);
    }else if(itemData._actionClick == action_add_test){
        clearForm();
        var level = treeAPI.itemData(currentParent);
        getPercentPass().attr("idLevel",level.id);
        currentPopup.find("#titlePopupTest").html("add test");
        currentPopup.find("#btnDeleteTest").hide();
        currentPopup.find("#arrowTest").html(nameOfCourse + " > " + level.label);
    }else if(itemData._actionClick == action_edit_test){
        currentPopup.find("#titlePopupTest").html("test management");
        var percent = itemData._title.split("%")[0];
        getPercentPass().val(percent);
        var level = treeAPI.itemData(currentParent);
        getPercentPass().attr("idLevel",level.id);
        currentPopup.find("#btnDeleteTest").show();
        currentPopup.find("#arrowTest").html(nameOfCourse+">" + level.label + ">" + itemData.label);
    }else if(itemData._actionClick == action_add_lesson){
        clearForm();
        currentPopup.find("#titlePopupLesson").html("add lesson");
        currentPopup.find("#btnDeleteLesson").hide();
        var objParent = treeAPI.itemData(currentParent);
        getNameLesson().attr("objID",objParent.id);
        var level = treeAPI.parent(currentParent);
        var levelItemData = treeAPI.itemData(level);
        currentPopup.find("#arrowLesson").html(nameOfCourse + " > " + levelItemData.label +" > "+ objParent.label);
    }else if(itemData._actionClick == action_edit_lesson){
        clearForm();
        currentPopup.find("#titlePopupLesson").html("lesson management");
        getNameLesson().val(itemData.label);
        getDescriptionLesson().val(itemData._description);
        getTypeLesson().val(itemData._type);
        getDetailLesson().val(itemData._details);
        currentPopup.find("#btnDeleteLesson").show();
        var objParent = treeAPI.itemData(currentParent);
        getNameLesson().attr("objID",objParent.id);
        var level = treeAPI.parent(currentParent);
        var levelItemData = treeAPI.itemData(level);
        currentPopup.find("#arrowLesson").html(nameOfCourse + " > " + levelItemData.label +" > "+ objParent.label);
    }else if(itemData._actionClick == action_add_question){
        clearForm();
        listWord=[];
        getListWord().empty();
        currentPopup.find("#titlePopupQuestion").html("question management");
        currentPopup.find("#btnDeleteQuestion").hide();
        var lesson = treeAPI.itemData(currentParent);
        var obj = treeAPI.parent(currentParent);
        var objItemData = treeAPI.itemData(obj);
        var level = treeAPI.parent(obj);
        var levelItemData = treeAPI.itemData(level);
        currentPopup.find("#arrowQuestion").html(nameOfCourse + " > " + levelItemData.label +" > " + objItemData.label + " > "+ lesson.label);
    }else if(itemData._actionClick == action_edit_question){
        clearForm();
        listWord=[];
        currentPopup.find("#titlePopupQuestion").html("question management");
        var lesson = treeAPI.itemData(currentParent);
        var obj = treeAPI.parent(currentParent);
        var objItemData = treeAPI.itemData(obj);
        var level = treeAPI.parent(obj);
        var levelItemData = treeAPI.itemData(level);
        currentPopup.find("#btnDeleteQuestion").show();
        getQuestionListWordEdit().attr("idLesson",lesson.id);
        drawListWord(itemData._title);
        currentPopup.find("#arrowQuestion").html(nameOfCourse + " > " + levelItemData.label +" > " + objItemData.label + " > "+ lesson.label);
    }else if(itemData._actionClick == action_add_question_test){
        listWord=[];
        clearForm();
        getListWordTest().empty();
        currentPopup.find("#titlePopupTestWord").html("add test question");
        currentPopup.find("#btnDeleteTestWord").hide();
        var level = treeAPI.itemData(currentParent);
        var row= nameOfCourse +" > " + level.label;
        getExplanationTest().attr("row",row);
        currentPopup.find("#arrowQuestionTest").html(nameOfCourse + " > " + level.label);
    }else if(itemData._actionClick == action_edit_question_test){
        listWord=[];
        clearForm();
        var lesson = treeAPI.itemData(currentParent);
        getListWordTest().empty();
        drawListWord(itemData._title);
        getTypeTest().val(itemData._type);
        getExplanationTest().val(itemData._description)
        currentPopup.find("#btnDeleteTestWord").show();
        currentPopup.find("#titlePopupTestWord").html("test question management");
        getExplanationTest().attr("idLesson",lesson._idLessonForTest);
        var test = treeAPI.itemData(currentParent);
        var level = treeAPI.parent(currentParent);
        var levelItem = treeAPI.itemData(level);
        var row= nameOfCourse +" > "+levelItem.label+ " > " + test.label;
        getExplanationTest().attr("row",row);
        currentPopup.find("#arrowQuestionTest").html(nameOfCourse + " > " + levelItem.label);
    }

    currentPopup.modal('show');
}

function drawListWord(listWord){
    var list=readListMail(listWord);
    if(list!=null && list.length>0){
        for(var i=0;i<list.length;i++){
            if(currentPopup.find(".action").val() == action_edit_question){
                getListWord().append(' <div style="margin-top: 5px;" ><p id="word" style="display: inline;background-color: rgb(85, 142, 213);color: white; border-radius: 3px; padding: 2px 10px; vertical-align: middle;">'+list[i]+'</p><i class="fa fa-minus-circle fa-2x" style="color: red;padding-left: 10px;vertical-align: middle;" title="remove word"  id="idWord" ></i></div>');
            }else if(currentPopup.find(".action").val() == action_edit_question_test){
                getListWordTest().append(' <div style="margin-top: 5px;" ><p id="word" style="display: inline;background-color: rgb(85, 142, 213);color: white; border-radius: 3px; padding: 2px 10px; vertical-align: middle;">'+list[i]+'</p><i class="fa fa-minus-circle fa-2x" style="color: red;padding-left: 10px;vertical-align: middle;" title="remove word"  id="idWord" ></i></div>');
            }
            loadWeightForWordEdit(list[i]);
        }


    }

}


function readListMail(txt) {
    if (txt == null || typeof txt == 'undefined' || txt.length == 0) return null;
    var data =  txt.split(',');
    var output = [];
    for (var i = 0; i < data.length; i++) {
        output.push(data[i]);
    }
    return output;
}



function btnSaveCourse(){
    $(document).on("click","#btnSaveCourse",function(){
        if(validateFormCourse()){
            editCourse();
        }
    });
}

function btnDeleteCourse(){
    $(document).on("click","#btnDeleteCourse",function(){
        confirmDeletePopup().modal('show');
        btnConfirmDelete().attr('action',"delete-course");
        var courseName = getCourseName().val();
        getLabelDeleted().html(courseName);
    });
}

/**
 * click on save Level button
 */
function btnSaveLevel(){
    $(document).on("click","#btnSaveLevel",function(){
        if(validateFormLevel()){
            if(currentPopup.find(".action").val() == action_add_level){
                addLevel();
            }else if(currentPopup.find(".action").val() == action_edit_level){
                editLevel();
            }
        }
    });
}

/**
 * click on delete button
 */
function btnDeleteLevel(){
    $(document).on("click","#btnDeleteLevel",function(){
        confirmDeletePopup().modal('show');
        btnConfirmDelete().attr('action',"delete-level");
        var levelName = getLevelName().val();
        getLabelDeleted().html(levelName);
    });
}

/**
 *click on save button
 */
function btnSaveObj(){
    $(document).on("click","#btnSaveObj",function(){
        if(validateFormObj()){
            if(currentPopup.find(".action").val() == action_add_obj){
                    addObj();
            }else if(currentPopup.find(".action").val() == action_edit_obj){
                    editObj();
            }
        }
    });
}

function btnDeleteObj(){
    $(document).on("click","#btnDeleteObj",function(){
        confirmDeletePopup().modal('show');
        btnConfirmDelete().attr('action',"delete-obj");
        var objName = getObjName().val();
        getLabelDeleted().html(objName);
    });
}


/**
 *click on save button
 */
function btnSaveTest(){
    $(document).on("click","#btnSaveTest",function(){
        if(validateFormTest()){
            if(currentPopup.find(".action").val() == action_add_test){
                addTest();
            }else if(currentPopup.find(".action").val() == action_edit_test){
                editTest();
            }
        }
    });
}

function btnDeleteTest(){
    $(document).on("click","#btnDeleteTest",function(){
        confirmDeletePopup().modal('show');
        btnConfirmDelete().attr('action',"delete-test");
        getLabelDeleted().html("test");
    });
}


/**
 * click on save lesson button
 */
function btnSaveLesson(){
    $(document).on("click","#btnSaveLesson",function(){
        if(validateFormLesson()){
            if(currentPopup.find(".action").val() == action_add_lesson){
                addLesson();
            }else if(currentPopup.find(".action").val() == action_edit_lesson){
                editLesson();
            }
        }
    });
}


function btnDeleteLesson(){
    $(document).on("click","#btnDeleteLesson",function(){
        confirmDeletePopup().modal('show');
        btnConfirmDelete().attr('action',"delete-lesson");
        var lessonName = getNameLesson().val();
        getLabelDeleted().html(lessonName);
    });
}



function showHelpIconTop(){
    $("#help-icons").show();
}

function showAddWord(){
    $(document).on("click","#btnAddWord",function() {
        getWordValidateMessage().hide();
        var idLesson= treeAPI.itemData(currentParent).id;
        $("#AddOrEditWord").val("add");
        $("#wordModal1").hide();
        $("#wordModal2").hide();
        $("#idLesson").val(idLesson);
        var row= $("#arrowQuestion").text() + " > question";
        $("#arrowWord").text(row);
        getAddWord().val("");
        getListPhonemes().html("");
        getListWeight().html("");
        getListIPA().html("");
        getPhonemeLable().html("");
        getWeightLable().html("");
        getIPAlable().html("");
        $("#yesadd").attr("disabled", true);
        $("#loadPhonemes").show();
        getAddWord().removeAttr('readonly');
        $("#loadPhonemes").attr("disabled",false);
        $("#addWord").attr("disabled",false);
        $("#addWordModal").attr("type","add-new-word");
        $("#addWordModal").modal('show');
        $("#addWordModal").find('#title-add-word').html("word management");

    });
}

function showAddWordForTest(){
    $(document).on("click","#btnAddWordTest",function() {
        $("#validateWordMsg").hide();
        $("#arrowWord").html("Test > question" );
        var idLesson= treeAPI.itemData(currentParent)._idLessonForTest;
        $("#AddOrEditWord").val("addWordTest");
        $("#idLesson").val(idLesson);
        $("#wordModal1").hide();
        $("#wordModal2").hide();
        $("#addWordModal").modal('show');
        getAddWord().val("");
        getListPhonemes().html("");
        getListWeight().html("");
        getListIPA().html("");
        getPhonemeLable().html("");
        getWeightLable().html("");
        getIPAlable().html("");
        $("#yesadd").attr("disabled", true);
        $("#loadPhonemes").show();
        getAddWord().removeAttr('readonly');
        $("#loadPhonemes").attr("disabled",false);
        $("#addWord").attr("disabled",false);
        $("#addWordModal").find('#title-add-word').html("add test word");

    });
}
function loadPhoneme() {
    $(document).on("click","#loadPhonemes",function() {
        getLoadPhoneme().attr("disabled", true);
        if(validateWord()){
            var word = getAddWord().val().toLowerCase();
            getAddWord().val(word);
            loadPhonemes();
        }
    })
}
function btnPublish(){
    $(document).on("click","#publish",function() {
        publishCourse();
    });

}

function appendWord(type){
    if(type=="add"){
        if(listWord !=null && listWord.length>0){
            getListWord().html("");
            $.each(listWord, function(i){
                getListWord().append(' <div style="margin-top: 5px;">' +
                    '<p id="word" style="display: inline;background-color: rgb(85, 142, 213);' +
                    'color: white; border-radius: 3px; padding: 5px 10px; vertical-align: middle;">'
                    + listWord[i].nameWord + '</p>' +
                    '<i class="fa fa-minus-circle fa-2x" style="color: red;padding-left: 10px;' +
                    'vertical-align: middle;" ' +
                    'title="remove word"  id="idWord" ></i> </div>');
            });
        }
    }else if(type=="addWordTest") {
        if(listWord !=null && listWord.length>0){
            getListWordForTest().html("");
            $.each(listWord, function (i) {
                getListWordForTest().append(' <div style="margin-top: 5px;"><p id="word" style="display: inline;background-color: rgb(85, 142, 213);color: white; border-radius: 3px; padding: 5px 10px; vertical-align: middle;">' + listWord[i].nameWord + '</p><i class="fa fa-minus-circle fa-2x" style="color: red;padding-left: 10px;vertical-align: middle;" title="remove word"  id="idWord" ></i> </div>');
            });

        }
    }
}

function saveWord(){
    $(document).on("click","#btnSaveWord",function() {
        var addOrEdit=$("#AddOrEditWord").val();
        var nameWord=$("#addWord").val();
        var idWord=$("#addWord").attr("idWord");
        if(validateWord()) {
            var listPhonemeName = getListPhonemes();
            var output = [];
            $(listPhonemeName).find('input').each(function (e) {
                var value = $(this).val();
                var ipa = $(this).attr("ipa");
                var index = $(this).attr("index");
                var weight = $("#weight" + index).val();
                output.push({
                    index: parseInt(index),
                    phoneme: value,
                    ipa: ipa,
                    weight: parseFloat(weight)
                });
            });
            pushToList(idWord,nameWord,output);
            appendWord(addOrEdit);
            $("#addWordModal").modal('hide');
        }
    });

}

function pushToList(idWord,word,listWeight){
    if(listWord !=null && listWord.length>0){
        var existed = false;
        var index_update = 0;
        $.each(listWord, function(i){
            if(listWord[i].nameWord === word) {
                existed = true;
                index_update = i;
            }
        });
        if(existed){
            listWord[index_update].listWeightPhoneme = listWeight;
        }else{
            listWord.push({
                idWord: idWord,
                nameWord: word,
                listWeightPhoneme: listWeight
            });
        }
    }else{
        listWord.push({
            idWord: idWord,
            nameWord: word,
            listWeightPhoneme: listWeight
        });
    }
}
function removeWord(){
    $(document).on("click","#idWord",function() {
        if(currentPopup.find(".action").val() == action_add_question) {
            var word= $(this).closest("div").find('p').text();
            if(listWord !=null && listWord.length>0){
                $.each(listWord, function(i){
                    if(listWord[i].nameWord === word) {
                        listWord.splice(i,1);
                        console.log(listWord);
                        return false;
                    }
                });
            }

           $(this).closest("div").remove();
        }else if(currentPopup.find(".action").val() == action_add_question_test) {
            var word= $(this).closest("div").find('p').text();
            if(listWord !=null && listWord.length>0){
                $.each(listWord, function(i){
                    if(listWord[i].nameWord === word) {
                        listWord.splice(i,1);
                        console.log(listWord);
                        return false;
                    }
                });
            }
            $(this).closest("div").remove();
        }else if(currentPopup.find(".action").val() == action_edit_question_test) {
            var word= $(this).closest("div").find('p').text();
            if(listWord !=null && listWord.length>0){
                $.each(listWord, function(i){
                    if(listWord[i].nameWord === word) {
                        listWord.splice(i,1);
                        return false;
                    }
                });
                removeWords(word);
                $(this).closest("div").remove();
            }
        }else if(currentPopup.find(".action").val() == action_edit_question) {
            var word= $(this).closest("div").find('p').text();
            if(listWord !=null && listWord.length>0){
                $.each(listWord, function(i){
                    if(listWord[i].nameWord === word) {
                        listWord.splice(i,1);
                        return false;
                    }
                });
                removeWords(word);
                $(this).closest("div").remove();
            }
        }
    });
}

function openEditWords(){
    $(document).on("click","#word",function() {
        getListPhonemes().html("");
        getListWeight().html("");
        getListIPA().html("");
        $("#addWordModal").attr('type','edit-word');
        if(currentPopup.find(".action").val() == action_add_question) {
            var word= $(this).closest("div").find('p').text();
            if(listWord !=null && listWord.length>0){
                $.each(listWord, function(i){
                    if(listWord[i].nameWord === word) {
                       var data=listWord[i];
                        $("#addWordModal").modal('show');
                        drawWord(data);
                    }
                });
            }
        }else if(currentPopup.find(".action").val() == action_add_question_test) {
            var word= $(this).closest("div").find('p').text();
            if(listWord !=null && listWord.length>0){
                $.each(listWord, function(i){
                    if(listWord[i].nameWord === word) {
                        var data=listWord[i];
                        $("#addWordModal").modal('show');
                        drawWord(data);
                    }
                });
            }
        }else if(currentPopup.find(".action").val() == action_edit_question_test) {
            var word= $(this).closest("div").find('p').text();
            if(listWord !=null && listWord.length>0){
                $.each(listWord, function(i){
                    if(listWord[i].nameWord === word) {
                        var data=listWord[i];
                        $("#addWordModal").modal('show');
                        $("#addWordModal").find('#title-add-word').html("test word management");
                        drawWord(data);
                    }
                });
            }else {
                /*loadWeightForWordEdit(word);*/
            }
        } else if(currentPopup.find(".action").val() == action_edit_question) {
            var word= $(this).closest("div").find('p').text();
            if(listWord !=null && listWord.length>0){
                $.each(listWord, function(i){
                    if(listWord[i].nameWord === word) {
                        var data=listWord[i];
                        $("#addWordModal").modal('show');
                        $("#addWordModal").find('#title-add-word').html("word management");
                        drawWord(data);
                    }
                });
            }else {
                /*loadWeightForWordEdit(word);*/
            }
        }
    });
}

function btnSaveQuestion(){
    $(document).on("click","#btnSaveQuestion",function() {
        if(currentPopup.find(".action").val() == action_add_question) {
            if(validateSaveQuestion(listWord)){
                addQuestions(listWord);
            }

        }else if(currentPopup.find(".action").val() == action_edit_question) {
            editQuestions(listWord);
        }
    });
}

function btnSaveQuestionForTest(){
    $(document).on("click","#btnSaveTestWord",function() {
        if(currentPopup.find(".action").val() == action_add_question_test) {
            if(validateSaveQuestionForTest(listWord)){
                addQuestionsForTest(listWord);
            }

        }else if(currentPopup.find(".action").val() == action_edit_question_test) {
            editQuestionsForTest(listWord);
        }
    });
}
function btnDeleteQuestion(){
    $(document).on("click","#btnDeleteQuestion",function(){
        confirmDeletePopup().modal('show');
        btnConfirmDelete().attr('action',"delete-question");
        getLabelDeleted().html("question");
    });
}
function btnDeleteQuestionForTest(){
    $(document).on("click","#btnDeleteTestWord",function(){
        confirmDeletePopup().modal('show');
        btnConfirmDelete().attr('action',"delete-question-test");
        getLabelDeleted().html("question");
    });
}

function clickTopHelp(){
    $(document).on("click","#help-icons", function(){
        getPopUpHelp().find(".modal-title").html("course administration");
        getPopUpHelp().find(".modal-body").empty();
        getPopUpHelp().find(".modal-body").html(initHelpCourseADM());
        getPopUpHelp().modal('show');
    });
}
function closePopupQuestion(){
    $('#popupQuestion').on('hidden.bs.modal', function () {
        $("#listWord").empty();
    });
}


function clickHelpAdd(){
    $(document).on("click",".helpInfor", function(){
        var id = $(this).attr("id");
        if(id == "helpAddCourse"){
            getPopUpHelp().find(".modal-title").html("course management");
            getPopUpHelp().find(".modal-body").empty();
            getPopUpHelp().find(".modal-body").html(initHelpAddCourse());
        }else
        if(id == "helpAddLevel"){
            getPopUpHelp().find(".modal-title").html("level management");
            getPopUpHelp().find(".modal-body").empty();
            getPopUpHelp().find(".modal-body").html(initHelpAddLevel());
        }else if(id == "helpAddObj"){
            getPopUpHelp().find(".modal-title").html("objective management");
            getPopUpHelp().find(".modal-body").empty();
            getPopUpHelp().find(".modal-body").html(initHelpAddObjective());
        }else if(id == "helpAddTest"){
            getPopUpHelp().find(".modal-title").html("test management");
            getPopUpHelp().find(".modal-body").empty();
            getPopUpHelp().find(".modal-body").html(initHelpAddTest());
        }else if(id == "helpAddLesson"){
            getPopUpHelp().find(".modal-title").html("lesson management");
            getPopUpHelp().find(".modal-body").empty();
            getPopUpHelp().find(".modal-body").html(initHelpAddLesson());
        }else if(id == "helpAddQuestion"){
            getPopUpHelp().find(".modal-title").html("question management");
            getPopUpHelp().find(".modal-body").empty();
            getPopUpHelp().find(".modal-body").html(initHelpAddQuestion());
        }else if(id=="helpDeleteBtn"){
            getPopUpHelp().find(".modal-title").html("deleting files");
            getPopUpHelp().find(".modal-body").empty();
            getPopUpHelp().find(".modal-body").html(initHelpDelete());
            getPopUpHelp().css("z-index","10000");
        }else if(id=="helpAddWord"){
            var id = currentPopup.attr('id');
            if(id == 'popupQuestion'){
                getPopUpHelp().find(".modal-title").html("word management");
            }else{
                getPopUpHelp().find(".modal-title").html("test word management");
            }

            getPopUpHelp().find(".modal-body").empty();
            getPopUpHelp().find(".modal-body").html(initHelpAddWord());
        }
        getPopUpHelp().modal('show');
    });
}

function confirmDelete(){
    $(document).on("click","#ConfirmDeletebtn",function(){
        var action = $(this).attr('action');
        if(action == "delete-course"){
            deleteCourse();
        }else if(action == "delete-level"){
            deleteLevel();
        }else if(action == "delete-obj"){
            deleteObj();
        }else if(action == "delete-test"){
            deleteTest();
        }else if(action == "delete-lesson"){
            deleteLesson();
        }else if(action == "delete-question"){
            deleteQuestion();
        }else if(action == "delete-question-test"){
            deleteQuestionForTest();
        }
    });

}
function collapseMenu(){
    $("#li-courses").find('ul').addClass('in');
}

$(document).ready(function(){
    collapseMenu();
    previewCourse();
    btnDeleteQuestionForTest();
    btnSaveQuestionForTest();
    showAddWordForTest();
    openEditWords();
    removeWord();
    btnDeleteQuestion();
    closePopupQuestion();
    btnSaveQuestion();
    saveWord();
    clickTopHelp();
    clickHelpAdd();
    loadPhoneme();
    showAddWord();
    btnSaveCourse();
    btnDeleteCourse();
    btnSaveLesson();
    btnDeleteLesson();
    btnSaveLevel();
    btnDeleteLevel();
    btnSaveObj();
    btnDeleteObj();
    btnSaveTest();
    btnDeleteTest();
    showHelpIconTop();
    enablePublishBtn();
    btnPublish();
    confirmDelete();
});