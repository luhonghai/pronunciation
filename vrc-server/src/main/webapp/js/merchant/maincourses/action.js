/**
 * Created by lantb on 2016-02-02.
 */
var servlet = "MainCourseServlet";
function makeOnceCheckboxSelected(){
    $('input[type="checkbox"]').on('change', function() {
        $('input[type="checkbox"]').not(this).prop('checked', false);
    });
}

function addCourse(){
    //post request to server
    $.ajax({
        url : servlet,
        type : "POST",
        data : {
            action: "addcourse",
            name: getName(),
            description: getDescription(),
            share : getShare
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


$(document).ready(function(){
    makeOnceCheckboxSelected();
});
