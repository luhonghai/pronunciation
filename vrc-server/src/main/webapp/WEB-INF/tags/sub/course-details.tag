<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@ tag import="com.cmg.merchant.common.Constant" %>
<%@ tag import="com.cmg.merchant.services.CourseServices" %>
<%@ tag import="com.cmg.merchant.util.SessionUtil" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%
    CourseServices cServices = new CourseServices();
    String company = (String) StringUtil.isNull(request.getSession().getAttribute(SessionUtil.ATT_CPNAME), "CMG");
    String idCourse = (String) StringUtil.isNull(request.getSession().getAttribute(SessionUtil.ATT_COURSE_ID), "66b3510d-8964-47a0-8c33-72dc14f8dded");
    String nameOfCourse = (String) StringUtil.isNull(cServices.getCourseName(idCourse), "");
%>
<style>
    #listPhonmes input{
        margin: 5px 5px 5px 0;
    }

    #listIpa input{
        margin: 5px 5px 5px 0;
    }

    #listWeight input{
        margin: 0px 5px 5px 0;
    }

</style>

<script>
    var idCourse = "<%=idCourse%>";
    var nameOfCourse = "<%=nameOfCourse%>";
    var loadCourse = "<%=Constant.TARGET_LOAD_COURSE%>";
    var action_add_level = "<%=Constant.ACTION_ADD_LEVEL%>";
    var action_edit_level = "<%=Constant.ACTION_EDIT_LEVEL%>";
    var action_delete_level = "<%=Constant.ACTION_DELETE_LEVEL%>";
    var action_add_obj = "<%=Constant.ACTION_ADD_OBJ%>";
    var action_delete_obj = "<%=Constant.ACTION_DELETE_OBJ%>";
    var action_edit_obj = "<%=Constant.ACTION_EDIT_OBJ%>";
    var action_add_test = "<%=Constant.ACTION_ADD_TEST%>";
    var action_edit_test = "<%=Constant.ACTION_EDIT_TEST%>";
    var action_delete_test = "<%=Constant.ACTION_DELETE_TEST%>";
    var action_add_lesson = "<%=Constant.ACTION_ADD_LESSON%>";
    var action_edit_lesson = "<%=Constant.ACTION_EDIT_LESSON%>";
    var action_delete_lesson = "<%=Constant.ACTION_DELETE_LESSON%>";
    var action_add_question = "<%=Constant.ACTION_ADD_QUESTION%>";
    var action_edit_question = "<%=Constant.ACTION_EDIT_QUESTION%>";
    var action_delete_question = "<%=Constant.ACTION_DELETE_QUESTION%>";
</script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/bower_components/aciTree/css/aciTree.css"
      media="all">
<link rel="stylesheet" type="text/css"
      href="<%=request.getContextPath() %>/bower_components/aciTree/css/jquery.contextMenu.css" media="all">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/tree.css" media="all">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/coursedetail.css" media="all">
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h4 class="page-header header-company"><%=company%> > course administration > <%=nameOfCourse%>
            </h4>
        </div>
    </div>

    <!-- /.row -->
    <div class="row">
        <div class="col-lg-12">
            <div id="tree" class="aciTree">

            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-11">
            <img id="help" style="float:right;cursor: pointer" src="/images/treeview/preview_button.gif" width="50px" height="50px"/>
        </div>
        <div class="col-lg-1">
            <img id="publish" class="btn" style="float:right;padding:0px;cursor: pointer" src="/images/treeview/publish_button.gif" width="50px" height="50px"/>
        </div>
    </div>
</div>

<div id="popupLevel" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true" style="display: none;">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <label id="arrow" class="modal-title"
                    style="text-align: left;"><%=nameOfCourse%>
                </label>
                <h2 id='titlePopup' class="modal-title">Add Level</h2>
                <h4 id="validateLvMsg" class="modal-title validateMsg"
                    style="text-align: center;font-weight: 200;color:red;display:none;">Enter your level
                    name</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="addLevel" name="addform">
                    <div class="form-group">
                        <label class="control-label col-md-4 lbl_addForm" for="lvName">name:</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="lvName" name="name"
                                   placeholder="Level name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-4 lbl_addForm" for="lvDesc">description:</label>

                        <div class="col-md-8">
                            <textarea rows="2" class="form-control" id="lvDesc" name="description"
                                      placeholder="Level description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-5">
                            <input type="hidden" class="action">
                            <input type="hidden" class="idHidden">
                            <%--<input type="button" style="float:left;cursor: pointer" class="helpBtnPopUp">--%>
                            <img id="helpAddLevel" class="helpInfor" src="/images/popup/help_50_50.png" width="36px" height="36px"/>
                        </div>
                        <div class="col-md-2">
                            <img id="btnDeleteLevel" src="/images/popup/trash_50x50.gif" width="36px" height="36px"/>
                           <%-- <input type="button" id="btnDeleteLevel" style="float:left;cursor:pointer;display:none;" class="deleteBtnPopUp">--%>
                        </div>
                        <div class="col-md-5">
                            <img id="btnSaveLevel" style="float: right" src="/images/popup/Save_50x50.gif" width="36px" height="36px"/>
                            <%--<input type="button" id="btnSaveLevel" style="float:right;cursor:pointer" class="saveBtnPopUp">--%>
                        </div>
                    </div>
                </form>
            </div>
            <!-- End of Modal body -->
        </div>
        <!-- End of Modal content -->
    </div>
    <!-- End of Modal dialog -->
