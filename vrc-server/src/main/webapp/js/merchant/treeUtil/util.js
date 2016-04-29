/**
 * Created by lantb on 2016-03-29.
 */
var servletAdd = "/TreeAddNodeServlet";
var currentPopup;
var listWord=[];
var myObject = new Object();

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
 *
 * @param itemData
 */
function openPopup(itemData){
    currentPopup = $('#'+ itemData._popupId);
    currentPopup.find(".validateMsg").hide();
    currentPopup.find(".action").val(itemData._actionClick);
    currentPopup.find(".idHidden").val(itemData.id);
    if (itemData._actionClick == action_edit_course){
        currentPopup.find("#titlePopupCourse").html("course management");
        getCourseName().val(itemData.label);
        getCourseDescription().val(itemData._title);
        currentPopup.find("#btnDeleteLevel").show();
        currentPopup.find("#arrowCourse").html(nameOfCourse);
    }else
    if (itemData._actionClick == action_add_level){
        clearForm();
        currentPopup.find("#arrow").html(nameOfCourse);
        currentPopup.find("#titlePopup").html("add level");
        currentPopup.find("#btnDeleteLevel").hide();
    }else if(itemData._actionClick == action_edit_level){
        currentPopup.find("#titlePopup").html("level management");
        getLevelName().val(itemData.label);
        getLevelDescription().val(itemData._title);
        currentPopup.find("#btnDeleteLevel").show();
        currentPopup.find("#arrow").html(nameOfCourse + " > " + itemData.label);
    }else if(itemData._actionClick == action_add_obj){
        clearForm();
        var level = treeAPI.itemData(currentParent);
        getObjName().attr("idLevel", level.id);
        currentPopup.find("#titlePopupObj").html("add objective");
        currentPopup.find("#btnDeleteObj").hide();
        currentPopup.find("#arrowObj").html(nameOfCourse+" > " + level.label);
    }else if(itemData._actionClick == action_edit_obj){
        currentPopup.find("#titlePopupObj").html("objective management");
        getObjName().val(itemData.label);
        getObjDescription().val(itemData._title);
        var level = treeAPI.itemData(currentParent);
        getObjName().attr("idLevel", level.id);
        currentPopup.find("#btnDeleteObj").show();
        currentPopup.find("#arrowObj").html(nameOfCourse+" > " + level.label + " > " + itemData.label);
    }else if(itemData._actionClick == action_add_test){
        clearForm();
        var level = treeAPI.itemData(currentParent);
        getPercentPass().attr("idLevel",level.id);
        currentPopup.find("#titlePopupTest").html("add test");
        currentPopup.find("#btnDeleteTest").hide();
        currentPopup.find("#arrowTest").html(nameOfCourse + " > " + level.label);
    }else if(itemData._actionClick == action_edit_test){
        currentPopup.find("#titlePopupTest").html("test management");
        var percent = itemData._title.split("%")[0];
        getPercentPass().val(percent);
        var level = treeAPI.itemData(currentParent);
        getPercentPass().attr("idLevel",level.id);
        currentPopup.find("#btnDeleteTest").show();
        currentPopup.find("#arrowTest").html(nameOfCourse+">" + level.label + ">" + itemData.label);
    }else if(itemData._actionClick == action_add_lesson){
        clearForm();
        currentPopup.find("#titlePopupLesson").html("add lesson");
        currentPopup.find("#btnDeleteLesson").hide();
        var objParent = treeAPI.itemData(currentParent);
        getNameLesson().attr("objID",objParent.id);
        var level = treeAPI.parent(currentParent);
        var levelItemData = treeAPI.itemData(level);
        currentPopup.find("#arrowLesson").html(nameOfCourse + " > " + levelItemData.label +" > "+ objParent.label);
    }else if(itemData._actionClick == action_edit_lesson){
        clearForm();
        currentPopup.find("#titlePopupLesson").html("lesson management");
        getNameLesson().val(itemData.label);
        getDescriptionLesson().val(itemData._description);
        getTypeLesson().val(itemData._type);
        getDetailLesson().val(itemData._details);
        currentPopup.find("#btnDeleteLesson").show();
        var objParent = treeAPI.itemData(currentParent);
        getNameLesson().attr("objID",objParent.id);
        var level = treeAPI.parent(currentParent);
        var levelItemData = treeAPI.itemData(level);
        currentPopup.find("#arrowLesson").html(nameOfCourse + " > " + levelItemData.label +" > "+ objParent.label);
    }else if(itemData._actionClick == action_add_question){
        clearForm();
        listWord=[];
        getListWord().empty();
        currentPopup.find("#titlePopupQuestion").html("question management");
        currentPopup.find("#btnDeleteQuestion").hide();
        var lesson = treeAPI.itemData(currentParent);
        var obj = treeAPI.parent(currentParent);
        var objItemData = treeAPI.itemData(obj);
        var level = treeAPI.parent(obj);
        var levelItemData = treeAPI.itemData(level);
        currentPopup.find("#arrowQuestion").html(nameOfCourse + " > " + levelItemData.label +" > " + objItemData.label + " > "+ lesson.label);
    }else if(itemData._actionClick == action_edit_question){
        clearForm();
        listWord=[];
        currentPopup.find("#titlePopupQuestion").html("question management");
        var lesson = treeAPI.itemData(currentParent);
        var obj = treeAPI.parent(currentParent);
        var objItemData = treeAPI.itemData(obj);
        var level = treeAPI.parent(obj);
        var levelItemData = treeAPI.itemData(level);
        currentPopup.find("#btnDeleteQuestion").show();
        getQuestionListWordEdit().attr("idLesson",lesson.id);
        drawListWord(itemData._title);
        currentPopup.find("#arrowQuestion").html(nameOfCourse + " > " + levelItemData.label +" > " + objItemData.label + " > "+ lesson.label);
    }else if(itemData._actionClick == action_add_question_test){
        listWord=[];
        clearForm();
        getListWordTest().empty();
        currentPopup.find("#titlePopupTestWord").html("add test question");
        currentPopup.find("#btnDeleteTestWord").hide();
        var level = treeAPI.itemData(currentParent);
        var row= nameOfCourse +" > " + level.label;
        getExplanationTest().attr("row",row);
        currentPopup.find("#arrowQuestionTest").html(nameOfCourse + " > " + level.label);
    }else if(itemData._actionClick == action_edit_question_test){
        listWord=[];
        clearForm();
        var lesson = treeAPI.itemData(currentParent);
        getListWordTest().empty();
        drawListWord(itemData._title);
        getTypeTest().val(itemData._type);
        getExplanationTest().val(itemData._description)
        currentPopup.find("#btnDeleteTestWord").show();
        currentPopup.find("#titlePopupTestWord").html("test question management");
        getExplanationTest().attr("idLesson",lesson._idLessonForTest);
        var test = treeAPI.itemData(currentParent);
        var level = treeAPI.parent(currentParent);
        var levelItem = treeAPI.itemData(level);
        var row = nameOfCourse +" > "+levelItem.label+ " > " + test.label;
        getExplanationTest().attr("row",row);
        currentPopup.find("#arrowQuestionTest").html(nameOfCourse + " > " + levelItem.label);
    }

    currentPopup.modal('show');
}

