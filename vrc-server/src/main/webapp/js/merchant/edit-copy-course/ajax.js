/**
 * Created by lantb on 2016-02-24.
 */
var servletAdd = "/TreeAddNodeServlet";
var servletEdit = "/TreeEditNodeServlet";
var servletDelete = "/TreeDeleteNodeServlet";
var servletPublish = "/PublishCourseServlet";
var servletCopy = "/CopyServlet";
var servletDrapDrop = "/DragDropServlet";
var servletUpload = "/UploadImgServlet";
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


function uploadImg(){
    var img = $('#uploadImage').val();
    if (isUpdateImg && img !== null && typeof img !== "undefined" && img.length > 0) {
        var form = $("#popupCourse");
        var formdata = false;
        if (window.FormData){
            formdata = new FormData(form[0]);
        }
        formdata.append("idCourse",idCourse);
        formdata.append("action","uploadImg");
        $.ajax({
            url         : servletUpload,
            data        : formdata ? formdata : form.serialize(),
            cache       : false,
            contentType : false,
            processData : false,
            dataType : "text",
            type        : 'POST',
            success     : function(data){
                if (data.indexOf("error") !=-1) {
                    //add false show the error
                    currentPopup.find(".validateMsg").hide();
                    currentPopup.find(".validateMsg").css("color","red");
                    currentPopup.find(".validateMsg").html(data);
                    currentPopup.find(".validateMsg").show();
                }else{
                    if(nameOfCourse.trim() != getCourseName().val().trim()){
                        isEditedTitle = true;
                        UpdateStateCourse();
                    }
                    currentParent = null;
                    firstLoad = true;
                    reloadTree();
                    currentPopup.find(".validateMsg").css("color","red");
                    currentPopup.modal('hide');
                    changeHeaderCourseName(getCourseName().val());
                    $("#listWord").empty();
                    swalNew("", "updated successfully", "success");
                }
                currentPopup.find("#btnSaveCourse").removeAttr("disabled");
            },
            error: function () {
                currentPopup.find(".validateMsg").hide();
                currentPopup.find(".validateMsg").css("color","red");
                currentPopup.find(".validateMsg").html("could not connect to server");
                currentPopup.find(".validateMsg").show();
                currentPopup.find("#btnSaveCourse").attr("disabled","disabled");
            }
        });
    }else{
        //reload the tree
        if(nameOfCourse.trim() != getCourseName().val().trim()){
            isEditedTitle = true;
            UpdateStateCourse();
        }
        currentParent = null;
        firstLoad = true;
        reloadTree();
        currentPopup.find(".validateMsg").css("color","red");
        currentPopup.modal('hide');
        changeHeaderCourseName(getCourseName().val());
        $("#listWord").empty();
        swalNew("", "updated successfully", "success");
        currentPopup.find("#btnSaveCourse").attr("disabled","disabled");
    }
}
function editCourse(){
    currentPopup.find(".validateMsg").html("Your course is being edited..");
    currentPopup.find(".validateMsg").css("color","#A6A6A6")
    currentPopup.find(".validateMsg").show();
    currentPopup.find("#btnSaveCourse").attr("disabled","disabled");
    $.ajax({
        url : servletEdit,
        type : "POST",
        data : {
            action: action_edit_course,
            idCourse : idCourse,
            name: getCourseName().val(),
            description: getCourseDescription().val(),
            share : getCourseShare().val()
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                /*//reload the tree
                if(nameOfCourse.trim() != getCourseName().val().trim()){
                    isEditedTitle = true;
                    UpdateStateCourse();
                }
                currentParent = null;
                firstLoad = true;
                reloadTree();
                currentPopup.find(".validateMsg").css("color","red");
                currentPopup.modal('hide');
                changeHeaderCourseName(getCourseName().val());
                $("#listWord").empty();
                swalNew("", "updated successfully", "success");*/
                uploadImg();
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").hide();
                currentPopup.find(".validateMsg").css("color","red");
                currentPopup.find(".validateMsg").html(data.split(":")[1]);
                currentPopup.find(".validateMsg").show();
            };
            currentPopup.find("#btnSaveCourse").attr("disabled","disabled");
        },
        error: function () {
            currentPopup.find(".validateMsg").hide();
            currentPopup.find(".validateMsg").css("color","red");
            currentPopup.find(".validateMsg").html("could not connect to server");
            currentPopup.find(".validateMsg").show();
            currentPopup.find("#btnSaveCourse").attr("disabled","disabled");
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
                swalNew("", "deleted successfully", "success");
                location.assign("/my-courses.jsp");
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



function addLevel(){
    currentPopup.find(".validateMsg").html("Your level is being added..");
    currentPopup.find(".validateMsg").css("color","#A6A6A6")
    currentPopup.find(".validateMsg").show();
    currentPopup.find("#btnSaveLevel").attr("disabled","disabled");
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
                var id = data.split(":")[1];
                reloadTree(id,"add");
                currentPopup.find(".validateMsg").css("color","red");
                currentPopup.modal('hide');
                swalNew("", "added successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").hide();
                currentPopup.find(".validateMsg").css("color","red");
                currentPopup.find(".validateMsg").html(data.split(":")[1]);
                currentPopup.find(".validateMsg").show();
            }
            currentPopup.find("#btnSaveLevel").attr("disabled","disabled");
        },
        error: function () {
            currentPopup.find(".validateMsg").hide();
            currentPopup.find(".validateMsg").css("color","red");
            currentPopup.find(".validateMsg").html("could not connect to server");
            currentPopup.find(".validateMsg").show();
            currentPopup.find("#btnSaveLevel").attr("disabled","disabled");
        }
    });
}

