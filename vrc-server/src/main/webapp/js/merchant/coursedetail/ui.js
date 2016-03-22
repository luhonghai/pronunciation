/**
 * Created by lantb on 2016-02-02.
 */


function drawWord(data){
    getAddWord().attr("idWord", data.idWord);
    getListPhonemes().html("");
    getListIPA().html("");
    getListWeight().html("");
    getAddWord().attr("readonly","readonly");
    $.each(data.listWeightPhoneme, function (idx, obj) {
        var phonemeName = obj.phoneme;
        var weightOfPhoneme = obj.weight;
        //alert(jsonItem);

        getListPhonemes().append('<input readonly="readonly" index="'+obj.index+'" value="'+phonemeName+'"  type="text">');
        getListIPA().append('<input readonly="readonly" index="'+obj.index+'" value="'+obj.ipa+'"  type="text">');
        getListWeight().append('<input onkeypress="return isNumberKey(event,this)" id="weight'+obj.index+'" class="phoneme-weight" value="'+weightOfPhoneme+'" type="text">');
        getListPhonemes().css({"width":(idx+1)*35});
        getListWeight().css({"width":(idx+1)*35});
        getListIPA().css({"width":(idx+1)*35});
    });
    $("#wordModal1").show();
    $("#wordModal2").show();
}