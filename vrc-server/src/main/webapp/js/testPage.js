
function TestWord(){
    $(document).on("click","#test",function(){
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
