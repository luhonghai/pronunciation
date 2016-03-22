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
    }else
    if (itemData._actionClick == action_add_level){
        clearForm();
        currentPopup.find("#titlePopup").html("add level");
        currentPopup.find("#btnDeleteLevel").hide();
    }else if(itemData._actionClick == action_edit_level){
        currentPopup.find("#titlePopup").html("level management");
        getLevelName().val(itemData.label);
        getLevelDescription().val(itemData._title);
        currentPopup.find("#btnDeleteLevel").show();
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
        currentPopup.find("#titlePopupLesson").html("edit lesson");
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
    }
    currentPopup.modal('show');
}

function drawListWord(listWord){
    var list=readListMail(listWord);
    if(list!=null && list.length>0){
        for(var i=0;i<list.length;i++){
            getListWord().append(' <div style="margin-top: 5px;" ><p id="word" style="display: inline;background-color: rgb(85, 142, 213);color: white; border-radius: 3px; padding: 2px 10px;">'+list[i]+'</p></div>');
        }

    }

}
function openEditWord(){
    $(document).on("click","#word",function(){
        var word=$(this).text();
        loadWeightForWordEdit(word);
    });
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
         deleteCourse();
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
        deleteLevel();
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
        deleteObj();
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
        deleteTest();
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
        deleteLesson();
    });
}



function showHelpIconTop(){
    $("#help-icons").show();
}

function showAddWord(){
    $(document).on("click","#btnAddWord",function() {
        var idLesson= treeAPI.itemData(currentParent).id;
        $("#wordModal1").hide();
        $("#wordModal2").hide();
        $("#idLesson").val(idLesson);
        var row=$("#arrowQuestion").text();
        $("#arrowWord").text(row);
        $("#addWordModal").modal('show');
        getAddWord().val("");
        getListPhonemes().html("");
        getListWeight().html("");
        getListIPA().html("");
        $(".phoneme-lable").html("");
        $(".weight-lable").html("");
        $(".ipa-lable").html("");
        $("#yesadd").attr("disabled", true);
        $("#loadPhonemes").show();
        getAddWord().removeAttr('readonly');
        $("#loadPhonemes").attr("disabled",false);
        $("#addWord").attr("disabled",false);
    });
}

function loadPhoneme() {
    $(document).on("click","#loadPhonemes",function() {
        getLoadPhoneme().attr("disabled", true);
        if(validateWord()){
            loadPhonemes();
        }
    })
}
function btnPublish(){
    $(document).on("click","#publish",function() {
        publishCourse();
    });

}
function saveWord(){
    $(document).on("click","#btnSaveWord",function() {
        var nameWord=$("#addWord").val();
        var idWord=$("#addWord").attr("idWord");
        if(currentPopup.find(".action").val() == action_add_question) {
            getListWord().append(' <div style="margin-top: 5px;"><p style="display: inline;background-color: rgb(85, 142, 213);color: white; border-radius: 3px; padding: 5px 10px;">' + nameWord + '</p><i class="fa fa-minus-circle fa-2x" style="color: red;padding-left: 10px;" title="remove word"  id=' + idWord + ' ></i> </div>');
        }
        var listPhonemeName=getListPhonemes();
        var output = [];

        $(listPhonemeName).find('input').each(function(e){
            var value = $(this).val();
            var index = $(this).attr("index");
            var weight = $("#weight" + index).val();
            output.push({
                index : parseInt(index),
                phoneme : value,
                weight : parseFloat(weight)
            });
        });
        $("#addWordModal").modal('hide');
        //output.clear();
        listWord.push({
            idWord:idWord,
            nameWord:nameWord,
            data:output
        });
        console.log(listWord);
    });

}

function removeWord(){
    $(document).on("click","#idWord",function() {
        if(currentPopup.find(".action").val() == action_add_question) {
           $(this).closest("div").remove();
        }else if(currentPopup.find(".action").val() == action_edit_question) {
            editQuestions(listWord);
        }
    });
}

function btnSaveQuestion(){
    $(document).on("click","#btnSaveQuestion",function() {
        if(currentPopup.find(".action").val() == action_add_question) {
            addQuestions(listWord);
        }else if(currentPopup.find(".action").val() == action_edit_question) {
            editQuestions(listWord);
        }
    });
}
function btnDeleteQuestion(){
    $(document).on("click","#btnDeleteQuestion",function(){
        deleteQuestion();
    });
}

function clickTopHelp(){
    $(document).on("click","#help-icons", function(){
        getPopUpHelp().find(".modal-title").html("course administration");
        getPopUpHelp().find(".modal-body").empty();
        getPopUpHelp().find(".modal-body").html("coming soon");
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
        }
        getPopUpHelp().modal('show');
    });
}




$(document).ready(function(){
    btnDeleteQuestion();
    openEditWord();
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
});