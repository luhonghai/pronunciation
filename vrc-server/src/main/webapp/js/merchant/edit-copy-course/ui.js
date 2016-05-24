/**
 * Created by lantb on 2016-02-02.
 */


function drawWord(data){
    getAddWord().attr("idWord", data.idWord);
    getListPhonemes().html("");
    getListIPA().html("");
    getListWeight().html("");
    getAddWord().attr("readonly","readonly");
    getAddWord().attr("check-duplicated", "not check");
    getAddWord().val(data.nameWord);
    $.each(data.listWeightPhoneme, function (idx, obj) {
        var phonemeName = obj.phoneme;
        var weightOfPhoneme = obj.weight;
        //alert(jsonItem);
        getListPhonemes().append('<input disabled="disabled" readonly="readonly" index="'+obj.index+'" ipa="'+obj.ipa+'" value="'+phonemeName+'"  type="text">');
        getListIPA().append('<input disabled="disabled" readonly="readonly" index="'+obj.index+'" value="'+obj.ipa+'"  type="text">');
        getListWeight().append('<input  onkeypress="return isNumberKey(event,this)" id="weight'+obj.index+'" class="phoneme-weight" value="'+weightOfPhoneme+'" type="text">');
        getListPhonemes().css({"width":(idx+1)*35});
        getListWeight().css({"width":(idx+1)*35});
        getListIPA().css({"width":(idx+1)*35});
    });
    getPhonemeLable().html("arpabet:");
    getWeightLable().html("weight:");
    getIPAlable().html("ipa:");
    $("#wordModal1").show();
    $("#wordModal2").show();
}

function drawPhonemeOfWord(data){
    $.each(data.phonemes, function (idx, obj) {
        var phonmeName = obj.phoneme;
        //alert(jsonItem);
        getListPhonemes().append('<input disabled="disabled" readonly="readonly" index="'+obj.index+'" ipa="'+obj.ipa+'" value="'+phonmeName+'"  type="text">');
        getListIPA().append('<input disabled="disabled" readonly="readonly" index="'+obj.index+'" value="'+obj.ipa+'"  type="text">');
        getListWeight().append('<input onkeypress="return isNumberKey(event,this)" id="weight'+obj.index+'" class="phoneme-weight" type="text">');
        getListPhonemes().css({"width":(idx+1)*35});
        getListWeight().css({"width":(idx+1)*35});
        getListIPA().css({"width":(idx+1)*35});
    });
}