/**
 * connect to server when edit level
 */
function editLevel(){
    currentPopup.find(".validateMsg").html("Your level is being edited..");
    currentPopup.find(".validateMsg").css("color","#A6A6A6")
    currentPopup.find(".validateMsg").show();
    currentPopup.find("#btnSaveLevel").attr("disabled","disabled");
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
                var idLevel = currentPopup.find(".idHidden").val();
                removeIdCopied(idLevel.trim());
                reloadTree();
                currentPopup.find(".validateMsg").css("color","red");
                currentPopup.modal('hide');
                swalNew("", "updated successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").hide();
                currentPopup.find(".validateMsg").css("color","red");
                currentPopup.find(".validateMsg").html(data.split(":")[1]);
                currentPopup.find(".validateMsg").show();
            }
            currentPopup.find("#btnSaveLevel").attr("disabled","disabled");
        },
        error: function () {
            currentPopup.find(".validateMsg").hide();
            currentPopup.find(".validateMsg").css("color","red");
            currentPopup.find(".validateMsg").html("could not connect to server");
            currentPopup.find(".validateMsg").show();
            currentPopup.find("#btnSaveLevel").attr("disabled","disabled");
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
    currentPopup.find(".validateMsg").html("Your objective is being added..");
    currentPopup.find(".validateMsg").css("color","#A6A6A6")
    currentPopup.find(".validateMsg").show();
    currentPopup.find("#btnSaveObj").attr("disabled","disabled");
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
                var id = data.split(":")[1];
                reloadTree(id,"add");
                currentPopup.find(".validateMsg").css("color","red");
                currentPopup.modal('hide');
                swalNew("", "added successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").hide();
                currentPopup.find(".validateMsg").css("color","red")
                currentPopup.find(".validateMsg").html(data.split(":")[1]);
                currentPopup.find(".validateMsg").show();
            }
            currentPopup.find("#btnSaveObj").removeAttr("disabled");
        },
        error: function () {
            currentPopup.find(".validateMsg").hide();
            currentPopup.find(".validateMsg").css("color","red");
            currentPopup.find(".validateMsg").html("could not connect to server");
            currentPopup.find(".validateMsg").show();
            currentPopup.find("#btnSaveObj").removeAttr("disabled");
        }
    });
}

