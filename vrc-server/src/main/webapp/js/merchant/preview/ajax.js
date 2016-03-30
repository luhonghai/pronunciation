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
                getHeaderLevel().find('label').html(nameOfCourse + " - levels");
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
                }
                showScreen(getObjScreen().attr('id'));
            }else if(action == targetLoadObj){
                getHeaderLesson().find('marquee').html(getHeaderObj().find('label').html() +" - "
                    + getPopUpPreview().find("#"+id).find('label').html());
                clearBodyLesson();
                $.each(data, function(i, item){
                    drawLessonPreview(item);
                });
                showScreen(getLessonScreen().attr('id'));
            }else if(action == targetLoadLesson){
                clearBodyQuestion();
                var description =  getPopUpPreview().find("#"+id).attr('description');
                getDescriptionQuestion().find('label').html(description);
                $.each(data, function(i, item){
                    drawQuestionPreview(item,i);
                });
                showScreen(getQuestionScreen().attr('id'));

            }
        },
        error: function () {

        }
    });
}
