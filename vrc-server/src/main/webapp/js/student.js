var myTable;
var idClass=$("#idClasst").val();
function listStudent(){

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
                    idClass:idClass,
                    student:$("#student").val(),
                    CreateDateFrom: $("#CreateDateFrom").val(),
                    CreateDateTo: $("#CreateDateTo").val()
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
                    $button = $('<button type="button"  style="margin-right:10px" id="delete" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Delete' + '</button>' + '<button  style="margin-right:10px" type="button" id="report" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Report' + '</button>');
                    $button.attr("id-column", data.id);
                    $button.attr("idStudent", data.idStudent);
                    return $("<div/>").append($button).html();
                }
            }]

        });


}

function addStudent(){
    $(document).on("click","#yesadd", function(){
        var idObjects = [];
        $('#select-student option:selected').map(function(a, item){ idObjects.push(item.value);});
        var dto={
            idClass:idClass,
            idObjects:idObjects
        }
            $.ajax({
                url: "StudentServlet",
                type: "POST",
                dataType: "text",
                data: {
                    add: "add",
                    objDto: JSON.stringify(dto)
                },
                success: function (data) {
                    if (data=="success") {
                        $("#add").modal('hide');
                        swal("Success!", "You have add success!", "success");
                        myTable.fnDraw();
                    }else{
                        swal("Error!", "Could not connect to server!", "error");
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
        var teacherName =$("#addUser").val();
        $("#yesadd").attr("disabled","disabled");
        $.ajax({
            url: "StudentServlet",
            type: "POST",
            dataType: "json",
            data: {
                listStudent: "listStudent",
                teacherName:teacherName,
                idClass:idClass
            },
            success: function (data) {
                if(data.message=="success"){
                    $("#select-student").empty();
                    if(data.studentMappingTeachers!=null && data.studentMappingTeachers.length>0){
                        var items=data.studentMappingTeachers;
                        $(items).each(function(){
                            $("#select-student").append('<option value="' + this.studentName + '">' + this.studentName + '</option>');
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



function openDelete(){
    $(document).on("click","#delete", function(){
        var studentName=$(this).attr('idStudent');
        $("#deletes").modal('show');
        $("#iddelete").val(studentName);
    });
}

function deleteStudent(){
    $(document).on("click","#deleteItems", function(){
        var studentName=$("#iddelete").val();
            $.ajax({
                url: "StudentServlet",
                type: "POST",
                dataType: "text",
                data: {
                    delete: "delete",
                    idClass: idClass,
                    studentName:studentName
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
function searchAdvanted(){
    $(document).on("click","#button-filter", function(){
        myTable.fnSettings().ajax = {
            "url": "StudentServlet",
            "type": "POST",
            "dataType":"json",
            "data":{
                list: "list",
                idClass:idClass,
                student:$("#student").val(),
                CreateDateFrom: $("#CreateDateFrom").val(),
                CreateDateTo: $("#CreateDateTo").val()
            }
        };
        $("tbody").html("");
        myTable.fnDraw();


    });
}

$(document).ready(function(){
    addStudent();
    openAdd();
    deleteStudent();
    openDelete();
    searchAdvanted();
    listStudent();
});

