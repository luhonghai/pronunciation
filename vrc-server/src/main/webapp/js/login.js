
function login(){
    $(document).on("click","#login", function (){
    var account=$("#account").val();
    var pass=$("#password").val();
        if (account==null || account=="") {

            $("#result").html("<strong>ERROR:</strong> Please enter username!");
            $("#result").css({"display": "block"});
            $("#account").focus();
            return false;
        }
        if (pass==null || pass=="") {

            $("#result").html("<strong>ERROR:</strong> Please enter password!");
            $("#result").css({"display": "block"});
            $("#password").focus();
            return false;
        }

    $.ajax({
        url: "../Login",
        type:"POST",
        dataType:"text",
        data:{
            login1:"login",
            account:account,
            pass:pass
        },
        success :function(result){
            if(result=="success"){
                window.location = "../dashboard.jsp";
            }
           if(result=="error"){
               window.location = "../login.jsp";
           }
           },
        error:function(){
            alert("loi");
        }

        })


    });
}
$(document).ready(function(){
    login();
});

