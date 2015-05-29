
function login(){
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
        url: "Login",
        type:"POST",
        dataType:"text",
        data:{
            login1:"login",
            account:account,
            pass:pass
        },
        success :function(result){
            if(result=="success"){
               window.location =CONTEXT_PATH + "/dashboard.jsp";
               // window.location = "/dashboard.jsp";
            }
           if(result=="error"){
               //window.location ="/login.jsp";
               window.location ="/login.jsp";
           }
           },
        error:function(){
            alert("loi");
        }

        })



}
function loginButton(){
    $(document).on("click","#login", function () {
        login();
    });
}
function loginEnter(){
    $(document).on("keyup","#password", function () {
        if(e.keyCode == 13)
        {
           login();
        }
    });
}

$(document).ready(function(){
    loginButton();
    loginEnter();
});

