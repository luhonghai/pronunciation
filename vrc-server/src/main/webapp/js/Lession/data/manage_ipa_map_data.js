/**
 * Created by CMGT400 on 10/8/2015.
 */
/**
 *
 * @param option
 */
function initSelect(option){
    var $vowel = $("<option>");
    $vowel.attr("value","vowel");
    $vowel.text("vowel");
    var $consonant = $("<option>");
    $consonant.attr("value","consonant");
    $consonant.text("consonant");
    if(option=="vowels"){
        $vowel.attr('selected', 'selected');
    }else if(option == "consonant"){
        $consonant.attr('selected', 'selected');
    }
    $("#type").append($vowel);
    $("#type").append($consonant);
    $('#type').multiselect();

}

/**
 * init date
 */
function initDate(){
    $('#CreateDateFrom').datetimepicker({
        format: 'DD/MM/YYYY'
    });

    $('#CreateDateTo').datetimepicker({
        format: 'DD/MM/YYYY'
    });
}
function isNumberKey(evt,e) {
    var charCode = (evt.which) ? evt.which : event.keyCode;
    if (charCode != 46 && charCode > 31
        && (charCode < 48 || charCode > 57)) {
        return false;
    }
}

/**
 * clear all field
 */
function clearForms(){
    $("input.form-control").each(function(){
       $(this).val("");
    });
    $("textarea.form-control").each(function(){$(this).val("");});
    $("form").find("#imageTongue").fileinput('destroy');
    $("form").find("#imageLips").fileinput('destroy');
    $("form").find("#imageJaw").fileinput('destroy');
    $('#type').multiselect('destroy');
    $("#type").empty();
    $("#ipa").removeAttr("readonly");
    $("#submitForm").removeAttr("id_mapping");
}

function validateForm(){
    var arpabet = $("#arpabet").val();
    var ipa = $("#ipa").val();
    if(typeof ipa == undefined || ipa=="" || ipa.length == 0){
        swal("Warning!", "Please enter value for IPA", "warning");
        return false;
    }
    if(typeof arpabet == undefined || arpabet == "" || arpabet.length == 0){
        swal("Warning!", "Please enter value for Arpabet", "warning");
        return false;
    }
    return true;
}
/**
 * get data form
 * @returns {{id: (*|jQuery), arpabet: (*|jQuery), ipa: (*|jQuery), description: (*|jQuery), tip: (*|jQuery), color: (*|jQuery), type: (*|jQuery), indexingType: (*|jQuery), mp3Url: (*|jQuery), words: (*|jQuery)}}
 */
function getDataForm(){
    var id = $("#submitForm").attr("id_mapping");
    if (typeof id === undefined) {
       id = "";
    }
    var arpabet = $("#arpabet").val();
    var ipa = $("#ipa").val();
    var description = $("#description").val();
    var tip = $("#tip").val();
    var color = $("#addColor").val();
    var type = $("#type").val();
    var mp3Url = $("#mp3").val();
    var index_type = $("#index_type").val();
    var words = $("#words").val();
    var dto = {
        id : id,
        arpabet :arpabet,
        ipa : ipa,
        description : description,
        tip : tip,
        color : color,
        type : type,
        indexingType : index_type,
        mp3Url : mp3Url,
        words : words
    };
    return dto;
}

/**
 *
 * @param data
 */
function includeDataForm(data){
    $("#submitForm").attr("action","edit");
    $("#submitForm").attr("id_mapping",data.id);
    $("#submitForm").attr("imgTongue",data.imgTongue);
    $("#submitForm").attr("imgLips",data.imgLip);
    $("#submitForm").attr("imgJaw",data.imgJaw);
    $("#arpabet").val(data.arpabet);
    $("#ipa").val(data.ipa);
    $("#description").val(data.description);
    $("#tip").val(data.tip);
    $("#addColor").colorpicker('setValue', data.color);
    $("#mp3").val(data.mp3Url);
    $("#mp3short").val(data.mp3UrlShort);
    $("#index_type").val(data.indexingType);
    $("#words").val(data.words);
    $("#tongueText").val(data.textTongue);
    $("#lipsText").val(data.textLip);
    $("#jawText").val(data.textJaw);
    if(data.imgTongue!=null){
        $("#imgTongue-edit").attr("src",data.imgTongue);
    }else{
        $("#imgTongue-edit").attr("src",'');
    }
    if(data.imgLip!=null){
        $("#imgLips-edit").attr("src",data.imgLip);
    }else{
        $("#imgLips-edit").attr("src",'');
    }
    if(data.imgJaw!=null){
        $("#imgJaw-edit").attr("src",data.imgJaw);
    }else{
        $("#imgJaw-edit").attr("src",'');
    }


    $("#ipa").attr("readonly","true");
    initSelect(data.type);
}
/**
 *
 */
function initColorPicker(){
    $("#addColor").colorpicker();
}