</div>


<div id="popupObjective" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true" style="display: none;">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <label id="arrowObj" class="modal-title"
                    style="text-align: left;"><%=nameOfCourse%>
                </label>
                <h2 id='titlePopupObj' class="modal-title">Add Objective</h2>
                <h4 id="validateObjMsg" class="modal-title validateMsg"
                    style="text-align: center;font-weight: 200;color:red;display:none;">Enter your level
                    name</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="addObj" name="addform">
                    <div class="form-group">
                        <label class="control-label col-md-4 lbl_addForm" for="objName">name:</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="objName" name="name"
                                   placeholder="Objective name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-4 lbl_addForm" for="objDesc">description:</label>

                        <div class="col-md-8">
                            <textarea rows="2" class="form-control" id="objDesc" name="description"
                                      placeholder="Objective description"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-5">
                            <input type="hidden" class="action">
                            <input type="hidden" class="idHidden">
                            <img id="helpAddObj" class="helpInfor" src="/images/popup/help_50_50.png" width="36px" height="36px"/>
                           <%-- <input type="button" id="btnHelpObj" style="float:left;cursor: pointer" class="helpBtnPopUp">--%>
                        </div>
                        <div class="col-md-2">
                            <img id="btnDeleteObj" src="/images/popup/trash_50x50.gif" width="36px" height="36px"/>
                            <%--<input type="button" id="btnDeleteObj" style="float:left;cursor:pointer;display:none;" class="deleteBtnPopUp">--%>
                        </div>
                        <div class="col-md-5">
                            <img id="btnSaveObj" style="float: right"  src="/images/popup/Save_50x50.gif" width="36px" height="36px"/>
                            <%--<input type="button" id="btnSaveObj"  style="float:right;cursor:pointer" class="saveBtnPopUp">--%>
                        </div>
                    </div>
                </form>
            </div>
            <!-- End of Modal body -->
        </div>
        <!-- End of Modal content -->
    </div>
    <!-- End of Modal dialog -->
</div>


<div id="popupTest" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true" style="display: none;">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <label id="arrowTest" class="modal-title"
                       style="text-align: left;"><%=nameOfCourse%>
                </label>
                <h2 id='titlePopupTest' class="modal-title">Add Test</h2>
                <h4 id="validateTestMsg" class="modal-title validateMsg"
                    style="text-align: center;font-weight: 200;color:red;display:none;">Enter your level
                    name</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="addTest" name="addform">
                    <div class="form-group">
                        <label class="control-label col-md-3 lbl_addForm" for="percent">pass %:</label>
                        <div class="col-md-8">
                            <input type="text" class="form-control" id="percent" name="percent"
                                   placeholder="Percent pass">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-5">
                            <input type="hidden" class="action">
                            <input type="hidden" class="idHidden">
                            <img id="helpAddTest" class="helpInfor" src="/images/popup/help_50_50.png" width="36px" height="36px"/>
                            <%--<input type="button" id="btnHelpTest" style="float:left;cursor: pointer" class="helpBtnPopUp">--%>
                        </div>
                        <div class="col-md-2">
                            <img id="btnDeleteTest" src="/images/popup/trash_50x50.gif" width="36px" height="36px"/>
                            <%--<input type="button" id="btnDeleteTest" style="float:left;cursor:pointer;display:none;" class="deleteBtnPopUp">--%>
                        </div>
                        <div class="col-md-5">
                            <img style="float: right" id="btnSaveTest" src="/images/popup/Save_50x50.gif" width="36px" height="36px"/>
                            <%--<input type="button" id="btnSaveTest"  style="float:right;cursor:pointer" class="saveBtnPopUp">--%>
                        </div>
                    </div>
                </form>
            </div>
            <!-- End of Modal body -->
        </div>
        <!-- End of Modal content -->
    </div>
    <!-- End of Modal dialog -->
