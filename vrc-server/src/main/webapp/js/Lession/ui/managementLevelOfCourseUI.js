/**
 * Created by lantb on 2015-10-22.
 */
function sortDropdown(){
    $("#level").append($("#level option").remove().sort(function(a, b) {
        var at = $(a).text(), bt = $(b).text();
        return (at > bt)?1:((at < bt)?-1:0);
    }));
}
/**
 *
 * @param item
 */
function buildDropdown(item){
    var $option = $("<option>");
    $option.attr("id" , item.id);
    $option.attr("value" , item.name);
    $option.text(item.name);
    if(item.isDemo === true){
        $option.attr('selected', 'selected');
    }
    $("#level").append($option);
}

/**
 *
 * @param item
 */
function buildPanelLevel(item){
    //for panel default
    var $panel_default = $("<div>");
    $panel_default.addClass("panel panel-default");
    var $panel_heading = createPanelHeadingLevel(item);
    var $collapse = createPanelCollapseLv(item);
    $panel_default.append($panel_heading);
    $panel_default.append($collapse);
    $("#accordion").append($panel_default);
}

/**
 *
 * @param item
 * @returns {*|jQuery|HTMLElement}
 */
function createPanelHeadingLevel(item){
    var $panel_heading = $("<div>");
    $panel_heading.addClass("panel-heading");
    $panel_heading.css("background-color", item.color);

    //for div row in heading
    var $divRow = $("<div>");
    $divRow.addClass("row");

    //for div col in left
    var $divColLeft =  $("<div>");
    $divColLeft.addClass("col-sm-3");
    //for title of panel
    var $h4 = $("<h4>");
    $h4.addClass("panel-title");
    //button for level show up
    var $buttonLv = $("<button>");
    $buttonLv.addClass("btn btn-default");
    $buttonLv.attr("data-toggle","collapse");
    $buttonLv.attr("data-target","#"+item.id);
    $buttonLv.text(item.name);
    $h4.append($buttonLv);
    $divColLeft.append($h4);

    //div contain remove level button
    var $divColRight =  $("<div>");
    $divColRight.addClass("col-sm-2 pull-right");
    var $buttonRemoveLv = $("<button>");
    $buttonRemoveLv.addClass("btn btn-default removelv");
    $buttonRemoveLv.attr("id_lv",item.id);
    $buttonRemoveLv.text("Remove Level");
    $divColRight.append($buttonRemoveLv);


    $divRow.append($divColLeft);
    $divRow.append($divColRight);
    $panel_heading.append($divRow);
    return $panel_heading;
}

/**
 *
 * @param item
 * @returns {*|jQuery|HTMLElement}
 */
function createPanelCollapseLv(item){
    var $collapse = $("<div>");
    $collapse.addClass("panel-collapse collapse");
    $collapse.attr("id",item.id);
    $collapse.attr("id_level",item.id);
    $collapse.attr("type","getObjAndTest");
    var $panel_body = $("<div>");
    $panel_body.addClass("panel-body");

    var $divRow = $("<div>");
    $divRow.addClass("row");

    var $divColLeft =  $("<div>");
    $divColLeft.addClass("col-sm-3");
    var $btnCreateObj = $("<button>");
    $btnCreateObj.addClass("btn btn-default");
    $btnCreateObj.text("Create Objective");
    $btnCreateObj.attr("id_lv", item.id);
    $divColLeft.append($btnCreateObj);

    var $divColRight =  $("<div>");
    $divColRight.addClass("col-sm-3");
    var $btnCreateTest = $("<button>");
    $btnCreateTest.addClass("btn btn-default");
    $btnCreateTest.text("Create Test");
    $btnCreateTest.attr("id_lv", item.id);
    $divColRight.append($btnCreateTest);


    var $divObjectives = $("<div>");
    $divObjectives.addClass("row");
    $divObjectives.attr("id","collection_objective");

    var $divTests = $("<div>");
    $divTests.addClass("row");
    $divTests.attr("id","collection_test");

    $divRow.append($divColLeft);
    $divRow.append($divColRight);
    $panel_body.append($divRow);
    $panel_body.append($divObjectives);
    $panel_body.append($divTests)
    $collapse.append($panel_body);
    return $collapse;
}



