/**
 * Created by lantb on 2016-02-24.
 */
var servletAdd = "/TreeAddNodeServlet";
var servletEdit = "/TreeEditNodeServlet";
var servletDelete = "/TreeDeleteNodeServlet";
/**
 * connect to server when add level
 */
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
            currentPopup.find(".validateMsg").html("Can not connect to server!");
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
            currentPopup.find(".validateMsg").html("Can not connect to server!");
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
            currentPopup.find(".validateMsg").html("Can not connect to server!");
            currentPopup.find(".validateMsg").show();
        }
    });
}