/**
 *
 * @param listWord
 */
function drawListWord(listWord){
    var list=readListMail(listWord);
    getListWord().html("");
    getListWordTest().html();
    if(list!=null && list.length>0){
        for(var i=0;i<list.length;i++){
            if(currentPopup.find(".action").val() == action_edit_question){
                getListWord().append(' <div style="margin-top: 5px;" ><p id="word" style="display: inline;background-color: rgb(85, 142, 213);color: white; border-radius: 3px; padding: 2px 10px; vertical-align: middle;">'+list[i]+'</p><i class="fa fa-minus-circle fa-2x" style="color: red;padding-left: 10px;vertical-align: middle;" title="remove word"  id="idWord" ></i></div>');
            }else if(currentPopup.find(".action").val() == action_edit_question_test){
                getListWordTest().append(' <div style="margin-top: 5px;" ><p id="word" style="display: inline;background-color: rgb(85, 142, 213);color: white; border-radius: 3px; padding: 2px 10px; vertical-align: middle;">'+list[i]+'</p><i class="fa fa-minus-circle fa-2x" style="color: red;padding-left: 10px;vertical-align: middle;" title="remove word"  id="idWord" ></i></div>');
            }
            loadWeightForWordEdit(list[i]);
        }


    }

}

/**
 *
 * @param txt
 * @returns {*}
 */
function readListMail(txt) {
    if (txt == null || typeof txt == 'undefined' || txt.length == 0) return null;
    var data =  txt.split(',');
    var output = [];
    for (var i = 0; i < data.length; i++) {
        output.push(data[i]);
    }
    return output;
}

/**
 *
 * @param type
 */
