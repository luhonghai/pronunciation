/**
 * Created by lantb on 2016-02-22.
 */
var servletAdd = "/TreeAddNodeServlet";
var currentPopup;
function openPopup(itemData){
    currentPopup = $('#'+ itemData._popupId);
    currentPopup.find(".validateLvMsg").hide();
    currentPopup.find(".action").val(itemData._actionClick);
    currentPopup.find(".idHidden").val(itemData.id);
    if (itemData._actionClick == action_add_level){
        clearForm();
        currentPopup.find("#titlePopup").html("Add Level");
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
    }else if(itemData._actionClick == action_edit_obj){
        currentPopup.find("#titlePopupObj").html("Objective management");
        getObjName().val(itemData.label);
        getObjDescription.val(itemData._title);
        var level = treeAPI.itemData(currentParent);
        getObjName().attr("idLevel", level.id);
        currentPopup.find("#btnDeleteObj").show();
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
        if(validateFormLevel()){
            if(currentPopup.find(".action").val() == action_add_obj){

            }else if(currentPopup.find(".action").val() == action_edit_obj){

            }
        }
    });
}


$(document).ready(function(){
    btnSaveLevel();
    btnDeleteLevel();
});