/**
 * Created by lantb on 2016-02-22.
 */


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
        if(isEditedContent){
            state = "edited";
        }else if(isEditedTitle && !isEditedContent){
            state = "duplicated";
        }
        publishCourse(true);
    });

}
function btnConfirmPublish(){
    $(document).on("click","#btn-confirm-pb",function() {
        publishCourse(false);
    });
}
function btnCancelPublish(){
    $(document).on("click","#cancel-publish",function() {
        $('#confirmPublish').modal('hide');
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
        }else if(id == "helpAddTestWord"){
            getPopUpHelp().find(".modal-title").html("test question management");
            getPopUpHelp().find(".modal-body").empty();
            getPopUpHelp().find(".modal-body").html(initHelpAddQuestion());
        }else if(id=="helpDeleteBtn"){
            getPopUpHelp().find(".modal-title").html("deleting files");
            getPopUpHelp().find(".modal-body").empty();
            getPopUpHelp().find(".modal-body").html(initHelpDelete());
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
    btnDeleteQuestionForTest();
    btnSaveQuestionForTest();
    btnDeleteQuestion();
    closePopupQuestion();
    btnSaveQuestion();
    clickTopHelp();
    clickHelpAdd();
    loadPhoneme();
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
    btnPublish();
    confirmDelete();
    btnConfirmPublish();
    btnCancelPublish();
});