/**
 * Created by CMGT400 on 10/8/2015.
 */
var myTable;
var ManageCountryServlet = "ManageCountryServlet";
var LoadDataForCountryServlet = "LoadDataForCountryServlet";

function listCountry(){

    myTable = $('#dataTables-example').dataTable({
        "retrieve": true,
        "destroy": true,
        "responsive": true,
        "bProcessing": true,
        "bServerSide": true,

        "ajax": {
            "url": LoadDataForCountryServlet,
            "type": "POST",
            "dataType": "json",
            "data": {
                action: "listCountry",
                language: $("#language").val(),
                CreateDateFrom: $("#CreateDateFrom").val(),
                CreateDateTo: $("#CreateDateTo").val()
            }
        },

        "columns": [{
            "sWidth": "20%",
            "data": "name",
            "sDefaultContent": ""
        }, {
            "sWidth": "20%",
            "data": "description",
            "sDefaultContent": ""
        }, {
            "sWidth": "20%",
            "data": null,
            "bSortable": false,
            "sDefaultContent": "",
            "mRender": function (data, type, full) {
                var $img = $("<img style='height: 200px;width: 300px'>");
                $img.attr("src",data.imageURL);
                return $("<div/>").append($img).html();
            }
        }, {
            "sWidth": "20%",
            "data": "timeCreated",
            "sDefaultContent": ""
        }, {
            "sWidth": "20%",
            "data": null,
            "bSortable": false,
            "sDefaultContent": "",
            "mRender": function (data, type, full) {
                $button = $('<button type="button" style="margin-right:10px" id="edit" class="btn btn-info btn-sm" ' + full[0] + '>' + 'Edit' + '</button>' + '<button style="margin-right:10px" type="button" id="delete" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Delete' + '</button>');
                $button.attr("id-country", data.id);
                $button.attr("name-country", data.name);
                $button.attr("description", data.description);
                $button.attr("img-src", data.imageURL);
                $button.attr("isDefault", data.isDefault);
                return $("<div/>").append($button).html();
            }
        }]

    });


}

function dateFrom(){
    $('#CreateDateFrom').datetimepicker({
        format: 'DD/MM/YYYY'
    });
}
function dateTo(){
    $('#CreateDateTo').datetimepicker({
        format: 'DD/MM/YYYY'
    });
}

function openPopupAdd(){
    $(document).on("click","#openAddMapping", function(){
        $("#title-modal").html("Add language");
        clearForm();
        $("#submitForm").attr("action","add");
        $('#country_name').prop('readonly', false);
        $("#wrap-img-edit").hide();
        $("#wrap-img-add").show();
        getAllCourse("",true);
        $('#image').fileinput({
            showUpload : false,
            allowedFileExtensions : ['jpg', 'png','gif']
        });
        $("#add").modal('show');
    });
}

function submitForm(){
    $(document).on("click","#submitForm", function(){
        var country = $("#country_name").val();
        var img = $('#image').val();
        //add
        if($(this).attr("action").indexOf("add") != -1){
            if (country == null || typeof country == "undefined" || country.length == 0){
                $("#country_name").focus();
                swal("Warning!", "Please input country name!", "warning");
                return;
            }
            if (img == null || typeof img == "undefined" || img.length == 0){
                $("#image").focus();
                swal("Warning!", "Please input images!", "warning");
                return;
            }
            var form = $("#addform");
            var formdata = false;
            if (window.FormData){
                formdata = new FormData(form[0]);
            }
            formdata.append("course",$("#select-course").val());
            formdata.append("action",$(this).attr("action"));
            $.ajax({
                url         : ManageCountryServlet,
                data        : formdata ? formdata : form.serialize(),
                cache       : false,
                contentType : false,
                processData : false,
                dataType : "json",
                type        : 'POST',
                //success     : function(data, textStatus, jqXHR){
                success     : function(data){
                    if (data.message.indexOf("success") !=-1) {
                        $("tbody").html("");
                        myTable.fnDraw();
                        $("#add").modal('hide');
                        swal("Success!", "You have add language success!", "success");
                    }else if(data.message.indexOf("isDefaut is existed") !=-1) {
                        swal("Error!", "Laguage only one default", "error");
                    }else {
                        swal("Could not add language!", data.message.split(":")[1], "error");
                    }
                },
                error: function () {
                    swal("Error!", "Could not connect to server", "error");
                }
            });
        }else{
            var isUpdateImg = true;
            if (img == null || typeof img == "undefined" || img.length == 0){
                isUpdateImg = false;
            }
            var form = $("#addform");
            var formdata = false;
            if (window.FormData){
                formdata = new FormData(form[0]);
            }
            formdata.append("course",$("#select-course").val());
            formdata.append("idCountry",$(this).attr("id-country"));
            formdata.append("isUpdateImg",isUpdateImg);
            formdata.append("action",$(this).attr("action"));
            $.ajax({
                url         : ManageCountryServlet,
                data        : formdata ? formdata : form.serialize(),
                cache       : false,
                contentType : false,
                processData : false,
                dataType : "json",
                type        : 'POST',
                success     : function(data){
                    if (data.message.indexOf("success") !=-1) {
                        $("tbody").html("");
                        myTable.fnDraw();
                        $("#add").modal('hide');
                        swal("Success!", "You have edit language success!", "success");
                    }else if(data.message.indexOf("isDefaut is existed") !=-1) {
                        swal("Error!", "Laguage only one default", "error");
                    }else{
                        if(data.message.indexOf("deleted") !=-1){
                            $("#add").modal('hide');
                            swal({title: "Warning!", text: "This language has been already deleted!",   type: "warning",timer:"5000" });
                            location.reload();
                        }else{
                            $("#add").modal('hide');
                            swal("Could not edit language!", data.message.split(":")[1], "error");
                        }

                    }
                },
                error: function () {
                    swal("Error!", "Could not connect to server", "error");
                }
            });
        }
    });

}