//----------------------------end ui lv------------------//

/**
 *
 * @param idLevel
 * @param obj
 */
function buildPanelObject(idLevel,obj){
    var $panel_default = $("<div>");
    $panel_default.addClass("panel panel-default");
    var $panel_heading = createPanelHeadingObject(obj);
    var $collapse = createPanelCollapseObject(obj);
    $panel_default.append($panel_heading);
    $panel_default.append($collapse);
    $("#"+idLevel).find("#collection_objective").append($panel_default);
}

/**
 *
 * @param idLevel
 * @param obj
 * @returns {*|jQuery|HTMLElement}
 */
function createPanelHeadingObject(idLevel,obj){
    var $panel_heading = $("<div>");
    $panel_heading.addClass("panel-heading");
    $panel_heading.css("background-color", "");

    //for div row in heading
    var $divRow = $("<div>");
    $divRow.addClass("row");


    //for div col in left
    var $divColLeft =  $("<div>");
    $divColLeft.addClass("col-sm-3");
    //for title of panel
    var $h4 = $("<h4>");
    $h4.addClass("panel-title");
    //button for level show up
    var $buttonObj = $("<button>");
    $buttonObj.addClass("btn btn-default");
    $buttonObj.attr("data-toggle","collapse");
    $buttonObj.attr("data-target","#"+obj.id);
    $buttonObj.attr("id_lv",idLevel);
    $buttonObj.attr("id_obj",obj.id);
    $buttonObj.text(obj.name);
    $h4.append($buttonObj);
    $divColLeft.append($h4);


    //div contain remove level button
    var $divColRightEdit =  $("<div>");
    $divColRightEdit.addClass("col-sm-2");
    var $buttonEditObj = $("<button>");
    $buttonEditObj.addClass("btn btn-default editObj");
    $buttonEditObj.attr("id_lv",idLevel);
    $buttonEditObj.attr("id_obj",obj.id);
    $buttonEditObj.text("Remove Objective");
    $divColRightEdit.append($buttonEditObj);

    //div contain remove level button
    var $divColRightRemoveObj =  $("<div>");
    $divColRightRemoveObj.addClass("col-sm-2");
    var $buttonRemoveObj = $("<button>");
    $buttonRemoveObj.addClass("btn btn-default removeObj");
    $buttonRemoveObj.attr("id_lv",idLevel);
    $buttonRemoveObj.attr("id_obj",obj.id);
    $buttonRemoveObj.text("Remove Objective");
    $divColRightRemoveObj.append($buttonRemoveObj);

    $divRow.append($divColLeft);
    $divRow.append($divColRightEdit);
    $divRow.append($divColRightRemoveObj);
    $panel_heading.append($divRow);
    return $panel_heading;

}

/**
 *
 * @param idLevel
 * @param obj
 * @returns {*|jQuery|HTMLElement}
 */
function createPanelCollapseObject(idLevel,obj){
    var $collapse = $("<div>");
    $collapse.addClass("panel-collapse collapse");
    $collapse.attr("id",obj.id);
    $collapse.attr("id_lv",idLevel);
    $collapse.attr("type","getLessonObj");
    var $panel_body = $("<div>");
    $panel_body.addClass("panel-body");

    var $divRow = $("<div>");
    $divRow.addClass("row");

    var $divObjectives = $("<div>");
    $divObjectives.addClass("row");
    $divObjectives.attr("id","collection_lesson_obj");

    $divRow.append($divObjectives);
    $panel_body.append($divRow);
    $collapse.append($panel_body);
    return $collapse;
}

//----------------------------end ui object------------------//