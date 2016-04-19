
function help(){
    $(document).on("click","#help-icons",function() {
        $("#helpReportModal").modal('show');
    });
}
$(document).ready(function(){
    $('#help-icons').show();
    help();
});

