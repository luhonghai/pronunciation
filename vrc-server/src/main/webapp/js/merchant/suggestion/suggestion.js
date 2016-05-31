/**
 * Created by lantb on 2016-02-02.
 */
var suggestionServlet = 'SuggestionServlet';
var page = window.location.pathname;
function suggestCourse(){
    var tmp = "";
    if(page.indexOf("my-courses")!= -1){
        tmp = "mycourses";
        $(".suggestCourse").typeahead({
            source : function(query, process){
                $.ajax({
                    url : suggestionServlet,
                    type : "POST",
                    data : "page="+tmp+"&action=course&query="+query,
                    dataType : "JSON",
                    async : true,
                    success : function(data){
                        process(data);
                    }
                });
            }
        });
    }else if(page.indexOf("main-courses") != -1){
        tmp = "maincourses";
        $(".suggestCourse").typeahead({
            source : function(query, process){
                $.ajax({
                    url : suggestionServlet,
                    type : "POST",
                    data : "page="+tmp+"&action=course&query="+query,
                    dataType : "JSON",
                    async : true,
                    success : function(data){
                        process(data);
                    }
                });
            }
        });
    }

}
function suggestCompany(){
    $(".suggestCompany").typeahead({
        source : function(query, process){
            $.ajax({
                url : suggestionServlet,
                type : "POST",
                data : "action=company&query="+query,
                dataType : "JSON",
                async : true,
                success : function(data){
                    process(data);
                }
            });
        }
    });
}

$(document).ready(function(){
    suggestCourse();
    suggestCompany();
});

