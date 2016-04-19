
function helpClass(){
    $(document).on("click","#help-icons",function() {
        $("#helpClass").modal('show');
    });
}
$(document).ready(function(){
    $('#help-icons').show();
    helpClass();
});

