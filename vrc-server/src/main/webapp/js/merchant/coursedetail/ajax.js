/**
 * Created by lantb on 2016-02-24.
 */
var servletAdd = "/TreeAddNodeServlet";
var servletEdit = "/TreeEditNodeServlet";
var servletDelete = "/TreeDeleteNodeServlet";
var servletPublish = "/PublishCourseServlet";
/**
 * connect to server when add level
 */

/**
 * connect to server when edit level
 */
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
                reloadTree();
                currentPopup.modal('hide');
                swal("Success!", "You have update course success!", "success");
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
                reloadTree();
                currentPopup.modal('hide');
                swal("Success!", "You have delete Level success!", "success");
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
                reloadTree();
                currentPopup.modal('hide');
                swal("Success!", "You have add Level success!", "success");
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
                swal("Success!", "You have update Level success!", "success");
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
                currentPopup.modal('hide');
                swal("Success!", "You have delete Level success!", "success");
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
                reloadTree();
                currentPopup.modal('hide');
                swal("Success!", "You have add Objective success!", "success");
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
                swal("Success!", "You have update Objective success!", "success");
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
                currentPopup.modal('hide');
                swal("Success!", "You have delete Objective success!", "success");
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
                reloadTree();
                currentPopup.modal('hide');
                swal("Success!", "You have add Test success!", "success");
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
                swal("Success!", "You have update Test success!", "success");
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
                swal("Success!", "You have delete Test success!", "success");
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
                reloadTree();
                currentPopup.modal('hide');
                swal("Success!", "You have add Objective success!", "success");
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
                reloadTree();
                currentPopup.modal('hide');
                swal("Success!", "You have add Objective success!", "success");
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
                currentPopup.modal('hide');
                swal("Success!", "You have delete Objective success!", "success");
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
                    getPhonemeLable().html("Arpabet:").css("padding-top","10px");
                    getWeightLable().html("Weight:").css("padding-top","15px");
                    getIPAlable().html("Ipa:").css("padding-top","10px");
                    getListPhonemes().html("");
                    getListWeight().html("");
                    getListIPA().html("");
                    //$("#addWord").attr("readonly","readonly");
                    getAddWord().attr("disabled",true);
                    $.each(data.phonemes, function (idx, obj) {
                        var phonmeName = obj.phoneme;
                        //alert(jsonItem);
                        getListPhonemes().append('<input readonly="readonly" index="'+obj.index+'" value="'+phonmeName+'"  type="text">');
                        getListIPA().append('<input readonly="readonly" index="'+obj.index+'" value="'+obj.ipa+'"  type="text">');
                        getListWeight().append('<input onkeypress="return isNumberKey(event,this)" id="weight'+obj.index+'" class="phoneme-weight" type="text">');
                        getListPhonemes().css({"width":(idx+1)*35});
                        getListWeight().css({"width":(idx+1)*35});
                        getListIPA().css({"width":(idx+1)*35});
                    });
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
                swal("Error!", "Could not connect to server", "error");
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
                //getPublishBtn().removeAttr("disabled");
            }else{
                //getPublishBtn().attr("disabled","disabled");
            }
        },
        error: function () {

        }
    });
}


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
                window.location.href = "/main-courses.jsp";
            }else{

            }
        },
        error: function () {

        }
    });
}