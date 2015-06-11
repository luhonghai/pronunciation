function getCouse(){
    $.ajax({
        url:"Dashboard",
        type:"POST",
        dataType:"json",
        data:{
            list:"list"
        },
        success:function(result){
            $("#feedback").text(result.x);
            $("#user").text(result.y);
            $("#pronunciation").text(result.z);
            $("#license").text(result.t);
        }
    });
}

$(document).ready(function(){

   getCouse();
});