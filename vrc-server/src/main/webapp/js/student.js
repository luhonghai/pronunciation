var myTable;
var idClass=$("#idClasst");
function listAdmin(){

        myTable = $('#dataTables-example').dataTable({
            "retrieve": true,
            "destroy": true,
            "responsive": true,
            "bProcessing": true,
            "bServerSide": true,

            "ajax": {
                "url": "StudentServlet",
                "type": "POST",
                "dataType": "json",
                "data": {
                    list: "list",
                    idClass:idClass
                }
            },

            "columns": [{
                "sWidth": "35%",
                "data": "studentName",
                "sDefaultContent": ""

            }, {
                "sWidth": "30%",
                "data": "createdDate",
                "sDefaultContent": ""
            },{
                "data": null,
                "bSortable": false,
                "sDefaultContent": "",
                "mRender": function (data, type, full) {
                    $button = $('<button type="button" id="delete" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Delete' + '</button>' + '<button type="button" id="report" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Report' + '</button>');
                    $button.attr("id-column", data.id);
                    $button.attr("idStudent", data.idStudent);
                    return $("<div/>").append($button).html();
                }
            }]

        });


}

function add(){
    $(document).on("click","#yesadd", function(){
        var idObjects = [];
        var idClass = $("#yesadd").val();
        $('#select-student option:selected').map(function(a, item){ idObjects.push(item.value);});
        var dto={
            idClass:idClass,
            idObjects:idObjects
        }
            $.ajax({
                url: "StudentServlet",
                type: "POST",
                dataType: "json",
                data: {
                    add: "add",
                    objDto: JSON.stringify(dto)
                },
                success: function (data) {
                    if (data.message.indexOf("success") !=-1) {
                        $("#add").modal('hide');
                        swal("Success!", "You have add Object success!", "success");
                        myTable();
                    }else{
                        swal("Could not add objective!", data.message.split(":")[1], "error");
                    }
                },
                error: function () {
                    swal("Error!", "Could not connect to server", "error");
                }


            });


    });





}

function openAdd(){
    $(document).on("click","#addUser", function(){
        $("#add").modal('show');
        $(".loading-lesson").show();
        var idTeacher =this.val();
        $("#yesadd").attr("disabled","disabled");
        $.ajax({
            url: "StudentServlet",
            type: "POST",
            dataType: "json",
            data: {
                listStudent: "listStudent",
                idTeacher:idTeacher
            },
            success: function (data) {
                if(data.message.indexOf("success")!=-1){
                    $("#select-student").empty();
                    if(typeof data.data !== undefined && data.data != null) {
                        $.each(data.data, function (idx, obj) {
                            $("#select-student").append("<option value='" + obj.id + "' title='"+obj.description+"'>" + obj.name + "</option>");
                        });
                    }else{
                        swal("Info!", "Student unavailable!", "info");
                        $("#add").modal('hide');
                        return;
                    }
                    $(".loading-lesson").hide();
                    $('#select-student').multiselect('destroy');
                    $('#select-student').multiselect({ enableFiltering: true, buttonWidth: '200px'});
                    $("#container-add-student").find(".btn-group").css("padding-left","14px");
                    $('#select-student').multiselect('refresh');
                    $("#yesadd").removeAttr("disabled");
                }else{
                    swal("Error!", data.message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });


    });
}



function deletes(){
    $(document).on("click","#delete", function(){
        $("#deletes").modal('show');
        var idd=$(this).attr('id-column');
        $("#iddelete").val(idd);
    });
}

function openDelete(){
    $(document).on("click","#deleteItems", function(){
            $.ajax({
                url: "StudentServlet",
                type: "POST",
                dataType: "text",
                data: {
                    delete: "delete",
                    id: id
                },
                success: function (data) {
                    if (data == "success") {

                        $("tbody").html("");
                        myTable.fnDraw();
                        $("#deletes").modal('hide');
                    }
                },
                error: function () {
                    swal("Error!", "Could not connect to server", "error");
                }

            });
});
}


$(document).ready(function(){
    var roleAdmin=$("#role").val();
    add();
    openAdd();
    deletes();
    openDelete();
    listAdmin();
});

