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
    $divColLeft.addClass("heading-col-left");
    //for title of panel
    var $h4 = $("<h4>");
    $h4.addClass("panel-title");
    //button for level show up
    var $buttonLv = $("<button>");
    $buttonLv.addClass("btn btn-default btn-success");
    $buttonLv.attr("data-toggle","collapse");
    $buttonLv.attr("data-target","#"+item.id);
    $buttonLv.attr("onclick","clickLevel(this);");
    $buttonLv.text(item.name + " ↓");
    $h4.append($buttonLv);
    $divColLeft.append($h4);

    //div contain remove level button
    var $divColRight =  $("<div>");
    $divColRight.addClass("heading-col-right");

    //button create objective
    var $btnCreateOldObj = $("<button>");
    $btnCreateOldObj.addClass("btn btn-default createOldObj");
    $btnCreateOldObj.text("Add Old Objective");
    $btnCreateOldObj.attr("id_lv", item.id);

    //button create objective
    var $btnCreateObj = $("<button>");
    $btnCreateObj.addClass("btn btn-default btn-info createObj");
    $btnCreateObj.text("Create Objective");
    $btnCreateObj.attr("id_lv", item.id);

    //buton create test
    var $btnCreateOldTest = $("<button>");
    $btnCreateOldTest.addClass("btn btn-default createOldTest");
    $btnCreateOldTest.text("Add Old Test");
    $btnCreateOldTest.attr("id_lv", item.id);

    //buton create test
    var $btnCreateTest = $("<button>");
    $btnCreateTest.addClass("btn btn-default btn-info createTest");
    $btnCreateTest.text("Create Test");
    $btnCreateTest.attr("id_lv", item.id);

    //buton remove level
    var $buttonRemoveLv = $("<button>");
    $buttonRemoveLv.addClass("btn btn-default btn-danger removelv");
    $buttonRemoveLv.attr("id_lv",item.id);
    $buttonRemoveLv.text("Remove Level");

    //$divColRight.append($btnCreateOldObj);
    $divColRight.append($btnCreateObj);
    //$divColRight.append($btnCreateOldTest);
    $divColRight.append($btnCreateTest);
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
    $collapse.addClass("panel-collapse collapse panelLv");
    $collapse.attr("id",item.id);
    $collapse.attr("id_level",item.id);
    $collapse.attr("type","getObjAndTest");
    var $panel_body = $("<div>");
    $panel_body.addClass("panel-body");

    var $divObjectives = $("<div>");
    $divObjectives.addClass("row");
    $divObjectives.attr("id","collection_objective");

    var $divTests = $("<div>");
    $divTests.addClass("row");
    $divTests.attr("id","collection_test");

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
function buildPanelObject(obj){
    var $panel_default = $("<div>");
    $panel_default.addClass("panel panel-default");
    var $panel_heading = createPanelHeadingObject(obj);
    var $collapse = createPanelCollapseObject(obj);
    $panel_default.append($panel_heading);
    $panel_default.append($collapse);
    $("#"+obj.idLevel).find("#collection_objective").append($panel_default);
}

/**
 *
 * @param idLevel
 * @param obj
 * @returns {*|jQuery|HTMLElement}
 */
function createPanelHeadingObject(obj){
    var $panel_heading = $("<div>");
    $panel_heading.addClass("panel-heading");
    $panel_heading.css("background-color", "");

    //for div row in heading
    var $divRow = $("<div>");
    $divRow.addClass("row");


    //for div col in left
    var $divColLeft =  $("<div>");
    $divColLeft.addClass("heading-col-left");
    //for title of panel
    var $h4 = $("<h4>");
    $h4.addClass("panel-title");
    //button for level show up
    var $buttonObj = $("<button>");
    $buttonObj.addClass("btn btn-default btn-info");
    $buttonObj.attr("data-toggle","collapse");
    $buttonObj.attr("data-target","#"+obj.idObjective);
    $buttonObj.attr("id_lv",obj.idLevel);
    $buttonObj.attr("id_obj",obj.idObjective);
    $buttonObj.attr("onclick","clickObj(this)");
    $buttonObj.text(obj.nameObj + " ↓");
    $h4.append($buttonObj);
    $divColLeft.append($h4);

    //div contain add  & remove obj button
    var $divColRight =  $("<div>");
    $divColRight.addClass("heading-col-right");
    //edit obj button
    var $buttonEditObj = $("<button>");
    $buttonEditObj.addClass("btn btn-default btn-info editObj");
    $buttonEditObj.attr("id_lv",obj.idLevel);
    $buttonEditObj.attr("id_obj",obj.idObjective);
    $buttonEditObj.text("Edit");
    //remove obj button
    var $buttonRemoveObj = $("<button>");
    $buttonRemoveObj.addClass("btn btn-default btn-info removeObj");
    $buttonRemoveObj.attr("id_lv",obj.idLevel);
    $buttonRemoveObj.attr("id_obj",obj.idObjective);
    $buttonRemoveObj.text("Remove");

    $divColRight.append($buttonEditObj);
    $divColRight.append($buttonRemoveObj);

    $divRow.append($divColLeft);
    $divRow.append($divColRight);
    $panel_heading.append($divRow);
    return $panel_heading;

}

