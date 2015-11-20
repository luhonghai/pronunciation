
function TestWord(){
    $(document).on("click","#test",function(){
        $("#alphabet").empty();
        $("#score").empty();
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
                if(data.data!=null){
                    var result=data.data.phonemeScores;

                    for(var i = 0; i < result.length; i++){
                        var listFrame=result[i].phonemes;
                        $("#alphabet").append('<input class="alphabet" readonly="readonly" index="'+result[i].index+'" value="'+result[i].name+'"  type="text">');
                        $("#score").append('<input class="score" readonly="readonly" index="'+result[i].index+'" value="'+result[i].totalScore+'"  type="text">');
                        $(".alphabet").css('width','100px');
                        $(".score").css('width','100px');

                        $("#alphabets").append('<input id="'+i+'" readonly="readonly" index="'+result[i].index+'" value="'+result[i].name+'"  type="text">');
                        if(listFrame.length>0){
                            for(var j = 0; j < listFrame.length; j++){
                                $("#listAlpabet").append('<input class="listAlpabet" readonly="readonly" index="'+listFrame[j].index+'" value="'+listFrame[j].name+'"  type="text">');
                                $("#count").append('<input class="count" readonly="readonly" index="'+listFrame[j].index+'" value="'+listFrame[j].count+'"  type="text">');
                                $(".listAlpabet").css('width','50px');
                                $(".count").css('width','50px');
                            }
                        }else{
                            $("#listAlpabet").append('<input class="listAlpabets" readonly="readonly" type="text">');
                            $("#count").append('<input class="counts" readonly="readonly" type="text">');
                            $(".listAlpabets").css('width','50px');
                            $(".counts").css('width','50px');
                        }
                        $("#totalScore").append('<input id="'+i+'s" readonly="readonly" index="'+result[i].index+'" value="'+result[i].totalScore+'"  type="text">');
                        if(listFrame.length>0){
                            $("#"+i+"").css({"width":(listFrame.length)*50});
                            $("#"+i+"s").css({"width":(listFrame.length)*50});
                        }else{
                            $("#"+i+"").css('width','50px');
                            $("#"+i+"s").css('width','50px');
                        }


                    }
                }
                $("#test").attr("disabled",false);


            }

        });


    });
}


$(document).ready(function(){
    $('#audio').fileinput({
        showUpload : false,
        allowedFileExtensions : ['mp3', 'wav']
    });
    TestWord();

});
