/**
 * Created by lantb on 2016-02-22.
 */
var servletAdd = "/TreeAddNodeServlet";
var currentPopup;
function openPopup(itemData){
    currentPopup = $('#'+ itemData._popupId);
    currentPopup.find(".validateMsg").hide();
    currentPopup.find(".action").val(itemData._actionClick);
    currentPopup.find(".idHidden").val(itemData.id);
    if (itemData._actionClick == action_add_level){
        clearForm();
        currentPopup.find("#titlePopup").html("Add Level");
        currentPopup.find("#btnDeleteLevel").hide();
    }else if(itemData._actionClick == action_edit_level){
        currentPopup.find("#titlePopup").html("Level management");
        getLevelName().val(itemData.label);
        getLevelDescription().val(itemData._title);
        currentPopup.find("#btnDeleteLevel").show();
    }else if(itemData._actionClick == action_add_obj){
        clearForm();
        var level = treeAPI.itemData(currentParent);
        getObjName().attr("idLevel", level.id);
        currentPopup.find("#titlePopupObj").html("Add Objective");
        currentPopup.find("#btnDeleteObj").hide();
    }else if(itemData._actionClick == action_edit_obj){
        currentPopup.find("#titlePopupObj").html("Objective management");
        getObjName().val(itemData.label);
        getObjDescription().val(itemData._title);
        var level = treeAPI.itemData(currentParent);
        getObjName().attr("idLevel", level.id);
        currentPopup.find("#btnDeleteObj").show();
        currentPopup.find("#arrowObj").html(nameOfCourse+">" + level.label + ">" + itemData.label);
    }else if(itemData._actionClick == action_add_test){
        clearForm();
        var level = treeAPI.itemData(currentParent);
        getPercentPass().attr("idLevel",level.id);
        currentPopup.find("#titlePopupTest").html("Add Test");
        currentPopup.find("#btnDeleteTest").hide();
        currentPopup.find("#arrowTest").html(nameOfCourse + ">" + level.label + ">" + itemData.label);
    }else if(itemData._actionClick == action_edit_test){
        currentPopup.find("#titlePopupTest").html("Test management");
        var percent = itemData._title.split("%")[0];
        getPercentPass().val(percent);
        var level = treeAPI.itemData(currentParent);
        getPercentPass().attr("idLevel",level.id);
        currentPopup.find("#btnDeleteTest").show();
        currentPopup.find("#arrowTest").html(nameOfCourse+">" + level.label + ">" + itemData.label);
    }
    currentPopup.modal('show');
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



$(document).ready(function(){
    btnSaveLevel();
    btnDeleteLevel();
    btnSaveObj();
    btnDeleteObj();
    btnSaveTest();
    btnDeleteTest();
});