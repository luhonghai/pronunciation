var nbPhoneme;
var beepPhonemes;
var cGreen = '#579e11';
var cOrange = '#ff7548';
var cRed = '#ff3333';
var cPurple = '#7030a0';
var cBlue = '#003da7';
function TestWord(){
    $(document).on("click","#test",function(){
        $("#test").attr("disabled",true);
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
        $("#bestPhoneme").empty();
        $.ajax({
            url:"TestPage",
            type: "POST",
            dataType: "json",
            data: formdata ? formdata : form.serialize(),
            cache: false,
            contentType: false,
            processData: false,
            success:function(data){
                $("#test").attr("disabled",false);
                nbPhoneme = data.neighbourPhones;
                beepPhonemes = data.beepPhonemes;
                if(data.data!=null){
                    var result=data.data.phonemeScores;
                    //$("#alphabets").append('<b>Appended text: </b>');
                    //$("#listAlpabet").append('<b>Appended text: </b>');
                    //$("#count").append('<b>Appended text: </b>');
                    //$("#totalScore").append('<b>Appended text: </b>');
                    var bestPhoneme=data.data.rawBestPhonemes;
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
                                var test=testNbPhoneme(listFrame[j].name,result[i].name);
                                $("#listAlpabet").append('<input id="'+i+''+j+'t" readonly="readonly" index="'+listFrame[j].index+'" value="'+listFrame[j].name+'"  type="text">');
                                $("#count").append('<input class="count" readonly="readonly" index="'+listFrame[j].index+'" value="'+listFrame[j].count+'"  type="text">');
                                if (isBeepPhonemes(result[i].name, listFrame[j].name)) {
                                    $("#"+i+ +j+"t").css({'width':'50px', 'font-weight': 'bold', 'text-align':'center', 'color': cPurple});
                                } else {
                                    if(test==0){
                                        $("#"+i+ +j+"t").css({'width':'50px','font-weight': 'bold', 'text-align':'center', 'color': cRed});
                                    }else if(test==1){
                                        $("#"+i+ +j+"t").css({'width':'50px','font-weight': 'bold', 'text-align':'center', 'color': cGreen});
                                    }else{
                                        $("#"+i+ +j+"t").css({'width':'50px','font-weight': 'bold','text-align':'center', 'color':cOrange});
                                    }
                                }
                                $(".count").css({'width':'50px','font-weight': 'bold', 'text-align':'center'});
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
                                $("#"+i+"s").css({"width":(listFrame.length)*50, 'text-align':'right', 'color': cRed});
                            }else if(45<=score && score<=80){
                                $("#"+i+"s").css({"width":(listFrame.length)*50, 'text-align':'right', 'color': cOrange});
                            }else{
                                $("#"+i+"s").css({"width":(listFrame.length)*50, 'text-align':'right', 'color': cGreen});
                            }

                        }else{
                            $("#"+i+"").css({'width':'50px','font-weight': 'bold', 'text-align':'center'});
                            $("#"+i+"s").css({'width':'50px','font-weight': 'bold', 'text-align':'right'});
                        }
                    }
                    $("#totalScore").append('<input class="counts" readonly="readonly" type="text" value="Score ='+data.data.score+'">');
                    $("#bestPhoneme").empty();
                    for(var i=0;i<bestPhoneme.length;i++){
                        $("#bestPhoneme").append('<input id="'+i+'b" readonly="readonly" value="'+bestPhoneme[i]+'" type="text"/><br/>');
                    }

                }else if(data.message=="notExist"){
                    swal("Error!", "Word not exist", "error");
                }

            },
            error:function(){
                swal("Error!", "Could not connect to server", "error");
                $("#test").attr("disabled",false);
            }
        });


    });
}

function isBeepPhonemes(phoneme, testPhoneme) {
    var test = beepPhonemes[phoneme];
    return (typeof test != 'undefined'
            && test != null
            && test.length > 0
            && test.toUpperCase() == testPhoneme.toUpperCase());
}

function testNbPhoneme(phoneme, testPhoneme) {
    var returnVal = 0;
    if (phoneme == testPhoneme) {
        returnVal = 1;
    }
    var mBeepPhoneme = beepPhonemes[testPhoneme];
    var mPhoneme = testPhoneme;
    if (typeof mBeepPhoneme != 'undefined' && mBeepPhoneme != null && mBeepPhoneme.length > 0) {
        mPhoneme = mBeepPhoneme;
    }
    if (typeof nbPhoneme != 'undefined' && nbPhoneme != null) {
        var phonemes = nbPhoneme[mPhoneme.toUpperCase()];
        if (typeof phonemes != 'undefined' && phonemes != null && phonemes.length > 0) {
            for (var i = 0; i < phonemes.length; i++) {
                if (phoneme.toUpperCase() == phonemes[i].toUpperCase()) {
                    returnVal = 2;
                    break;
                }
            }
        }
    }
    return returnVal;
}

$(document).ready(function(){
    $('#audio').fileinput({
        showUpload : false,
        allowedFileExtensions : ['wav']
    });
    TestWord();

});
