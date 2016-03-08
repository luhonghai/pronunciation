/**
 * Created by lantb on 2016-02-02.
 */
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

$(document).ready(function(){
    makeOnceCheckboxSelected();
    saveCourse();
});
