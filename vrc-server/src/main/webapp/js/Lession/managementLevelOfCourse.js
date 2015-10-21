/**
 * Created by CMGT400 on 10/5/2015.
 */
var myTable;
var servletName="ManagementLevelOfCourseServlet";

function listLevel(){
    var $selected=$("#level");
    var $listLevel=$("#accordion");
    var id=$("#idCourse").val();
    $('#level option[value!="-1"]').remove();
    $.ajax({
        "url": servletName,
        "type": "POST",
        "dataType":"json",
        "data": {
            action: "listLevel",
            id:id
        },
        success:function(data){
            var items=data.dataforDropdown;
            var listLevel=data.data;
            $(items).each(function(){
                var newOption = '<option color="'+this.color+'" id="'+this.id+'" value="' + this.name + '">' + this.name + '</option>';
                $selected.append(newOption);
            });
            $("#level").append($("#company option").remove().sort(function(a, b) {
                var at = $(a).text(), bt = $(b).text();
                return (at > bt)?1:((at < bt)?-1:0);
            }));
            $(listLevel).each(function(){
                var newOption = '  <div class="panel panel-default" id="panel1"> ' +
                    '<div class="panel-heading"> ' +
                    '<h4 class="panel-title"> ' +
                    '<a data-toggle="collapse" data-target="#collapseOne" href="#collapseOne">' +
                    ''+this.name+' </a> ' +
                    '</h4> ' +
                    '</div>' +
                    '<div id="collapseOne" class="panel-collapse collapse in"> ' +
                    '<div class="panel-body">' +
                     '<div class="row"><button type="button" name="createObject" id="createObject" class="btn btn-default" value="yes" >Create Object</button></div>' +
                     '<div class="row"><button type="button" name="createTest" id="createTest" class="btn btn-default" value="yes" >Create Test</button></div>' +
                     '<div id="testAndObject"></div>'
                    '</div> ' +
                    '</div> ' +
                    '</div>';
                $listLevel.append(newOption);
            });


        }

    });
}

function addLevel(){
    $(document).on("click","#addlevel", function(){
        var idLevel=$("#level option:selected").attr('id');
        var idCourse=$("#idCourse").val();

        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "json",
            data: {
                action: "addLevel",
                idLevel:idLevel,
                idCourse:idCourse
            },
            success: function (data) {
                if(data.message.indexOf("success")!=-1){
                    listLevel();
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

function openPopupAdd(){
    $("#add-word-of-question").click(function(){
        $("#add").modal('show');
        $("#addWord").val("");
        $("#listPhonmes").html("");
        $("#listWeight").html("");
        $(".phoneme-lable").html("");
        $(".weight-lable").html("");
        $("#yesadd").attr("disabled", true);
        $("#loadPhonemes").attr("disabled",false)
        $("#addWord").attr("disabled",false);
        //$("#addWord").removeAttr("readonly");
    });
}

function addWord(){
    //add word click
    $(document).on("click","#yesadd", function(){
        var txtWord=$("#addWord").val();
        if (txtWord == null || typeof txtWord == "undefined" || txtWord.length == 0){
            swal("Warning!", "Word not null!", "warning");
            return;
        }
        var word = {
            idWord: $("#addWord").attr("idWord"),
            idQuestion: $("#id-question").html(),
            data:getWeightAndPhoneme("#listPhonmes","#weight")
        };
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "json",
            data: {
                add: "add",
                word: JSON.stringify(word)
            },
            success: function (data) {
                var message = data.message;
                if(message.indexOf("success")!=-1){
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#add").modal('hide');
                }else{
                    $("#listPhonmes").html("");
                    $("#listWeight").html("");
                    $(".phoneme-lable").html("");
                    $(".weight-lable").html("");
                    $("#yesadd").attr("disabled", true);
                    $("#loadPhonemes").attr("disabled",false)
                    $("#addWord").attr("disabled",false)
                    swal("Error!",message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });


    });

    //load phonemes click

}

$(document).ready(function(){
    addLevel();
    listLevel();
});


