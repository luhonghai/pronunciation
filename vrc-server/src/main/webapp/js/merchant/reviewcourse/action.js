/**
 * Created by lantb on 2016-02-22.
 */
var currentPopup;
function openPopup(itemData){
    currentPopup = $('#'+ itemData._popupId);
    currentPopup.find(".validateMsg").hide();
    currentPopup.find(".action").val(itemData._actionClick);
    currentPopup.find(".idHidden").val(itemData.id);
    if (itemData._actionClick == action_edit_course){
        clearForm();
        currentPopup.find("#titlePopupCourse").html("course management");
        getCourseName().val(itemData.label);
        getCourseDescription().val(itemData._title);
        currentPopup.find("#arrowCourse").html(nameOfCourse);
    }else
    if(itemData._actionClick == action_edit_level){
        clearForm();
        currentPopup.find("#titlePopup").html("level management");
        getLevelName().val(itemData.label);
        getLevelDescription().val(itemData._title);
        currentPopup.find("#arrow").html(nameOfCourse + " > " + itemData.label);
    }else if(itemData._actionClick == action_edit_obj){
        clearForm();
        currentPopup.find("#titlePopupObj").html("objective management");
        getObjName().val(itemData.label);
        getObjDescription().val(itemData._title);
        var level = treeAPI.itemData(currentParent);
        getObjName().attr("idLevel", level.id);
        currentPopup.find("#arrowObj").html(nameOfCourse+" > " + level.label + " > " + itemData.label);
    }else if(itemData._actionClick == action_edit_test){
        clearForm();
        currentPopup.find("#titlePopupTest").html("test management");
        var percent = itemData._title.split("%")[0];
        getPercentPass().val(percent);
        var level = treeAPI.itemData(currentParent);
        getPercentPass().attr("idLevel",level.id);
        currentPopup.find("#arrowTest").html(nameOfCourse+">" + level.label + ">" + itemData.label);
    }else if(itemData._actionClick == action_edit_lesson){
        clearForm();
        currentPopup.find("#titlePopupLesson").html("edit lesson");
        getNameLesson().val(itemData.label);
        getDescriptionLesson().val(itemData._description);
        getTypeLesson().val(itemData._type);
        getDetailLesson().val(itemData._details);
        var objParent = treeAPI.itemData(currentParent);
        getNameLesson().attr("objID",objParent.id);
        var level = treeAPI.parent(currentParent);
        var levelItemData = treeAPI.itemData(level);
        currentPopup.find("#arrowLesson").html(nameOfCourse + " > " + levelItemData.label +" > "+ objParent.label);
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
    }else if(itemData._actionClick == action_edit_question_test){
        listWord=[];
        clearForm();
        var lesson = treeAPI.itemData(currentParent);
        getListWordForTest().empty();
        drawListWord(itemData._title);
        getTypeTest().val(itemData._type);
        getExplanationTest().val(itemData._description)
        currentPopup.find("#btnDeleteTestWord").show();
        currentPopup.find("#titlePopupTestWord").html("edit test question");
        getExplanationTest().attr("idLesson",lesson._idLessonForTest);
        var test = treeAPI.itemData(currentParent);
        var level = treeAPI.parent(currentParent);
        var levelItem = treeAPI.itemData(level);
        var row= nameOfCourse +" > " + levelItem.label+ " > "+test.label;
        getExplanationTest().attr("row",row);
    }
    currentPopup.modal('show');
}

function drawListWord(listWord){
    var list=readListMail(listWord);
    if(list!=null && list.length>0){
        getQuestionListWordEdit().empty();
        for(var i=0;i<list.length;i++){
            getQuestionListWordEdit().append(' <div style="margin-top: 5px;" ><p id="word" style="display: inline;background-color: rgb(85, 142, 213);color: white; border-radius: 3px; padding: 2px 10px; vertical-align: middle;">'+list[i]+'</p><i class="fa fa-minus-circle fa-2x" style="color: red;padding-left: 10px;vertical-align: middle;" title="remove word"  id="idWord" ></i></div>');
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
function showHelpIconTop(){
    $("#help-icons").show();
}

function clickCopyCourse(){
    $(document).on("click","#copyCourse",function(){
        copyCourse();
    });
}
function clickTopHelp(){
    $(document).on("click","#help-icons", function(){
        getPopUpHelp().find(".modal-body").empty();
        getPopUpHelp().find(".modal-body").html(initHelpTopData());
        getPopUpHelp().modal('show');
    });
}

function openEditWords(){
    $(document).on("click","#word",function() {
        getListPhonemes().html("");
        getListWeight().html("");
        getListIPA().html("");
        if(currentPopup.find(".action").val() == action_edit_question) {
            var word= $(this).closest("div").find('p').text();
            if(listWord !=null && listWord.length>0){
                $.each(listWord, function(i){
                    if(listWord[i].nameWord === word) {
                        var data=listWord[i];
                        $("#addWordModal").modal('show');
                        drawWord(data);
                    }
                });
            }else {
                loadWeightForWordEdit(word);
            }
        }
    });
}


$(document).ready(function(){
    showHelpIconTop();
    clickTopHelp();
    clickCopyCourse();
    openEditWords();
});