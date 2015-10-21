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
        var txtLevel=$("#level").val();
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

function openPopupDeletes(){
    $(document).on("click","#delete", function(){
        $("#deletes").modal('show');
        var idd=$(this).attr('id-column');
        $("#iddelete").val(idd);
    });
}

function deleteWord(){
    $(document).on("click","#deleteItems", function(){
        var idWord =  $("#iddelete").val();
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "json",
            data: {
                delete: "delete",
                idWord: idWord,
                idQuestion: $("#id-question").html()
            },
            success: function (data) {
                var messages=data.message;
                if (messages.indexOf("success") != -1) {
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#deletes").modal('hide');
                }else{
                    swal("Error!",message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    });
}

function openPopupEdit(){
    $(document).on("click","#edit", function() {
        $("#edits").modal('show');
        var idWord = $(this).attr('id-column');
        var word = $(this).attr('word');
        $("#idedit").val(idWord);
        $("#editWord").val(word);

        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "json",
            data: {
                listPhonemesEdit: "listPhonemesEdit",
                idWord: idWord,
                idQuestion: $("#id-question").html()
            },
            success: function (data) {
                var message = data.message;
                if(message.indexOf("success") != -1){
                    $("#editWord").attr("idWord", idWord);
                    $("#listPhonmesEdit").html("");
                    $("#listWeightEdit").html("");
                    $("#editWord").attr("readonly","readonly");
                    $.each(data.listWeightPhoneme, function (idx, obj) {
                        var phonemeName = obj.phoneme;
                        var weightOfPhoneme = obj.weight;
                        //alert(jsonItem);

                        $("#listPhonmesEdit").append('<input readonly="readonly" index="'+obj.index+'" value="'+phonemeName+'"  type="text">');
                        $("#listWeightEdit").append('<input onkeypress="return isNumberKey(event,this)" id="weight-edit'+obj.index+'" class="phoneme-weight" value="'+weightOfPhoneme+'" type="text">');
                        $("#listPhonmesEdit").css({"width":(idx+1)*35});
                        $("#listWeightEdit").css({"width":(idx+1)*35});
                    });
                    //$("#yesadd").show();
                }else{
                    swal("Error!",message.split(":")[1], "error");
                    //$("#listPhonmes").html("");
                    //$("#listWeight").html("");
                    //$("#yesadd").hide();
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });

    });

}

function editWord(){
    $(document).on("click","#yesedit", function(){
        var txtWord=$("#editWord").val();
        if (txtWord == null || typeof txtWord == "undefined" || txtWord.length == 0){
            swal("Warning!", "Word not null!", "warning");
            return;
        }
        var word = {
            idWord: $("#editWord").attr("idWord"),
            idQuestion: $("#id-question").html(),
            data:getWeightAndPhoneme("#listPhonmesEdit","#weight-edit")
        };
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "json",
            data: {
                edit: "edit",
                word: JSON.stringify(word)
            },
            success: function (data) {
                var message = data.message;
                if(message.indexOf("success")!=-1){
                    $("tbody").html("");
                    myTable.fnDraw();
                    $("#edits").modal('hide');
                }else{
                    swal("Error!",message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });


    });
}

$(document).ready(function(){
    //getIdQuestion()
    //openPopupAdd();
    //addWord();
    //openPopupDeletes();
    //deleteWord();
    //openPopupEdit();
    //editWord();
    //listWordOfQuestion();
    addLevel();
    listLevel();
});