</div>


<div id="popupLesson" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true" style="display: none;">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <label id="arrowLesson" class="modal-title"
                       style="text-align: left; padding-left: 15px;"><%=nameOfCourse%>
                </label>
                <h2 id='titlePopupLesson' class="modal-title">add lesson</h2>
                <h4 id="validateLessonMsg" class="modal-title validateMsg"
                    style="text-align: center;font-weight: 200;color:red;display:none;">Enter your lesson
                    name</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="addLesson" name="addform">
                    <div class="form-group">
                        <label class="control-label col-md-4 lbl_addForm">name:</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="lessonName" name="lessonName"
                                   placeholder="Lesson name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-md-4 lbl_addForm">description:</label>

                        <div class="col-md-8">
                            <textarea rows="2" class="form-control" id="lessonDesc" name="description"
                                      placeholder="Lesson description"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-md-4 lbl_addForm">type:</label>

                        <div class="col-md-8">
                            <input type="text" class="form-control" id="lessonType" name="type"
                                   placeholder="type">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="control-label col-md-2 lbl_addForm">lesson details:</label>
                        <img src="/images/popup/p_menu_lesson.png" class="control col-md-2" style="width:50px;height: 50px;padding-left: 0px;padding-right: 0px;margin-left: 12px;">
                        <div class="col-md-8">
                            <textarea rows="3" class="form-control" id="lessonDetail" name="details"
                                      placeholder="Lesson details"></textarea>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-5">
                            <input type="hidden" class="action">
                            <input type="hidden" class="idHidden">
                            <img id="helpAddLesson" class="helpInfor" src="/images/popup/help_50_50.png" width="36px" height="36px"/>
                            <%--<input type="button" id="btnHelpTest" style="float:left;cursor: pointer" class="helpBtnPopUp">--%>
                        </div>
                        <div class="col-md-2">
                            <img id="btnDeleteLesson" src="/images/popup/trash_50x50.gif" width="36px" height="36px"/>
                            <%--<input type="button" id="btnDeleteTest" style="float:left;cursor:pointer;display:none;" class="deleteBtnPopUp">--%>
                        </div>
                        <div class="col-md-5">
                            <img style="float: right" id="btnSaveLesson" src="/images/popup/Save_50x50.gif" width="36px" height="36px"/>
                            <%--<input type="button" id="btnSaveTest"  style="float:right;cursor:pointer" class="saveBtnPopUp">--%>
                        </div>
                    </div>
                </form>
            </div>
            <!-- End of Modal body -->
        </div>
        <!-- End of Modal content -->
    </div>
    <!-- End of Modal dialog -->
</div>


<div id="popupQuestion" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true" style="display: none;">
    <div class="modal-dialog">
        <div class="modal-content modal-question-word">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <label id="arrowQuestion" class="modal-title"
                       style="text-align: left;"><%=nameOfCourse%>
                </label>
                <h2 align="center" id='titlePopupQuestion' class="modal-title">question management</h2>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="addQuestion" name="addform">
                    <div class="form-group">
                        <label class="control-label">Please add the words that you want to be used for this question. If you add more than one word they will be randomised to give variety to the student.</label>
                    </div>
                    <div class="form-group">
                        <a id="btnAddWord" style="background-color: orange;color: white;border-radius: 3px; padding: 5px 5px; text-decoration: none;">
                            <img src="/images/teacher/invite_students_48x48.gif" style="width: 24px;height: 24px;"> add word <i class="fa fa-plus"></i>
                        </a>
                        <div id="listWord">

                        </div>

                    </div>
                    <div class="form-group">
                        <div class="col-md-5" style="padding-left: 0px;">
                            <input type="hidden" class="action">
                            <input type="hidden" class="idHidden">
                            <img id="helpAddQuestion" class="helpInfor" src="/images/popup/help_50_50.png" width="36px" height="36px"/>
                            <%--<input type="button" id="btnHelpTest" style="float:left;cursor: pointer" class="helpBtnPopUp">--%>
                        </div>
                        <div class="col-md-2">
                            <img id="btnDeleteQuestion" src="/images/popup/trash_50x50.gif" width="36px" height="36px"/>
                            <%--<input type="button" id="btnDeleteTest" style="float:left;cursor:pointer;display:none;" class="deleteBtnPopUp">--%>
                        </div>
                        <div class="col-md-5" style="padding-right: 0px;">
                            <img style="float: right" id="btnSaveQuestion" src="/images/popup/Save_50x50.gif" width="36px" height="36px"/>
                            <%--<input type="button" id="btnSaveTest"  style="float:right;cursor:pointer" class="saveBtnPopUp">--%>
                        </div>
                    </div>
                </form>
            </div>
            <!-- End of Modal body -->
        </div>
        <!-- End of Modal content -->
    </div>
    <!-- End of Modal dialog -->
