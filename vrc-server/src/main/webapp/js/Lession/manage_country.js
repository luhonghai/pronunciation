/**
 * Created by CMGT400 on 10/8/2015.
 */
var myTable;
var servletName="ManageCountryServlet";

function openPopupAdd(){
    $(document).on("click","#openAddMapping", function(){
        $("#submitForm").attr("action","add");
        getAllCourse("");
        $('#image').fileinput({
            showUpload : false,
            allowedFileExtensions : ['jpg', 'png','gif'],
        });
        $("#add").modal('show');
    });
}

function submitForm(){
    $(document).on("click","#submitForm", function(){
        var form = $("#addform");
        var formdata = false;
        if (window.FormData){
            formdata = new FormData(form[0]);
        }
        formdata.append("course",$("#select-course").val());
        formdata.append("action",$(this).attr("action"));
        $.ajax({
            url         : servletName,
            data        : formdata ? formdata : form.serialize(),
            cache       : false,
            contentType : false,
            processData : false,
            type        : 'POST',
            success     : function(data, textStatus, jqXHR){
                // Callback code

            }
        });
    });

}



function openEditForm(){
    $(document).on("click",".editMap",function(){

    });

}



function openPopupDelete(){
    //open popup
    $(document).on("click","#delete", function(){

        $("#deletes").modal('show');

    });

    //for delete
    $(document).on("click","#deleteItems", function(){
        var id=  $("#iddelete").val();
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "text",
            data: {
                action: "delete",
                id: id
            },
            success: function (data) {
                if (data.indexOf("success") !=-1) {
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#deletes").modal('hide');
                    swal("Success!", "You delete this mapping success!", "success");
                }else{
                    swal("Could not delete question!", data.message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    });
}

function searchAdvanted(){
    $(document).on("click","#button-filter", function(){
        myTable.fnSettings().ajax = {
            "url": servletName,
            "type": "POST",
            "dataType": "json",
            "data": {
                action: "list",
                CreateDateFrom: $("#CreateDateFrom").val(),
                CreateDateTo: $("#CreateDateTo").val()
            }
        };
        $("tbody").html("");
        myTable.fnDraw();
    });
}
$(document).ready(function(){

    openPopupAdd();
    submitForm();
});


