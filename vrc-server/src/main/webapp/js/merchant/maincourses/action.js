/**
 * Created by lantb on 2016-02-02.
 */
var progress;
function makeOnceCheckboxSelected(){
    $('input[type="checkbox"]').on('change', function() {
        $('input[type="checkbox"]').not(this).prop('checked', false);
    });
}

function saveCourse(){
    $(document).on("click","#btnSaveCourse", function(){
        if(validateFormAddCourse()){
            addCourse();
        }
    });

}

function openPopup(){
    $(document).on("click","#open_popup",function(){
        clearFormAdd();
        getPoupAdd().modal('show');
    });
}

function searchHeader(){
    $(document).on("click","#btnSaveCourse", function(){

    });
}


function searchDetail(){
    $(document).on("click","#btnSaveCourse", function(){

    });
}

$(document).ready(function(){
    makeOnceCheckboxSelected();
    saveCourse();
    openPopup();
    loadAllCourse();
});
