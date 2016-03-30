/**
 * Created by lantb on 2016-03-30.
 */
function getPopUpPreview(){
    return $('#preview-popup');
}

//level screen
function getLevelScreen(){
    return $('#screen-level');
}
function getHeaderLevel(){
    return getLevelScreen().find('.header');
}
function getBodyLevel(){
    return getLevelScreen().find('.body-screen');
}
function clearBodyLevel(){
    getBodyLevel().html("");
}

//objective screen
function getObjScreen(){
    return $('#screen-objective');
}
function getHeaderObj(){
    return getObjScreen().find('.header');
}
function getBodyObj(){
    return getObjScreen().find('.body-screen');
}
function clearBodyObj(){
    getBodyObj().html("");
}

function getBodyTest(){
    return getObjScreen().find('.footer-screen');
}
function clearBodyTest(){
    getBodyTest().html("");
}

//lesson screen
function getLessonScreen(){
    return $('#screen-lesson');
}
function getHeaderLesson(){
    return getLessonScreen().find('.header');
}
function getBodyLesson(){
    return getLessonScreen().find('.body-screen');
}

function clearBodyLesson(){
    getBodyLesson().html("");
}


//question screen
function getQuestionScreen(){
    return $('#screen-question');
}

function getBodyQuestion(){
    return getQuestionScreen().find('.body-screen');
}

function getDescriptionQuestion(){
    return getBodyQuestion().find('.description-question');
}
function getContainWord(){
    return  getBodyQuestion().find('.contain-word');
}

function getContainQuestion(){
    return getBodyQuestion().find('.contain-question');
}
function clearBodyQuestion(){
    getContainQuestion().html("");
}


function showScreen(id){
    getPopUpPreview().find('.screen-detail').each(function(){
        var idCompare = $(this).attr('id');
        if(id!=idCompare){
            $(this).hide();
        }else{
            $(this).show();
        }
    });
}