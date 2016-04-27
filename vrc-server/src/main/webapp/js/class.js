
function helpClass(){
    $(document).on("click","#help-icons",function() {
        $("#helpClass").modal('show');
    });
}
function collapseMenu(){
    $("#li-class").find('ul').addClass('in');
}
$(document).ready(function(){
    $('#help-icons').show();
    collapseMenu();
    helpClass();
});

