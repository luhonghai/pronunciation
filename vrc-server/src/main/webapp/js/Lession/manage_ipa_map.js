/**
 * Created by CMGT400 on 10/8/2015.
 */
var myTable;
var servletName="ManageIpaMapArpabetServlet";





function openPopupAdd(){
    $(document).on("click","#openAddMapping", function(){
        clearForms();
        $("#submitForm").attr("action","add");
        $("#wrap-imgTongue-edit").hide();
        $("#wrap-imgTongue-add").show();
        $("#wrap-imgLips-edit").hide();
        $("#wrap-imgLips-add").show();
        $("#wrap-imgJaw-edit").hide();
        $("#wrap-imgJaw-add").show();
        initSelect("");
        $('#imageTongue').fileinput({
            showUpload : false,
            allowedFileExtensions : ['jpg', 'png','gif']
        });
        $('#imageLips').fileinput({
            showUpload : false,
            allowedFileExtensions : ['jpg', 'png','gif']
        });
        $('#imageJaw').fileinput({
            showUpload : false,
            allowedFileExtensions : ['jpg', 'png','gif']
        });
        $("#add").modal('show');
    });
}

function submitForm(){
    $(document).on("click","#submitForm", function(){
        if(validateForm()){
            var action = $(this).attr("action");
            var id = $(this).attr("id_mapping");
            var imgTongue = $(this).attr("imgTongue");
            var imgLips = $(this).attr("imgLips");
            var imgJaw = $(this).attr("imgJaw");
            if($(this).attr("action").indexOf("add") != -1) {
                var form = $("#addform");
                var formdata = false;
                if (window.FormData) {
                    formdata = new FormData(form[0]);
                }
                formdata.append("action", action);
                // var dto = getDataForm();
                $.ajax({
                    url: servletName,
                    type: "POST",
                    dataType: "json",
                    data: formdata ? formdata : form.serialize(),
                    cache: false,
                    contentType: false,
                    processData: false,
                    type: 'POST',
                    success: function (data) {
                        if (data.message.indexOf("success") != -1) {
                            $("tbody").html("");
                            myTable.fnDraw();
                            $("#add").modal('hide');
                            swal("Success!", "You have add mapping success!", "success");
                        } else {
                            swal("Could not add mapping!", data.message.split(":")[1], "error");
                        }
                    },
                    error: function () {
                        swal("Error!", "Could not connect to server", "error");
                    }

                });
            }else{
                var form = $("#addform");
                var formdata = false;
                if (window.FormData) {
                    formdata = new FormData(form[0]);
                }
                formdata.append("action", action);
                formdata.append("id", id);
                formdata.append("imgTongue", imgTongue);
                formdata.append("imgLips", imgLips);
                formdata.append("imgJaw", imgJaw);
                // var dto = getDataForm();
                $.ajax({
                    url: servletName,
                    type: "POST",
                    dataType: "json",
                    data: formdata ? formdata : form.serialize(),
                    cache: false,
                    contentType: false,
                    processData: false,
                    type: 'POST',
                    success: function (data) {
                        if (data.message.indexOf("success") != -1) {
                            $("tbody").html("");
                            myTable.fnDraw();
                            $("#add").modal('hide');
                            swal("Success!", "You have edit mapping success!", "success");
                        } else {
                            swal("Could not add mapping!", data.message.split(":")[1], "error");
                        }
                    },
                    error: function () {
                        swal("Error!", "Could not connect to server", "error");
                    }

                });
            }
        }
    });

}



function openEditForm(){
    $(document).on("click","#edit",function(){
        var id = $(this).attr('id-column');
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "json",
            data: {
                getById: "getById",
                id : id
            },
            success: function (data) {
                if(data !== undefined){
                    clearForms();
                    includeDataForm(data);
                    $("#add").modal('show');
                    $("#wrap-imgTongue-add").hide();
                    $("#wrap-imgLips-add").hide();
                    $("#wrap-imgJaw-add").hide();
                    $("#wrap-imgTongue-edit").show();
                    $("#wrap-imgLips-edit").show();
                    $("#wrap-imgJaw-edit").show();
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    });
    $("#btn-imgTongue-edit").click(function(){
        $("#wrap-imgTongue-add").show();
        $('#imageTongue').fileinput({
            showUpload : false,
            allowedFileExtensions : ['jpg', 'png','gif']
        });
    });
    $("#btn-imgLips-edit").click(function(){
        $("#wrap-imgLips-add").show();
        $('#imageLips').fileinput({
            showUpload : false,
            allowedFileExtensions : ['jpg', 'png','gif']
        });
    });
    $("#btn-imgJaw-edit").click(function(){
        $("#wrap-imgJaw-add").show();
        $('#imageJaw').fileinput({
            showUpload : false,
            allowedFileExtensions : ['jpg', 'png','gif']
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
                delete: "delete",
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
                loadData: "list",
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


