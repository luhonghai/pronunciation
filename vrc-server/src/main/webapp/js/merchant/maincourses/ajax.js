/**
 * Created by lantb on 2016-03-07.
 */
var servlet = "/MainCourseServlet";
function addCourse(){
    //post request to server
    $.ajax({
        url : servlet,
        type : "POST",
        data : {
            action: "addcourse",
            name: getCourseName().val(),
            description: getCourseDescription().val(),
            share : getCourseShare().val()
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                //add success will draw again the list or redirect to the new page
                alert(data);
            }else{
                //add false show the error
                alert('error');
            }
        },
        error: function () {
            swal("Error!", "Could not connect to server", "error");
        }
    });
}


function loadAllCourse(){
    $.ajax({
        url : servlet,
        type : "POST",
        data : {
            action: "addcourse",
            name: getCourseName().val(),
            description: getCourseDescription().val(),
            share : getCourseShare().val()
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("success") !=-1) {
                //add success will draw again the list or redirect to the new page.
            }else{
                //add false show the error
            }
        },
        error: function () {
            swal("Error!", "Could not connect to server", "error");
        }
    });
}