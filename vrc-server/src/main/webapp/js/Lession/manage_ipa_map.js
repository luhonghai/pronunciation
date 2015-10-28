/**
 * Created by CMGT400 on 10/8/2015.
 */
var myTable;
var servletName="ManageIpaMapArpabetServlet";





function openPopupAdd(){
    $(document).on("click","#openAddMapping", function(){
        clearForm();
        $("#submitForm").attr("action","add");
        initSelect("");
        $("#add").modal('show');
    });
}

function submitForm(){
    $(document).on("click","#submitForm", function(){
        var action = $(this).attr("action");
        var dto = getDataForm();
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "json",
            data: {
                action: action,
                dto : JSON.stringify(dto)
            },
            success: function (data) {
                if (data.message.indexOf("success") !=-1) {
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#add").modal('hide');
                    swal("Success!", "You have add mapping success!", "success");
                }else{
                    swal("Could not add mapping!", data.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });


    });

}



function openEditForm(){
    $(document).on("click",".editMap",function(){
        var id = $(this).attr('id-column');
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "json",
            data: {
                action: "getById",
                id : id
            },
            success: function (data) {
                if(data !== undefined){
                    clearForm();
                    includeDataForm(data);
                    $("#add").modal('show');
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    });

}



function openPopupDelete(){
    //open popup
    $(document).on("click","#delete", function(){
        var idd=$(this).attr('id-column');
        $("#iddelete").val(idd);
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
                    swal("Could not delete question!", data.split(":")[1], "error");
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
    buildTable();
    initDate();
    initColorPicker();
    openPopupAdd();
    openEditForm();
    openPopupDelete();
    submitForm();
    searchAdvanted();
});


