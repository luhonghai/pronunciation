/**
 * Created by lantb on 2016-03-07.
 */
function validateFormAddCourse(){
    var name = getCourseName().val();
    var description = getCourseDescription().val();
    if(name == '' || typeof name === "undefined"){
        getCourseName().focus();
        getMsgAddCourse().html('Please enter a name!');
        getMsgAddCourse().show();
        return false;
    }
    if(description == '' || typeof description === "undefined"){
        getCourseDescription().focus();
        getMsgAddCourse().html('Please enter a description!');
        getMsgAddCourse().show();
        return false;
    }
    getMsgAddCourse().hide();
    return true;
}

function validateFormSearchHeader(){
    var course = getSuggestCourseHeader().val();
    if(course == '' || typeof course === "undefined"){
        getSuggestCourseHeader().focus();
        return false;
    }
    return true;
}