/**
 *
 * @param idLevel
 * @param obj
 * @returns {*|jQuery|HTMLElement}
 */
function createPanelCollapseObject(obj){
    var $collapse = $("<div>");
    $collapse.addClass("panel-collapse collapse panelObj");
    $collapse.attr("id",obj.idObjective);
    $collapse.attr("id_lv",obj.idLevel);
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



/**
 *
 * @param idLevel
 * @param obj
 */
function buildPanelTest(obj){
    var $panel_default = $("<div>");
    $panel_default.addClass("panel panel-default");
    var $panel_heading = createPanelHeadingTest(obj);
    var $collapse = createPanelCollapseTest(obj);
    $panel_default.append($panel_heading);
    $panel_default.append($collapse);
    $("#"+obj.idLevel).find("#collection_test").append($panel_default);
}

/**
 *
 * @param idLevel
 * @param obj
 * @returns {*|jQuery|HTMLElement}
 */
function createPanelHeadingTest(obj){
    var $panel_heading = $("<div>");
    $panel_heading.addClass("panel-heading");
    $panel_heading.css("background-color", "#D46752");

    //for div row in heading
    var $divRow = $("<div>");
    $divRow.addClass("row");


    //for div col in left
    var $divColLeft =  $("<div>");
    $divColLeft.addClass("heading-col-left");
    //for title of panel
    var $h4 = $("<h4>");
    $h4.addClass("panel-title");
    //button for level show up
    var $buttonObj = $("<button>");
    $buttonObj.addClass("btn btn-default btn-info");
    $buttonObj.attr("data-toggle","collapse");
    $buttonObj.attr("data-target","#"+obj.idTest);
    $buttonObj.attr("id_lv",obj.idLevel);
    $buttonObj.attr("id_test",obj.idTest);
    $buttonObj.attr("onclick","clickTest(this)");
    $buttonObj.text(obj.nameTest + " ↓" + " - PercentPass: "+ obj.percentPass);
    $h4.append($buttonObj);
    $divColLeft.append($h4);

    //div contain edit & remove test button
    var $divColRight =  $("<div>");
    $divColRight.addClass("heading-col-right");
    //button edit test
    var $buttonEditObj = $("<button>");
    $buttonEditObj.addClass("btn btn-default btn-info editTest");
    $buttonEditObj.attr("id_lv",obj.idLevel);
    $buttonEditObj.attr("id_test",obj.idTest);
    $buttonEditObj.text("Edit");
    //button remove test
    var $buttonRemoveObj = $("<button>");
    $buttonRemoveObj.addClass("btn btn-default btn-info removeTest");
    $buttonRemoveObj.attr("id_lv",obj.idLevel);
    $buttonRemoveObj.attr("id_test",obj.idTest);
    $buttonRemoveObj.text("Remove");
    $divColRight.append($buttonEditObj);
    $divColRight.append($buttonRemoveObj);

    $divRow.append($divColLeft);
    $divRow.append($divColRight);
    $panel_heading.append($divRow);
    return $panel_heading;

}

/**
 *
 * @param idLevel
 * @param obj
 * @returns {*|jQuery|HTMLElement}
 */
function createPanelCollapseTest(obj){
    var $collapse = $("<div>");
    $collapse.addClass("panel-collapse collapse panelTest");
    $collapse.attr("id",obj.idTest);
    $collapse.attr("id_lv",obj.idLevel);
    $collapse.attr("type","getLessonTest");
    var $panel_body = $("<div>");
    $panel_body.addClass("panel-body");

    var $divRow = $("<div>");
    $divRow.addClass("row");

    var $divObjectives = $("<div>");
    $divObjectives.addClass("row");
    $divObjectives.attr("id","collection_lesson_test");

    $divRow.append($divObjectives);
    $panel_body.append($divRow);
    $collapse.append($panel_body);
    return $collapse;
}

//----------------------------end ui test------------------//
