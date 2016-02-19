/**
 * Created by lantb on 2016-02-16.
 */

var treeAPI;
var currentParent;
var nodeCopied;
var theTree = $('#tree4');
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
                settings.url = settings.url + "?target=" + target + "&idTarget=" + id;
            }else{
                //load course
                settings.url = settings.url + "?target=" + loadcourse + "&idTarget=" + idCourse;
            }

        },
        itemHook: function(parent, item, itemData, level) {
             // a custom item implementation to show a link
            //item.attr('id',itemData['id']);
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

function reloadRoot(){
    treeAPI = theTree.aciTree('api');
    treeAPI.destroy({
        success: function(item) {
            initTree();
        }
    });
}
function saveParent(){
    theTree.on('acitree', function(event, api, item, eventName, options) {
        if (eventName == 'selected'){
            // do something when a item is selected
            var itemData = api.itemData(item);
            currentParent = api.parent(item);
        }
    });
}
function reload(){
    treeAPI = theTree.aciTree('api');
    var itemData = treeAPI.itemData(currentParent);
    var target = itemData._targetLoad;
    if(target == loadcourse){
        reloadRoot();
    }else{
        treeAPI.unload(currentParent, {
            success: function(item) {
                this.close(item);
            }
        });
        treeAPI.ajaxLoad(currentParent, {
            success: function(item) {
                this.open(item);
            }
        });
    }
    treeAPI.searchId(true,true,{
       success : function(item,options){
           var itemData = treeAPI.itemData(item);
           alert(itemData);
       },
       fail : function (item,options){
            alert('a')
       } ,
        id : "obj1"
    });
}


function copyAndPaste(){
    $('#tree4').contextMenu({
        selector: '.aciTreeLine',
        build: function(element) {
            var api = $('#tree4').aciTree('api');
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

function drag2drop(){
    theTree.on('acitree', function(event, api, item, eventName, options) {
        switch (eventName) {
            case 'checkdrop':
                if (options.isContainer) {
                    // mark the drop target as invalid
                    return false;
                } else {
                    if (options.before === null) {
                        // container creation is disabled
                        return false;
                    }
                    // check to have the same parent
                    var target = api.itemFrom(options.hover);
                    if (!api.sameParent(item, target)) {
                        // mark the drop target as invalid
                        return false;
                    }

                    var itemData = api.itemData(item);
                    var label = itemData['label'];
                    if(label == 'Test'){
                        return false;
                    }
                }
                break;
        }
    });
}
$(function() {
    drag2drop();
    copyAndPaste();
    saveParent();
    initTree();
});
