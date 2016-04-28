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
function loadPreviewTest(){
    $(document).on('click','.test',function(){
        loadContent(targetLoadTest,$(this).attr('id'));
    });
}
function slideBackPreview(){
    $(document).on('click','.slide-back',function(){
        showScreen($(this).attr('back'));
    });
}
function clickCircleQuestion(){
    $(document).on('click','.circle-question',function(){
        var isFromTest = $(this).attr('isFromTest');
        if(isFromTest){
            getDescriptionQuestion().find('label').html($(this).attr('description'));
        }
        var words = $(this).attr('words');
        drawRandomWord(words);
    });
}

function showPoUpObjDescription(){
    $(document).on('click','.selection-popup-obj',function(){
        var description = $(this).attr('description');
        getLessonScreen().find('.body-popup-obj').find('.obj-description').html(description);
        getLessonScreen().find('.body-popup-obj').show();
    });
    $(document).on('click','#circle-popup-obj',function(){
        var description = $(this).attr('description');
        getLessonScreen().find('.body-popup-obj').find('.obj-description').html(description);
        getLessonScreen().find('.body-popup-obj').show();
    });
}
function previewCourse(){
    $(document).on("click","#preview",function(){
        $('#preview-popup').modal('show');
    });
}

function closePreview(){
    $(document).on("click","#close-popup-preview",function(){
        $('#preview-popup').modal('hide');
    });
}
function closeObjPopUp(){
    $(document).on("click","#close-popup-obj",function(){
        $('.body-popup-obj').hide();
    });
}

$(document).ready(function(){
    closePreview();
    closeObjPopUp();
    previewCourse();
    beforeShowPreview();
    loadPreviewLevel();
    loadPreviewObjective();
    loadPreviewLesson();
    loadPreviewTest();
    slideBackPreview();
    clickCircleQuestion();
    showPoUpObjDescription();
});