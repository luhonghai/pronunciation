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
                }
            }, {
                "data": null,
                "bSortable": false,
                "sDefaultContent": "",
                "mRender": function (data, type, full) {
                    $button = $('<button type="button" style="margin-right:10px" id="edit" class="btn btn-info btn-sm" ' + full[0] + '>' + 'Edit' + '</button>' + '<button type="button" id="delete" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Delete' + '</button>');
                    $button.attr("id-column", data.id);
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
        var idd=$(this).attr('id-column');
        $("#iddelete").val(idd);
    });
}

function deleteuser(){
    $(document).on("click","#deleteItems", function(){
        var id=  $("#iddelete").val();
        var ids=$("#ids").val();
        if(id!=ids) {
            $.ajax({
                url: "Admins",
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
                    //if (data == "error") {
                    //    alert("You don't allow to delete yourshelf!");
                    //    $("tbody").html("");
                    //    myTable.fnDraw();
                    //    $("#deletes").modal('hide');
                    //}

                },
                error: function () {
                    alert("error");
                }

            });
        }
        if(id==ids){
            $.ajax({
                url: "Admins",
                type: "POST",
                dataType: "text",
                data: {
                    delete: "delete",
                    id: id
                },
                success: function (data) {
                    if (data == "success") {
                        window.location =CONTEXT_PATH + "/logout.jsp";
                    }
                    //if (data == "error") {
                    //    alert("You don't allow to delete yourshelf!");
                    //    $("tbody").html("");
                    //    myTable.fnDraw();
                    //    $("#deletes").modal('hide');
                    //}

                },
                error: function () {
                    alert("error");
                }

            });

        }


    });

}

function edit(){
    $(document).on("click","#edit", function() {
        $("#edits").modal('show');
        var roles;
        var idd = $(this).attr('id-column');
        var first = $(this).attr('first');
        var last=$(this).attr('last');
        var role = $(this).attr('role');
        //var pass = $(this).attr('pass');
        if(role==1){
            roles="Admin";
        }
        if(role==2){
            roles="User";
        }

        $("#idedit").val(idd);
        $("#editfirstname").val(first);
       $("#editlastname").val(last);
       $("#editpassword").val("");
       $("#editrole").val(roles);
    });

}

function edituser(){
    $(document).on("click","#yesedit", function(){
        var valide=validateFormUpdate();
        if(valide==true) {
            var id = $("#idedit").val();
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
                    alert("error");
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
    edit();
    edituser();
    deletes();
    deleteuser();
   // if(roleAdmin=="1"){
        listAdmin();
    //}
    //if(roleAdmin=="2"){
    //    window.location =CONTEXT_PATH + "/error.jsp";
    //}

    searchAdvanted();
});

