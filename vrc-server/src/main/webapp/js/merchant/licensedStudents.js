function drawStudent(student){
    var $divContainer = $("<div>");
    $divContainer.css("display","block");
    $divContainer.css("margin-top","5px");

    var divCB = $("<div>");
    divCB.css("float","left");
    divCB.css("padding","5px 15px 0px 0px");
    var $cb = $("<input>");
    $cb.attr("type","checkbox");
    $cb.attr("id","check");
    $cb.css("width","20px");
    $cb.css("height","20px");
    $cb.attr("id-column",student.id);
    divCB.append($cb);

    var divA = $("<div>");
    var $a = $("<a>");
    $a.addClass("btn btn-default");
    $a.css("background-color", "#003366");
    $a.css("color", "#ffffff");
    $a.css("border-radius", "5px");
    $a.css("padding", "5px 20px 5px 0px");
    $a.css("border-color", "transparent");
    var $img = $("<img>");
    $img.attr("src","/images/teacher/student48x48.gif");
    $img.css("width","24px");
    $img.css("height","24px");
    $img.css("cursor","pointer");
    var $lbl = $("<label>");
    $lbl.html(student.studentName);
    $lbl.css("cursor","pointer");
    $a.append($img);
    $a.append($lbl);
    divA.append($a);
    $divContainer.append(divCB);
    $divContainer.append(divA);

    getContainerListStudent().append($divContainer);
}

function getContainerListStudent(){
    return $("#listStudent");
}
function clearContentListStudent(){
    getContainerListStudent().html("");
}

function listLicensedStudent(){
    $.ajax({
        "url": "SendMailUser",
        "type": "POST",
        "dataType":"json",
        "data": {
            action: "listLicensedStudents"
        },
        success:function(data){
            if(data.message=="success"){
                clearContentListStudent();
                var listStudent = data.students;
                $.each(listStudent, function(i, item){
                    drawStudent(item);
                });
            }else{
                clearContentListStudent();
            }
        },
        error:function(e){
            swal("Error!", "Could not connect to server", "error");
        }

    });
}


function addStudent(){
    $(document).on("click","#addStudents",function(){
        var listStudent = [];
        $("input[type=checkbox]:checked").each(function(i){
            listStudent[i] = $(this).attr('id-column');
        });

        var obj={
            listStudent:listStudent
        }
        if(listStudent!=null && listStudent.length>0) {
            $.ajax({
                "url": "SendMailUser",
                "type": "POST",
                "dataType": "text",
                "data": {
                    action: "addStudents",
                    listStudent: JSON.stringify(obj)
                },
                success: function (data) {
                    if (data == "success") {
                        $("#listStudent").empty();
                        listLicensedStudent();
                        swal("Success!", "Add success", "success");
                    }
                },
                error: function (e) {
                    swal("Error!", "Could not connect to server", "error");
                }

            });
        }
    })

}

function helpLicenceStudent(){
    $(document).on("click","#help-icons",function() {
        $("#helpLicensedStudentModal").modal('show');
    });
}


$(document).ready(function(){
    $('#checkAll').click(function (e) {
        if ($(this).is(':checked')) {
            $("#checkAllModal").modal('show');
        }
        $('input:checkbox').prop('checked', this.checked);
    });
    $('#help-icons').show();
    helpLicenceStudent();
    addStudent();
    listLicensedStudent();
});

