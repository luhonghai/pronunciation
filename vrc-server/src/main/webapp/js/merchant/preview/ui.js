/**
 * Created by lantb on 2016-03-30.
 */

function drawLevelPreview(level){
    var $html = $("<div>");
    $html.addClass("container-selection");
    var $a = $('<a>');
    $a.addClass("btn selection level");
    $a.attr("id",level.id);
    var $label = $('<label>');
    $label.html(level.name);
    $label.addClass("name");
    var $circle = $('<div>');
    $circle.addClass('circle');
    $a.append($label);
    $a.append($circle);
    $html.append($a);
    getBodyLevel().append($html);
}

function drawObjPreview(obj){
    var $html = $("<div>");
    $html.addClass("container-selection");
    var $a = $('<a>');
    $a.addClass("btn selection obj");
    $a.attr("id",obj.id);
    $a.attr("description",obj.description);
    var $label = $('<label>');
    $label.html(obj.name);
    $label.addClass("name");
    var $circle = $('<div>');
    $circle.addClass('circle');
    $a.append($label);
    $a.append($circle);
    $html.append($a);
    getBodyObj().append($html);
}

function drawTestPreview(test){
    var $html = $("<div>");
    $html.addClass("container-selection");
    var $a = $('<a>');
    $a.addClass("btn selection test");
    if(test!=null){
        $a.attr("id",test.id);
        $a.removeAttr("disabled");
        $a.css("background-color","#DDF7F6");
    }else{
        $a.attr("disabled","disabled");
        $a.css("background-color","#C0C0C0");
    }
    var $label = $('<label>');
    $label.html("test");
    $label.addClass("name");
    var $circle = $('<div>');
    $circle.addClass('circle');
    $a.append($label);
    $a.append($circle);
    $html.append($a);
    getBodyTest().append($html);
}

/**
 *
 * @param lesson
 */
function drawLessonPreview(lesson){
    var $html = $("<div>");
    $html.addClass("container-selection");
    var $a = $('<a>');
    $a.addClass("btn selection lesson");
    $a.attr("id",lesson.id);
    $a.attr("description",lesson.name);
    var $label = $('<label>');
    $label.html(lesson.title);
    $label.addClass("name");
    var $circle = $('<div>');
    $circle.addClass('circle');
    $a.append($label);
    $a.append($circle);
    $html.append($a);
    getBodyLesson().append($html);
}

function drawQuestionPreview(question, index, isFormTest){
    index = index + 1;
    var $html = $("<div>");
    $html.addClass("circle circle-question");
    $html.attr('id',question.id);
    $html.attr('isformtest',isFormTest);
    $html.attr('description',question.description);
    $html.attr('words',parseList(question.words));
    $html.html("Q" + index);
    getContainQuestion().append($html);
}

function parseList(words){
    var list = "";
    $.each(words, function(i, item){
        list = list + item.word + "-";
    });
    return list.substring(0,list.length - 1);
}

function randomWords(words){
    if(typeof words != "undefined"){
        var array = words.split("-");
        var rand = Math.floor(Math.random() * array.length);
        return array[rand];
    }

}

function drawRandomWord(words){
    var wordShow = randomWords(words);
    getContainQuestionWord().html(wordShow);
}