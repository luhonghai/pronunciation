function appDetail(){
    $.ajax({
        url:"AppDetails",
        type:"POST",
        dataType:"json",
        data:{
            list:"list"
        },
        success:function(result){

                $("#noAccessMessage").val(result[0].noAccessMessage);
                $("#id").val(result[0].id);
                if(result[0].registration==true){
                    $("#checkbox").attr('checked',"checked");
                }
                if(result[0].registration==false){
                    $("#checkbox").removeAttr('checked');

                }
            }

    });

}
function save(){
    $(document).on("click","#save",function(){
        var message=$("#noAccessMessage").val();
        var id=$("#id").val();
        var regis;
        if($("#checkbox").is(':checked')){
            regis=true;
        }
        else{
            regis=false;
        }
        $.ajax({
            url:"AppDetails",
            type:"POST",
            dataType:"text",
            data:{
                save:"save",
                id:id,
                message:message,
                regis:regis
            },
            success:function(data){
                if(data=="success"){
                    appDetail();
                }

            }

        });


    });
}

$(document).ready(function(){
    save();
    appDetail();
});
