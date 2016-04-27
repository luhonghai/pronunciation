/**
 * Created by lantb on 2016-03-29.
 */

function ClickOnTree(){
    $(document).on("click",".aciTreeItem",function(){
        var disabled = $(this).attr('disabled');
        if(disabled!="disabled"){
            treeAPI = theTree.aciTree('api');
            var item = $(this).closest("li");
            var itemData = treeAPI.itemData(item);
            currentParent = treeAPI.parent(item);
            var popupId = itemData._popupId;
            openPopup(itemData);
        }

    });
}


/**
 * drag&drop
 */
function drag2drop(){
    theTree.on('acitree', function(event, api, item, eventName, options) {
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
                    if(dropData._targetLoad == targetLoadTest){
                        //do not move the test node
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
    treeAPI = theTree.aciTree('api');
    treeAPI.unload(currentParent, {
        success: function(item) {
            this.close(item);
            treeAPI.ajaxLoad(currentParent, {
                success: function(item) {
                    this.open(item);
                    enablePublishBtn();
                    enableAddLevel();
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
                    var dataCurrentParent = api.itemData(currentParent);
                    var dataCopied = api.itemData(nodeCopied);
                    if(dataCopied._isButton){
                        return true;
                    }
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