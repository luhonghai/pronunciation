var myTable;
var idCompany=$("#idCompany").val();
var company=$("#company").val();
function listTeacherAndStaff(){
        myTable = $('#dataTables-example').dataTable({
            "retrieve": true,
            "destroy": true,
            "responsive": true,
            "bProcessing": true,
            "bServerSide": true,

            "ajax": {
                "url": "AddTeaherOrStaff",
                "type": "POST",
                "dataType": "json",
                "data": {
                    list: "list",
                    idCompany:idCompany
                }
            },

            "columns": [{
                "sWidth": "40%",
                "data": "userName",
                "sDefaultContent": ""

            }, {
                "sWidth": "30%",
                "data": "role",
                "sDefaultContent": ""

            }, {
                "data": null,
                "bSortable": false,
                "sDefaultContent": "",
                "mRender": function (data, type, full) {
                    $button = $('<button type="button" style="margin-right:10px" id="edit" class="btn btn-info btn-sm" ' + full[0] + '>' + 'Edit' + '</button>' + '<button type="button" id="delete" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Delete' + '</button>');
                    $button.attr("id-column", data.id);
                    $button.attr("username", data.userName);
                    $button.attr("idCompany", data.idCompany);
                    $button.attr("company", data.company);
                    $button.attr("role", data.role);
                    return $("<div/>").append($button).html();
                }
            }]

        });


}

function adduser(){
    $(document).on("click","#yesadd", function(){
        var valide=validateFormAdd();
        if(valide==true) {
            var username = $("#addusername").val();
            var firstname = $("#addfirstname").val();
            var lastname = $("#addlastname").val();
            var password = $("#addpassword").val();
            var role = $("#addrole").val();

                $.ajax({
                    url: "AddTeaherOrStaff",
                    type: "POST",
                    dataType: "text",
                    data: {
                        add: "add",
                        username: username,
                        firstname: firstname,
                        lastname: lastname,
                        password: password,
                        role: role,
                        idCompany:idCompany,
                        company:company
                    },
                    success: function (data) {
                        if (data == "success") {
                            $("tbody").html("");
                            myTable.fnDraw();
                            $("#add").modal('hide');
                            swal("Success!", "Add user success.", "success");
                        }
                        if (data == "error") {
                            $("#UserNameExitAdd").show();
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
        $("#addusername").val("");
        $("#addfirstname").val("");
        $("#addlastname").val("");
        $("#addpassword").val("");
        $("#addrole").val("");

    });
}

function validateFormAdd(){
    var filter=/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i
    var username=$.trim($("#addusername").val());
    var pass=$.trim($("#addpassword").val());
    var role=$.trim($("#addrole").val());
    if(username == "" || username.length == 0 || typeof username ==='underfined'){
        $("#nameadds").show();
        return false;
    }else{
        $("#nameadds").hide();
    }
    if(!filter.test(username)){
        $("#nameaddsemail").show();
        return false;
    }else{
        $("#nameaddsemail").hide();
    }

    if(pass =="" || pass.length == 0 || typeof pass ==='underfined'){
        $("#passadds").show();
        return false;

    }else{
        $("#passadds").hide();
    }

    if( role.length ==0 || typeof role == 'underfined'){
        $("#roleadds").show();
        return false;
    }else{
        $("#roleadds").hide();
    }


    return true;
}


function deletes(){
    $(document).on("click","#delete", function(){
        $("#deletes").modal('show');
        var idd=$(this).attr('id-column');
        var role=$(this).attr('role');
        var username=$(this).attr('username');
        $("#username").val(username);
        $("#iddelete").val(idd);
        $("#roledelete").val(role);
    });
}

function deleteuser(){
    $(document).on("click","#deleteItems", function(){
        var idd = $("#iddelete").val();
        var role = $("#roledelete").val();
        var username= $("#username").val();
        var usernameLogin=$("#usernameLogin").val();
        if(username==usernameLogin){
            swal("Error!", "You can not delete your account", "error");
        }else {
            $.ajax({
                url: "AddTeaherOrStaff",
                type: "POST",
                dataType: "text",
                data: {
                    delete: "delete",
                    id: idd,
                    role: role,
                    username: username
                },
                success: function (data) {
                    if (data == "success") {

                        $("tbody").html("");
                        myTable.fnDraw();
                        $("#deletes").modal('hide');
                        swal("Success!", "Deleted user success", "success");
                    }
                },
                error: function () {
                    swal("Error!", "Could not connect to server", "error");
                }

            });
        }
    });


}

function edit(){
    $(document).on("click","#edit", function() {
        $("#edits").modal('show');
        var idd = $(this).attr('id-column');
        var role = $(this).attr('role');
        var username = $(this).attr('username');
        $("#editUserName").val(username);
        $("#idedit").val(idd);
        $("#editpassword").val("");
        $("#editrole").val(role).change();
        $("#roleold").val(role);

    });

}

function edituser(){
    $(document).on("click","#yesedit", function(){
        var valide=validateFormUpdate();
        if(valide==true) {
            var id = $("#idedit").val();
            var password = $("#editpassword").val();
            var role = $("#editrole").val();
            var roleold=$("#roleold").val();
            var username=$("#editUserName").val();


            $.ajax({
                url: "AddTeaherOrStaff",
                type: "POST",
                dataType: "text",
                data: {
                    edit: "edit",
                    id: id,
                    password: password,
                    role: role,
                    roleold:roleold,
                    username:username,
                    idCompany:idCompany,
                    company:company
                },
                success: function (data) {
                    if (data == "success") {
                        $("tbody").html("");
                        myTable.fnDraw();
                        $("#edits").modal('hide');
                        swal("Success!", "Updated user success", "success");
                    }
                    if (data == "error") {
                        $("#UserNameExitUpdate").show();

                    }

                },
                error: function () {
                    swal("Error!", "Could not connect to server", "error");
                }

            });
        }

    });
}

function validateFormUpdate(){
    var filter=/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
    var pass=$.trim($("#editpassword").val());
    if(pass =="" || pass.length == 0 || typeof pass ==='underfined'){
        $("#passedit").show();
        return false;

    }else{
        $("#passedit").hide();
    }


    return true;

}


function searchAdvanted(){
    $(document).on("click","#button-filter", function(){
        myTable.fnSettings().ajax = {
            "url": "Admins",
            "type": "POST",
            "dataType": "json",
            "data": {
                list: "list",
                username: $("#username").val(),
                firstname:$("#firstname").val(),
                lastname:$("#lastname").val()
            }
        };
        $("tbody").html("");
        myTable.fnDraw();


    });
}

function hideaddmessage(){
    $(document).on("click","#closeadd", function(){
        $("#nameadds").hide();
        $("#nameaddsemail").hide();
        $("#passadds").hide();
        $("#roleadds").hide();
        $("#UserNameExitAdd").hide();
    });

}
function hideeditmessage(){
    $(document).on("click","#closeedit", function(){
        $("#nameeditsemail").hide();

    });

}

$(document).ready(function(){

    hideaddmessage();
    hideeditmessage();
    add();
    adduser();
    edit();
    edituser();
    deletes();
    deleteuser();
    listTeacherAndStaff();

    searchAdvanted();
});

