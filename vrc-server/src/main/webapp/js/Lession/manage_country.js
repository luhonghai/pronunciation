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
            "sDefaultContent": "",
            "mRender": function (data, type, full) {
                var $img = $("<img>");
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
                $button = $('<button type="button" style="margin-right:10px" id="edit" class="btn btn-info btn-sm" ' + full[0] + '>' + 'Edit' + '</button>' + '<button style="margin-right:10px" type="button" id="delete" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Delete' + '</button>' + '<a href="ManagementWordOfQuestion.jsp?id='+ data.id +'" type="button" id="addword" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Add Word ' + '</a>');
                $button.attr("id-column", data.id);
                $button.attr("question", data.name);
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
        $("#submitForm").attr("action","add");
        getAllCourse("");
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
        if (country == null || typeof country == "undefined" || country.length == 0){
            $("#country_name").focus();
            swal("Warning!", "Please input country name!", "warning");
            return;
        }
        if (img == null || typeof img == "undefined" || img.length == 0){
            $("#country_name").focus();
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
                }else{
                    swal("Could not add country!", data.message.split(":")[1], "error");
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
            "url": LoadDataForCountryServlet,
            "type": "POST",
            "dataType": "json",
            "data": {
                action: "listCountry",
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
});



