
function send(){
    $(document).on("click","#send",function(){
        var mail=$("#listmail").val();
        var teacher=$("#merchant").val();
        var obj= {
                    listmail : readListMail(mail),
                    teacher : teacher
                }
        if(mail!=null && mail.length>0) {
            $.ajax({
                url: "SendMailUser",
                type: "POST",
                dataType: "text",
                data: {
                    action: "send",
                    listmail: JSON.stringify(obj)
                },
                success: function (data) {
                    if (data.message == "success") {
                        swal("Success!", "Send success!", "success");
                    } else {

                    }

                }

            });
        }else{
            swal("Warning!", "List email not null!", "warning");
        }


    });
}
function readListMail(txt) {
    if (txt == null || typeof txt == 'undefined' || txt.length == 0) return null;
    var data =  txt.split(" ");
    var output = [];
    for (var i = 0; i < data.length; i++) {
        output.push(data[i]);

    }
    return output;
}


$(document).ready(function(){
    send();
});
