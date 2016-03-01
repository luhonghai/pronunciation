

function listMyClasses(){
    $.ajax({
        url: "ClassServlet",
        type: "POST",
        dataType: "json",
        data: {
           action:"listMyClass"
        },
        success: function (data) {
            if(data.message=="success" && data.listclass!=null){
                var listMyClass=data.listclass;
                for(var i=0;i<listMyClass.length;i++){
                    $button = $('<button type="button"  style="display: block; margin-top: 5px;" id="info" class="btn btn-info btn-sm" title='+listMyClass[i].definition+'><img src="/images/teacher/my%20classes24x24.gif" style="width: 30px;height: 30px"> '+listMyClass[i].className+'</button>');
                    $button.attr("id-column", listMyClass[i].id);
                    $button.attr("className", listMyClass[i].className);
                    $button.attr("definition", listMyClass[i].definition);
                    $button.css({"background-color": "#003366","color":"#ffffff"});
                    $("#listMyClass").append($button).html();
                }
            }
        },
        error: function () {
            swal("Error!", "Could not connect to server", "error");
        }

    });

}


function addClass(){
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
                        $("#add").modal('hide');
                        swal("Success!", "Add class success.", "success");
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
        var id=$("#iddelete").val();
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
                        $("#deletes").modal('hide');
                        swal("Success!", "Delete class success", "success");
                    }else{
                        $("#deletes").modal('hide');
                        swal({title: "Warning!", text: "This class has been already deleted!",   type: "warning",timer:"5000" });
                        location.reload();
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
            var difinition = $("#editDefinition").val();


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
                        $("#edits").modal('hide');
                        swal("Success!", "Update class success", "success");
                    }else{
                        $("#deletes").modal('hide');
                        swal({title: "Warning!", text: "This class has been already deleted!",   type: "warning",timer:"5000" });
                        location.reload();
                    }
                },
                error: function () {
                    swal("Error!", "Could not connect to server", "error");
                }

            });

    });
}
function helpMyClass(){
    $(document).on("click","#help-icons",function() {
        $("#helpMyClassModal").modal('show');
    });
}

$(document).ready(function(){
    $('#help-icons').show();
    add();
    addClass();
    helpMyClass();
    listMyClasses();
});

