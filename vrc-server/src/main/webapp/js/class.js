var myTable;

function listAdmin(){

        myTable = $('#dataTables-example').dataTable({
            "retrieve": true,
            "destroy": true,
            "responsive": true,
            "bProcessing": true,
            "bServerSide": true,

            "ajax": {
                "url": "ClassServlet",
                "type": "POST",
                "dataType": "json",
                "data": {
                    list: "list",
                    classname: $("#class").val(),
                    CreateDateFrom: $("#CreateDateFrom").val(),
                    CreateDateTo: $("#CreateDateTo").val()
                }
            },

            "columns": [{
                "sWidth": "25%",
                "data": "className",
                "sDefaultContent": ""

            }, {
                "sWidth": "20%",
                "data": "definition",
                "sDefaultContent": ""
            }, {
                "sWidth": "20%",
                "data": "createdDate",
                "sDefaultContent": ""
            }, {
                "data": null,
                "bSortable": false,
                "sDefaultContent": "",
                "mRender": function (data, type, full) {
                    $button = $('<button type="button" style="margin-right:10px" id="edit" class="btn btn-info btn-sm" ' + full[0] + '>' + 'Edit' + '</button>' + '<button type="button" id="delete" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Delete' + '</button>' + '<a href="student-manage.jsp?idClass='+ data.id +'" type="button" id="addStudent" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Add Student' + '</a>');
                    $button.attr("id-column", data.id);
                    $button.attr("classname", data.className);
                    $button.attr("definition", data.definition);
                    return $("<div/>").append($button).html();
                }
            }]

        });


}

function adduser(){
    $(document).on("click","#yesadd", function(){
        var valide=validateFormAdd();
        if(valide==true) {
            var classname = $("#addClassName").val();
            var definition = $("#addDefinition").val();
            $.ajax({
                url: "ClassServlet",
                type: "POST",
                dataType: "text",
                data: {
                    add: "add",
                    classname: classname,
                    definition: definition
                },
                success: function (data) {
                    if (data == "success") {
                        $("tbody").html("");
                        myTable.fnDraw();
                        $("#add").modal('hide');
                    }
                    if (data == "error") {
                        $("#addClassNameExits").show();
                    }
                },
                error: function () {
                    swal("Error!", "Could not connect to server", "error");
                }

            });
        }

    });





}

function add(){
    $(document).on("click","#addUser", function(){
        $("#add").modal('show');
        $("#addClassName").val("");
        $("#addDefinition").val("");

    });
}

function validateFormAdd(){
    var className=$.trim($("#addClassName").val());
    if(className == "" || className.length == 0 || typeof className ==='underfined'){
        $("#addClassNameExits").show();
        return false;
    }else{
        $("#addClassNameExits").hide();
    }

    return true;
}


function deletes(){
    $(document).on("click","#delete", function(){
        $("#deletes").modal('show');
        var idd=$(this).attr('id-column');
        $("#iddelete").val(idd);
    });
}

function deleteuser(){
    $(document).on("click","#deleteItems", function(){
            $.ajax({
                url: "ClassServlet",
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

function edit(){
    $(document).on("click","#edit", function() {
        $("#edits").modal('show');
        var idd = $(this).attr('id-column');
        var classname = $(this).attr('classname');
        var difinition=$(this).attr('definition');

        $("#idedit").val(idd);
        $("#editClassName").val(classname);
       $("#editDefinition").val(difinition);
    });

}

function edituser(){
    $(document).on("click","#yesedit", function(){
            var id = $("#idedit").val();
            var difinition = $("#editlastname").val();


            $.ajax({
                url: "ClassServlet",
                type: "POST",
                dataType: "text",
                data: {
                    edit: "edit",
                    id: id,
                    difinition: difinition
                },
                success: function (data) {
                    if (data == "success") {
                        $("tbody").html("");
                        myTable.fnDraw();
                        $("#edits").modal('hide');
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
            "url": "ClassServlet",
            "type": "POST",
            "dataType": "json",
            "data": {
                list: "list",
                classname: $("#class").val(),
                CreateDateFrom: $("#CreateDateFrom").val(),
                CreateDateTo: $("#CreateDateTo").val()
            }
        };
        $("tbody").html("");
        myTable.fnDraw();


    });
}


$(document).ready(function(){
    var roleAdmin=$("#role").val();
    add();
    adduser();
    edit();
    edituser();
    deletes();
    deleteuser();
    listAdmin();
    searchAdvanted();
});

