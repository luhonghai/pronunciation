function clickTopHelp(){
    $(document).on("click","#help-icons", function(){
        getPopUpHelp().find(".modal-body").empty();
        getPopUpHelp().find(".modal-title").html("teachers’ console");
        getPopUpHelp().find(".modal-body").html(initHelpTopDataMyCourse());
        getPopUpHelp().modal('show');
    });
}
$(document).ready(function(){
    $('#help-icons').show();;
    help();
});

