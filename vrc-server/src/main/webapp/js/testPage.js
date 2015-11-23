var nbPhoneme;
function TestWord(){
    $(document).on("click","#test",function(){
        //$("#alphabet").empty();
        //$("#score").empty();
        $("#alphabets").empty();
        $("#listAlpabet").empty();
        $("#count").empty();
        $("#totalScore").empty();
        var form = $("#testform");
        var formdata = false;
        if (window.FormData) {
            formdata = new FormData(form[0]);
        }
        formdata.append("test", "test");
        $.ajax({
            url:"TestPage",
            type: "POST",
            dataType: "json",
            data: formdata ? formdata : form.serialize(),
            cache: false,
            contentType: false,
            processData: false,
            success:function(data){
                nbPhoneme = data.neighbourPhones;
                if(data.data!=null){
                    var result=data.data.phonemeScores;

                    for(var i = 0; i < result.length; i++){
                        var score=result[i].totalScore;
                        var listFrame=result[i].phonemes;
                        //$("#alphabet").append('<input class="alphabet" readonly="readonly" index="'+result[i].index+'" value="'+result[i].name+'"  type="text">');
                        //$("#score").append('<input class="score" readonly="readonly" index="'+result[i].index+'" value="'+result[i].totalScore+'"  type="text">');
                        //$(".alphabet").css('width','100px');
                        //$(".score").css('width','100px');

                        $("#alphabets").append('<input id="'+i+'" readonly="readonly" index="'+result[i].index+'" value="'+result[i].name+'"  type="text">');
                        if(listFrame.length>0){
                            for(var j = 0; j < listFrame.length; j++){
                                var test=testNbPhoneme(result[i].name,listFrame[j].name);
                                $("#listAlpabet").append('<input class="listAlpabet" readonly="readonly" index="'+listFrame[j].index+'" value="'+listFrame[j].name+'"  type="text">');
                                $("#count").append('<input class="count" readonly="readonly" index="'+listFrame[j].index+'" value="'+listFrame[j].count+'"  type="text">');
                                if(test=0){
                                    $(".listAlpabet").css({'width':'50px', 'text-align':'center', 'color': 'red'});
                                }else if(test=1){
                                    $(".listAlpabet").css({'width':'50px', 'text-align':'center', 'color':'green'});
                                }else{
                                    $(".listAlpabet").css({'width':'50px', 'text-align':'center', 'color':'orange'});
                                }

                                $(".count").css({'width':'50px', 'text-align':'center'});
                            }
                        }else{
                            $("#listAlpabet").append('<input class="listAlpabets" readonly="readonly" type="text">');
                            $("#count").append('<input class="counts" readonly="readonly" type="text">');
                            $(".listAlpabets").css('width','50px');
                            $(".counts").css('width','50px');
                        }
                        $("#totalScore").append('<input id="'+i+'s" readonly="readonly" index="'+result[i].index+'" value="'+result[i].totalScore+'"  type="text">');
                        if(listFrame.length>0){
                            $("#"+i+"").css({"width":(listFrame.length)*50, 'text-align':'center'});
                            if(score<45){
                                $("#"+i+"s").css({"width":(listFrame.length)*50, 'text-align':'right', 'color': 'red'});
                            }else if(45<=score && score<=80){
                                $("#"+i+"s").css({"width":(listFrame.length)*50, 'text-align':'right', 'color': 'orange'});
                            }else{
                                $("#"+i+"s").css({"width":(listFrame.length)*50, 'text-align':'right', 'color': 'green'});
                            }

                        }else{
                            $("#"+i+"").css({'width':'50px', 'text-align':'center'});
                            $("#"+i+"s").css({'width':'50px', 'text-align':'right'});
                        }


                    }
                }
                $("#test").attr("disabled",false);


            }

        });


    });
}

function testNbPhoneme(phoneme, testPhoneme) {
    if (phoneme == testPhoneme) return 1;
    if (typeof nbPhoneme != 'undefined' && nbPhoneme != null) {
        var phonemes = nbPhoneme[phoneme.toLowerCase()];
        if (typeof phonemes != 'undefined' && phonemes != null && phonemes.length > 0) {
            for (var i = 0; i < phonemes.length; i++) {
                if (testPhoneme.toUpperCase() == phonemes[i].toUpperCase()) return 2;
            }
        }
    }
    return 0;
}

$(document).ready(function(){
    $('#audio').fileinput({
        showUpload : false,
        allowedFileExtensions : ['mp3', 'wav']
    });
    TestWord();

});