</div>



<div id="addWordModal" class="modal fade" aria-hidden="true" style="display: none;">
    <div class="modal-dialog">
        <div class="modal-content modal-question-word">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <div class="row">
                    <div class="col-xs-10 col-xs-offset-1">
                        <label id="arrowWord" class="modal-title"></label>
                        <h2 align="center">add word</h2>
                        <h4 id="validateWordMsg" class="modal-title validateMsg"
                            style="text-align: center;font-weight: 200;color:red;display:none;">Enter your word</h4>
                        <form name="add" class="form-horizontal" id="addWords">
                            <div class="form-group">
                                <div class="row">
                                    <label class="control-label">Please add the words that you want to be used for this question. If you add more than one word they will be randomised to give variety to the student.
                                    </label>
                                </div>

                                <div class="row">
                                    <div class="col-xs-3  col-sm-2">
                                        <div class="row"><label class="control-label">Word:</label></div>
                                    </div>
                                    <div class="col-xs-5  col-sm-6">
                                        <div class="row"><input  type="text" id="addWord" name="addWord" class=" form-control"></div>
                                    </div>
                                    <div class="col-xs-4  col-sm-4">
                                        <div class="row"><button type="button" name="loadPhonemes" id="loadPhonemes" class="btn btn-default" style="background-color: lightgreen;" value="yes" >Load Phonemes</button></div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-xs-3 col-sm-2">
                                        <div class="row"><label class="control-label phoneme-lable"></label></div>
                                        <div class="row"><label class="control-label ipa-lable"></label></div>
                                        <div class="row"><label class="control-label weight-lable"></label></div>
                                    </div>
                                    <div class="col-xs-9 col-sm-10 group-phoneme-weight">
                                        <div class="row" id="listPhonmes"></div>
                                        <div class="row" id="listIpa"></div>
                                        <div class="row" id="listWeight"></div>
                                    </div>
                                </div>

                            </div>
                            <div class="form-group">
                                <div class="col-md-6" style="padding-left: 0px;">
                                    <input type="hidden" class="action">
                                    <input type="hidden" class="idHidden">
                                    <img id="helpAddWord" src="/images/popup/help_50_50.png" width="36px" height="36px"/>
                                    <%--<input type="button" id="btnHelpTest" style="float:left;cursor: pointer" class="helpBtnPopUp">--%>
                                </div>

                                <div class="col-md-6" style="padding-right: 0px;">
                                    <img style="float: right" id="btnSaveWord" src="/images/popup/Save_50x50.gif" width="36px" height="36px"/>
                                    <%--<input type="button" id="btnSaveTest"  style="float:right;cursor:pointer" class="saveBtnPopUp">--%>
                                </div>
                            </div>

                        </form>
                    </div>
                </div>
            </div>
            <!-- End of Modal body -->
        </div>
        <!-- End of Modal content -->
    </div>
    <!-- End of Modal dialog -->
</div>

<div id="help-popup" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"
     style="display: none;color:#957F7F">
    <div class="modal-dialog" style="width:500px">
        <div class="modal-content">
            <div class="modal-header" style="border-bottom: transparent;padding-bottom: 0px;text-align: center">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h2 class="modal-title" style="font-weight: 700;">add course</h2>
            </div>
            <div class="modal-body">

            </div>
            <!-- End of Modal body -->
        </div>
        <!-- End of Modal content -->
    </div>
    <!-- End of Modal dialog -->
</div>



<script src="<%=request.getContextPath() %>/bower_components/aciTree/js/jquery.aciPlugin.min.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/aciTree/js/jquery.aciSortable.min.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/aciTree/js/jquery.aciTree.dom.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/aciTree/js/jquery.aciTree.core.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/aciTree/js/jquery.aciTree.utils.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/aciTree/js/jquery.aciTree.selectable.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/aciTree/js/jquery.aciTree.sortable.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/aciTree/js/jquery.contextMenu.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/coursedetail/data.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/coursedetail/validateform.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/coursedetail/action.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/coursedetail/treeApi.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/coursedetail/ajax.js"></script>

