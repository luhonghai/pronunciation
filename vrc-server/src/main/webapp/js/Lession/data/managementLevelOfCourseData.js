/**
 * Created by lantb on 2015-10-22.
 */
function getDtoAddObjective(){
    var idLevel = $("#yesadd").attr("id_level");
    var idCourse = $("#idCourse").val();
    var nameObj = $("#").val();
    var descriptionObj =  $("#").val();
    var idLessons = $('#select-lesson option:selected').map(function(a, item){return item.value;});
    var dto = {
        idLevel : idLevel,
        idCourse: idCourse,
        nameObj : nameObj,
        descriptionObj : descriptionObj,
        idLessons :
    };

}