/**
 * connect to server when edit obj
 */
function editObj(){
    currentPopup.find(".validateMsg").html("Your objective is being edited..");
    currentPopup.find(".validateMsg").css("color","#A6A6A6");
    currentPopup.find(".validateMsg").show();
    currentPopup.find("#btnSaveObj").attr("disabled","disabled");
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
                var idObj = currentPopup.find(".idHidden").val();
                removeIdCopied(idObj.trim());
                reloadTree();
                currentPopup.find(".validateMsg").css("color","red");
                currentPopup.modal('hide');
                swalNew("", "updated successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").hide();
                currentPopup.find(".validateMsg").css("color","red");
                currentPopup.find(".validateMsg").html(data.split(":")[1]);
                currentPopup.find(".validateMsg").show();
            }
        },
        error: function () {
            currentPopup.find(".validateMsg").hide();
            currentPopup.find(".validateMsg").css("color","red");
            currentPopup.find(".validateMsg").html("could not connect to server");
            currentPopup.find(".validateMsg").show();
            currentPopup.find("#btnSaveObj").removeAttr("disabled");
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
                isEditedContent = true;
                UpdateStateCourse();
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
                isEditedContent = true;
                UpdateStateCourse();
                var idTest = currentPopup.find(".idHidden").val();
                removeIdCopied(idTest.trim());
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
                isEditedContent = true;
                UpdateStateCourse();
                reloadTree();
                confirmDeletePopup().modal('hide');
                currentPopup.modal('hide');
                swalNew("", "deleted successfully", "success");
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





function addLesson(){
    currentPopup.find(".validateMsg").html("Your lesson is being added..");
    currentPopup.find(".validateMsg").css("color","#A6A6A6")
    currentPopup.find(".validateMsg").show();
    currentPopup.find("#btnSaveLesson").attr("disabled","disabled");
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
                var id = data.split(":")[1];
                reloadTree(id,"add");
                currentPopup.find(".validateMsg").css("color","red");
                currentPopup.modal('hide');
                swalNew("", "added successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").hide();
                currentPopup.find(".validateMsg").css("color","red");
                currentPopup.find(".validateMsg").html(data.split(":")[1]);
                currentPopup.find(".validateMsg").show();
            }
            currentPopup.find("#btnSaveLesson").removeAttr("disabled");
        },
        error: function () {
            currentPopup.find(".validateMsg").hide();
            currentPopup.find(".validateMsg").css("color","red");
            currentPopup.find(".validateMsg").html("could not connect to server");
            currentPopup.find(".validateMsg").show();
            currentPopup.find("#btnSaveLesson").removeAttr("disabled");
        }
    });
}


