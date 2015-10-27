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


/**
 * clear all field
 */
function clearForm(){
    $("input.form-control").each(function(){
       $(this).val("");
    });
    $("textarea.form-control").each(function(){$(this).val("");});
    $('#type').multiselect('destroy');
    $("#type").empty();
    $("#ipa").removeAttr("readonly");
    $("#submitForm").removeAttr("id_mapping");
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
    $("#arpabet").val(data.arpabet);
    $("#ipa").val(data.ipa);
    $("#description").val(data.description);
    $("#tip").val(data.tip);
    $("#addColor").colorpicker('setValue', data.color);
    $("#mp3").val(data.mp3Url);
    $("#index_type").val(data.indexingType);
    $("#words").val(data.words);
    $("#ipa").attr("readonly","true");
    initSelect(data.type);
}
/**
 *
 */
function initColorPicker(){
    $("#addColor").colorpicker();
}

