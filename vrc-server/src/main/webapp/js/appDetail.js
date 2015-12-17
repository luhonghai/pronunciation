function appDetail(){
    $.ajax({
        url:"AppDetails",
        type:"POST",
        dataType:"json",
        data:{
            list:"list"
        },
        success:function(result) {
            if (result.valueOf() != null) {
                $("#noAccessMessage").val(result.noAccessMessage);
                $("#id").val(result.idAppDetail);
                $("#idnumber").val(result.idNumberDate);
                $("#numberDate").val(result.numberDate);
                if (result.registration == true) {
                    $("#checkbox").attr('checked', "checked");
                }
                if (result.registration == false) {
                    $("#checkbox").removeAttr('checked');

                }
            }else{
                swal("Error!", "Could not edit information", "error");
            }
        }

    });

}
function save(){
    $(document).on("click","#save",function(){
        var message=$("#noAccessMessage").val();
        var id=$("#id").val();
        var idnumber=$("#idnumber").val();
        var numberDate=$("#numberDate").val();
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
            data: {
                save: "save",
                id: id,
                idnumber:idnumber,
                message: message,
                regis: regis,
                numberDate:numberDate

            },
            success:function(data){
                if(data=="success"){
                    appDetail();
                    swal("Success!", "Save success!", "success");
                }
                if(data=="error"){
                    swal("Error!", "Could not edit information", "error");
                    appDetail();
                    $("#save").attr("disabled",'disabled');
                }

            }

        });


    });
}
function buttons(){
    var visible=$("#role").val();
    if(visible=="2"){
        $("#save").attr("disabled",'disabled');
    }
}

$(document).ready(function(){

    buttons();
    save();
    appDetail();
});