function editLesson(){
    currentPopup.find(".validateMsg").html("Your lesson is being added..");
    currentPopup.find(".validateMsg").css("color","#A6A6A6")
    currentPopup.find(".validateMsg").show();
    currentPopup.find("#btnSaveLesson").attr("disabled","disabled");
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
                var idLesson = currentPopup.find(".idHidden").val();
                removeIdCopied(idLesson.trim());
                reloadTree();
                currentPopup.find(".validateMsg").css("color","red");
                currentPopup.modal('hide');
                swalNew("", "updated successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").hide();
                currentPopup.find(".validateMsg").css("color","red");
                currentPopup.find(".validateMsg").html(data.split(":")[1]);
                currentPopup.find(".validateMsg").show();
            }
            currentPopup.find("#btnSaveLesson").removeAttr("disabled");
        },
        error: function () {
            currentPopup.find(".validateMsg").hide();
            currentPopup.find(".validateMsg").css("color","red");
            currentPopup.find(".validateMsg").html("could not connect to server");
            currentPopup.find(".validateMsg").show();
            currentPopup.find("#btnSaveLesson").removeAttr("disabled");
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

function addQuestions(listWord){
    myObject.listWord = listWord;
    currentPopup.find(".validateMsg").html("Your question is being saved..");
    currentPopup.find(".validateMsg").css("color","#A6A6A6");
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
                isEditedContent = true;
                UpdateStateCourse();
                reloadTree();
                currentPopup.find(".validateMsg").css("color","red");
                currentPopup.find(".validateMsg").hide();
                currentPopup.modal('hide');
                swalNew("", "added successfully", "success");
            }else{
                //add false show the error
                currentPopup.find(".validateMsg").hide();
                currentPopup.find(".validateMsg").css("color","red");
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

function editQuestions(listWord){
    myObject.listWord = listWord;
    currentPopup.find(".validateMsg").html("Your question is being edited..");
    currentPopup.find(".validateMsg").css("color","#A6A6A6");
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
                isEditedContent = true;
                UpdateStateCourse();
                reloadTree();
                currentPopup.find(".validateMsg").css("color","red")
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
            currentPopup.find(".validateMsg").css("color","red")
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
                isEditedContent = true;
                UpdateStateCourse();
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
                isEditedContent = true;
                UpdateStateCourse();
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
            currentPopup.find("#btnSaveTestWord").removeAttr("disabled");
        },
        error: function () {
            currentPopup.find(".validateMsg").hide();
            currentPopup.find(".validateMsg").css("color","red")
            currentPopup.find(".validateMsg").html("could not connect to server");
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
                isEditedContent = true;
                UpdateStateCourse();
                reloadTree();
                currentPopup.find(".validateMsg").css("color","red")
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
            currentPopup.find("#btnSaveTestWord").removeAttr("disabled");
        },
        error: function () {
            currentPopup.find(".validateMsg").hide();
            currentPopup.find(".validateMsg").css("color","red")
            currentPopup.find(".validateMsg").html("could not connect to server");
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
                isEditedContent = true;
                UpdateStateCourse();
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
                //swalNew("Error!",message.split(":")[1], "error");
            }
        },
        error: function () {
            //swalNew("Error!", "Could not connect to server", "error");
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
                    //$("#addWordModal").find('#btnSaveWord').attr("disabled", false);
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
        if(isEditedTitle){
            state = "edited title";
        }else{
            state = "duplicated";
        }

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
            if(state.trim() != "duplicated"){
                $('#'+idCourse).find('.aciTreeItem').css("background-color","#558ED5");
            }
            enablePublishBtn();
        },
        error: function () {
            swalNew("","could not connect to server","error");
        }
    });
}

/**
 *
 */
function publishCourse(checkData){
    getDivContainTree().hide();
    getProcessBar().show();
    progress = getProcessBar().progressTimer({
        timeLimit: 120,
        onFinish: function () {
            getProcessBar().hide();
            getDivContainTree().show();
            progress.progressTimer('destroy');
        }
    });
    $.ajax({
        url : servletPublish,
        type : "POST",
        data : {
            action: "publish",
            idCourse : idCourse,
            checkData : checkData
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                window.location.href = "/my-courses.jsp";
                $('#confirmPublish').modal('hide');
            }else if(data.indexOf("showpopup")!= -1){
                $('#confirmPublish').modal('show');
            }else {
                $('#confirmPublish').modal('hide');
                swalNew("","could not connect to server","error");
            }
        },
        error: function () {
            swalNew("","could not connect to server","error");
        }
    }).error(function(){
        progress.progressTimer('error', {
            errorText:'error',
            onFinish:function(){
                getDivContainTree().show();
                progress.progressTimer('destroy');
                swalNew("","publish course fail","error");
            }
        });
    }).done(function(){
        progress.progressTimer('complete');
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
            if(action == targetLoadQuestion){
                setInterval(function(){ reloadTree(); }, 500);
            }
        },
        error: function () {

        }
    });
}