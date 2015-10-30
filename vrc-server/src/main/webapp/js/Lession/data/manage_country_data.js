/**
 * Created by CMGT400 on 10/8/2015.
 */
var myTable;
var loadServlet="LoadDataForCountryServlet";



function clearForm(){
    $("form").find(".form-control").each(function(){
       $(this).val("");
    });
    $("form").find("input:checkbox").attr('checked',false);
}

function getAllCourse(idSelected){
    $.ajax({
        url: loadServlet,
        type: "POST",
        dataType: "json",
        data: {
            action: "getAllCourse",
        },
        success: function (obj) {
            if (obj.message.indexOf("success") !=-1) {
                alert(idSelected);
                buildSelectBox(obj.data,idSelected);
            }else{
                swal("Could not get courses!", obj.split(":")[1], "error");
            }
        },
        error: function () {
            swal("Error!", "Could not connect to server", "error");
        }

    });
}