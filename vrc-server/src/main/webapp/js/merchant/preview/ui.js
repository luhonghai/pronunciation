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
    $a.addClass("btn selection obj");
    $a.attr("id",test.id);
    var $label = $('<label>');
    $label.html("Test");
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
    $a.attr("description",lesson.description);
    var $label = $('<label>');
    $label.html(lesson.name);
    $label.addClass("name");
    var $circle = $('<div>');
    $circle.addClass('circle');
    $a.append($label);
    $a.append($circle);
    $html.append($a);
    getBodyLesson().append($html);
}

function drawQuestionPreview(question, index){
    index = index + 1;
    var $html = $("<div>");
    $html.addClass("circle circle-question");
    $html.attr('id',question.id);
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
    var array = words.split("-");
    var rand = Math.floor(Math.random() * array.length);
    console.log("random : " + rand);
    return array[rand];
}

function drawRandomWord(words){
    var wordShow = randomWords(words);
    getContainQuestionWord().html(wordShow);
}