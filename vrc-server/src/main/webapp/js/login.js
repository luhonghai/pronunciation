
function login(){
    $("#result").empty();
    var account=$("#account").val();
    var pass=$("#password").val();
        if (account==null || account=="") {

            $("#result").html('<div class="alert alert-danger alert-dismissible" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button><strong>Missing data!</strong> Please enter your email account</div>');
            $("#result").show();
            $("#account").focus();
            return false;
        }
        if (pass==null || pass=="") {

            $("#result").html('<div class="alert alert-danger alert-dismissible" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button><strong>Missing data!</strong> Please enter your password</div>');
            $("#result").show();
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
            } else {
                $("#result").html('<div class="alert alert-danger alert-dismissible" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button><strong>Could not login!</strong> Invalid email or password</div>');
                $("#result").show();
                $("#account").focus();
            }
           },
        error:function(){
            $("#result").html('<div class="alert alert-danger alert-dismissible" role="alert"><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button><strong>Error!</strong> Could not connect to server</div>');
            $("#result").show();
            $("#account").focus();
        }

        })



}
function loginButton(){
    $(document).on("click","#login", function () {
        login();
    });
}
function loginEnter(){
    $(document).on("keyup","#password", function (e) {
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

