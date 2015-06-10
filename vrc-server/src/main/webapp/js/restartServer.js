function restart(){
    $(document).on("click","#restart",function(){
        $.ajax({
            url:"AppDetails",
            type:"POST",
            dataType:"text",
            data: {
                restart: "restart"
            },
            success:function(data){
                if(data=="success"){
                    appDetail();
                }
                if(data=="error"){
                    alert("You can not edit information!");
                    appDetail();
                    $("#save").attr("disabled",'disabled');
                }

            }

        });


    });
}
function button(){
    var visible=$("#roles").val();
    if(visible=="1"){
        $("#server").show();
    }
}
$(document).ready(function(){
    button();
    restart();

});

