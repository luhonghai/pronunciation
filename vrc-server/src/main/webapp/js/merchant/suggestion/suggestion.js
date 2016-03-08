/**
 * Created by lantb on 2016-02-02.
 */
var suggestionServlet = 'SuggestionServlet';
function suggestCourse(){
    $(".suggestCourse").typeahead({
        source : function(query, process){
            $.ajax({
               url : suggestionServlet,
               type : "POST",
               data : "action=course&query="+query,
               dataType : "JSON",
               async : true,
               success : function(data){
                   console.log(data);
                   process(data);
               }
            });
        }
    });
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
                    console.log(data);
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

