/**
 * Created by lantb on 2016-02-22.
 */
var treeAPI;
var theTree = $('#tree');
var firstLoad = false;
var showBtnAction = true;
/**
 *  init the tree view.
 */
function initTree(){
    theTree.aciTree({
        ajax: {
            url: serlvet
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
                settings.url = settings.url + "?target=" + loadcourse + "&idTarget=" + idCourse +"&firstLoad=" + firstLoad + "&showBtnAction="+showBtnAction;
                firstLoad = true;
            }

        },
        itemHook: function(parent, item, itemData, level) {
            item.find('.aciTreeItem').css('background-color', itemData['_backgroundColor']);
            item.find('.aciTreeItem').css('padding', '5px');
            item.find('.aciTreeItem').css('border-radius', '5px');
            item.find('.aciTreeButton').css('padding-top', '5px');
            item.find('.aciTreeButton').css('padding-bottom', '5px');
            item.find('.aciTreeText').css('color', itemData['_textColor']);
            item.find('.aciTreeText').css('font-size', 'larger');
            item.find('.aciTreeText').css('padding-left', '5px');
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