function openEditForm(){
    $(document).on("click","#edit", function() {
        clearForm();
        $("#title-modal").html("Edit language");
        $("#submitForm").attr("action","edit");
        $("#submitForm").attr("id-country",$(this).attr("id-country"));
        $("#country_name").val($(this).attr("name-country"));
        $('#country_name').prop('readonly', true);
        $("#add-description").val($(this).attr("description"));
        $("#img-edit").attr("src",$(this).attr("img-src"));
        $("#wrap-img-edit").show();
        $("#wrap-img-add").hide();
        if($(this).attr("isDefault").indexOf("true") != -1){
            $("#default").prop('checked', true);
        }else{
            $("#default").prop('checked', false);
        }

        getAllCourseForUpdate($(this).attr("id-country"));
        $('#image').fileinput({
            showUpload : false,
            allowedFileExtensions : ['jpg', 'png','gif']
        });
        $("#add").modal('show');
    });

    //show box input image
    $("#btn-img-edit").click(function(){
        $("#wrap-img-add").show();
    });
}

function openPopupDelete(){
    //open popup
    $(document).on("click","#delete", function(){
        $("#deletes").modal('show');
        $("#deleteItems").attr("action","delete");
        $("#deleteItems").attr("id-country",$(this).attr("id-country"));
    });

    //for delete
    $(document).on("click","#deleteItems", function(){
        var form = $("#form-delete");
        var formdata = false;
        if (window.FormData){
            formdata = new FormData(form[0]);
        }
        formdata.append("idCountry",$(this).attr("id-country"));
        formdata.append("action",$(this).attr("action"));
        $.ajax({
            url         : ManageCountryServlet,
            data        : formdata ? formdata : form.serialize(),
            cache       : false,
            contentType : false,
            processData : false,
            dataType : "json",
            type        : 'POST',
            success     : function(data){
                if (data.message.indexOf("success") !=-1) {
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#deletes").modal('hide');
                    swal("Success!", "You have delete language success!", "success");
                }else{
                    if(data.message.indexOf("deleted") !=-1){
                        $("#deletes").modal('hide');
                        swal({title: "Warning!", text: "This language has been already deleted!",   type: "warning",timer:"5000" });
                        location.reload();
                    }else{
                        $("#deletes").modal('hide');
                        swal("Could not delete language!", data.message.split(":")[1], "error");
                    }

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
            "url": LoadDataForCountryServlet,
            "type": "POST",
            "dataType": "json",
            "data": {
                action: "listCountry",
                language: $("#language").val(),
                CreateDateFrom: $("#CreateDateFrom").val(),
                CreateDateTo: $("#CreateDateTo").val()
            }
        };
        $("tbody").html("");
        myTable.fnDraw();
    });
}

$(document).ready(function(){
    dateFrom();
    dateTo();
    listCountry();
    openPopupAdd();
    submitForm();
    searchAdvanted();
    openEditForm();
    openPopupDelete();
});



