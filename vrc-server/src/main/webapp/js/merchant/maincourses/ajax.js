/**
 * Created by lantb on 2016-03-07.
 */
var servlet = "/MainCourseServlet";
var progress;
function addCourse(){
    //post request to server
    $.ajax({
        url : servlet,
        type : "POST",
        data : {
            action: "addcourse",
            name: getCourseName().val(),
            description: getCourseDescription().val(),
            share : getCourseShare().val()
        },
        dataType : "text",
        success : function(data){
            if (data.indexOf("error") !=-1) {
                getMsgAddCourse().html("an error has been occurred in server");
                getMsgAddCourse().show();
            }else{
                //add success will draw again the list or redirect to the new page
                window.location.href = "/course-details.jsp?idCourse="+data;
            }
        },
        error: function () {
            getMsgAddCourse().html("an error has been occurred in server");
            getMsgAddCourse().show();
        }
    });
}


function loadAllCourse(){
    getDivContainCourse().empty();
    getDivContainCourse().hide();
    getProcessBar().show();
    progress = getProcessBar().progressTimer({
        timeLimit: 10,
        onFinish: function () {
            getProcessBar().delay(2000).hide();
            getDivContainCourse().show();
            progress.progressTimer('destroy');
        }
    });
    $.ajax({
        url : servlet,
        type : "GET",
        data : {
            action: "listall"
        },
        dataType : "json",
        success : function(data){
            if(data.length > 0 ){
                $(data).each(function(){
                    buildCourse(this);
                });
            }else{
                getDivContainCourse().html("<label class='welcome'>There are no courses currently available.</label>");
            }
        },
        error: function () {
            getDivContainCourse().html("<label class='welcome'>could not connect to server</label>");
        }
    }).error(function(){
        progress.progressTimer('error', {
            errorText:'ERROR!',
            onFinish:function(){
                getDivContainCourse().html("<label class='welcome'>could not connect to server</label>");
                getDivContainCourse().show();
            }
        });
    }).done(function(){
        progress.progressTimer('complete');
    });
}


function searchCourseHeader(){
    getDivContainCourse().empty();
    getDivContainCourse().hide();
    getProcessBar().show();
    progress = getProcessBar().progressTimer({
        timeLimit: 10,
        onFinish: function () {
            getProcessBar().delay(2000).hide();
            getDivContainCourse().show();
            progress.progressTimer('destroy');
        }
    });
    $.ajax({
        url : servlet,
        type : "GET",
        data : {
            action: "searchHeader",
            name : getSuggestCourseHeader().val()
        },
        dataType : "json",
        success : function(data){
            if(data.length > 0 ){
                $(data).each(function(){
                    buildCourse(this);
                });
            }else{
                getDivContainCourse().html("<label class='welcome'>No records were found that match the specified search criteria.</label>");
            }
        },
        error: function () {
            getDivContainCourse().html("<label class='welcome'>could not connect to server</label>");
        }
    }).error(function(){
        progress.progressTimer('error', {
            errorText:'ERROR!',
            onFinish:function(){
                getDivContainCourse().html("<label class='welcome'>could not connect to server</label>");
                getDivContainCourse().show();
            }
        });
    }).done(function(){
        progress.progressTimer('complete');
    });
}

function searchCourseDetail(){
    getDivContainCourse().empty();
    getDivContainCourse().hide();
    getProcessBar().show();
    progress = getProcessBar().progressTimer({
        timeLimit: 10,
        onFinish: function () {
            getProcessBar().delay(2000).hide();
            getDivContainCourse().show();
            progress.progressTimer('destroy');
        }
    });
    $.ajax({
        url : servlet,
        type : "POST",
        data : {
            action: "searchDetail",
            cpName : getSuggestCompany().val(),
            cName : getSuggestCourse().val(),
            dateFrom : getFromDate().val(),
            dateTo : getDateTo().val()
        },
        dataType : "json",
        success : function(data){
            if(data.length > 0 ){
                $(data).each(function(){
                    buildCourse(this);
                });
            }else{
                getDivContainCourse().html("<label class='welcome'>No records were found that match the specified search criteria.&nbsp</label>");
            }
            closeDropdown();
        },
        error: function () {
            getDivContainCourse().html("<label class='welcome'>could not connect to server</label>");
            closeDropdown();
        }
    }).error(function(){
        progress.progressTimer('error', {
            errorText:'ERROR!',
            onFinish:function(){
                getDivContainCourse().html("<label class='welcome'>could not connect to server</label>");
                getDivContainCourse().show();
                closeDropdown();
            }
        });
    }).done(function(){
        progress.progressTimer('complete');
    });

}