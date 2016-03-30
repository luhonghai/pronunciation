/**
 * Created by lantb on 2016-03-29.
 */
function getBodyLevel(){
    return $('#screen-level').find('#body-screen');
}

function clearBodyLevel(){
    getBodyLevel().empty();
}

function drawLevel(level){
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


function getBodyObj(){
    return $('#screen-objective').find('#body-screen');
}
function clearBodyObj(){
    getBodyObj().empty();
}

function drawObj(obj){
    var $html = $("<div>");
    $html.addClass("container-selection");
    var $a = $('<a>');
    $a.addClass("btn selection obj");
    $a.attr("id",obj.id);
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




function getBodyTest(){
    return $('#screen-objective').find('#footer-screen');
}
function clearBodyTest(){
    getBodyTest().empty();
}

function drawTest(test){
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