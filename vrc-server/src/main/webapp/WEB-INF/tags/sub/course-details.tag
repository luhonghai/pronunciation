<%@ tag import="com.cmg.vrc.util.StringUtil" %>
<%@ tag import="com.cmg.merchant.common.Constant" %>
<%@ tag import="com.cmg.merchant.services.CourseServices" %>
<%@ tag import="com.cmg.merchant.util.SessionUtil" %>
<%@tag description="appDetail" pageEncoding="UTF-8" %>
<%@attribute name="pageTitle" required="true" %>
<%
    CourseServices cServices = new CourseServices();
    String company = (String) StringUtil.isNull(request.getSession().getAttribute(SessionUtil.ATT_CPNAME), "CMG");
    String idCourse = (String) StringUtil.isNull(request.getSession().getAttribute(SessionUtil.ATT_COURSE_ID), request.getParameter("idCourse"));
    String nameOfCourse = (String) StringUtil.isNull(cServices.getCourseName(idCourse), "");
    request.getSession().removeAttribute(SessionUtil.ATT_COURSE_ID);
%>

<script>
    var targetLoadCourse = "<%=Constant.TARGET_LOAD_COURSE%>";
    var targetLoadLevel = "<%=Constant.TARGET_LOAD_LEVEL%>";
    var targetLoadObj = "<%=Constant.TARGET_LOAD_OBJECTIVE%>";
    var targetLoadTest = "<%=Constant.TARGET_LOAD_TEST%>";
    var targetLoadLesson = "<%=Constant.TARGET_LOAD_LESSONS%>";
    var idCourse = "<%=idCourse%>";
    var nameOfCourse = "<%=nameOfCourse%>";
    var loadCourse = "<%=Constant.TARGET_LOAD_COURSE%>";
    var action_edit_course = "<%=Constant.ACTION_EDIT_COURSE%>";
    var action_delete_course = "<%=Constant.ACTION_DELETE_COURSE%>";
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
    var action_delete_word = "<%=Constant.ACTION_DELETE_WORD%>";

    var action_add_question_test = "<%=Constant.ACTION_ADD_QUESTION_TEST%>";
    var action_edit_question_test = "<%=Constant.ACTION_EDIT_QUESTION_TEST%>";
    var action_delete_question_test = "<%=Constant.ACTION_DELETE_QUESTION_TEST%>";
</script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/bower_components/aciTree/css/aciTree.css"
      media="all">
<link rel="stylesheet" type="text/css"
      href="<%=request.getContextPath() %>/bower_components/aciTree/css/jquery.contextMenu.css" media="all">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/tree.css" media="all">
<link rel="stylesheet" type="text/css"
      href="<%=request.getContextPath() %>/bower_components/AJAX_PROCESS_BAR/dist/css/jquery.progresstimer.min.css"
      media="all">
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
            <div id="process-bar" class="center-block" style="padding-top: 20px;width:400px"></div>
        </div>
    </div>
    <div class="row">
        <div class="col-lg-11">
            <img id="preview" title="preview course" style="float:right;cursor: pointer" src="/images/treeview/preview_button.gif" width="50px"
                 height="50px"/>
        </div>
        <div class="col-lg-1">
            <img title="publish course" id="publish" class="btn" style="float:right;padding:0px;cursor: pointer"
                 src="/images/treeview/publish_button.gif" width="50px" height="50px"/>
        </div>
    </div>
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
<script src="<%=request.getContextPath() %>/js/merchant/data/popupdata.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/validate/validateform.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/coursedetail/action.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/coursedetail/treeApi.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/treeUtil/util.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/coursedetail/ajax.js"></script>
<script src="<%=request.getContextPath() %>/bower_components/AJAX_PROCESS_BAR/dist/js/jquery.progresstimer.min.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/coursedetail/ui.js"></script>
<script src="<%=request.getContextPath() %>/js/merchant/copy/ajax.js"></script>