function appendWord(type){
    if(type=="add"){
        if(listWord !=null && listWord.length>0){
            getListWord().html("");
            $.each(listWord, function(i){
                getListWord().append(' <div style="margin-top: 5px;">' +
                    '<p id="word" style="display: inline;background-color: rgb(85, 142, 213);' +
                    'color: white; border-radius: 3px; padding: 5px 10px; vertical-align: middle;">'
                    + listWord[i].nameWord + '</p>' +
                    '<i class="fa fa-minus-circle fa-2x" style="color: red;padding-left: 10px;' +
                    'vertical-align: middle;" ' +
                    'title="remove word"  id="idWord" ></i> </div>');
            });
        }
    }else if(type=="addWordTest") {
        if(listWord !=null && listWord.length>0){
            getListWordForTest().html("");
            $.each(listWord, function (i) {
                getListWordForTest().append(' <div style="margin-top: 5px;"><p id="word" style="display: inline;background-color: rgb(85, 142, 213);color: white; border-radius: 3px; padding: 5px 10px; vertical-align: middle;">' + listWord[i].nameWord + '</p><i class="fa fa-minus-circle fa-2x" style="color: red;padding-left: 10px;vertical-align: middle;" title="remove word"  id="idWord" ></i> </div>');
            });

        }
    }
}
/**
 *
 */
function saveWord(){
    $(document).on("click","#btnSaveWord",function() {
        var addOrEdit=$("#AddOrEditWord").val();
        var nameWord=$("#addWord").val();
        var idWord=$("#addWord").attr("idWord");
        if(validateWord()) {
            var listPhonemeName = getListPhonemes();
            var output = [];
            $(listPhonemeName).find('input').each(function (e) {
                var value = $(this).val();
                var ipa = $(this).attr("ipa");
                var index = $(this).attr("index");
                var weight = $("#weight" + index).val();
                output.push({
                    index: parseInt(index),
                    phoneme: value,
                    ipa: ipa,
                    weight: parseFloat(weight)
                });
            });
            pushToList(idWord,nameWord,output);
            appendWord(addOrEdit);
            $("#addWordModal").modal('hide');
        }
    });
}
/**
 *
 * @param idWord
 * @param word
 * @param listWeight
 */
function pushToList(idWord,word,listWeight){
    if(listWord !=null && listWord.length>0){
        var existed = false;
        var index_update = 0;
        $.each(listWord, function(i){
            if(listWord[i].nameWord === word) {
                existed = true;
                index_update = i;
            }
        });
        if(existed){
            listWord[index_update].listWeightPhoneme = listWeight;
        }else{
            listWord.push({
                idWord: idWord,
                nameWord: word,
                listWeightPhoneme: listWeight
            });
        }
    }else{
        listWord.push({
            idWord: idWord,
            nameWord: word,
            listWeightPhoneme: listWeight
        });
    }
}
/**
 *
 */
function showAddWord(){
    $(document).on("click","#btnAddWord",function() {
        getWordValidateMessage().hide();
        var idLesson= treeAPI.itemData(currentParent).id;
        $("#AddOrEditWord").val("add");
        $("#wordModal1").hide();
        $("#wordModal2").hide();
        $("#idLesson").val(idLesson);
        var row= $("#arrowQuestion").text() + " > question";
        $("#arrowWord").text(row);
        getAddWord().val("");
        getListPhonemes().html("");
        getListWeight().html("");
        getListIPA().html("");
        getPhonemeLable().html("");
        getWeightLable().html("");
        getIPAlable().html("");
        $("#yesadd").attr("disabled", true);
        $("#loadPhonemes").show();
        getAddWord().removeAttr('readonly');
        $("#loadPhonemes").attr("disabled",false);
        $("#addWord").attr("disabled",false);
        $("#addWordModal").attr("type","add-new-word");
        $("#addWordModal").modal('show');
        $("#addWordModal").find('#title-add-word').html("word management");
        $("#addWordModal").find('#btnSaveWord').attr("disabled", true);
    });
}

function showAddWordForTest(){
    $(document).on("click","#btnAddWordTest",function() {
        $("#validateWordMsg").hide();
        $("#arrowWord").html("Test > question" );
        var idLesson= treeAPI.itemData(currentParent)._idLessonForTest;
        $("#AddOrEditWord").val("addWordTest");
        $("#idLesson").val(idLesson);
        $("#wordModal1").hide();
        $("#wordModal2").hide();
        $("#addWordModal").modal('show');
        getAddWord().val("");
        getListPhonemes().html("");
        getListWeight().html("");
        getListIPA().html("");
        getPhonemeLable().html("");
        getWeightLable().html("");
        getIPAlable().html("");
        $("#yesadd").attr("disabled", true);
        $("#loadPhonemes").show();
        getAddWord().removeAttr('readonly');
        $("#loadPhonemes").attr("disabled",false);
        $("#addWord").attr("disabled",false);
        $("#addWordModal").find('#title-add-word').html("add test word");
        $("#addWordModal").find('#btnSaveWord').attr("disabled", true);
    });
}

