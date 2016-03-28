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
var dragDrop = null;
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
            enablePublishBtn();
        },
        itemHook: function(parent, item, itemData, level){
            item.attr("id",itemData['id']);
            item.find('.aciTreeItem').attr("title", itemData['_title']);
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



function ClickOnTree(){

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
        console.log('event :' + eventName);
        switch (eventName) {
            case 'sorted' :
                if(dragDrop!=null){
                    DragDrop(dragDrop.action,dragDrop.parentId,dragDrop.childId,dragDrop.indexDrop,dragDrop.move);
                    dragDrop = null;
                }
                return true;
            case 'checkdrop':
                if (options.isContainer) {
                    return false;
                } else {
                    // check to have the same parent
                    dragDrop = new Object();
                    var drop = api.itemFrom(options.hover);
                    var dropData = api.itemData(drop);
                    var dragData = api.itemData(item);
                    if(dropData._isButton){
                        return false;
                    }
                    if(dragData._isButton){
                        return false;
                    }
                    if (options.before === null) {
                        // container creation is disabled
                        return false;
                    }
                    if(dragData._targetLoad == targetLoadTest){
                        //do not move the test node
                        return false;
                    }
                    if (!api.sameParent(item, drop)) {
                        // mark the drop target as invalid
                        return false;
                    }else{
                        var parent = api.parent(item);
                        var parentId = api.itemData(parent).id;
                        var action = dragData._targetLoad;
                        var childId = dragData.id;
                        var indexDrop = api.getIndex(drop);
                        console.log(indexDrop);
                        var indexDrag = api.getIndex(item);
                        console.log(indexDrag);
                        var move = "down";
                        if(indexDrag > indexDrop){
                            move = "up"
                        }
                        dragDrop.action = action;
                        dragDrop.parentId = parentId;
                        dragDrop.childId = childId;
                        dragDrop.indexDrop = indexDrop;
                        dragDrop.move = move;
                        return true;
                    }
                }
        }
    });
}
/**
 * reload the tree
 */
function reloadTree(){
    enablePublishBtn();
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
        selector: '.aciTreeItem',
        build: function(element) {
            var api = theTree.aciTree('api');
            var item = api.itemFrom(element);
            var menu = {
            };
            menu['copy'] = {
                name: 'Copy',
                callback: function() {
                    nodeCopied = item;
                    currentParent = api.parent(item);
                }
            };
            menu['paste'] = {
                name: 'Paste',
                callback: function() {
                    var dataTarget = api.itemData(item);
                    var dataCurrentParent = api.itemData(currentParent);
                    var dataCopied = api.itemData(nodeCopied);
                    if(dataTarget['_targetLoad'] == dataCurrentParent['_targetLoad']){
                        if(dataTarget['_targetLoad'] == targetLoadCourse){
                            var idCourse = dataTarget['id'];
                            var idLevel = dataCopied['id'];
                            copyLevel(idCourse,idLevel);
                        }else if(dataTarget['_targetLoad'] == targetLoadLevel){
                            var idLevel = dataTarget['id'];
                            var type = dataCopied['icon'];
                            if(type == "test"){
                                var idTest = dataCopied['id'];
                                currentParent = item;
                                copyTest(idLevel,idTest);
                            }else if(type == "obj"){
                                var idObj = dataCopied['id'];
                                currentParent = item;
                                copyObj(idLevel,idObj);
                            }
                        }else if(dataTarget['_targetLoad'] == targetLoadObj){
                            var idObj = dataTarget['id'];
                            var idLesson = dataCopied['id'];
                            currentParent = item;
                            copyLesson(idObj,idLesson);
                        }else if(dataTarget['_targetLoad'] == targetLoadLesson){
                            var idLesson = dataTarget['id'];
                            var idQuestion = dataCopied['id'];
                            currentParent = item;
                            copyQuestion (idLesson,idQuestion);
                        }else if(dataTarget['_targetLoad'] == targetLoadTest){
                            var idLesson = dataCurrentParent['_idLessonForTest'];
                            var idQuestion = dataCopied['id'];
                            currentParent = item;
                            copyQuestion (idLesson,idQuestion);
                        }
                    }else{
                        swal("",'could not paste the data copied to this node',"error");
                    }

                }, disabled : function(key, opt) {
                    if(nodeCopied == null) return true;
                    var dataCurrentParent = api.itemData(currentParent)
                    var temp = dataCurrentParent['_targetLoad'];
                    if(temp == null || typeof temp == 'undefined'){return true;}
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
    copyAndPaste();
    ClickOnTree();
    drag2drop();
    initTree();
    enablePublishBtn();
});
