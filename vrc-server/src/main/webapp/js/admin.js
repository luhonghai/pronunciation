var myTable;
function listAdmin(){

    myTable=$('#dataTables-example').dataTable({
        "retrieve": true,
        "destroy": true,
        "bProcessing": true,
        "bServerSide": true,

        "ajax": {
            "url": "Admins",
            "type": "POST",
            "dataType":"json",
            "data":{
                list:"list",
                username:$("#username").val(),
                firstname:$("#firstname").val(),
                lastname:$("#lastname").val()
            }
        },

        "columns": [{
            "sWidth": "25%",
            "data": "userName",
            "sDefaultContent":""

        },{
            "sWidth": "20%",
            "data": "firstName",
            "sDefaultContent":""
        },{
            "sWidth": "20%",
            "data": "lastName",
            "sDefaultContent":""
        }, {
            "sWidth": "20%",
            "data": "role",
            "sDefaultContent":""
        }, {
            "data": null,
            "bSortable": false,
            "mRender": function (data, type, full) {
                console.log(data);
                return '<button type="button" style="margin-right:10px" id="edit" id-column-edit=' + data.id + ' username=' + data.userName + ' firstname=' + data.firstName + ' lastname=' + data.lastName + ' role=' + data.role + ' pass=' + data.password + ' class="btn btn-info btn-sm" ' + full[0] + '>' + 'Edit' + '</button>'+'<button type="button" id="delete" id-column-delete=' + data.id + ' class="btn btn-info btn-sm" ' + full[0] + '>' + ' Delete' + '</button>';
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
                    alert("error");
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
        var idd=$(this).attr('id-column-delete');
        $("#iddelete").val(idd);
    });
}

function deleteuser(){
    $(document).on("click","#deleteItems", function(){
        var id=  $("#iddelete").val();
            $.ajax({
                url: "Admins",
                type: "POST",
                dataType: "text",
                data: {
                    delete:"delete",
                    id:id
                },
                success: function (data) {
                    if (data == "success") {
                        $("tbody").html("");
                        myTable.fnDraw();
                        $("#deletes").modal('hide');
                    }
                },
                error: function () {
                    alert("error");
                }

            });


    });

}

function edit(){
    $(document).on("click","#edit", function() {
        $("#edits").modal('show');
        var idd = $(this).attr('id-column-edit');
        var user=$(this).attr('username');
        var first = $(this).attr('firstname');
        var last=$(this).attr('lastname');
        var role = $(this).attr('role');
        var pass = $(this).attr('pass');

        $("#idedit").val(idd);
       $("#editusername").val(user);
        $("#editfirstname").val(first);
       $("#editlastname").val(last);
       $("#editpassword").val("pass");
       $("#editrole").val(role);
    });

}

function edituser(){
    $(document).on("click","#yesedit", function(){
        var valide=validateFormUpdate();
        if(valide==true) {
            var id = $("#idedit").val();
            var username = $("#editusername").val();
            var firstname = $("#editfirstname").val();
            var lastname = $("#editlastname").val();
            var password = $("#editpassword").val();
            var role = $("#editrole").val();


            $.ajax({
                url: "Admins",
                type: "POST",
                dataType: "text",
                data: {
                    edit: "edit",
                    id: id,
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
                        $("#UserNameExitUpdate").show();

                    }

                },
                error: function () {
                    alert("error");
                }

            });
        }

    });
}

function validateFormUpdate(){
    var filter=/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
    var username=$.trim($("#editusername").val());
    var pass=$.trim($("#editpassword").val());
    var role=$.trim($("#editrole").val());

    if(username == "" || username.length == 0 || typeof username ==='underfined'){
        $("#nameEdits").show();
        return false;
    }else{
        $("#nameEdits").hide();
    }

    if(!filter.test(username)){
        $("#nameeditsemail").show();
        return false;
    }else{
        $("#nameeditsemail").hide();
    }

    if(pass =="" || pass.length == 0 || typeof pass ==='underfined'){
        $("#passEdits").show();
        return false;
    }else{
        $("#passEdits").hide();
    }

    if( role.length ==0 || typeof role == 'underfined'){
        $("#roleEdits").show();
        return false;
    }else{
        $("#roleEdits").hide();
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
        $("#nameEdits").hide();
        $("#passEdits").hide();
        $("#nameeditsemail").hide();
        $("#UserNameExitUpdate").hide();
        $("#roleEdits").hide();

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
    listAdmin();
    searchAdvanted();
});