/**
 *
 */
function openEditWords(){
    $(document).on("click","#word",function() {
        getListPhonemes().html("");
        getListWeight().html("");
        getListIPA().html("");
        if(currentPopup.find(".action").val() == action_add_question) {
            var word= $(this).closest("div").find('p').text();
            if(listWord !=null && listWord.length>0){
                $.each(listWord, function(i){
                    if(listWord[i].nameWord === word) {
                        var data=listWord[i];
                        $("#addWordModal").modal('show');
                        $("#addWordModal").find('#btnSaveWord').attr("disabled", false);
                        drawWord(data);
                    }
                });
            }
        }else if(currentPopup.find(".action").val() == action_add_question_test) {
            var word= $(this).closest("div").find('p').text();
            if(listWord !=null && listWord.length>0){
                $.each(listWord, function(i){
                    if(listWord[i].nameWord === word) {
                        var data=listWord[i];
                        $("#addWordModal").modal('show');
                        $("#addWordModal").find('#btnSaveWord').attr("disabled", false);
                        drawWord(data);
                    }
                });
            }
        }else if(currentPopup.find(".action").val() == action_edit_question_test) {
            var word= $(this).closest("div").find('p').text();
            if(listWord !=null && listWord.length>0){
                $.each(listWord, function(i){
                    if(listWord[i].nameWord === word) {
                        var data=listWord[i];
                        $("#addWordModal").modal('show');
                        $("#addWordModal").find('#title-add-word').html("test word management");
                        $("#addWordModal").find('#btnSaveWord').attr("disabled", false);
                        drawWord(data);
                    }
                });
            }else {
                /*loadWeightForWordEdit(word);*/
            }
        } else if(currentPopup.find(".action").val() == action_edit_question) {
            var word= $(this).closest("div").find('p').text();
            if(listWord !=null && listWord.length>0){
                $.each(listWord, function(i){
                    if(listWord[i].nameWord === word) {
                        var data=listWord[i];
                        $("#addWordModal").modal('show');
                        $("#addWordModal").find('#title-add-word').html("word management");
                        $("#addWordModal").find('#btnSaveWord').attr("disabled", false);
                        drawWord(data);
                    }
                });
            }else {
                /*loadWeightForWordEdit(word);*/
            }
        }
    });
}
/**
 *
 */
function removeWord(){
    $(document).on("click","#idWord",function() {
        if(currentPopup.find(".action").val() == action_add_question) {
            var word= $(this).closest("div").find('p').text();
            if(listWord !=null && listWord.length>0){
                $.each(listWord, function(i){
                    if(listWord[i].nameWord === word) {
                        listWord.splice(i,1);
                        console.log(listWord);
                        return false;
                    }
                });
            }

            $(this).closest("div").remove();
        }else if(currentPopup.find(".action").val() == action_add_question_test) {
            var word= $(this).closest("div").find('p').text();
            if(listWord !=null && listWord.length>0){
                $.each(listWord, function(i){
                    if(listWord[i].nameWord === word) {
                        listWord.splice(i,1);
                        console.log(listWord);
                        return false;
                    }
                });
            }
            $(this).closest("div").remove();
        }else if(currentPopup.find(".action").val() == action_edit_question_test) {
            var word= $(this).closest("div").find('p').text();
            if(listWord !=null && listWord.length>0){
                $.each(listWord, function(i){
                    if(listWord[i].nameWord === word) {
                        listWord.splice(i,1);
                        return false;
                    }
                });
                removeWords(word);
                $(this).closest("div").remove();
            }
        }else if(currentPopup.find(".action").val() == action_edit_question) {
            var word= $(this).closest("div").find('p').text();
            if(listWord !=null && listWord.length>0){
                $.each(listWord, function(i){
                    if(listWord[i].nameWord === word) {
                        listWord.splice(i,1);
                        return false;
                    }
                });
                removeWords(word);
                $(this).closest("div").remove();
            }
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
                    if(dropData._targetLoad == targetLoadQuestion){
                        //do not move the test node
                        return false;
                    }
                    if(dragData._targetLoad == targetLoadQuestion){
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
                            move = "up";
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
                        swalNew("",'could not paste the data copied to this node',"error");
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

$(document).ready(function(){
    removeWord();
    saveWord();
    showAddWord();
    showAddWordForTest();
    openEditWords();
});