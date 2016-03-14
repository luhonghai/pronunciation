/**
 * Created by lantb on 2016-02-24.
 */
var servletDuplicated = "/TreeAddNodeServlet";
/**
 * connect to server when add level
 */
function addLevel(){
    $.ajax({
        url : servletDuplicated,
        type : "POST",
        data : {
            action: action_add_level,
            idCourse : idCourse
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
