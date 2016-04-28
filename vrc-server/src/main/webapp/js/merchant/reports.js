
function help(){
    $(document).on("click","#help-icons",function() {
        $("#helpReportModal").modal('show');
    });
}
function collapseMenu(){
    $("#li-reports").find('ul').addClass('in');
}
$(document).ready(function(){
    collapseMenu();
    $('#help-icons').show();
    help();
});

