/**
 * Created by lantb on 2016-02-24.
 */
var CopyServlet = "/CopyServlet";
/**
 * connect to server when copy course
 */
function copyCourse(){
    getDivContainTree().hide();
    getProcessBar().show();
    progress = getProcessBar().progressTimer({
        timeLimit: 300,
        onFinish: function () {
            getProcessBar().hide();
            getDivContainTree().show();
            progress.progressTimer('destroy');
        }
    });
    $.ajax({
        url : CopyServlet,
        type : "POST",
        data : {
            action: "cpCourse",
            idCourse : idCourse
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                //redirect to edit copy course page
                var newIdCourse = data.split(":")[1];
                window.location.href = "/edit-copy-course.jsp?idCourse="+newIdCourse;
            }else{
                //add false show the error
                swal("","copy course fail","error");
            }
        },
        error: function () {
            swal("","copy course fail","error");
        }
    }).error(function(){
        progress.progressTimer('error', {
            errorText:'ERROR!',
            onFinish:function(){
                getDivContainTree().show();
                progress.progressTimer('destroy');
                swal("","copy course fail","error");
            }
        });
    }).done(function(){
        progress.progressTimer('complete');
    });
}

function loadWeightForWordEdit(word){
    $("#addWordModal").modal('show');
    getAddWord().val(word);
    $("#loadPhonemes").hide();
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
                drawWord(data);
            }else{
                swal("Error!",message.split(":")[1], "error");
            }
        },
        error: function () {
            swal("Error!", "Could not connect to server", "error");
        }

    });
}