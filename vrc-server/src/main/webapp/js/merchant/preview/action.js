/**
 * Created by lantb on 2016-03-30.
 */
function beforeShowPreview(){
    $('#preview-popup').on('show.bs.modal', function () {
        loadContent(targetLoadCourse,idCourse);
    });
}

function loadPreviewLevel(){
    $(document).on('click','.level',function(){
        loadContent(targetLoadLevel,$(this).attr('id'));
    });
}
function loadPreviewObjective(){
    $(document).on('click','.obj',function(){
        loadContent(targetLoadObj,$(this).attr('id'));
    });
}

function loadPreviewLesson(){
    $(document).on('click','.lesson',function(){
        loadContent(targetLoadLesson,$(this).attr('id'));
    });
}
function slideBackPreview(){
    $(document).on('click','.slide-back',function(){
        showScreen($(this).attr('back'));
    });
}



$(document).ready(function(){
    beforeShowPreview();
    loadPreviewLevel();
    loadPreviewObjective();
    loadPreviewLesson();
    slideBackPreview();
});