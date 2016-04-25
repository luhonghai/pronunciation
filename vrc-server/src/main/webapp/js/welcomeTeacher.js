function clickTopHelp(){
    $(document).on("click","#help-icons", function(){
        getPopUpHelp().find(".modal-body").empty();
        getPopUpHelp().find(".modal-title").html("teachers’ console");
        getPopUpHelp().find(".modal-body").html(initHelpTopWelcome());
        getPopUpHelp().modal('show');
    });
}
function getPopUpHelp(){
    return $("#help-popup");
}
$(document).ready(function(){
    $('#help-icons').show();
    clickTopHelp();
});

