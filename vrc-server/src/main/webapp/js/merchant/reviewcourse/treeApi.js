/**
 * Created by lantb on 2016-02-22.
 */
var treeAPI;
var theTree = $('#tree');
var firstLoad = true;
var showBtnAction = false;
var servlet = "/TreeLoadServlet";
var currentParent;
/**
 *  init the tree view.
 */
function initTree(){
    theTree.aciTree({
        ajax: {
            url: servlet
        },
        autoInit: false,
        sortable: true,
        ajaxHook: function(item, settings){
            var itemData = this.itemData(item);
            if(itemData!=null){
                //for load other node in course
                var target = itemData._targetLoad;
                var id = itemData.id;
                settings.url = settings.url + "?target=" + target + "&idTarget=" + id + "&showBtnAction=" + showBtnAction;
            }else{
                //load course
                settings.url = settings.url + "?target=" + loadCourse + "&idTarget=" + idCourse +"&firstLoad=" + firstLoad + "&showBtnAction="+showBtnAction;
                firstLoad = false;
            }

        },
        itemHook: function(parent, item, itemData, level){
            item.attr("id",itemData['id']);
            item.find(".aciTreeItem").attr("title", itemData['_title']);
            item.find('.aciTreeItem').css('background-color', itemData['_backgroundColor']);
            item.find('.aciTreeItem').css('padding', '5px');
            item.find('.aciTreeItem').css('border-radius', '5px');
            item.find('.aciTreeButton').css('padding-top', '9px');
            item.find('.aciTreeButton').css('padding-bottom', '5px');
            item.find('.aciTreeText').css('color', itemData['_textColor']);
            item.find('.aciTreeText').css('vertical-align', 'middle');
            item.find('.aciTreeIcon').css('vertical-align', 'middle');
            if(itemData['_isButton']){
                this.setLabel(item, {
                    label:  itemData.label + '<i style="vertical-align: middle;padding-left: 5px" class="fa fa-plus"/>'
                });
            }

        }
    });
    treeAPI = theTree.aciTree('api');
    treeAPI.init({
        success: function(item) {
            this.open(item);
        }
    });
    theTree.removeClass("aciTree0");
    theTree.addClass("aciTreeArrow");
    theTree.addClass("aciTreeNoBranches");
    theTree.addClass("aciTreeBig");
}

/**
 * drag&drop
 */
function DisableDrag2drop(){
    theTree.on('acitree', function(event, api, item, eventName, options) {
        switch (eventName) {
            case 'checkdrop': return false;
        }
    });
}
$(function() {
    DisableDrag2drop();
    ClickOnTree();
    initTree();
});