/**
 * Created by lantb on 2016-02-24.
 */
var servletCopy = "/CopyServlet";
/**
 *
 * @param idCourse
 * @param idLevel
 */
function copyLevel(idCourse, idLevel){
    getDivContainTree().hide();
    getProcessBar().show();
    progress = getProcessBar().progressTimer({
        timeLimit: 40,
        onFinish: function () {
            getProcessBar().hide();
            getDivContainTree().show();
            progress.progressTimer('destroy');
        }
    });
    $.ajax({
        url : servletCopy,
        type : "POST",
        data : {
            action: "cpLevel",
            idCourse : idCourse,
            idLevel : idLevel
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
               reloadTree();
               swal("","Copy level successfully","success");
            }else{
                getDivContainTree().show();
                swal("","Copy level fail","error");
            }
        },
        error: function () {
            getProcessBar().hide();
            getDivContainTree().show();
            swal("","Copy level fail","error");
        }
    }).error(function(){
        progress.progressTimer('error', {
            errorText:'ERROR!',
            onFinish:function(){
              getDivContainTree().show();
              progress.progressTimer('destroy');
              swal("","Copy level fail","error");
            }
        });
    }).done(function(){
        progress.progressTimer('complete');
    });
}

/**
 *
 * @param idCourse
 * @param idLevel
 */
function copyObj(idLevel, idObj){
    getDivContainTree().hide();
    getProcessBar().show();
    progress = getProcessBar().progressTimer({
        timeLimit: 30,
        onFinish: function () {
            getProcessBar().hide();
            getDivContainTree().show();
            progress.progressTimer('destroy');
        }
    });
    $.ajax({
        url : servletCopy,
        type : "POST",
        data : {
            action: "cpObj",
            idLevel : idLevel,
            idObj : idObj
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                reloadTree();
                swal("","Copy objective successfully","success");
            }else{
                getProcessBar().hide();
                getDivContainTree().show();
                swal("","Copy objective fail","error");
            }
        },
        error: function () {
            getProcessBar().hide();
            getDivContainTree().show();
            swal("","Copy objective fail","error");
        }
    }).error(function(){
        progress.progressTimer('error', {
            errorText:'ERROR!',
            onFinish:function(){
                getDivContainTree().show();
                swal("","Copy objective fail","error");
            }
        });
    }).done(function(){
        progress.progressTimer('complete');
    });
}

/**
 *
 * @param idCourse
 * @param idLevel
 */
function copyTest(idLevel, idTest){
    getDivContainTree().hide();
    getProcessBar().show();
    progress = getProcessBar().progressTimer({
        timeLimit: 30,
        onFinish: function () {
            getProcessBar().hide();
            getDivContainTree().show();
            progress.progressTimer('destroy');
        }
    });
    $.ajax({
        url : servletCopy,
        type : "POST",
        data : {
            action: "cpTest",
            idLevel : idLevel,
            idTest : idTest
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                reloadTree();
                swal("","Copy test successfully","success");
            }else{
                getDivContainTree().show();
                swal("","Copy test fail","error");
            }
        },
        error: function () {
            getProcessBar().hide();
            getDivContainTree().show();
            swal("","Copy test fail","error");
        }
    }).error(function(){
        progress.progressTimer('error', {
            errorText:'ERROR!',
            onFinish:function(){
                getDivContainTree().show();
                swal("","Copy test fail","error");
            }
        });
    }).done(function(){
        progress.progressTimer('complete');
    });
}

/**
 *
 * @param idCourse
 * @param idLevel
 */
function copyLesson(idObj, idLesson){
    getDivContainTree().hide();
    getProcessBar().show();
    progress = getProcessBar().progressTimer({
        timeLimit: 30,
        onFinish: function () {
            getProcessBar().hide();
            getDivContainTree().show();
            progress.progressTimer('destroy');
        }
    });
    $.ajax({
        url : servletCopy,
        type : "POST",
        data : {
            action: "cpLesson",
            idObj : idObj,
            idLesson : idLesson
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                reloadTree();
                swal("","Copy lesson successfully","success");
            }else{
                getProcessBar().hide();
                getDivContainTree().show();
                swal("","Copy lesson fail","error");
            }
        },
        error: function () {
            getProcessBar().hide();
            getDivContainTree().show();
            swal("","Copy lesson fail","error");
        }
    }).error(function(){
        progress.progressTimer('error', {
            errorText:'ERROR!',
            onFinish:function(){
                getDivContainTree().show();
                swal("","Copy lesson fail","error");
            }
        });
    }).done(function(){
        progress.progressTimer('complete');
    });
}

/**
 *
 * @param idCourse
 * @param idLevel
 */
function copyQuestion(idLesson, idQuestion){
    getDivContainTree().hide();
    getProcessBar().show();
    progress = getProcessBar().progressTimer({
        timeLimit: 30,
        onFinish: function () {
            getProcessBar().hide();
            getDivContainTree().show();
            progress.progressTimer('destroy');
        }
    });
    $.ajax({
        url : servletCopy,
        type : "POST",
        data : {
            action: "cpQuestion",
            idQuestion : idQuestion,
            idLesson : idLesson
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                reloadTree();
                swal("","Copy question successfully","success");
            }else{
                getDivContainTree().show();
                swal("","Copy question fail","error");
            }
        },
        error: function () {
            getProcessBar().hide();
            getDivContainTree().show();
            swal("","Copy question fail","error");
        }
    }).error(function(){
        progress.progressTimer('error', {
            errorText:'ERROR!',
            onFinish:function(){
                getProcessBar().hide();
                getDivContainTree().show();
                swal("","Copy question fail","error");
            }
        });
    }).done(function(){
        progress.progressTimer('complete');
    });
}
