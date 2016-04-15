var myTable;

function listAdmin(){

        myTable = $('#dataTables-example').dataTable({
            "retrieve": true,
            "destroy": true,
            "responsive": true,
            "bProcessing": true,
            "bServerSide": true,

            "ajax": {
                "url": "Admins",
                "type": "POST",
                "dataType": "json",
                "data": {
                    list: "list",
                    username: $("#username").val(),
                    firstname: $("#firstname").val(),
                    lastname: $("#lastname").val()
                }
            },

            "columns": [{
                "sWidth": "25%",
                "data": "userName",
                "sDefaultContent": ""

            }, {
                "sWidth": "20%",
                "data": "firstName",
                "sDefaultContent": ""
            }, {
                "sWidth": "20%",
                "data": "lastName",
                "sDefaultContent": ""
            }, {
                "data": null,
                "bSortable": true,
                "sDefaultContent": "",
                "mRender": function (data, type, full) {
                    if (data.role == 1) {
                        return '<p>' + 'Admin' + '</p>';
                    }
                    if (data.role == 2) {
                        return '<p>' + 'User' + '</p>';
                    }
                    if (data.role == 3) {
                        return '<p>' + 'Staff' + '</p>';
                    }
                    if (data.role == 4) {
                        return '<p>' + 'Teacher' + '</p>';
                    }
                }
            }, {
                "data": null,
                "bSortable": false,
                "sDefaultContent": "",
                "mRender": function (data, type, full) {
                    $button = $('<button type="button" style="margin-right:10px" id="edit" class="btn btn-info btn-sm" ' + full[0] + '>' + 'Edit' + '</button>' + '<button type="button" id="delete" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Delete' + '</button>');
                    $button.attr("id-column", data.id);
                    $button.attr("username", data.userName);
                    $button.attr("first", data.firstName);
                    $button.attr("last", data.lastName);
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
            if(role=="Admin" || role=="User") {
                $.ajax({
                    url: "Admins",
                    type: "POST",
                    dataType: "text",
                    data: {
                        add: "add",
                        username: username,
                        firstname: firstname,
                        lastname: lastname,
                        password: password,
                        role: role
                    },
                    success: function (data) {
                        if (data == "success") {
                            $("tbody").html("");
                            myTable.fnDraw();
                            $("#add").modal('hide');
                        }
                        if (data == "error") {
                            $("#UserNameExitAdd").show();
                        }
                    },
                    error: function () {
                        swal("Error!", "Could not connect to server", "error");
                    }

                });
            }else{
                $(".loading-lesson").show();
                $("#teacher").modal('show');
                $("#fullNames").val(username);
                $("#firstNames").val(firstname);
                $("#lastNames").val(lastname);
                $("#passwords").val(password);
                $("#roles").val(role);
                getCompany();
            }
        }

    });
}
function getCompany(){
    $.ajax({
        url: "Admins",
        type: "POST",
        dataType: "json",
        data: {
            action: "getCompany"
        },
        success: function (data) {
            if(data.message=="success"){
                $("#select-company").empty();
                if(data.clientCodes!=null){
                    var items=data.clientCodes;
                    $(items).each(function(){
                        $("#select-company").append('<option value="' + this.id + '">' + this.companyName + '</option>');
                    });
                }else{
                    swal("Info!", "Company unavailable!", "info");
                    $("#teacher").modal('hide');
                    return;
                }
                $(".loading-lesson").hide();
                $('#select-company').multiselect('destroy');
                $('#select-company').multiselect({ enableFiltering: true, buttonWidth: '200px'});
                $("#container-add-company").find(".btn-group").css("padding-left","14px");
                $('#select-company').multiselect('refresh');

            }else{
                swal("Error!", data.message.split(":")[1], "error");
            }
        },
        error: function () {
            swal("Error!", "Could not connect to server", "error");
        }

    });
}
function getCompanys(role,username){
    $.ajax({
        url: "Admins",
        type: "POST",
        dataType: "json",
        data: {
            getCompany: "getCompany",
            role:role,
            username:username
        },
        success: function (data) {
            if(data.message=="success"){
                $("#select-company-edit").empty();
                if(data.clientCodes!=null || data.check!=null){
                    var items=data.clientCodes;
                    var item=data.check;
                    if(data.clientCodes!=null && data.check==null){
                        $(items).each(function(){
                            $("#select-company-edit").append('<option value="' + this.id + '">' + this.companyName + '</option>');
                        });
                    }
                    if(data.clientCodes==null && data.check!=null){
                        $(item).each(function () {
                            $("#select-company-edit").append("<option selected='selected' value='" + this.id + "'>" + this.companyName + "</option>");

                        });
                    }
                    if(data.clientCodes!=null && data.check!=null) {
                        $(item).each(function () {
                            $("#select-company-edit").append("<option selected='selected' value='" + this.id + "'>" + this.companyName + "</option>");

                        });
                        $(items).each(function () {
                            $("#select-company-edit").append('<option value="' + this.id + '">' + this.companyName + '</option>');
                        });
                    }
                }else{
                    swal("Info!", "Company unavailable!", "info");
                    $("#teacher").modal('hide');
                    return;
                }
                $(".loading-lesson").hide();
                $('#select-company-edit').multiselect('destroy');
                $('#select-company-edit').multiselect({ enableFiltering: true, buttonWidth: '200px'});
                $("#container-add-company").find(".btn-group").css("padding-left","14px");
                $('#select-company-edit').multiselect('refresh');

            }else{
                swal("Error!", data.message.split(":")[1], "error");
            }
        },
        error: function () {
            swal("Error!", "Could not connect to server", "error");
        }

    });
}

function addMappingTeacherAndCompany(){
    $(document).on("click","#addteacher", function(){
        var fullName=$("#fullNames").val();
        var firstName=$("#firstNames").val();
        var lastName=$("#lastNames").val();
        var password=$("#passwords").val();
        var role=$("#roles").val();
        var idObjects=[];
        $('#select-company option:selected').map(function(a, item){ idObjects.push({idCompany:item.value,companyName:item.text});});

        var dto={
            fullName:fullName,
            firstName:firstName,
            lastName:lastName,
            password:password,
            role:role,
            companies:idObjects
        }
        console.log(dto);
        $.ajax({
            url: "Admins",
            type: "POST",
            dataType: "text",
            data: {
                addTeacher: "addTeacher",
                objDto: JSON.stringify(dto)// to json word,
            },
            success: function (data) {
                if (data=="success") {
                    $("#add").modal('hide');
                    $("#teacher").modal('hide');
                    swal("Success!", "You have add success!", "success");
                    myTable.fnDraw();
                }else{
                    swal("Warning!", "User name exist.", "warning");
                    $("#teacher").modal('hide');
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });

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
        $("#roleDelete").val(role);
        $("#iddelete").val(idd);
        $("#usernameDelete").val(username);
    });
}

function deleteuser(){
    $(document).on("click","#deleteItems", function(){
        var username=  $("#usernameDelete").val();
        var role=  $("#roleDelete").val();
        var id=  $("#iddelete").val();
        var ids=$("#ids").val();
        if(id!=ids) {
            $.ajax({
                url: "Admins",
                type: "POST",
                dataType: "text",
                data: {
                    delete: "delete",
                    id: id,
                    role:role,
                    username:username
                },
                success: function (data) {
                    if (data == "success") {
                        $("tbody").html("");
                        myTable.fnDraw();
                        $("#deletes").modal('hide');
                    }else{
                        swal("Error!", "You can not delete your account", "error");
                    }

                },
                error: function () {
                    swal("Error!", "Could not connect to server", "error");
                }

            });
        }
        if(id==ids){
            swal("Error!", "You can not delete your account", "error");
        }


    });

}

function openEdit(){
    $(document).on("click","#edit", function() {
        $("#edits").modal('show');
        $("#editRoles").empty();
        var roles;
        var idd = $(this).attr('id-column');
        var username=$(this).attr('username');
        var first = $(this).attr('first');
        var last=$(this).attr('last');
        var role = $(this).attr('role');

        //var pass = $(this).attr('pass');
        if(role==1) {
            roles = "Admin";
        }else if(role==2){
            roles="User";
        }else if(role==3){
            roles="Staff";
        }else{
            roles="Teacher";
        }
        $.ajax({
            url: "Admins",
            type: "POST",
            dataType: "json",
            data: {
                edittest: "edit",
                id: idd
            },
            success: function (data) {
                if (data.message == "success") {
                    $("#editRoles").append(data.content);
                    $("#idedit").val(idd);
                    $("#usernames").val(username);
                    $("#editfirstname").val(first);
                    $("#editlastname").val(last);
                    $("#editpassword").val("");
                    $("#editrole").val(roles);
                }
                if (data.message == "error") {
                    swal("Error!", "User not exist.", "error");

                }

            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    });

}


function edituser(){
    $(document).on("click","#yesedit", function(){
        var valide=validateFormUpdate();
        if(valide==true) {
            var id = $("#idedit").val();
            var username=$("#usernames").val();
            var firstname = $("#editfirstname").val();
            var lastname = $("#editlastname").val();
            var password = $("#editpassword").val();
            var role = $("#editrole").val();

            if (role == "Admin" || role == "User") {
                $.ajax({
                    url: "Admins",
                    type: "POST",
                    dataType: "text",
                    data: {
                        edit: "edit",
                        id: id,
                        firstname: firstname,
                        lastname: lastname,
                        password: password,
                        role: role
                    },
                    success: function (data) {
                        if (data == "success") {
                            $("tbody").html("");
                            myTable.fnDraw();
                            $("#edits").modal('hide');
                        }
                        if (data == "error") {
                            $("#UserNameExitUpdate").show();

                        }

                    },
                    error: function () {
                        swal("Error!", "Could not connect to server", "error");
                    }

                });
            }else{
                $(".loading-lesson").show();
                $("#teachers").modal('show');
                $("#fullNamesEdit").val(username);
                $("#firstNamesEdit").val(firstname);
                $("#lastNamesEdits").val(lastname);
                $("#passwordsEdit").val(password);
                $("#rolesEdit").val(role);
                getCompanys(role,username);
            }
        }

    });
}

function editMappingTeacherAndCompany(){
    $(document).on("click","#editteacher", function(){
        var fullName=$("#fullNamesEdit").val();
        var firstName=$("#firstNamesEdit").val();
        var lastName=$("#lastNamesEdits").val();
        var password=$("#passwordsEdit").val();
        var role=$("#rolesEdit").val();
        var idObjects=[];
        $('#select-company-edit option:selected').map(function(a, item){ idObjects.push({idCompany:item.value,companyName:item.text});});

        var dto={
            fullName:fullName,
            firstName:firstName,
            lastName:lastName,
            password:password,
            role:role,
            companies:idObjects
        }
        console.log(dto);
        $.ajax({
            url: "Admins",
            type: "POST",
            dataType: "text",
            data: {
                editTeacher: "editTeacher",
                objDto: JSON.stringify(dto)// to json word,
            },
            success: function (data) {
                if (data=="success") {
                    $("#edits").modal('hide');
                    $("#teachers").modal('hide');
                    swal("Success!", "You have edit success!", "success");
                    myTable.fnDraw();
                }else{
                    swal("Warning!", "User name exist.", "warning");
                    $("#teachers").modal('hide');
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });

    });
}

function validateFormUpdate(){
    var filter=/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
    var username=$.trim($("#editusername").val());
    if(username.length>0 && !filter.test(username)){
        $("#nameeditsemail").show();
        return false;
    }else{
        $("#nameeditsemail").hide();
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
    var roleAdmin=$("#role").val();
    hideaddmessage();
    hideeditmessage();
    add();
    adduser();
    //edit();
    openEdit();
    edituser();
    deletes();
    deleteuser();
    addMappingTeacherAndCompany();
        listAdmin();
    editMappingTeacherAndCompany();
    searchAdvanted();
});

