/**
 * Created by CMGT400 on 10/5/2015.
 */
var myTable;
var servletName="ManagementLevelOfCourseServlet";

function listLevel(){
    var $selected=$("#level");
    $('#company option[value!="-1"]').remove();
    $.ajax({
        "url": servletName,
        "type": "POST",
        "dataType":"json",
        "data": {
            action: "listLevel"
        },
        success:function(data){
            var items=data;
            $(items).each(function(){
                var newOption = '<option value="' + this.companyName + '">' + this.companyName + '</option>';
                $selected.append(newOption);
            });
            $("#level").append($("#company option").remove().sort(function(a, b) {
                var at = $(a).text(), bt = $(b).text();
                return (at > bt)?1:((at < bt)?-1:0);
            }));


        }

    });
}


function loadAudio(){
    $('.cp-jplayer').each(function() {
        var id = $(this).attr('id');
        var audioUrl = $(this).attr('audioUrl');
        new CirclePlayer("#" + id,
            {
                mp3: audioUrl
                // wav: audioUrl + "&type=wav"
            }, {
                cssSelectorAncestor: '#' + id + 's'
            });

    });

}

function getWeightAndPhoneme(listPhonemeName, listWeightName){
    var output = [];
    $(listPhonemeName).find('input').each(function(e){
        var value = $(this).val();
        var index = $(this).attr("index");
        var weight = $(listWeightName + index).val();
        output.push({
            index : parseInt(index),
            phoneme : value,
            weight : parseInt(weight)
        });
    });
    return output;
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
    $("#loadPhonemes").click(function(){
        $("#loadPhonemes").attr("disabled",true);
        var word = $("#addWord").val();
        if (word == null || typeof word == "undefined" || word.length == 0){
            $("#loadPhonemes").attr("disabled",false);
            $("#addWord").focus();
            swal("Warning!", "Word not null!", "warning");
            return;
        }
        $.ajax({
            url: servletName,
            type: "POST",
            dataType: "json",
            data: {
                listPhonemes: "listPhonemes",
                word: word
            },
            success: function (data) {
                var message = data.message;
                if(message.indexOf("success") != -1){
                    $("#addWord").attr("idWord", data.id);
                    //$("#loadPhonemes").attr("disabled",true);
                    $(".phoneme-lable").html("Phonemes:");
                    $(".weight-lable").html("WeightPhonemes:");
                    $("#listPhonmes").html("");
                    $("#listWeight").html("");
                    //$("#addWord").attr("readonly","readonly");
                    $("#addWord").attr("disabled",true);
                    $.each(data.phonemes, function (idx, obj) {
                        var phonmeName = obj.phoneme;
                        //alert(jsonItem);
                        $("#listPhonmes").append('<input readonly="readonly" index="'+obj.index+'" value="'+phonmeName+'"  type="text">');
                        $("#listWeight").append('<input onkeypress="return isNumberKey(event,this)" id="weight'+obj.index+'" class="phoneme-weight" type="text">');
                        $("#listPhonmes").css({"width":(idx+1)*35});
                        $("#listWeight").css({"width":(idx+1)*35});
                    });
                    $("#yesadd").attr("disabled", false);
                }else{
                    $("#loadPhonemes").attr("disabled",false);
                    $("#listPhonmes").html("");
                    $("#listWeight").html("");
                    $(".phoneme-lable").html("");
                    $(".weight-lable").html("");
                    $("#yesadd").attr("disabled", true);
                    $("#addWord").focus();
                    swal("Error!",message.split(":")[1], "error");
                }
            },
            error: function () {
                swal("Error!", "Could not connect to server", "error");
            }

        });
    });
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
    getIdQuestion()
    openPopupAdd();
    addWord();
    openPopupDeletes();
    deleteWord();
    openPopupEdit();
    editWord();
    listWordOfQuestion();
});


