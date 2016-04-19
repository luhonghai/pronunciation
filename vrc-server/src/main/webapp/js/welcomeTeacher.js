function help(){
    $(document).on("click","#HelpPage",function() {
        $("#helpMyStudent").modal('show');
    });
}
$(document).ready(function(){
    $('#help-icons').show();;
    help();
});

