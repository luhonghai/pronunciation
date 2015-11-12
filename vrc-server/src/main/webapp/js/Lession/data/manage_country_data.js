/**
 * Created by CMGT400 on 10/8/2015.
 */
var myTable;
var LoadDataForCountryServlet="LoadDataForCountryServlet";



function clearForm(){
    $("form").find(".form-control").each(function(){
       $(this).val("");
    });
    $("form").find("#image").fileinput('destroy');
    $("form").find("input:checkbox").attr('checked',false);
}

function getAllCourse(idSelected, isSingleSelected){
    $.ajax({
        url: LoadDataForCountryServlet,
        type: "POST",
        dataType: "json",
        data: {
            action: "getAllCourse"
        },
        success: function (obj) {
            if (obj.message.indexOf("success") !=-1) {
                buildSelectBox(obj.data,idSelected, isSingleSelected);
            }else{
                swal("Could not get courses!", obj.split(":")[1], "error");
            }
        },
        error: function () {
            swal("Error!", "Could not connect to server", "error");
        }

    });
}

function getAllCourseForUpdate(idCountry){
    $.ajax({
        url: LoadDataForCountryServlet,
        type: "POST",
        dataType: "json",
        data: {
            action: "getAllCourseForUpdate",
            idCountry: idCountry
        },
        success: function (data) {
            if (data.message.indexOf("success") !=-1) {
                var idSelected;
                $.each(data.data,function(idx, obj){
                    if(obj.idCourse != null || obj.idCourse.id.length != 0){
                        idSelected = obj.idCourse;
                    }
                });
                if (idSelected != "undefined"){
                    //if(idSelected == "null"){
                        getAllCourse(idSelected,true);
                    //}else{
                       // getAllCourse(idSelected,false);
                    //}
                }else{
                    swal("Could not get courses!", obj.split(":")[1], "error");
                }

            }else{
                swal("Could not get courses!", obj.split(":")[1], "error");
            }
        },
        error: function () {
            swal("Error!", "Could not connect to server", "error");
        }

    });
}
