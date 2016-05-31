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
                    $button.attr("idCompany", data.idCompany);
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
            var idCompany = $('#select-company').val();
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
                    role: role,
                    idCompany : idCompany
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
                }
            }else{
                swal("Error!", data.message.split(":")[1], "error");
            }
        },
        error: function () {
            swal("Error!", "Could not connect to server", "error");
        }
    });
}
function getCompanyEdit(idCompany){
    $.ajax({
        url: "Admins",
        type: "POST",
        dataType: "json",
        data: {
            action: "getCompany"
        },
        success: function (data) {
            if(data.message=="success"){
                $("#select-company-edit").empty();
                if(data.clientCodes!=null){
                    var items=data.clientCodes;
                    if(data.clientCodes!=null){
                        $(items).each(function(){
                            if(this.id.trim() == idCompany.trim()){
                                $("#select-company-edit").append('<option selected value="' + this.id + '">' + this.companyName + '</option>');
                            }else{
                                $("#select-company-edit").append('<option value="' + this.id + '">' + this.companyName + '</option>');
                            }

                        });
                    }
                }
                $('#container-company-edit').show();
                $('#select-company-edit').multiselect('destroy');
                $('#select-company-edit').multiselect({ enableFiltering: true, maxHeight:300,buttonWidth: '100%'});
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
    });

    $('#add').on("show.bs.modal",function(){
        $("#select-company").hide();
        $("#addusername").val("");
        $("#addfirstname").val("");
        $("#addlastname").val("");
        $("#addpassword").val("");
        $('#addrole').multiselect('destroy');
        $('#addrole').multiselect({ enableFiltering: false, maxHeight: 300,buttonWidth: '100%'});
        $('#addrole').multiselect('refresh');
        $('#addrole').multiselect('select', 'Admin');
        getCompany();
    });

    $(document).on("change", "#addrole", function () {
        var vl = $(this).val();
        if(vl == "Teacher" || vl == "Staff"){
            if($('#container-company').is(':visible') == false){
                $('#container-company').show();
                $('#select-company').multiselect('destroy');
                $('#select-company').multiselect({ enableFiltering: true, maxHeight: 300,buttonWidth: '100%'});
                $('#select-company').multiselect('refresh');

            }
        }else{
            $('#container-company').hide();
        }
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
        $("#editRoles").empty();
        var roles;
        var idd = $(this).attr('id-column');
        var username=$(this).attr('username');
        var first = $(this).attr('first');
        var last=$(this).attr('last');
        var role = $(this).attr('role');
        var idCompany = $(this).attr('idCompany');
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
                    $("#usernames-edit").val(username);
                    $("#editfirstname").val(first);
                    $("#editlastname").val(last);
                    $("#editpassword").val("");
                    $('#editrole').val(roles);
                    $('#editrole').multiselect('destroy');
                    $('#editrole').multiselect({ enableFiltering: false, maxHeight: 300,buttonWidth: '100%'});
                    $('#editrole').multiselect('refresh');


                    if(roles == "Staff" || roles == "Teacher"){
                        getCompanyEdit(idCompany);
                    }else{
                        $('#container-company-edit').hide();
                    }
                    $("#edits").modal('show');
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
            var username=$("#usernames-edit").val();
            var firstname = $("#editfirstname").val();
            var lastname = $("#editlastname").val();
            var password = $("#editpassword").val();
            var role = $("#editrole").val();
            var idCompany = $('#select-company-edit').val();
                $.ajax({
                    url: "Admins",
                    type: "POST",
                    dataType: "text",
                    data: {
                        edit: "edit",
                        id: id,
                        username : username,
                        firstname: firstname,
                        lastname: lastname,
                        password: password,
                        role: role,
                        idCompany : idCompany
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

        }

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
    openEdit();
    edituser();
    deletes();
    deleteuser();
    addMappingTeacherAndCompany();
    listAdmin();
    searchAdvanted();
});

