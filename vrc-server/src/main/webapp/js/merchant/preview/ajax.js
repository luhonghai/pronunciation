/**
 * Created by lantb on 2016-03-30.
 */
var servletPreview = "/PreviewServlet";



function loadContent(action,id){
    $.ajax({
        url : servletPreview,
        type : "POST",
        data : {
            action: action,
            id: id
        },
        dataType : "json",
        success : function(data){
            if(action == targetLoadCourse){
                getHeaderLevel().find('marquee').html(nameOfCourse + " - levels");
                clearBodyLevel();
                $.each(data, function(i, item){
                    drawLevelPreview(item);
                });
                showScreen(getLevelScreen().attr('id'));
            }else if(action == targetLoadLevel){
                getHeaderObj().find('label').html(getPopUpPreview().find("#"+id).find('label').html());
                clearBodyObj();
                $.each(data.list, function(i, item){
                    drawObjPreview(item);
                });
                clearBodyTest();
                if(data.test!=null){
                    drawTestPreview(data.test);
                }else{
                    drawTestPreview(null);
                }
                showScreen(getObjScreen().attr('id'));
            }else if(action == targetLoadObj){
                getHeaderLesson().find('marquee').html(getHeaderObj().find('label').html() +" - "
                    + getPopUpPreview().find("#"+id).find('label').html());
                clearBodyLesson();
                var descriptionObj = getPopUpPreview().find("#"+id).attr('description');
                getLessonScreen().find('.selection-popup-obj').attr('description',descriptionObj);
                $.each(data, function(i, item){
                    drawLessonPreview(item);
                });
                showScreen(getLessonScreen().attr('id'));
            }else if(action == targetLoadLesson){
                clearBodyQuestion();
                var description =  getPopUpPreview().find("#"+id).attr('description');
                getDescriptionQuestion().find('label').html(description);
                $.each(data, function(i, item){
                    if(i==0){
                        drawRandomWord(parseList(item.words));
                    }
                    drawQuestionPreview(item,i, false);
                });
                getQuestionScreen().find('.slide-back').attr('back','screen-lesson');
                showScreen(getQuestionScreen().attr('id'));

            }else if(action == targetLoadTest){
                clearBodyQuestion();
                $.each(data, function(i, item){
                    if(i==0){
                        getDescriptionQuestion().find('label').html(item.description);
                        drawRandomWord(parseList(item.words));
                    }
                    drawQuestionPreview(item,i, true);
                });
                getQuestionScreen().find('.slide-back').attr('back','screen-objective');
                showScreen(getQuestionScreen().attr('id'));
            }
        },
        error: function () {
        }
    });
}
