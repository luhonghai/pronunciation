var myTable;

function listCompany(){

    myTable = $('#dataTables-example').dataTable({
        "retrieve": true,
        "destroy": true,
        "responsive": true,
        "bProcessing": true,
        "bServerSide": true,

        "ajax": {
            "url": "ClientCodeServlet",
            "type": "POST",
            "dataType": "json",
            "data": {
                list: "list",
                companyname: $("#companyname").val(),
                contactname: $("#contactname").val(),
                email: $("#email").val()
            }
        },

        "columns": [{
            "sWidth": "25%",
            "data": "companyName",
            "sDefaultContent": ""

        }, {
            "sWidth": "30%",
            "data": "contactName",
            "sDefaultContent": ""
        }, {
            "sWidth": "30%",
            "data": "email",
            "sDefaultContent": ""
        }, {
            "sWidth": "15%",
            "data": null,
            "bSortable": false,
            "sDefaultContent": "",
            "mRender": function (data, type, full) {
                $button = $('<button type="button" style="margin-right:10px" id="edit" class="btn btn-info btn-sm" ' + full[0] + '>' + 'Edit' + '</button>' + '<button type="button" id="delete" class="btn btn-info btn-sm" ' + full[0] + '>' + ' Delete' + '</button>');
                $button.attr("id-column", data.id);
                $button.attr("company", data.companyName);
                $button.attr("contact", data.contactName);
                $button.attr("email", data.email);
                return $("<div/>").append($button).html();
            }
        }]

    });


}

function adduser(){
    $(document).on("click","#yesadd", function(){
        var valide=validateFormAdd();
        if(valide==true) {
            var companyname = $("#addcompanyname").val();
            var contactname = $("#addcontactname").val();
            var email = $("#addemail").val();
            $.ajax({
                url: "ClientCodeServlet",
                type: "POST",
                dataType: "text",
                data: {
                    add: "add",
                    companyname: companyname,
                    contactname: contactname,
                    email: email
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
    $(document).on("click","#adduser", function(){
        $("#add").modal('show');
        $("#addcompanyname").val("");
        $("#addcontactname").val("");
        $("#addemail").val("");

    });
}

function validateFormAdd(){
    var filter=/^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i
    var companyname=$.trim($("#addcompanyname").val());
    var contactname=$.trim($("#addcontactname").val());
    var email=$.trim($("#addemail").val());

    if(companyname == "" || companyname.length == 0 || typeof companyname ==='underfined'){
        $("#addcompanynames").show();
        return false;
    }else{
        $("#addcompanynames").hide();
    }

    if(contactname == "" || contactname.length == 0 || typeof contactname ==='underfined'){
        $("#addcontactnames").show();
        return false;
    }else{
        $("#addcontactnames").hide();
    }

    if(email == "" || email.length == 0 || typeof email ==='underfined'){
        $("#nameadds").show();
        return false;
    }else{
        $("#nameadds").hide();
    }
    if(!filter.test(email)){
        $("#nameaddsemail").show();
        return false;
    }else{
        $("#nameaddsemail").hide();
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
            $.ajax({
                url: "ClientCodeServlet",
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
                    alert("error");
                }

            });



    });

}

function edit(){
    $(document).on("click","#edit", function() {
        $("#edits").modal('show');

        var idd = $(this).attr('id-column');
        var company = $(this).attr('company');
        var contact=$(this).attr('contact');
        var email = $(this).attr('email');

        $("#idedit").val(idd);
        $("#editcompanyname").val(company);
        $("#editcontactname").val(contact);
        $("#editemail").val(email);
    });

}

function edituser(){
    $(document).on("click","#yesedit", function(){
        var valide=validateFormUpdate();
        if(valide==true) {
            var id = $("#idedit").val();
            var contact = $("#editcontactname").val();
            var email = $("#editemail").val();

            $.ajax({
                url: "ClientCodeServlet",
                type: "POST",
                dataType: "text",
                data: {
                    edit: "edit",
                    id: id,
                    contact: contact,
                    email: email
                },
                success: function (data) {
                    if (data == "success") {
                        $("tbody").html("");
                        myTable.fnDraw();
                        $("#edits").modal('hide');
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
    var email=$.trim($("#editemail").val());
    if(email == "" || email.length == 0 || typeof email ==='underfined'){
        $("#nameedits").show();
        return false;
    }else{
        $("#nameedits").hide();
    }
    if(!filter.test(email)){
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
            "url": "ClientCodeServlet",
            "type": "POST",
            "dataType": "json",
            "data": {
                list: "list",
                companyname: $("#companyname").val(),
                contactname: $("#contactname").val(),
                email: $("#email").val()
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
    listCompany();
    searchAdvanted();
});


