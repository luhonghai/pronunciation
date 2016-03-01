/**
 * Created by lantb on 2016-02-22.
 */
var treeAPI;
var theTree = $('#tree');
var firstLoad = true;
var showBtnAction = true;
var servlet = "/TreeLoadServlet";
var currentParent;
var nodeCopied;
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
            item.attr("title", itemData['_title']);
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



function ClickOnTree(){
    /*theTree.on('acitree', function(event, api, item, eventName, options) {
        if (eventName == 'selected'){
            var itemData = api.itemData(item);
            currentParent = api.parent(item);
            var popupId = itemData._popupId;
            api.blur(item,{
                success : function(item){
                    item.removeClass('aciTreeSelected');
                    item.removeClass('aciTreeFocus');
                }
            });
            openPopup(itemData);
        }
    });*/

    $(document).on("click",".aciTreeItem",function(){
        treeAPI = theTree.aciTree('api');
        var item = $(this).closest("li");
        var itemData = treeAPI.itemData(item);
        currentParent = treeAPI.parent(item);
        var popupId = itemData._popupId;
        openPopup(itemData);
    });


}
/**
 * drag&drop
 */
function drag2drop(){
    theTree.on('acitree', function(event, api, item, eventName, options) {
        switch (eventName) {
            case 'checkdrop':
                if (options.isContainer) {
                    // mark the drop target as invalid
                    return false;
                } else {
                    // check to have the same parent
                    var target = api.itemFrom(options.hover);
                    var targetData = api.itemData(target);
                    var itemData = api.itemData(item);
                    if(targetData._isButton){
                        return false;
                    }
                    if(itemData._isButton){
                        return false;
                    }
                    if (options.before === null) {
                        // container creation is disabled
                        return false;
                    }

                    if (!api.sameParent(item, target)) {
                        // mark the drop target as invalid
                        return false;
                    }

                }
                break;
        }
    });
}
/**
 * reload the tree
 */
function reloadTree(){
    treeAPI = theTree.aciTree('api');
    treeAPI.unload(currentParent, {
        success: function(item) {
            this.close(item);
            treeAPI.ajaxLoad(currentParent, {
                success: function(item) {
                    this.open(item);
                }
            });
        }
    });


}
/**
 * use for copy and paste
 */
function copyAndPaste(){
    theTree.contextMenu({
        selector: '.aciTreeLine',
        build: function(element) {
            var api = theTree.aciTree('api');
            var item = api.itemFrom(element);
            var menu = {
            };
            menu['copy'] = {
                name: 'Copy',
                callback: function() {
                    nodeCopied = item;
                }
            };
            menu['paste'] = {
                name: 'Paste',
                callback: function() {
                    var data = api.itemData(nodeCopied);
                    var lblcopied = data['label'];
                    lblcopied = 'copy of ' + lblcopied;
                    data['label'] = lblcopied;
                    api.append(item,{
                        itemData : [data]
                    })
                }
            };
            return {
                autoHide: true,
                items: menu
            };
        }
    });
}
$(function() {
    //copyAndPaste();
    ClickOnTree();
    drag2drop();
    initTree();

});
